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

}