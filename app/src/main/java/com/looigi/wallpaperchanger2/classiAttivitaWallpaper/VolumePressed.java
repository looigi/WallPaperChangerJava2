package com.looigi.wallpaperchanger2.classiAttivitaWallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.MainStart;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.AndroidCameraApi;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.gps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class VolumePressed extends ContentObserver {
    private int previousVolume;
    private Context context;
    private Long datella1 = null;
    private int lastVolume;
    private Activity act;

    public VolumePressed(Context c, Handler handler) {
        super(handler);

        context=c;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int volume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (previousVolume != volume) {
            previousVolume = volume;

            if (datella1 == null) {
                Handler handlerTimer;
                Runnable rTimer;

                datella1 = System.currentTimeMillis();
                UtilityDetector.getInstance().Vibra(context, 100);

                handlerTimer = new Handler();
                rTimer = new Runnable() {
                    public void run() {
                        datella1 = null;
                    }
                };
                handlerTimer.postDelayed(rTimer, 2000);
            } else {
                long diff = System.currentTimeMillis() - datella1;

                datella1 = null;

                if (diff < 1950) {
                    // audio.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume);
                    UtilityDetector.getInstance().Vibra(context, 1000);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent myIntent = new Intent(
                                    VariabiliStaticheStart.getInstance().getContext(),
                                    AndroidCameraApi.class);
                            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            VariabiliStaticheStart.getInstance().getContext().startActivity(myIntent);
                        }
                    }, 1000);

                    VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                    VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
                }
            }
        }
    }
}