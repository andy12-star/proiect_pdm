package com.example.booktrack.ui.my_books.add_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.booktrack.data.models.Book
import com.example.booktrack.data.repositories.BookRepository
import kotlinx.coroutines.launch

class AddBookViewModel(private val repository: BookRepository) : ViewModel() {

    fun insertBook(book: Book) {
        viewModelScope.launch {
            repository.insertBook(book)
        }
    }

}