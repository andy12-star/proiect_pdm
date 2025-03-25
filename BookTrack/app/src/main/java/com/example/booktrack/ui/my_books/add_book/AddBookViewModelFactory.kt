package com.example.booktrack.ui.my_books.add_book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.booktrack.data.repositories.BookRepository

class AddBookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory
{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(AddBookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddBookViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}