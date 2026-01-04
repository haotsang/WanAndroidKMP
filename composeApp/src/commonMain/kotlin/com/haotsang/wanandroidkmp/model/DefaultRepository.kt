package com.haotsang.wanandroidkmp.model

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.haotsang.wanandroidkmp.model.bean.Article
import com.haotsang.wanandroidkmp.model.bean.BannerBean
import com.haotsang.wanandroidkmp.model.bean.ChapterList
import com.haotsang.wanandroidkmp.model.bean.CoinCountRankingBean
import com.haotsang.wanandroidkmp.model.bean.NullData
import com.haotsang.wanandroidkmp.model.bean.SearchBean
import com.haotsang.wanandroidkmp.model.bean.UserCoinCountData
import com.haotsang.wanandroidkmp.model.bean.UserCoinCountListData
import com.haotsang.wanandroidkmp.model.bean.UserFullInfoBean
import com.haotsang.wanandroidkmp.model.bean.UserInfoBean
import com.haotsang.wanandroidkmp.model.bean.WechatAccountSortData
import com.haotsang.wanandroidkmp.model.datasource.ArticleInSystemSource
import com.haotsang.wanandroidkmp.model.datasource.ArticleInWechatAccountSource
import com.haotsang.wanandroidkmp.model.datasource.CoinCountRankingLocalSource
import com.haotsang.wanandroidkmp.model.datasource.CoinCountRankingRemoteMediator
import com.haotsang.wanandroidkmp.model.datasource.CoinCountRankingSource
import com.haotsang.wanandroidkmp.model.datasource.HomeArticleSource
import com.haotsang.wanandroidkmp.model.datasource.SearchResultSource
import com.haotsang.wanandroidkmp.model.datasource.SquareArticleSource
import com.haotsang.wanandroidkmp.model.datasource.UserCoinCountListSource
import com.haotsang.wanandroidkmp.model.datasource.WendaArticleSource
import com.haotsang.wanandroidkmp.model.local.getRoomDatabase
import com.haotsang.wanandroidkmp.network.WanAndroidResponse.Companion.catchData
import com.haotsang.wanandroidkmp.network.dataResultBody
import com.haotsang.wanandroidkmp.network.httpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.parameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class DefaultRepository: Repository {

    override fun bannerList(): Flow<List<BannerBean>> {
        return flow {
            val result = httpClient().get("banner/json")
                .dataResultBody<List<BannerBean>>().catchData.orEmpty()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun userInfo(): Flow<UserFullInfoBean?> {
        return flow {
            val result =
                httpClient().get("user/lg/userinfo/json").dataResultBody<UserFullInfoBean>().catchData
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun hotkey(): Flow<List<SearchBean>> {
        return flow {
            val result = httpClient().get("/hotkey/json")
                .dataResultBody<List<SearchBean>>().catchData.orEmpty()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun searchResult(keyword: String): Flow<PagingData<Article>> {
        return Pager(config = PagingConfig(
            initialLoadSize = 10, pageSize = 20, prefetchDistance = 1
        ), pagingSourceFactory = {
            SearchResultSource(keyword)
        }).flow.flowOn(Dispatchers.IO)
    }

    override fun userCoinCount(): Flow<UserCoinCountData> {
        return flow {
            val result = httpClient().get("lg/coin/userinfo/json")
                .dataResultBody<UserCoinCountData>().catchData ?: UserCoinCountData.Empty
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun userCoinCountList(): Flow<PagingData<UserCoinCountListData>> {
        return Pager(config = PagingConfig(
            initialLoadSize = 10, pageSize = 20, prefetchDistance = 1
        ), pagingSourceFactory = {
            UserCoinCountListSource()
        }).flow.flowOn(Dispatchers.IO)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun coinCountRanking(): Flow<PagingData<CoinCountRankingBean>> {
        val db = getRoomDatabase()
        val dao = db.coinCountRankingDao()
        val keysDao = db.coinRankRemoteKeysDao()

        return Pager(
            config = PagingConfig(
                initialLoadSize = 10, pageSize = 20, prefetchDistance = 1
            ),
            remoteMediator = CoinCountRankingRemoteMediator(db, dao, keysDao),
            pagingSourceFactory = {
                CoinCountRankingLocalSource(dao)
            }
        ).flow.flowOn(Dispatchers.IO)
    }

    override fun homeArticles(): Flow<PagingData<Article>> {
        return Pager(config = PagingConfig(
            initialLoadSize = 10, pageSize = 20, prefetchDistance = 1
        ), pagingSourceFactory = {
            HomeArticleSource()
        }).flow.flowOn(Dispatchers.IO)
    }

    override fun squareArticles(): Flow<PagingData<Article>> {
        return Pager(config = PagingConfig(
            initialLoadSize = 10, pageSize = 20, prefetchDistance = 1
        ), pagingSourceFactory = {
            SquareArticleSource()
        }).flow.flowOn(Dispatchers.IO)
    }

    override fun wendaArticles(): Flow<PagingData<Article>> {
        return Pager(config = PagingConfig(
            initialLoadSize = 10, pageSize = 20, prefetchDistance = 1
        ), pagingSourceFactory = {
            WendaArticleSource()
        }).flow.flowOn(Dispatchers.IO)
    }

    override fun architectureTree(): Flow<List<ChapterList>> {
        return flow {
            val result = httpClient().get("tree/json")
                .dataResultBody<List<ChapterList>>().catchData.orEmpty()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun architectureDetail(cid: Int): Flow<PagingData<Article>> {
        return Pager(config = PagingConfig(
            initialLoadSize = 10, pageSize = 20, prefetchDistance = 1
        ), pagingSourceFactory = {
            ArticleInSystemSource(cid)
        }).flow.flowOn(Dispatchers.IO)
    }

    override fun wechatAccountSort(): Flow<List<WechatAccountSortData>> {
        return flow {
            val result = httpClient().get("wxarticle/chapters/json")
                .dataResultBody<List<WechatAccountSortData>>().catchData.orEmpty()
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


    override fun articleInWechatAccount(id: Int): Flow<PagingData<Article>> {
        return Pager(config = PagingConfig(
            initialLoadSize = 10, pageSize = 20, prefetchDistance = 1
        ), pagingSourceFactory = {
            ArticleInWechatAccountSource(id)
        }).flow.flowOn(Dispatchers.IO)
    }


    override fun login(username: String, password: String): Flow<UserInfoBean?> {
        return flow {
            val result = httpClient().submitForm(
                "/user/login",
                parameters {
                    append("username", username)
                    append("password", password)
                }
            ).dataResultBody<UserInfoBean>().catchData
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun register(
        username: String, password: String, rePassword: String
    ): Flow<UserInfoBean?> {
        return flow {
            val result = httpClient().submitForm(
                "/user/register",
                parameters {
                    append("username", username)
                    append("password", password)
                    append("repassword", rePassword)
                }
            ).dataResultBody<UserInfoBean>().catchData
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun logout(): Flow<NullData?> {
        return flow {
            val result = httpClient().get("/user/logout/json").dataResultBody<NullData>().catchData
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun favoriteArticle(id: Int): Flow<String?> {
        return flow {
            val result = httpClient().submitForm("lg/collect/$id/json")
                .dataResultBody<String>().catchData
            emit(result)
        }.flowOn(Dispatchers.IO)
    }

    override fun cancelFavoriteArticle(id: Int): Flow<String?> {
        return flow {
            val result = httpClient().post("lg/uncollect_originId/$id/json")
                .dataResultBody<String>().catchData
            emit(result)
        }.flowOn(Dispatchers.IO)
    }


}