package com.haotsang.wanandroidkmp.ui.search

import androidx.lifecycle.ViewModel
import com.haotsang.wanandroidkmp.model.DefaultRepository
import com.haotsang.wanandroidkmp.model.Repository
import kotlinx.coroutines.flow.catch

class SearchViewModel(
    private val repository: Repository
) : ViewModel() {

    val hotkeys = repository.hotkey().catch { emit(emptyList()) }

}