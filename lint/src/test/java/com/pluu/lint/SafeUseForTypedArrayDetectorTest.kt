package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

@Suppress("UnstableApiUsage")
class SafeUseForTypedArrayDetectorTest {
    @Test
    fun testSuccess() {
        lint().files(
            Stubs.TYPEDARRAY_USE,
            kotlin(
                """                    
                    import android.content.Context
                    import androidx.core.content.res.use

                    private fun test(context: Context) {
                        context.obtainStyledAttributes(intArrayOf()).use { }
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(SafeUseForTypedArrayDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun testFail() {
        lint().files(
            kotlin(
                """                    
                    import android.content.Context

                    private fun test(context: Context) {
                        context.obtainStyledAttributes(intArrayOf()).use { }
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(SafeUseForTypedArrayDetector.ISSUE)
            .run()
            .expect(
                """
                    src/test.kt:4: Warning: AndroidX core의 use extension을 사용해주세요. [SafeUseForTypedArrayDetector]
                        context.obtainStyledAttributes(intArrayOf()).use { }
                                                                     ~~~
                    0 errors, 1 warnings
                """.trimIndent()
            )
    }
}