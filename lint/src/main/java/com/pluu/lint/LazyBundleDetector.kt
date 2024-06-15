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
import com.intellij.psi.PsiClassType
import com.pluu.lint.util.isKotlin
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UExpression
import org.jetbrains.uast.UField
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USimpleNameReferenceExpression
import java.util.EnumSet

class LazyBundleDetector : Detector(), SourceCodeScanner {

    private val lazyDefined = mutableSetOf<String>()

    override fun applicableSuperClasses(): List<String> = listOf(
        "android.app.Activity",
        "androidx.fragment.app.Fragment"
    )

    override fun visitClass(context: JavaContext, declaration: UClass) {
        super.visitClass(context, declaration)
        if (!isKotlin(declaration)) return
        for (field in declaration.fields) {
            checkField(context, field)
        }
    }

    private fun checkField(context: JavaContext, field: UField) {
        if (findLazyType(field)) {
            lazyDefined.add(field.nameIdentifier.text)
        } else {
            val initializer = field.uastInitializer ?: return
            val matchName = findIdentifier(initializer) ?: return
            if (lazyDefined.contains(matchName)) {
                val incident = Incident(context, ISSUE)
                    .message(message)
                    .at(initializer)
                context.report(incident)
            }
        }
    }

    private fun findLazyType(node: UField): Boolean {
        val rawType = (node.type as? PsiClassType)?.rawType() ?: return false
        return rawType.canonicalText == "com.pluu.lintstudy.lazybundle.LazyProvider"
    }

    private fun findIdentifier(expression: UExpression): String? {
        return when (expression) {
            is USimpleNameReferenceExpression -> expression.identifier
            is UQualifiedReferenceExpression -> {
                (expression.receiver as? USimpleNameReferenceExpression)?.identifier
            }

            else -> null
        }
    }

    companion object {
        private const val message = "Lazy한 값을 클래스 인스턴스화 시점에 사용하면 안됨"

        @JvmField
        val ISSUE = Issue.create(
            id = "LazyDetector",
            briefDescription = "LazyDetector",
            explanation = message,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                LazyBundleDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}