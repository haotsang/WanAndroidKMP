package com.haotsang.wanandroidkmp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Architecture
import androidx.compose.material.icons.rounded.Collections
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Minimize
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Square
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface Route: NavKey {
    val icon: ImageVector
    val title: String
}

@Serializable
data object Login : NavKey

@Serializable
data object Home : Route {
    override val icon: ImageVector = Icons.Rounded.Home
    override val title: String = "首页"
}

@Serializable
data object Search : NavKey

@Serializable
data object Square : Route {
    override val icon: ImageVector = Icons.Rounded.Square
    override val title: String = "广场"
}

@Serializable
data object UserCoin : Route {
    override val icon: ImageVector = Icons.Rounded.CurrencyBitcoin
    override val title: String = "我的积分"
}

@Serializable
data object Collections : Route {
    override val icon: ImageVector = Icons.Rounded.Collections
    override val title: String = "我的收藏"
}

@Serializable
data object Setting : Route {
    override val icon: ImageVector = Icons.Rounded.Settings
    override val title: String = "设置"
}

@Serializable
data object Rank : NavKey

@Serializable
data object Architecture : Route {
    override val icon: ImageVector = Icons.Rounded.Architecture
    override val title: String = "体系"
}

@Serializable
data object Wenda : Route {
    override val icon: ImageVector = Icons.Rounded.QuestionMark
    override val title: String = "问答"
}

@Serializable
data class ArchitectureDetail(val cid: Int) : NavKey

@Serializable
data object Profile : Route {
    override val icon: ImageVector = Icons.Rounded.Minimize
    override val title: String = "我的"
}

@Serializable
data class WebView(val url: String) : NavKey


val NavConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Login::class, Login.serializer())
            subclass(Home::class, Home.serializer())
            subclass(Search::class, Search.serializer())
            subclass(Square::class, Square.serializer())
            subclass(Profile::class, Profile.serializer())
            subclass(WebView::class, WebView.serializer())
            subclass(Setting::class, Setting.serializer())
        }
    }
}