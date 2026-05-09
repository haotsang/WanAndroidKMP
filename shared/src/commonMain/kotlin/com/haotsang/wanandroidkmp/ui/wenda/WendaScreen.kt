package com.haotsang.wanandroidkmp.ui.wenda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.haotsang.wanandroidkmp.ui.common.WanCenterAlignedTopAppBar
import com.haotsang.wanandroidkmp.ui.common.pagingFooter
import com.haotsang.wanandroidkmp.ui.common.ArticleItem
import com.haotsang.wanandroidkmp.ui.common.PagingFullLoadLayout
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WendaScreen(
    viewModel: WendaViewModel = koinViewModel(),
    onNavigateToWebView: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val articleState = viewModel.wendaArticles.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            WanCenterAlignedTopAppBar(title = "问答", scrollBehavior = scrollBehavior)
        }, modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        PagingFullLoadLayout(
            modifier = Modifier.fillMaxSize(), pagingState = articleState
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = innerPadding
            ) {
                items(
                    count = articleState.itemCount,
                    key = articleState.itemKey { it.id },
                    itemContent = { index ->
                        val item = articleState[index]
                        if (item != null) {
                            ArticleItem(article = item, onClickArticle = onNavigateToWebView, onFavoriteClick = {
                                if (it) {
                                    viewModel.favoriteArticle(item.id)
                                } else {
                                    viewModel.cancelFavoriteArticle(item.id)
                                }
                            })
                        }
                    }
                )
                pagingFooter(pagingState = articleState)
            }
        }
    }
}