package com.example.sd2

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.activity.ComponentActivity

class Game1Lev1 : ComponentActivity() {

    private lateinit var viewFlipper: ViewFlipper
    private lateinit var nextButton: Button
    private lateinit var prevButton: Button
    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var proceedButton: Button

    private val emotions = arrayOf("Happy", "Sad", "Angry", "Surprised")
    private var currentIndex = 0
    private var emotionsVisited = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game1_lev1)

        viewFlipper = findViewById(R.id.viewFlipper)
        nextButton = findViewById(R.id.sadButt)
        prevButton = findViewById(R.id.happyButt)
        imageView = findViewById(R.id.imageView2)
        textView = findViewById(R.id.textView)
        proceedButton = findViewById(R.id.proceedButton)

        // Flip animation (both files)
        val inAnimation = AnimationUtils.loadAnimation(this, R.anim.flip_in)
        val outAnimation = AnimationUtils.loadAnimation(this, R.anim.flip_out)
        viewFlipper.inAnimation = inAnimation
        viewFlipper.outAnimation = outAnimation

        viewFlipper.setOnClickListener {
            viewFlipper.showNext()
            checkEmotionIndex()
        }

        nextButton.setOnClickListener {
            showNextEmotion()
            checkEmotionIndex()
        }

        prevButton.setOnClickListener {
            showPreviousEmotion()
            checkEmotionIndex()
        }

        proceedButton.setOnClickListener {
            goToNextActivity(this, Game1Lev1Test::class.java)
        }

        // Set initial emotion
        updateEmotion()
    }

    private fun showNextEmotion() {
        // Increment index
        currentIndex = (currentIndex + 1) % emotions.size
        // Update emotion
        updateEmotion()
    }

    private fun showPreviousEmotion() {
        // Decrement index
        currentIndex = (currentIndex - 1 + emotions.size) % emotions.size
        // Update emotion
        updateEmotion()
    }

    private fun updateEmotion() {

        val emotion = emotions[currentIndex]
        val drawableId = resources.getIdentifier(emotion.toLowerCase(), "drawable", packageName)
        imageView.setImageResource(drawableId)
        textView.text = emotion
        // Increment emotionsVisited
        emotionsVisited++
    }

    private fun checkEmotionIndex() {
        if (emotionsVisited >= emotions.size) {

            proceedButton.visibility = View.VISIBLE
        } else {
            proceedButton.visibility = View.GONE
        }
    }
}
