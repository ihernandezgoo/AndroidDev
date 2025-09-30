package com.example.ssp_app

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);

        val previewButton : Button = findViewById(R.id.button_preview);
        previewButton.setOnClickListener {
            onPreviewClicked();
        }
    }
}

private fun MainActivity.onPreviewClicked() {
    val contactName : EditText = findViewById(R.id.edit_text_contact_name);
    val contactNumber : EditText = findViewById(R.id.edit_text_contact_number);
    val testString : String = contactName.text.toString() + "" + contactNumber.text.toString();

    Toast.makeText(this.applicationContext, testString, Toast.LENGTH_SHORT).show()
}
