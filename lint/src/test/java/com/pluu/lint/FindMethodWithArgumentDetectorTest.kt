package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

@Suppress("UnstableApiUsage")
class FindMethodWithArgumentDetectorTest {

    @Test
    fun testJava() {
        lint().files(
            java(
                """
                    import java.util.List;

                    public class TestClass {
                        void test(List<Integer> list) {
                            list.indexOf(0);
                            list.indexOf(1);
                        }
                    }
                    """
            ).indented()
        )
            .issues(FindMethodWithArgumentDetector.ISSUE)
            .run()
            .expect(
                """
                    src/TestClass.java:6: Warning: 특정 메소드& 특정 값을 사용하는 케이스 [FindMethodWithArgumentDetector]
                            list.indexOf(1);
                                 ~~~~~~~
                    0 errors, 1 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testKotlin() {
        lint().files(
            kotlin(
                """
                    fun test(list: List<Int>) {
                        list.indexOf(0)
                        list.indexOf(1)
                    }
                    """
            ).indented()
        )
            .issues(FindMethodWithArgumentDetector.ISSUE)
            .run()
            .expect(
                """
                    src/test.kt:3: Warning: 특정 메소드& 특정 값을 사용하는 케이스 [FindMethodWithArgumentDetector]
                        list.indexOf(1)
                             ~~~~~~~
                    0 errors, 1 warnings
                """.trimIndent()
            )
    }
}