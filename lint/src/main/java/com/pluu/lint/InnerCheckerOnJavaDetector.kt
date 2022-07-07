package com.pluu.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.pluu.lint.util.classPackageName
import org.jetbrains.uast.UClass
import java.util.*

@Suppress("UnstableApiUsage")
class InnerCheckerOnJavaDetector : Detector(), Detector.UastScanner {
    override fun getApplicableUastTypes() = listOf(
        UClass::class.java,
    )

    override fun createUastHandler(context: JavaContext): UElementHandler {
        val isKotlin = isKotlin(context.psiFile)
        return object : UElementHandler() {
            override fun visitClass(node: UClass) {
                // 테스트 패키지내에서만 동작
                if (node.classPackageName?.endsWith("innercheck") == false) return

                if (isKotlin) return
                val innerClass = node.innerClasses.mapNotNull {
                    it.qualifiedName
                }

                for (field in node.fields) {
                    val type = field.type
                    val typeQualifiedName = type.getCanonicalText(false)
                    if (innerClass.contains(typeQualifiedName)) {
                        context.report(
                            ISSUE,
                            node,
                            context.getLocation(field),
                            message
                        )
                    }
                }
            }
        }
    }

    companion object {
        private val message = "얘네들 inner 타입"

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