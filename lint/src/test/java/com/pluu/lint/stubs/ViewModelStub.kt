@file:Suppress("UnstableApiUsage")

package com.pluu.lint.stubs

import com.android.tools.lint.checks.infrastructure.LintDetectorTest

object ViewModelStub {
    val viewModelStub = LintDetectorTest.java(
        """
            package androidx.lifecycle;            
            public abstract class ViewModel {}
        """.trimIndent()
    ).indented().within("src")

    val viewModelScopeStub = LintDetectorTest.kotlin(
        """
            package androidx.lifecycle
            import kotlinx.coroutines.CoroutineScope            
            public val ViewModel.viewModelScope: CoroutineScope
                get() {
                    return this.getTag("") as CoroutineScope
                }
        """.trimIndent()
    ).indented().within("src")
}