package com.haotsang.wanandroidkmp.util

actual object HtmlParser {
    actual fun fromHtml(source: String): String {
        return source.replace(Regex("<[^>]+>"), "")
            .replace(Regex("\\s+"), " ")
            .trim()
    }
}
