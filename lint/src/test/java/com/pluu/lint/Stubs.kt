package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestFile

object Stubs {
    val TYPEDARRAY_USE: TestFile = LintDetectorTest.kotlin(
        "androidx/core/content/res/TypedArray.kt",
        """
                package androidx.core.content.res
                import android.content.res.TypedArray             
                public inline fun <R> TypedArray.use(block: (TypedArray) -> R): R {
                    return block(this)
                }
            """
    )
        .indented().within("src")
}