package com.pluu.lintstudy.observe_non_null_of_nothing

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.pluu.lintstudy.utils.observeNotNull

class SampleActivity : AppCompatActivity() {
    private val sample: LiveData<Nothing> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sample.observeNotNull(this) {
        }
    }
}