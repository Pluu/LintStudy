package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

class RequiredCustomViewAttributeDetectorTest {

    @Test
    fun testBasic() {
        val layout = xml(
            "layout/image_view.xml",
            """
                <FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
                    xmlns:app="http://schemas.android.com/apk/res-auto"
                    android:layout_width="match_parent"
                    android:layout_height="match_parent">
                
                    <com.pluu.lintstudy.MyView
                        android:layout_width="300dp"
                        android:layout_height="300dp"
                        app:exampleDimension="24sp"
                        app:exampleString="Hello, MyView" />
                
                </FrameLayout>
            """.trimIndent()
        ).indented().within("res")

        lint().files(
            layout
        ).issues(RequiredCustomViewAttributeDetector.ISSUE)
            .run()
            .expect(
                """
                    res/layout/image_view.xml:6: Error: exampleDrawable 속성이 필수인 케이스 [RequiredCustomViewAttributeDetector]
                        <com.pluu.lintstudy.MyView
                         ~~~~~~~~~~~~~~~~~~~~~~~~~
                    1 errors, 0 warnings
                """.trimIndent()
            )
    }
}