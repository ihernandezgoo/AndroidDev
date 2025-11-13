package com.example.hoopscoreb.ui

import com.example.hoopscoreb.databinding.ActivityMainBinding


class ScoreView(
    private val binding: ActivityMainBinding,
    private val timerView: TimerView
) {

    private var scoreTeamA = 0
    private var scoreTeamB = 0

    fun setupScoreButtons() {
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
    }

    private fun addPoints(points: Int, isTeamA: Boolean) {
        if (isTeamA) scoreTeamA += points else scoreTeamB += points
        updateScores()
        timerView.resetPossessionTime()
    }

    fun updateScores() {
        binding.tvScoreA.text = scoreTeamA.toString()
        binding.tvScoreB.text = scoreTeamB.toString()
    }

    fun resetScores() {
        scoreTeamA = 0
        scoreTeamB = 0
        updateScores()
    }

    fun getScoreTeamA() = scoreTeamA
    fun getScoreTeamB() = scoreTeamB
}