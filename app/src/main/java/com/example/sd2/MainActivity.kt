package com.example.sd2


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


//testing github
// testing farah
//summmmmmm testing

// test again
class MainActivity : ComponentActivity() {

    private var userID: Int = -1 // Default value indicating no userID
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
                println("Response body: $responseBody")
                if (responseBody != null) {
                    val json = JSONObject(responseBody)
                    val status = json.optString("status")
                    if (status == "success") {
                        val userID = json.getInt("userID")
                        val userType = json.optString("userType") // Assuming userType is returned from PHP
                        // Store userID globally
                        (application as MyApp).userID = userID
                        println("User ID: $userID")

                        runOnUiThread {
                            Toast.makeText(applicationContext, "Login successful", Toast.LENGTH_SHORT).show()
                            // Redirect based on userType
                            if (userType == "student") {
                                goToNextActivity(this@MainActivity, Dashboard::class.java)
                            } else if (userType == "caretaker") {
                                goToNextActivity(this@MainActivity, CTDashboard::class.java)
                            } else {
                                // Handle unknown userType
                                Toast.makeText(applicationContext, "Unknown user type", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        println("Status is not 'success'")
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Login failed: Invalid username or password", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    println("Response body is null")
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Login failed: Server response is empty", Toast.LENGTH_SHORT).show()
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

fun saveProgressToDatabase(userID: Int, gameID: Int, levelID: Int, progress: Int) {
    GlobalScope.launch(Dispatchers.IO) {
        try {
            val url = URL("http://192.168.56.1/seniordes/progressRep.php")
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.doOutput = true
            urlConnection.requestMethod = "POST"

            val postData = "userID=$userID&gameID=$gameID&levelID=$levelID&progress=$progress"
            urlConnection.outputStream.write(postData.toByteArray(Charsets.UTF_8))

            val responseCode = urlConnection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                println("Progress saved successfully")
            } else {
                println("Error saving progress")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
