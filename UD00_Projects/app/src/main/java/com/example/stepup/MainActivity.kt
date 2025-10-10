package com.example.stepup

import android.os.Bundle
import android.view.LayoutInflater
import android.os.CountDownTimer
import android.widget.Toast
import java.util.concurrent.TimeUnit
import androidx.appcompat.app.AppCompatActivity
import com.example.stepup.databinding.ActivityMainBinding
import com.example.stepup.databinding.CardHabitBinding

class MainActivity : AppCompatActivity() {
    

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addHabitButton.setOnClickListener {
            addNewHabit("Titulo del habito", "Descripcion del habito")
        }


    }

    private fun addNewHabit(title: String, description: String) {
        val habitCardBinding = CardHabitBinding.inflate(LayoutInflater.from(this))

        // ✅ Reiniciamos todo el estado visual
        habitCardBinding.habitTitle.isEnabled = true
        habitCardBinding.habitDescription.isEnabled = true
        habitCardBinding.saveHabitButton.visibility = android.view.View.VISIBLE
        habitCardBinding.habitDayButton.visibility = android.view.View.GONE
        habitCardBinding.habitDayCount.text = ""
        habitCardBinding.habitTitle.setText(title)
        habitCardBinding.habitDescription.setText(description)

        var days = 0
        var createButtonClicked = false
        var canAddDay = true

        // Guardar hábito
        habitCardBinding.saveHabitButton.setOnClickListener {
            habitCardBinding.habitTitle.isEnabled = false
            habitCardBinding.habitDescription.isEnabled = false
            habitCardBinding.saveHabitButton.visibility = android.view.View.GONE
            habitCardBinding.habitDayButton.visibility = android.view.View.VISIBLE
            habitCardBinding.habitDayCount.text = "Racha de 0 días"
            createButtonClicked = true
            Toast.makeText(this, "Hábito guardado", Toast.LENGTH_SHORT).show()
        }

        // Añadir un día
        habitCardBinding.habitDayButton.setOnClickListener {
            if (!createButtonClicked) return@setOnClickListener

            if (canAddDay) {
                days++
                habitCardBinding.habitDayCount.text = "Racha de $days días"
                canAddDay = false
                habitCardBinding.habitDayButton.isEnabled = false
                Toast.makeText(this, "Nuevo día añadido", Toast.LENGTH_SHORT).show()

                // Temporizador de 24h
                val millisIn24Hours = 24 * 60 * 60 * 1000L

                object : CountDownTimer(millisIn24Hours, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
                        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60
                        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60
                        habitCardBinding.habitDayButton.text = String.format(
                            "%02d:%02d:%02d",
                            hours, minutes, seconds
                        )
                    }

                    override fun onFinish() {
                        canAddDay = true
                        habitCardBinding.habitDayButton.isEnabled = true
                        habitCardBinding.habitDayButton.text = "Añadir un día"
                        Toast.makeText(this@MainActivity, "¡Ya puedes añadir otro día!", Toast.LENGTH_SHORT).show()
                    }
                }.start()
            } else {
                Toast.makeText(this, "Debes esperar 24 horas antes de añadir otro día ⏳", Toast.LENGTH_SHORT).show()
            }
        }

        // Finalmente agregamos la tarjeta al contenedor
        binding.habitContainer.addView(habitCardBinding.root)
    }
}