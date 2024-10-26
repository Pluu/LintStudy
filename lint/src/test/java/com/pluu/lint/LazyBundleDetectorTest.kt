package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LazyBundleDetectorTest : LintDetectorTest() {

    override fun getDetector() = LazyBundleDetector()

    override fun getIssues() = listOf(
        LazyBundleDetector.ISSUE
    )

    @Test
    fun testSuccess() {
        val activityFile = kotlin(
            """
package com.pluu.lintstudy.lazybundle

import android.app.Activity

class LazySampleActivity : Activity() {

    private val string = "a"
    private val int = 1
    private val dataClass = Sample("A")
    private val index by inject<String>()
    private val lazyIndex by lazy { index.toInt() }
    private val d = Sample("A")
}

data class Sample(val a: String)
            """.trimIndent(),
        ).indented()

        lint().files(
            extension_Stub,
            activityFile
        ).run()
            .expectClean()
    }

    @Test
    fun testFail() {
        val activityFile = kotlin(
            """
package com.pluu.lintstudy.lazybundle

import android.app.Activity

class LazySampleActivity : Activity() {
    private val index by inject<String>()
    private val i = index.toInt()
    private val i2 = index
}
            """.trimIndent(),
        ).indented()

        lint().files(
            extension_Stub,
            activityFile
        ).run()
            .expect(
                """
src/com/pluu/lintstudy/lazybundle/LazySampleActivity.kt:7: Error: Lazy한 값을 클래스 인스턴스화 시점에 사용하면 안됨 [LazyDetector]
    private val i = index.toInt()
                    ~~~~~~~~~~~~~
src/com/pluu/lintstudy/lazybundle/LazySampleActivity.kt:8: Error: Lazy한 값을 클래스 인스턴스화 시점에 사용하면 안됨 [LazyDetector]
    private val i2 = index
                     ~~~~~
2 errors, 0 warnings
                """.trimIndent()
            )
    }
}

private val extension_Stub = kotlin(
    """
package com.pluu.lintstudy.lazybundle

import android.app.Activity
import kotlin.reflect.KProperty

inline fun <reified T> Activity.inject(): LazyProvider<Activity, T> {
    return lazy { null }
}

interface LazyProvider<A, T> {
    operator fun provideDelegate(thisRef: A, prop: KProperty<*>): Lazy<T>
}
            """.trimIndent()
).indented().within("src")