package com.looigi.wallpaperchanger2.UtilitiesVarie.AutoStart;

import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

public class RunServiceOnBoot extends android.app.Service {
    private Handler handler;
    private Runnable runnable;
    private final int runTime = 5000;

    @Override
    public void onCreate() {
        super.onCreate();

        handler = new Handler(Looper.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(runnable, runTime);
            }
        };
        handler.post(runnable);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(runnable);
        }
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_REDELIVER_INTENT ;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }
}