package com.haotsang.wanandroidkmp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Architecture
import androidx.compose.material.icons.rounded.Collections
import androidx.compose.material.icons.rounded.CurrencyBitcoin
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Minimize
import androidx.compose.material.icons.rounded.QuestionMark
import androidx.compose.material.icons.rounded.Radio
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Square
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface TopLevelRoute: NavKey {
    val title: String
}

@Serializable
data object Login : TopLevelRoute {
    override val title: String = "Login"
}

@Serializable
data object Register : TopLevelRoute {
    override val title: String = "Register"
}

@Serializable
data object Home : TopLevelRoute {
    override val title: String = "首页"
}

@Serializable
data object Square : TopLevelRoute {
    override val title: String = "广场"
}

@Serializable
data object UserCoin : TopLevelRoute {
    override val title: String = "我的积分"
}

@Serializable
data object Collections : TopLevelRoute {
    override val title: String = "我的收藏"
}

@Serializable
data object Setting : TopLevelRoute {
    override val title: String = "设置"
}

@Serializable
data object Rank : TopLevelRoute {
    override val title: String = "Rank"
}

@Serializable
data object Architecture : TopLevelRoute {
    override val title: String = "体系"
}

@Serializable
data object Wenda : TopLevelRoute {
    override val title: String = "问答"
}

@Serializable
data class ArchitectureDetail(val cid: Int) : TopLevelRoute {
    override val title: String = "ArchitectureDetail"
}

@Serializable
data object Profile : TopLevelRoute {
    override val title: String = "我的"
}

@Serializable
data class WebView(val url: String) : TopLevelRoute {
    override val title: String = "WebView"
}


// 创建扩展属性来获取实际的 ImageVector
val TopLevelRoute.icon: ImageVector
    get() = when(this) {
        is Home -> Icons.Rounded.Home
        is Square -> Icons.Rounded.Square
        is UserCoin -> Icons.Rounded.CurrencyBitcoin
        is Collections -> Icons.Rounded.Collections
        is Setting -> Icons.Rounded.Settings
        is Architecture -> Icons.Rounded.Architecture
        is Wenda -> Icons.Rounded.QuestionMark
        is Profile -> Icons.Rounded.Face
        else -> Icons.Rounded.Radio
    }

val NavConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(Login::class, Login.serializer())
            subclass(Register::class, Register.serializer())
            subclass(Rank::class, Rank.serializer())
            subclass(WebView::class, WebView.serializer())
            subclass(ArchitectureDetail::class, ArchitectureDetail.serializer())

            subclass(Home::class, Home.serializer())
            subclass(Square::class, Square.serializer())
            subclass(UserCoin::class, UserCoin.serializer())
            subclass(Collections::class, Collections.serializer())
            subclass(Architecture::class, Architecture.serializer())
            subclass(Wenda::class, Wenda.serializer())
            subclass(Profile::class, Profile.serializer())
            subclass(Setting::class, Setting.serializer())
        }
    }
}