package com.example.booktrack.ui.my_books

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.booktrack.data.models.Book
import com.example.booktrack.data.repositories.BookRepository

class MyBooksViewModel(private val bookRepository: BookRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is my books Fragment"
    }
    val text: LiveData<String> = _text

    val allBooks: LiveData<List<Book>> = bookRepository.getAllBooks().asLiveData()
}
