package com.example.sd2

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class homepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_homepage)

        val buttonLogin = findViewById<Button>(R.id.button2)
        val buttonReg = findViewById<Button>(R.id.register)

        buttonLogin.setOnClickListener {
            goToNextActivity(this, MainActivity::class.java)
        }

        buttonReg.setOnClickListener {
            goToNextActivity(this, Registration::class.java)
        }

    }
}

// 192.168.132.103 Amaal
// 192.168.56.1 Farah
