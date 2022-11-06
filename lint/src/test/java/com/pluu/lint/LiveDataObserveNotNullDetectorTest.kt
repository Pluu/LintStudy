package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.pluu.lint.stubs.LIVEDATA_STUBS
import org.junit.Test

@Suppress("UnstableApiUsage")
class LiveDataObserveNotNullDetectorTest {
    @Test
    fun testSuccess() {
        lint().files(
            *LIVEDATA_STUBS,
            LIVEDATA_OBSERVE_NOT_NULL,
            kotlin(
                """
                import androidx.activity
                import androidx.lifecycle.LiveData
                import androidx.lifecycle.MutableLiveData
                import com.pluu.lintstudy.utils.observeNotNull

                class SampleActivity : ComponentActivity() {
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
            kotlin(
                """
                import androidx.activity
                import androidx.lifecycle.LiveData
                import androidx.lifecycle.MutableLiveData
                import com.pluu.lintstudy.utils.observeNotNull

                class SampleActivity : ComponentActivity() {
                    private val sample: LiveData<Nothing> = MutableLiveData<Nothing>()
                    private val sample2 = MutableLiveData<Nothing>()
                
                    fun test() {
                        sample.observeNotNull(this) {
                        }
                        sample2.observeNotNull(this) {
                        }
                    }
                }
                """.trimIndent()
            ).indented()
        )
            .issues(LiveDataObserveNotNullDetector.ISSUE)
            .run()
            .expect(
                """
src/SampleActivity.kt:11: Error: LiveData<Nothing>에는 observeNotNull을 사용할 수 없습니다. [LiveDataObserveNotNullDetector]
        sample.observeNotNull(this) {
               ~~~~~~~~~~~~~~
src/SampleActivity.kt:13: Error: LiveData<Nothing>에는 observeNotNull을 사용할 수 없습니다. [LiveDataObserveNotNullDetector]
        sample2.observeNotNull(this) {
                ~~~~~~~~~~~~~~
2 errors, 0 warnings
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