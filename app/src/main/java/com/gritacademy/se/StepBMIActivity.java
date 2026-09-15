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
    private Sensor accelerometer;
    private SensorManager sensorManager;
    private int steps = 0;
    private float lastMagnitude = 0;
    private long lastStepTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_step_bmiactivity);

        bmiScore = findViewById(R.id.bmiScore);

        stepText = findViewById(R.id.stepText);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

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

        if (stepCounter != null) {
            sensorManager.registerListener(
                    this,
                    stepCounter,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
        } else if(accelerometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL
            );
            stepText.setText("Stepcounter not working");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {

            int totalSteps = (int) event.values[0];
            stepText.setText("Steps: " + totalSteps);

        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            float magnitude = (float) Math.sqrt(x * x + y * y + z * z);

            if (magnitude - lastMagnitude > 2) {

                long currentTime = System.currentTimeMillis();

                if (currentTime - lastStepTime > 500) {

                    steps++;
                    stepText.setText("Steps: " + steps);

                    lastStepTime = currentTime;
                }
            }

            lastMagnitude = magnitude;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}

