package com.haotsang.wanandroidkmp.ui.square

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
fun SquareScreen(
    viewModel: SquareViewModel = koinViewModel(),
    onNavigateToWebView: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val articles = viewModel.squareArticles().collectAsLazyPagingItems()

    Scaffold(topBar = {
        WanCenterAlignedTopAppBar(title = "广场", scrollBehavior = scrollBehavior)
    }, modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) { innerPadding ->
        PagingFullLoadLayout(
            modifier = Modifier.fillMaxSize(), pagingState = articles
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = innerPadding
            ) {
                items(
                    count = articles.itemCount,
                    key = articles.itemKey { it.id },
                    itemContent = { index ->
                        val article = articles[index]
                        if (article != null) {
                            ArticleItem(
                                article = article,
                                onClickArticle = onNavigateToWebView,
                                onFavoriteClick = { favoriteState ->
                                    if (favoriteState) {
                                        viewModel.favoriteArticle(article.id)
                                    } else {
                                        viewModel.cancelFavoriteArticle(article.id)
                                    }
                                }
                            )
                        }
                    }
                )
                pagingFooter(pagingState = articles)
            }
        }
    }
}