package com.haotsang.wanandroidkmp.model.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haotsang.wanandroidkmp.model.bean.UserFavoriteArticleData
import com.haotsang.wanandroidkmp.network.PagingListData
import com.haotsang.wanandroidkmp.network.WanAndroidResponse.Companion.catchData
import com.haotsang.wanandroidkmp.network.dataResultBody
import com.haotsang.wanandroidkmp.network.httpClient
import io.ktor.client.request.get

class UserFavoriteArticlesSource : PagingSource<Int, UserFavoriteArticleData>() {

    override fun getRefreshKey(state: PagingState<Int, UserFavoriteArticleData>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UserFavoriteArticleData> {
        return runCatching {
            val page = params.key ?: 0
            val result = httpClient().get("lg/collect/list/$page/json")
                .dataResultBody<PagingListData<UserFavoriteArticleData>>().catchData
            val dataList = result?.dataList.orEmpty()
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (dataList.size < params.loadSize) null else page + 1
            )
        }.getOrElse { LoadResult.Error(it) }
    }
}