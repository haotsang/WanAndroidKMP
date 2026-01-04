package com.haotsang.wanandroidkmp.model.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haotsang.wanandroidkmp.model.bean.Article
import com.haotsang.wanandroidkmp.model.bean.CoinCountRankingBean
import com.haotsang.wanandroidkmp.network.PagingListData
import com.haotsang.wanandroidkmp.network.WanAndroidResponse
import com.haotsang.wanandroidkmp.network.WanAndroidResponse.Companion.catchData
import com.haotsang.wanandroidkmp.network.dataResultBody
import com.haotsang.wanandroidkmp.network.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlin.collections.orEmpty

class SearchResultSource(private val keyword: String) : PagingSource<Int, Article>() {

    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        return runCatching {
            val page = params.key ?: 0

            val result = httpClient().post("/article/query/$page/json") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(FormDataContent(Parameters.build {
                    append("k", keyword) // 搜索关键词参数
                }))
            }.dataResultBody<PagingListData<Article>>().catchData

            val dataList = result?.dataList.orEmpty()
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (dataList.size < params.loadSize) null else page + 1
            )
        }.getOrElse { LoadResult.Error(it) }

    }
}
