package com.example.hearxassessment.database
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.hearxassessment.api.models.TestScore

@Dao
interface TestScoreDao {
    @Query("SELECT * FROM TestScore ORDER BY score DESC")
    fun getAll(): List<TestScore>

    @Insert
    fun insert(score: TestScore)
}
