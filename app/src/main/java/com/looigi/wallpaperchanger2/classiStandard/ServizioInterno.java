package com.looigi.wallpaperchanger2.classiStandard;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.MainActivityDetector;
import com.looigi.wallpaperchanger2.MainWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.InizializzaMascheraDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.db_dati_detector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.db_dati_wallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

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

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "onConfigurationChanged: " + newConfig.uiMode);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Start Command");

        return START_STICKY;
    }

    @SuppressLint({"ForegroundServiceType", "WakelockTimeout"})
    @Override
    public void onCreate() {
        context = this;

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "On Create");

        mScreenReceiver = new ScreenReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(mScreenReceiver, filter);

        // CPU Attiva
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                VariabiliStaticheWallpaper.channelName);
        wl.acquire();
        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Notification notifica = GestioneNotifiche.getInstance().StartNotifica(this);
        // VariabiliStatiche.getInstance().setNotifica(GestioneNotifiche.getInstance().StartNotifica(this));
        if (notifica != null) {
            startForeground(VariabiliStaticheWallpaper.NOTIFICATION_CHANNEL_ID, notifica);
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Notifica instanziata");
            GestioneNotifiche.getInstance().AggiornaNotifica();

            Esecuzione e = new Esecuzione(context);
            e.startServizio1();

            // PARTENZA MASCHERE
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Apro db");
            db_dati_wallpaper db = new db_dati_wallpaper(context);
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Creo tabelle");
            db.CreazioneTabelle();
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Leggo impostazioni");
            boolean letto = db.LeggeImpostazioni();
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Impostazioni lette: " + letto);
            VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(letto);

            db_dati_detector dbD = new db_dati_detector(context);
            dbD.CreazioneTabelle();

            Intent iW = new Intent(context, MainWallpaper.class);
            iW.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(iW);

            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Leggo impostazioni");
            boolean lettoD = UtilityDetector.getInstance().LeggeImpostazioni(context);
            VariabiliStaticheDetector.getInstance().setLetteImpostazioni(lettoD);

            if (VariabiliStaticheStart.getInstance().isDetector() &&
                    !VariabiliStaticheDetector.getInstance().isMascheraPartita() &&
                    VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi()) {
                Intent iD = new Intent(context, MainActivityDetector.class);
                iD.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iD);

                Handler handlerTimer = new Handler();
                Runnable rTimer = new Runnable() {
                    public void run() {
                        if (VariabiliStaticheDetector.getInstance().getMainActivity() != null) {
                            InizializzaMascheraDetector id = new InizializzaMascheraDetector();
                            id.inizializzaMaschera(
                                    context,
                                    VariabiliStaticheDetector.getInstance().getMainActivity());
                        }
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }

            UtilityWallpaper.getInstance().ApreToast(context, "Wallpaper Partito");

            if (VariabiliStaticheStart.getInstance().isDetector() &&
                    !VariabiliStaticheDetector.getInstance().isMascheraPartita() &&
                    VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi()) {
                Notification notificaDetector = GestioneNotificheDetector.getInstance().StartNotifica(context);
                if (notificaDetector != null) {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Notifica instanziata");

                    UtilityDetector.getInstance().ContaFiles(context);

                    UtilityWallpaper.getInstance().ApreToast(context, "Detector Partito");
                }
            }
            // PARTENZA MASCHERE
        } else {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Notifica " + VariabiliStaticheWallpaper.channelName + " nulla");
            Toast.makeText(this, "Notifica " + VariabiliStaticheWallpaper.channelName + " nulla", Toast.LENGTH_SHORT).show();
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

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "On Destroy");

        if (wl != null) {
            wl.release();
        }

        if (VariabiliStaticheWallpaper.getInstance().isSbragaTutto()) {
            ChiudeTutto();
        }
    }
}