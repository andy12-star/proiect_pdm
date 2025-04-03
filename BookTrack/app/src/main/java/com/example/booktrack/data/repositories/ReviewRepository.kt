package com.example.booktrack.data.repositories


import com.example.booktrack.data.dao.BookDao
import com.example.booktrack.data.dao.ReviewDao
import com.example.booktrack.data.dao.UserDao
import com.example.booktrack.data.models.Review
import kotlinx.coroutines.flow.Flow

class ReviewRepository(private val reviewDao: ReviewDao, private val bookDao: BookDao, private val userDao: UserDao) {

    // inserarea unui review:
    suspend fun insertReview(review: Review) {
        reviewDao.insert(review)
    }

    // obtinerea review-urilor pentru o anumita carte:
    fun getReviewsForBook(bookId: Int): Flow<List<Review>> {
        return reviewDao.getReviewsForBook(bookId)
    }

    // obtinerea review-urilor pentru un anumit utilizator:
    fun getReviewsForUser(userId: Int): Flow<List<Review>> {
        return reviewDao.getReviewsForUser(userId)
    }

    // stergerea unui review:
    suspend fun deleteReview(review: Review) {
        reviewDao.delete(review)
    }

    suspend fun isBookExist(bookId: Int): Boolean {
        return bookDao.getBookById(bookId) != null
    }

    suspend fun isUserExist(userId: Int): Boolean {
        return userDao.getUserById(userId) != null
    }
}