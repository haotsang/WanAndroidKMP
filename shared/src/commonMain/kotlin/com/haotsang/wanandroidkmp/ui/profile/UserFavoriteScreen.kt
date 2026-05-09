package com.haotsang.wanandroidkmp.ui.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.haotsang.wanandroidkmp.model.bean.UserFavoriteArticleData
import com.haotsang.wanandroidkmp.ui.common.PagingFullLoadLayout
import com.haotsang.wanandroidkmp.ui.common.WanTopAppBar
import com.haotsang.wanandroidkmp.ui.common.pagingFooter
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserFavoriteScreen(
    viewModel: UserFavoriteViewModel = koinViewModel(),
    onBack: () -> Unit,
    onNavigateToWebView: (String) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            WanTopAppBar("我的收藏", onBackClick = onBack)
        },
        content = { innerPadding ->
            val favoriteArticlesState = viewModel.favoriteArticles.collectAsLazyPagingItems()
            val favoriteState by viewModel.favoriteState.collectAsState(null)

            var isFavoriteAction by remember { mutableStateOf(false) }


            favoriteState?.let {
                LaunchedEffect(it) {
//                    if (it.isLoginExpired) {
//                        val result = navigator.navigateForResult(
//                            Router.Login.path,
//                            NavOptions(launchSingleTop = true)
//                        )
//                        if (result == true) {
//                            favoriteArticlesState.refresh()
//                        }
//                    }
//                    if (favoriteState is UiStateSuccess) {
//                        isFavoriteAction = true
//                        favoriteArticlesState.refresh()
//                    }
                }
            }

            PagingFullLoadLayout(
                modifier = Modifier.fillMaxSize(),
                pagingState = favoriteArticlesState,
                content = {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                            .padding(horizontal = 16.dp),
                        contentPadding = innerPadding,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(
                            favoriteArticlesState.itemCount,
                            key = favoriteArticlesState.itemKey { it.id }
                        ) { index ->
                            val data = favoriteArticlesState[index]
                            if (data != null) {
                                FavoriteArticleItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    data = data,
                                    onClick = {
                                        onNavigateToWebView(data.link ?: "")
                                    }, onFavoriteClick = {
                                        viewModel.cancelFavoriteArticle(
                                            id = data.id,
                                            originId = data.originId
                                        )
                                    }
                                )
                            }
                        }
                        pagingFooter(pagingState = favoriteArticlesState)
                    }
                },
            )
        },
    )
}


@Composable
private fun FavoriteArticleItem(
    modifier: Modifier = Modifier,
    data: UserFavoriteArticleData,
    onFavoriteClick: UserFavoriteArticleData.(Boolean) -> Unit = {},
    onLongClick: (UserFavoriteArticleData) -> Unit = {},
    onClick: (UserFavoriteArticleData) -> Unit = {}
) {
    val favoriteState by rememberSaveable(data.id) { mutableStateOf(true) }
    Column(
        modifier = modifier.fillMaxWidth().clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.large)
            .combinedClickable(onLongClick = { onLongClick.invoke(data) },
                onClick = { onClick.invoke(data) }).padding(12.dp).clipToBounds(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = data.title,
            maxLines = 2,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Bold
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = data.author.orEmpty(),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    modifier = Modifier.align(Alignment.Start)
                )
                Text(
                    text = data.niceDate.orEmpty(),
                    color = MaterialTheme.colorScheme.outline,
                    fontSize = MaterialTheme.typography.titleSmall.fontSize,
                    modifier = Modifier.align(Alignment.Start),
                )
            }
            IconToggleButton(
                checked = favoriteState,
                onCheckedChange = {
                    onFavoriteClick.invoke(data, it)
                },
                content = {
                    Icon(
                        imageVector = if (favoriteState) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                },
            )
        }

    }
}
