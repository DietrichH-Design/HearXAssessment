package com.example.hearxassessment.api.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestScore(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val score: Int
)
