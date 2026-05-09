package com.haotsang.wanandroidkmp.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T : Any> HttpResponse.dataResultBody(): WanAndroidResponse<T> =
    call.body<WanAndroidResponse<T>>()