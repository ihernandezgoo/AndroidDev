package com.example.hoopscoreb.ui

import androidx.appcompat.app.AlertDialog
import com.example.hoopscoreb.databinding.ActivityMainBinding

class QuarterView(
    private val binding: ActivityMainBinding,
    private val timerView: TimerView,
    private val scoreView: ScoreView
) {

    fun setupQuarterClick() {
        binding.tvQuarter.setOnClickListener {
            val currentQuarter = binding.tvQuarter.text.toString().replace("Q", "").toInt()
            val nextQuarter = currentQuarter + 1

            if (nextQuarter <= 4) {
                binding.tvQuarter.text = "Q$nextQuarter"
                timerView.gameRunning = false
                timerView.resetPossessionTime()
            }

            if (nextQuarter > 4) {
                showFinalScore()
            }
        }
    }

    private fun showFinalScore() {
        val builder = AlertDialog.Builder(binding.root.context)
        builder.setTitle("Resultado Final")
        builder.setMessage("Equipo A: ${scoreView.getScoreTeamA()}\nEquipo B: ${scoreView.getScoreTeamB()}")
        builder.setPositiveButton("Cerrar") { dialog, _ ->
            dialog.dismiss()
            resetMatch()
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun resetMatch() {
        scoreView.resetScores()
        binding.tvQuarter.text = "Q1"
        timerView.gameRunning = false
        timerView.resetPossessionTime()
    }
}