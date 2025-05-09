package com.example.booktrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.booktrack.data.models.Book
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert
    suspend fun insertBook(book: Book)

    @Query("SELECT * FROM book_table")
    fun getAllBooks(): Flow<List<Book>>

    @Query("SELECT * FROM book_table WHERE genre = :genre")
    suspend fun getBooksByGenre(genre: String): List<Book>

    @Query("SELECT * FROM book_table WHERE title = :title LIMIT 1")
    suspend fun getBookByTitle(title: String): Book?

    @Query("SELECT * FROM book_table WHERE id = :bookId LIMIT 1")
    suspend fun getBookById(bookId: Int): Book?

    @Query("SELECT id FROM book_table WHERE title = :title LIMIT 1")
    suspend fun getBookIdByTitle(title: String): Int

//    @Query("SELECT DISTINCT genre FROM book_table ORDER BY genre ASC")
//    suspend fun getAllGenres(): List<String>

}