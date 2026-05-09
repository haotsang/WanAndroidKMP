package com.haotsang.wanandroidkmp.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coin_rank_remote_keys")
data class CoinRankRemoteKeys(
    @PrimaryKey override val id: Long,
    override val prevKey: Int?,
    override val nextKey: Int?
) : RemoteKey
