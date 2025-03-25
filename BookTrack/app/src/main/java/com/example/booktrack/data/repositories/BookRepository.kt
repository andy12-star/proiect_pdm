package com.example.booktrack.data.repositories

import com.example.booktrack.data.dao.BookDao
import com.example.booktrack.data.models.Book
import kotlinx.coroutines.flow.Flow

class BookRepository(private val bookDao: BookDao) {

    suspend fun insertBook(book: Book) {
        bookDao.insertBook(book)
    }

    fun getAllBooks(): Flow<List<Book>> {
        return bookDao.getAllBooks()
    }

}