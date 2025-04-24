package com.looigi.wallpaperchanger2.classeGps;

import android.location.Location;

public class CalcoloVelocita {
    private static final float MIN_ACCURACY = 10.0f; // metri
    private static final float MAX_VALID_SPEED = 200.0f; // km/h

    private Location lastLocation = null;
    private long lastUpdateTime = 0;
    private float lastSmoothedSpeed = 0f;

    public float calculateRobustSpeed(Location currentLocation) {
        if (currentLocation == null || currentLocation.getAccuracy() > MIN_ACCURACY) {
            return 0f;
        }

        float rawSpeedMps = currentLocation.getSpeed();

        // Se getSpeed è affidabile, usiamolo
        if (rawSpeedMps > 0 && rawSpeedMps < MAX_VALID_SPEED / 3.6f) {
            return smoothSpeed(rawSpeedMps * 3.6f); // Convertito in km/h
        }

        // Altrimenti calcoliamo la velocità tra due posizioni
        if (lastLocation != null) {
            float distanceMeters = currentLocation.distanceTo(lastLocation);
            long timeDeltaMillis = currentLocation.getTime() - lastUpdateTime;

            if (timeDeltaMillis > 0) {
                float speedMps = (distanceMeters / timeDeltaMillis) * 1000; // da ms a s
                float speedKmh = speedMps * 3.6f;
                if (speedKmh < MAX_VALID_SPEED) {
                    lastLocation = currentLocation;
                    lastUpdateTime = currentLocation.getTime();
                    return smoothSpeed(speedKmh);
                }
            }
        }

        // Aggiorna gli ultimi valori
        lastLocation = currentLocation;
        lastUpdateTime = currentLocation.getTime();

        return 0f;
    }

    // Media mobile semplice per evitare picchi
    private float smoothSpeed(float newSpeedKmh) {
        final float alpha = 0.7f; // quanto pesa il nuovo valore (0 = liscio, 1 = reattivo)
        lastSmoothedSpeed = alpha * newSpeedKmh + (1 - alpha) * lastSmoothedSpeed;

        return lastSmoothedSpeed;
    }
}
