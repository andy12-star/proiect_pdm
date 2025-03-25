package com.example.booktrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_table")
class Book (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val genre: String,
    val author: String,
//    val coverUri: String?
    val coverImageResId: Int
)