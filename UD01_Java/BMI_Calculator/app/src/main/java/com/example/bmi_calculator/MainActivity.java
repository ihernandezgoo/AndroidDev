package com.example.bmi_calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {

    RadioButton maleRadio, femaleRadio;
    EditText ageText, feetText, inchesText, weightText;
    Button calculateButton;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();

        String popupText = "Hello to the BMI Calculator";

        // POPUP TENPORALA SORTZEN DU HASIERATZEKOAN
        Toast.makeText(this, popupText, Toast.LENGTH_LONG).show();
        setupButtonListener();



    }

    private void setupButtonListener() {
        // BOTOIA KLIKATZEAN POPUP MEZUA AGERTUKO DA
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Calculate button clicked", Toast.LENGTH_SHORT).show();
                double bmiResult = calculateBMI();
                displayResult(bmiResult);
            }
        });
    }

    private double calculateBMI() {
        String newAge = ageText.getText().toString();
        String newFeet = feetText.getText().toString();
        String newInches = inchesText.getText().toString();
        String newWeight = weightText.getText().toString();

        int age = Integer.parseInt(newAge);
        int feet = Integer.parseInt(newFeet);
        int inches = Integer.parseInt(newInches);
        int weight = Integer.parseInt(newWeight);

        int totalInches = (feet * 12) + inches;
        double heightInMeters = totalInches*0.0254;

        return (double) weight / (heightInMeters * heightInMeters);


    }

    private void displayResult(double bmi) {
        DecimalFormat myDecimalFormat = new DecimalFormat("0,00");
        String bmiResult = myDecimalFormat.format(bmi);



        String fullStringResult;
        if (bmi < 18.5) {
            fullStringResult = bmiResult + " Your are underweight";
        } else if (bmi > 25) {
            fullStringResult = bmiResult + " Your are overweight";
        } else {
            fullStringResult = bmiResult + " Your are a healthy weight";
        }

        resultText.setText(fullStringResult);
    }

    private void displayGuidance(double bmi) {
        DecimalFormat df = new DecimalFormat("0.00");
        String bmiResult = df.format(bmi);

        String fullResult;

        if (maleRadio.isChecked()) {
            fullResult = "Your BMI is " + bmiResult + ". As a male, consider the following advice...";
        } else if (femaleRadio.isChecked()) {
            fullResult = "Your BMI is " + bmiResult + ". As a female, consider the following advice...";
        } else {
            fullResult = "Your BMI is " + bmiResult + ". Please select a gender for tailored advice.";
        }
        resultText.setText(fullResult);
    }

    private void findViews() {
        maleRadio = findViewById(R.id.radio_button_male);
        femaleRadio = findViewById(R.id.radio_button_female);

        ageText = findViewById(R.id.age_text);
        feetText = findViewById(R.id.feet_text);
        inchesText = findViewById(R.id.inches_text);
        weightText = findViewById(R.id.weight_text);

        calculateButton = findViewById(R.id.button_calculate);
        resultText = findViewById(R.id.result_text);
    }
}