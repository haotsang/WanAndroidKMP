package com.haotsang.wanandroidkmp.model.local

import androidx.room.RoomDatabase

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    error("Room database is not supported on JavaScript target.")
}
