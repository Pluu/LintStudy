package com.pluu.lint.util

import com.intellij.psi.PsiElement

fun isKotlin(element: PsiElement?): Boolean {
    return element != null && com.android.tools.lint.detector.api.isKotlin(element.language)
}