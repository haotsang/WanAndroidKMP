package com.haotsang.wanandroidkmp.model.bean

import androidx.compose.runtime.Stable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Stable
@Serializable
@Entity(tableName = "coin_count_ranking")
data class CoinCountRankingBean(
    @SerialName("coinCount")
    val coinCount: Int,
    @SerialName("level")
    val level: Int,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("rank")
    val rank: String,
    @PrimaryKey
    @SerialName("userId")
    val userId: Long,
    @SerialName("username")
    val username: String
) {
    val rankDesc
        get() = when (rank) {
            "1" -> "\uD83E\uDD47"
            "2" -> "\uD83E\uDD48"
            "3" -> "\uD83E\uDD49"
            else -> rank
        }
}