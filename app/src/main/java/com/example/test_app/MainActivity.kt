package com.example.test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getRecipes()
    }

    private fun getRecipes() {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params.put("ingredients", "cinnamon")
        params.put("number", "10")

        val headers = RequestHeaders()
        headers.put("X-RapidAPI-Key", "72e3afa377msh7ef35d064c63a73p1dc7b9jsn2241f09376c0")
        headers.put("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")

        client.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch", headers, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.d("success", "$json")

                // Parse the JSON response to get the image URL
                val imageUrl = json.jsonObject.getJSONArray("results").getJSONObject(0).getString("title")


                // Now you can use this URL to load the image, e.g., using an image loading library like Glide or Picasso
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String, throwable: Throwable?) {
                Log.e("API Error", "Failed to fetch data: $response")
            }
        })

    }

}