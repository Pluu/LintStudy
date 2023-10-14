package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import com.pluu.lint.stubs.LIVEDATA_STUBS
import org.junit.Test

class LiveDataObserveNotNullDetectorTest {
    @Test
    fun testSuccess() {
        lint().files(
            *LIVEDATA_STUBS,
            LIVEDATA_OBSERVE_NOT_NULL,
            BaseActivity,
            kotlin(
                """
import androidx.activity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluu.lintstudy.BaseActivity
import com.pluu.lintstudy.utils.observeNotNull

class SampleActivity : BaseActivity() {
    private val stringLiveData: LiveData<String> = MutableLiveData<String>()
    private val stringNullableLiveData: LiveData<String?> = MutableLiveData<String?>()
    private val unitLiveData: LiveData<Unit> = MutableLiveData<Unit>()

    private val stringLiveData2 = MutableLiveData<String>()
    private val stringNullableLiveData2 = MutableLiveData<String?>()
    private val unitLiveData2 = MutableLiveData<Unit>()

    fun test() {
        stringLiveData.observeNotNull(this) {}
        stringNullableLiveData.observeNotNull(this) {}
        unitLiveData.observeNotNull(this) {}

        stringLiveData2.observeNotNull(this) {}
        stringNullableLiveData2.observeNotNull(this) {}
        unitLiveData2.observeNotNull(this) {}

        stringLiveData.observeNotNull {}
        stringNullableLiveData.observeNotNull {}
        unitLiveData.observeNotNull {}

        stringLiveData2.observeNotNull {}
        stringNullableLiveData2.observeNotNull {}
        unitLiveData2.observeNotNull {}
    }
}
                """.trimIndent()
            ).indented()
        )
            .issues(LiveDataObserveNotNullDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun testFail() {
        lint().files(
            *LIVEDATA_STUBS,
            LIVEDATA_OBSERVE_NOT_NULL,
            BaseActivity,
            kotlin(
                """
import com.pluu.lintstudy.BaseActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluu.lintstudy.utils.observeNotNull

class SampleActivity : BaseActivity() {
    private val sample: LiveData<Nothing> = MutableLiveData<Nothing>()
    private val sample2 = MutableLiveData<Nothing>()

    fun test() {
        sample.observeNotNull(this) {}
        sample2.observeNotNull(this) {}

        sample.observeNotNull {}
        sample2.observeNotNull {}
    }
}
                """.trimIndent()
            ).indented()
        )
            .issues(LiveDataObserveNotNullDetector.ISSUE)
            .testModes(TestMode.REORDER_ARGUMENTS)
            .run()
            .expect(
                """
src/SampleActivity.kt:11: Error: LiveData<Nothing>에는 observeNotNull을 사용할 수 없습니다. [LiveDataObserveNotNullDetector]
        sample.observeNotNull(action = {}, lifecycleOwner = this)
               ~~~~~~~~~~~~~~
src/SampleActivity.kt:12: Error: LiveData<Nothing>에는 observeNotNull을 사용할 수 없습니다. [LiveDataObserveNotNullDetector]
        sample2.observeNotNull(action = {}, lifecycleOwner = this)
                ~~~~~~~~~~~~~~
src/SampleActivity.kt:14: Error: LiveData<Nothing>에는 observeNotNull을 사용할 수 없습니다. [LiveDataObserveNotNullDetector]
        sample.observeNotNull {}
               ~~~~~~~~~~~~~~
src/SampleActivity.kt:15: Error: LiveData<Nothing>에는 observeNotNull을 사용할 수 없습니다. [LiveDataObserveNotNullDetector]
        sample2.observeNotNull {}
                ~~~~~~~~~~~~~~
4 errors, 0 warnings
                """.trimIndent()
            ).expectFixDiffs(
                """
Fix for src/SampleActivity.kt line 11: Use observe function:
@@ -11 +11
-         sample.observeNotNull(action = {}, lifecycleOwner = this)
+         sample.observe(action = {}, lifecycleOwner = this)
Fix for src/SampleActivity.kt line 12: Use observe function:
@@ -12 +12
-         sample2.observeNotNull(action = {}, lifecycleOwner = this)
+         sample2.observe(action = {}, lifecycleOwner = this)
Fix for src/SampleActivity.kt line 14: Use observe function:
@@ -14 +14
-         sample.observeNotNull {}
+         sample.observe {}
Fix for src/SampleActivity.kt line 15: Use observe function:
@@ -15 +15
-         sample2.observeNotNull {}
+         sample2.observe {}
            """.trimIndent()
            )
    }
}

private val LIVEDATA_OBSERVE_NOT_NULL = kotlin(
    "com/pluu/lintstudy/utils/LiveData.kt",
    """
package com.pluu.lintstudy.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LiveData<T>.observeNotNull(
    lifecycleOwner: LifecycleOwner,
    action: (T & Any) -> Unit
) { }
    """.trimIndent()
).indented().within("src")

private val BaseActivity = kotlin(
    "com/pluu/lintstudy/BaseActivity.kt",
    """
package com.pluu.lintstudy

import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import com.pluu.lintstudy.utils.observeNotNull

open class BaseActivity : ComponentActivity() {
    protected fun <T> LiveData<T>.observeNotNull(action: (T & Any) -> Unit) {
        observeNotNull(this@BaseActivity, action)
    }
}
    """.trimIndent()
).indented().within("src")