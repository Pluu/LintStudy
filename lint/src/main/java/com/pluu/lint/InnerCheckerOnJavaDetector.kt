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
import com.android.tools.lint.detector.api.isKotlin
import com.intellij.psi.PsiType
import com.pluu.lint.util.classPackageName
import com.pluu.lint.util.findTypeAndGeneric
import org.jetbrains.uast.UClass
import java.util.EnumSet

class InnerCheckerOnJavaDetector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes() = listOf(
        UClass::class.java,
    )

    override fun createUastHandler(context: JavaContext) = object : UElementHandler() {
        override fun visitClass(node: UClass) {
            // 테스트 패키지내에서만 동작
            if (node.classPackageName?.endsWith("innercheck") == false) return

            // Kotlin 파일은 처리하지 않음
            if (isKotlin(context.psiFile)) return
            val innerClassesQualifiedNames = node.innerClasses.mapNotNull {
                it.qualifiedName
            }

            for (field in node.fields) {
                val types: List<PsiType> = field.findTypeAndGeneric()
                val typesQualifiedNames = types.map {
                    it.canonicalText
                }.toSet()

                if (innerClassesQualifiedNames.intersect(typesQualifiedNames).isNotEmpty()) {
                    // Lint 7.0 이상부터 사용
                    val incident = Incident(context, ISSUE)
                        .message(message)
                        .at(field)
                    context.report(incident)

                    // Lint 7.0 미만
//                    context.report(
//                        ISSUE,
//                        field,
//                        context.getLocation(field),
//                        message
//                    )
                }
            }
        }
    }

    companion object {
        private const val message = "얘네들 inner 타입"

        @JvmField
        val ISSUE = Issue.create(
            id = "InnerCheckerOnJavaDetector",
            briefDescription = "Update",
            explanation = message,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                InnerCheckerOnJavaDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}