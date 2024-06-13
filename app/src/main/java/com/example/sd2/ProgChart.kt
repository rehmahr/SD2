package com.example.sd2

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
//import kotlinx.android.synthetic.main.activity_progress_chart2.*
import android.widget.FrameLayout
import android.widget.TextView

class ProgChart : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_chart2)

        // Sample data
        val studentName = "Sami Abdallah"
        val scores = listOf(
            Entry(1f, 70f), // Game 1 Level 1
            Entry(2f, 65f), // Game 1 Level 2
            Entry(3f, 90f)  // Game 2 Level 1
        )

        // Set student name
        findViewById<TextView>(R.id.studentName).text = studentName

        // Create and configure the line chart
        val lineChart = LineChart(this)
        val lineDataSet = LineDataSet(scores, "Average Scores")
        lineDataSet.color = Color.BLUE
        lineDataSet.valueTextColor = Color.BLACK
        lineDataSet.valueTextSize = 16f

        val lineData = LineData(lineDataSet)
        lineChart.data = lineData

        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.granularity = 1f
        lineChart.xAxis.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value) {
                    1f -> " Game1Lvl1 "
                    2f -> " Game1Lvl2 "
                    3f -> " Game2Lvl1 "
                    else -> value.toString()
                }
            }
        })
        lineChart.axisRight.isEnabled = false
        lineChart.description.isEnabled = false

        // Add the line chart to the CardView
        val chartContainer = findViewById<FrameLayout>(R.id.chart_container)
        chartContainer.addView(lineChart)
    }
}
