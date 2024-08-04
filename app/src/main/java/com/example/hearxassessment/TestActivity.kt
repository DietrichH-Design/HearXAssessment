package com.example.hearxassessment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hearxassessment.api.models.TestResults
import com.example.hearxassessment.api.models.TestScore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.hearxassessment.api.uploadResults
import com.example.hearxassessment.database.AppDatabase

class TestActivity : AppCompatActivity() {

    private lateinit var exitTestButton: Button
    private lateinit var submitButton: Button
    private lateinit var inputField: EditText
    private lateinit var scoreView: TextView
    private lateinit var roundView: TextView
    private lateinit var testManager: TestManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        exitTestButton = findViewById(R.id.exitTestButton)
        submitButton = findViewById(R.id.submitButton)
        inputField = findViewById(R.id.inputField)
        scoreView = findViewById(R.id.scoreView)
        roundView = findViewById(R.id.roundView)

        testManager = TestManager(this)

        Handler(Looper.getMainLooper()).postDelayed({ testManager.startTest() }, 3000)

        exitTestButton.setOnClickListener {
            onDestroy()
        }

        submitButton.setOnClickListener {
            testManager.submitAnswer(inputField.text.toString())
            testManager.nextRound() // Advance to the next round
            updateUI() // Then update the UI
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI() {
        scoreView.text = "Score: ${testManager.score}"
        roundView.text = "Round: ${testManager.currentRound}/10"
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!isFinishing) {
            uploadResultsAndShowFinalScore()
        }
    }

    private fun uploadResultsAndShowFinalScore() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Upload test results
                val testResults = createTestResults() // Create a TestResults object with the needed data
                uploadResults(testResults) // Upload results to the API

                // Save the final score to the database
                val db = AppDatabase.getDatabase(this@TestActivity)
                db.testScoreDao().insert(TestScore(score = testManager.score))

                withContext(Dispatchers.Main) {
                    // Show final score in a popup
                    showFinalScorePopup()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    showErrorPopup()
                }
            }
        }
    }

    private fun showFinalScorePopup() {
        AlertDialog.Builder(this)
            .setTitle("Test Complete")
            .setMessage("Your final score is: ${testManager.score}")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .show()
    }

    private fun showErrorPopup() {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage("An error occurred while saving or uploading the test results.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }

    private fun createTestResults(): TestResults {
        // Construct your TestResults object with the test results and flow data
        // Example:
        return TestResults(
            score = testManager.score,
            rounds = testManager.getRounds() // Ensure this method is properly implemented
        )
    }
}
