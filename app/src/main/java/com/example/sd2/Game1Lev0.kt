package com.example.sd2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Game1Lev0 : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game1_lev0)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageButton1 = findViewById<ImageButton>(R.id.imageButton)
        val imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        val imageButton3 = findViewById<ImageButton>(R.id.imageButton3)
        val imageButton4 = findViewById<ImageButton>(R.id.imageButton4)
        val imageButton5 = findViewById<ImageButton>(R.id.imageButton5)
        val nextButton = findViewById<Button>(R.id.button9)

        imageButton1.setOnClickListener {
            findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.angry)
        }

        imageButton2.setOnClickListener {
            findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.happy)
        }

        imageButton3.setOnClickListener {
            findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.sad)
        }

        imageButton4.setOnClickListener {
            findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.surprised)
        }

        imageButton5.setOnClickListener {

            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)

        }

        nextButton.setOnClickListener {

            val intent = Intent(this, Game1Lev1::class.java)
            startActivity(intent)

        }
    }
}
