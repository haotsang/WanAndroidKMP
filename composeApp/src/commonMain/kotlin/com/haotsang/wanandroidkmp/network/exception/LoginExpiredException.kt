package com.haotsang.wanandroidkmp.network.exception

import okio.IOException

class LoginExpiredException(val code: Int, override val message: String) : IOException(message)