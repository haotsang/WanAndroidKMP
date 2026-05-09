package com.haotsang.wanandroidkmp

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import java.util.prefs.Preferences

class DesktopPlatform : Platform {
    override val name: String = System.getProperty("os.name").orEmpty()
}

actual fun getPlatform(): Platform = DesktopPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(CIO) {
    config(this)
}

actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()

object DesktopKvStorage : IKvStorage {
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

actual fun getKvStorage(): IKvStorage = DesktopKvStorage
