package com.pluu.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import org.jetbrains.uast.UCallExpression
import java.util.*

@Suppress("UnstableApiUsage")
class LiveDataObserveNotNullDetector : Detector(), SourceCodeScanner {

    override fun getApplicableUastTypes() = listOf(
        UCallExpression::class.java
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitCallExpression(node: UCallExpression) {
            if (!isKotlin(context.psiFile)) return
            if (node.methodName != "observeNotNull") return
            // Find Nothing LiveData
            if (!nothingLiveDataCase.contains(node.receiverType?.canonicalText)) return

            // Lint 7.0 이상부터 사용
            val incident = Incident(context, ISSUE)
                .message(message)
                .at(node.methodIdentifier!!)
            context.report(incident)
        }
    }

    companion object {
        private val message = "LiveData<Nothing>에는 observeNotNull을 사용할 수 없습니다."

        // LiveData<Nothing> ==> androidx.lifecycle.LiveData
        // MutableLiveData<Nothing> ==> androidx.lifecycle.MutableLiveData
        private val nothingLiveDataCase = arrayOf(
            "androidx.lifecycle.LiveData",
            "androidx.lifecycle.MutableLiveData"
        )

        @JvmField
        val ISSUE = Issue.create(
            id = "LiveDataObserveNotNullDetector",
            briefDescription = "LiveData",
            explanation = "Cannot be used for LiveData<Nothing>",
            category = Category.USABILITY,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                LiveDataObserveNotNullDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}