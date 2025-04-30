package com.example.booktrack.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.booktrack.data.models.Book
import com.example.booktrack.data.models.UserBook

@Dao
interface UserBookDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userBook: UserBook)

    @Query("""
    SELECT b.* FROM book_table b
    INNER JOIN user_book_table ub ON b.id = ub.bookId
    WHERE ub.userId = :userId AND ub.bookshelf = :bookshelf
    """)
    fun getBooksByShelf(userId: Int, bookshelf: String): LiveData<List<Book>>

    @Query("UPDATE user_book_table SET bookshelf = :bookshelf WHERE userId = :userId AND bookId = :bookId")
    suspend fun updateBookshelf(userId: Int, bookId: Int, bookshelf: String)

    @Query("DELETE FROM user_book_table WHERE userId = :userId AND bookId = :bookId")
    suspend fun removeBookFromShelf(userId: Int, bookId: Int)

    @Query("SELECT * FROM user_book_table WHERE userId = :userId AND bookId = :bookId LIMIT 1")
    suspend fun getUserBook(userId: Int, bookId: Int): UserBook?

}