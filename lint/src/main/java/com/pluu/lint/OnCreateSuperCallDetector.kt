package com.pluu.lint

import com.android.SdkConstants
import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.Scope
import com.android.tools.lint.detector.api.Severity
import com.intellij.psi.PsiClass
import com.intellij.psi.util.InheritanceUtil
import com.pluu.lint.util.isKotlin
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.UIfExpression
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.UQualifiedReferenceExpression
import org.jetbrains.uast.USuperExpression
import org.jetbrains.uast.visitor.AbstractUastVisitor
import java.util.EnumSet

class OnCreateSuperCallDetector : Detector(), Detector.UastScanner {

    override fun getApplicableUastTypes() = listOf(UMethod::class.java)

    override fun createUastHandler(context: JavaContext): UElementHandler? {
        if (!isKotlin(context.psiFile)) return null
        return object : UElementHandler() {
            override fun visitMethod(node: UMethod) {
                // Activity 하위가 아닌 경우는 탐지 안함
                val psiClass = node.containingClass ?: return
                if (!findAInheritanceActivity(psiClass)) return

                val visitor = AbortCheckVisitor()
                node.uastBody?.accept(visitor)
                if (visitor.abortCount > 0) {
                    context.report(ISSUE, node, context.getNameLocation(node), message)
                }
            }
        }
    }

    private fun findAInheritanceActivity(psiClass: PsiClass): Boolean {
        return InheritanceUtil.isInheritor(psiClass, SdkConstants.CLASS_ACTIVITY)
    }

    private class AbortCheckVisitor : AbstractUastVisitor() {
        var abortCount: Int = 0
        var visitSuperOnCreate = false
        var isVisitorEnd = false

        private val visitedMethods = mutableSetOf<UCallExpression>()

        override fun visitIfExpression(node: UIfExpression): Boolean {
            if (isVisitorEnd) return true
            val thenBranch = node.thenExpression
            thenBranch?.accept(this)
            return true
        }

        override fun visitQualifiedReferenceExpression(node: UQualifiedReferenceExpression): Boolean {
            if (isVisitorEnd) return true
            val receiver = node.receiver
            val selector = node.selector
            if (receiver is USuperExpression && selector is UCallExpression && selector.methodIdentifier?.name == "onCreate") {
                visitSuperOnCreate = true
                isVisitorEnd = true
            }
            return super.visitQualifiedReferenceExpression(node)
        }

        override fun visitCallExpression(node: UCallExpression): Boolean {
            if (isVisitorEnd) return true
            if (visitedMethods.contains(node)) return super.visitCallExpression(node)

            val name = node.methodIdentifier?.name ?: return super.visitCallExpression(node)
            visitedMethods.add(node)

            if (name == "finish") {
                isVisitorEnd = true
                abortCount++
                return super.visitCallExpression(node)
            }
            return super.visitCallExpression(node)
        }
    }

    companion object {

        private const val message = "OnCreateSuperCallDetector"

        @JvmField
        val ISSUE = Issue.create(
            id = OnCreateSuperCallDetector::class.java.simpleName,
            briefDescription = "OnCreateSuperCallDetector",
            explanation = message,
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = Implementation(
                OnCreateSuperCallDetector::class.java,
                EnumSet.of(Scope.JAVA_FILE),
                Scope.JAVA_FILE_SCOPE
            )
        )
    }
}