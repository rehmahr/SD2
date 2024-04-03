package com.example.sd2


import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.activity.ComponentActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URLEncoder


//testing github
// testing farah
//summmmmmm testing

// test again
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editTextUsername = findViewById<EditText>(R.id.editTextText)
        val editTextPassword = findViewById<EditText>(R.id.editTextTextPassword)
        val buttonLogin = findViewById<Button>(R.id.button3)
        val buttonReg = findViewById<Button>(R.id.button4)

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString()
            val password = editTextPassword.text.toString()
            login(username, password)
        }

        buttonReg.setOnClickListener {
            goToNextActivity(this, Registration::class.java)
        }
    }

    private fun login(username: String, password: String) {
        val client = OkHttpClient()
        val url = "http://192.168.56.1/seniordes/login.php"
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(applicationContext, "Login failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (responseBody == "success") {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                        // Proceed to next activity or perform any other actions
                        goToNextActivity(this@MainActivity, Dashboard::class.java)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Login failed: Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}

    fun goToNextActivity(context: Context, nextActivityClass: Class<*>) {
    val intent = Intent(context, nextActivityClass)
    context.startActivity(intent)
}