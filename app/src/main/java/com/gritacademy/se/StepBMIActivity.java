package com.gritacademy.se;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.SharedPreferences;
import android.widget.TextView;

public class StepBMIActivity extends AppCompatActivity implements SensorEventListener {

    private TextView bmiScore;
    private TextView stepText;
    private Sensor stepCounter;
    private SensorManager sensorManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step_bmiactivity);

        bmiScore = findViewById(R.id.bmiScore);

        stepText = findViewById(R.id.stepText);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

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

    @Override
    protected void onResume() {
        super.onResume();

        sensorManager.registerListener(
                this,
                stepCounter,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        int steps = (int) event.values[0];

        stepText.setText("Steps: " + steps);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

