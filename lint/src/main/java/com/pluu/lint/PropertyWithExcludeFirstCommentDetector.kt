package com.pluu.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.*
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.allChildren
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UField
import org.jetbrains.uast.asRecursiveLogString
import org.jetbrains.uast.getUastParentOfType
import java.util.*

class PropertyWithExcludeFirstCommentDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes() = listOf(UClass::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler {
        val isKotlin = isKotlin(context.psiFile)
        return object : UElementHandler() {

            override fun visitClass(node: UClass) {
                // 테스트 패키지내에서만 동작
                if (classPackageName(node)?.endsWith("exclude_first_comment") == false) return

                // Target Annotation 체크
                // 활성화 여부 체크
                val attribute = node.findAnnotation(TARGET_ANNOTATION)
                    ?.findAttributeValue(ATTR_VALUE) ?: return
                val isEnable = ConstantEvaluator.evaluate(context, attribute) as? Boolean ?: false

                if (!isEnable) return

                context.report(ISSUE, context.getNameLocation(node), node.asRecursiveLogString())
                if (node.hasAnnotation(TARGET_ANNOTATION)) {
                    val parent = node.getUastParentOfType(UClass::class.java, false)!!
                    if (parent.hasAnnotation(TARGET_ANNOTATION)) {
                        for (field in parent.fields) {
                            if (isKotlin) {
                                findFieldInKotlin(field)
                            } else {
                                findFieldInJava(field)
                            }
                        }
                    }
                }
            }

            private fun findFieldInJava(field: UField) {
                val psi = field.javaPsi as? PsiField ?: return
                val excludeOffset = psi.modifierList?.startOffsetInParent ?: 0
                report(psi, excludeOffset)
            }

            private fun findFieldInKotlin(field: UField) {
                val property = (field.originalElement as? KtProperty) ?: return

                val excludeOffset = property.allChildren.firstOrNull {
                    it !is PsiComment && it !is PsiWhiteSpace
                }?.startOffsetInParent ?: 0

                report(property, excludeOffset)
            }

            private fun report(psiElement: PsiElement, excludeOffset: Int) {
                context.report(
                    ISSUE,
                    context.getRangeLocation(
                        psiElement,
                        excludeOffset,
                        psiElement.textLength - excludeOffset
                    ),
                    """
                        Original Element : ${psiElement.originalElement}
                        Detect Property&Filed in ${if (isKotlin) "Kotlin" else "Java"}
                    """.trimIndent()
                )
            }
        }
    }

    private fun classPackageName(node: UClass): String? =
        (node.containingFile as? PsiJavaFile)?.packageName

    companion object {
        val TARGET_ANNOTATION = "com.pluu.lintstudy.SampleAnnotation"
        val ATTR_VALUE = "isEnabled"

        val ISSUE = Issue.create(
            id = "SampleDetector",
            briefDescription = "Update",
            explanation = "....",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                PropertyWithExcludeFirstCommentDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}
