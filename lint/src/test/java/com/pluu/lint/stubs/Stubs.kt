@file:Suppress("UnstableApiUsage")

package com.pluu.lint.stubs

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
        """.trimIndent()
    ).indented().within("src")

    val SAMPLE_ANNOTATION: TestFile = LintDetectorTest.kotlin(
        "com/pluu/lintstudy/SampleAnnotation.kt",
        """
            package com.pluu.lintstudy
            
            @Retention(AnnotationRetention.BINARY)
            @Target(AnnotationTarget.CLASS)
            annotation class SampleAnnotation(val isEnabled: Boolean = true)
        """.trimIndent()
    ).indented().within("src")

    val SharedPreferences_edit : TestFile = LintDetectorTest.kotlin(
        "androidx/core/content/SharedPreferences.kt",
        """
            package androidx.core.content
            import android.content.SharedPreferences

            public inline fun SharedPreferences.edit(
                commit: Boolean = false,
                action: SharedPreferences.Editor.() -> Unit
            ) {
            }
        """.trimIndent()
    ).indented().within("src")
}