package com.example.booktrack.ui.my_books.user_book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.models.UserBook
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class UserBookViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).userBookDao()

    fun insert(userBook: UserBook) {
        viewModelScope.launch {
            dao.insert(userBook)
        }
    }

    fun getUserBook(userId: Int, bookId: Int): UserBook? {
        return runBlocking {
            dao.getUserBook(userId, bookId)
        }
    }

}