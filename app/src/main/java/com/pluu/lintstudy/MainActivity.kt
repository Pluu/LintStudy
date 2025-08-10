package com.pluu.lintstudy

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.LocalContext
import com.pluu.lintstudy.inject.InjectSampleActivity
import com.pluu.sample.SampleLayout
import com.pluu.sample.ui.theme.SampleTheme
import com.pluu.sample.utils.startActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            SampleTheme {
                SampleLayout(title = "Sample") {
                    ListItem(title = "Inject") {
                        context.startActivity<InjectSampleActivity>()
                    }
                }
            }
        }
    }
}