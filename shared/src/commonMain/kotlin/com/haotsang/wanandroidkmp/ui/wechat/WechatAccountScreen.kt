package com.haotsang.wanandroidkmp.ui.wechat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.haotsang.wanandroidkmp.model.UiState
import com.haotsang.wanandroidkmp.ui.common.WanCenterAlignedTopAppBar

import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WechatAccountScreen(
    viewModel: WechatAccountViewModel = koinViewModel(),
    onNavigateToWechatArticleList: (Int, String) -> Unit
) {
    LaunchedEffect(viewModel) {
        viewModel.getWechatAccountList()
    }

    val wechatAccountState by viewModel.wechatAccountList.collectAsState()

    Scaffold(
        topBar = {
            WanCenterAlignedTopAppBar(title = "微信公众号")
        }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = it
        ) {
            when (wechatAccountState) {
                is UiState.Loading -> {
                    item {
                        CircularProgressIndicator(
                            modifier = Modifier.fillMaxSize()
                                .padding(16.dp)
                        )
                    }
                }
                is UiState.Exception -> {
                    item {
                        Text(
                            text = (wechatAccountState as UiState.Exception).throwable.message ?: "加载失败",
                            modifier = Modifier.fillMaxSize()
                                .padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                is UiState.Success -> {
                    val data = (wechatAccountState as UiState.Success).data
                    items(data.size) {
                        Card(
                            modifier = Modifier.fillMaxWidth()
                                .clickable {
                                    onNavigateToWechatArticleList(data[it].id, data[it].name)
                                },
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = data[it].name,
                                modifier = Modifier.padding(16.dp),
                                fontSize = 16.sp,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
                            )
                        }
                    }
                }
                is UiState.Failed -> {
                    item {
                        Text(
                            text = (wechatAccountState as UiState.Failed).message,
                            modifier = Modifier.fillMaxSize()
                                .padding(16.dp),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}