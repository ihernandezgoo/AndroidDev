package com.example.hoopscorec

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.hoopscorec.databinding.ActivitySetupBinding

class SetupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetupBinding
    private val jugadoresEquipo1 = mutableListOf<EditText>()
    private val jugadoresEquipo2 = mutableListOf<EditText>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar con 5 jugadores por equipo (mínimo)
        repeat(5) {
            addJugadorEquipo1()
            addJugadorEquipo2()
        }

        binding.btnAddJugador1.setOnClickListener {
            if (jugadoresEquipo1.size < 12) {
                addJugadorEquipo1()
            } else {
                Toast.makeText(this, "Máximo 12 jugadores por equipo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnAddJugador2.setOnClickListener {
            if (jugadoresEquipo2.size < 12) {
                addJugadorEquipo2()
            } else {
                Toast.makeText(this, "Máximo 12 jugadores por equipo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnIniciarPartido.setOnClickListener {
            validarYIniciarPartido()
        }
    }

    private fun addJugadorEquipo1() {
        val editText = EditText(this).apply {
            hint = "Jugador ${jugadoresEquipo1.size + 1}"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        }
        jugadoresEquipo1.add(editText)
        binding.containerJugadores1.addView(editText)
    }

    private fun addJugadorEquipo2() {
        val editText = EditText(this).apply {
            hint = "Jugador ${jugadoresEquipo2.size + 1}"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        }
        jugadoresEquipo2.add(editText)
        binding.containerJugadores2.addView(editText)
    }

    private fun validarYIniciarPartido() {
        val nombreEquipo1 = binding.etNombreEquipo1.text.toString().trim()
        val nombreEquipo2 = binding.etNombreEquipo2.text.toString().trim()

        if (nombreEquipo1.isEmpty() || nombreEquipo2.isEmpty()) {
            Toast.makeText(this, "Debes ingresar los nombres de ambos equipos", Toast.LENGTH_SHORT).show()
            return
        }

        val jugadores1 = jugadoresEquipo1.mapNotNull {
            val nombre = it.text.toString().trim()
            if (nombre.isNotEmpty()) nombre else null
        }

        val jugadores2 = jugadoresEquipo2.mapNotNull {
            val nombre = it.text.toString().trim()
            if (nombre.isNotEmpty()) nombre else null
        }

        if (jugadores1.size < 5 || jugadores2.size < 5) {
            Toast.makeText(this, "Cada equipo debe tener al menos 5 jugadores", Toast.LENGTH_SHORT).show()
            return
        }

        // Iniciar MainActivity con los datos
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("EQUIPO1_NOMBRE", nombreEquipo1)
            putExtra("EQUIPO2_NOMBRE", nombreEquipo2)
            putStringArrayListExtra("EQUIPO1_JUGADORES", ArrayList(jugadores1))
            putStringArrayListExtra("EQUIPO2_JUGADORES", ArrayList(jugadores2))
        }
        startActivity(intent)
        finish()
    }
}

