package com.example.hoopscoreb

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hoopscoreb.addTeams.AddTeamsActivity
import com.example.hoopscoreb.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.openScoreboard.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.addTeams.setOnClickListener {
            val intent = Intent(this, AddTeamsActivity::class.java)
            startActivity(intent)
        }
    }
}