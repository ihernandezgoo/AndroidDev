package com.example.stepup

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
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

        habitCardBinding.habitTitle.setText(title)
        habitCardBinding.habitDescription.setText(description)
        habitCardBinding.habitDayCount.text = "Racha de 0 días"

        var days = 0

        habitCardBinding.saveHabitButton.setOnClickListener {
            habitCardBinding.habitTitle.isEnabled = false
            habitCardBinding.habitDescription.isEnabled = false
            habitCardBinding.saveHabitButton.visibility = android.view.View.GONE
            Toast.makeText(this, "Hábito guardado", Toast.LENGTH_SHORT).show()
        }

        habitCardBinding.habitDayButton.setOnClickListener {
            days++
            habitCardBinding.habitDayCount.text = "Racha de $days días"
        }

        binding.habitContainer.addView(habitCardBinding.root)


    }
}