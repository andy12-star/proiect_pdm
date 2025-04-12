package com.example.booktrack.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(tableName = "user_book_table",
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"], onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = Book::class, parentColumns = ["id"], childColumns = ["bookId"], onDelete = ForeignKey.CASCADE)
                  ],
    indices = [Index(value = ["userId", "bookId"], unique = true)])
data class UserBook(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: Int,
    val bookId: Int,
    val bookshelf: String,
    val dateAdded: Long
)