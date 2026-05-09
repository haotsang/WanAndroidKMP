package com.haotsang.wanandroidkmp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.UiState
import com.haotsang.wanandroidkmp.model.bean.UserFullInfoBean
import com.haotsang.wanandroidkmp.model.bean.UserInfoBean
import com.haotsang.wanandroidkmp.model.local.UserInfoHelper
import com.haotsang.wanandroidkmp.network.exception.DataResultException
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: Repository
) : ViewModel() {

    private val _userInfoState = MutableStateFlow<UiState<UserFullInfoBean?>>(UiState.Loading)
    val userInfoState = _userInfoState.asStateFlow()

    private val _logoutState = MutableSharedFlow<UiState<Boolean>>()
    val logoutState = _logoutState.asSharedFlow()

    fun logout() {
        viewModelScope.launch {
            repository.logout().onStart {
                _logoutState.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    _logoutState.emit(UiState.Failed(it.message))
                } else {
                    _logoutState.emit(UiState.Exception(it))
                }
            }.collectLatest {
                UserInfoHelper.clear()
                _logoutState.emit(UiState.Success(true))
            }
        }
    }


    fun userInfo() {
        viewModelScope.launch {
            repository.userInfo().onStart {
                _userInfoState.emit(UiState.Loading)
            }.catch {
                if (it is DataResultException) {
                    _userInfoState.emit(UiState.Failed(it.message))
                } else {
                    _userInfoState.emit(UiState.Exception(it))
                }
            }.collectLatest {
                _userInfoState.emit(UiState.Success(it))
            }
        }
    }
}