package com.haotsang.wanandroidkmp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haotsang.wanandroidkmp.model.DefaultRepository
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.local.UserInfoHelper
import com.haotsang.wanandroidkmp.network.exception.DataResultException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: Repository
) : ViewModel() {


    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _isLoginSuccess = MutableStateFlow(false)
    val isLoginSuccess = _isLoginSuccess.asStateFlow()

    private val _usernameInput = MutableStateFlow("")
    val usernameInput = _usernameInput.asStateFlow()

    private val _passwordInput = MutableStateFlow("")
    val passwordInput = _passwordInput.asStateFlow()


    fun usernameUpdate(userName: String) {
        _usernameInput.update { userName }
    }

    fun passwordUpdate(password: String) {
        _passwordInput.update { password }
    }

    fun login() {
        viewModelScope.launch {
            repository.login(_usernameInput.value, _passwordInput.value).onStart {
                _isLoading.emit(true)
            }.catch {
                if (it is DataResultException) {
//                    mLoginState.emit(UiState.Failed(it.message))
                } else {
//                    mLoginState.emit(UiState.Exception(it))
                }
            }.collectLatest {
                if (it != null) {
                    _isLoginSuccess.value = true
                    UserInfoHelper.save(_usernameInput.value, password = _passwordInput.value)
                    _isLoading.emit(false)
                }


//                mLoginState.emit(UiState.Success(it))
            }
        }
    }


}