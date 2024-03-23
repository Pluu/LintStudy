package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.detector.api.Detector
import com.pluu.lint.stubs.DataBindingStub
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class DataBindingDuplicationDetectorTest : LintDetectorTest() {
    override fun getDetector(): Detector = DataBindingDuplicationDetector()

    override fun getIssues() = listOf(DataBindingDuplicationDetector.ISSUE)

    @Test
    fun testDetectCase() {
        lint()
            .files(
                DataBindingStub.BindingAdapterStub,
                kotlin(
                    """
import androidx.databinding.BindingAdapter

class SampleView {
    companion object {
        @JvmStatic
        @BindingAdapter("test")
        fun setTest(view: View, value: Int) {
        }
    }
}
                """
                )
                    .indented(),
            )
            .run()
            .expect(
                """
src/SampleView.kt:5: Warning: 복수 DataBinding 생성으로 IDE 경고가 발생합니다. [DataBindingDuplicationDetector]
        @JvmStatic
        ^
0 errors, 1 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testNonDetectCase_NonJvmStatic() {
        lint()
            .files(
                DataBindingStub.BindingAdapterStub,
                kotlin(
                    """
import androidx.databinding.BindingAdapter

class SampleView {
    companion object {
        @BindingAdapter("test")
        fun setTest(view: View, value: Int) {
        }
    }
}
                """
                )
                    .indented(),
            )
            .run()
            .expectClean()
    }

    @Test
    fun testNonDetectCase_AnotherClass() {
        lint()
            .files(
                DataBindingStub.BindingAdapterStub,
                kotlin(
                    """
import androidx.databinding.BindingAdapter

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("test1")
    fun setTest1(view: View, value: Int) {
    }

    @BindingAdapter("test2")
    fun setTest2(view: View, value: Int) {
    }
}

@BindingAdapter("test3")
fun setTest3(view: View, value: Int) {
}
                """
                )
                    .indented(),
            )
            .run()
            .expectClean()
    }
}