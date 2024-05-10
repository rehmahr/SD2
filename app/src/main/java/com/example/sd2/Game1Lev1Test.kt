package com.example.sd2

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.concurrent.schedule
class Game1Lev1Test : AppCompatActivity() {

    private var startTime: Long = 0
    private var endTime: Long = 0

    private val images = listOf(
        R.drawable.happy,
        R.drawable.sad,
        R.drawable.angry,
        R.drawable.surprised
    ).shuffled()

    private lateinit var gamePanel: View
    private lateinit var congratsPanel: View
    private lateinit var imageView: ImageView
    private lateinit var happyButton: Button
    private lateinit var sadButton: Button
    private lateinit var angryButton: Button
    private lateinit var surprisedButton: Button
    private lateinit var proceedButton: Button
    private lateinit var bgmMediaPlayer: MediaPlayer


    private var currentIndex = -1
    private var mistakes = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game1_lev1_test)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        gamePanel = findViewById(R.id.gamePanel)
        congratsPanel = findViewById(R.id.congratsPanel)
        imageView = findViewById(R.id.imageView2)
        happyButton = findViewById(R.id.happyButt)
        sadButton = findViewById(R.id.sadButt)
        angryButton = findViewById(R.id.angryButt)
        surprisedButton = findViewById(R.id.surprisedButt)
        proceedButton = findViewById(R.id.button)

        // Start game
        nextImage()

        // Set click listeners
        happyButton.setOnClickListener { checkAnswer("happy") }
        sadButton.setOnClickListener { checkAnswer("sad") }
        angryButton.setOnClickListener { checkAnswer("angry") }
        surprisedButton.setOnClickListener { checkAnswer("surprised") }

        startTime = System.currentTimeMillis()

    }

    private fun nextImage() {
        currentIndex = (currentIndex + 1) % images.size
        val imageResource = images[currentIndex]
        imageView.setImageResource(imageResource)
    }

    private fun checkAnswer(guess: String) {
        val correctAnswer = getImageName(images[currentIndex])
        if (guess == correctAnswer) {
            // Correct answer
            nextImage()
            if (currentIndex == 0) {
                // All images shown, show congratulations panel and save score
                saveScoreToDatabase()

                    val progress = 40
                    val userID = (application as MyApp).userID

                    saveProgressToDatabase(userID, 1, 1, progress)

                    val intent = Intent(this, Congratulations::class.java)
                    intent.putExtra("CURRENT_LEVEL", "Game1Lev1Test")
                    startActivity(intent)
                    finish()

            }
        } else {
            // Incorrect answer
            mistakes++
        }
    }

    private fun saveScoreToDatabase() {

        endTime = System.currentTimeMillis()
        val timeTaken = endTime - startTime

        val minutes = (timeTaken / 1000) / 60
        val seconds = (timeTaken / 1000) % 60

        // Format time as mm:ss
        val formattedTime = String.format("%02d:%02d", minutes, seconds)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val userID = (application as MyApp).userID
                println(userID)
                val gameID = 1 // Assuming gameID for game1 is 1
                val levelID = 1 // Assuming levelID for level1 is 1

                val url = URL("http://192.168.56.1/seniordes/g1l1test.php")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.doOutput = true
                urlConnection.requestMethod = "POST"

                // Construct POST data
                val postData = "userID=$userID&gameID=$gameID&levelID=$levelID&mistakes=$mistakes&time=$formattedTime"
                println(postData)
                urlConnection.outputStream.write(postData.toByteArray(Charsets.UTF_8))

                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Score saved successfully
                    println("Score saved successfully")
                } else {
                    // Error saving score
                    println("Error saving score")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun getImageName(imageResId: Int): String {
        return resources.getResourceEntryName(imageResId)
    }

    override fun onPause() {
        bgmMediaPlayer.stop() // Stop background music when activity is stopped
        bgmMediaPlayer.release()
        super.onPause()
    }
}