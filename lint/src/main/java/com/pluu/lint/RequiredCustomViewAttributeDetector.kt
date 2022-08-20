package com.pluu.lint

import com.android.SdkConstants.AUTO_URI
import com.android.tools.lint.detector.api.*
import org.w3c.dom.Element


@Suppress("UnstableApiUsage")
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

        private val message = "$DETECT_ATTRIBUTE 속성이 필수인 케이스"

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