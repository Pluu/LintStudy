package com.pluu.lint

import com.android.SdkConstants.AUTO_URI
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.LayoutDetector
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.XmlContext
import org.w3c.dom.Element


class RequiredCustomViewAttributeDetector : LayoutDetector() {

    override fun getApplicableElements() = listOf(
        "com.pluu.lintstudy.MyView"
    )

    override fun getApplicableAttributes() = listOf(
        DETECT_ATTRIBUTE
    )

    override fun visitElement(context: XmlContext, element: Element) {
        if (!element.hasAttributeNS(AUTO_URI, DETECT_ATTRIBUTE)) {
            val fix = fix().set()
                .todo(AUTO_URI, DETECT_ATTRIBUTE)
                .build()

            // Lint 7.0 이상부터 사용
            Incident(context, ISSUE)
                .message(message)
                .fix(fix)
                .scope(element)
                .location(context.getNameLocation(element))
                .report(context)
        }
    }

    companion object {
        private const val DETECT_ATTRIBUTE = "exampleDrawable"

        private const val message = "$DETECT_ATTRIBUTE 속성이 필수인 케이스"

        @JvmField
        val ISSUE = Issue.create(
            id = "RequiredCustomViewAttributeDetector",
            briefDescription = "Update",
            explanation = message,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                RequiredCustomViewAttributeDetector::class.java,
                Scope.RESOURCE_FILE_SCOPE
            )
        )
    }
}