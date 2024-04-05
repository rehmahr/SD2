package com.example.sd2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class Game2lev12 : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var continueButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2_lev12)

        videoView = findViewById(R.id.videoView)

        val offlineUri: Uri = Uri.parse("android.resource://$packageName/${R.raw.angry_lev1}")
        videoView.setVideoURI(offlineUri)

        setupMediaControls()

        val imageButton5 = findViewById<ImageButton>(R.id.home_button)

        imageButton5.setOnClickListener {

            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)

        }

        videoView.setOnCompletionListener {
            showContinueButton()
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

    private fun showContinueButton() {
        continueButton = Button(this)
        continueButton.text = "Continue"
        continueButton.setOnClickListener {
            // Start next activity here
            // Example:
            val intent = Intent(this, Game2Lev13::class.java)
            startActivity(intent)
        }

        val layout: ConstraintLayout = findViewById(R.id.constraintLayout)
        val params: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        params.topToBottom = videoView.id
        params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        params.setMargins(0, 16, 0, 0)
        layout.addView(continueButton, params)
    }
}

