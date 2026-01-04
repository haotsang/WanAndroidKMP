package com.haotsang.wanandroidkmp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil3.compose.AsyncImage
import com.haotsang.wanandroidkmp.model.UiState.Companion.isLoginExpired
import com.haotsang.wanandroidkmp.model.UiStateSuccess
import com.haotsang.wanandroidkmp.model.bean.BannerBean
import com.haotsang.wanandroidkmp.ui.common.ArticleItem
import com.haotsang.wanandroidkmp.ui.common.PagingFullLoadLayout
import com.haotsang.wanandroidkmp.ui.common.pagingFooter
import com.haotsang.wanandroidkmp.ui.search.SearchPanel
import kotlinx.coroutines.delay
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onNavigateToWebView: (String) -> Unit,
) {

    val bannerList by viewModel.banners.collectAsState(emptyList())

    val articleState = viewModel.articles.collectAsLazyPagingItems()

    val favoriteState by viewModel.favoriteState.collectAsState(null)

    LaunchedEffect(favoriteState) {
        if (favoriteState != null) {
            if (favoriteState!!.isLoginExpired) {
                onNavigateToLogin()
            }

            if (favoriteState is UiStateSuccess) {
                articleState.refresh()
            }
        }
    }

    val scrollBehavior = SearchBarDefaults.enterAlwaysSearchBarScrollBehavior()

    Scaffold(topBar = {
        SearchPanel(
            scrollBehavior = scrollBehavior,
            onNavigateToWebView = onNavigateToWebView,
            onNavigateToLogin = onNavigateToLogin
        )
    }, modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)) { innerPadding ->
        PagingFullLoadLayout(
            modifier = Modifier.fillMaxSize(), pagingState = articleState
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = innerPadding
            ) {
                item {
                    AutoScrollBanner(
                        bannerList = bannerList,
                        onNavigateToWebView = onNavigateToWebView
                    )
                }

                items(
                    count = articleState.itemCount,
                    key = articleState.itemKey { it.id },
                    itemContent = { index ->
                        val item = articleState[index]
                        if (item != null) {
                            ArticleItem(
                                article = item,
                                onClickArticle = onNavigateToWebView,
                                onFavoriteClick = { favoriteState ->
                                    if (favoriteState) {
                                        viewModel.favoriteArticle(item.id)
                                    } else {
                                        viewModel.cancelFavoriteArticle(item.id)
                                    }
                                }
                            )
                        }
                    }
                )
                pagingFooter(pagingState = articleState)
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AutoScrollBanner(
    modifier: Modifier = Modifier.fillMaxWidth().height(200.dp),
    bannerList: List<BannerBean>,
    autoScroll: Boolean = true,
    autoPlayInterval: Long = 5000L, // 轮播间隔（毫秒）
    indicatorColor: Color = Color.LightGray, // 指示器颜色
    activeIndicatorColor: Color = Color.DarkGray, // 高亮指示器颜色
    onNavigateToWebView: (String) -> Unit,
) {
    if (bannerList.isEmpty()) {
        return
    }

    val virtualCount = Int.MAX_VALUE
    val actualCount = bannerList.size
    val initialIndex = virtualCount / 2
    val pagerState = rememberPagerState(initialPage = initialIndex, pageCount = { virtualCount })

    // 自动轮播逻辑
    LaunchedEffect(autoScroll) {
        while (autoScroll) {
            delay(autoPlayInterval)
            pagerState.animateScrollToPage(pagerState.currentPage + 1)
        }
    }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            pageSpacing = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) { index ->
            val actualIndex = (index - initialIndex).floorMod(actualCount)
            AsyncImage(
                model = bannerList[actualIndex].imagePath,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
                    .clickable { onNavigateToWebView(bannerList[actualIndex].url) }
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Crop
            )
        }

        // 底部指示器
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(actualCount) { i ->
                val isActive = (pagerState.currentPage - initialIndex).floorMod(actualCount) == i
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(if (isActive) activeIndicatorColor else indicatorColor)
                )
            }
        }
    }

}
// 辅助函数：取模运算
fun Int.floorMod(other: Int): Int = when (other) {
    0 -> this
    else -> this - floorDiv(other) * other
}