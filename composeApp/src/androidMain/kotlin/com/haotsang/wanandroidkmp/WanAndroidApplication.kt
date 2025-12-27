package com.haotsang.wanandroidkmp

import android.app.Application
import android.content.Context
import com.haotsang.wanandroidkmp.di.appModule
import com.haotsang.wanandroidkmp.di.viewModelModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.includes
import kotlin.properties.Delegates

class WanAndroidApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        CONTEXT = base
    }

    private fun initKoin() {
        stopKoin()
        startKoin {
            modules(appModule)
            modules(viewModelModule)
        }
    }

    companion object {
        var CONTEXT by Delegates.notNull<Context>()
            private set
    }
}