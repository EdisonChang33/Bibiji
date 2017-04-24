package com.bibizhaoji.bibiji.support;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.speech.utils.LogEnv;

/**
 * Created by EdisonChang on 2016/6/30.
 */
public class ShakeDetector implements SensorEventListener {

    private static final String TAG = "ShakeDetector";

    private static final double SHAKE_SHRESHOLD = 7000d;
    private Context mContext;
    private long lastTime;
    private float last_x;
    private float last_y;
    private float last_z;

    private SensorManager sensorManager;
    private onShakeListener shakeListener;

    public ShakeDetector(Context context) {
        mContext = context;
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
    }

    public boolean registerListener() {

        if (sensorManager != null) {
            Sensor sensor = sensorManager
                    .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            if (sensor != null) {
                this.sensorManager.registerListener(this, sensor,
                        SensorManager.SENSOR_DELAY_GAME);
                if (LogEnv.enable) {
                    Log.d(TAG, "registerListener: succeed!!");
                }

                return true;
            }
        }
        return false;
    }

    public void unRegisterListener() {
        if (sensorManager != null)
            sensorManager.unregisterListener(this);
    }

    public void setOnShakeListener(onShakeListener listener) {
        shakeListener = listener;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        long curTime = java.lang.System.currentTimeMillis();
        if ((curTime - lastTime) > 10) {
            long diffTime = (curTime - lastTime);
            lastTime = curTime;
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];
            float speed = Math.abs(x + y + z - last_x - last_y - last_z)
                    / diffTime * 10000;
            if (speed > SHAKE_SHRESHOLD) {
                shakeListener.onShake();
            }
            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public interface onShakeListener {
        void onShake();
    }
}
