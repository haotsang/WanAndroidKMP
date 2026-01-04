package com.haotsang.wanandroidkmp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExpandedFullScreenSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SearchBarDefaults.inputFieldColors
import androidx.compose.material3.SearchBarScrollBehavior
import androidx.compose.material3.SearchBarValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopSearchBar
import androidx.compose.material3.rememberSearchBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.haotsang.wanandroidkmp.model.UiState
import com.haotsang.wanandroidkmp.model.bean.Article
import com.haotsang.wanandroidkmp.ui.common.ArticleItem
import com.haotsang.wanandroidkmp.ui.common.PagingFullLoadLayout
import com.haotsang.wanandroidkmp.ui.common.pagingFooter
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPanel(
    viewModel: SearchViewModel = koinViewModel(),
    scrollBehavior: SearchBarScrollBehavior,
    onNavigateToWebView: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
) {

    val textFieldState = rememberTextFieldState()
    val searchBarState = rememberSearchBarState()
    val scope = rememberCoroutineScope()

    val favoriteState by viewModel.favoriteState.collectAsState(null)
    val searchResults = viewModel.searchResults.collectAsState(initial = emptyFlow())
    val lazyPagingItems = searchResults.value.collectAsLazyPagingItems()

    // 输入框收起时，清空输入框
    val lastState = remember { mutableStateOf(searchBarState.currentValue) }
    LaunchedEffect(searchBarState.currentValue) {
        val previous = lastState.value
        val current = searchBarState.currentValue
        lastState.value = current

        if (previous == SearchBarValue.Expanded && current == SearchBarValue.Collapsed) {
            textFieldState.clearText()
            viewModel.updateSearchKeyword("")
        }
    }

    // 处理收藏状态变化
    LaunchedEffect(favoriteState) {
        if (favoriteState is UiState.Success) {
            lazyPagingItems.refresh()
        }
    }

    fun onCollapse() {
        textFieldState.clearText()
        viewModel.updateSearchKeyword("")
        // 收起输入框
        scope.launch { searchBarState.animateToCollapsed() }
    }

    val inputFieldColor = inputFieldColors()

    // 监听输入框内容变化
    LaunchedEffect(textFieldState.text.toString()) {
        viewModel.updateSearchKeyword(textFieldState.text.toString())
    }

    val inputField =
        @Composable {
            SearchBarDefaults.InputField(
                colors = inputFieldColor,
                modifier = Modifier,
                searchBarState = searchBarState,
                textFieldState = textFieldState,
                onSearch = { 
                    viewModel.search()
                },
                placeholder = {
                    Text(
                        text = "搜索...",
                        modifier = Modifier
                            .fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {
                        IconButton(onClick = {
                            onCollapse()
                        }) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    } else {
                        IconButton(onClick = {

                        }) {
                            Icon(Icons.Rounded.Search, contentDescription = null)
                        }
                    }
                },
                trailingIcon = {
                    if (searchBarState.currentValue == SearchBarValue.Expanded) {
                        IconButton(onClick = {
                            viewModel.search()
                        }) {
                            Icon(Icons.Rounded.Search, contentDescription = null)
                        }
                    } else {

                    }
                }
            )
        }

    TopSearchBar(
        state = searchBarState,
        inputField = inputField,
        scrollBehavior = scrollBehavior
    )
    ExpandedFullScreenSearchBar(state = searchBarState, inputField = inputField) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = {
                        onCollapse()
                    })
                }
        ) { innerPadding ->
            SearchScreen(
                innerPadding = innerPadding,
                viewModel = viewModel,
                searchResults = lazyPagingItems,
                onNavigateToWebView = onNavigateToWebView,
                onNavigateToLogin = onNavigateToLogin
            )
        }
    }

}

@Composable
fun SearchScreen(
    innerPadding: PaddingValues = PaddingValues(),
    viewModel: SearchViewModel,
    searchResults: LazyPagingItems<Article>,
    onNavigateToWebView: (String) -> Unit,
    onNavigateToLogin: () -> Unit
) {

    val hotkeys by viewModel.hotkeys.collectAsState(emptyList())
    val searchKeyword by viewModel.searchKeyword.collectAsState()

    Box(modifier = Modifier.fillMaxSize()
        .padding(innerPadding)
        .padding(top = 16.dp)
    ) {
        if (searchKeyword.isNotEmpty()) {
            // 显示搜索结果
            PagingFullLoadLayout(
                modifier = Modifier.fillMaxSize(),
                pagingState = searchResults
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = innerPadding
                ) {
                    items(
                        count = searchResults.itemCount,
                        key = searchResults.itemKey { it.id },
                        itemContent = { index ->
                            val article = searchResults[index]
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
                    pagingFooter(pagingState = searchResults)
                }
            }
        } else {
            // 显示热门搜索
            FlowRow(
                modifier = Modifier.fillMaxWidth().padding(innerPadding).padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp), // 水平间距
                verticalArrangement = Arrangement.spacedBy(12.dp),  // 垂直间距
                itemVerticalAlignment = Alignment.Top,
                maxItemsInEachRow = Int.MAX_VALUE, // 不限制每行数量（自动换行）
                maxLines = Int.MAX_VALUE
            ) {
                hotkeys.forEach {
                    Box(
                        modifier = Modifier.background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                            .height(40.dp)
                            .clickable {
                                viewModel.updateSearchKeyword(it.name)
                                viewModel.search()
                            }
                            .padding(start = 12.dp, end = 12.dp),
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

}


