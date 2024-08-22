package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.pluu.lint.stubs.CoroutineStub
import com.pluu.lint.stubs.ViewModelStub
import org.junit.Test

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

    @Test
    fun hasCeh() {
        lint().files(
            ViewModelStub.viewModelStub,
            ViewModelStub.viewModelScopeStub,
            CoroutineStub.coroutine,
            customCoroutineScope,
            kotlin(
                """                    
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlin.coroutines.EmptyCoroutineContext

private class SampleViewModel : ViewModel() {
    private val ceh = CoroutineExceptionHandler { c, t -> }
    
    init {
        viewModelScope.launchLoading(CoroutineExceptionHandler { c, t ->
        }) { }

        viewModelScope.launchLoading(ceh) { }
    }
}
                """.trimIndent()
            ).indented()
        ).allowCompilationErrors()
            .issues(
                ViewModelScopeLaunchDetector.ISSUE,
                ViewModelScopeLaunchDetector.CEH_ISSUE
            )
            .run()
            .expectClean()
    }

    @Test
    fun noneCeh() {
        lint().files(
            ViewModelStub.viewModelStub,
            ViewModelStub.viewModelScopeStub,
            CoroutineStub.coroutine,
            customCoroutineScope,
            kotlin(
                """                    
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

private class SampleViewModel : ViewModel() {
    
    init {
        viewModelScope.launchLoading { }
    }
}
                """.trimIndent()
            ).indented()
        ).allowCompilationErrors()
            .issues(
                ViewModelScopeLaunchDetector.ISSUE,
                ViewModelScopeLaunchDetector.CEH_ISSUE
            )
            .run()
            .expect(
"""
src/SampleViewModel.kt:7: Error: Detect CEH not param [ViewModelScopeLaunchDetector]
        viewModelScope.launchLoading { }
                       ~~~~~~~~~~~~~
1 errors, 0 warnings
""".trimIndent()
            )
    }

    @Test
    fun hasEmptyCeh() {
        lint().files(
            ViewModelStub.viewModelStub,
            ViewModelStub.viewModelScopeStub,
            CoroutineStub.coroutine,
            customCoroutineScope,
            kotlin(
                """                    
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlin.coroutines.EmptyCoroutineContext

private class SampleViewModel : ViewModel() {
    
    init {
        viewModelScope.launchLoading(EmptyCoroutineContext) { }
    }
}
                """.trimIndent()
            ).indented()
        ).allowCompilationErrors()
            .issues(
                ViewModelScopeLaunchDetector.ISSUE,
                ViewModelScopeLaunchDetector.CEH_ISSUE
            )
            .run()
            .expect(
                """
src/SampleViewModel.kt:8: Error: Detect EmptyCoroutineContext [ViewModelScopeLaunchDetector]
        viewModelScope.launchLoading(EmptyCoroutineContext) { }
                       ~~~~~~~~~~~~~
1 errors, 0 warnings
""".trimIndent()
            )
    }

    private val customCoroutineScope = kotlin(
        """
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launchLoading(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    block: () -> Unit
) = launch(coroutineContext) {
    block()
}
    """.trimIndent()
    )
}