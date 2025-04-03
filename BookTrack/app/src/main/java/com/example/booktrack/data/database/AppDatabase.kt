package com.example.booktrack.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.booktrack.data.dao.BookDao
import com.example.booktrack.data.dao.ReviewDao
import com.example.booktrack.data.dao.UserDao
import com.example.booktrack.data.models.Book
import com.example.booktrack.data.models.Review
import com.example.booktrack.data.models.User


@Database(entities = [Book::class, User::class, Review::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao
    abstract fun reviewDao(): ReviewDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}