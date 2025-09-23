package com.example.vins_androidprojects;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_project);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView list = findViewById(R.id.main);

        Project[] projects = {
                new Project("Getting started app", "Our first project, Hello world and Project creations", R.drawable.helloworld),
                new Project("BMI Calculator", "Based on gender, kg and height calculates the perfect BMI for every person", R.drawable.bmi),
                new Project("Inches App", "Converts inches to centimeters and centimeters to inches", R.drawable.ic_cal),
                new Project("The Hungry Dev", "App for activities and events practice with dishes", R.drawable.thdev)
        };



    }
}