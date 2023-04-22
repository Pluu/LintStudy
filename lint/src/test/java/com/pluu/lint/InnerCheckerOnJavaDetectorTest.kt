package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import org.junit.Test

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
                """.trimIndent()
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

    @Test
    fun testBasicGeneric() {
        lint().files(
            java(
                """
                    package com.pluu.lintstudy.innercheck;

                    import java.util.List;
                    import java.util.Map;
                    import java.util.Set;

                    public class TestClass {
                        private List<InnerClass> a1;

                        private List<InnerEnum> a2;

                        private Set<InnerClass> b1;

                        private Set<InnerEnum> b2;

                        private Map<String, InnerClass> c1;

                        private Map<String, InnerEnum> c2;

                        public class InnerClass {
                        }

                        public enum InnerEnum {
                            A
                        }
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(InnerCheckerOnJavaDetector.ISSUE)
            .run()
            .expect(
                """
                    src/com/pluu/lintstudy/innercheck/TestClass.java:8: Warning: 얘네들 inner 타입 [InnerCheckerOnJavaDetector]
                        private List<InnerClass> a1;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/com/pluu/lintstudy/innercheck/TestClass.java:10: Warning: 얘네들 inner 타입 [InnerCheckerOnJavaDetector]
                        private List<InnerEnum> a2;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/com/pluu/lintstudy/innercheck/TestClass.java:12: Warning: 얘네들 inner 타입 [InnerCheckerOnJavaDetector]
                        private Set<InnerClass> b1;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/com/pluu/lintstudy/innercheck/TestClass.java:14: Warning: 얘네들 inner 타입 [InnerCheckerOnJavaDetector]
                        private Set<InnerEnum> b2;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/com/pluu/lintstudy/innercheck/TestClass.java:16: Warning: 얘네들 inner 타입 [InnerCheckerOnJavaDetector]
                        private Map<String, InnerClass> c1;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/com/pluu/lintstudy/innercheck/TestClass.java:18: Warning: 얘네들 inner 타입 [InnerCheckerOnJavaDetector]
                        private Map<String, InnerEnum> c2;
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    0 errors, 6 warnings
                """.trimIndent()
            )
    }
}