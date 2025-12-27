package com.haotsang.wanandroidkmp.model.local


import com.haotsang.wanandroidkmp.getKvStorage
import io.ktor.util.decodeBase64String
import io.ktor.util.encodeBase64

object UserInfoHelper {

    const val loginUserName = "loginUserName"
    const val loginUserPassword = "loginUserPassword"

    fun save(username: String, password: String) {
        kotlin.runCatching {
            getKvStorage().saveString(loginUserName, username.encodeBase64())
            getKvStorage().saveString(loginUserPassword, password.encodeBase64())
        }.onFailure {
            println("UserInfoHelper: save failed, cause=${it.cause}")
        }
    }

    fun getUsername(): String {
        val username = getKvStorage().getString(loginUserName)?.decodeBase64String() ?: ""
        println("UserInfoHelper: getUsername, username=$username")
        return username
    }

    fun getPassword(): String {
        val pwd = getKvStorage().getString(loginUserPassword)?.decodeBase64String() ?: ""
        println("UserInfoHelper: getPassword, pwd=$pwd")
        return pwd
    }

    fun clear() {
        kotlin.runCatching {
            getKvStorage().saveString(loginUserName, "")
            getKvStorage().saveString(loginUserPassword, "")
        }.onFailure {
            println("UserInfoHelper: save failed, cause=${it.cause}")
        }
    }

}