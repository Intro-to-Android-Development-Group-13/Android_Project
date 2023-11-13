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
import android.widget.EditText
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    private lateinit var ingredientsEditText: EditText
    private lateinit var ingredientsListTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var userIngredients: String = "apple,cinnamon,sugar"

        ingredientsEditText = findViewById(R.id.ingredients)
        ingredientsListTextView = findViewById(R.id.ingredientsList)

        /**
         * Mina - userIngredients will be what you add to. The above is just a placeholder
         * for mine and siyuan's testing. You'll want to create an event listener similar
         * to below such that ingredients gets added to the square box in the middle of
         * activity_main layout and concatenated with userIngredients (make sure to start
         * with an empty string for userIngredients! lol)
         * for reference userIngredients should be separated by a comma (no space).
         */

        // When user submits, api call to rapidapi will commence
        val submit: Button = findViewById(R.id.button2)
        submit.setOnClickListener {
            getRecipes(userIngredients)
        }

        val addButton: Button = findViewById(R.id.addButton)
        addButton.setOnClickListener {
            addIngredientToList()
        }
    }

    private fun getRecipes(ingredients: String) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        params.put("ingredients", ingredients)
        params.put("number", "10")
        params.put("rank", "10")

        val headers = RequestHeaders()
        headers.put("X-RapidAPI-Key", "72e3afa377msh7ef35d064c63a73p1dc7b9jsn2241f09376c0")
        headers.put("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")

        client.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/complexSearch", headers, params, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.d("success", "$json")

                // fetch json results - relevant keys: id, title and image
                val resultsArray: JSONArray = json.jsonObject.getJSONArray("results")
                val recipesArray: ArrayList<HashMap<String, String>> = ArrayList()

                // recipesArray format: [{image=https://spoonacular.com/recipeImages/592479-312x231.jpg, id=592479, title=Kale and Quinoa Salad with Black Beans}, ...]
                for ( i in 0 until resultsArray.length()) {
                    val recipe = resultsArray.getJSONObject(i)
                    recipesArray.add(hashMapOf(
                        "id" to recipe.optString("id", "id Missing"),
                        "title" to recipe.optString("title", "Title Missing"),
                        "image" to recipe.optString("image", "Image Missing"),
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

    private fun addIngredientToList() {
        val ingredient = ingredientsEditText.text.toString().trim()

        if (ingredient.isNotEmpty()) {
            // Append the ingredient to the TextView
            val currentText = ingredientsListTextView.text.toString()
            val newText = if (currentText.isEmpty()) {
                "- $ingredient"
            } else {
                "$currentText\n- $ingredient"
            }

            ingredientsListTextView.text = newText

            // Clear the EditText for the next input
            ingredientsEditText.text.clear()
        }
    }

}