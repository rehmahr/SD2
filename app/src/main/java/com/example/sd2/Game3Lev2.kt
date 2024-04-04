package com.example.sd2

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class Game3Lev2 : AppCompatActivity() {
    private lateinit var buttons: List<ImageButton>
    private lateinit var cards: List<MemoryCard>
    private var indexOfSingleSelectedCard: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game3_lev2)

        val images = mutableListOf(
            R.drawable.happy_side, R.drawable.surprised_side,
            R.drawable.angry_word, R.drawable.surprised_word,
            R.drawable.sad_word, R.drawable.sad_side,
            R.drawable.angry_side, R.drawable.happy_word
        )
        // Add each image twice so we can create pairs
        images.addAll(images)
        // Randomize the order of images
        images.shuffle()

        buttons = listOf(
            findViewById(R.id.front_side1), findViewById(R.id.front_side2),
            findViewById(R.id.front_side3), findViewById(R.id.front_side4),
            findViewById(R.id.front_side5), findViewById(R.id.front_side6),
            findViewById(R.id.front_side7), findViewById(R.id.front_side8)
        )

        cards = buttons.indices.map { index ->
            MemoryCard(images[index])
        }

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                // Update models
                updateModels(index)
                // Update the UI for the game
                updateViews()
            }
        }
    }

    private fun updateViews() {
        cards.forEachIndexed { index, card ->
            val button = buttons[index]
            if (card.isMatched) {
                button.alpha = 0.1f
            }
            button.setImageResource(if (card.isFaceUp) card.identifier else R.drawable.front_side)
        }
    }

    private fun updateModels(position: Int) {
        val card = cards[position]
        // Error checking:
        if (card.isFaceUp) {
            Toast.makeText(this, "Invalid move!", Toast.LENGTH_SHORT).show()
            return
        }
        // Two cases
        // 0 or 2 cards selected previously => restore cards + flip over the selected card
        // 1 card selected previously => flip over the selected card + check if the images match
        if (indexOfSingleSelectedCard == null) {
            // 0 or 2 selected cards previously
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            // exactly 1 card was selected previously
            checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp = !card.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.isMatched) {
                card.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {
        val card1 = cards[position1]
        val card2 = cards[position2]

        // Check if the identifiers match
        if (card1.identifier == card2.identifier) {
            Toast.makeText(this, "Match found!!", Toast.LENGTH_SHORT).show()
            card1.isMatched = true
            card2.isMatched = true
        } else {
            // If the identifiers don't match, flip the cards back
            card1.isFaceUp = false
            card2.isFaceUp = false
        }
    }

    data class MemoryCard(val identifier: Int, var isFaceUp: Boolean = false, var isMatched: Boolean = false)
}