package com.example.sd2

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Congratulations2 : AppCompatActivity() {
    private lateinit var dashButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_congratulations2)

        dashButton = findViewById(R.id.buttonNext)

        dashButton.setOnClickListener{
            goToNextActivity(this, Dashboard::class.java)
        }
    }
}