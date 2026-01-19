package com.haotsang.wanandroidkmp.ui.arch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.UiState
import com.haotsang.wanandroidkmp.model.bean.ChapterList
import com.haotsang.wanandroidkmp.network.exception.DataResultException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ArchitectureViewModel(
    private val repository: Repository
) : ViewModel() {

//    val architectureTree = repository.architectureTree().catch { emit(emptyList()) }

    private val _architectureTree = MutableStateFlow<UiState<List<ChapterList>>>(UiState.Loading)
    val architectureTree = _architectureTree.asStateFlow()

    init {
        refreshData()
    }

    fun refreshData() {
        viewModelScope.launch {
            repository.architectureTree().onStart {
                _architectureTree.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    _architectureTree.emit(UiState.Failed(it.message))
                } else {
                    _architectureTree.emit(UiState.Exception(it))
                }
            }.collectLatest {
                _architectureTree.emit(UiState.Success(it))
            }
        }
    }




}