package com.haotsang.wanandroidkmp.ui.webview

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.haotsang.wanandroidkmp.ui.common.WanCenterAlignedTopAppBar
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewScreen(onBack: () -> Unit, url: String) {

    val scope = rememberCoroutineScope()
    var title by rememberSaveable(url) { mutableStateOf("详情") }

    val webViewState = rememberWebViewState(url)
    DisposableEffect(Unit) {
        val job = snapshotFlow { webViewState.pageTitle }.map {
            title = it ?: "详情"
        }.launchIn(scope)
        onDispose {
            job.cancel()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            WanCenterAlignedTopAppBar(
                title = title,
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(imageVector = Icons.Outlined.Close, contentDescription = "关闭")
                    }
                }
            )
        },
        content = { paddingValues ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(paddingValues)
            ) {
                WebView(
                    state = webViewState,
                    modifier = Modifier.fillMaxSize(),
                )
                if (webViewState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                        content = {
                            CircularProgressIndicator(modifier = Modifier.size(48.dp))
                        }
                    )
                }
            }
        }
    )
}