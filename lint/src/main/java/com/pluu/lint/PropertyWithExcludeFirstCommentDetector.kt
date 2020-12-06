package com.pluu.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.asJava.elements.KtLightField
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.allChildren
import org.jetbrains.uast.*
import java.util.*

class PropertyWithExcludeFirstCommentDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes() =
        listOf<Class<out UElement>>(UClass::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler {
        val isKotlin = isKotlin(context.psiFile)
        return object : UElementHandler() {

            override fun visitClass(node: UClass) {
                if (classPackageName(node) != "com.pluu.lintstudy.exclude_first_comment") return

                context.report(ISSUE, context.getNameLocation(node), node.asRecursiveLogString())
                if (node.hasAnnotation(targetAnnotation)) {
                    val parent = node.getUastParentOfType(UClass::class.java, false)!!
                    if (parent.hasAnnotation(targetAnnotation)) {
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
                val startPosition = field.modifierList?.startOffsetInParent ?: 0
                val element: UElement = field as? UElement ?: return

                context.report(
                    ISSUE,
                    context.getRangeLocation(
                        element,
                        startPosition,
                        field.textLength - startPosition
                    ),
                    ">>> Java"
                )
            }

            private fun findFieldInKotlin(field: UField) {
                val property = (field.javaPsi as? KtLightField)?.kotlinOrigin as? KtProperty
                    ?: return

                val firstPosition = property.allChildren.firstOrNull {
                    it !is PsiComment && it !is PsiWhiteSpace
                }?.startOffsetInParent ?: 0

                context.report(
                    ISSUE,
                    context.getRangeLocation(
                        property,
                        firstPosition,
                        property.textLength - firstPosition
                    ),
                    ">>> Kotlin"
                )
            }
        }
    }

    private fun classPackageName(node: UClass): String? =
        (node.containingFile as? PsiJavaFile)?.packageName

    companion object {
        val targetAnnotation = "com.pluu.lintstudy.SampleAnnotation"

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
