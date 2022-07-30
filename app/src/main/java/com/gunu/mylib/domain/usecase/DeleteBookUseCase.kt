package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.repository.IRepository

class DeleteBookUseCase(
    private val repository: IRepository,
    private val isbn: Long
): UseCase<Unit> {

    override suspend fun execute() {
        repository.deleteBook(isbn)
    }
}