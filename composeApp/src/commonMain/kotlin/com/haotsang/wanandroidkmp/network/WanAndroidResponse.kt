package com.haotsang.wanandroidkmp.network

import com.haotsang.wanandroidkmp.network.exception.DataResultException
import com.haotsang.wanandroidkmp.network.exception.LoginExpiredException
import kotlinx.serialization.Serializable

@Serializable
data class WanAndroidResponse<out T>(val errorCode: Int, val errorMsg: String, val data: T? = null) {

    companion object {
        // 需要登录的状态值
        const val NoLogin = -1001

        // 数据成功状态
        const val Success = 0

        inline val <T : Any> WanAndroidResponse<T>.isSuccess get() = errorCode == Success
        inline val <T : Any> WanAndroidResponse<T>.isLoginExpired get() = errorCode == NoLogin

        inline val <T : Any> WanAndroidResponse<T>.catchData
            get() = if (isSuccess) {
                data
            } else {
                if (isLoginExpired) {
                    throw LoginExpiredException(errorCode, errorMsg)
                }
                throw DataResultException(errorCode, errorMsg)
            }
    }
}