package com.example.test_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestHeaders
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

class ResultsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        // get the list of recipes brought from MainActivity
        val recipesList: ArrayList<HashMap<String, String>> =
            intent.getSerializableExtra("recipes") as ArrayList<HashMap<String, String>>

        Log.d("recipesList", "$recipesList")

        /**
         * Siyuan - recipesList already contains the image and title. To access you can
         * resultList[0]["title"] and you'll get the string or image url if you search
         * via image. This is a new activity so you can basically ignore MainActivity.kt and
         * activity_main.xml and instead focus your efforts on the recycler view in activity_results.xml
         * It will just be like the lab when creating the recylcer view (using glide) there.
         * NOTE: I added an imageview in the activity_results.xml for my own testing purposes. The
         * code below shows how to transcribe an image with a tag. You can ignore it if you want to
         * follow a different way.
         */


        val imageId: String = recipesList[0]["id"].toString()
        val imageView: ImageView = findViewById(R.id.placeholder)
        if (imageView != null) {
            Glide.with(this@ResultsActivity)
                .load(recipesList[0]["image"])
                .into(imageView)
        }
        else {
            Log.e("Image", "image is null")
        }
        imageView.isClickable = true
        imageView.tag = imageId
        imageView.setOnClickListener {recipe ->
            val clickedImage = recipe.tag as? String
            // Handle click
            getRecipeInfo(clickedImage)
        }
    }

    private fun getRecipeInfo(recipeId: String?) {
        val client = AsyncHttpClient()
        val params = RequestParams()
        val id: Int = recipeId?.toIntOrNull()?: 0

        val headers = RequestHeaders()
        headers.put("X-RapidAPI-Key", "72e3afa377msh7ef35d064c63a73p1dc7b9jsn2241f09376c0")
        headers.put("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")

        client.get("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/${id}/information", headers, params, object: JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON?) {
                val instructions = json?.jsonObject?.getString("instructions")

                Log.d("success-Info","$instructions")

                val intent = Intent(this@ResultsActivity, RecipeInfoActivity::class.java)
                intent.putExtra("instructions", instructions)
                startActivity(intent)

            }
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e("API Error", "Failed to fetch data: $response")
            }
        })
    }
}