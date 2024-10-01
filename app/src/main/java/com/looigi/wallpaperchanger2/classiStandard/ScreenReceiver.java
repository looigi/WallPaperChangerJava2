package com.looigi.wallpaperchanger2.classiStandard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;

public class ScreenReceiver extends BroadcastReceiver {
    private boolean screenOff;
    private static String NomeMaschera = "SCREENRECEVIER";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
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