package com.haotsang.wanandroidkmp.model.local

import androidx.room.Room
import androidx.room.RoomDatabase
import com.haotsang.wanandroidkmp.AndroidAppContext

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val appContext = AndroidAppContext.appContext
    val dbFile = appContext.getDatabasePath("wan_android.db")
    return Room.databaseBuilder<AppDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
}
