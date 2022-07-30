package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.domain.repository.IRepository

class UpdateBookmarkUseCase(
    private val repository: IRepository,
    private val book: Book,
    private val isBookmarked: Boolean
): UseCase<Unit> {

    override suspend fun execute() {
        repository.updateBookmark(book, isBookmarked)
    }
}