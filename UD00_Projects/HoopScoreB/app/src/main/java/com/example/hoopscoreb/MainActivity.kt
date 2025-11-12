package com.example.hoopscoreb

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.hoopscoreb.databinding.ActivityMainBinding
import kotlin.math.sqrt

class MainActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityMainBinding

    private var scoreTeamA = 0
    private var scoreTeamB = 0

    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var currentPossession: Boolean? = null

    // Cronómetro principal
    private var gameRunning = false
    private var gameTimeSeconds = 10 * 60 // 10 minutos
    private val gameHandler = Handler(Looper.getMainLooper())
    private val gameRunnable = object : Runnable {
        override fun run() {
            if (gameRunning && gameTimeSeconds > 0) {
                gameTimeSeconds--
                updateGameTime()
                gameHandler.postDelayed(this, 1000)
            } else if (gameTimeSeconds == 0) {
                gameRunning = false
                stopPossessionTimer()
            }
        }
    }

    // 24 segundos
    private var possessionTime = 24
    private var possessionRunning = false
    private val possessionHandler = Handler(Looper.getMainLooper())
    private val possessionRunnable = object : Runnable {
        override fun run() {
            if (possessionRunning && possessionTime > 0) {
                possessionTime--
                binding.tvPossessionTime.text = possessionTime.toString()
                possessionHandler.postDelayed(this, 1000)
            } else if (possessionTime == 0) {
                possessionRunning = false
                // Cuando se acabe el 24, también se detiene el cronómetro principal
                gameRunning = false
                gameHandler.removeCallbacks(gameRunnable)
            }
        }
    }

    private var lastClickTime = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupSensor()
        updateScores()
        updateGameTime()
        binding.tvPossessionTime.text = possessionTime.toString()
    }

    private fun setupListeners() {
        // Equipo A
        binding.btnAMinus.setOnClickListener { addPoints(-1, true) }
        binding.btnA1.setOnClickListener { addPoints(1, true) }
        binding.btnA2.setOnClickListener { addPoints(2, true) }
        binding.btnA3.setOnClickListener { addPoints(3, true) }

        // Equipo B
        binding.btnBMinus.setOnClickListener { addPoints(-1, false) }
        binding.btnB1.setOnClickListener { addPoints(1, false) }
        binding.btnB2.setOnClickListener { addPoints(2, false) }
        binding.btnB3.setOnClickListener { addPoints(3, false) }

        // Botones de la izquierda (controlan 24 segundos)
        binding.btnLeft1.setOnClickListener { setPossessionTime(14) }
        binding.btnLeft2.setOnClickListener { setPossessionTime(24) }

        // Botón derecho (controla cronómetro principal)
        binding.btnRight.setOnClickListener {
            gameRunning = !gameRunning
            if (gameRunning) {
                startGameTimer()
                startPossessionTimer()
                binding.btnRight.text = "Gelditu"
            } else {
                gameHandler.removeCallbacks(gameRunnable)
                stopPossessionTimer()
                binding.btnRight.text = "Jarraitu"
            }
        }

        // Doble click sobre 24 segundos -> 14
        binding.tvPossessionTime.setOnClickListener {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < 400) {
                setPossessionTime(14)
            }
            lastClickTime = clickTime
        }
    }

    private fun setPossessionTime(time: Int) {
        possessionTime = time
        binding.tvPossessionTime.text = possessionTime.toString()
        if (gameRunning) startPossessionTimer()
    }

    private fun addPoints(points: Int, isTeamA: Boolean) {
        if (isTeamA) scoreTeamA += points else scoreTeamB += points
        updateScores()
        resetPossessionTime()
    }

    private fun updateScores() {
        binding.tvScoreA.text = scoreTeamA.toString()
        binding.tvScoreB.text = scoreTeamB.toString()
    }

    private fun setupQuarterClick() {
        binding.tvQuarter.setOnClickListener {
            val currentQuarter = binding.tvQuarter.text.toString().replace("Q", "").toInt()
            val nextQuarter = currentQuarter + 1

            if (nextQuarter <= 4) {
                binding.tvQuarter.text = "Q$nextQuarter"
                gameTimeSeconds = 10 * 60
                updateGameTime()
                resetPossessionTime()
            }

            if (nextQuarter > 4) {
                showFinalScore()
            }
        }
    }

    private fun showFinalScore() {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Resultado Final")
        builder.setMessage("Equipo A: $scoreTeamA\nEquipo B: $scoreTeamB")
        builder.setPositiveButton("Cerrar") { dialog, _ ->
            dialog.dismiss()
            resetMatch()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun resetMatch() {
        scoreTeamA = 0
        scoreTeamB = 0
        updateScores()

        binding.tvQuarter.text = "Q1"

        gameTimeSeconds = 10 * 60
        updateGameTime()
        gameRunning = false

        resetPossessionTime()
    }

    private fun startGameTimer() {
        gameHandler.removeCallbacks(gameRunnable)
        gameHandler.post(gameRunnable)
    }

    private fun startPossessionTimer() {
        possessionRunning = true
        possessionHandler.removeCallbacks(possessionRunnable)
        possessionHandler.post(possessionRunnable)
    }

    private fun stopPossessionTimer() {
        possessionRunning = false
        possessionHandler.removeCallbacks(possessionRunnable)
    }

    private fun updateGameTime() {
        val minutes = gameTimeSeconds / 60
        val seconds = gameTimeSeconds % 60
        binding.tvGameTime.text = String.format("%02d:%02d", minutes, seconds)
    }

    private fun setupSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }

    private fun updatePossession(tiltX: Float) {
        val threshold = 2.0f

        if (tiltX < -threshold && currentPossession != true) {
            currentPossession = true
            binding.ivPossessionArrow.rotation = 180f
            binding.ivPossessionArrow.visibility = android.view.View.VISIBLE
            resetPossessionTime()
        } else if (tiltX > threshold && currentPossession != false) {
            currentPossession = false
            binding.ivPossessionArrow.rotation = 0f
            binding.ivPossessionArrow.visibility = android.view.View.VISIBLE
            resetPossessionTime()
        }
    }

    private fun resetPossessionTime() {
        possessionTime = 24
        binding.tvPossessionTime.text = possessionTime.toString()
        if (gameRunning) startPossessionTimer()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]
                val magnitude = sqrt(x * x + y * y + z * z)
                if (magnitude > 15f) {
                    resetPossessionTime()
                } else {
                    updatePossession(x)
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onResume() {
        super.onResume()
        accelerometer?.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
        gameHandler.removeCallbacks(gameRunnable)
        possessionHandler.removeCallbacks(possessionRunnable)
    }
}