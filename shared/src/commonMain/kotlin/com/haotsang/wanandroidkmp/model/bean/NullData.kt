package com.haotsang.wanandroidkmp.model.bean

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Stable
@Serializable
data class NullData(
    val message: String? = "",
)