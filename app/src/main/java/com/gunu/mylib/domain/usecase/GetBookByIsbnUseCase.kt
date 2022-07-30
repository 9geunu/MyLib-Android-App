package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.domain.repository.IRepository

class GetBookByIsbnUseCase(
    private val repository: IRepository,
    private val isbn: Long
): UseCase<Book?> {

    override suspend fun execute(): Book? {
        return repository.getBookByIsbn(isbn)
    }
}