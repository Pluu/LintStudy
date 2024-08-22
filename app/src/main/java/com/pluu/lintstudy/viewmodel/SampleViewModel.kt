package com.pluu.lintstudy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

private class SampleViewModel : ViewModel() {

    private val ceh = CoroutineExceptionHandler { coroutineContext, throwable ->
    }

    init {
        viewModelScope.launch { }

        viewModelScope.launch(CoroutineExceptionHandler { c, t ->
        }) { }

        viewModelScope.launch(ceh) { }

        viewModelScope.launchLoading { }

        viewModelScope.launchLoading(CoroutineExceptionHandler { c, t ->
        }) { }

        viewModelScope.launchLoading(ceh) { }

        viewModelScope.launchLoading(EmptyCoroutineContext) { }
    }
}

fun CoroutineScope.launchLoading(
    coroutineContext: CoroutineContext = EmptyCoroutineContext,
    block: () -> Unit
) = launch(coroutineContext) {
    block()
}