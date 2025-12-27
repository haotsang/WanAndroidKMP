package com.haotsang.wanandroidkmp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.DefaultRepository
import com.haotsang.wanandroidkmp.model.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: Repository
) : ViewModel() {


    val articles = repository.homeArticles().onEach { delay(100) }.cachedIn(viewModelScope)

    val banners = repository.bannerList().catch { emit(emptyList()) }


    private val mFavoriteState = MutableSharedFlow<UiState<Boolean>>()
    val favoriteState = mFavoriteState.asSharedFlow()

    fun favoriteArticle(id: Int) {
        viewModelScope.launch {
            repository.favoriteArticle(id).onStart {
                mFavoriteState.emit(UiState.Loading)
            }.onEach {
                mFavoriteState.emit(UiState.Success(true))
            }.catch {
                mFavoriteState.emit(UiState.Exception(it))
            }.launchIn(viewModelScope)
        }
    }

    fun cancelFavoriteArticle(id: Int) {
        viewModelScope.launch {
            repository.cancelFavoriteArticle(id).onStart {
                mFavoriteState.emit(UiState.Loading)
            }.onEach {
                mFavoriteState.emit(UiState.Success(true))
            }.catch {
                mFavoriteState.emit(UiState.Exception(it))
            }.launchIn(viewModelScope)
        }
    }

}