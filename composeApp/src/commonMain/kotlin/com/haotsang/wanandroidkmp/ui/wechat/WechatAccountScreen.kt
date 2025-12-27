package com.haotsang.wanandroidkmp.ui.wechat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WechatAccountScreen(
    viewModel: WechatAccountViewModel = koinViewModel()
) {
    LaunchedEffect(viewModel) {
        viewModel.getWechatAccountList()
    }

    val wechatAccountState by viewModel.wechatAccountList.collectAsState()

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = innerPadding
        ) {


        }
    }

}