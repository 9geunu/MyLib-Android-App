package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.domain.repository.IRepository

class SearchBooksUseCase(
    private val repository: IRepository,
    private val query: String
): UseCase<List<Book>> {

    override suspend fun execute(): List<Book> {
        return repository.searchBooks(query)
    }
}