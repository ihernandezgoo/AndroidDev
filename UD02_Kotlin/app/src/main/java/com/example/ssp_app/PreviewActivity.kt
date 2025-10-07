package com.example.ssp_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ssp_app.databinding.ActivityPreviewBinding

class PreviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPreviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras

        if (extras != null) {
            val contactName = extras.getString("EXTRA_CONTACT_NAME")
            val contactNumber = extras.getString("EXTRA_CONTACT_NUMBER")
            val displayName = extras.getString("EXTRA_DISPLAY_NAME")
            val isJunior = extras.getBoolean("EXTRA_IS_JUNIOR")
            val jobPosition = extras.getString("EXTRA_JOB_POSITION")
            val isImmediateStart = extras.getBoolean("EXTRA_IMMEDIATE_START")
            val availableDate = extras.getString("EXTRA_AVAILABLE_DATE")

            val message = "Contact Name: $contactName\n" +
             "Contact Number: $contactNumber\n" +
             "Display Name: $displayName\n" +
             "Junior Developer: ${if (isJunior) "Yes" else "No"}\n" +
             "Job Position: $jobPosition\n" +
             "Immediate Start: ${if (isImmediateStart) "Yes" else "No"}\n" +
             "Available From: $availableDate"

            binding.textViewPreview.text = message
        }
    }
}
