package com.pluu.lint

import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.android.tools.lint.detector.api.isKotlin
import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiMethod
import org.jetbrains.kotlin.utils.addToStdlib.firstIsInstanceOrNull
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.ULambdaExpression
import java.util.EnumSet

class LiveDataObserveNotNullDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String> {
        return listOf("observeNotNull")
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        super.visitMethodCall(context, node, method)
        if (!isKotlin(context.psiFile)) return
        if (!isLiveDataReceiver(context, node)) return

        val lambda = node.valueArguments.firstIsInstanceOrNull<ULambdaExpression>()
            ?.valueParameters ?: return
        if (lambda.size != 1) return
        val firstParamType = lambda.first().type
        if (firstParamType.canonicalText == "java.lang.Void") {
            report(context, node)
        }
    }

    private fun isLiveDataReceiver(context: JavaContext, node: UCallExpression): Boolean {
        val psiClassType = (node.receiver?.getExpressionType() as? PsiClassType) ?: return false
        return context.evaluator.extendsClass(
            psiClassType.rawType().resolve(),
            "androidx.lifecycle.LiveData",
            false
        )
    }

    private fun report(context: JavaContext, node: UCallExpression) {
        // Lint 7.0 이상부터 사용
        val incident = Incident(context, ISSUE)
            .message(message)
            .at(node.methodIdentifier!!)
            .fix(
                fix()
                    .name("Use observe function")
                    .replace()
                    .text(node.methodName)
                    .with("observe")
                    .reformat(true)
                    .build()
            )
        context.report(incident)
    }

    companion object {
        private const val message = "LiveData<Nothing>에는 observeNotNull을 사용할 수 없습니다."

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