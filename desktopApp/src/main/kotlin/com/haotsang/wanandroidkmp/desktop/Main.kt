package com.haotsang.wanandroidkmp.desktop

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.haotsang.wanandroidkmp.App
import com.haotsang.wanandroidkmp.di.appModule
import com.haotsang.wanandroidkmp.di.viewModelModule
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

fun main() = application {
    initKoinIfNeeded()

    val windowState = rememberWindowState(width = 1280.dp, height = 800.dp)
    Window(
        onCloseRequest = ::exitApplication,
        title = "WanAndroidKMP",
        state = windowState,
    ) {
        App(useNavRail = windowState.size.width >= 840.dp)
    }
}

private fun initKoinIfNeeded() {
    if (GlobalContext.getOrNull() != null) return
    startKoin {
        modules(appModule)
        modules(viewModelModule)
    }
}
