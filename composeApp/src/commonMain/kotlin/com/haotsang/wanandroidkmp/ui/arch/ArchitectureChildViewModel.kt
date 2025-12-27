package com.haotsang.wanandroidkmp.ui.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.haotsang.wanandroidkmp.model.DefaultRepository
import com.haotsang.wanandroidkmp.model.Repository

class ArchitectureChildViewModel : ViewModel() {

    val repository: Repository = DefaultRepository()

    fun articleState(cid: Int) = repository.architectureDetail(cid).cachedIn(viewModelScope)

}