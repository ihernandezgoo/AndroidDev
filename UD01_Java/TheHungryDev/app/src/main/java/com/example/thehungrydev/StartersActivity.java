package com.example.thehungrydev;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class StartersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_starters);

        ListView startersList = findViewById(R.id.startersList);

        Dish dishOne = new Dish("Mushroom and tofu maki", "None", 10);
        Dish dishTwo = new Dish("Egg and avocado uramaki", "None", 10);
        Dish dishThree = new Dish("Melon and lemon soup", "None", 10);
        Dish dishFour = new Dish("Coconut and chocolate mousse", "None", 10);
        Dish dishFive = new Dish("Spinach and cabbage wontons", "None", 10);


        String[] dishes = {
                dishOne.title,
                dishTwo.title,
                dishThree.title,
                dishFour.title,
                dishFive.title
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dishes);
        startersList.setAdapter(adapter);
    }
}