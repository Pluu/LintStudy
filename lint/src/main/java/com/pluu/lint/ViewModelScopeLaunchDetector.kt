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
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.tryResolveNamed
import java.util.EnumSet

class ViewModelScopeLaunchDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodIdentifier?.name != "launch") return
            if (node.methodPackageName() == "kotlinx.coroutines.launch") return
            if (node.receiverType?.canonicalText != "kotlinx.coroutines.CoroutineScope") return
            if (node.receiver?.tryResolveNamed()?.name != "getViewModelScope") return

            // Lint 7.0 이상부터 사용
            val incident = Incident(context, ISSUE)
                .message(message)
                .at(node.methodIdentifier!!)
            context.report(incident)

            // Lint 7.0 미만
//            val element = node.methodIdentifier ?: return
//            context.report(ISSUE, element, context.getLocation(element), message)
        }
    }

    companion object {
        private const val message = "Detect ViewModelScope launch"

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
    }
}