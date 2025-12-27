package com.haotsang.wanandroidkmp

import androidx.compose.ui.window.ComposeUIViewController
import com.haotsang.wanandroidkmp.di.appModule
import com.haotsang.wanandroidkmp.di.viewModelModule
import org.koin.core.context.startKoin
import org.koin.dsl.module
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController = ComposeUIViewController {
    startKoin {
        modules(appModule)
        modules(viewModelModule)
        allowOverride(false)
    }
    App()
}