package com.example.hearxassessment

import android.content.Context
import android.media.MediaPlayer
import android.util.Log

class AudioPlayer(private val context: Context) {

    private var noisePlayer: MediaPlayer? = null

    fun playNoise(difficulty: Int, onCompletion: () -> Unit) {
        // Replace with the actual noise resource file name if needed
        val noiseResId = context.resources.getIdentifier("noise_$difficulty", "raw", context.packageName)
        noisePlayer = MediaPlayer.create(context, noiseResId)?.apply {
            setOnCompletionListener {
                onCompletion()
            }
            start()
        }
    }

    fun stopNoise() {
        noisePlayer?.apply {
            stop()
            release()
        }
        noisePlayer = null
    }

    fun playDigit(digit: Int, onCompletion: () -> Unit) {
        // Ensure digit is formatted to match file naming
        val digitResId = context.resources.getIdentifier("_$digit", "raw", context.packageName)
        MediaPlayer.create(context, digitResId)?.apply {
            setOnCompletionListener {
                onCompletion()
            }
            start()
        } ?: run {
            // Log an error if the resource is not found
            Log.e("AudioPlayer", "Resource for digit $digit not found")
            throw IllegalArgumentException("Resource for digit $digit not found")
        }
    }
}
