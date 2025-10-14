package com.example.stepup

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stepup.databinding.ActivityStatisticsBinding

class StatisticsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStatisticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStatisticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.statsTextView.text = statsText

        binding.buttonBack.setOnClickListener {
            val main = android.content.Intent(this, MainActivity::class.java)
            startActivity(main)
        }
    }
}