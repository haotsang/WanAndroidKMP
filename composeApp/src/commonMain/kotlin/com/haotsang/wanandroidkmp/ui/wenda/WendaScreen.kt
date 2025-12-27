package com.haotsang.wanandroidkmp.ui.wenda

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.haotsang.wanandroidkmp.ui.common.pagingFooter
import com.haotsang.wanandroidkmp.ui.home.ArticleItem
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WendaScreen(viewModel: WendaViewModel = koinViewModel()) {

    val articleState = viewModel.wendaArticles.collectAsLazyPagingItems()

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = innerPadding
        ) {
            items(
                count = articleState.itemCount,
                key = articleState.itemKey { it.id },
                itemContent = { index ->
                    val item = articleState[index]
                    if (item != null) {
                        ArticleItem(article = item, onClickArticle = {

                        })
                    }
                }
            )
            pagingFooter(pagingState = articleState)
        }
    }
}