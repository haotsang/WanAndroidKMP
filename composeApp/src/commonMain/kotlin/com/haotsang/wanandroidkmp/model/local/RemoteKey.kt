package com.haotsang.wanandroidkmp.model.local

interface RemoteKey {
    val id: Long
    val prevKey: Int?
    val nextKey: Int?
}
