package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.repository.IRepository

class DeleteBookmarkedBooksUseCase(
    private val repository: IRepository
): UseCase<Unit> {

    override suspend fun execute() {
        repository.deleteBookmarkedBooks()
    }
}