package com.haotsang.wanandroidkmp.di

import com.haotsang.wanandroidkmp.ui.coin.UserCoinCountViewModel
import com.haotsang.wanandroidkmp.ui.home.HomeViewModel
import com.haotsang.wanandroidkmp.ui.login.LoginViewModel
import com.haotsang.wanandroidkmp.ui.profile.ProfileViewModel
import com.haotsang.wanandroidkmp.ui.rank.RankViewModel
import com.haotsang.wanandroidkmp.ui.search.SearchViewModel
import com.haotsang.wanandroidkmp.ui.wechat.WechatAccountViewModel
import com.haotsang.wanandroidkmp.ui.wenda.WendaViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::WendaViewModel)
    viewModelOf(::RankViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::WechatAccountViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::UserCoinCountViewModel)
}