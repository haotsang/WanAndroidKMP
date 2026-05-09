package com.haotsang.wanandroidkmp.util

expect object HtmlParser {
    fun fromHtml(source: String): String
}