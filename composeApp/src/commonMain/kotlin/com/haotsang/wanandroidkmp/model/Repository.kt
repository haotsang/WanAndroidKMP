package com.haotsang.wanandroidkmp.model

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
import kotlinx.coroutines.flow.Flow

interface Repository {

    fun bannerList(): Flow<List<BannerBean>>

    fun wendaArticles(): Flow<PagingData<Article>>

    fun architectureTree(): Flow<List<ChapterList>>

    fun architectureDetail(cid: Int): Flow<PagingData<Article>>


    fun homeArticles(): Flow<PagingData<Article>>

    fun squareArticles(): Flow<PagingData<Article>>

    fun hotkey(): Flow<List<SearchBean>>

    /**
     * 搜索结果
     *
     * @param keyword 关键词
     * @return
     */
    fun searchResult(keyword: String): Flow<PagingData<Article>>

    fun userInfo(): Flow<UserFullInfoBean?>

    fun login(username: String, password: String): Flow<UserInfoBean?>

    fun register(username: String, password: String, rePassword: String): Flow<UserInfoBean?>

    fun logout(): Flow<NullData?>

    /**
     * 用户总积分
     *
     * @return
     */
    fun userCoinCount(): Flow<UserCoinCountData>

    /**
     * 用户积分记录
     *
     * @return
     */
    fun userCoinCountList(): Flow<PagingData<UserCoinCountListData>>

    /**
     * 积分排行榜
     *
     * @return
     */
    fun coinCountRanking(): Flow<PagingData<CoinCountRankingBean>>


    /**
     * 公众号列表
     *
     * @return
     */
    fun wechatAccountSort(): Flow<List<WechatAccountSortData>>

    /**
     * 某个公众号的文章
     *
     * @param id 公众号 id
     * @return
     */
    fun articleInWechatAccount(id: Int): Flow<PagingData<Article>>

    /**
     * 收藏文章
     *
     * @param id
     * @return
     */
    fun favoriteArticle(id: Int): Flow<String?>

    /**
     * 取消收藏
     *
     * @param id
     * @return
     */
    fun cancelFavoriteArticle(id: Int): Flow<String?>
}