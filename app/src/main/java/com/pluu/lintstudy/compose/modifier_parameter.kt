package com.pluu.lintstudy.compose

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SampleSuccess(text: String, modifier: Modifier) {
    BasicText(text = text, modifier = modifier)
}

@Composable
fun SampleFailure(text: String) {
    BasicText(text = text)
}

@Composable
private fun Sample3(text: String) {
    BasicText(text = text)
}

@Preview
@Composable
fun SampleFailure4() {

}