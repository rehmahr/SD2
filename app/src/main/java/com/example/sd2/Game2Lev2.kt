package com.example.sd2

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

class Game2Lev2 : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var continueButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2_lev2)

        showContinueButton()

        videoView = findViewById(R.id.videoView)

        val offlineUri: Uri = Uri.parse("android.resource://$packageName/${R.raw.g2l2}")
        videoView.setVideoURI(offlineUri)

        setupMediaControls()

        val imageButton5 = findViewById<ImageButton>(R.id.home_button)

        imageButton5.setOnClickListener {

            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)

        }

        videoView.setOnCompletionListener {
            val intent = Intent(this, Congratulations::class.java)
            intent.putExtra("CURRENT_LEVEL", "Game2Lev2")
            startActivity(intent)



            val progress = 10;
            val userID = (application as MyApp).userID

            saveProgressToDatabase(userID, 2, 11, progress)
            finish()

        }
    }

    private fun showContinueButton() {
        continueButton = findViewById(R.id.continueBtn)
        continueButton.visibility = View.VISIBLE

        continueButton.setOnClickListener {
            val intent = Intent(this, Congratulations::class.java)
            startActivity(intent)

            val progress = 10;
            val userID = (application as MyApp).userID

            saveProgressToDatabase(userID, 2, 9, progress)
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
            videoView.seekTo(videoView.currentPosition - 500)
        }

        findViewById<ImageButton>(R.id.forward).setOnClickListener {
            videoView.seekTo(videoView.currentPosition + 500)
        }
    }
}