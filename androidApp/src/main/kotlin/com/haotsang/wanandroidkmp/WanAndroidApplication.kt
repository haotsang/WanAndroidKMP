package com.haotsang.wanandroidkmp

import android.app.Application
import com.haotsang.wanandroidkmp.di.appModule
import com.haotsang.wanandroidkmp.di.viewModelModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class WanAndroidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidAppContext.init(this)
        initKoin()
    }

    private fun initKoin() {
        stopKoin()
        startKoin {
            modules(appModule)
            modules(viewModelModule)
        }
    }
}
