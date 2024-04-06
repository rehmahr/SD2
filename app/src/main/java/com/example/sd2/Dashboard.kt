package com.example.sd2

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class Dashboard : ComponentActivity() {

    private lateinit var game1_butt: Button
    private lateinit var game2_butt: Button
    private lateinit var game3_butt: Button
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

        val pieChart1: PieChart = findViewById(R.id.pieChart1)
        val pieChart2: PieChart = findViewById(R.id.pieChart2)
        val pieChart3: PieChart = findViewById(R.id.pieChart3)

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(40f, "Green"))
        entries.add(PieEntry(30f, "Red"))
        entries.add(PieEntry(30f, "Blue"))

        val dataSet = PieDataSet(entries, "Pie Chart Data")
        dataSet.setColors(Color.parseColor("#A06CD5"), Color.parseColor("#6247AA"), Color.parseColor("#102B3F"))
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 12f

        val data = PieData(dataSet)
        pieChart1.data = data
        pieChart1.description.isEnabled = false
        pieChart1.isDrawHoleEnabled = true
        pieChart1.setHoleColor(Color.TRANSPARENT)
        pieChart1.setDrawEntryLabels(true)
        pieChart1.invalidate()

        pieChart2.data = data
        pieChart2.description.isEnabled = false
        pieChart2.isDrawHoleEnabled = true
        pieChart2.setHoleColor(Color.TRANSPARENT)
        pieChart2.setDrawEntryLabels(true)
        pieChart2.invalidate()

        pieChart3.data = data
        pieChart3.description.isEnabled = false
        pieChart3.isDrawHoleEnabled = true
        pieChart3.setHoleColor(Color.TRANSPARENT)
        pieChart3.setDrawEntryLabels(true)
        pieChart3.invalidate()
    }
}
