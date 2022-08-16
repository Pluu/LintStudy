package com.pluu.lintstudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private class SampleViewModel : ViewModel() {
    init {
        viewModelScope.launch {

        }
    }
}