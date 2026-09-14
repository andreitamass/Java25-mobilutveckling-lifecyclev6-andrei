package com.gritacademy.se;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private EditText profileName;
    private SeekBar profileHeight;
    private EditText profileWeight;
    private Spinner profileAge;
    private RadioButton profileGenderMan;
    private RadioButton profileGenderWoman;
    private RadioButton profileGenderOther;
    TextView heightView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileHeight = findViewById(R.id.profileHeight);
        profileWeight = findViewById(R.id.profileWeight);

        profileAge = findViewById(R.id.profileAge);
        profileGenderMan = findViewById(R.id.profileGenderMan);
        profileGenderWoman = findViewById(R.id.profileGenderWoman);
        profileGenderOther = findViewById(R.id.profileGenderOther);
        heightView = findViewById(R.id.textView);

        profileHeight.setMax(80);
        profileHeight.setProgress(40);

        String[] ages = new String[99]; // 99 åldrar

        for (int i = 0; i < ages.length; i++) {
            ages[i] = String.valueOf(i + 18);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, ages
        );

        profileAge.setAdapter(adapter);









        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}