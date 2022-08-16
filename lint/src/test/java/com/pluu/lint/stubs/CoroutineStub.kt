@file:Suppress("UnstableApiUsage")

package com.pluu.lint.stubs

import com.android.tools.lint.checks.infrastructure.LintDetectorTest

object CoroutineStub {
    val coroutine = LintDetectorTest.kotlin(
        """
        package kotlinx.coroutines

        object CoroutineScope

        fun CoroutineScope.launch(
            block: suspend CoroutineScope.() -> Unit
        ) {}
        """.trimIndent()
    ).indented().within("src")
}