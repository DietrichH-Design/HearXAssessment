package com.example.hearxassessment

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.example.hearxassessment.api.models.RoundResult


class TestManager(private val context: Context) {

    var currentRound = 1
    var score = 0
    private var difficulty = 5
    private var currentTriplet: List<Int> = generateTriplet()
    private val roundsData = mutableListOf<RoundResult>()

    fun startTest() {
        playTriplet()
    }

    fun nextRound() {
        if (currentRound < 10) {
            currentRound++
            currentTriplet = generateTriplet()
            playTriplet()
        } else {
            // End test and show final score
        }
    }

    fun submitAnswer(answer: String) {
        val correctAnswer = currentTriplet.joinToString("")
        val tripletPlayed = currentTriplet.joinToString("")

        roundsData.add(
            RoundResult(
                difficulty = difficulty,
                triplet_played = tripletPlayed,
                triplet_answered = answer
            )
        )

        if (answer == correctAnswer) {
            difficulty++
            score += difficulty
        } else {
            difficulty--
        }
    }

    fun getRounds(): List<RoundResult> {
        return roundsData
    }

    private fun playTriplet() {
        val audioPlayer = AudioPlayer(context)
        audioPlayer.playNoise(difficulty) {
            // Delay between playing digits can be handled within this block
            val delay = 1000L // 1 second delay, adjust as needed
            Handler(Looper.getMainLooper()).postDelayed({
                audioPlayer.playDigit(currentTriplet[0]) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        audioPlayer.playDigit(currentTriplet[1]) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                audioPlayer.playDigit(currentTriplet[2]) {
                                    // Noise stop handling
                                }
                            }, delay)
                        }
                    }, delay)
                }
            }, delay)
        }
    }

    private fun generateTriplet(): List<Int> {
        val triplet = mutableListOf<Int>()
        while (triplet.size < 3) {
            val digit = (1..9).random()
            if (!triplet.contains(digit)) {
                triplet.add(digit)
            }
        }
        return triplet
    }
}
