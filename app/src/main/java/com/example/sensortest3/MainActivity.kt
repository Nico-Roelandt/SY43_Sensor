package com.example.sensortest3

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null
    private var gyroscopeSensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val deviceSensors: List<Sensor> = sensorManager.getSensorList(Sensor.TYPE_ALL)

        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)

        setContent {
            SensorAppContent(lightSensor, accelerometerSensor, gyroscopeSensor, deviceSensors)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_LIGHT -> {
                    lightValue = "Light: ${it.values[0]}"
                }
                Sensor.TYPE_ACCELEROMETER -> {
                    accelerometerValue = "Accelerometer: X=${it.values[0]}, Y=${it.values[1]}, Z=${it.values[2]}"
                }
                Sensor.TYPE_GYROSCOPE -> {
                    gyroscopeValue = "Gyroscope: X=${it.values[0]}, Y=${it.values[1]}, Z=${it.values[2]}"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    companion object {
        var lightValue by mutableStateOf("Sensor light not available")
        var accelerometerValue by mutableStateOf("Sensor speed not available")
        var gyroscopeValue by mutableStateOf("Sensor gyroscope not available")
    }

    override fun onResume() {
        super.onResume()
        lightSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        accelerometerSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        gyroscopeSensor?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}

@Composable
fun SensorAppContent(
    lightSensor: Sensor?,
    accelerometerSensor: Sensor?,
    gyroscopeSensor: Sensor?,
    deviceSensors: List<Sensor>
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Sensor demo", modifier = Modifier.padding(bottom = 16.dp))

        if (lightSensor != null) {
            Text(text = MainActivity.lightValue, modifier = Modifier.padding(bottom = 8.dp))
        } else {
            Text(text = "Light sensor not available", modifier = Modifier.padding(bottom = 8.dp))
        }

        if (accelerometerSensor != null) {
            Text(text = MainActivity.accelerometerValue, modifier = Modifier.padding(bottom = 8.dp))
        } else {
            Text(text = "Acceleration sensor not available", modifier = Modifier.padding(bottom = 8.dp))
        }


        if (gyroscopeSensor != null) {
            Text(text = MainActivity.gyroscopeValue, modifier = Modifier.padding(bottom = 8.dp))
        } else {
            Text(text = "Gyroscope sensor not available", modifier = Modifier.padding(bottom = 8.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                deviceSensors.forEach { sensor ->
                    Column(modifier = Modifier.padding(8.dp)) {
                        Text(text = "Name of sensor : ${sensor.name}")
                        Text(text = "Type of sensor : ${sensor.type}")
                        Text(text = "Manufacturer : ${sensor.vendor}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
@Preview
fun DefaultPreview() {
    SensorAppContent(lightSensor = null, accelerometerSensor = null, gyroscopeSensor = null, deviceSensors = emptyList())
}
