package com.pluu.lint.stubs;

import com.android.tools.lint.checks.infrastructure.LintDetectorTest.kotlin
import com.android.tools.lint.checks.infrastructure.TestFile

object AppCompatStub {
    val APPCOMPAT_ACTIVITY: TestFile =
        kotlin(
            "androidx/appcompat/app/AppCompatActivity.kt",
            """
                package androidx.appcompat.app
                import android.app.Activity
                open class AppCompatActivity: Activity()
            """
        )
            .indented()
            .within("src")
}