package com.haotsang.wanandroidkmp.model.bean

import kotlinx.serialization.Serializable

@Serializable
data class UserInfoBean(
    val email: String,
    val icon: String,
    val id: Int,
    val password: String,
    val token: String,
    val type: Int,
    val coinCount: Int,
    val nickname: String,
    val username: String,
    val publicName: String,
)