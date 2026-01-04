package com.haotsang.wanandroidkmp.model.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CoinRankRemoteKeysDao {

    @Query("SELECT * FROM coin_rank_remote_keys WHERE id = :id")
    suspend fun remoteKeysById(id: Long): CoinRankRemoteKeys?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(keys: List<CoinRankRemoteKeys>)

    @Query("DELETE FROM coin_rank_remote_keys")
    suspend fun clearAll()
}
