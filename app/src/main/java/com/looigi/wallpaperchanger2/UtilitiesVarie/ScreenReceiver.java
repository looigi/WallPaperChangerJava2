package com.looigi.wallpaperchanger2.UtilitiesVarie;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.looigi.wallpaperchanger2.Wallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.Wallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.Notifiche.notificaTasti.GestioneNotificheTasti;

import java.util.Objects;

public class ScreenReceiver extends BroadcastReceiver {
    private boolean screenOff;
    private static String NomeMaschera = "Screen_Receiver";
    private Long datella1 = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        /* if (VariabiliStaticheDetector.getInstance().isFotoSuTriploTastoCuffie()) {
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

                // UtilitiesGlobali.getInstance().ApreToast(context, String.valueOf(diff));

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
                    }, 100);

                    VariabiliStaticheDetector.getInstance().ChiudeActivity(true);
                    VariabiliStaticheWallpaper.getInstance().ChiudeActivity(true);
                }
            }
        } */

        if (Objects.equals(intent.getAction(), Intent.ACTION_SCREEN_ON)) {
            screenOff = false;

            int acc = VariabiliStaticheStart.getInstance().getAccensioniDiSchermo();
            VariabiliStaticheStart.getInstance().setAccensioniDiSchermo(acc + 1);
            GestioneNotificheTasti.getInstance().AggiornaNotifica();

            GestioneNotificheWP.getInstance().AggiornaNotifica();

            /* if (!VariabiliStaticheWallpaper.getInstance().isServizioAttivo()) {
                Esecuzione e = new Esecuzione(context);
                e.startServizio1();
            } */
            if (VariabiliStaticheWallpaper.getInstance().isImpostataConSchermoSpento()) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                        "Cambio immagine per flag impostato");

                UtilityWallpaper.getInstance().CambiaImmagine(context);
            }
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
        }

        VariabiliStaticheWallpaper.getInstance().setScreenOn(!screenOff);

        GestioneNotificheWP.getInstance().AggiornaNotifica();

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Cambio schermo: " + screenOff);
    }
}