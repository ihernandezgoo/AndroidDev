package com.example.hoopscoreb.ui

import android.view.View
import com.example.hoopscoreb.databinding.ActivityMainBinding

class PossessionView(
    private val binding: ActivityMainBinding,
    private val timerView: TimerView
) {
    private var currentPossession: Boolean? = null
    private var lastClickTime = 0L

    fun setupDoubleClick() {
        binding.tvPossessionTime.setOnClickListener {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < 400) {
                timerView.setPossessionTime(14)
            }
            lastClickTime = clickTime
        }
    }

    fun updateArrowByTilt(tiltX: Float) {
        val threshold = 2.0f

        when {
            tiltX < -threshold && currentPossession != true -> {
                currentPossession = true
                binding.ivPossessionArrow.rotation = 90f
                binding.ivPossessionArrow.visibility = View.VISIBLE
                timerView.resetPossessionTime()
            }
            tiltX > threshold && currentPossession != false -> {
                currentPossession = false
                binding.ivPossessionArrow.rotation = 270f
                binding.ivPossessionArrow.visibility = View.VISIBLE
                timerView.resetPossessionTime()
            }
        }
    }

    fun resetPossessionTime() {
        timerView.resetPossessionTime()
    }
}