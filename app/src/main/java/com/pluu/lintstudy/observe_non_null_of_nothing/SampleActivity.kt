package com.pluu.lintstudy.observe_non_null_of_nothing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluu.lintstudy.observe_non_null_of_nothing.base.BaseActivity
import com.pluu.lintstudy.utils.observeNotNull

class SampleActivity : BaseActivity() {
    private val sample: LiveData<String> = MutableLiveData<String>()
    private val sample2 = MutableLiveData<String>()

    private val sample3: LiveData<Nothing> = MutableLiveData<Nothing>()
    private val sample4 = MutableLiveData<Nothing>()

    fun test() {
        sample.observeNotNull(this) {}
        sample2.observeNotNull(this) {}
        sample3.observeNotNull(this) {}
        sample4.observeNotNull(this) {}

        sample.observeNotNull {}
        sample2.observeNotNull {}
        sample3.observeNotNull {}
        sample4.observeNotNull {}
    }
}