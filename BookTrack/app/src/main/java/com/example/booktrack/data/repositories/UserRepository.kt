package com.example.booktrack.data.repositories

import androidx.lifecycle.LiveData
import com.example.booktrack.data.dao.UserDao
import com.example.booktrack.data.models.User

class UserRepository(private val userDao: UserDao) {

    val readAllData: LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, password)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

}