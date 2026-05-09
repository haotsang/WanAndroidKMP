package com.haotsang.wanandroidkmp.model.bean

import kotlinx.serialization.Serializable


@Serializable
data class Article(
    val id: Int,
    val title: String,
    val chapterId: Int,
    val chapterName: String,
    val envelopePic: String,
    val link: String,
    val author: String,
    val origin: String,
    val publishTime: Long,
    val zan: Int,
    val desc: String,
    val visible: Int,
    val niceDate: String,
    val niceShareDate: String = "",
    val courseId: Int,
    val collect: Boolean = false,
    val apkLink: String = "",
    val projectLink: String = "",
    val superChapterId: Int = -1,
    val superChapterName: String? = null,
    val type: Int = -1,
    val fresh: Boolean = false,
    val audit: Int = 0,
    val prefix: String = "",
    val selfVisible: Int = 0,
    val shareDate: Long? = 0L,
    val shareUser: String = "",
    val userId: Int
)