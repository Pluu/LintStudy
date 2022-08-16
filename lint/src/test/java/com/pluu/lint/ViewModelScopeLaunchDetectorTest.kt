package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.pluu.lint.stubs.CoroutineStub
import com.pluu.lint.stubs.ViewModelStub
import org.junit.Test

@Suppress("UnstableApiUsage")
class ViewModelScopeLaunchDetectorTest {
    @Test
    fun detectViewModelScope() {
        lint().files(
            ViewModelStub.viewModelStub,
            ViewModelStub.viewModelScopeStub,
            CoroutineStub.coroutine,
            kotlin(
                """                    
                    import androidx.lifecycle.ViewModel
                    import androidx.lifecycle.viewModelScope
                    import kotlinx.coroutines.launch
                    
                    private class SampleViewModel : ViewModel() {
                        init {
                            viewModelScope.launch {
                    
                            }
                        }
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(ViewModelScopeLaunchDetector.ISSUE)
            .run()
            .expect(
                """
                    src/SampleViewModel.kt:7: Warning: Detect ViewModelScope launch [ViewModelScopeLaunchDetector]
                            viewModelScope.launch {
                                           ~~~~~~
                    0 errors, 1 warnings
                """.trimIndent()
            )
    }

    @Test
    fun notExistViewModelScope() {
        lint().files(
            ViewModelStub.viewModelStub,
            kotlin(
                """                    
                    import androidx.lifecycle.ViewModel
                    
                    private class SampleViewModel : ViewModel() {}
                """.trimIndent()
            ).indented()
        )
            .issues(ViewModelScopeLaunchDetector.ISSUE)
            .run()
            .expectClean()
    }
}