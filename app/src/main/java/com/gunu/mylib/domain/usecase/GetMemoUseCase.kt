package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.repository.IRepository

class GetMemoUseCase(
    private val repository: IRepository,
    private val isbn13: Long
): UseCase<String?> {

    override suspend fun execute(): String? {
        return repository.getMemo(isbn13)
    }
}