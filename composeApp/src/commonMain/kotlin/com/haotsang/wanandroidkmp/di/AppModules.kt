package com.haotsang.wanandroidkmp.di

import com.haotsang.wanandroidkmp.model.DefaultRepository
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.ui.home.HomeViewModel
import com.haotsang.wanandroidkmp.ui.login.LoginViewModel
import com.haotsang.wanandroidkmp.ui.rank.RankViewModel
import com.haotsang.wanandroidkmp.ui.search.SearchViewModel
import com.haotsang.wanandroidkmp.ui.wenda.WendaViewModel
import org.koin.dsl.module


val appModule = module {

    single<Repository> { DefaultRepository() }

//    factory { HomeViewModel(get()) }
//
//    factory { LoginViewModel(get()) }
//
//    factory { WendaViewModel(get()) }
//
//    factory { RankViewModel(get()) }
//    factory { SearchViewModel(get()) }

}