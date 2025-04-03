package com.example.booktrack.data.models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "review_table", foreignKeys = [
    ForeignKey(
        entity = Book::class,
        parentColumns = ["id"],
        childColumns = ["bookId"],
        onDelete = ForeignKey.CASCADE
    ),
    ForeignKey(
        entity = User::class,
        parentColumns = ["id"],
        childColumns = ["userId"],
        onDelete = ForeignKey.CASCADE
    )
])
data class Review(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val bookId: Int,
    val userId: Int,

    val rating: Float,
    val reviewText: String,
    val reviewDate: Long
)