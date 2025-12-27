package com.haotsang.wanandroidkmp.network.exception

import okio.IOException

class DataResultException(val code: Int, override val message: String) : IOException(message)