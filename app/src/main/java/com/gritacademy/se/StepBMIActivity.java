package com.gritacademy.se;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.widget.TextView;

public class StepBMIActivity extends AppCompatActivity {

    private TextView bmiScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step_bmiactivity);

        bmiScore = findViewById(R.id.bmiScore);

        SharedPreferences preferences = getSharedPreferences("profile", 0);

        double height = Double.parseDouble(preferences.getString("height", "0"));
        double weight = Double.parseDouble(preferences.getString("weight", "0"));

        double bmi = weight / ((height / 100) * (height / 100));

        bmiScore.setText("BMI: " + bmi);







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}