package com.example.booktrack.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.booktrack.data.dao.BookDao
import com.example.booktrack.data.models.Book

@Database(entities = [Book::class], version = 2) // versiunea bazei de date trebuie sa fie corecta
abstract class BookDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao

    companion object {
        @Volatile
        private var INSTANCE: BookDatabase? = null

        // funcțtia pentru obtinerea bazei de date
        fun getDatabase(context: Context): BookDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BookDatabase::class.java,
                    "book_database"
                )
                    // foloseșste fallbackToDestructiveMigration() pt a sterge baza de date veche si a crea una noua
                    .fallbackToDestructiveMigration() // sterge baza de date si o recreeaza complet
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
