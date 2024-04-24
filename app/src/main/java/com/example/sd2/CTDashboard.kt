package com.example.sd2

import android.os.Bundle
import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.URL

class CTDashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ctdashboard)

        // Fetch students data from PHP endpoint
        fetchStudentsData()
    }

    private fun fetchStudentsData() {
        val client = OkHttpClient()
        val url = URL("http://192.168.56.1/seniordes/ctDash.php") // Replace with your PHP endpoint URL

        // Access user ID from MyApp
        val userID = (application as MyApp).userID

        val request = Request.Builder()
            .url("$url?userID=$userID") // Pass user ID as a query parameter
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("CTDashboard", "Failed to fetch students data: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseData = response.body?.string()
                    runOnUiThread {
                        parseStudentsData(responseData)
                    }
                }
            }
        })
    }

    private fun parseStudentsData(responseData: String?) {
        if (responseData.isNullOrBlank()) {
            Log.e("CTDashboard", "Response data is null or blank")
            return
        }

        try {
            val jsonObject = JSONObject(responseData)
            val success = jsonObject.getBoolean("success")
            if (success) {
                val studentsArray = jsonObject.getJSONArray("students")
                println(studentsArray)
                for (i in 0 until studentsArray.length()) {
                    val studentObject = studentsArray.getJSONObject(i)
                    val studentId = studentObject.getInt("student_id")
                    val studentName = studentObject.getString("student_name")

                    // Populate the table with student data
                    addStudentToTable(studentId, studentName)
                }
            } else {
                val message = jsonObject.getString("message")
                Log.e("CTDashboard", "Failed to fetch students data: $message")
            }
        } catch (e: Exception) {
            Log.e("CTDashboard", "Error parsing students data: ${e.message}")
        }
    }

    private fun addStudentToTable(studentId: Int, studentName: String) {
        val tableLayout = findViewById<TableLayout>(R.id.tableLayout)

        val row = TableRow(this)
        val layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT)
        row.layoutParams = layoutParams

        val idTextView = TextView(this)
        idTextView.text = studentId.toString()
        val nameTextView = TextView(this)
        nameTextView.text = studentName

        row.addView(idTextView)
        row.addView(nameTextView)

        tableLayout.addView(row)
    }
}