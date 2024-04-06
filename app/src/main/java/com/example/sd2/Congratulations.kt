package com.example.sd2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Congratulations : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_congratulations)

        val currentLevel = intent.getStringExtra("CURRENT_LEVEL")

        val nextLevel = getNextLevel(currentLevel)

        val nextLevelButton = findViewById<Button>(R.id.buttonNext)
        nextLevelButton.setOnClickListener {
            val nextLevelIntent = when (nextLevel) {
                "Congratulations" -> Intent(this, Congratulations::class.java)
                else -> Intent(this, Class.forName("com.example.sd2.$nextLevel"))
            }
            nextLevelIntent.putExtra("CURRENT_LEVEL", nextLevel)
            startActivity(nextLevelIntent)
            finish()
        }
    }

    private fun getNextLevel(currentLevel: String?): String {
        return when (currentLevel) {
            "Game1Lev1Test" -> "Game1Lev2"
            "Game2Lev14" -> "Game2Lev2"
            "Game2Lev2" -> "Game2Lev31"
            "Game3Lev1" -> "Game3Lev2"

            else -> ""
        }
    }
}