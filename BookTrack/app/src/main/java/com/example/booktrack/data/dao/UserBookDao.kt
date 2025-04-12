package com.example.booktrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.booktrack.data.models.UserBook

@Dao
interface UserBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userBook: UserBook)

    @Query("SELECT * FROM user_book_table WHERE userId = :userId AND bookshelf = :bookshelf")
    suspend fun getBooksByShelf(userId: Int, bookshelf: String): List<UserBook>

    @Query("UPDATE user_book_table SET bookshelf = :bookshelf WHERE userId = :userId AND bookId = :bookId")
    suspend fun updateBookshelf(userId: Int, bookId: Int, bookshelf: String)

    @Query("DELETE FROM user_book_table WHERE userId = :userId AND bookId = :bookId")
    suspend fun removeBookFromShelf(userId: Int, bookId: Int)

    @Query("SELECT * FROM user_book_table WHERE userId = :userId AND bookId = :bookId LIMIT 1")
    suspend fun getUserBook(userId: Int, bookId: Int): UserBook?


}