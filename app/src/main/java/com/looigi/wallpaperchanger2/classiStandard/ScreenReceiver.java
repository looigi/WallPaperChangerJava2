package com.looigi.wallpaperchanger2.classiStandard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classiAttivitaDetector.AndroidCameraApi;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.Objects;

public class ScreenReceiver extends BroadcastReceiver {
    private boolean screenOff;
    private static String NomeMaschera = "SCREENRECEVIER";
    private Long datella1 = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (VariabiliStaticheDetector.getInstance().isFotoSuPower()) {
            if (datella1 == null) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio schermo 1");

                Handler handlerTimer;
                Runnable rTimer;

                datella1 = System.currentTimeMillis();
                // UtilityDetector.getInstance().Vibra(context, 100);

                handlerTimer = new Handler(Looper.getMainLooper());
                rTimer = new Runnable() {
                    public void run() {
                        // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio schermo 1 - Annullo. Troppo Tempo");

                        datella1 = null;
                    }
                };
                handlerTimer.postDelayed(rTimer, 1100);
            } else {
                long diff = System.currentTimeMillis() - datella1;

                datella1 = null;

                // UtilityWallpaper.getInstance().ApreToast(context, String.valueOf(diff));

                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio schermo 2. Diff: " + diff);

                if (diff < 1050) {
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio schermo 2. Eseguo");

                    UtilityDetector.getInstance().Vibra(context, 200);

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

        if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON)) {
           screenOff = false;

            if (!VariabiliStaticheWallpaper.getInstance().isServizioAttivo()) {
                Esecuzione e = new Esecuzione(context);
                e.startServizio1();
            }
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
        }

        VariabiliStaticheWallpaper.getInstance().setScreenOn(!screenOff);

        GestioneNotifiche.getInstance().AggiornaNotifica();

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio schermo: " + screenOff);
    }
}