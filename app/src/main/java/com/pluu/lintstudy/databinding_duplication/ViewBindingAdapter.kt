package com.pluu.lintstudy.databinding_duplication

import android.view.View
import androidx.databinding.BindingAdapter

object ViewBindingAdapter {
    @JvmStatic
    @BindingAdapter("test1")
    fun setTest1(view: View, value: Int) {
        // TBD
    }

    @BindingAdapter("test2")
    fun setTest2(view: View, value: Int) {
        // TBD
    }
}

@BindingAdapter("test3")
fun setTest3(view: View, value: Int) {
    // TBD
}