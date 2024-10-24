package com.looigi.wallpaperchanger2.classeAvvio;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.looigi.wallpaperchanger2.Segnale.ControlloSegnale2;
import com.looigi.wallpaperchanger2.classeDetector.MainActivityDetector;
import com.looigi.wallpaperchanger2.classeGps.GestioneGPS;
import com.looigi.wallpaperchanger2.classeGps.ServizioDiAvvioGPS;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classeOnomastici.MainOnomastici;
import com.looigi.wallpaperchanger2.classePlayer.cuffie.GestioneTastiCuffieNuovo;
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
import com.looigi.wallpaperchanger2.utilities.ScreenReceiver;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.Date;

// implements SensorEventListener2
public class ServizioInterno extends Service {
    private static final String NomeMaschera = "Servizio_Interno";
    private Context context;
    private ScreenReceiver mScreenReceiver;
    private PowerManager.WakeLock wl;
    private Intent intentSegnale;
    private Intent intentCuffie;

    /* private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float lastAcc = 0.0f;
    private float acceleration = 0.0f;
    private float totAcc = 0.0f;
    private boolean onEvent = false; */

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

        return START_STICKY;
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

        // GESTIONE TASTI CUFFIE
        intentCuffie = new Intent(this, GestioneTastiCuffieNuovo.class);
        startService(intentCuffie);

        // SENSORI DI MOVIMENTO
        /* mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastAcc=SensorManager.GRAVITY_EARTH;
        acceleration=SensorManager.GRAVITY_EARTH;
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL); */

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
            // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Apro db");
            db_dati_wallpaper db = new db_dati_wallpaper(context);
            // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Creo tabelle");
            // db.CreazioneTabelle();
            // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Leggo impostazioni");
            // boolean letto = db.LeggeImpostazioni();
            // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Impostazioni lette: " + letto);
            // VariabiliStaticheWallpaper.getInstance().setLetteImpostazioni(letto);

            db_dati_detector dbD = new db_dati_detector(context);
            dbD.CreazioneTabelle();

            Intent iW = new Intent(context, MainWallpaper.class);
            iW.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(iW);

            // UtilityDetector.getInstance().ScriveLog(context, NomeMaschera,"Leggo impostazioni");
            // boolean lettoD = UtilityDetector.getInstance().LeggeImpostazioni(context, "SERVIZIOINTERNO");
            // VariabiliStaticheDetector.getInstance().setLetteImpostazioni(lettoD);

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

            if (VariabiliStaticheStart.getInstance().isDetector() &&
                    !VariabiliStaticheDetector.getInstance().isMascheraPartita() &&
                    VariabiliStaticheWallpaper.getInstance().isCiSonoPermessi()) {
                // db_dati_gps db2 = new db_dati_gps(context);
                // db2.CaricaAccensioni(context);

                // VariabiliStaticheGPS.getInstance().setGpsAttivo(true);

                /* GestioneGPS g = new GestioneGPS();
                VariabiliStaticheGPS.getInstance().setGestioneGPS(g);
                g.AbilitaTimer(context);
                g.AbilitaGPS(); */

                VariabiliStaticheStart.getInstance().setServizioForegroundGPS(new Intent(this, ServizioDiAvvioGPS.class));
                startForegroundService(VariabiliStaticheStart.getInstance().getServizioForegroundGPS());
            }

            Intent iO = new Intent(context, MainOnomastici.class);
            iO.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(iO);

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

        if (VariabiliStaticheStart.getInstance().getServizioForegroundGPS() != null) {
            stopService(VariabiliStaticheStart.getInstance().getServizioForegroundGPS());
            VariabiliStaticheStart.getInstance().setServizioForegroundGPS(null);
        }

        if (intentCuffie != null) {
            context.stopService(intentCuffie);
        }

        if (intentSegnale != null) {
            context.stopService(intentSegnale);
        }

        if (mScreenReceiver != null) {
            context.unregisterReceiver(mScreenReceiver);
        }

        // if (mSensorManager != null) {
        //     mSensorManager.unregisterListener(this);
        // }

        if (wl != null) {
            wl.release();
        }

