package com.haotsang.wanandroidkmp.model.bean

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Stable
@Serializable
data class CoinInfoBean(
    @SerialName("coinCount")
    val coinCount: Int,
    @SerialName("level")
    val level: Int,
    @SerialName("nickname")
    val nickName: String,
    @SerialName("rank")
    val rank: String,
    @SerialName("userId")
    val userId: Int,
    @SerialName("username")
    val userName: String,
)
