package com.pluu.lintstudy.observe_non_null_of_nothing

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SampleViewModel : ViewModel() {
    private val _sample = MutableLiveData<Nothing>()
    val sample: LiveData<Nothing> get() = _sample

    private val _sample2 = MutableLiveData<String?>()
    val sample2: LiveData<String?> get() = _sample2
}