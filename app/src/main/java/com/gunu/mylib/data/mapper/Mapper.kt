package com.gunu.mylib.data.mapper

import com.gunu.mylib.domain.model.Book

object Mapper {
    fun domainToData(book: Book): com.gunu.mylib.data.local.model.Book {
        return com.gunu.mylib.data.local.model.Book(
            book.title,
            book.subtitle,
            book.isbn13,
            book.price,
            book.image,
            book.url,
            book.isBookmarked,
            book.memo
        )
    }

    fun dataToDomain(book: com.gunu.mylib.data.local.model.Book): Book {
        return Book(
            book.title,
            book.subtitle,
            book.isbn13,
            book.price,
            book.image,
            book.url,
            book.isBookmarked,
            book.memo
        )
    }
}