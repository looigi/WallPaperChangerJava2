/* package com.looigi.wallpaperchanger2.classeGps;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class ServizioDiAvvioGPS extends Service {
    private static final String NomeMaschera = "Servizio_Interno_GPS";
    private Context context;
    private NotificationManager manager;

    @Override
    public void onCreate() {
        super.onCreate();

        VariabiliStaticheGPS.getInstance().setContext(this);

        Notification notifica = GestioneNotificaGPS.getInstance().StartNotifica(this);
        if (notifica != null) {
            VariabiliStaticheGPS.getInstance().setGpsAttivo(false);

            // startForeground(VariabiliStaticheGPS.NOTIFICATION_CHANNEL_ID, notifica);
            // GestioneNotificaGPS.getInstance().AggiornaNotifica();

            if (VariabiliStaticheGPS.getInstance().getGestioneGPS() == null) {
                VariabiliStaticheGPS.getInstance().setGestioneGPS(new GestioneGPS());
                VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaTimer(context);
                VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS("On Create Servizio GPS");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        String NomeMaschera = "Gestione_GPS";
        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "OnDestroy Servizio di avvio GPS");

        if (VariabiliStaticheGPS.getInstance().getGestioneGPS() == null) {
            VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("SERVIZIO");
            VariabiliStaticheGPS.getInstance().setGestioneGPS(null);
        }

        // if (VariabiliStaticheStart.getInstance().getServizioForegroundGPS() == null) {
        //     stopService(VariabiliStaticheStart.getInstance().getServizioForegroundGPS());
        // }

        GestioneNotificaGPS.getInstance().RimuoviNotifica();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
*/