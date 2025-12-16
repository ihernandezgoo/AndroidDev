package com.example.hoopscorec

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.hoopscorec.databinding.ActivityScoreBinding
import com.example.hoopscorec.models.Player
import com.example.hoopscorec.models.Team
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.properties.TextAlignment
import com.itextpdf.layout.properties.UnitValue
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class ScoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScoreBinding
    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "basketball_game_prefs"
        private const val KEY_TEAM_A = "team_a"
        private const val KEY_TEAM_B = "team_b"
        private const val KEY_TIME_LEFT = "time_left"
        private const val KEY_QUARTER = "quarter"
        private const val KEY_FOULS_A = "fouls_a"
        private const val KEY_FOULS_B = "fouls_b"
        private const val KEY_TIMEOUTS_A = "timeouts_a"
        private const val KEY_TIMEOUTS_B = "timeouts_b"
        private const val KEY_SELECTED_INDEX_A = "selected_index_a"
        private const val KEY_SELECTED_INDEX_B = "selected_index_b"
        private const val KEY_ACTION_HISTORY = "action_history"
        private const val KEY_GAME_ACTIVE = "game_active"
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    // Equipos
    private var teamA: Team? = null
    private var teamB: Team? = null

    // Jugadores seleccionados
    private var selectedIndexA: Int = 0
    private var selectedIndexB: Int = 0

    // Temporizador
    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 600000L // 10 minutos por defecto
    private var isTimerRunning = false

    // Período actual
    private var currentQuarter = 1

    // Faltas y timeouts
    private var foulsTeamA = 0
    private var foulsTeamB = 0
    private var timeoutsTeamA = 5
    private var timeoutsTeamB = 5

    // Historial para deshacer
    private data class Action(
        val type: String, // "POINT", "FOUL", "TIMEOUT"
        val isTeamA: Boolean,
        val value: Int,
        val playerIndex: Int = -1
    )
    private val actionHistory = mutableListOf<Action>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            binding = ActivityScoreBinding.inflate(layoutInflater)
            setContentView(binding.root)

            // Inicializar SharedPreferences
            prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

            // Verificar si hay un juego guardado
            val hasGameSaved = prefs.getBoolean(KEY_GAME_ACTIVE, false)

            if (hasGameSaved) {
                // Cargar juego guardado
                loadGameState()
                Toast.makeText(this, "Partido cargado", Toast.LENGTH_SHORT).show()
            } else {
                // Obtener equipos desde el Intent (nuevo juego)
                teamA = intent.getSerializableExtra("EXTRA_TEAM_A") as? Team
                teamB = intent.getSerializableExtra("EXTRA_TEAM_B") as? Team

                if (teamA == null || teamB == null) {
                    val playersA = List(5) { i -> Player("A${i + 1}") }
                    val playersB = List(5) { i -> Player("B${i + 1}") }
                    teamA = Team("ORANGE", playersA)
                    teamB = Team("BLUE", playersB)
                    Toast.makeText(this, "No se recibieron equipos; usando ejemplo", Toast.LENGTH_SHORT).show()
                }

                // Marcar el juego como activo
                prefs.edit().putBoolean(KEY_GAME_ACTIVE, true).apply()
            }

            if (teamA!!.players.isEmpty() || teamB!!.players.isEmpty()) {
                Toast.makeText(this, "Cada equipo debe tener al menos 1 jugador", Toast.LENGTH_LONG).show()
                finish()
                return
            }


            setupUI()
            renderPlayerLists()
            updateUI()

            // Configurar el callback para el botón atrás
            onBackPressedDispatcher.addCallback(this) {
                AlertDialog.Builder(this@ScoreActivity)
                    .setTitle("Salir del partido")
                    .setMessage("El partido se guardará automáticamente. ¿Deseas salir?")
                    .setPositiveButton("Salir") { _, _ ->
                        saveGameState()
                        finish()
                    }
                    .setNegativeButton("Seguir jugando", null)
                    .show()
            }

        } catch (t: Throwable) {
            Log.e("ScoreActivity", "Error inicializando ScoreActivity", t)
            Toast.makeText(this, "Error al abrir marcador: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun setupUI() {
        // Nombres de equipos
        binding.tvNombreEquipo1.text = teamA!!.name
        binding.tvNombreEquipo2.text = teamB!!.name

        // Botones de puntos
        binding.btnMas1Equipo1.setOnClickListener { showPlayerPickerAndAddPoints(true, 1) }
        binding.btnMas2Equipo1.setOnClickListener { showPlayerPickerAndAddPoints(true, 2) }
        binding.btnMas3Equipo1.setOnClickListener { showPlayerPickerAndAddPoints(true, 3) }

        binding.btnMas1Equipo2.setOnClickListener { showPlayerPickerAndAddPoints(false, 1) }
        binding.btnMas2Equipo2.setOnClickListener { showPlayerPickerAndAddPoints(false, 2) }
        binding.btnMas3Equipo2.setOnClickListener { showPlayerPickerAndAddPoints(false, 3) }

        // Botones de faltas
        binding.btnFaltaEquipo1.setOnClickListener { addFoul(true) }
        binding.btnFaltaEquipo2.setOnClickListener { addFoul(false) }

        // Control del temporizador
        binding.btnStartStop.setOnClickListener { toggleTimer() }
        binding.btnReset.setOnClickListener { resetTimer() }

        // Botones globales
        binding.btnUndo.setOnClickListener { undoLastAction() }
        binding.btnNextQuarter.setOnClickListener { nextQuarter() }
        binding.btnStats.setOnClickListener { showStatistics() }
        binding.btnEndGame.setOnClickListener { confirmEndGame() }
    }

    // Nueva función: muestra lista de jugadores y suma puntos al elegido
    private fun showPlayerPickerAndAddPoints(isTeamA: Boolean, points: Int) {
        val team = if (isTeamA) teamA else teamB
        if (team == null || team.players.isEmpty()) return
        val names = team.players.map { it.name }.toTypedArray()
        AlertDialog.Builder(this)
            .setTitle("Seleccionar jugador que anotó")
            .setItems(names) { dialog, which ->
                // Añadir puntos al jugador seleccionado
                addPointsToPlayerAtIndex(team, which, points)
                // Guardar acción en historial
                actionHistory.add(Action("POINT", isTeamA, points, which))
                // Actualizar UI
                if (isTeamA) selectedIndexA = which else selectedIndexB = which
                updateSelectionVisuals(isTeamA)
                refreshPlayerRowPoints(isTeamA)
                updateUI()
                saveGameState() // Guardar estado
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun addPointsToPlayerAtIndex(team: Team, index: Int, points: Int) {
        if (index < 0 || index >= team.players.size) return
        team.players[index].points += points
    }

    private fun teamTotalPoints(team: Team?): Int {
        return team?.players?.sumOf { it.points } ?: 0
    }

    private fun addFoul(isTeamA: Boolean) {
        val team = if (isTeamA) teamA else teamB
        if (team == null || team.players.isEmpty()) return

        val names = team.players.mapIndexed { index, player ->
            "${player.name} (${player.fouls} faltas)"
        }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Seleccionar jugador que cometió falta")
            .setItems(names) { dialog, which ->
                // Añadir falta al jugador
                team.players[which].fouls++
                // Incrementar contador de faltas del equipo
                if (isTeamA) {
                    foulsTeamA++
                    actionHistory.add(Action("FOUL", true, 1, which))
                } else {
                    foulsTeamB++
                    actionHistory.add(Action("FOUL", false, 1, which))
                }
                // Actualizar UI
                refreshPlayerRowPoints(isTeamA)
                updateUI()
                saveGameState()
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun toggleTimer() {
        if (isTimerRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimerDisplay()
                // Guardar estado cada 5 segundos
                if ((timeLeftInMillis / 1000) % 5 == 0L) {
                    saveGameState()
                }
            }

            override fun onFinish() {
                timeLeftInMillis = 0
                isTimerRunning = false
                binding.btnStartStop.text = "▶"
                updateTimerDisplay()
                saveGameState() // Guardar estado
                Toast.makeText(this@ScoreActivity, "¡Tiempo finalizado!", Toast.LENGTH_SHORT).show()
            }
        }.start()
        isTimerRunning = true
        binding.btnStartStop.text = "⏸"
    }

    private fun pauseTimer() {
        timer?.cancel()
        isTimerRunning = false
        binding.btnStartStop.text = "▶"
        saveGameState() // Guardar estado al pausar
    }

    private fun resetTimer() {
        timer?.cancel()
        isTimerRunning = false
        timeLeftInMillis = 600000L // 10 minutos
        binding.btnStartStop.text = "▶"
        updateTimerDisplay()
        saveGameState() // Guardar estado
    }

    private fun updateTimerDisplay() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        binding.tvClock.text = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
    }

    private fun nextQuarter() {
        if (currentQuarter >= 4) {
            AlertDialog.Builder(this)
                .setTitle("Fin del partido")
                .setMessage("El partido ha terminado. ¿Deseas iniciar tiempo extra?")
                .setPositiveButton("Sí") { _, _ ->
                    currentQuarter = 5 // OT
                    resetTimer()
                    updateUI()
                    saveGameState() // Guardar estado
                }
                .setNegativeButton("No") { _, _ ->
                    // Mostrar opción para finalizar el partido
                    showEndGameDialog()
                }
                .show()
        } else {
            currentQuarter++
            resetTimer()
            updateUI()
            saveGameState() // Guardar estado
        }
    }

    private fun undoLastAction() {
        if (actionHistory.isEmpty()) {
            Toast.makeText(this, "No hay acciones para deshacer", Toast.LENGTH_SHORT).show()
            return
        }

        val lastAction = actionHistory.removeAt(actionHistory.lastIndex)
        when (lastAction.type) {
            "POINT" -> {
                val team = if (lastAction.isTeamA) teamA else teamB
                if (team != null && lastAction.playerIndex >= 0 && lastAction.playerIndex < team.players.size) {
                    team.players[lastAction.playerIndex].points -= lastAction.value
                    refreshPlayerRowPoints(lastAction.isTeamA)
                }
            }
            "FOUL" -> {
                if (lastAction.isTeamA) {
                    foulsTeamA = maxOf(0, foulsTeamA - 1)
                } else {
                    foulsTeamB = maxOf(0, foulsTeamB - 1)
                }
            }
            "TIMEOUT" -> {
                if (lastAction.isTeamA) {
                    timeoutsTeamA = minOf(5, timeoutsTeamA + 1)
                } else {
                    timeoutsTeamB = minOf(5, timeoutsTeamB + 1)
                }
            }
        }
        updateUI()
        saveGameState() // Guardar estado
        Toast.makeText(this, "Acción deshecha", Toast.LENGTH_SHORT).show()
    }

    private fun updateUI() {
        // Actualizar puntuaciones
        binding.tvPuntosEquipo1.text = teamTotalPoints(teamA).toString()
        binding.tvPuntosEquipo2.text = teamTotalPoints(teamB).toString()

        // Actualizar período
        val quarterText = when (currentQuarter) {
            1 -> "1º"
            2 -> "2º"
            3 -> "3º"
            4 -> "4º"
            else -> "OT"
        }
        binding.tvQuarter.text = quarterText

        // Actualizar faltas y timeouts
        binding.tvFaltasEquipo1.text = foulsTeamA.toString()
        binding.tvFaltasEquipo2.text = foulsTeamB.toString()
        binding.tvTimeoutsEquipo1.text = timeoutsTeamA.toString()
        binding.tvTimeoutsEquipo2.text = timeoutsTeamB.toString()

        // Actualizar temporizador
        updateTimerDisplay()
    }

    private fun renderPlayerLists() {
        binding.llPlayersTeam1.removeAllViews()
        binding.llPlayersTeam2.removeAllViews()

        // Renderizar todos los jugadores de cada equipo
        teamA?.players?.forEachIndexed { index, player ->
            binding.llPlayersTeam1.addView(createPlayerRow(index, true))
        }

        teamB?.players?.forEachIndexed { index, player ->
            binding.llPlayersTeam2.addView(createPlayerRow(index, false))
        }

        updateSelectionVisuals(true)
        updateSelectionVisuals(false)
    }

    private fun createPlayerRow(index: Int, isTeamA: Boolean): View {
        val context = this
        val row = LinearLayout(context)
        row.orientation = LinearLayout.HORIZONTAL
        val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        lp.setMargins(0, 4, 0, 4)
        row.layoutParams = lp
        row.setPadding(12, 12, 12, 12)
        row.setBackgroundColor(Color.parseColor("#3d3d3d"))
        row.tag = if (isTeamA) "A-$index" else "B-$index"

        // Nombre del jugador (50%)
        val tvName = TextView(context)
        tvName.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.50f)
        tvName.gravity = Gravity.CENTER_VERTICAL
        tvName.textSize = 14f
        tvName.setTextColor(Color.WHITE)

        // Puntos del jugador (25%)
        val tvPoints = TextView(context)
        tvPoints.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f)
        tvPoints.gravity = Gravity.CENTER_VERTICAL or Gravity.END
        tvPoints.textSize = 14f
        tvPoints.setTextColor(Color.parseColor("#4CAF50"))

        // Faltas del jugador (25%)
        val tvFouls = TextView(context)
        tvFouls.layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 0.25f)
        tvFouls.gravity = Gravity.CENTER_VERTICAL or Gravity.END
        tvFouls.textSize = 14f
        tvFouls.setTextColor(Color.parseColor("#FFC107"))

        val team = if (isTeamA) teamA else teamB
        if (team != null && index < team.players.size) {
            val player = team.players[index]
            tvName.text = player.name
            tvPoints.text = "${player.points} pts"
            tvFouls.text = "${player.fouls} F"

            // Resaltar si tiene 5 o más faltas (eliminado)
            if (player.fouls >= 5) {
                tvFouls.setTextColor(Color.parseColor("#F44336"))
                tvName.setTextColor(Color.parseColor("#F44336"))
            }
        } else {
            tvName.text = "-"
            tvPoints.text = "0 pts"
            tvFouls.text = "0 F"
            row.alpha = 0.5f
        }

        row.addView(tvName)
        row.addView(tvPoints)
        row.addView(tvFouls)

        // Click para seleccionar
        row.setOnClickListener {
            val teamExists = if (isTeamA) teamA != null && index < teamA!!.players.size
                             else teamB != null && index < teamB!!.players.size
            if (!teamExists) return@setOnClickListener
            if (isTeamA) {
                selectedIndexA = index
                updateSelectionVisuals(true)
            } else {
                selectedIndexB = index
                updateSelectionVisuals(false)
            }
        }

        return row
    }

    private fun updateSelectionVisuals(isTeamA: Boolean) {
        val container = if (isTeamA) binding.llPlayersTeam1 else binding.llPlayersTeam2
        val selectedIndex = if (isTeamA) selectedIndexA else selectedIndexB
        val highlightColor = if (isTeamA) Color.parseColor("#FF6B00") else Color.parseColor("#1976D2")

        for (i in 0 until container.childCount) {
            val row = container.getChildAt(i)
            val team = if (isTeamA) teamA else teamB
            val exists = (team?.players?.size ?: 0) > i

            if (!exists) {
                row.setBackgroundColor(Color.parseColor("#3d3d3d"))
                row.alpha = 0.5f
                continue
            }

            if (i == selectedIndex) {
                row.setBackgroundColor(highlightColor)
                row.alpha = 1.0f
            } else {
                row.setBackgroundColor(Color.parseColor("#3d3d3d"))
                row.alpha = 1.0f
            }
        }
    }

    private fun refreshPlayerRowPoints(isTeamA: Boolean) {
        val container = if (isTeamA) binding.llPlayersTeam1 else binding.llPlayersTeam2
        val team = if (isTeamA) teamA else teamB

        for (i in 0 until container.childCount) {
            val row = container.getChildAt(i) as? LinearLayout ?: continue
            val tvName = row.getChildAt(0) as? TextView
            val tvPoints = row.getChildAt(1) as? TextView
            val tvFouls = row.getChildAt(2) as? TextView

            if (i < (team?.players?.size ?: 0)) {
                val player = team?.players?.get(i)
                tvPoints?.text = "${player?.points ?: 0} pts"
                tvFouls?.text = "${player?.fouls ?: 0} F"

                // Resaltar si tiene 5 o más faltas
                if ((player?.fouls ?: 0) >= 5) {
                    tvFouls?.setTextColor(Color.parseColor("#F44336"))
                    tvName?.setTextColor(Color.parseColor("#F44336"))
                } else {
                    tvFouls?.setTextColor(Color.parseColor("#FFC107"))
                    tvName?.setTextColor(Color.WHITE)
                }
            } else {
                tvPoints?.text = "0 pts"
                tvFouls?.text = "0 F"
            }
        }
    }

    private fun saveGameState() {
        val editor = prefs.edit()

        // Guardar equipos (convertir a JSON)
        editor.putString(KEY_TEAM_A, gson.toJson(teamA))
        editor.putString(KEY_TEAM_B, gson.toJson(teamB))

        // Guardar tiempo y estado del juego
        editor.putLong(KEY_TIME_LEFT, timeLeftInMillis)
        editor.putInt(KEY_QUARTER, currentQuarter)

        // Guardar faltas y timeouts
        editor.putInt(KEY_FOULS_A, foulsTeamA)
        editor.putInt(KEY_FOULS_B, foulsTeamB)
        editor.putInt(KEY_TIMEOUTS_A, timeoutsTeamA)
        editor.putInt(KEY_TIMEOUTS_B, timeoutsTeamB)

        // Guardar selecciones
        editor.putInt(KEY_SELECTED_INDEX_A, selectedIndexA)
        editor.putInt(KEY_SELECTED_INDEX_B, selectedIndexB)

        // Guardar historial de acciones
        editor.putString(KEY_ACTION_HISTORY, gson.toJson(actionHistory))

        // Guardar que el juego está activo
        editor.putBoolean(KEY_GAME_ACTIVE, true)

        editor.apply()
    }

    private fun loadGameState() {
        // Cargar equipos
        val teamAJson = prefs.getString(KEY_TEAM_A, null)
        val teamBJson = prefs.getString(KEY_TEAM_B, null)

        if (teamAJson != null) {
            teamA = gson.fromJson(teamAJson, Team::class.java)
        }
        if (teamBJson != null) {
            teamB = gson.fromJson(teamBJson, Team::class.java)
        }

        // Cargar tiempo y estado del juego
        timeLeftInMillis = prefs.getLong(KEY_TIME_LEFT, 600000L)
        currentQuarter = prefs.getInt(KEY_QUARTER, 1)

        // Cargar faltas y timeouts
        foulsTeamA = prefs.getInt(KEY_FOULS_A, 0)
        foulsTeamB = prefs.getInt(KEY_FOULS_B, 0)
        timeoutsTeamA = prefs.getInt(KEY_TIMEOUTS_A, 5)
        timeoutsTeamB = prefs.getInt(KEY_TIMEOUTS_B, 5)

        // Cargar selecciones
        selectedIndexA = prefs.getInt(KEY_SELECTED_INDEX_A, 0)
        selectedIndexB = prefs.getInt(KEY_SELECTED_INDEX_B, 0)

        // Cargar historial de acciones
        val historyJson = prefs.getString(KEY_ACTION_HISTORY, null)
        if (historyJson != null) {
            val type = object : TypeToken<MutableList<Action>>() {}.type
            val loadedHistory: MutableList<Action>? = gson.fromJson(historyJson, type)
            if (loadedHistory != null) {
                actionHistory.clear()
                actionHistory.addAll(loadedHistory)
            }
        }
    }

    private fun showEndGameDialog() {
        val teamAScore = teamTotalPoints(teamA)
        val teamBScore = teamTotalPoints(teamB)
        val winner = when {
            teamAScore > teamBScore -> teamA?.name ?: "Equipo 1"
            teamBScore > teamAScore -> teamB?.name ?: "Equipo 2"
            else -> "Empate"
        }

        val message = if (winner == "Empate") {
            "El partido terminó en empate: $teamAScore - $teamBScore"
        } else {
            "¡$winner ganó el partido!\n${teamA?.name}: $teamAScore - ${teamB?.name}: $teamBScore"
        }

        AlertDialog.Builder(this)
            .setTitle("Fin del Partido")
            .setMessage(message)
            .setPositiveButton("Nuevo Partido") { _, _ ->
                clearGameState()
                finish()
            }
            .setNegativeButton("Ver Estadísticas") { _, _ ->
                // Mantener en pantalla para ver estadísticas
            }
            .setCancelable(false)
            .show()
    }

    private fun clearGameState() {
        prefs.edit().clear().apply()
    }

    private fun showStatistics() {
        val stats = generateStatisticsText()

        AlertDialog.Builder(this)
            .setTitle("📊 Estadísticas del Partido")
            .setMessage(stats)
            .setPositiveButton("Exportar PDF") { _, _ ->
                checkPermissionsAndExportPDF()
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }

    private fun generateStatisticsText(): String {
        val teamAScore = teamTotalPoints(teamA)
        val teamBScore = teamTotalPoints(teamB)
        val quarterText = when (currentQuarter) {
            1 -> "1º Cuarto"
            2 -> "2º Cuarto"
            3 -> "3º Cuarto"
            4 -> "4º Cuarto"
            else -> "Tiempo Extra"
        }
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60
        val timeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

        val sb = StringBuilder()
        sb.append("═══════════════════════════\n")
        sb.append("MARCADOR\n")
        sb.append("═══════════════════════════\n\n")
        sb.append("${teamA?.name ?: "Equipo 1"}: $teamAScore\n")
        sb.append("${teamB?.name ?: "Equipo 2"}: $teamBScore\n\n")
        sb.append("Período: $quarterText\n")
        sb.append("Tiempo: $timeText\n\n")

        sb.append("═══════════════════════════\n")
        sb.append("${teamA?.name ?: "Equipo 1"}\n")
        sb.append("═══════════════════════════\n")
        sb.append("Faltas del equipo: $foulsTeamA\n")
        sb.append("Timeouts restantes: $timeoutsTeamA\n\n")
        sb.append("JUGADORES:\n")
        teamA?.players?.sortedByDescending { it.points }?.forEach { player ->
            sb.append("• ${player.name}\n")
            sb.append("  ${player.points} pts | ${player.fouls} faltas\n")
        }

        sb.append("\n═══════════════════════════\n")
        sb.append("${teamB?.name ?: "Equipo 2"}\n")
        sb.append("═══════════════════════════\n")
        sb.append("Faltas del equipo: $foulsTeamB\n")
        sb.append("Timeouts restantes: $timeoutsTeamB\n\n")
        sb.append("JUGADORES:\n")
        teamB?.players?.sortedByDescending { it.points }?.forEach { player ->
            sb.append("• ${player.name}\n")
            sb.append("  ${player.points} pts | ${player.fouls} faltas\n")
        }

        return sb.toString()
    }

    private fun checkPermissionsAndExportPDF() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ no necesita permisos para acceder a Downloads
            exportToPDF()
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_CODE
                )
            } else {
                exportToPDF()
            }
        } else {
            exportToPDF()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportToPDF()
            } else {
                Toast.makeText(this, "Permiso denegado. No se puede exportar el PDF", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun exportToPDF() {
        try {
            val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            val fileName = "estadisticas_partido_${dateFormat.format(Date())}.pdf"

            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, fileName)

            val writer = PdfWriter(FileOutputStream(file))
            val pdfDoc = PdfDocument(writer)
            val document = Document(pdfDoc)

            // Título
            document.add(
                Paragraph("ESTADÍSTICAS DEL PARTIDO")
                    .setFontSize(20f)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
            )

            document.add(Paragraph("\n"))

            // Marcador
            val teamAScore = teamTotalPoints(teamA)
            val teamBScore = teamTotalPoints(teamB)

            document.add(
                Paragraph("${teamA?.name ?: "Equipo 1"}: $teamAScore")
                    .setFontSize(16f)
                    .setBold()
            )
            document.add(
                Paragraph("${teamB?.name ?: "Equipo 2"}: $teamBScore")
                    .setFontSize(16f)
                    .setBold()
            )

            document.add(Paragraph("\n"))

            // Información del juego
            val quarterText = when (currentQuarter) {
                1 -> "1º Cuarto"
                2 -> "2º Cuarto"
                3 -> "3º Cuarto"
                4 -> "4º Cuarto"
                else -> "Tiempo Extra"
            }
            val minutes = (timeLeftInMillis / 1000) / 60
            val seconds = (timeLeftInMillis / 1000) % 60
            val timeText = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)

            document.add(Paragraph("Período: $quarterText").setFontSize(12f))
            document.add(Paragraph("Tiempo restante: $timeText").setFontSize(12f))

            document.add(Paragraph("\n"))

            // Estadísticas del Equipo A
            document.add(
                Paragraph("${teamA?.name ?: "Equipo 1"}")
                    .setFontSize(14f)
                    .setBold()
            )
            document.add(Paragraph("Faltas del equipo: $foulsTeamA").setFontSize(10f))
            document.add(Paragraph("Timeouts restantes: $timeoutsTeamA").setFontSize(10f))

            document.add(Paragraph("\n"))

            // Tabla de jugadores Equipo A
            val tableA = Table(UnitValue.createPercentArray(floatArrayOf(3f, 1f, 1f)))
                .useAllAvailableWidth()
            tableA.addHeaderCell("Jugador")
            tableA.addHeaderCell("Puntos")
            tableA.addHeaderCell("Faltas")

            teamA?.players?.sortedByDescending { it.points }?.forEach { player ->
                tableA.addCell(player.name)
                tableA.addCell(player.points.toString())
                tableA.addCell(player.fouls.toString())
            }

            document.add(tableA)
            document.add(Paragraph("\n"))

            // Estadísticas del Equipo B
            document.add(
                Paragraph("${teamB?.name ?: "Equipo 2"}")
                    .setFontSize(14f)
                    .setBold()
            )
            document.add(Paragraph("Faltas del equipo: $foulsTeamB").setFontSize(10f))
            document.add(Paragraph("Timeouts restantes: $timeoutsTeamB").setFontSize(10f))

            document.add(Paragraph("\n"))

            // Tabla de jugadores Equipo B
            val tableB = Table(UnitValue.createPercentArray(floatArrayOf(3f, 1f, 1f)))
                .useAllAvailableWidth()
            tableB.addHeaderCell("Jugador")
            tableB.addHeaderCell("Puntos")
            tableB.addHeaderCell("Faltas")

            teamB?.players?.sortedByDescending { it.points }?.forEach { player ->
                tableB.addCell(player.name)
                tableB.addCell(player.points.toString())
                tableB.addCell(player.fouls.toString())
            }

            document.add(tableB)

            // Pie de página
            document.add(Paragraph("\n\n"))
            document.add(
                Paragraph("Generado el ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}")
                    .setFontSize(8f)
                    .setTextAlignment(TextAlignment.CENTER)
            )

            document.close()

            Toast.makeText(this, "PDF guardado en: ${file.absolutePath}", Toast.LENGTH_LONG).show()

            // Mostrar diálogo para abrir o compartir el PDF
            showPDFOptionsDialog(file)

        } catch (e: Exception) {
            Log.e("ScoreActivity", "Error exportando PDF", e)
            Toast.makeText(this, "Error al exportar PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showPDFOptionsDialog(file: File) {
        AlertDialog.Builder(this)
            .setTitle("PDF Generado")
            .setMessage("El archivo se guardó en: ${file.name}")
            .setPositiveButton("Abrir") { _, _ ->
                openPDF(file)
            }
            .setNeutralButton("Compartir") { _, _ ->
                sharePDF(file)
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }

    private fun openPDF(file: File) {
        try {
            val uri = androidx.core.content.FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/pdf")
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "No se encontró una aplicación para abrir PDFs", Toast.LENGTH_SHORT).show()
        }
    }

    private fun sharePDF(file: File) {
        try {
            val uri = androidx.core.content.FileProvider.getUriForFile(
                this,
                "${applicationContext.packageName}.provider",
                file
            )
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            startActivity(Intent.createChooser(intent, "Compartir estadísticas"))
        } catch (e: Exception) {
            Toast.makeText(this, "Error al compartir PDF", Toast.LENGTH_SHORT).show()
        }
    }

    private fun confirmEndGame() {
        AlertDialog.Builder(this)
            .setTitle("Finalizar Partido")
            .setMessage("¿Estás seguro de que quieres finalizar el partido?")
            .setPositiveButton("Sí") { _, _ ->
                showEndGameDialog()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }


    override fun onPause() {
        super.onPause()
        // Pausar el temporizador y guardar estado cuando la app pasa a segundo plano
        if (isTimerRunning) {
            pauseTimer()
        }
        saveGameState()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
