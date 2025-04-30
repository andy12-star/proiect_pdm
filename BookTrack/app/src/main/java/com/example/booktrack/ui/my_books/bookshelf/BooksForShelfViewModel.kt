package com.example.booktrack.ui.my_books.bookshelf

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.booktrack.data.database.AppDatabase
import com.example.booktrack.data.models.Book
import kotlinx.coroutines.launch

class BooksForShelfViewModel(application: Application) : AndroidViewModel(application) {

    private val userBookDao = AppDatabase.getDatabase(application).userBookDao()

    fun getBooksForShelf(userId: Int, bookshelf: String): LiveData<List<Book>> {
        return userBookDao.getBooksByShelf(userId, bookshelf)
    }

}
