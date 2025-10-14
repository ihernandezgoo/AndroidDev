package com.example.stepup

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class StatisticsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics)

        val statsTextView = findViewById<TextView>(R.id.statsTextView)

        val prefs = getSharedPreferences("habits_prefs", MODE_PRIVATE)
        val all = prefs.all
        val uniqueIds = all.keys.mapNotNull { it.substringBefore("_") }.distinct()

        var totalHabits = 0
        var totalDays = 0
        var maxStreak = 0
        var maxStreakHabit = ""

        for (id in uniqueIds) {
            val days = prefs.getInt("${id}_days", 0)
            val title = prefs.getString("${id}_title", "Hábito") ?: "Hábito"

            totalHabits++
            totalDays += days

            if (days > maxStreak) {
                maxStreak = days
                maxStreakHabit = title
            }
        }

        val statsText = """
            Total de hábitos: $totalHabits
            Total de días acumulados: $totalDays
            Mayor racha: $maxStreak días (${maxStreakHabit})
        """.trimIndent()

        statsTextView.text = statsText
    }
}