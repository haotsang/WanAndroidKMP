package com.haotsang.wanandroidkmp.model.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haotsang.wanandroidkmp.model.bean.CoinCountRankingBean
import com.haotsang.wanandroidkmp.network.PagingListData
import com.haotsang.wanandroidkmp.network.WanAndroidResponse.Companion.catchData
import com.haotsang.wanandroidkmp.network.dataResultBody
import com.haotsang.wanandroidkmp.network.httpClient
import io.ktor.client.request.get

class CoinCountRankingSource : PagingSource<Int, CoinCountRankingBean>() {

    override fun getRefreshKey(state: PagingState<Int, CoinCountRankingBean>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinCountRankingBean> {
        return runCatching {
            val page = params.key ?: 1

            val result = httpClient().get("coin/rank/${page}/json").dataResultBody<PagingListData<CoinCountRankingBean>>().catchData
            val dataList = result?.dataList.orEmpty()
            LoadResult.Page(
                data = dataList,
                prevKey = null,
                nextKey = if (dataList.size < params.loadSize) null else page + 1
            )
        }.getOrElse { LoadResult.Error(it) }
    }
}