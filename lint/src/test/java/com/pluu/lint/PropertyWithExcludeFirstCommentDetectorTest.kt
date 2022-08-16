package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.pluu.lint.stubs.Stubs
import org.junit.Test

@Suppress("UnstableApiUsage")
class PropertyWithExcludeFirstCommentDetectorTest {

    @Test
    fun testJava() {
        lint().files(
            Stubs.SAMPLE_ANNOTATION,
            java(
                """                    
                    package exclude_first_comment;                    

                    import com.pluu.lintstudy.SampleAnnotation;
                    
                    @SampleAnnotation
                    public class SampleJava {
                        private final String a1 = "A"; // aakak
                    
                        // comment
                        private final String a2 = "A"; // aakak
                    
                        /**
                         * comment
                         */
                        private final String a3 = "A"; // aakak
                    
                        final String b1 = "A"; // aakak
                    
                        // comment
                        final String b2 = "A"; // aakak
                    
                        /**
                         * comment
                         */
                        final String b3 = "A"; // aakak
                    
                        ///////////////////////////////////////////////////////////////////////////
                        // Complex
                        ///////////////////////////////////////////////////////////////////////////
                    
                        // Multiple Comment
                        final String d1 /**ajdadajsd*/ = /**ajdadajsd*/ "A"; // akaka
                    
                        // Nullable Variable
                        String d2;
                    
                        String d3 = null;
                    
                        String d4 = "";
                    
                        ///////////////////////////////////////////////////////////////////////////
                        // aksdkdk
                        ///////////////////////////////////////////////////////////////////////////
                        String d5 = "";
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(PropertyWithExcludeFirstCommentDetector.ISSUE)
            .run()
            .expect(
                """
                    src/exclude_first_comment/SampleJava.java:7: Warning: found inner type [SampleDetector]
                        private final String a1 = "A"; // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:10: Warning: found inner type [SampleDetector]
                        private final String a2 = "A"; // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:15: Warning: found inner type [SampleDetector]
                        private final String a3 = "A"; // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:17: Warning: found inner type [SampleDetector]
                        final String b1 = "A"; // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:20: Warning: found inner type [SampleDetector]
                        final String b2 = "A"; // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:25: Warning: found inner type [SampleDetector]
                        final String b3 = "A"; // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:32: Warning: found inner type [SampleDetector]
                        final String d1 /**ajdadajsd*/ = /**ajdadajsd*/ "A"; // akaka
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:35: Warning: found inner type [SampleDetector]
                        String d2;
                        ~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:37: Warning: found inner type [SampleDetector]
                        String d3 = null;
                        ~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:39: Warning: found inner type [SampleDetector]
                        String d4 = "";
                        ~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleJava.java:44: Warning: found inner type [SampleDetector]
                        String d5 = "";
                        ~~~~~~~~~~~~~~~
                    0 errors, 11 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testKotlin() {
        lint().files(
            Stubs.SAMPLE_ANNOTATION,
            kotlin(
                """                    
                    package exclude_first_comment

                    import com.pluu.lintstudy.SampleAnnotation

                    @SampleAnnotation
                    class SampleKotlin {
                        private val a1 = "A" // aakak

                        // comment
                        private val a2 = "A" // aakak

                        /**
                         * comment
                         */
                        private val a3 = "A" // aakak

                        val b1 = "adasa"

                        // comment
                        val b2 = "adasa"

                        /**
                         * comment
                         */
                        val b3 = "adasa"

                        internal val c1 = "A" // aakak

                        // comment
                        internal val c2 = "A" // aakak

                        /**
                         * comment
                         */
                        internal val c3 = "A" // aakak

                        ///////////////////////////////////////////////////////////////////////////
                        // Complex
                        ///////////////////////////////////////////////////////////////////////////

                        // Multiple Comment
                        val d1 /**ajdadajsd*/ = /**ajdadajsd*/ "A" // akaka

                        // Expression
                        val d2 = if (a2.length == 3) {
                            "1"
                        } else {
                            "2"
                        }

                        // Nullable Variable
                        var d3: String? = null

                        ///////////////////////////////////////////////////////////////////////////
                        // Comment Block
                        ///////////////////////////////////////////////////////////////////////////
                        var d4 = ""

                        // kotlin.NotImplementedError: An operation is not implemented: Not yet implemented
                        // Late Init
                    //    lateinit var d5: String

                        fun test() {
                            val a = ""
                            println(a)
                        }

                        companion object {
                            private val a1 = "A" // aakak

                            // comment
                            private val a2 = "A" // aakak

                            /**
                             * comment
                             */
                            private val a3 = "A" // aakak

                            val b1 = "adasa"

                            // comment
                            val b2 = "adasa"

                            /**
                             * comment
                             */
                            val b3 = "adasa"

                            internal val c1 = "A" // aakak

                            // comment
                            internal val c2 = "A" // aakak

                            /**
                             * comment
                             */
                            internal val c3 = "A" // aakak

                            // Complex

                            // Multiple Comment
                            val d1 /**ajdadajsd*/ = /**ajdadajsd*/ "A" // akaka

                            // Expression
                            val d2 = if (a2.length == 3) {
                                "1"
                            } else {
                                "2"
                            }

                            // Nullable Variable
                            var d3: String? = null

                            ///////////////////////////////////////////////////////////////////////////
                            // Comment Block
                            ///////////////////////////////////////////////////////////////////////////
                            var d4 = ""

                            // Late Init
                            lateinit var d5: String
                        }
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(PropertyWithExcludeFirstCommentDetector.ISSUE)
            .run()
            .expect(
                """
                    src/exclude_first_comment/SampleKotlin.kt:7: Warning: found inner type [SampleDetector]
                        private val a1 = "A" // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:10: Warning: found inner type [SampleDetector]
                        private val a2 = "A" // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:15: Warning: found inner type [SampleDetector]
                        private val a3 = "A" // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:17: Warning: found inner type [SampleDetector]
                        val b1 = "adasa"
                        ~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:20: Warning: found inner type [SampleDetector]
                        val b2 = "adasa"
                        ~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:25: Warning: found inner type [SampleDetector]
                        val b3 = "adasa"
                        ~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:27: Warning: found inner type [SampleDetector]
                        internal val c1 = "A" // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:30: Warning: found inner type [SampleDetector]
                        internal val c2 = "A" // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:35: Warning: found inner type [SampleDetector]
                        internal val c3 = "A" // aakak
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:42: Warning: found inner type [SampleDetector]
                        val d1 /**ajdadajsd*/ = /**ajdadajsd*/ "A" // akaka
                        ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:45: Warning: found inner type [SampleDetector]
                        val d2 = if (a2.length == 3) {
                        ^
                    src/exclude_first_comment/SampleKotlin.kt:52: Warning: found inner type [SampleDetector]
                        var d3: String? = null
                        ~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:57: Warning: found inner type [SampleDetector]
                        var d4 = ""
                        ~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:69: Warning: found inner type [SampleDetector]
                            private val a1 = "A" // aakak
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:72: Warning: found inner type [SampleDetector]
                            private val a2 = "A" // aakak
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:77: Warning: found inner type [SampleDetector]
                            private val a3 = "A" // aakak
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:79: Warning: found inner type [SampleDetector]
                            val b1 = "adasa"
                            ~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:82: Warning: found inner type [SampleDetector]
                            val b2 = "adasa"
                            ~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:87: Warning: found inner type [SampleDetector]
                            val b3 = "adasa"
                            ~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:89: Warning: found inner type [SampleDetector]
                            internal val c1 = "A" // aakak
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:92: Warning: found inner type [SampleDetector]
                            internal val c2 = "A" // aakak
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:97: Warning: found inner type [SampleDetector]
                            internal val c3 = "A" // aakak
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:102: Warning: found inner type [SampleDetector]
                            val d1 /**ajdadajsd*/ = /**ajdadajsd*/ "A" // akaka
                            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:105: Warning: found inner type [SampleDetector]
                            val d2 = if (a2.length == 3) {
                            ^
                    src/exclude_first_comment/SampleKotlin.kt:112: Warning: found inner type [SampleDetector]
                            var d3: String? = null
                            ~~~~~~~~~~~~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:117: Warning: found inner type [SampleDetector]
                            var d4 = ""
                            ~~~~~~~~~~~
                    src/exclude_first_comment/SampleKotlin.kt:120: Warning: found inner type [SampleDetector]
                            lateinit var d5: String
                            ~~~~~~~~~~~~~~~~~~~~~~~
                    0 errors, 27 warnings
                """.trimIndent()
            )
    }
}