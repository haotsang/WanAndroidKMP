package com.haotsang.wanandroidkmp.model.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.haotsang.wanandroidkmp.WanAndroidApplication

fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = { WanAndroidApplication.CONTEXT.filesDir.resolve(dataStoreFileName).absolutePath }
)