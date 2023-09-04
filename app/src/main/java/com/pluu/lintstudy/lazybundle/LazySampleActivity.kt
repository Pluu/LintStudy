package com.pluu.lintstudy.lazybundle

import androidx.appcompat.app.AppCompatActivity

class LazySampleActivity : AppCompatActivity() {
    private val a = "1"
    private val index by inject<String>()
    private val i = index.toInt()
    private val i2 = index
    private val i3 by lazy {
        index.toInt()
    }
    private val d = Sample("A")
}

data class Sample(val a: String)
