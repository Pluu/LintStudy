package com.pluu.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression
import java.util.*

@Suppress("UnstableApiUsage")
class FindMethodWithArgumentDetector : Detector(), SourceCodeScanner {
    override fun getApplicableMethodNames() = listOf(DETECT_METHOD_NAME)

    override fun visitMethodCall(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        if (detectMethod(node)) {
            // Lint 7.0 이상부터 사용
            val incident = Incident(context, ISSUE)
                .message(message)
                .at(node.methodIdentifier!!)
            context.report(incident)

            // Lint 7.0 미만
//            val element = node.sourcePsiElement ?: return
//            context.report(ISSUE, element, context.getLocation(element), message)
        }
    }

    private fun detectMethod(node: UCallExpression): Boolean {
        if (node.valueArgumentCount != 1) {
            return false
        }
        val argument = node.valueArguments.first().evaluate() as Int
        return argument == 1
    }

    companion object {
        private const val DETECT_METHOD_NAME = "indexOf"

        private val message = "특정 메소드& 특정 값을 사용하는 케이스"

        @JvmField
        val ISSUE = Issue.create(
            id = "FindMethodWithArgumentDetector",
            briefDescription = "Update",
            explanation = message,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                FindMethodWithArgumentDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}