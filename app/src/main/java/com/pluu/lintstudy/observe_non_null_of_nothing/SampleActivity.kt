package com.pluu.lintstudy.observe_non_null_of_nothing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluu.lintstudy.utils.observeNotNull

class SampleActivity : AppCompatActivity() {
    private val viewModel = SampleViewModel()

    private val sample: LiveData<Nothing> = MutableLiveData()

    private val sample2 = MutableLiveData<Nothing>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sample.observeNotNull(this) {
        }
        sample2.observeNotNull(this) {
        }

        viewModel.sample.observeNotNull(this) {
        }
        viewModel.sample2.observeNotNull(this) {
        }
    }
}