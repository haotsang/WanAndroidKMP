package com.haotsang.wanandroidkmp.model.bean

import kotlinx.serialization.Serializable

@Serializable
data class BannerBean(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)