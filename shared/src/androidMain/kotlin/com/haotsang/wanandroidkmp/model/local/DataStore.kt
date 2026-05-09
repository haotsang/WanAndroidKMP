package com.haotsang.wanandroidkmp.model.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.haotsang.wanandroidkmp.AndroidAppContext

fun createDataStore(): DataStore<Preferences> = createDataStore(
    producePath = { AndroidAppContext.appContext.filesDir.resolve(dataStoreFileName).absolutePath }
)
