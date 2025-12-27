package com.haotsang.wanandroidkmp.model.bean

import kotlinx.serialization.Serializable
@Serializable
data class SearchBean(
    val id: Int,
    val link: String,
    val name: String,
    val order: Int,
    val visible: Int
)