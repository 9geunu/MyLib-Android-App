package com.gunu.mylib.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gunu.mylib.domain.model.Book

@Database(entities = [Book::class], version = 4, exportSchema = false)
public abstract class BookLocalDatabase: RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        val DB_NAME = "book-db"

        @Volatile
        private var instance: BookLocalDatabase? = null

        fun getInstance(context: Context): BookLocalDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): BookLocalDatabase {
            return Room.databaseBuilder(context.applicationContext, BookLocalDatabase::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
        }
    }
}