package com.pluu.lintstudy.oncreate_super_call

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pluu.lintstudy.R

class SampleActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // A
        // B
        // C
        val a = 10 % 2
        if (check()) {
            finish()
        }
        abcd()
        super.onCreate(savedInstanceState)
        finish()
    }

    private fun check(): Boolean = false

    private fun abcd() {}
}

class SampleActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_LintStudy)
        super.onCreate(savedInstanceState)
        finish()
    }
}