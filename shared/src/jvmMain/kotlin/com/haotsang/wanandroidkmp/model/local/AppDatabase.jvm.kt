package com.haotsang.wanandroidkmp.model.local

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    val baseDir = System.getenv("LOCALAPPDATA")?.takeIf { it.isNotBlank() }
        ?.let { File(it, "WanAndroidKMP") }
        ?: File(System.getProperty("user.home"), ".wanandroidkmp")
    baseDir.mkdirs()
    return Room.databaseBuilder<AppDatabase>(
        name = File(baseDir, "wan_android.db").absolutePath
    )
}
