package com.haotsang.wanandroidkmp.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.UiState
import com.haotsang.wanandroidkmp.model.usecase.CancelUserFavoriteArticleUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class UserFavoriteViewModel(
    private val repository: Repository,
    private val cancelUserFavoriteArticleUseCase: CancelUserFavoriteArticleUseCase
) : ViewModel() {

    val favoriteArticles = repository.userFavoriteArticles().cachedIn(viewModelScope)

    private val _favoriteState = MutableSharedFlow<UiState<Boolean>>()
    val favoriteState = _favoriteState.asSharedFlow()

    fun cancelFavoriteArticle(id: Int, originId: Int) {
        viewModelScope.launch {
            cancelUserFavoriteArticleUseCase(id = id, originId = originId).onStart {
                _favoriteState.emit(UiState.Loading)
            }.onEach {
                _favoriteState.emit(UiState.Success(true))
            }.catch {
                _favoriteState.emit(UiState.Exception(it))
            }.launchIn(viewModelScope)
        }
    }

}