package com.haotsang.wanandroidkmp.model.datasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.haotsang.wanandroidkmp.model.bean.CoinCountRankingBean
import com.haotsang.wanandroidkmp.model.local.CoinCountRankingDao

class CoinCountRankingLocalSource(
    private val dao: CoinCountRankingDao
) : PagingSource<Int, CoinCountRankingBean>() {

    override fun getRefreshKey(state: PagingState<Int, CoinCountRankingBean>): Int? {
        return state.anchorPosition?.let {
            val anchorPage = state.closestPageToPosition(it)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CoinCountRankingBean> {
        return try {
            val page = params.key ?: 1
            val allData = dao.getAll()

            // 计算分页数据
            val startIndex = (page - 1) * params.loadSize
            val endIndex = minOf(startIndex + params.loadSize, allData.size)
            val pageData = allData.subList(startIndex, endIndex)

            LoadResult.Page(
                data = pageData,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (endIndex < allData.size) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    private fun minOf(a: Int, b: Int): Int {
        return if (a < b) a else b
    }
}