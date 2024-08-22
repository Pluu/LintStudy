package com.pluu.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.pluu.lint.util.methodPackageName
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import org.jetbrains.uast.tryResolveNamed
import java.util.EnumSet

class ViewModelScopeLaunchDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitCallExpression(node: UCallExpression) {
            if (node.receiverType?.canonicalText != "kotlinx.coroutines.CoroutineScope") return
            if (node.receiver?.tryResolveNamed()?.name != "getViewModelScope") return

            val methodName = node.methodIdentifier?.name ?: return
            when (methodName) {
                "launch" -> handleLaunch(context, node)
                "launchLoading" -> handleLaunchLoading(context, node)
            }
        }
    }

    private fun handleLaunch(context: JavaContext, node: UCallExpression) {
        if (node.methodPackageName() == "kotlinx.coroutines.launch") return

        // Lint 7.0 이상부터 사용
        val incident = Incident(context, ISSUE)
            .message(message)
            .at(node.methodIdentifier!!)
        context.report(incident)

        // Lint 7.0 미만
//            val element = node.methodIdentifier ?: return
//            context.report(ISSUE, element, context.getLocation(element), message)
    }

    private fun handleLaunchLoading(context: JavaContext, node: UCallExpression) {
        if (node.valueArgumentCount == 1) {
            val incident = Incident(context, CEH_ISSUE)
                .message(ceh_message)
                .at(node.methodIdentifier!!)
            context.report(incident)
            return
        }

        val identifier = node.valueArguments.firstIsInstanceOrNull<USimpleNameReferenceExpression>()
            ?.getExpressionType()?.canonicalText ?: return
        if (identifier == "kotlin.coroutines.EmptyCoroutineContext") {
            val incident = Incident(context, CEH_ISSUE)
                .message(empty_coroutine_context_message)
                .at(node.methodIdentifier!!)
            context.report(incident)
        }
    }

    companion object {
        private const val message = "Detect ViewModelScope launch"

        private const val ceh_message = "Detect CEH not param"

        private const val empty_coroutine_context_message = "Detect EmptyCoroutineContext"

        val ISSUE = Issue.create(
            id = "ViewModelScopeLaunchDetector",
            briefDescription = message,
            explanation = "....",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                ViewModelScopeLaunchDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )

        val CEH_ISSUE = Issue.create(
            id = "ViewModelScopeLaunchDetector",
            briefDescription = ceh_message,
            explanation = "....",
            category = Category.CORRECTNESS,
            priority = 3,
            severity = Severity.ERROR,
            implementation = Implementation(
                ViewModelScopeLaunchDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}