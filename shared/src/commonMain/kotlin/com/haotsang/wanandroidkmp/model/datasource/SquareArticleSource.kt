package com.haotsang.wanandroidkmp.model.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haotsang.wanandroidkmp.model.bean.Article
import com.haotsang.wanandroidkmp.network.PagingListData
import com.haotsang.wanandroidkmp.network.WanAndroidResponse.Companion.catchData
import com.haotsang.wanandroidkmp.network.dataResultBody
import com.haotsang.wanandroidkmp.network.httpClient
import io.ktor.client.request.get

class SquareArticleSource : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return runCatching {
            val page = params.key ?: 0

            val result = httpClient().get("user_article/list/$page/json").dataResultBody<PagingListData<Article>>().catchData
            val dataList = result?.dataList.orEmpty()
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (result?.dataList.isNullOrEmpty()) null else page + 1
            )
        }.getOrElse { LoadResult.Error(it) }
    }
}