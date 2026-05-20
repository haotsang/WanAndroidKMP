package com.haotsang.wanandroidkmp

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import java.util.prefs.Preferences

class JvmPlatform : Platform {
    override val name: String = System.getProperty("os.name").orEmpty()
}

actual fun getPlatform(): Platform = JvmPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(OkHttp) {
    config(this)
}

actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

object JvmKvStorage : IKvStorage {
    private val preferences: Preferences by lazy {
        Preferences.userRoot().node("com/haotsang/wanandroidkmp")
    }

    override fun saveString(key: String, value: String) {
        preferences.put(key, value)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return preferences.get(key, defaultValue)
    }
}

actual fun getKvStorage(): IKvStorage = JvmKvStorage
