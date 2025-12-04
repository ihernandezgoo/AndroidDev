package com.example.hoopscoreb.addTeams

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hoopscoreb.databinding.ActivityAddTeamsBinding
import com.example.hoopscoreb.ui.ScoreView

class AddTeamsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTeamsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTeamsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mostrar Team1 por defecto
        binding.layoutTeam1.visibility = android.view.View.VISIBLE
        binding.layoutTeam2.visibility = android.view.View.GONE

        // Cambiar entre equipos
        binding.btnTeam1.setOnClickListener {
            binding.layoutTeam1.visibility = android.view.View.VISIBLE
            binding.layoutTeam2.visibility = android.view.View.GONE
        }

        binding.btnTeam2.setOnClickListener {
            binding.layoutTeam1.visibility = android.view.View.GONE
            binding.layoutTeam2.visibility = android.view.View.VISIBLE
        }

        // Guardar nombres y pasar al marcador
        binding.btnSaveTeams.setOnClickListener {
            val nameTeam1 = binding.etTeam1Name.text.toString().ifBlank { "Equipo 1" }
            val nameTeam2 = binding.etTeam2Name.text.toString().ifBlank { "Equipo 2" }

            val intent = Intent(this, ScoreView::class.java).apply {
                putExtra("TEAM_1_NAME", nameTeam1)
                putExtra("TEAM_2_NAME", nameTeam2)
            }
            startActivity(intent)
            finish()
        }
    }
}
