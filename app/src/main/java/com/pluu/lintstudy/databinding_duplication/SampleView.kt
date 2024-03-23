package com.pluu.lintstudy.databinding_duplication

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.databinding.BindingAdapter

class SampleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    companion object {
        @JvmStatic
        @BindingAdapter("android:paddingLeft")
        fun setPaddingLeft(view: View, padding: Int) {
            // TBD
        }

        @BindingAdapter("android:paddingStart")
        fun setPaddingStart(view: SampleView, padding: Int) {
            // TBD
        }
    }
}

