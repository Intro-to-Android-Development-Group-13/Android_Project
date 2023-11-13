package com.example.test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_info)

        // add recipe info to textview
        var recipeInfo: TextView = findViewById(R.id.recipeInfo)
        recipeInfo.text = intent.getStringExtra("instructions")

        // add ingredients info to textview
        var ingredientsInfo: TextView = findViewById(R.id.ingredientsInfo)
        ingredientsInfo.text = intent.getStringExtra("extendedIngredients")

        val backButton: Button = findViewById(R.id.infoBackBtn)
        backButton.setOnClickListener {
            val back = Intent(this@RecipeInfoActivity, ResultsActivity::class.java)
            startActivity(back)
        }
    }
}