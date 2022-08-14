package com.pluu.lintstudy.preference_key

import android.content.SharedPreferences
import androidx.core.content.edit
import java.util.prefs.Preferences

class SamplePreference_Kotlin(
    private val preferences: Preferences,
    private val sharedPref: SharedPreferences,
    private val aa: AA
) {
    private val KEY_INT_A = "KEY_INT_A"
    private val KEY_INT_B = "KEY_INT_"
    private val KEY_INT_ = "KEY_INT_C"

    fun test() {
        preferences.putInt("KEY_INT_A", 1)
        preferences.putInt("KEY_INT_", 1)
        preferences.putInt(AA.KEY_INT_A, 1)
        sharedPref.edit()
            .putBoolean(KEY_INT_A, true)
            .putBoolean(KEY_INT_B, true)
            .putBoolean(KEY_INT_, true)
            .putBoolean(KEY_STRING_A, true)
            .putBoolean(KEY_STRING_B, true)
            .putBoolean(KEY_STRING_, true)
            .commit()

        sharedPref.edit {
            putInt("KEY_INT_A", 1)
            putInt("KEY_INT_", 1)
        }
    }

    fun test2(key: String) {
        preferences.putInt(key, 1)
    }

    fun skip() {
        aa.putInt("string", 1)
        aa.getInt("string")
    }

    companion object {
        private val KEY_STRING_A = "KEY_INT_A"
        private val KEY_STRING_B = "KEY_INT_"
        private val KEY_STRING_ = "KEY_INT_C"
    }
}

class AA {
    fun putInt(key: String, value: Int) {}
    fun getInt(key: String) = 1

    companion object {
        const val KEY_INT_A = "KEY_INT_"
    }
}