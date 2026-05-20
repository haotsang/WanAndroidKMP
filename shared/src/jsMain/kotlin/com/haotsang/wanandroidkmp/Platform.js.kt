package com.haotsang.wanandroidkmp

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js
import kotlinx.datetime.Clock

class JsPlatform : Platform {
    override val name: String = "JavaScript"
}

actual fun getPlatform(): Platform = JsPlatform()

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(Js) {
    config(this)
}

actual fun getCurrentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()

object JsKvStorage : IKvStorage {
    private val values = mutableMapOf<String, String>()

    override fun saveString(key: String, value: String) {
        values[key] = value
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return values[key] ?: defaultValue
    }
}

actual fun getKvStorage(): IKvStorage = JsKvStorage
