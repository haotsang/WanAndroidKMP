package com.haotsang.wanandroidkmp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreen(
    viewModel: SearchViewModel = koinViewModel()
) {

    val hotkeys by viewModel.hotkeys.collectAsState(emptyList())

    Scaffold { innerPadding ->

        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(8.dp), // 水平间距
            verticalArrangement = Arrangement.spacedBy(6.dp),  // 垂直间距
            itemVerticalAlignment = Alignment.Top,
            maxItemsInEachRow = Int.MAX_VALUE, // 不限制每行数量（自动换行）
            maxLines = Int.MAX_VALUE
        ) {
            hotkeys.forEach {
                Box(
                    modifier = Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(6.dp))
                        .height(38.dp)
                        .clickable {
//                            viewModel.updateSearchText(it.name)
//                            viewModel.search(false)
                        }
                        .padding(start = 10.dp, end = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = it.name,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

}


