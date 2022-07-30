package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.domain.repository.IRepository

class GetBooksUseCase(
    private val repository: IRepository
): UseCase<List<Book>> {

    override suspend fun execute(): List<Book> {
        return repository.getBooks()
    }
}