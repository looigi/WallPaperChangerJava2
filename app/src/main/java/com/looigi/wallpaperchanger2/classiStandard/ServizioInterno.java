package com.looigi.wallpaperchanger2.classiStandard;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.MainActivityDetector;
import com.looigi.wallpaperchanger2.MainWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.db_dati_detector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.db_dati;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

public class ServizioInterno extends Service {
    private static final String NomeMaschera = "SERVIZIOINTERNO";
    private Context context;
    private ScreenReceiver mScreenReceiver;
    private PowerManager.WakeLock wl;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Utility.getInstance().stopService(context);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "onConfigurationChanged: " + newConfig.uiMode);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Start Command");

        return START_STICKY;
    }

    @SuppressLint({"ForegroundServiceType", "WakelockTimeout"})
    @Override
    public void onCreate() {
        context = this;
        VariabiliStaticheServizio.getInstance().setContext(this);

        Utility.getInstance().ScriveLog(context, NomeMaschera, "On Create");

        mScreenReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenReceiver, filter);

        // CPU Attiva
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                VariabiliStaticheServizio.channelName);
        wl.acquire();
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Notification notifica = GestioneNotifiche.getInstance().StartNotifica(this);
        // VariabiliStatiche.getInstance().setNotifica(GestioneNotifiche.getInstance().StartNotifica(this));
        if (notifica != null) {
            startForeground(VariabiliStaticheServizio.NOTIFICATION_CHANNEL_ID, notifica);
            Utility.getInstance().ScriveLog(context, NomeMaschera, "Notifica instanziata");
            GestioneNotifiche.getInstance().AggiornaNotifica();

            Esecuzione e = new Esecuzione(context);
            e.startServizio1();

            // PARTENZA MASCHERE
            Utility.getInstance().ScriveLog(context, NomeMaschera, "Apro db");
            db_dati db = new db_dati(context);
            Utility.getInstance().ScriveLog(context, NomeMaschera,"Creo tabelle");
            db.CreazioneTabelle();
            Utility.getInstance().ScriveLog(context, NomeMaschera,"Leggo impostazioni");
            boolean letto = db.LeggeImpostazioni();
            Utility.getInstance().ScriveLog(context, NomeMaschera,"Impostazioni lette: " + letto);
            VariabiliStaticheServizio.getInstance().setLetteImpostazioni(letto);

            db_dati_detector dbD = new db_dati_detector(context);
            dbD.CreazioneTabelle();

            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Leggo impostazioni");
            boolean lettoD = UtilityDetector.getInstance().LeggeImpostazioni(context);
            VariabiliStaticheDetector.getInstance().setLetteImpostazioni(lettoD);

            if (VariabiliStaticheServizio.getInstance().isDetector() &&
                    !VariabiliStaticheDetector.getInstance().isMascheraPartita() &&
                    VariabiliStaticheServizio.getInstance().isCiSonoPermessi()) {
                Intent iD = new Intent(context, MainActivityDetector.class);
                iD.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iD);
            }

            Intent iW = new Intent(context, MainWallpaper.class);
            iW.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(iW);

            Utility.getInstance().ApreToast(context, "Wallpaper Partito");

            if (VariabiliStaticheServizio.getInstance().isDetector() &&
                    !VariabiliStaticheDetector.getInstance().isMascheraPartita() &&
                    VariabiliStaticheServizio.getInstance().isCiSonoPermessi()) {
                Notification notificaDetector = GestioneNotificheDetector.getInstance().StartNotifica(context);
                if (notificaDetector != null) {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Notifica instanziata");
                    GestioneNotificheDetector.getInstance().AggiornaNotifica();

                    Utility.getInstance().ApreToast(context, "Detector Partito");
                }
            }
            // PARTENZA MASCHERE
        } else {
            Utility.getInstance().ScriveLog(context, NomeMaschera, "Notifica " + VariabiliStaticheServizio.channelName + " nulla");
            Toast.makeText(this, "Notifica " + VariabiliStaticheServizio.channelName + " nulla", Toast.LENGTH_SHORT).show();
        }
    }

    private void ChiudeTutto() {
        GestioneNotifiche.getInstance().RimuoviNotifica();

        if (mScreenReceiver != null) {
            unregisterReceiver(mScreenReceiver);
        }

        stopForeground(true);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Utility.getInstance().ScriveLog(context, NomeMaschera, "On Destroy");

        if (wl != null) {
            wl.release();
        }

        if (VariabiliStaticheServizio.getInstance().isSbragaTutto()) {
            ChiudeTutto();
        }
    }
}