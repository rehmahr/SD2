import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.example.sd2.Dashboard
import com.example.sd2.Game2lev12
import com.example.sd2.R

class Game2Lev03 : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private lateinit var continueButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game2_lev0)

        videoView = findViewById(R.id.videoView)


        // Directly specify the video file name within the URI string
        val offlineUri: Uri = Uri.parse("android.resource://$packageName/${R.raw.angry_levl0}")
        videoView.setVideoURI(offlineUri)

        setupMediaControls()

        videoView.setOnCompletionListener {
            showContinueButton()
        }

        val imageButton5 = findViewById<ImageButton>(R.id.home_button)
        imageButton5.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
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
        continueButton = findViewById(R.id.continueBtn)
        continueButton.visibility = View.VISIBLE


        continueButton.setOnClickListener {
            val intent = Intent(this, Game2lev12::class.java)
            startActivity(intent)
            finish()
        }
    }


}