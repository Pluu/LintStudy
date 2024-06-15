package com.pluu.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.pluu.lint.util.isKotlin
import org.jetbrains.kotlin.psi.KtObjectDeclaration
import org.jetbrains.uast.UClass
import java.util.EnumSet

class DataBindingDuplicationDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes() = listOf(UClass::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        if (!isKotlin(context.psiFile)) return null
        return object : UElementHandler() {
            override fun visitClass(node: UClass) {
                val element = node.sourceElement ?: return
                if (element is KtObjectDeclaration && element.isCompanion()) {
                    node.methods
                        .filter { method ->
                            detectAnnotation.any { annotation ->
                                method.hasAnnotation(annotation)
                            } && method.hasAnnotation(jvmStaticAnnotation)
                        }.forEach { method ->
                            context.report(
                                ISSUE,
                                method,
                                context.getLocation(method),
                                message
                            )
                        }
                }
            }
        }
    }


    companion object {
        private val detectAnnotation = arrayOf(
            "androidx.databinding.BindingAdapter"
        )

        private val jvmStaticAnnotation = "kotlin.jvm.JvmStatic"

        private const val message = "복수 DataBinding 생성으로 IDE 경고가 발생합니다."

        @JvmField
        val ISSUE = Issue.create(
            id = DataBindingDuplicationDetector::class.java.simpleName,
            briefDescription = "DataBindingDuplicationDetector",
            explanation = message,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                DataBindingDuplicationDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}