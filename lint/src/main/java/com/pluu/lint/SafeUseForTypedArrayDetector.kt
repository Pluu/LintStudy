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
import java.util.EnumSet

class SafeUseForTypedArrayDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitCallExpression(node: UCallExpression) {
            if (node.methodName != "use") return
            if (node.methodPackageName() == "androidx.core.content.res") return
            if (node.receiverType?.canonicalText != "android.content.res.TypedArray") return

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
        private const val message = "AndroidX core의 use extension을 사용해주세요."

        @JvmField
        val ISSUE = Issue.create(
            id = "SafeUseForTypedArrayDetector",
            briefDescription = "TypedArray",
            explanation = "Use safe 'use' extension for TypedArray",
            moreInfo = "https://github.com/android/android-ktx/issues/193",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                SafeUseForTypedArrayDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}