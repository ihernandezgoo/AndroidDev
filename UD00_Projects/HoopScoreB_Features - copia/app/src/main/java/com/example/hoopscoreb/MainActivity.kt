package com.example.hoopscoreb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hoopscoreb.databinding.ActivityMainBinding
import com.example.hoopscoreb.sensor.AccelerometerHandler
import com.example.hoopscoreb.ui.PossessionView
import com.example.hoopscoreb.ui.QuarterView
import com.example.hoopscoreb.ui.ScoreView
import com.example.hoopscoreb.ui.TimerView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var scoreView: ScoreView
    private lateinit var timerView: TimerView
    private lateinit var quarterView: QuarterView
    private lateinit var possessionView: PossessionView
    private lateinit var accelerometerHandler: AccelerometerHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Orden de inicialización corregido:
        // 1. timerView debe crearse primero.
        timerView = TimerView(binding)
        // 2. Ahora sí podemos usar timerView para crear scoreView.
        scoreView = ScoreView(binding, timerView)
        
        quarterView = QuarterView(binding, timerView, scoreView)
        possessionView = PossessionView(binding, timerView)
        accelerometerHandler = AccelerometerHandler(this, possessionView)

        setupListeners()
    }

    private fun setupListeners() {
        scoreView.setupScoreButtons()
        timerView.setupControlButtons()
        quarterView.setupQuarterClick()
        possessionView.setupDoubleClick()
    }

    override fun onResume() {
        super.onResume()
        accelerometerHandler.register()
        timerView.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        accelerometerHandler.unregister()
        timerView.pauseTimers()
    }
}