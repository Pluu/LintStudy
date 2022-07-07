package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

@Suppress("UnstableApiUsage")
class InnerCheckerOnJavaDetectorTest {

    @Test
    fun testBasic() {
        lint().files(
            java(
                """
                    package com.pluu.lintstudy.innercheck;

                    public class TestClass {
                        private InnerClass innerClass;

                        private InnerEnum innerEnum;

                        public class InnerClass {
                        }

                        public enum InnerEnum {
                            A
                        }
                    }
                    """
            ).indented()
        )
            .issues(InnerCheckerOnJavaDetector.ISSUE)
            .run()
            .expect(
                """
                    src/com/pluu/lintstudy/innercheck/TestClass.java:4: Warning: 얘네들 inner 타입 [InnerCheckerOnJavaDetector]
                        private InnerClass innerClass;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/com/pluu/lintstudy/innercheck/TestClass.java:6: Warning: 얘네들 inner 타입 [InnerCheckerOnJavaDetector]
                        private InnerEnum innerEnum;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    0 errors, 2 warnings
                """.trimIndent()
            )
    }
}