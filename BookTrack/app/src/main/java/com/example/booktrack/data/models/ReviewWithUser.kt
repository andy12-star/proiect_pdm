package com.example.booktrack.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class ReviewWithUser(
    @Embedded val review: Review, // Încarcă review-ul
    @Relation(
        parentColumn = "userId",  // Columna în Review care face referire la User
        entityColumn = "id"      // Columna în User care e cheia primară
    )
    val user: User               // Include informațiile despre utilizator
)
