package com.example.stepup

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.stepup.databinding.ActivityMainBinding
import com.example.stepup.databinding.CardHabitBinding
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val prefs by lazy { getSharedPreferences("habits_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSavedHabits()

        binding.addHabitButton.setOnClickListener {
            val habitId = System.currentTimeMillis().toString()
            addNewHabit("Título del hábito", "Descripción del hábito", habitId, isNew = true)
        }

        binding.habitsStats.setOnClickListener {
            val intent = android.content.Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun addNewHabit(title: String, description: String, habitId: String, isNew: Boolean) {
        val habitCardBinding = CardHabitBinding.inflate(LayoutInflater.from(this))

        val marginInDp = 16
        val scale = resources.displayMetrics.density
        val marginInPx = (marginInDp * scale + 0.5f).toInt()
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(0, marginInPx, 0, 0)
        binding.habitContainer.addView(habitCardBinding.root, layoutParams)

        var days = prefs.getInt("${habitId}_days", 0)
        var canAddDay = prefs.getBoolean("${habitId}_canAddDay", true)
        var endTime = prefs.getLong("${habitId}_endTime", 0L)
        var lastAddedDay = prefs.getLong("${habitId}_lastAddedDay", 0L)
        var countDownTimer: CountDownTimer? = null

        habitCardBinding.habitTitle.setText(title)
        habitCardBinding.habitDescription.setText(description)
        habitCardBinding.habitDayCount.text = "Racha de $days días"

        if (isNew) {
            habitCardBinding.habitTitle.isEnabled = true
            habitCardBinding.habitDescription.isEnabled = true
            habitCardBinding.saveHabitButton.visibility = View.VISIBLE
            habitCardBinding.habitDayButton.visibility = View.GONE
        } else {
            habitCardBinding.habitTitle.isEnabled = false
            habitCardBinding.habitDescription.isEnabled = false
            habitCardBinding.saveHabitButton.visibility = View.GONE
            habitCardBinding.habitDayButton.visibility = View.VISIBLE
        }

        if (!isNew && lastAddedDay != 0L) {
            val lastDay = Calendar.getInstance().apply { timeInMillis = lastAddedDay }
            val today = Calendar.getInstance()
            if (today.get(Calendar.YEAR) != lastDay.get(Calendar.YEAR) ||
                today.get(Calendar.DAY_OF_YEAR) != lastDay.get(Calendar.DAY_OF_YEAR)) {
                days = 0
                habitCardBinding.habitDayCount.text = "Racha de $days días"
                saveHabitState(habitId, days, canAddDay, 0L, title, description)
            }
        }

        habitCardBinding.saveHabitButton.setOnClickListener {
            val titleText = habitCardBinding.habitTitle.text.toString()
            val descriptionText = habitCardBinding.habitDescription.text.toString()
            habitCardBinding.habitTitle.isEnabled = false
            habitCardBinding.habitDescription.isEnabled = false
            habitCardBinding.saveHabitButton.visibility = View.GONE
            habitCardBinding.habitDayButton.visibility = View.VISIBLE
            habitCardBinding.habitDayCount.text = "Racha de $days días"
            saveHabitState(habitId, days, canAddDay, endTime, titleText, descriptionText)
        }

        fun startStreakTimer(duration: Long) {
            countDownTimer?.cancel()
            countDownTimer = object : CountDownTimer(duration, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                    val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                    habitCardBinding.habitDayButton.text = String.format("Añadir un día (%02d:%02d:%02d)", hours, minutes, seconds)
                }

                override fun onFinish() {
                    canAddDay = true
                    habitCardBinding.habitDayButton.isEnabled = true
                    habitCardBinding.habitDayButton.text = "Añadir un día"
                    // Si no se pulsó durante el día, reiniciar la racha
                    val today = Calendar.getInstance()
                    val lastDay = Calendar.getInstance().apply { timeInMillis = lastAddedDay }
                    if (today.get(Calendar.YEAR) != lastDay.get(Calendar.YEAR) ||
                        today.get(Calendar.DAY_OF_YEAR) != lastDay.get(Calendar.DAY_OF_YEAR)) {
                        days = 0
                        habitCardBinding.habitDayCount.text = "Racha de $days días"
                    }
                    saveHabitState(habitId, days, canAddDay, 0L, habitCardBinding.habitTitle.text.toString(), habitCardBinding.habitDescription.text.toString())
                }
            }.start()
        }

        if (!canAddDay) {
            val remainingTime = endTime - System.currentTimeMillis()
            val adjustedTime = if (remainingTime > getMillisUntilMidnight()) getMillisUntilMidnight() else remainingTime
            if (adjustedTime > 0) {
                habitCardBinding.habitDayButton.isEnabled = false
                startStreakTimer(adjustedTime)
            } else {
                canAddDay = true
                habitCardBinding.habitDayButton.isEnabled = true
                habitCardBinding.habitDayButton.text = "Añadir un día"
            }
        }

        habitCardBinding.removeHabits.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar este hábito?")
                .setPositiveButton("Sí") { _, _ ->
                    prefs.edit().apply {
                        remove("${habitId}_days")
                        remove("${habitId}_canAddDay")
                        remove("${habitId}_endTime")
                        remove("${habitId}_title")
                        remove("${habitId}_description")
                        remove("${habitId}_lastAddedDay")
                        apply()
                    }
                    binding.habitContainer.removeView(habitCardBinding.root)
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        habitCardBinding.habitDayButton.setOnClickListener {
            if (!canAddDay) {
                return@setOnClickListener
            }

            days++
            canAddDay = false
            lastAddedDay = System.currentTimeMillis()
            habitCardBinding.habitDayButton.isEnabled = false
            habitCardBinding.habitDayCount.text = "Racha de $days días"

            val remainingMillis = getMillisUntilMidnight()
            val endTimestamp = System.currentTimeMillis() + remainingMillis

            saveHabitState(habitId, days, canAddDay, endTimestamp, habitCardBinding.habitTitle.text.toString(), habitCardBinding.habitDescription.text.toString(), lastAddedDay)
            startStreakTimer(remainingMillis)
        }
    }

    private fun getMillisUntilMidnight(): Long {
        val now = Calendar.getInstance()
        val midnight = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_YEAR, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return midnight.timeInMillis - now.timeInMillis
    }

    private fun saveHabitState(habitId: String, days: Int, canAddDay: Boolean, endTime: Long, title: String = "", description: String = "", lastAddedDay: Long = 0L) {
        prefs.edit().apply {
            putInt("${habitId}_days", days)
            putBoolean("${habitId}_canAddDay", canAddDay)
            putLong("${habitId}_endTime", endTime)
            putString("${habitId}_title", title)
            putString("${habitId}_description", description)
            putLong("${habitId}_lastAddedDay", lastAddedDay)
            apply()
        }
    }

    private fun loadSavedHabits() {
        val all = prefs.all
        val uniqueIds = all.keys.mapNotNull { it.substringBefore("_") }.distinct()
        for (id in uniqueIds) {
            val title = prefs.getString("${id}_title", "Título del hábito") ?: "Título del hábito"
            val description = prefs.getString("${id}_description", "Descripción del hábito") ?: "Descripción del hábito"
            addNewHabit(title, description, id, isNew = false)
        }
    }
}