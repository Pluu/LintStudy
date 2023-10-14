package com.pluu.lintstudy.observe_non_null_of_nothing.base

import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import com.pluu.lintstudy.utils.observeNotNull

open class BaseActivity : ComponentActivity() {
    protected fun <T> LiveData<T>.observeNotNull(
        action: (T & Any) -> Unit
    ) {
        observeNotNull(this@BaseActivity, action)
    }
}