package com.example.booktrack.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.booktrack.data.models.Notification

@Dao
interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(notification: Notification)

    @Query("SELECT * FROM notifications ORDER BY id DESC")
    fun getAllNotifications(): LiveData<List<Notification>>
}
