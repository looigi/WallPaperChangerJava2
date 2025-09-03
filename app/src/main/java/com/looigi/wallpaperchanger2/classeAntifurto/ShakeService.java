package com.looigi.wallpaperchanger2.classeAntifurto;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;

import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class ShakeService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 1.1f;
    private float accel, accelCurrent, accelLast;
    private Context context;
    private long ultimoTS = 0;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        ultimoTS = System.currentTimeMillis() + 5000;
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        }

        accel = 0.0f;
        accelCurrent = SensorManager.GRAVITY_EARTH;
        accelLast = SensorManager.GRAVITY_EARTH;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = y / SensorManager.GRAVITY_EARTH;
        float gZ = z / SensorManager.GRAVITY_EARTH;

        float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        long ts = System.currentTimeMillis();
        if (ts - ultimoTS > 1000) {
            ultimoTS = ts;

            if (VariabiliStaticheAntifurto.getInstance().isAllarmeSuMovimento()) {
                if (VariabiliStaticheAntifurto.getInstance().getTxtInfo1() != null) {
                    VariabiliStaticheAntifurto.getInstance().getTxtInfo1()
                            .setText("gForce=" + gForce);
                }

                if (gForce >= VariabiliStaticheAntifurto.getInstance().getgForcePerAllarme()) { // && listener != null) {
                    if (!VariabiliStaticheAntifurto.getInstance().isAllarmeInCorso()) {
                        VariabiliStaticheAntifurto.getInstance().setAllarmeInCorso(true);
                        if (VariabiliStaticheAntifurto.getInstance().getTxtAllarme() != null) {
                            VariabiliStaticheAntifurto.getInstance().getTxtAllarme().setText("MOVIMENTO RILEVATO");
                        }

                        Intent i = new Intent(context, MainAntifurto.class);
                        i.putExtra("DO", "ALLARMEMOVIMENTO");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            } else {
                if (VariabiliStaticheAntifurto.getInstance().getTxtInfo1() != null) {
                    VariabiliStaticheAntifurto.getInstance().getTxtInfo1()
                            .setText("Movimento disabilitato");
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    public IBinder onBind(Intent intent) {
        return null; // non serve se è un service "fire-and-forget"
    }
}
