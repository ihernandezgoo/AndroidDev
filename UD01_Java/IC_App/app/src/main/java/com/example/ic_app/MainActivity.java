package com.example.ic_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText inches;
    Button calculate;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inchesString = inches.getText().toString().trim();

                if (inchesString.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Inches box is empty, try again!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        double resultDouble = calculateHeight(inchesString);
                        DecimalFormat myDF = new DecimalFormat("0.00");
                        String resultString = "Your height is " + myDF.format(resultDouble) + " cm";
                        result.setText(resultString);
                    } catch (NumberFormatException e) {
                        Toast.makeText(MainActivity.this, "Please enter a valid number!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private double calculateHeight(String inches) throws NumberFormatException {
        double inchesDouble = Double.parseDouble(inches);
        return inchesDouble * 0.0254;
    }

    private void findViews() {
        inches = findViewById(R.id.inchesText);
        calculate = findViewById(R.id.calculateButton);
        result = findViewById(R.id.TextResult);
    }
}