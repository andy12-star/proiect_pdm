package com.example.booktrack.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.booktrack.data.models.Review
import com.example.booktrack.data.models.ReviewWithUser
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Insert
    suspend fun insert(review: Review)

    @Query("SELECT * FROM review_table WHERE bookId = :bookId")
    fun getReviewsForBook(bookId: Int): Flow<List<Review>>

    @Query("SELECT * FROM review_table WHERE userId = :userId")
    fun getReviewsForUser(userId: Int): Flow<List<Review>>

    @Transaction
    @Query("SELECT * FROM review_table WHERE bookId = :bookId")
    suspend fun getReviewsWithUsers(bookId: Int): List<ReviewWithUser>

    @Query("SELECT rating FROM review_table WHERE bookId = :bookId")
    suspend fun getRatingsForBook(bookId: Int): List<Float>

    @Delete
    suspend fun delete(review: Review)

}