package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.model.Book
import com.gunu.mylib.domain.repository.IRepository

class InsertBookUseCase(
    private val repository: IRepository,
    private val book: Book
): UseCase<Unit> {

    override suspend fun execute() {
        repository.insertBook(book)
    }
}