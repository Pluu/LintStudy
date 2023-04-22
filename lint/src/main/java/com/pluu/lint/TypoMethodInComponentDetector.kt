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
import org.jetbrains.uast.UClass
import org.jetbrains.uast.UMethod
import java.util.EnumSet

class TypoMethodInComponentDetector : Detector(), SourceCodeScanner {

    override fun applicableSuperClasses(): List<String> = listOf(
        "android.app.Activity",
        "androidx.fragment.app.Fragment",
        "android.view.View"
    )

    override fun visitClass(context: JavaContext, declaration: UClass) {
        super.visitClass(context, declaration)

        declaration.methods.asSequence()
            .forEach { method ->
                val methodName = method.name

                val findSetUpViews = setUpViews_method.equals(methodName, ignoreCase = true)
                val findSetUpObservers = setUpObservers_method.equals(methodName, ignoreCase = true)
                if (findSetUpViews || findSetUpObservers) {
                    if (findSetUpViews) {
                        if (setUpViews_method != methodName) {
                            report(context, method, setUpViews_method)
                        }
                    } else if (findSetUpObservers) {
                        if (setUpObservers_method != methodName) {
                            report(context, method, setUpObservers_method)
                        }
                    }
                }
            }
    }

    private fun report(context: JavaContext, method: UMethod, fixName: String) {
        val fix = fix().replace()
            .with(fixName)
            .build()

        Incident(context, ISSUE)
            .message(MESSAGE)
            .fix(fix)
            .at(method)
            .report(context)
    }

    companion object {

        private const val setUpViews_method = "setUpViews"
        private const val setUpObservers_method = "setUpObservers"
        private const val MESSAGE = "오타 수정 필요"

        @JvmField
        val ISSUE = Issue.create(
            id = "TypoMethodInComponentDetector",
            briefDescription = "Update",
            explanation = MESSAGE,
            category = Category.CORRECTNESS,
            priority = 10,
            severity = Severity.WARNING,
            implementation = Implementation(
                TypoMethodInComponentDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}