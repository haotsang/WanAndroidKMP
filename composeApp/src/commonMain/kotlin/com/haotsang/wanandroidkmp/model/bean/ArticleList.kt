package com.haotsang.wanandroidkmp.model.bean

import kotlinx.serialization.Serializable

@Serializable
data class ArticleList(
    val offset: Int = 0,
    val size: Int = 0,
    val total: Int = 0,
    val pageCount: Int = 0,
    val curPage: Int = 0,
    val over: Boolean = false,
    val datas: List<Article>? = null
)