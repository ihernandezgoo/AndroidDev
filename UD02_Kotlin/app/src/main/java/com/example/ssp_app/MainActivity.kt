package com.example.ssp_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ssp_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPreview.setOnClickListener {
            onPreviewClicked()
        }
    }

    private fun onPreviewClicked() {
        val contactName = binding.editTextContactName.text.toString()
        val contactNumber = binding.editTextContactNumber.text.toString()
        val displayName = binding.editTextDisplayName.text.toString()
        val isJunior = binding.checkboxJunior.isChecked
        val jobPosition = binding.spinnerJobs.selectedItem.toString()
        val isImmediateStart = binding.startNow.isChecked
        val availableDate = binding.editTextAvailableFrom.text.toString()

        val previewActivityIntent = Intent(this, PreviewActivity::class.java).apply {
            putExtra("EXTRA_CONTACT_NAME", contactName)
            putExtra("EXTRA_CONTACT_NUMBER", contactNumber)
            putExtra("EXTRA_DISPLAY_NAME", displayName)
            putExtra("EXTRA_IS_JUNIOR", isJunior)
            putExtra("EXTRA_JOB_POSITION", jobPosition)
            putExtra("EXTRA_IMMEDIATE_START", isImmediateStart)
            putExtra("EXTRA_AVAILABLE_DATE", availableDate)
        }
        startActivity(previewActivityIntent)
    }
}
