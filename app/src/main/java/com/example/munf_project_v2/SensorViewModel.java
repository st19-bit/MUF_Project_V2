package com.example.munf_project_v2;

import android.app.Application;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class SensorViewModel extends AndroidViewModel {

    // AccelerationInformation als Variable LiveData definieren
    final LiveData<AccelerationInformation> accelerationLiveData; // final = konstantes Feld/nur lesender Zugriff

    public SensorViewModel(@NonNull Application application) {
        super(application);
        accelerationLiveData = new AccelerationLiveData(application.getApplicationContext());
    }

    private final static class AccelerationLiveData extends LiveData<AccelerationInformation>{
        // AccelerationInformation = generischer Datentyp (= eigene Klasse)
        private final AccelerationInformation accelerationInformation = new AccelerationInformation();
        private SensorManager sm;
        private Sensor acceleromter;
        private Sensor gravitySensor;

        private float[] gravity;

        private SensorEventListener listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                switch (sensorEvent.sensor.getType()) {
                    case Sensor.TYPE_ACCELEROMETER:
                        float[] values = removeGravity(gravity, sensorEvent.values);
                        // float[] values = event.values; // Alternative: entfernt Gravitation nicht
                        accelerationInformation.setXYZ(values[0], values[1], values[2]);
                        // Datentyp "Sensor" in accelerationInformation hineinbringen um
                        // später Metainformationen vom Sensor (resulution, etc.) einfach zu erhalten
                        accelerationInformation.setSensor(sensorEvent.sensor);
                        setValue(accelerationInformation);
                        break;
                    case Sensor.TYPE_GRAVITY:
                        gravity = sensorEvent.values;
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };

        AccelerationLiveData(Context context){
            sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
            // sm kann leer sein -> check
            if(sm != null){
                // Do: (wenn sm nicht leer ist)
                gravitySensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
                acceleromter = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            }else {
                // sollte eigentlich nicht auftreten -> absturz der App
                throw new RuntimeException("Du Spezialist...");
            }
        }

        @Override
        protected void onActive() {
            super.onActive();
            // Listener registrieren: (wenn LiveCycle on Resume)
            sm.registerListener(listener, gravitySensor,SensorManager.SENSOR_DELAY_NORMAL);
            sm.registerListener(listener, acceleromter, SensorManager.SENSOR_DELAY_NORMAL);
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            // Listener unregistrieren: (wenn LiveCyle on Pause)
            sm.unregisterListener(listener);

        }

    private float[] removeGravity(float[] gravity, float[] values) {

        if (gravity == null) {
            return values;
        }
        final float alpha = 0.8f;
        float g[] = new float[3];
        g[0] = alpha * gravity[0] + (1 - alpha) * values[0];
        g[1] = alpha * gravity[2] + (1 - alpha) * values[1];
        g[2] = alpha * gravity[2] + (1 - alpha) * values[2];
        // liefert Acceleration-Werte ohne Gravitationsbeschleunigung
        return new float[]{
                values[0] - g[0],
                values[1] - g[1],
                values[2] - g[2]
        };
    };
    }

}
