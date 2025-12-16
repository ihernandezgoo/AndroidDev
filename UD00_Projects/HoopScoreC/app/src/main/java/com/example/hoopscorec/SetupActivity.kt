package com.example.hoopscorec

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.hoopscorec.databinding.ActivitySetupBinding
import com.example.hoopscorec.models.Player
import com.example.hoopscorec.models.Team
import java.io.ByteArrayOutputStream
import java.io.ObjectOutputStream

class SetupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySetupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Verificar si hay un juego guardado
        val prefs = getSharedPreferences("basketball_game_prefs", Context.MODE_PRIVATE)
        val hasGameSaved = prefs.getBoolean("game_active", false)

        if (hasGameSaved) {
            // Preguntar si quiere continuar el juego guardado
            AlertDialog.Builder(this)
                .setTitle("Partido en curso")
                .setMessage("Hay un partido guardado. ¿Deseas continuar o iniciar uno nuevo?")
                .setPositiveButton("Continuar") { _, _ ->
                    // Ir directamente a ScoreActivity
                    val intent = Intent(this, ScoreActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Nuevo Partido") { _, _ ->
                    // Limpiar el juego guardado
                    prefs.edit().clear().apply()
                    // Continuar con el setup normal
                    setupActivity()
                }
                .setCancelable(false)
                .show()
        } else {
            setupActivity()
        }
    }

    private fun setupActivity() {
        binding = ActivitySetupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicialmente agregar 5 campos por equipo
        repeat(5) {
            addPlayerField(binding.llPlayersA)
            addPlayerField(binding.llPlayersB)
        }

        binding.btnAddPlayerA.setOnClickListener {
            val count = binding.llPlayersA.childCount
            if (count >= 12) {
                Toast.makeText(this, "Máximo 12 jugadores", Toast.LENGTH_SHORT).show()
            } else {
                addPlayerField(binding.llPlayersA)
            }
        }

        binding.btnAddPlayerB.setOnClickListener {
            val count = binding.llPlayersB.childCount
            if (count >= 12) {
                Toast.makeText(this, "Máximo 12 jugadores", Toast.LENGTH_SHORT).show()
            } else {
                addPlayerField(binding.llPlayersB)
            }
        }

        binding.btnStartGame.setOnClickListener {
            val teamAName = binding.etTeamAName.text.toString().trim()
            val teamBName = binding.etTeamBName.text.toString().trim()

            if (teamAName.isEmpty() || teamBName.isEmpty()) {
                Toast.makeText(this, "Los nombres de los equipos no pueden estar vacíos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val playersA = collectPlayerNames(binding.llPlayersA)
            val playersB = collectPlayerNames(binding.llPlayersB)

            if (playersA.size < 5 || playersB.size < 5) {
                Toast.makeText(this, "Cada equipo debe tener al menos 5 jugadores", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Construir objetos Team
            val teamA = Team(teamAName, playersA.map { Player(it) })
            val teamB = Team(teamBName, playersB.map { Player(it) })

            // Log para depuración: mostrar nombres y tamaño de listas
            android.util.Log.d("SetupActivity", "Iniciando partido: teamA=${teamA.name} players=${teamA.players.size}, teamB=${teamB.name} players=${teamB.players.size}")

            // Intentar serializar objetos para detectar problemas de serialización antes de startActivity
            try {
                val baos = ByteArrayOutputStream()
                ObjectOutputStream(baos).use { oos ->
                    oos.writeObject(teamA)
                    oos.writeObject(teamB)
                }
            } catch (e: Exception) {
                android.util.Log.e("SetupActivity", "Error serializando equipos", e)
                Toast.makeText(this, "Error serializando equipos: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val intent = Intent(this, com.example.hoopscorec.ScoreActivity::class.java)
            intent.putExtra("EXTRA_TEAM_A", teamA as java.io.Serializable)
            intent.putExtra("EXTRA_TEAM_B", teamB as java.io.Serializable)
            startActivity(intent)
            finish() // Cerrar SetupActivity después de iniciar el juego
        }
    }

    private fun addPlayerField(container: android.widget.LinearLayout) {
        val et = EditText(this)
        et.hint = "Jugador"
        container.addView(et)
    }

    private fun collectPlayerNames(container: android.widget.LinearLayout): List<String> {
        val names = mutableListOf<String>()
        for (i in 0 until container.childCount) {
            val v = container.getChildAt(i)
            if (v is EditText) {
                val s = v.text.toString().trim()
                if (s.isNotEmpty()) names.add(s)
            }
        }
        return names
    }
}
