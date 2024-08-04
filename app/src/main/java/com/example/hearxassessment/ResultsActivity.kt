package com.example.hearxassessment

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hearxassessment.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ResultsActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var adapter: ResultsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        listView = findViewById(R.id.listView)

        // Load data from database using a coroutine
        loadData()
    }

    private fun loadData() {
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(this@ResultsActivity)
            val scores = withContext(Dispatchers.IO) {
                db.testScoreDao().getAll() // Perform the database operation on the IO thread
            }
            adapter = ResultsAdapter(this@ResultsActivity, scores)
            listView.adapter = adapter
        }
    }
}
