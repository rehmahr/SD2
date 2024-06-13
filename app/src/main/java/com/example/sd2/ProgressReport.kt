package com.example.sd2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class ProgressReport : AppCompatActivity() {

    private lateinit var game1DisableButton: Button
    private lateinit var game2DisableButton: Button
    private lateinit var game3DisableButton: Button

    private val gameStatusViewModel: GameStatusViewModel by viewModels()
    private fun getUserIdFromIntent(): Int {
        return intent.getIntExtra("USER_ID", -1) // -1 is the default value if USER_ID is not found
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_report)

        val userId = getUserIdFromIntent()
        if (userId != -1) {
            // Fetch progress data for the specific user
            fetchProgressData(userId)
        } else {
            Log.e("ProgressReport", "User ID not found in intent extras")
            showToast("User ID not found")
            finish()
        }

        game1DisableButton = findViewById(R.id.disableGame01)
        game2DisableButton = findViewById(R.id.disableGame02)
        game3DisableButton = findViewById(R.id.disableGame03)

        game1DisableButton.setOnClickListener {
            gameStatusViewModel.game1Enabled.value = false
            // Update UI to reflect the disabled state
        }

        game2DisableButton.setOnClickListener {
            gameStatusViewModel.game2Enabled.value = false
            // Update UI to reflect the disabled state
        }

        game3DisableButton.setOnClickListener {
            gameStatusViewModel.game3Enabled.value = false
            // Update UI to reflect the disabled state
        }
    }

    private fun fetchProgressData(userId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://192.168.0.192/seniordes/progCalc.php")
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

                    // Check if response is valid JSON
                    if (responseData.isNotEmpty() && responseData.startsWith("{")) {
                        // Response is JSON object
                        val jsonObject = JSONObject(responseData)

                        if (jsonObject.has("error")) {
                            // User not found in the table, handle this case
                            Log.e("ProgressReport", "User not found in the table")
                            showToast("User not found")
                            runOnUiThread {
                                changeCardViewsToGray()
                            }
                        } else {
                            // Iterate over game IDs and update card views accordingly
                            val gameIds = listOf(1, 2, 3) // Assuming game IDs 1, 2, 3
                            runOnUiThread {
                                gameIds.forEach { gameId ->
                                    val progress = jsonObject.optDouble(gameId.toString(), 0.0).toFloat()
                                    updateCardView(getCardViewId(gameId), progress)
                                }
                            }
                            // Fetch additional data (total wrong answers and total time taken)
                            fetchAdditionalData(userId)
                        }
                    } else {
                        // Response is not a JSON object, handle this case
                        Log.e("ProgressReport", "Invalid response format")
                    }
                } else {
                    // HTTP error, handle this case
                    Log.e("ProgressReport", "HTTP error code: $responseCode")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ProgressReport", "Error fetching progress data: ${e.message}")
            }
        }
    }

    private fun fetchAdditionalData(userId: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://192.168.56.1/seniordes/progRepGet.php")
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

                    // Check if response is valid JSON
                    if (responseData.isNotEmpty() && responseData.startsWith("{")) {
                        // Response is JSON object
                        val jsonObject = JSONObject(responseData)

                        if (jsonObject.has("error")) {
                            // User not found in the table, handle this case
                            Log.e("ProgressReport", "User not found in the table")
                            showToast("User not found")
                        } else {
                            // Iterate over game IDs and update text views accordingly
                            val gameIds = listOf(1, 2, 3) // Assuming game IDs 1, 2, 3
                            runOnUiThread {
                                gameIds.forEach { gameId ->
                                    val gameData = jsonObject.optJSONObject("$gameId")
                                    val totalWrongAnswers = gameData?.optInt("totalWrongAnswers", 0)
                                    val totalTimeTaken = gameData?.optString("totalTimeTaken", "00:00:00")
                                    val formattedTime = formatTime(totalTimeTaken)
                                    updateTextViews(gameId, totalWrongAnswers ?: 0, formattedTime)
                                }
                            }
                        }
                    } else {
                        // Response is not a JSON object, handle this case
                        Log.e("ProgressReport", "Invalid response format")
                    }
                } else {
                    // HTTP error, handle this case
                    Log.e("ProgressReport", "HTTP error code: $responseCode")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ProgressReport", "Error fetching additional data: ${e.message}")
            }
        }
    }

    private fun formatTime(time: String?): String {
        if (time.isNullOrEmpty()) return "00:00" // Return default if time is null or empty
        val parts = time.split(":")
        if (parts.size >= 2) {
            val minutes = parts[0].toIntOrNull() ?: 0
            val seconds = parts[1].toIntOrNull() ?: 0
            return String.format("%02d:%02d", minutes, seconds)
        }
        return "00:00" // Return default if time format is invalid
    }



    private fun updateTextViews(gameId: Int, totalWrongAnswers: Int, totalTimeTaken: String) {
        val textViewWrongAnswers = findViewById<TextView>(getTextViewId(gameId, "wrongAnswers"))
        val textViewTimeTaken = findViewById<TextView>(getTextViewId(gameId, "timeTaken"))

        textViewWrongAnswers.text = "$totalWrongAnswers"
        textViewTimeTaken.text = "$totalTimeTaken"
    }

    private fun getTextViewId(gameId: Int, dataType: String): Int {
        return when (gameId) {
            1 -> when (dataType) {
                "wrongAnswers" -> R.id.game01WrongAnswers
                "timeTaken" -> R.id.game01TimeTaken
                else -> throw IllegalArgumentException("Invalid data type")
            }
            2 -> when (dataType) {
                "wrongAnswers" -> R.id.game02WrongAnswers
                "timeTaken" -> R.id.game02TimeTaken
                else -> throw IllegalArgumentException("Invalid data type")
            }
            3 -> when (dataType) {
                "wrongAnswers" -> R.id.game03WrongAnswers
                "timeTaken" -> R.id.game03TimeTaken
                else -> throw IllegalArgumentException("Invalid data type")
            }
            else -> throw IllegalArgumentException("Invalid game ID")
        }
    }

    private fun getCardViewId(gameId: Int): Int {
        return when (gameId) {
            1 -> R.id.game01
            2 -> R.id.game02
            3 -> R.id.overallScores
            else -> throw IllegalArgumentException("Invalid game ID")
        }
    }

    private fun updateCardView(gameId: Int, progress: Float) {
        val cardView = findViewById<CardView>(gameId)
        val grayColor = ContextCompat.getColor(this, android.R.color.darker_gray)
        if (progress < 100f) {
            runOnUiThread {
                cardView.setCardBackgroundColor(grayColor)
            }
        }
    }

    private fun changeCardViewsToGray() {
        val grayColor = ContextCompat.getColor(this, android.R.color.darker_gray)
        val gameIds = listOf(R.id.game01, R.id.game02, R.id.overallScores) // Assuming game IDs 1, 2, 3
        runOnUiThread {
            gameIds.forEach { gameId ->
                findViewById<CardView>(gameId)?.setCardBackgroundColor(grayColor)
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }
}