package com.example.sd2

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class Dashboard : ComponentActivity() {

    private lateinit var game1_butt: Button
    private lateinit var game2_butt: Button
    private lateinit var game3_butt: Button
    private lateinit var pieChart1: PieChart
    private lateinit var pieChart2: PieChart
    private lateinit var pieChart3: PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        game1_butt = findViewById(R.id.button6)
        game2_butt = findViewById(R.id.button7)
        game3_butt = findViewById(R.id.button8)

        game1_butt.setOnClickListener {
            goToNextActivity(this, Game1Lev0::class.java)
        }

        game2_butt.setOnClickListener {
            goToNextActivity(this, Game2Lev1::class.java)
        }

        game3_butt.setOnClickListener {
            goToNextActivity(this, Game3Lev1::class.java)
        }

        pieChart1 = findViewById(R.id.pieChart1)
        //pieChart2 = findViewById(R.id.pieChart2)
        //pieChart3 = findViewById(R.id.pieChart3)

        // Fetch progress data from the server
        fetchProgressData()
    }

    private fun fetchProgressData() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val url = URL("http://192.168.56.1/seniordes/progCalc.php")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "POST"
                urlConnection.doOutput = true

                // Prepare POST data
                val userID = (application as MyApp).userID // Get userID from MyApp class
                val postData = "userID=$userID" // Construct POST data
                urlConnection.outputStream.write(postData.toByteArray(Charsets.UTF_8))

                // Get response
                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val responseData = urlConnection.inputStream.bufferedReader().readText()

                    // Parse JSON response as JSONObject
                    val jsonObject = JSONObject(responseData)

                    // Iterate over keys (game IDs) in the JSONObject
                    val keysIterator = jsonObject.keys()
                    while (keysIterator.hasNext()) {
                        val gameID = keysIterator.next()
                        val progress = jsonObject.getDouble(gameID).toFloat()

                        println("Game ID: $gameID, Progress: $progress")

                        // Update pie chart based on gameID
                        when (gameID) {
                            "1" -> updatePieChart(pieChart1, progress)
                            //"2" -> updatePieChart(pieChart2, progress)
                            //"3" -> updatePieChart(pieChart3, progress)
                            // Add more cases if you have more games
                        }
                    }
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun updatePieChart(pieChart: PieChart, progress: Float) {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(progress, "Completed"))
        entries.add(PieEntry(100 - progress, "Remaining"))

        val dataSet = PieDataSet(entries, "Pie Chart Data")
        dataSet.setColors(Color.parseColor("#A06CD5"), Color.parseColor("#6247AA"))
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setDrawEntryLabels(true)
        pieChart.invalidate()
    }
}
