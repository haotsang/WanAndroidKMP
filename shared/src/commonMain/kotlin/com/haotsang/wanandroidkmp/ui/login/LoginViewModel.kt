package com.haotsang.wanandroidkmp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.UiState
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

    private val _usernameInput = MutableStateFlow("")
    val usernameInput = _usernameInput.asStateFlow()

    private val _passwordInput = MutableStateFlow("")
    val passwordInput = _passwordInput.asStateFlow()

    private val _rePasswordInput = MutableStateFlow("")
    val rePasswordInput = _rePasswordInput.asStateFlow()

    private val _loginState = MutableStateFlow<UiState<Boolean>?>(null)
    val loginState = _loginState.asStateFlow()

    private val _registerState = MutableStateFlow<UiState<Boolean>?>(null)
    val registerState = _registerState.asStateFlow()

    fun usernameUpdate(userName: String) {
        _usernameInput.update { userName }
    }

    fun passwordUpdate(password: String) {
        _passwordInput.update { password }
    }

    fun rePasswordUpdate(rePassword: String) {
        _rePasswordInput.update { rePassword }
    }

    fun resetLoginState() {
        _loginState.value = null
        usernameUpdate("")
        passwordUpdate("")
        rePasswordUpdate("")
    }

    fun resetRegisterState() {
        _registerState.value = null
        usernameUpdate("")
        passwordUpdate("")
        rePasswordUpdate("")
    }

    fun login() {
        val username = _usernameInput.value
        val password = _passwordInput.value

        if (username.isEmpty()) {
            _loginState.value = UiState.Failed("请输入用户名")
            return
        }

        if (password.isEmpty()) {
            _loginState.value = UiState.Failed("请输入密码")
            return
        }

        viewModelScope.launch {
            repository.login(username, password)
                .onStart {
                    _loginState.emit(UiState.Loading)
                }
                .catch {
                    if (it is DataResultException) {
                        _loginState.emit(UiState.Failed(it.message))
                    } else {
                        _loginState.emit(UiState.Exception(it))
                    }
                }
                .collectLatest {
                    if (it != null) {
                        UserInfoHelper.save(username, password = password)
                        _loginState.emit(UiState.Success(true))
                    } else {
                        _loginState.emit(UiState.Failed("登录失败，请检查用户名和密码"))
                    }
                }
        }
    }

    fun register() {
        val username = _usernameInput.value
        val password = _passwordInput.value
        val rePassword = _rePasswordInput.value

        if (username.isEmpty()) {
            _registerState.value = UiState.Failed("请输入用户名")
            return
        }

        if (password.isEmpty()) {
            _registerState.value = UiState.Failed("请输入密码")
            return
        }

        if (password.length < 6) {
            _registerState.value = UiState.Failed("密码长度不能少于6位")
            return
        }

        if (password != rePassword) {
            _registerState.value = UiState.Failed("两次输入的密码不一致")
            return
        }

        viewModelScope.launch {
            repository.register(username, password, rePassword)
                .onStart {
                    _registerState.emit(UiState.Loading)
                }
                .catch {
                    if (it is DataResultException) {
                        _registerState.emit(UiState.Failed(it.message))
                    } else {
                        _registerState.emit(UiState.Exception(it))
                    }
                }
                .collectLatest {
                    if (it != null) {
                        UserInfoHelper.save(username, password = password)
                        _registerState.emit(UiState.Success(true))
                    } else {
                        _registerState.emit(UiState.Failed("注册失败，请稍后重试"))
                    }
                }
        }
    }
}