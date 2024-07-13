package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestFiles.xml
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.android.tools.lint.checks.infrastructure.TestMode
import org.junit.Test

class TwoMoreBlankLineDetectorTest {

    @Test
    fun detectKotlin_success() {
        lint().files(
            kotlin(
                """
class Sample {
    
    val a = 0

    val b = 0

}
"""
            ).indented()
        )
            .skipTestModes(TestMode.SUPPRESSIBLE)
            .issues(TwoMoreBlankLineDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun detectKotlin_waring() {
        lint().files(
            kotlin(
                """
class Sample {
    
    val a = 0


    val b = 0


}
"""
            ).indented()
        )
            .skipTestModes(TestMode.SUPPRESSIBLE)
            .issues(TwoMoreBlankLineDetector.ISSUE)
            .run()
            .expect(
                """
src/Sample.kt:4: Warning: 2줄 이상의 공백이 탐지됨 [ExtraBlankLines]

^
src/Sample.kt:7: Warning: 2줄 이상의 공백이 탐지됨 [ExtraBlankLines]

^
0 errors, 2 warnings
""".trimIndent()
            ).expectFixDiffs(
                """
Fix for src/Sample.kt line 4: 1줄로 바꾸기:
@@ -5 +5
-
Fix for src/Sample.kt line 7: 1줄로 바꾸기:
@@ -8 +8
-
            """.trimIndent()
            )
    }

    @Test
    fun detectXml_success() {
        lint().files(
            xml(
                "res/layout/sample.xml",
                """
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
        
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
            
</FrameLayout>
""".trimIndent()
            )
        )
            .skipTestModes(TestMode.SUPPRESSIBLE)
            .issues(TwoMoreBlankLineDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun detectXml_waring() {
        lint().files(
            xml(
                "res/layout/sample.xml",
                """
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
        
    <FrameLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
        
            
</FrameLayout>
""".trimIndent()
            ).indented()
        )
            .skipTestModes(TestMode.SUPPRESSIBLE)
            .issues(TwoMoreBlankLineDetector.ISSUE)
            .run()
            .expect(
                """
res/layout/sample.xml:5: Warning: 2줄 이상의 공백이 탐지됨 [ExtraBlankLines]
    
^
res/layout/sample.xml:10: Warning: 2줄 이상의 공백이 탐지됨 [ExtraBlankLines]
        
^
0 errors, 2 warnings
                """.trimIndent()
            ).expectFixDiffs(
                """
Fix for res/layout/sample.xml line 5: 1줄로 바꾸기:
@@ -5 +5
-
-
+
Fix for res/layout/sample.xml line 10: 1줄로 바꾸기:
@@ -10 +10
-
-
+
            """.trimIndent()
            )
    }
}