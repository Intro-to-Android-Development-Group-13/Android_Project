package com.example.test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.*
import android.content.Intent



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // When user submits, api call to rapidapi will commence
        val submit: Button = findViewById(R.id.button2)
        submit.setOnClickListener {
            getRecipes()
        }
    }

    private fun getRecipes() {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params.put("ingredients", "cinnamon")
        params.put("number", "10")
        params.put("rank", "10")

        val headers = RequestHeaders()
        headers.put("X-RapidAPI-Key", "key")
        headers.put("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")

        client.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch", headers, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.d("success", "$json")

                // fetch json results - relevant keys: id, title and image
                val resultsArray: JSONArray = json.jsonObject.getJSONArray("results")
                val recipesArray: ArrayList<HashMap<String, String>> = ArrayList()

                for ( i in 0 until resultsArray.length()) {
                    val recipe = resultsArray.getJSONObject(i)
                    recipesArray.add(hashMapOf(
                        "id" to recipe.optString("id"),
                        "title" to recipe.optString("title"),
                        "image" to recipe.optString("image")
                    ))
                }
                Log.d("parsed results", "$recipesArray")
                // carry resultsArray to new activity
                val intent = Intent(this@MainActivity, ResultsActivity::class.java)
                intent.putExtra("recipes", recipesArray)
                startActivity(intent)
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String, throwable: Throwable?) {
                Log.e("API Error", "Failed to fetch data: $response")
            }
        })

    }

}