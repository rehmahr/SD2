package com.example.sd2

import android.os.Bundle
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_game3_lev1.*



class Game3Lev1 : AppCompatActivity() {
    private var offsetX = 0f
    private var offsetY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game3_lev1)

        // Set onTouchListener to enable drag and drop for all images
        arrayOf(matchSad, matchHappy, matchSurprised).forEach { imageView ->
            imageView.setOnTouchListener { view, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    val data = ClipData.newPlainText("", "")
                    val shadowBuilder = View.DragShadowBuilder(view)
                    view.startDragAndDrop(data, shadowBuilder, view, 0)
                    view.visibility = View.VISIBLE
                    offsetX = event.x - view.x
                    offsetY = event.y - view.y
                    true
                } else {
                    false
                }
            }
        }

        // Set onDragListener to handle dropping
        matchEmptyFace.setOnDragListener { _, event ->
            when (event.action) {
                DragEvent.ACTION_DROP -> {
                    // Handle the drop event
                    val view = event.localState as View
                    view.x = event.x - offsetX
                    view.y = event.y - offsetY

                    // Check if dropped on correct target
                    if ((view == matchSad && matchEmptyFace == findViewById(R.id.matchSad)) ||
                        (view == matchHappy && matchEmptyFace == findViewById(R.id.matchHappy)) ||
                        (view == matchSurprised && matchEmptyFace == findViewById(R.id.matchSurprised))
                    ) {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Try AGAIN", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }
    }
}