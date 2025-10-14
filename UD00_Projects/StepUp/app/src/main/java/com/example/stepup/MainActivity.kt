package com.example.stepup

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.stepup.databinding.ActivityMainBinding
import com.example.stepup.databinding.CardHabitBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val prefs by lazy { getSharedPreferences("habits_prefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Cargar hábitos guardados
        loadSavedHabits()

        // Botón para añadir nuevo hábito
        binding.addHabitButton.setOnClickListener {
            val habitId = System.currentTimeMillis().toString()
            addNewHabit("Título del hábito", "Descripción del hábito", habitId, isNew = true)
        }

        binding.habitsStats.setOnClickListener {
            val intent = android.content.Intent(this, StatisticsActivity::class.java)
            startActivity(intent)
        }
    }
    private fun addNewHabit(
        title: String,
        description: String,
        habitId: String,
        isNew: Boolean
    ) {
        val habitCardBinding = CardHabitBinding.inflate(LayoutInflater.from(this))
        binding.habitContainer.addView(habitCardBinding.root)

        var days = prefs.getInt("${habitId}_days", 0)
        var canAddDay = prefs.getBoolean("${habitId}_canAddDay", true)
        var endTime = prefs.getLong("${habitId}_endTime", 0L)
        var countDownTimer: CountDownTimer? = null

        // Estado inicial de los campos
        habitCardBinding.habitTitle.setText(title)
        habitCardBinding.habitDescription.setText(description)
        habitCardBinding.habitDayCount.text = "Racha de $days días"

        if (isNew) {
            // Nuevo hábito → se puede editar
            habitCardBinding.habitTitle.isEnabled = true
            habitCardBinding.habitDescription.isEnabled = true
            habitCardBinding.saveHabitButton.visibility = View.VISIBLE
            habitCardBinding.habitDayButton.visibility = View.GONE
        } else {
            // Hábito cargado desde memoria → bloqueado
            habitCardBinding.habitTitle.isEnabled = false
            habitCardBinding.habitDescription.isEnabled = false
            habitCardBinding.saveHabitButton.visibility = View.GONE
            habitCardBinding.habitDayButton.visibility = View.VISIBLE
        }

        // Guardar hábito (bloquea edición y muestra el botón de día)
        habitCardBinding.saveHabitButton.setOnClickListener {
            val titleText = habitCardBinding.habitTitle.text.toString()
            val descriptionText = habitCardBinding.habitDescription.text.toString()

            habitCardBinding.habitTitle.isEnabled = false
            habitCardBinding.habitDescription.isEnabled = false
            habitCardBinding.saveHabitButton.visibility = View.GONE
            habitCardBinding.habitDayButton.visibility = View.VISIBLE
            habitCardBinding.habitDayCount.text = "Racha de $days días"

            saveHabitState(habitId, days, canAddDay, endTime, titleText, descriptionText)

            Toast.makeText(this, "Hábito guardado ✅", Toast.LENGTH_SHORT).show()
        }

        // Si el contador estaba activo al cerrar la app, reanudarlo
        if (!canAddDay) {
            val remainingTime = endTime - System.currentTimeMillis()
            if (remainingTime > 0) {
                countDownTimer = start24hTimer(habitCardBinding, remainingTime) {
                    canAddDay = true
                    saveHabitState(habitId, days, canAddDay, 0L)
                    habitCardBinding.habitDayButton.isEnabled = true
                    habitCardBinding.habitDayButton.text = "Añadir un día"
                    Toast.makeText(this, "¡Ya puedes añadir otro día!", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Si ya pasó el tiempo, reactivar el botón
                canAddDay = true
                saveHabitState(habitId, days, canAddDay, 0L)
                habitCardBinding.habitDayButton.text = "Añadir un día"
                habitCardBinding.habitDayButton.isEnabled = true
            }
        }

        habitCardBinding.removeHabits.setOnClickListener {
            prefs.edit().apply {
                remove("${habitId}_days")
                remove("${habitId}_canAddDay")
                remove("${habitId}_endTime")
                remove("${habitId}_title")
                remove("${habitId}_description")
                apply()
            }

            binding.habitContainer.removeView(habitCardBinding.root)
            Toast.makeText(this, "Hábito eliminado", Toast.LENGTH_SHORT).show()
        }

        // Lógica del botón "Añadir un día"
        habitCardBinding.habitDayButton.setOnClickListener {
            if (canAddDay) {
                // Añadir día correctamente
                days++
                canAddDay = false
                habitCardBinding.habitDayCount.text = "Racha de $days días"
                habitCardBinding.habitDayButton.isEnabled = false
                Toast.makeText(this, "Nuevo día añadido 🎉", Toast.LENGTH_SHORT).show()

                val endTimestamp = System.currentTimeMillis() + 24 * 60 * 60 * 1000L
                saveHabitState(
                    habitId,
                    days,
                    canAddDay,
                    endTimestamp,
                    habitCardBinding.habitTitle.text.toString(),
                    habitCardBinding.habitDescription.text.toString()
                )

                countDownTimer = start24hTimer(habitCardBinding, 24 * 60 * 60 * 1000L) {
                    canAddDay = true
                    saveHabitState(
                        habitId,
                        days,
                        canAddDay,
                        0L,
                        habitCardBinding.habitTitle.text.toString(),
                        habitCardBinding.habitDescription.text.toString()
                    )
                    habitCardBinding.habitDayButton.isEnabled = true
                    habitCardBinding.habitDayButton.text = "Añadir un día"
                    Toast.makeText(this@MainActivity, "¡Ya puedes añadir otro día!", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Si pulsa antes → reinicia la racha
                days = 0
                canAddDay = false
                habitCardBinding.habitDayCount.text = "Racha de 0 días"
                Toast.makeText(this, "Has roto la racha 😢 Empieza de nuevo.", Toast.LENGTH_SHORT).show()

                countDownTimer?.cancel()
                val endTimestamp = System.currentTimeMillis() + 24 * 60 * 60 * 1000L
                saveHabitState(
                    habitId,
                    days,
                    canAddDay,
                    endTimestamp,
                    habitCardBinding.habitTitle.text.toString(),
                    habitCardBinding.habitDescription.text.toString()
                )

                countDownTimer = start24hTimer(habitCardBinding, 24 * 60 * 60 * 1000L) {
                    canAddDay = true
                    saveHabitState(
                        habitId,
                        days,
                        canAddDay,
                        0L,
                        habitCardBinding.habitTitle.text.toString(),
                        habitCardBinding.habitDescription.text.toString()
                    )
                    habitCardBinding.habitDayButton.isEnabled = true
                    habitCardBinding.habitDayButton.text = "Añadir un día"
                }
            }
        }
    }

    private fun start24hTimer(
        habitCardBinding: CardHabitBinding,
        duration: Long,
        onFinish: () -> Unit
    ): CountDownTimer {
        return object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                habitCardBinding.habitDayButton.text = String.format(
                    "%02d:%02d:%02d", hours, minutes, seconds
                )
            }

            override fun onFinish() {
                onFinish()
            }
        }.start()
    }

    private fun saveHabitState(
        habitId: String,
        days: Int,
        canAddDay: Boolean,
        endTime: Long,
        title: String = "",
        description: String = ""
    ) {
        prefs.edit().apply {
            putInt("${habitId}_days", days)
            putBoolean("${habitId}_canAddDay", canAddDay)
            putLong("${habitId}_endTime", endTime)
            putString("${habitId}_title", title)
            putString("${habitId}_description", description)
            apply()
        }
    }

    private fun loadSavedHabits() {
        val all = prefs.all
        val uniqueIds = all.keys.mapNotNull { it.substringBefore("_") }.distinct()

        for (id in uniqueIds) {
            val title = prefs.getString("${id}_title", "Título del hábito") ?: "Título del hábito"
            val description = prefs.getString("${id}_description", "Descripción del hábito")
                ?: "Descripción del hábito"
            addNewHabit(title, description, id, isNew = false)
        }
    }
}