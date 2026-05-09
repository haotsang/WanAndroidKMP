package com.haotsang.wanandroidkmp.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.flow.flowOf

@Stable
fun <T : Any> emptyPagingFlow() = flowOf(PagingData.from(emptyList<T>()))

@Composable
fun <T : Any> collectAsLazyEmptyPagingItems() = emptyPagingFlow<T>().collectAsLazyPagingItems()