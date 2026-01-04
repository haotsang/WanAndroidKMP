package com.haotsang.wanandroidkmp.model.usecase

import com.haotsang.wanandroidkmp.model.Repository

class FavoriteArticleUseCase(
    private val repository: Repository
) {
    operator fun invoke(id: Int) = repository.favoriteArticle(id)
}