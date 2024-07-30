package com.pluu.lint.compose

import androidx.compose.lint.Names
import androidx.compose.lint.inheritsFrom
import androidx.compose.lint.isComposable
import androidx.compose.lint.returnsUnit
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Incident
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.android.tools.lint.detector.api.SourceCodeScanner
import com.intellij.psi.PsiModifier
import org.jetbrains.kotlin.psi.KtParameter
import org.jetbrains.uast.UMethod
import java.util.EnumSet

class RequiredModifierParameterDetector : Detector(), SourceCodeScanner {
    override fun getApplicableUastTypes() = listOf(UMethod::class.java)

    override fun createUastHandler(context: JavaContext) =
        object : UElementHandler() {
            override fun visitMethod(node: UMethod) {
                // Ignore non-composable functions
                if (!node.isComposable) return
                // Ignore non-unit composable functions
                if (!node.returnsUnit) return
                // Ignore private composable functions
                if (node.modifierList.hasModifierProperty(PsiModifier.PRIVATE)) return

                val noneModifier = node.uastParameters.none { parameter ->
                    parameter.sourcePsi is KtParameter &&
                            parameter.type.inheritsFrom(Names.Ui.Modifier)
                }
                if (noneModifier) {
                    val incident = Incident(context, ISSUE)
                        .message(message)
                        .at(node)
                    context.report(incident)
                }
            }
        }

    companion object {
        private const val message = "public/internal Composable functions, the Modifier parameter is required."
//        private const val message = "Public/Internal Composable function에는 Modifier 파라미터가 필수입니다."
        @JvmField
        val ISSUE = Issue.create(
            id = "RequiredModifierParameterDetector",
            briefDescription = "Need Modifier",
            explanation = message,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.WARNING,
            implementation = Implementation(
                RequiredModifierParameterDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}