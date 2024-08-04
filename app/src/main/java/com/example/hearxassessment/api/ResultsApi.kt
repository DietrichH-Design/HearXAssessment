package com.example.hearxassessment.api

import com.example.hearxassessment.api.models.TestResults
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

// Retrofit service interface
interface ApiService {
    @POST("/")
    suspend fun uploadTestResults(@Body testResults: TestResults): Response<Unit>
}


// Retrofit instance
object RetrofitInstance {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://enoqczf2j2pbadx.m.pipedream.net/") // Ensure this matches the URL in Postman
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}


// Function to upload test results
suspend fun uploadResults(testResults: TestResults) {
    try {
        val response = RetrofitInstance.api.uploadTestResults(testResults)
        if (response.isSuccessful) {
            val responseBody = response.body()
            // Handle the successful response body
        } else {
            // Handle the error response
            val errorBody = response.errorBody()?.string()
            // Process errorBody
        }
    } catch (e: Exception) {
        // Handle exception
    }
}
