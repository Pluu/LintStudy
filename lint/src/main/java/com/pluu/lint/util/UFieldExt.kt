package com.pluu.lint.util

import com.intellij.psi.PsiClassType
import com.intellij.psi.PsiSubstitutor
import com.intellij.psi.PsiType
import org.jetbrains.uast.UField

internal fun UField.findTypeAndGeneric(): List<PsiType> = when (val psiType = type) {
    is PsiClassType -> {
        val substitutor = psiType.resolveGenerics().substitutor
        if (substitutor == PsiSubstitutor.EMPTY) {
            // Non
            listOf(psiType)
        } else {
            // Generic
            substitutor.substitutionMap.values.toList()
        }
    }
    else -> emptyList()
}