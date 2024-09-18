package com.looigi.wallpaperchanger2.classiStandard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;

public class ScreenReceiver extends BroadcastReceiver {
    private boolean screenOff;
    private static String NomeMaschera = "SCREENRECEVIER";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
           screenOff = false;

           /* if (VariabiliStaticheServizio.getInstance().isImmagineCambiataConSchermoSpento()) {
               Utility.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine dopo schermo spento---");
               VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);


               Handler handlerTimer = new Handler(Looper.getMainLooper());
               Runnable rTimer = new Runnable() {
                   public void run() {
                       ChangeWallpaper c = new ChangeWallpaper(context);
                       boolean fatto = c.setWallpaperLocale(context, VariabiliStaticheServizio.getInstance().getUltimaImmagine());
                       Utility.getInstance().ScriveLog(context, NomeMaschera, "---Immagine impostata online: " + fatto + "---");
                       GestioneNotifiche.getInstance().AggiornaNotifica();
                   }
               };
               handlerTimer.postDelayed(rTimer, 2000);

           }*/

            if (!VariabiliStaticheWallpaper.getInstance().isServizioAttivo()) {
                /* Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() { */
                Esecuzione e = new Esecuzione(context);
                e.startServizio1();

                        /* ChangeWallpaper c = new ChangeWallpaper(context);
                        boolean fatto = c.setWallpaperLocale(context, VariabiliStaticheServizio.getInstance().getUltimaImmagine());
                        Utility.getInstance().ScriveLog(context, NomeMaschera, "---Immagine impostata online: " + fatto + "---");
                        GestioneNotifiche.getInstance().AggiornaNotifica(); */
                    /* }
                };
                handlerTimer.postDelayed(rTimer, 2000); */
            }
        }
        else if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenOff = true;
        }

        VariabiliStaticheWallpaper.getInstance().setScreenOn(!screenOff);

        GestioneNotifiche.getInstance().AggiornaNotifica();

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Cambio schermo: " + screenOff);
    }
}