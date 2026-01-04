package com.haotsang.wanandroidkmp.model.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.haotsang.wanandroidkmp.model.bean.CoinCountRankingBean

@Dao
interface CoinCountRankingDao {
    @Query("SELECT * FROM coin_count_ranking ORDER BY coinCount DESC")
    suspend fun getAll(): List<CoinCountRankingBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<CoinCountRankingBean>)

    @Query("DELETE FROM coin_count_ranking")
    suspend fun clearAll()
}