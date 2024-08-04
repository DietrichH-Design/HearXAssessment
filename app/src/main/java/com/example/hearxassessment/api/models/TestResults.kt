package com.example.hearxassessment.api.models

data class TestResults(
    val score: Int ,
    val rounds: List<RoundResult>
)
