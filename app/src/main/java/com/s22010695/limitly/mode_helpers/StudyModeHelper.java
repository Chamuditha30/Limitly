package com.s22010695.limitly.mode_helpers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.s22010695.limitly.db_helpers.StudyModeTableHandler;


public class StudyModeHelper implements SensorEventListener {
    //declare objects
    private final StudyModeTableHandler dbHelper;
    private final SensorManager sensorManager;
    private final Sensor accelerometer;

    //declare variables to track face down, pending face down, face down start time and delay
    private boolean isFaceDown = false;
    private boolean pendingFaceDown = false;
    private long faceDownStartTime = 0;
    private static final long FACE_DOWN_DELAY_MS = 10000;

    //create constructor
    public StudyModeHelper(Context context) {
        this.dbHelper = new StudyModeTableHandler(context);
        this.sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        this.accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public boolean isEnable() {
        return dbHelper.getIsEnable();
    }

    //start tracking if enabled
    public void start() {
        if (isEnable()) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    //stop tracking
    public void stop() {
        sensorManager.unregisterListener(this);
        isFaceDown = false;
        pendingFaceDown = false;
        faceDownStartTime = 0;
    }

    //returns true if apps should be blocked
    public boolean apply() {
        return isFaceDown;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (!isEnable()) return;

        float z = event.values[2];

        long currentTime = System.currentTimeMillis();

        if (z < -9.0f) {
            //device is face down
            if (!pendingFaceDown) {
                pendingFaceDown = true;
                faceDownStartTime = currentTime;
            } else {
                //if face down for over 10 seconds
                if (!isFaceDown && (currentTime - faceDownStartTime) >= FACE_DOWN_DELAY_MS) {
                    isFaceDown = true;
                }
            }
        } else if (z > 9.0f) {
            //device is face up
            isFaceDown = false;
            pendingFaceDown = false;
            faceDownStartTime = 0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
