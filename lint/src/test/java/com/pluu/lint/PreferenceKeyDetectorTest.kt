package com.pluu.lint

import com.android.tools.lint.checks.infrastructure.TestFiles.java
import com.android.tools.lint.checks.infrastructure.TestFiles.kotlin
import com.android.tools.lint.checks.infrastructure.TestLintTask.lint
import com.pluu.lint.stubs.Stubs
import org.junit.Test

@Suppress("UnstableApiUsage")
class PreferenceKeyDetectorTest {

    @Test
    fun testJava_Success() {
        lint().files(
            java(
                """
                    import android.content.SharedPreferences;
                    
                    import java.util.prefs.Preferences;
                    
                    public class Test {
                        private final Preferences preferences;
                        private final SharedPreferences sharedPref;
                    
                        private final String KEY_INT_A = "KEY_INT_A";
                        private final String KEY_STRING_A = "KEY_STRING_A";
                    
                        public Test(Preferences preferences, SharedPreferences sharedPref) {
                            this.preferences = preferences;
                            this.sharedPref = sharedPref;
                        }
                    
                        public void test() {
                            preferences.putInt("KEY_INT_A", 1);                    
                            sharedPref.edit()
                                    .putInt(KEY_INT_A, 1)
                                    .putString(KEY_STRING_A, "true")
                                    .commit();
                        }
                    
                        public void test2(String key) {
                            preferences.putInt(key, 1);
                        }
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(PreferencesKeyDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun testJava_Fail() {
        lint().files(
            java(
                """
                    import android.content.SharedPreferences;
                    
                    import java.util.prefs.Preferences;
                    
                    public class Test {
                        private final Preferences preferences;
                        private final SharedPreferences sharedPref;
                    
                        private final String KEY_INT_A = "KEY_INT_";
                        private final String KEY_INT_ = "KEY_INT_B";
                        private final String KEY_STRING_A = "KEY_STRING_";
                        private final String KEY_STRING_ = "KEY_STRING_B";
                    
                        public Test(Preferences preferences, SharedPreferences sharedPref) {
                            this.preferences = preferences;
                            this.sharedPref = sharedPref;
                        }
                    
                        public void test() {
                            preferences.putInt("KEY_INT_", 1);
                    
                            sharedPref.edit()
                                    .putInt(KEY_INT_A, 1)
                                    .putInt(KEY_INT_, 1)
                                    .putString(KEY_STRING_A, "true")
                                    .putString(KEY_STRING_, "true")
                                    .commit();
                        }
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(PreferencesKeyDetector.ISSUE)
            .run()
            .expect(
                """
                    src/Test.java:20: Warning: 실제값이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                            preferences.putInt("KEY_INT_", 1);
                                        ~~~~~~
                    src/Test.java:23: Warning: 실제값이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                    .putInt(KEY_INT_A, 1)
                                     ~~~~~~
                    src/Test.java:24: Warning: 변수명이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                    .putInt(KEY_INT_, 1)
                                     ~~~~~~
                    src/Test.java:25: Warning: 실제값이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                    .putString(KEY_STRING_A, "true")
                                     ~~~~~~~~~
                    src/Test.java:26: Warning: 변수명이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                    .putString(KEY_STRING_, "true")
                                     ~~~~~~~~~
                    0 errors, 5 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testKotlin_Success() {
        lint().files(
            Stubs.SharedPreferences_edit,
            kotlin(
                """
                    import android.content.SharedPreferences
                    import androidx.core.content.edit
                    import java.util.prefs.Preferences
                    
                    class Test(
                        private val preferences: Preferences,
                        private val sharedPref: SharedPreferences
                    ) {
                        private val KEY_INT_A = "KEY_INT_A"
                    
                        fun test() {
                            preferences.putInt("KEY_INT_A", 1)
                            sharedPref.edit()
                                .putInt(KEY_INT_A, 1)
                                .putString(KEY_STRING_A, "true")
                                .commit()
                    
                            sharedPref.edit {
                                putInt("KEY_INT_A", 1)
                            }
                        }
                    
                        fun test2(key: String) {
                            preferences.putInt(key, 1)
                        }
                    
                        companion object {
                            private val KEY_STRING_A = "KEY_INT_A"
                        }
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(PreferencesKeyDetector.ISSUE)
            .run()
            .expectClean()
    }

    @Test
    fun testKotlin_Fail() {
        lint().files(
            Stubs.SharedPreferences_edit,
            kotlin(
                """import android.content.SharedPreferences
                    import androidx.core.content.edit
                    import java.util.prefs.Preferences
                    
                    class Test(
                        private val preferences: Preferences,
                        private val sharedPref: SharedPreferences
                    ) {
                        private val KEY_INT_A = "KEY_INT_"
                        private val KEY_INT_ = "KEY_INT_B"
                    
                        fun test() {
                            preferences.putInt("KEY_INT_", 1)
                            sharedPref.edit()
                                .putInt(KEY_INT_A, 1)
                                .putInt(KEY_INT_, 2)
                                .putString(KEY_STRING_A, "true")
                                .putString(KEY_STRING_, "true")
                                .commit()
                    
                            sharedPref.edit {
                                putInt("KEY_INT_", 1)
                            }
                        }
                    
                        companion object {
                            private val KEY_STRING_A = "KEY_INT_"
                            private val KEY_STRING_ = "KEY_INT_B"
                        }
                    }""".trimIndent()
            ).indented()
        )
            .issues(PreferencesKeyDetector.ISSUE)
            .run()
            .expect(
                """
                    src/Test.kt:13: Warning: 실제값이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                                preferences.putInt("KEY_INT_", 1)
                                                            ~~~~~~
                    src/Test.kt:15: Warning: 실제값이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                                    .putInt(KEY_INT_A, 1)
                                                     ~~~~~~
                    src/Test.kt:16: Warning: 변수명이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                                    .putInt(KEY_INT_, 2)
                                                     ~~~~~~
                    src/Test.kt:17: Warning: 실제값이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                                    .putString(KEY_STRING_A, "true")
                                                     ~~~~~~~~~
                    src/Test.kt:18: Warning: 변수명이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                                    .putString(KEY_STRING_, "true")
                                                     ~~~~~~~~~
                    src/Test.kt:22: Warning: 실제값이 'KEY_[TYPE]_'형태로 시작해야함 [PreferencesKeyDetector]
                                                    putInt("KEY_INT_", 1)
                                                    ~~~~~~
                    0 errors, 6 warnings
                """.trimIndent()
            )
    }

    @Test
    fun testKotlin_Skip() {
        lint().files(
            kotlin(
                """                    
                    class Test(
                        private val aa: AA
                    ) {
                        fun skip() {
                            aa.putInt("string", 1)
                            aa.getInt("string")
                        }
                    }
                    
                    class AA {
                        fun putInt(key: String, value: Int) {}
                        fun getInt(key: String) = 1

                        companion object {
                            const val KEY_INT_A = "KEY_INT_"
                        }
                    }
                """.trimIndent()
            ).indented()
        )
            .issues(PreferencesKeyDetector.ISSUE)
            .run()
            .expectClean()
    }
}