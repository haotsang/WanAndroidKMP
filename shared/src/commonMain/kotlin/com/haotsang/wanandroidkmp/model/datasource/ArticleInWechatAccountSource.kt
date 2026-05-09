package com.haotsang.wanandroidkmp.model.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haotsang.wanandroidkmp.model.bean.Article
import com.haotsang.wanandroidkmp.network.PagingListData
import com.haotsang.wanandroidkmp.network.WanAndroidResponse.Companion.catchData
import com.haotsang.wanandroidkmp.network.dataResultBody
import com.haotsang.wanandroidkmp.network.httpClient
import io.ktor.client.request.get

class ArticleInWechatAccountSource(
    private val id: Int,
) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {
            val anchorPosition = state.closestPageToPosition(it)
            return anchorPosition?.prevKey?.plus(1) ?: anchorPosition?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return runCatching {
            val page = params.key ?: 1
            val url = "wxarticle/list/$id/$page/json"
            val result = httpClient().get(url).dataResultBody<PagingListData<Article>>()
            val dataList = result.catchData?.dataList.orEmpty()
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (dataList.size < params.loadSize) null else page + 1
            )
        }.getOrElse { LoadResult.Error(it) }
    }
}