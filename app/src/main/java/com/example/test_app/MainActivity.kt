package com.example.test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import android.util.Log
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getRecipes()

    }
    private fun getRecipes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient()

                val request = Request.Builder()
                    .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/findByIngredients?ingredients=apples%2Cflour%2Csugar&number=5&ignorePantry=true&ranking=1")
                    .get()
                    .addHeader("X-RapidAPI-Key", "key")
                    .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                    .build()

                val response = client.newCall(request).execute()

                withContext(Dispatchers.Main) {
                    // Update UI or log response here
                    Log.d("success", response.body?.string() ?: "No response")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Handle error, update UI
                    Log.e("error", e.message ?: "Error occurred")
                }
            }
        }
    }

}