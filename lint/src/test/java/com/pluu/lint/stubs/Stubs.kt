@file:Suppress("UnstableApiUsage")

package com.pluu.lint.stubs

import com.android.tools.lint.checks.infrastructure.LintDetectorTest
import com.android.tools.lint.checks.infrastructure.LintDetectorTest.java
import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestFile

object Stubs {
    val TYPEDARRAY_USE: TestFile = LintDetectorTest.kotlin(
        "androidx/core/content/res/TypedArray.kt",
        """
            package androidx.core.content.res
            import android.content.res.TypedArray             
            public inline fun <R> TypedArray.use(block: (TypedArray) -> R): R {
                return block(this)
            }
        """.trimIndent()
    ).indented().within("src")

    val SAMPLE_ANNOTATION: TestFile = LintDetectorTest.kotlin(
        "com/pluu/lintstudy/SampleAnnotation.kt",
        """
            package com.pluu.lintstudy
            
            @Retention(AnnotationRetention.BINARY)
            @Target(AnnotationTarget.CLASS)
            annotation class SampleAnnotation(val isEnabled: Boolean = true)
        """.trimIndent()
    ).indented().within("src")

    val SharedPreferences_edit : TestFile = LintDetectorTest.kotlin(
        "androidx/core/content/SharedPreferences.kt",
        """
            package androidx.core.content
            import android.content.SharedPreferences

            public inline fun SharedPreferences.edit(
                commit: Boolean = false,
                action: SharedPreferences.Editor.() -> Unit
            ) {
            }
        """.trimIndent()
    ).indented().within("src")
}

private val COMPONENT_ACTIVITY = java(
    """
package androidx.activity;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

public class ComponentActivity {
}
"""
)

private val LIFECYCLE = kotlin(
    "androidx/lifecycle/Lifecycle.kt",
    """
package androidx.lifecycle;

abstract class Lifecycle {
    enum class State { CREATED, STARTED }
    fun isAtLeast(state: State): Boolean {
        return true
    }
}
    """
).indented().within("src")

private val LIVEDATA = java(
    """package androidx.lifecycle;

public abstract class LiveData<T> {
    public void observe(LifecycleOwner owner, Observer<? super T> observer) {}
}
"""
)

private val MUTABLE_LIVEDATA = java(
    """
package androidx.lifecycle;

public class MutableLiveData<T> extends LiveData<T> { }
"""
)

private val LIFECYCLE_OWNER = java(
    """
package androidx.lifecycle;

public interface LifecycleOwner {
    Lifecycle getLifecycle();
}
"""
)

internal val LIVEDATA_STUBS = arrayOf(
    COMPONENT_ACTIVITY,
    LIFECYCLE,
    LIVEDATA,
    MUTABLE_LIVEDATA,
    LIFECYCLE_OWNER
)