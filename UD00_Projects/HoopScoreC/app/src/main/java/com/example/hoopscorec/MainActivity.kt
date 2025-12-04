package com.example.hoopscorec

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hoopscorec.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var puntosEquipo1 = 0
    private var puntosEquipo2 = 0
    private var nombreEquipo1 = "Equipo 1"
    private var nombreEquipo2 = "Equipo 2"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos de los equipos
        nombreEquipo1 = intent.getStringExtra("EQUIPO1_NOMBRE") ?: "Equipo 1"
        nombreEquipo2 = intent.getStringExtra("EQUIPO2_NOMBRE") ?: "Equipo 2"

        binding.tvNombreEquipo1.text = nombreEquipo1
        binding.tvNombreEquipo2.text = nombreEquipo2

        // Botones Equipo 1
        binding.btnMas1Equipo1.setOnClickListener {
            puntosEquipo1 += 1
            actualizarMarcador()
        }

        binding.btnMas2Equipo1.setOnClickListener {
            puntosEquipo1 += 2
            actualizarMarcador()
        }

        binding.btnMas3Equipo1.setOnClickListener {
            puntosEquipo1 += 3
            actualizarMarcador()
        }

        // Botones Equipo 2
        binding.btnMas1Equipo2.setOnClickListener {
            puntosEquipo2 += 1
            actualizarMarcador()
        }

        binding.btnMas2Equipo2.setOnClickListener {
            puntosEquipo2 += 2
            actualizarMarcador()
        }

        binding.btnMas3Equipo2.setOnClickListener {
            puntosEquipo2 += 3
            actualizarMarcador()
        }

        // Botón Reset
        binding.btnReset.setOnClickListener {
            puntosEquipo1 = 0
            puntosEquipo2 = 0
            actualizarMarcador()
        }

        actualizarMarcador()
    }

    private fun actualizarMarcador() {
        binding.tvPuntosEquipo1.text = puntosEquipo1.toString()
        binding.tvPuntosEquipo2.text = puntosEquipo2.toString()
    }
}