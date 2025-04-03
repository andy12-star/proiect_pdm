package com.example.booktrack.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.booktrack.data.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("SELECT * FROM user_table WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User?

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user_table WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    @Query("SELECT * FROM user_table WHERE id = :userId LIMIT 1")
    suspend fun getUserById(userId: Int): User?

    @Delete
    suspend fun delete(user: User)

    // (opțional) dacă vrei să ștergi după email direct:
    @Query("DELETE FROM user_table WHERE email = :email")
    suspend fun deleteByEmail(email: String)

}