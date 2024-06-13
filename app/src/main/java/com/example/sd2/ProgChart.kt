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
import android.widget.FrameLayout
import android.widget.TextView

class ProgChart : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress_chart2)

        // Sample data
        val studentName = "Sami Abdallah"
        val scoresGame1 = listOf(
            Entry(1f, 40f), // Game 1 Day 1
            Entry(2f, 61f), // Game 1 Day 2
            Entry(3f, 61f)  // Game 1 Day 3
        )

        val scoresGame2 = listOf(
            Entry(1f, 76f), // Game 2 Day 1
            Entry(2f, 80f), // Game 2 Day 2
            Entry(3f, 85f)  // Game 2 Day 3
        )

        val scoresGame3 = listOf(
            Entry(1f, 74f), // Game 3 Day 1
            Entry(2f, 70f), // Game 3 Day 2
            Entry(3f, 95f)  // Game 3 Day 3
        )

        // Set student name
        findViewById<TextView>(R.id.studentName).text = studentName

        // Create and configure the line chart
        val lineChart = LineChart(this)

        // LineDataSet for Game 1
        val lineDataSetGame1 = LineDataSet(scoresGame1, "Game 1 Scores")
        lineDataSetGame1.color = Color.BLUE
        lineDataSetGame1.valueTextColor = Color.BLACK
        lineDataSetGame1.valueTextSize = 16f

        // LineDataSet for Game 2
        val lineDataSetGame2 = LineDataSet(scoresGame2, "Game 2 Scores")
        lineDataSetGame2.color = Color.RED
        lineDataSetGame2.valueTextColor = Color.BLACK
        lineDataSetGame2.valueTextSize = 16f

        // LineDataSet for Game 3
        val lineDataSetGame3 = LineDataSet(scoresGame3, "Game 3 Scores")
        lineDataSetGame3.color = Color.GREEN
        lineDataSetGame3.valueTextColor = Color.BLACK
        lineDataSetGame3.valueTextSize = 16f

        val lineData = LineData(lineDataSetGame1, lineDataSetGame2, lineDataSetGame3)
        lineChart.data = lineData

        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.xAxis.granularity = 1f
        lineChart.xAxis.setValueFormatter(object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return when (value) {
                    1f -> " Day 1 "
                    2f -> " Day 2 "
                    3f -> " Day 3 "
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
