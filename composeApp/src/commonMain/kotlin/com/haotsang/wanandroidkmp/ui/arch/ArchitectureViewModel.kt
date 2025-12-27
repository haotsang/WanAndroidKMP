package com.haotsang.wanandroidkmp.ui.arch

import androidx.lifecycle.ViewModel
import com.haotsang.wanandroidkmp.model.DefaultRepository
import com.haotsang.wanandroidkmp.model.Repository
import kotlinx.coroutines.flow.catch

class ArchitectureViewModel : ViewModel() {

    val repository: Repository = DefaultRepository()

    val architectureTree = repository.architectureTree().catch { emit(emptyList()) }

}