package com.example.hoopscoreb.ui

import android.os.Handler
import android.os.Looper
import com.example.hoopscoreb.databinding.ActivityMainBinding

class TimerView(private val binding: ActivityMainBinding) {

    var gameRunning = false
    private var gameTimeSeconds = 10 * 60
    private var possessionTime = 24
    private var possessionRunning = false
    private val gameHandler = Handler(Looper.getMainLooper())
    private val possessionHandler = Handler(Looper.getMainLooper())

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

    private val possessionRunnable = object : Runnable {
        override fun run() {
            if (possessionRunning && possessionTime > 0) {
                possessionTime--
                binding.tvPossessionTime.text = possessionTime.toString()
                possessionHandler.postDelayed(this, 1000)
            } else if (possessionTime == 0) {
                possessionRunning = false
                gameRunning = false
                gameHandler.removeCallbacks(gameRunnable)
            }
        }
    }

    fun setupControlButtons() {
        binding.btnLeft1.setOnClickListener { setPossessionTime(14) }
        binding.btnLeft2.setOnClickListener { setPossessionTime(24) }

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
    }

    fun setPossessionTime(time: Int) {
        possessionTime = time
        binding.tvPossessionTime.text = possessionTime.toString()
        if (gameRunning) startPossessionTimer()
    }

    fun resetPossessionTime() {
        setPossessionTime(24)
    }

    private fun startGameTimer() {
        gameHandler.removeCallbacks(gameRunnable)
        gameHandler.post(gameRunnable)
    }

    fun startPossessionTimer() {
        possessionRunning = true
        possessionHandler.removeCallbacks(possessionRunnable)
        possessionHandler.post(possessionRunnable)
    }

    fun stopPossessionTimer() {
        possessionRunning = false
        possessionHandler.removeCallbacks(possessionRunnable)
    }

    fun updateGameTime() {
        val minutes = gameTimeSeconds / 60
        val seconds = gameTimeSeconds % 60
        binding.tvGameTime.text = String.format("%02d:%02d", minutes, seconds)
    }

    fun resumeTimers() {
        if (gameRunning) startGameTimer()
        if (possessionRunning) startPossessionTimer()
    }

    fun pauseTimers() {
        gameHandler.removeCallbacks(gameRunnable)
        possessionHandler.removeCallbacks(possessionRunnable)
    }
}