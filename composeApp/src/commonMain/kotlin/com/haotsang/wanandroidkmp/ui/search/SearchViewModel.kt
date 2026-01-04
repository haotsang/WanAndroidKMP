package com.haotsang.wanandroidkmp.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.haotsang.wanandroidkmp.model.Repository
import com.haotsang.wanandroidkmp.model.UiState
import com.haotsang.wanandroidkmp.model.bean.Article
import com.haotsang.wanandroidkmp.model.bean.SearchBean
import com.haotsang.wanandroidkmp.model.usecase.CancelFavoriteArticleUseCase
import com.haotsang.wanandroidkmp.model.usecase.FavoriteArticleUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: Repository,
    private val favoriteArticleUseCase: FavoriteArticleUseCase,
    private val cancelFavoriteArticleUseCase: CancelFavoriteArticleUseCase
) : ViewModel() {

    val hotkeys = repository.hotkey().catch { emit(emptyList()) }

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword: StateFlow<String> = _searchKeyword.asStateFlow()

    private val _searchResults = MutableStateFlow<Flow<PagingData<Article>>>(emptyFlow())
    val searchResults: StateFlow<Flow<PagingData<Article>>> = _searchResults.asStateFlow()

    private val mFavoriteState = MutableSharedFlow<UiState<Boolean>>()
    val favoriteState = mFavoriteState.asSharedFlow()

    fun updateSearchKeyword(keyword: String) {
        _searchKeyword.value = keyword
    }

    fun search() {
        if (_searchKeyword.value.isNotEmpty()) {
            _searchResults.value = repository.searchResult(_searchKeyword.value)
                .cachedIn(viewModelScope)
        }
    }

    fun favoriteArticle(id: Int) {
        viewModelScope.launch {
            favoriteArticleUseCase(id).onStart {
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
            cancelFavoriteArticleUseCase(id).onStart {
                mFavoriteState.emit(UiState.Loading)
            }.onEach {
                mFavoriteState.emit(UiState.Success(true))
            }.catch {
                mFavoriteState.emit(UiState.Exception(it))
            }.launchIn(viewModelScope)
        }
    }

}