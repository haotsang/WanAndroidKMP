package com.haotsang.wanandroidkmp.ui.rank

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.DefaultRepository


class RankViewModel(private val service: Repository) : ViewModel() {



    val coinCountRankingState = service.coinCountRanking().cachedIn(viewModelScope)


}