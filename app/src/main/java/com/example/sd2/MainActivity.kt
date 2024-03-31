package com.example.sd2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity

//testing github
// testing farah
class MainActivity : ComponentActivity() {

    private lateinit var reg_button: Button
    private lateinit var dash_button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        reg_button = findViewById(R.id.button4)
        dash_button = findViewById(R.id.button3)

        reg_button.setOnClickListener {
            goToNextActivity(this, Registration::class.java)
        }

        dash_button.setOnClickListener {
            goToNextActivity(this, Dashboard::class.java)
        }
    }
}

fun goToNextActivity(context: Context, nextActivityClass: Class<*>) {
    val intent = Intent(context, nextActivityClass)
    context.startActivity(intent)
}
