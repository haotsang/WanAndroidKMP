package com.haotsang.wanandroidkmp.model.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.Transactor
import androidx.room.useWriterConnection
import com.haotsang.wanandroidkmp.model.bean.CoinCountRankingBean
import com.haotsang.wanandroidkmp.model.local.AppDatabase
import com.haotsang.wanandroidkmp.model.local.CoinCountRankingDao
import com.haotsang.wanandroidkmp.model.local.CoinRankRemoteKeys
import com.haotsang.wanandroidkmp.model.local.CoinRankRemoteKeysDao
import com.haotsang.wanandroidkmp.network.PagingListData
import com.haotsang.wanandroidkmp.network.WanAndroidResponse.Companion.catchData
import com.haotsang.wanandroidkmp.network.dataResultBody
import com.haotsang.wanandroidkmp.network.httpClient
import io.ktor.client.request.get

@OptIn(ExperimentalPagingApi::class)
class CoinCountRankingRemoteMediator(
    private val db: AppDatabase,
    private val dao: CoinCountRankingDao,
    private val keysDao: CoinRankRemoteKeysDao
) : RemoteMediator<Int, CoinCountRankingBean>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, CoinCountRankingBean>): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1

                LoadType.PREPEND -> {
                    val firstItem = state.firstItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    val keys = keysDao.remoteKeysById(firstItem.userId)
                    keys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    val keys = keysDao.remoteKeysById(lastItem.userId)
                    keys?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val result = httpClient().get("coin/rank/${page}/json")
                .dataResultBody<PagingListData<CoinCountRankingBean>>()
                .catchData

            val dataList = result?.dataList.orEmpty()
            
            // 正确判断是否到达分页末尾：当返回的数据数量小于请求的页面大小时
            val endReached = dataList.size < state.config.pageSize

            val keys = dataList.map {
                CoinRankRemoteKeys(
                    id = it.userId,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (endReached) null else page + 1
                )
            }

            db.useWriterConnection {
                it.withTransaction(Transactor.SQLiteTransactionType.IMMEDIATE) {
                    if (loadType == LoadType.REFRESH) {
                        keysDao.clearAll()
                        dao.clearAll()
                    }

                    keysDao.insertAll(keys)
                    dao.insertAll(dataList)
                }
            }

            return MediatorResult.Success(endOfPaginationReached = endReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}