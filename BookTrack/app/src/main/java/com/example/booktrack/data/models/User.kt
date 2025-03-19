package com.example.booktrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val role: String,
    val username: String,
    val email: String,
    val password: String
)