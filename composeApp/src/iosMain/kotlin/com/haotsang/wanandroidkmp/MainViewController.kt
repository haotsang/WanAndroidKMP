package com.haotsang.wanandroidkmp

import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.window.ComposeUIViewController
import com.haotsang.wanandroidkmp.di.appModule
import com.haotsang.wanandroidkmp.di.viewModelModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun MainViewController(): UIViewController = ComposeUIViewController {
    startKoin {
        modules(appModule)
        modules(viewModelModule)
        allowOverride(false)
    }
    val windowSizeClass = calculateWindowSizeClass()
    val useNavRail = windowSizeClass.widthSizeClass > WindowWidthSizeClass.Compact

    App(useNavRail)
}