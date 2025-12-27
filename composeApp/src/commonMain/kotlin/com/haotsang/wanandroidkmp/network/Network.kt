package com.haotsang.wanandroidkmp.network

import com.haotsang.wanandroidkmp.httpClient
import com.haotsang.wanandroidkmp.model.local.UserInfoHelper
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.cookie
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


fun httpClient() = Network.httpClient


object Network {
    val httpClient = httpClient {
        expectSuccess = true
        engine {
            pipelining = true
        }
        defaultRequest {
            url("https://www.wanandroid.com")

            cookie(UserInfoHelper.loginUserName, UserInfoHelper.getUsername())
            cookie(UserInfoHelper.loginUserPassword, UserInfoHelper.getPassword())
        }
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 3000
            connectTimeoutMillis = 3000
            socketTimeoutMillis = 3000
        }
        install(ContentNegotiation) {
            json(json = Json {
                coerceInputValues = true
                encodeDefaults = true
                isLenient = true
                allowSpecialFloatingPointValues = true
                allowStructuredMapKeys = true
                prettyPrint = false
                useArrayPolymorphism = false
                ignoreUnknownKeys = true
            })
        }
    }

}