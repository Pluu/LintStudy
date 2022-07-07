package com.pluu.lint.util

import com.intellij.psi.PsiJavaFile
import org.jetbrains.uast.UClass

internal val UClass.classPackageName: String?
    get() = (this.containingFile as? PsiJavaFile)?.packageName