        if (VariabiliStaticheWallpaper.getInstance().isSbragaTutto()) {
            ChiudeTutto();
        }
    }

    // SENSORI
    /* private long ultimoSpostamento = -1;
    private long spostamentoAttuale = -1;
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;
    private int conta = 0;
    private boolean timerAttivo = false;

    @Override
    public void onFlushCompleted(Sensor sensor) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (VariabiliStaticheStart.getInstance().isCeWifi() ||
            VariabiliStaticheGPS.getInstance().isBloccatoDaTasto()) {
            return;
        }

        // if (!onEvent) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            acceleration = x*x+y*y+z*z;
            float diff = acceleration - lastAcc;
            totAcc = diff*acceleration;

            if (totAcc>3000) {
                spostamentoAttuale = new Date().getTime();

                // Prende la data dell'ultimo spostamento
                if (!timerAttivo) {
                    // Abbiamo un timer fermo. Controllo se devo accendere
                    if (spostamentoAttuale - ultimoSpostamento < 3000) {
                        UtilityWallpaper.getInstance().ScriveLog(context, "Sensore",
                                "Movimento: " + totAcc + ". Timer Attivo: " + timerAttivo);

                        // L'ultimo spostamento è inferiore a 1 secondo fa
                        conta++;

                        UtilityWallpaper.getInstance().ScriveLog(context, "Sensore",
                                "Incremento movimento per timer non attivo: " + conta);

                        if (conta >= 5) {
                            // Ci sono stati spostamenti continui negli ultimi 15 secondi. Abilito il GPS
                            // se non è attivo
                            conta = 0;

                            UtilityWallpaper.getInstance().ScriveLog(context, "Sensore",
                                    "Arrivato a 15 secondi continui. Attivo GPS e timer");

                            VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS();

                            // Attivo il timer per vedere se ci siamo fermati
                            AttivaTimerMovimento();
                        }
                    } else {
                        UtilityWallpaper.getInstance().ScriveLog(context, "Sensore",
                                "Resetto contatore. Spostamenti da più di tre secondi");

                        // L'ultimo spostamento è superiore a 3 secondi fa. Resetto il contatore
                        conta = 0;
                    }
                }
                ultimoSpostamento = spostamentoAttuale;
            }

            lastAcc = acceleration;
        // }
    }

    private void DisattivaTimerMovimento() {
        timerAttivo = false;
        conta = 0;

        if (handler != null && r != null && handlerThread != null) {
            handlerThread.quit();

            handler.removeCallbacksAndMessages(null);

            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    }

    private void AttivaTimerMovimento() {
        if (handlerThread != null || handler != null || r != null) {
            return;
        }

        timerAttivo = true;
        conta = 0;

        UtilityWallpaper.getInstance().ScriveLog(context, "Sensore", "Attivo timer");

        handlerThread = new HandlerThread("background-thread_Sensore_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                long diff = spostamentoAttuale - ultimoSpostamento;

                UtilityWallpaper.getInstance().ScriveLog(context, "Sensore",
                        "Controllo ultimo movimento: " + diff);

                boolean ok = true;
                // Siamo in movimento. Controllo l'ultimo spostamento
                if (diff >= 10000) {
                    conta++;

                    UtilityWallpaper.getInstance().ScriveLog(context, "Sensore",
                            "Incremento movimento: " + conta);

                    if (conta >= 3) {
                        ok = false;

                        UtilityWallpaper.getInstance().ScriveLog(context, "Sensore",
                                "Ultimo movimento superiore a 30 secondi. Blocco GPS");

                        // La differenza fra l'ultimo spostamento e l'attuale è superiore a 10 secondi * 3 volte.
                        // Vuol dire che non ci siamo mossi dall'ultimo controllo.
                        // Disattivo il timer

                        VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("SENSORE");
                        DisattivaTimerMovimento();
                    } else {

                    }
                } else {
                    UtilityWallpaper.getInstance().ScriveLog(context, "Sensore",
                            "Tempo inferiore a 10 secondi. Azzero contatore");

                    ultimoSpostamento = spostamentoAttuale;
                }

                if (ok) {
                    if (handler != null) {
                        handler.postDelayed(this, 10000);
                    }
                }
            }
        };
        handler.postDelayed(r, 10000);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    } */
}