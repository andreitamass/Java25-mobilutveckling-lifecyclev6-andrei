package com.gritacademy.se

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.math.sqrt

class StepBMIActivity : AppCompatActivity(), SensorEventListener {
    private var bmiScore: TextView? = null
    private var stepText: TextView? = null
    private var stepCounter: Sensor? = null
    private var accelerometer: Sensor? = null
    private var sensorManager: SensorManager? = null
    private var steps = 0
    private var lastMagnitude = 0f
    private var lastStepTime: Long = 0
    private var goToProfile: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.enableEdgeToEdge()
        setContentView(R.layout.activity_step_bmiactivity)

        bmiScore = findViewById<TextView>(R.id.bmiScore)

        stepText = findViewById<TextView>(R.id.stepText)

        goToProfile = findViewById<Button>(R.id.goToProfile)

        goToProfile!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val intent = Intent(this@StepBMIActivity, ProfileActivity::class.java)
                startActivity(intent)
            }
        })

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager

        stepCounter = sensorManager!!.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        accelerometer = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val preferences = getSharedPreferences("profile", 0)

        val height = preferences.getString("height", "0")!!.toDouble()
        val weight = preferences.getString("weight", "0")!!.toDouble()

        val bmi = weight / ((height / 100) * (height / 100))

        bmiScore!!.setText("BMI: " + bmi)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById<View?>(R.id.main),
            OnApplyWindowInsetsListener { v: View?, insets: WindowInsetsCompat? ->
                val systemBars = insets!!.getInsets(WindowInsetsCompat.Type.systemBars())
                v!!.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            })
    }

    override fun onResume() {
        super.onResume()

        if (stepCounter != null) {
            sensorManager!!.registerListener(
                this,
                stepCounter,
                SensorManager.SENSOR_DELAY_NORMAL
            )
        } else if (accelerometer != null) {
            sensorManager!!.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL
            )
            stepText!!.setText("Stepcounter not working")
        }
    }

    override fun onPause() {
        super.onPause()

        sensorManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            val totalSteps = event.values[0].toInt()
            stepText!!.setText("Steps: " + totalSteps)
        } else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]

            val magnitude = sqrt((x * x + y * y + z * z).toDouble()).toFloat()

            if (magnitude - lastMagnitude > 2) {
                val currentTime = System.currentTimeMillis()

                if (currentTime - lastStepTime > 500) {
                    steps++
                    stepText!!.setText("Steps: " + steps)

                    lastStepTime = currentTime
                }
            }

            lastMagnitude = magnitude
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
}

