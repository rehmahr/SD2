package com.example.sd2

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class Game1Lev0 : ComponentActivity() {

    private lateinit var bgmMediaPlayer: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game1_lev0)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageButton1 = findViewById<ImageButton>(R.id.imageButton)
        val imageButton2 = findViewById<ImageButton>(R.id.imageButton2)
        val imageButton3 = findViewById<ImageButton>(R.id.imageButton3)
        val imageButton4 = findViewById<ImageButton>(R.id.imageButton4)
        val imageButton5 = findViewById<ImageButton>(R.id.home_button)
        val nextButton = findViewById<Button>(R.id.scaredans)

        imageButton1.setOnClickListener {
            findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.angry)
            playSound(R.raw.angry_audio)
        }

        imageButton2.setOnClickListener {
            findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.happy)
            playSound(R.raw.happy_audio)
        }

        imageButton3.setOnClickListener {
            findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.sad)
            playSound(R.raw.sad_audio)
        }

        imageButton4.setOnClickListener {
            findViewById<ImageView>(R.id.imageView).setImageResource(R.drawable.surprised)
            playSound(R.raw.surprised_audio)
        }

        imageButton5.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }

        nextButton.setOnClickListener {
            val intent = Intent(this, Game1Lev1::class.java)
            startActivity(intent)
        }

        // Initialize background music
        bgmMediaPlayer = MediaPlayer.create(this, R.raw.bgm1)
        bgmMediaPlayer.isLooping = true
        bgmMediaPlayer.setVolume(0.05f, 0.05f) // Set the volume level here (0.5f for half volume, 1.0f is full volume)
        bgmMediaPlayer.start()
    }

    private fun playSound(audioResource: Int) {
        val mediaPlayer = MediaPlayer.create(this, audioResource)
        mediaPlayer?.setVolume(1.5f, 1.5f) // Set the volume level here (0.5f for half volume, 1.0f is full volume)
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener {
            // Release MediaPlayer when playback is completed
            it.release()
        }
    }
}
