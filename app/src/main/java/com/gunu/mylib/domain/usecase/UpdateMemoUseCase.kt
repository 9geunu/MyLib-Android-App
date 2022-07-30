package com.gunu.mylib.domain.usecase

import com.gunu.mylib.domain.repository.IRepository

class UpdateMemoUseCase(
    private val repository: IRepository,
    private val isbn13: Long,
    private val memo: String
): UseCase<Unit> {

    override suspend fun execute() {
        repository.updateMemo(isbn13, memo)
    }
}