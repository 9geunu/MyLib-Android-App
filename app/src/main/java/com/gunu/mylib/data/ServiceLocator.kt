package com.gunu.mylib.data

import android.content.Context
import androidx.annotation.VisibleForTesting
import com.gunu.mylib.data.local.BookLocalDatabase
import com.gunu.mylib.data.repository.Repository
import com.gunu.mylib.domain.repository.IRepository

object ServiceLocator {

    private var database: BookLocalDatabase? = null

    @Volatile
    var repository: IRepository? = null
        @VisibleForTesting set

    fun provideRepository(context: Context): IRepository {
        synchronized(this) {
            return repository ?: repository ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): IRepository {
        val database = database ?: createDatabase(context)
        return Repository(database.bookDao())
    }

    fun createDatabase(context: Context): BookLocalDatabase {
        database = BookLocalDatabase.getInstance(context)
        return database as BookLocalDatabase
    }

    fun resetRepository() {
        repository = null
    }
}