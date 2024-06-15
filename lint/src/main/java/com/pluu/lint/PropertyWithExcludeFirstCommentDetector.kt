package com.pluu.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.ConstantEvaluator
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.pluu.lint.util.classPackageName
import com.pluu.lint.util.isKotlin
import org.jetbrains.kotlin.psi.KtProperty
import org.jetbrains.kotlin.psi.psiUtil.allChildren
import org.jetbrains.uast.UClass
import java.util.EnumSet

class PropertyWithExcludeFirstCommentDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes() = listOf(UClass::class.java)

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {

        private val isKotlinFile = isKotlin(context.psiFile)

        override fun visitClass(node: UClass) {
            // 테스트 패키지내에서만 동작
            if (node.classPackageName?.endsWith("exclude_first_comment") == false) return

            // Target Annotation 체크
            // 활성화 여부 체크
            val attribute = node.findAnnotation(TARGET_ANNOTATION)
                ?.findAttributeValue(ATTR_VALUE) ?: return
            val isEnable = ConstantEvaluator.evaluate(context, attribute) as? Boolean ?: false

            if (!isEnable) return

            // Debugging
//            context.report(ISSUE, context.getNameLocation(node), node.asRecursiveLogString())

            for (field in node.fields) {
                if (isKotlinFile) {
                    val property = (field.originalElement as? KtProperty) ?: continue
                    findField(property)
                } else {
                    findField(field)
                }
            }
        }

        private fun findField(field: PsiElement) {
            val excludeOffset = field.allChildren.firstOrNull {
                it !is PsiComment && it !is PsiWhiteSpace
            }?.startOffsetInParent ?: 0

            report(field, excludeOffset)
        }

        private fun report(element: PsiElement, excludeOffset: Int) {
            // Lint 7.0 이상부터 사용
            Incident(context, ISSUE)
                .message("found inner type")
                .scope(element)
                .location(
                    context.getRangeLocation(
                        element,
                        excludeOffset,
                        element.textLength - excludeOffset
                    )
                )
                .report(context)

            // Lint 7.0 미만
//            context.report(
//                ISSUE,
//                context.getRangeLocation(
//                    element,
//                    excludeOffset,
//                    element.textLength - excludeOffset
//                ),
//                "found inner type"
                // Debug Message
//                """
//                        Original Element : ${element.originalElement}
//                        Detect Property&Filed in ${if (isKotlin) "Kotlin" else "Java"}
//                    """.trimIndent()
//            )
        }
    }

    companion object {
        private const val TARGET_ANNOTATION = "com.pluu.lintstudy.SampleAnnotation"
        private const val ATTR_VALUE = "isEnabled"

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
