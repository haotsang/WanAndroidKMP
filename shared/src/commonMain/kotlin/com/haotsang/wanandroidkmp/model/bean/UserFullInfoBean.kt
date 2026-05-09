package com.haotsang.wanandroidkmp.model.bean

import androidx.compose.runtime.Stable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class UserFullInfoBean(
    @SerialName("coinInfo")
    val coinInfoData: CoinInfoBean?,
    @SerialName("userInfo")
    val userInfoData: UserInfoBean?,
)
