package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestFile
import com.android.tools.lint.checks.infrastructure.TestLintResult
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Issue
import com.pluu.lint.stubs.AppCompatStub
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class OnCreateSuperCallDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = OnCreateSuperCallDetector()

    override fun getIssues(): MutableList<Issue> =
        mutableListOf(OnCreateSuperCallDetector.ISSUE)

    @Test
    fun testActivitySuccess() {
        val testFile = generateActivity {
            """
setTheme(R.style.AppTheme)
super.onCreate(savedInstanceState)
            """.trimIndent()
        }

        runActivityLint(testFile)
            .expectClean()
    }

    @Test
    fun testActivityConditionSuccess() {
        val testFile = generateActivity {
            """
if (check()) {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
} else {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
}
            """.trimIndent()
        }

        runActivityLint(testFile)
            .expectClean()
    }

    @Test
    fun testAppCompatActivitySuccess() {
        val testFile = generateAppCompatActivity {
            """
setTheme(R.style.AppTheme)
super.onCreate(savedInstanceState)
            """.trimIndent()
        }

        runAppCompatLint(testFile)
            .expectClean()
    }

    @Test
    fun testAppCompatActivityConditionSuccess() {
        val testFile = generateAppCompatActivity {
            """
if (check()) {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
} else {
    setTheme(R.style.AppTheme)
    super.onCreate(savedInstanceState)
}
            """.trimIndent()
        }

        runAppCompatLint(testFile)
            .expectClean()
    }

    @Test
    fun testActivityFail() {
        val testFile = generateActivity {
            """
finish()
super.onCreate(savedInstanceState)
            """.trimIndent()
        }

        runActivityLint(testFile)
            .expect(
                """
src/com/example/CustomActivity.kt:7: Error: OnCreateSuperCallDetector [OnCreateSuperCallDetector]
    override fun onCreate(savedInstanceState: Bundle?) {
                 ~~~~~~~~
1 errors, 0 warnings
            """.trimIndent()
            )
    }

    @Test
    fun testAppCompatActivityFail() {
        val testFile = generateAppCompatActivity {
            """
finish()
super.onCreate(savedInstanceState)
            """.trimIndent()
        }

        runAppCompatLint(testFile)
            .expect(
                """
src/com/example/CustomActivity.kt:7: Error: OnCreateSuperCallDetector [OnCreateSuperCallDetector]
    override fun onCreate(savedInstanceState: Bundle?) {
                 ~~~~~~~~
1 errors, 0 warnings
            """.trimIndent()
            )
    }

    @Test
    fun testNotActivity() {
        val testFile = kotlin(
            "com/example/CustomActivity.kt",
            """
package com.example

import android.os.Bundle

class CustomActivity {
    fun onCreate(savedInstanceState: Bundle?) {
        finish()
    }

    private fun finish() {}
}
            """
        )
            .indented()

        lint().files(testFile)
            .run()
            .expectClean()
    }

    ///////////////////////////////////////////////////////////////////////////
    // Fail condition
    ///////////////////////////////////////////////////////////////////////////

    // if 내에서 종료하는 경우
    @Test
    fun testFail1() {
        val testFile = generateActivity {
            """
if (check()) {
    finish()
}

setTheme(R.style.AppTheme)
super.onCreate(savedInstanceState)
            """.trimIndent()
        }

        runActivityLint(testFile)
            .expect(
                """
src/com/example/CustomActivity.kt:7: Error: OnCreateSuperCallDetector [OnCreateSuperCallDetector]
    override fun onCreate(savedInstanceState: Bundle?) {
                 ~~~~~~~~
1 errors, 0 warnings
                """.trimIndent()
            )
    }

    private fun generateActivity(builder: () -> String): TestFile = kotlin(
        "com/example/CustomActivity.kt",
        """
package com.example

import android.app.Activity
import android.os.Bundle

class CustomActivity: Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
${builder()}
    }

    private fun check(): Boolean = true
}
            """
    )
        .indented()
        .within("src")

    private fun generateAppCompatActivity(builder: () -> String): TestFile = kotlin(
        "com/example/CustomActivity.kt",
        """
package com.example

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class CustomActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
${builder()}
    }

    private fun check(): Boolean = true
}
            """
    )
        .indented()
        .within("src")

    private fun runActivityLint(classFile: TestFile): TestLintResult {
        return lint().files(classFile)
            .run()
    }

    private fun runAppCompatLint(classFile: TestFile): TestLintResult {
        return lint().files(AppCompatStub.APPCOMPAT_ACTIVITY, classFile)
            .run()
    }
}