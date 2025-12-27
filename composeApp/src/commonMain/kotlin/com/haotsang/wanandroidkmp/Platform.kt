package com.haotsang.wanandroidkmp

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient

expect fun getCurrentTimeMillis(): Long

expect fun htmlUnescape(input: String): String

interface IKvStorage {
    fun saveString(key: String, value: String)
    fun getString(key: String, defaultValue: String? = null): String?
}

expect fun getKvStorage(): IKvStorage
