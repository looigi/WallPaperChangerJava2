package com.looigi.wallpaperchanger2.classeStandard;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.widget.Toast;

import com.looigi.wallpaperchanger2.Segnale.ControlloSegnale2;
import com.looigi.wallpaperchanger2.classeDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classeWallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.classeWallpaper.MainWallpaper;
import com.looigi.wallpaperchanger2.classeDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classeDetector.InizializzaMascheraDetector;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeDetector.db_dati_detector;
import com.looigi.wallpaperchanger2.classeWallpaper.db_dati_wallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

public class ServizioInterno extends Service {
    private static final String NomeMaschera = "SERVIZIOINTERNO";
    private Context context;
    private ScreenReceiver mScreenReceiver;
    private PowerManager.WakeLock wl;
    private Intent intentSegnale;

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

        // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        return START_NOT_STICKY;
    }

    @SuppressLint({"ForegroundServiceType", "WakelockTimeout"})
    @Override
    public void onCreate() {
        context = this;

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "On Create");

        // CPU Attiva
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                VariabiliStaticheWallpaper.channelName);
        wl.acquire();

        // CONTROLLO SEGNALE
        intentSegnale = new Intent(this, ControlloSegnale2.class);
        startService(intentSegnale);

        // GESTIONE ACCENSIONE SCHERMO
        mScreenReceiver = new ScreenReceiver();
        IntentFilter filterSO = new IntentFilter();
        filterSO.addAction(Intent.ACTION_SCREEN_OFF);
        filterSO.addAction(Intent.ACTION_SCREEN_ON);
        filterSO.setPriority(9999);
        context.registerReceiver(mScreenReceiver, filterSO);

        Notification notificaTasti = GestioneNotificheTasti.getInstance().StartNotifica(context);
        if (notificaTasti != null) {
            // startForeground(VariabiliStaticheTasti.NOTIFICATION_CHANNEL_ID, notificaTasti);
            GestioneNotificheTasti.getInstance().AggiornaNotifica();
        }

        Notification notifica = GestioneNotificheWP.getInstance().StartNotifica(this);
        // VariabiliStatiche.getInstance().setNotifica(GestioneNotifiche.getInstance().StartNotifica(this));
        if (notifica != null) {
            startForeground(VariabiliStaticheWallpaper.NOTIFICATION_CHANNEL_ID, notifica);
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Notifica instanziata");
            GestioneNotificheWP.getInstance().AggiornaNotifica();

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
            boolean lettoD = UtilityDetector.getInstance().LeggeImpostazioni(context, "SERVIZIOINTERNO");
            VariabiliStaticheDetector.getInstance().setLetteImpostazioni(lettoD);

            if (VariabiliStaticheStart.getInstance().isDetector() &&
                    !VariabiliStaticheDetector.getInstance().isMascheraPartita() &&
                    VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi() &&
                    VariabiliStaticheDetector.getInstance().getMainActivity() != null) {
                Intent iD = new Intent(context, MainActivityDetector.class);
                iD.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iD);

                Handler handlerTimer = new Handler(Looper.getMainLooper());
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

            UtilitiesGlobali.getInstance().ApreToast(context, "Wallpaper Partito");

            if (VariabiliStaticheStart.getInstance().isDetector() &&
                    !VariabiliStaticheDetector.getInstance().isMascheraPartita() &&
                    VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi()) {

                Notification notificaDetector = GestioneNotificheDetector.getInstance().StartNotifica(context);
                if (notificaDetector != null) {
                    UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Notifica instanziata");

                    UtilityDetector.getInstance().ContaFiles(context);

                    UtilitiesGlobali.getInstance().ApreToast(context, "Detector Partito");
                }
            }

            // PARTENZA MASCHERE
        } else {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Notifica " + VariabiliStaticheWallpaper.channelName + " nulla");
            Toast.makeText(this, "Notifica " + VariabiliStaticheWallpaper.channelName + " nulla", Toast.LENGTH_SHORT).show();
        }
    }

    private void ChiudeTutto() {
        GestioneNotificheWP.getInstance().RimuoviNotifica();

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

        /* if (VariabiliStaticheStart.getInstance().getmTelephonyManager() != null) {
            VariabiliStaticheStart.getInstance().setmTelephonyManager(null);
        }

        if (mVolumePressed != null) {
            getApplicationContext().getContentResolver().unregisterContentObserver(mVolumePressed);
            mVolumePressed = null;
        }

        if (mAudioManagerInterno != null) {
            // unregisterReceiver(volPressed);
            mAudioManagerInterno.unregisterMediaButtonEventReceiver(mReceiverComponentInterno);
            mReceiverComponentInterno = null;
        } */

        if (intentSegnale != null) {
            context.stopService(intentSegnale);
        }

        if (mScreenReceiver != null) {
            context.unregisterReceiver(mScreenReceiver);
        }

        if (wl != null) {
            wl.release();
        }

        if (VariabiliStaticheWallpaper.getInstance().isSbragaTutto()) {
            ChiudeTutto();
        }
    }

    /* public static class VolumePressed extends BroadcastReceiver {
        private static final String NomeMaschera = "VOLUMEPRESSED";

        public VolumePressed() {
            super();

            UtilityWallpaper.getInstance().ScriveLog(
                    VariabiliStaticheStart.getInstance().getMainActivity(),
                    NomeMaschera,
                    "Instanziamento");
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String intentAction = intent.getAction();
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Azione: " + intentAction);

            if (!Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
                return;
            }

            KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) {
                return;
            }

            int action = event.getAction();
            if (action == KeyEvent.ACTION_DOWN) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "ACTION DOWN");

                Toast.makeText(context, "BUTTON PRESSED!", Toast.LENGTH_SHORT).show();
            }

            abortBroadcast();
        }
    } */
}