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

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.content.Intent;

public class ProfileActivity extends AppCompatActivity {

    private EditText profileName;
    private SeekBar profileHeight;
    private EditText profileWeight;
    private Spinner profileAge;
    private RadioButton profileGenderMan;
    private RadioButton profileGenderWoman;
    private RadioButton profileGenderOther;
    private TextView heightView;
    private Button profileSaveButton;

    private Button goToSteps;


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

        profileSaveButton = findViewById(R.id.profileSaveButton);

        goToSteps = findViewById(R.id.goToSteps);

        heightView = findViewById(R.id.textView);

        profileHeight.setProgress(180);

        profileHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                heightView.setText(progress + "cm");
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });


        String[] ages = new String[103]; //  åtkomst till 120 åldrar

        for (int i = 0; i < ages.length; i++) {
            ages[i] = String.valueOf(i + 18);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, ages
        );

        profileAge.setAdapter(adapter);

        profileSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = profileName.getText().toString();
                String height = String.valueOf(profileHeight.getProgress());
                String weight = profileWeight.getText().toString();
                String age = profileAge.getSelectedItem().toString();

                String gender = "";

                if (profileGenderMan.isChecked()) {
                    gender = "Man";
                } else if (profileGenderWoman.isChecked()) {
                    gender = "Woman";
                } else if (profileGenderOther.isChecked()) {
                    gender = "Other";
                }

                SharedPreferences preferences =
                        getSharedPreferences("profile", 0);

                SharedPreferences.Editor editor = preferences.edit();

                if (!name.isEmpty()) {
                    editor.putString("name", name);
                }

                if (!height.isEmpty()) {
                    editor.putString("height", height);
                }

                if (!weight.isEmpty()) {
                    editor.putString("weight", weight);
                }

                if (!age.isEmpty()) {
                    editor.putString("age", age);
                }

                if (!gender.isEmpty()) {
                    editor.putString("gender", gender);
                }

                editor.apply();

                Intent intent = new Intent(ProfileActivity.this, StepBMIActivity.class);
                startActivity(intent);

            }
        });

        goToSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, StepBMIActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}