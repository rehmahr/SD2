package com.example.sd2

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class ProgressReport : AppCompatActivity() {
    private lateinit var game01wa: CardView
    private lateinit var game01tt: CardView
    private lateinit var game02wa: CardView
    private lateinit var game02tt: CardView
    private lateinit var game03wa: CardView
    private lateinit var game03tt: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_report)

        // Initialize CardViews
        game01wa = findViewById(R.id.game01wa)
        game01tt = findViewById(R.id.game01tt)
        game02wa = findViewById(R.id.game02wa)
        game02tt = findViewById(R.id.game02tt)
        game03wa = findViewById(R.id.game03wa)
        game03tt = findViewById(R.id.game03tt)

        val userId = getUserIdFromIntent()
        if (userId != -1) {
            // Fetch progress data for the specific user
            fetchProgressData(userId)
        } else {
            Log.e("ProgressReport", "User ID not found in intent extras")
        }
    }

    private fun getUserIdFromIntent(): Int {
        return intent.getIntExtra("USER_ID", -1) // -1 is the default value if USER_ID is not found
    }

    private fun fetchProgressData(userId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://192.168.56.1/seniordes/progCalc.php")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true

                // Prepare POST data
                val postData = "userID=$userId" // Construct POST data
                urlConnection.outputStream.write(postData.toByteArray(Charsets.UTF_8))

                // Get response
                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseData = urlConnection.inputStream.bufferedReader().readText()

                    // Handle response data
                    handleProgressDataResponse(responseData)
                } else {
                    // Handle error
                    Log.e("ProgressReport", "HTTP error code: $responseCode")
                }
            } catch (e: IOException) {
                Log.e("ProgressReport", "Error fetching progress data: ${e.message}")
            }
        }
    }

    private fun handleProgressDataResponse(responseData: String) {
        try {
            if (responseData.isEmpty()) {
                runOnUiThread {
                    setCardViewsGray()
                }
            } else {
                val jsonObject = JSONObject(responseData)
                var totalProgress = 0

                val game01Progress = jsonObject.optInt("1", 0)
                val game02Progress = jsonObject.optInt("2", 0)
                val game03Progress = jsonObject.optInt("3", 0)

                totalProgress += game01Progress + game02Progress + game03Progress

                runOnUiThread {
                    updateCardView(game01wa, game01tt, game01Progress)
                    updateCardView(game02wa, game02tt, game02Progress)
                    updateCardView(game03wa, game03tt, game03Progress)

                    if (totalProgress != 300) {
                        setCardViewsGray()
                    } else {
                        setCardViewsColored()
                    }
                }
            }
        } catch (e: JSONException) {
            Log.e("ProgressReport", "Error parsing JSON: ${e.message}")
        }
    }

    private fun updateCardView(waCardView: CardView, ttCardView: CardView, progress: Int) {
        if (progress == 100) {
            waCardView.setCardBackgroundColor(Color.parseColor("#87CFD8"))
            ttCardView.setCardBackgroundColor(Color.parseColor("#87CFD8"))
        } else {
            waCardView.setCardBackgroundColor(Color.parseColor("#BEBEBE"))
            ttCardView.setCardBackgroundColor(Color.parseColor("#BEBEBE"))
        }
    }

    private fun setCardViewsGray() {
        game01wa.setCardBackgroundColor(Color.parseColor("#BEBEBE"))
        game01tt.setCardBackgroundColor(Color.parseColor("#BEBEBE"))
        game02wa.setCardBackgroundColor(Color.parseColor("#BEBEBE"))
        game02tt.setCardBackgroundColor(Color.parseColor("#BEBEBE"))
        game03wa.setCardBackgroundColor(Color.parseColor("#BEBEBE"))
        game03tt.setCardBackgroundColor(Color.parseColor("#BEBEBE"))
        // Add more CardViews if needed
    }

    private fun setCardViewsColored() {
        game01wa.setCardBackgroundColor(Color.parseColor("#87CFD8"))
        game01tt.setCardBackgroundColor(Color.parseColor("#87CFD8"))
        game02wa.setCardBackgroundColor(Color.parseColor("#D490DF"))
        game02tt.setCardBackgroundColor(Color.parseColor("#D490DF"))
        game03wa.setCardBackgroundColor(Color.parseColor("#FE9DB0"))
        game03tt.setCardBackgroundColor(Color.parseColor("#FE9DB0"))
        // Add more CardViews if needed
    }
}