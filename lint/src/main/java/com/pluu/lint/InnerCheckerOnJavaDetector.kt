package com.pluu.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiType
import com.pluu.lint.util.classPackageName
import com.pluu.lint.util.findTypeAndGeneric
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
                val innerClassesQualifiedNames = node.innerClasses.mapNotNull {
                    it.qualifiedName
                }

                for (field in node.fields) {
                    val types: List<PsiType> = field.findTypeAndGeneric()
                    val typesQualifiedNames = types.map {
                        it.canonicalText
                    }.toSet()

                    if (innerClassesQualifiedNames.intersect(typesQualifiedNames).isNotEmpty()) {
                        context.report(
                            ISSUE,
                            field,
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