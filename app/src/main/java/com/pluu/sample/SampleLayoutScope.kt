package com.pluu.sample

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluu.sample.ui.theme.SampleTheme

@SampleLayoutScopeMarker
interface SampleCustomLayoutScope {
    // 여기에 커스텀 레이아웃 콘텐츠 내에서 접근 가능한 속성 또는 함수를 추가할 수 있습니다.
    @Composable
    fun ListItem(
        title: String,
        modifier: Modifier = Modifier,
        subTitle: String = "",
        onClick: () -> Unit = {}
    )
}

private class SampleCustomLayoutScopeImpl : SampleCustomLayoutScope {
    @Composable
    override fun ListItem(
        title: String,
        modifier: Modifier,
        subTitle: String,
        onClick: () -> Unit
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(64.dp)
                .clickable(onClick = onClick)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (subTitle.isNotEmpty()) {
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = subTitle,
                        color = Color.DarkGray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleLayout(
    modifier: Modifier = Modifier,
    title: String = "",
    onBack: (() -> Unit)? = null,
    content: @Composable SampleCustomLayoutScope.() -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    onBack?.let {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // 콘텐츠 람다에 스코프 인스턴스 제공
            val scope = SampleCustomLayoutScopeImpl()
            scope.content()
        }
    }

}

@Preview(showBackground = true)
@Composable
fun SampleLayoutPreview() {
    SampleTheme {
        SampleLayout(
            title = "Sample",
            onBack = {}
        ) {
            ListItem(
                title = "Title"
            )
            ListItem(
                title = "Title",
                subTitle = "Sub title",
            )
        }
    }
}