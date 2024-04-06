package com.example.sd2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class Game2Lev14 : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var continueButton: Button
    private lateinit var happyButton: Button
    private lateinit var angryButton: Button
    private lateinit var scaredButton: Button
    private lateinit var sadButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2_lev14)

        videoView = findViewById(R.id.videoView)
        happyButton = findViewById(R.id.happy_ans)
        angryButton = findViewById(R.id.angry_ans)
        scaredButton = findViewById(R.id.scared_ans)
        sadButton = findViewById(R.id.sad_ans)

        val offlineUri: Uri = Uri.parse("android.resource://$packageName/${R.raw.scared_lev1}")
        videoView.setVideoURI(offlineUri)

        setupMediaControls()

        val imageButton5 = findViewById<ImageButton>(R.id.home_button)

        happyButton.setOnClickListener { checkAnswer("happy") }
        angryButton.setOnClickListener { checkAnswer("angry") }
        scaredButton.setOnClickListener { checkAnswer("scared") }
        sadButton.setOnClickListener { checkAnswer("sad") }

        imageButton5.setOnClickListener {

            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)

        }

        videoView.setOnCompletionListener {
            val intent = Intent(this, Congratulations::class.java)
            intent.putExtra("CURRENT_LEVEL", "Game2Lev14")
            startActivity(intent)
            finish()

        }

    }

    private fun setupMediaControls() {
        findViewById<ImageButton>(R.id.play).setOnClickListener {
            if (!videoView.isPlaying) {
                videoView.start()
            }
        }

        findViewById<ImageButton>(R.id.pause).setOnClickListener {
            if (videoView.isPlaying) {
                videoView.pause()
            }
        }

        findViewById<ImageButton>(R.id.rewind).setOnClickListener {
            videoView.seekTo(videoView.currentPosition - 5000)
        }

        findViewById<ImageButton>(R.id.forward).setOnClickListener {
            videoView.seekTo(videoView.currentPosition + 5000)
        }
    }

    private fun checkAnswer(selectedOption: String) {
        val correctOption = getCorrectOptionFromRawFileName()
        Log.d("Game2Lev1", "Correct option: $correctOption, Selected option: $selectedOption")



        if (selectedOption == correctOption) {
            // Set the selected button to green
            setOtherButtonsGray(selectedOption)

            val greenColor = 0xFF00FF00.toInt() // Green color
            when (selectedOption) {
                "happy" -> happyButton.setBackgroundColor(greenColor)
                "angry" -> angryButton.setBackgroundColor(greenColor)
                "scared" -> scaredButton.setBackgroundColor(greenColor)
                "sad" -> sadButton.setBackgroundColor(greenColor)
            }
            // Set other buttons to gray

        } else {
            // Show toast message to try again
            Toast.makeText(this, "Try again!", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setOtherButtonsGray(selectedOption: String) {
        val buttons = listOf(happyButton, angryButton, scaredButton, sadButton)
        for (button in buttons) {
            if (button.tag != selectedOption) {
                button.setBackgroundColor(0xFFCCCCCC.toInt()) // Gray color
            }
        }
    }

    private fun getCorrectOptionFromRawFileName(): String {

        return "scared" // Dummy value for now
    }
}