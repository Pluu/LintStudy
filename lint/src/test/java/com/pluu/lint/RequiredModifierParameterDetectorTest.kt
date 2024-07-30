package com.pluu.lint

import androidx.compose.lint.test.ComposeStubs
import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.pluu.lint.compose.RequiredModifierParameterDetector
import org.junit.Test

class RequiredModifierParameterDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = RequiredModifierParameterDetector()

    override fun getIssues(): MutableList<Issue> =
        mutableListOf(RequiredModifierParameterDetector.ISSUE)

    @Test
    fun testSuccess() {
        lint()
            .files(
                kotlin(
                    """
package com.pluu.lintstudy.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SampleSuccess(text: String, modifier: Modifier) {
}
            """
                ),
                ComposeStubs.Composable,
                ComposeStubs.Modifier
            )
            .run()
            .expectClean()
    }

    @Test
    fun testFailure() {
        lint()
            .files(
                kotlin(
                    """
package com.pluu.lintstudy.compose

import androidx.compose.runtime.Composable

@Composable
fun SampleSuccess(text: String) {
}
            """
                ),
                ComposeStubs.Composable,
                ComposeStubs.Modifier
            )
            .run()
            .expect(
                """
src/com/pluu/lintstudy/compose/test.kt:7: Warning: public/internal Composable functions, the Modifier parameter is required. [RequiredModifierParameterDetector]
fun SampleSuccess(text: String) {
    ~~~~~~~~~~~~~
0 errors, 1 warnings
                """.trimIndent()
            )
    }
}