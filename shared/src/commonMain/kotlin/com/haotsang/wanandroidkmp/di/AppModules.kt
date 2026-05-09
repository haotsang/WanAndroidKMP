package com.haotsang.wanandroidkmp.di

import com.haotsang.wanandroidkmp.model.DefaultRepository
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.usecase.CancelFavoriteArticleUseCase
import com.haotsang.wanandroidkmp.model.usecase.CancelUserFavoriteArticleUseCase
import com.haotsang.wanandroidkmp.model.usecase.FavoriteArticleUseCase
import com.haotsang.wanandroidkmp.ui.home.HomeViewModel
import com.haotsang.wanandroidkmp.ui.login.LoginViewModel
import com.haotsang.wanandroidkmp.ui.rank.RankViewModel
import com.haotsang.wanandroidkmp.ui.search.SearchViewModel
import com.haotsang.wanandroidkmp.ui.wenda.WendaViewModel
import org.koin.dsl.module


val appModule = module {

    single<Repository> { DefaultRepository() }

    single { FavoriteArticleUseCase(get()) }
    single { CancelFavoriteArticleUseCase(get()) }
    single { CancelUserFavoriteArticleUseCase(get()) }

//    factory { HomeViewModel(get()) }
//
//    factory { LoginViewModel(get()) }
//
//    factory { WendaViewModel(get()) }
//
//    factory { RankViewModel(get()) }
//    factory { SearchViewModel(get()) }

}