package com.pluu.lint.util

import com.intellij.psi.PsiJavaFile
import org.jetbrains.uast.UCallExpression

internal fun UCallExpression.methodPackageName(): String? =
    (resolve()?.containingFile as? PsiJavaFile)?.packageName

internal fun UCallExpression.methodQualifiedName(): String? =
    resolve()?.containingClass?.qualifiedName