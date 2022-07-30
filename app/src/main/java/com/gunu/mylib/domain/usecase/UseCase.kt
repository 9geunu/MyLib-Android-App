package com.gunu.mylib.domain.usecase

interface UseCase<R> {
    suspend fun execute(): R
}