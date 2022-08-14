package com.pluu.lintstudy.preference_key;

import android.content.SharedPreferences;

import java.util.prefs.Preferences;

@SuppressWarnings("FieldCanBeLocal")
public class SamplePreference_Java {
    private final Preferences preferences;
    private final SharedPreferences sharedPref;
    private final AA aa;

    private final String KEY_INT_A = "KEY_INT_A";
    private final String KEY_INT_B = "KEY_INT_";
    private final String KEY_INT_ = "KEY_INT_C";
    private final String KEY_STRING_A = "KEY_STRING_A";
    private final String KEY_STRING_B = "KEY_STRING_";
    private final String KEY_STRING_ = "KEY_STRING_C";

    public SamplePreference_Java(Preferences preferences, SharedPreferences sharedPref, AA aa) {
        this.preferences = preferences;
        this.sharedPref = sharedPref;
        this.aa = aa;
    }

    public void test() {
        preferences.putInt("KEY_INT_A", 1);
        preferences.putInt("KEY_INT_", 1);
        preferences.putInt(AA.KEY_INT_A, 1);

        sharedPref.edit()
                .putBoolean(KEY_INT_A, true)
                .putBoolean(KEY_INT_B, true)
                .putBoolean(KEY_INT_, true)
                .putBoolean(KEY_STRING_A, true)
                .putBoolean(KEY_STRING_B, true)
                .putBoolean(KEY_STRING_, true)
                .commit();

        aa.putInt("string", 1);
    }

    public void test2(String key) {
        preferences.putInt(key, 1);
    }
}
