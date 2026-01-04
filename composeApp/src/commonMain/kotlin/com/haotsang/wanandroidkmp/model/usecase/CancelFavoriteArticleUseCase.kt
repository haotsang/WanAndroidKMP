package com.haotsang.wanandroidkmp.model.usecase

import com.haotsang.wanandroidkmp.model.Repository

class CancelFavoriteArticleUseCase(private val repository: Repository) {

    operator fun invoke(id: Int) = repository.cancelFavoriteArticle(id)
}