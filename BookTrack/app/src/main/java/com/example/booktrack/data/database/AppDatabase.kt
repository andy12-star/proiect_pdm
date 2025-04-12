package com.example.booktrack.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.booktrack.data.dao.BookDao
import com.example.booktrack.data.dao.NotificationDao
import com.example.booktrack.data.dao.ReviewDao
import com.example.booktrack.data.dao.UserBookDao
import com.example.booktrack.data.dao.UserDao
import com.example.booktrack.data.models.Book
import com.example.booktrack.data.models.Notification
import com.example.booktrack.data.models.Review
import com.example.booktrack.data.models.User
import com.example.booktrack.data.models.UserBook

@Database(entities = [Book::class, User::class, Review::class, Notification::class, UserBook::class], version = 3)
// andreea modif in 2!!!!1
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookDao(): BookDao
    abstract fun userDao(): UserDao
    abstract fun reviewDao(): ReviewDao
    abstract fun notificationDao(): NotificationDao
    abstract fun userBookDao() : UserBookDao

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