package com.haotsang.wanandroidkmp.util

fun String.stripToPlainText(): String {
    return this
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&amp;", "&")
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
        .replace("&nbsp;", " ")
        .replace(Regex("`+"), "") // 去除代码标记
}