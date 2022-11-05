package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestMode
import com.pluu.lint.stubs.FRAGMENT_STUB
import org.junit.Test

@Suppress("UnstableApiUsage")
class TypoMethodInComponentDetectorTest : LintDetectorTest() {

    override fun getDetector() = TypoMethodInComponentDetector()

    override fun getIssues() = listOf(
        TypoMethodInComponentDetector.ISSUE
    )

    @Test
    fun testKotlin_success() {
        val activityFile = kotlin(
            """
                import android.app.Activity
                class TypoActivitySample : Activity() {
                    fun setUpViews() {}
                
                    fun setUpObservers() {}
                
                    fun test() {}
                }
            """.trimIndent()
        ).indented().within("src")

        val fragmentFile = kotlin(
            """
                import androidx.fragment.app.Fragment

                class TypoFragmentSample : Fragment() {
                    fun setUpViews() {}
                
                    fun setUpObservers() {}
                
                    fun test() {}                 
                }
            """.trimIndent()
        ).indented().within("src")

        lint().files(
            activityFile,
            fragmentFile,
            FRAGMENT_STUB
        ).run()
            .expectClean()
    }

    @Test
    fun testKotlin_non_super_success() {
        val file = kotlin(
            """
                fun setupviews() {

                }

                fun setupobservers() {

                }

                class TypoSample {
                    fun setUpViews() {

                    }

                    fun setupobservers() {

                    }

                    fun test() {

                    }
                }
            """.trimIndent()
        ).indented().within("src")

        lint().files(
            file
        ).run()
            .expectClean()
    }

    @Test
    fun testKotlin_fail() {
        val file = kotlin(
            """
                import android.app.Activity
                
                class TypoActivitySample : Activity() {
                    fun setupviews() {
                
                    }
                
                    fun setupobservers() {
                
                    }
                
                    fun test() {
                
                    }
                }
            """.trimIndent()
        ).indented().within("src")

        lint().files(
            file
        ).testModes(TestMode.UI_INJECTION_HOST)
            .run()
            .expectWarningCount(2)
    }
}