package com.example.test_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import org.json.JSONArray
import org.json.JSONObject

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        val recipesList: ArrayList<HashMap<String, String>> =
            intent.getSerializableExtra("recipes") as ArrayList<HashMap<String, String>>
        val recyclerView: RecyclerView = findViewById(R.id.recipes_list)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = RecipesAdapter(recipesList) { recipeId ->
            Log.d("ResultsActivity", "Recipe with ID $recipeId was clicked.")
            getRecipeInfo(recipeId)
        }
        recyclerView.adapter = adapter
    }

    private fun getRecipeInfo(recipeId: String?) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        val id: Int = recipeId?.toIntOrNull()?: 0
        val headers = RequestHeaders()

        headers.put("X-RapidAPI-Key", "72e3afa377msh7ef35d064c63a73p1dc7b9jsn2241f09376c0")
        headers.put("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")

        client.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/${id}/information",
            headers, params, object: JsonHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                    // get instructions
                    val instructions = json?.jsonObject?.getString("instructions")
                    Log.d("success-instructions","$instructions")

                    // get ingredients
                    val extIngredArrObj: JSONArray? = json?.jsonObject?.getJSONArray("extendedIngredients")
                    Log.d("success-extIngredArrObj", "$extIngredArrObj")
                    var extIngredArray: String? = ""
                    for (i in 0 until (extIngredArrObj?.length() ?: 0)) {
                        val extIngredObj: JSONObject? = extIngredArrObj?.getJSONObject(i)
                        extIngredArray += extIngredObj?.getString("original") + ".\n\n"
                    }
                    Log.d("success-extIngredArray", "$extIngredArray")

                    // carry ingredients and instructions to next RecipeInfoActivity
                    val intent = Intent(this@ResultsActivity, RecipeInfoActivity::class.java)
                    intent.putExtra("instructions", (instructions?.replace(".", ".\n\n"))?.substringAfter("Instructions"))
                    intent.putExtra("extendedIngredients", extIngredArray)
                    startActivity(intent)
                }
                override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                    Log.e("API Error", "Failed to fetch data: $response")
                }
            })
    }
}

class RecipesAdapter(private val recipesList: ArrayList<HashMap<String, String>>, private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.recipeImage)
        val textView: TextView = view.findViewById(R.id.recipeName)
        val rootView: View = view
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recipe_item,parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recipe = recipesList[position]
        Glide.with(holder.imageView.context)
            .load(recipe["image"])
            .into(holder.imageView)
        holder.textView.text = recipe["title"]

        // Set a tag for the root view of the ViewHolder
        holder.rootView.tag = recipe["id"]
        holder.rootView.setOnClickListener { view ->
            // Retrieve the tag and use it as the recipe ID
            val clickedRecipeId = view.tag as? String
            clickedRecipeId?.let {
                onClick(it)
            }
        }
    }
    override fun getItemCount(): Int {
        return recipesList.size
    }
}
