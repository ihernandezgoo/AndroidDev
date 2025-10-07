package com.example.ssp_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    var contactNameText : TextInputEditText? = null;
    var contactNumberText : TextInputEditText? = null;
    var myDisplayName : TextInputEditText? = null;
    var juniorCheckBox : CheckBox? = null;
    var jobSpinner : Spinner? = null;
    var immediateStart : CheckBox? = null;
    var availableFrom : TextInputEditText? = null;


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

    contactNameText = findViewById(R.id.edit_text_contact_name);
    contactNumberText = findViewById(R.id.edit_text_contact_number);
    myDisplayName = findViewById(R.id.edit_text_display_name);
    juniorCheckBox = findViewById(R.id.checkbox_junior);
    jobSpinner = findViewById(R.id.spinner_jobs);
    immediateStart = findViewById(R.id.start_now);
    availableFrom = findViewById(R.id.edit_text_available_from);

    val previewActivityInt = Intent(this, PreviewActivity::class.java);

    startActivity(previewActivityInt)

}
