package com.pluu.lint

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiParameter
import org.jetbrains.uast.*
import java.util.*

@Suppress("UnstableApiUsage")
class PreferencesKeyDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames() = listOf(
        *PREFERENCES_METHODS.toTypedArray(),
        *SHARED_PREFERENCES_METHODS.toTypedArray(),
    ).distinct()

    override fun visitMethodCall(
        context: JavaContext,
        node: UCallExpression,
        method: PsiMethod
    ) {
        if (detectMethod(context, method)) {
            val keyParam = node.valueArguments.first()

            // 0. Check literal value
            val literal = keyParam.literal()
            if (literal != null && !keyRegex.matches(literal)) {
                report(context, node, "실제값이 \'KEY_[TYPE]_\'형태로 시작해야함")
                return
            }

            // 1. Get name
            val identifier = keyParam.identifierWithoutParameter() ?: return

            // 2. Check, Reference Name
            if (!keyRegex.matches(identifier)) {
                // identifier matching
                report(context, node, "변수명이 \'KEY_[TYPE]_\'형태로 시작해야함")
                return
            }

            // 3. Check, Real value key
            val value = ConstantEvaluator.evaluate(context, keyParam) as? String ?: return
            if (!keyRegex.matches(value)) {
                report(context, node, "실제값이 \'KEY_[TYPE]_\'형태로 시작해야함")
            }
        }
    }

    // Preferences/SharedPreferences 에서 호출되었는지 체크
    private fun detectMethod(
        context: JavaContext,
        method: PsiMethod
    ): Boolean {
        return context.evaluator.isMemberInClass(method, PREFERENCES_CLS) or
                context.evaluator.isMemberInClass(method, SHARED_PREFERENCES_CLS)
    }

    private fun UExpression.literal(): String? = when (this) {
        is ULiteralExpression,
        is UPolyadicExpression -> evaluateString()
        else -> null
    }

    private fun UExpression.identifierWithoutParameter(): String? {
        val element = tryResolve() ?: return null
        if (element is PsiParameter) {
            return null
        }

        return when (element) {
            is PsiNamedElement -> element.name
            else -> null
        }
    }

    private fun report(
        context: JavaContext,
        node: UCallExpression,
        message: String
    ) {
        // Lint 7.0 이상부터 사용
        val incident = Incident(context, ISSUE)
            .message(message)
            .at(node.methodIdentifier!!)
        context.report(incident)

        // Lint 7.0 미만
//            val element = node.sourcePsiElement ?: return
//            context.report(ISSUE, element, context.getLocation(element), message)
    }

    companion object {
        private const val PREFERENCES_CLS = "java.util.prefs.Preferences"
        private const val SHARED_PREFERENCES_CLS = "android.content.SharedPreferences.Editor"

        private val keyRegex = "^KEY_(INT|BOOLEAN|FLOAT|BYTE|STRING)_.+".toRegex()

        private val PREFERENCES_METHODS = listOf(
            "put",
            "putInt",
            "putBoolean",
            "putFloat",
            "putDouble",
            "putByteArray",
            "putLong",
            "get",
            "getInt",
            "getBoolean",
            "getFloat",
            "getDouble",
            "getByteArray",
            "getLong",
        )

        private val SHARED_PREFERENCES_METHODS = listOf(
            "putBoolean",
            "putLong",
            "putFloat",
            "putInt",
            "putString",
            "putStringSet",
            "getBoolean",
            "getLong",
            "getFloat",
            "getInt",
            "getString",
            "getStringSet",
        )

        private val message = "Preferences Key/Value 정의가 올바르지 않음"

        @JvmField
        val ISSUE = Issue.create(
            id = "PreferencesKeyDetector",
            briefDescription = "Update",
            explanation = message,
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.WARNING,
            implementation = Implementation(
                PreferencesKeyDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}