package com.example.hearxassessment.database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.hearxassessment.api.models.TestScore

@Database(entities = [TestScore::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun testScoreDao(): TestScoreDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "appDataBase" // Database name must be a String
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}



