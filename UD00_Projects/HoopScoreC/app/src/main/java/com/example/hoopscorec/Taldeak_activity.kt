package com.example.hoopscorec

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.hoopscorec.databinding.ActivityTaldeakBinding

class Taldeak_activity : AppCompatActivity() {
    private lateinit var binding: ActivityTaldeakBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTaldeakBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}