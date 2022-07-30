package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.model.DetailBook
import com.gunu.mylib.domain.repository.IRepository

class GetDetailBookUseCase(
    private val repository: IRepository,
    private val isbn: String
): UseCase<DetailBook> {

    override suspend fun execute(): DetailBook {
        return repository.getDetailBook(isbn)
    }
}