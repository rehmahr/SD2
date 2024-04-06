package com.example.sd2

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

data class DraggableEmotion(
    val imageViewId: Int,
    val emotionDrawableId: Int,
    val emotionWord: String
)

class Game3Lev1 : AppCompatActivity() {
    private lateinit var emotionWordTextView: TextView
    private lateinit var faceGap: ImageView
    private lateinit var matchEmptyFace: ImageView

    private lateinit var currentEmotionWord: String
    private val draggableEmotions = listOf(
        DraggableEmotion(R.id.matchSad, R.drawable.sad_face, "SAD"),
        DraggableEmotion(R.id.matchHappy, R.drawable.happy_face, "HAPPY"),
        DraggableEmotion(R.id.matchSurprised, R.drawable.suprised_face, "SURPRISED")
    )

    private val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game3_lev1)

        emotionWordTextView = findViewById(R.id.emotionWord)
        faceGap = findViewById(R.id.faceGap)
        matchEmptyFace = findViewById(R.id.matchEmptyFace)

        setDragListener(matchEmptyFace)

        val draggableViews = listOf(R.id.matchSad, R.id.matchHappy, R.id.matchSurprised)
        draggableViews.forEach { viewId ->
            findViewById<ImageView>(viewId).setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val dragShadowBuilder = View.DragShadowBuilder(view)
                    view.startDrag(null, dragShadowBuilder, view, 0)
                    view.visibility = View.INVISIBLE
                    currentEmotionWord = draggableEmotions.find { it.imageViewId == viewId }?.emotionWord ?: ""
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setDragListener(targetView: ImageView) {
        targetView.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    val draggedView = event.localState as ImageView

                    val draggedEmotion = draggableEmotions.find { it.imageViewId == draggedView.id }

                    if (draggedEmotion?.emotionWord == emotionWordTextView.text.toString()) {
                        faceGap.setImageResource(draggedEmotion.emotionDrawableId)
                        showMessage("Correct!")
                        handler.postDelayed({
                            resetGame()
                        }, 3000)
                    } else {
                        showMessage("Try again!")
                        draggedView.visibility = View.VISIBLE
                    }
                    true
                }
                else -> true
            }
        }
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private var currentEmotionIndex = 0

    private fun resetGame() {
        currentEmotionIndex++
        if (currentEmotionIndex < draggableEmotions.size) {
            val newEmotion = draggableEmotions[currentEmotionIndex]
            emotionWordTextView.text = newEmotion.emotionWord
            draggableEmotions.shuffled()
            faceGap.setImageResource(android.R.drawable.ic_menu_help)
            val draggableViews = listOf(R.id.matchSad, R.id.matchHappy, R.id.matchSurprised)
            draggableViews.forEach { viewId ->
                findViewById<ImageView>(viewId).visibility = View.VISIBLE
            }
        } else {
            // All emotions completed, navigate to Congratulations activity
            val intent = Intent(this, Congratulations::class.java)
            intent.putExtra("CURRENT_LEVEL", "Game3Lev1")
            startActivity(intent)
            finish()
        }
    }
}