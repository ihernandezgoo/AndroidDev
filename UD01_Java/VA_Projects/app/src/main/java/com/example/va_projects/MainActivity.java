package com.example.va_projects; // Make sure this package name is correct for your project

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {    @Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main); // This should be your main layout XML file

    @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView projectsRecyclerView = findViewById(R.id.recycler_view_projects);

    List<Project> projectsList = new ArrayList<>();

    projectsList.add(new Project("Getting started app", "Our first project, Hello world and Project creations", R.drawable.ic_launcher_background)); // Placeholder image
    projectsList.add(new Project("BMI Calculator", "Based on gender, kg and height calculates the perfect BMI for every person", R.drawable.ic_launcher_background)); // Placeholder image
    projectsList.add(new Project("Inches App", "Converts inches to centimeters and centimeters to inches", R.drawable.ic_launcher_background)); // Placeholder image
    projectsList.add(new Project("The Hungry Dev", "App for activities and events practice with dishes", R.drawable.ic_launcher_background)); // Placeholder image

    ProjectAdapter adapter = new ProjectAdapter(projectsList);

    projectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    projectsRecyclerView.setAdapter(adapter);
}
}
