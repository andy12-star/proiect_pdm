package com.example.booktrack.data.repositories

import android.util.Log
import com.example.booktrack.data.dao.NotificationDao
import com.example.booktrack.data.models.Notification

class NotificationRepository(private val dao: NotificationDao) {

    val allNotifications = dao.getAllNotifications()

    suspend fun insert(notification: Notification) {
        Log.d("DEBUG", "INSERT în DB: ${notification.message}")
        dao.insert(notification)
    }


}
