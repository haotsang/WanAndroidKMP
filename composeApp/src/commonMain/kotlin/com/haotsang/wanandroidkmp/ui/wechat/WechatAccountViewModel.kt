package com.haotsang.wanandroidkmp.ui.wechat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.UiState
import com.haotsang.wanandroidkmp.model.bean.WechatAccountSortData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class WechatAccountViewModel(private val repository: Repository) : ViewModel() {
    private val mWechatAccountList =
        MutableStateFlow<UiState<List<WechatAccountSortData>>>(UiState.Loading)
    val wechatAccountList = mWechatAccountList.asStateFlow()

    private val mSelectIndex = MutableStateFlow(0)
    val selectedIndex = mSelectIndex.asStateFlow()

    fun updateSelected(index: Int) {
        viewModelScope.launch {
            mSelectIndex.update { index }
        }
    }

    fun getWechatAccountList() {
        viewModelScope.launch {
            repository.wechatAccountSort().catch {
                mWechatAccountList.emit(UiState.Exception(it))
            }.collectLatest {
                mWechatAccountList.emit(UiState.Success(it))
            }
        }
    }
}
