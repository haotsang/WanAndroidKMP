package com.haotsang.wanandroidkmp.util

import android.text.Html

actual object HtmlParser {
    actual fun fromHtml(source: String): String {
        return Html.fromHtml(source, Html.FROM_HTML_MODE_COMPACT).toString()
    }

}