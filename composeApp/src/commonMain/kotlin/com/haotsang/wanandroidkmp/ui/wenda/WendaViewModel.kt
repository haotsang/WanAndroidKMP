package com.haotsang.wanandroidkmp.ui.wenda

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.haotsang.wanandroidkmp.model.DefaultRepository
import com.haotsang.wanandroidkmp.model.Repository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onEach

class WendaViewModel(
    private val repository: Repository
) : ViewModel() {

    val wendaArticles = repository.homeArticles().onEach { delay(100) }.cachedIn(viewModelScope)

}