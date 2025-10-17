package com.example.stepup

import android.content.Intent
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

        // Variables base
        var totalHabits = 0
        var totalDays = 0
        var maxStreak = 0
        var maxStreakHabit = ""
        var activeHabits = 0
        var completedHabits = 0
        var abandonedHabits = 0
        var mostRecentHabit = ""
        var mostRecentTime = 0L

        // Recorrer todos los hábitos
        for (id in uniqueIds) {
            val days = prefs.getInt("${id}_days", 0)
            val title = prefs.getString("${id}_title", "Hábito") ?: "Hábito"
            val canAdd = prefs.getBoolean("${id}_canAddDay", true)
            val endTime = prefs.getLong("${id}_endTime", 0L)
            val lastAddedDay = prefs.getLong("${id}_lastAddedDay", 0L)

            totalHabits++
            totalDays += days

            if (days > maxStreak) {
                maxStreak = days
                maxStreakHabit = title
            }

            if (canAdd) activeHabits++ else completedHabits++
            if (endTime > 0L && !canAdd) abandonedHabits++

            if (lastAddedDay > mostRecentTime) {
                mostRecentTime = lastAddedDay
                mostRecentHabit = title
            }
        }

        val avgDays = if (totalHabits > 0) totalDays / totalHabits else 0
        val points = totalDays

        val statsText = """
            📊 ESTADÍSTICAS GENERALES
            -------------------------
            Total de hábitos: $totalHabits
            Hábitos completados: $completedHabits
            Hábitos abandonados: $abandonedHabits

            💪 PROGRESO
            -----------
            Total de días acumulados: $totalDays
            Promedio de días por hábito: $avgDays
            Mayor racha: $maxStreak días ($maxStreakHabit)
            Hábito más reciente: ${if (mostRecentHabit.isNotEmpty()) mostRecentHabit else "N/A"}

            🏆 MOTIVACIÓN
            --------------
            Puntos totales: $points XP
            Nivel estimado: ${points / 50 + 1}
        """.trimIndent()

        binding.statsTextView.text = statsText

        binding.buttonBack.setOnClickListener {
            val main = Intent(this, MainActivity::class.java)
            startActivity(main)
        }
    }
}