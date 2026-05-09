package com.haotsang.wanandroidkmp

import android.content.Context
import android.os.Build
import androidx.core.content.edit
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(OkHttp) {
    config(this)
}

actual fun getCurrentTimeMillis(): Long {
    return System.currentTimeMillis()
}

object AndroidKVStorage : IKvStorage {
    private val sharedPreferences by lazy {
        AndroidAppContext.appContext.getSharedPreferences("wanAndroidApp", Context.MODE_PRIVATE)
    }

    override fun saveString(key: String, value: String) {
        sharedPreferences.edit { putString(key, value) }
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return sharedPreferences.getString(key, defaultValue)
    }

}

actual fun getKvStorage(): IKvStorage {
    return AndroidKVStorage
}
