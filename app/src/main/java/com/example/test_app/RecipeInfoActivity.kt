package com.example.test_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.content.Intent

class RecipeInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_info)

        var info: TextView = findViewById(R.id.recipeInfo)
        val intentInfo = intent.getStringExtra("instructions")
        info.text = intentInfo

        val backButton: Button = findViewById(R.id.infoBackBtn)

        backButton.setOnClickListener {
            val back = Intent(this@RecipeInfoActivity, ResultsActivity::class.java)
            startActivity(back)
        }
    }
}