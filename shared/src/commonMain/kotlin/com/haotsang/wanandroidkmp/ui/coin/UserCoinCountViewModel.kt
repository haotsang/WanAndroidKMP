package com.haotsang.wanandroidkmp.ui.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.bean.UserCoinCountData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class UserCoinCountViewModel(
    private val repository: Repository
) : ViewModel() {

    val userCoinCountList = repository.userCoinCountList().cachedIn(viewModelScope)

    private val mUserCoinCountState = MutableStateFlow(UserCoinCountData.Empty)
    val userCoinCountState = mUserCoinCountState.asStateFlow()

    fun userCoinCount() {
        viewModelScope.launch {
            repository.userCoinCount().catch {
                mUserCoinCountState.emit(UserCoinCountData.Empty)
            }.collectLatest {
                mUserCoinCountState.emit(it)
            }
        }
    }

}