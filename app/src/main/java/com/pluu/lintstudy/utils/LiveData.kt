package com.pluu.lintstudy.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

fun <T> LiveData<T>.observeNotNull(
    lifecycleOwner: LifecycleOwner,
    action: (T & Any) -> Unit
) {
    observe(lifecycleOwner) {
        if (it != null) {
            action(it)
        }
    }
}