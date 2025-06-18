package com.looigi.wallpaperchanger2.classeGps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.LocationRequest;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaPuntiSpegnimento;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class GestioneGPS extends Service {
    private static final String NomeMaschera = "Gestione_GPS";
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Context context;
    private HandlerThread handlerThread;
    private Looper looper;
    private HandlerThread handlerThreadGps;
    private Looper looperGps;
    private long lastLocationTimestamp;
    private Location vecchiaLocation;

    // private boolean ultimoNull = false;
    // private HandlerThread handlerThreadAccensione;
    private Handler handlerGps;
    private Runnable rGps;
    // private long ultimoTSLocation = -1;
    // private boolean statoAttivo = true;
    // private HandlerThread handlerThread1;
    // private Handler handler1;
    // private Runnable r1;
    // private boolean wifi;
    // private boolean nonScriverePunti = false;
    private CalcoloVelocita cv;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        VariabiliStaticheGPS.getInstance().setContext(this);

        cv = new CalcoloVelocita();
        VariabiliStaticheGPS.getInstance().setGpsAttivo(false);
        VariabiliStaticheGPS.getInstance().setBloccatoDaTasto(true);

        // VariabiliStaticheGPS.getInstance().setNonScriverePunti(false);

        /* if (intent != null && intent.getAction() != null && intent.getAction().equals("CONTROLLO_ATTIVAZIONE")) {
            ControlloAccSpegn();
        } else {
            if (intent != null && intent.getAction() != null && intent.getAction().equals("BLOCCO_GPS")) {
                BloccaGPS("NOTIFICA");
            } else {
                if (intent != null && intent.getAction() != null && intent.getAction().equals("ABILITA_GPS")) {
                    AbilitaGPS("NOTIFICA");
                } else { */

                    AbilitaTimer(context);

                    ControlloAccSpegn("OnStartCommand Classe GestioneGPS");

                    // UtilitiesGlobali.getInstance().ApreToast(context, "GPS Partito");

                    Notification notificaGPS = GestioneNotificaGPS.getInstance().StartNotifica(this);
                    startForeground(VariabiliStaticheGPS.NOTIFICATION_CHANNEL_ID, notificaGPS);

                    // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                // }
            // }
        // }

        UtilitiesGlobali.getInstance().ImpostaServizioGPS(context,
                "CONTROLLO_ATTIVAZIONE",
                "GestioneGPS");

        // return START_REDELIVER_INTENT ;
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "Gestione GPS. ON CREATE");

        super.onCreate();
    }

    @Override
    public void onDestroy() {
        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "Gestione GPS. ON DESTROY!!!");

        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "Gestione GPS. ON LOW MEMORY!!!");

        super.onLowMemory();
    }

    @Override
    public void onTimeout(int startId) {
        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "Gestione GPS. ON TIMEOUT!!!");

        super.onTimeout(startId);
    }

    /* @Override
    public void onDestroy() {
        super.onDestroy();

        BloccaGPS("On Destroy");
        ChiudeMaschera();
    }

    public LocationManager LocationManagerAttivo() {
        return locationManager;
    }

    public void NullaLocationManager() {
        locationManager.removeUpdates(locationListenerGPS);
        locationManager = null;
    } */

    public void BloccaGPS(String daDove) {
        if (!VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Blocco GPS Annullato visto che è già bloccato da " + daDove);
            return;
        }

        // context = UtilitiesGlobali.getInstance().tornaContextValido();
        if (context == null) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Blocco GPS Context nullo da " + daDove + ". Tento di riprenderlo");
            context = UtilitiesGlobali.getInstance().tornaContextValido();
            if (context == null) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Blocco GPS Annullato per context nullo da " + daDove);
                return;
            }
        }

        // statoAttivo = false;
        VariabiliStaticheGPS.getInstance().setGpsAttivo(false);
        GestioneNotificaGPS.getInstance().AggiornaNotifica();

        /* if (handlerThreadAccensione != null) {
            handlerThreadAccensione.stop();
            handlerThreadAccensione = null;
        }

        if (handlerAccensione != null && rAccensione != null) {
            handlerAccensione.removeCallbacks(rAccensione);
            handlerAccensione = null;
            rAccensione = null;
        } */

        Bitmap bmGps;
        if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
            bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite);
        } else {
            bmGps = BitmapFactory.decodeResource(context.getResources(), R.drawable.satellite_off);
        }
        if (VariabiliStaticheGPS.getInstance().getBitmapHome() != null) {
            VariabiliStaticheGPS.getInstance().getBitmapHome().setImageBitmap(bmGps);
        }

        GestioneNotificaGPS.getInstance().AggiornaNotifica();

        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Aggiornata Notifica " + daDove);

        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "GPS Bloccato da " + daDove);

        // if (VariabiliStaticheDetector.getInstance().getModalitaGps()) {
            if (fusedLocationClient != null && locationCallback != null) {
                fusedLocationClient.removeLocationUpdates(locationCallback);
                locationCallback = null;
                fusedLocationClient = null;
            }
        // } else {
            if (locationManager != null) {
                locationManager.removeUpdates(locationListenerGPS);
                locationListenerGPS = null;
                locationManager = null;
            }
        // }

        if (handlerThread != null && looper != null) {
            handlerThread.quitSafely();
            looper.quit();
            handlerThread = null;
            looper = null;
        }

        if (handlerThreadGps != null && looperGps != null) {
            handlerThreadGps.quitSafely();
            looperGps.quit();
            handlerThreadGps = null;
            looperGps = null;
        }

        cv = null;
        // db_dati_gps db = new db_dati_gps(context);
        // db.ScriveAccensioni(context);
    }

    public void BloccaTimer() {
        /* if (handler1 != null) {
            handler1.removeCallbacksAndMessages(r1);
            handler1.removeCallbacks(r1);
            handler1 = null;
            handlerThread1 = null;
            // return;
        } */
    }

    public void AbilitaTimer(Context context) {
        /* if (handler1 != null) {
            handler1.removeCallbacks(r1);
            handler1 = null;
            handlerThread1 = null;
            // return;
        }

        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Abilita Timer GPS");

        this.context = context;

        this.ControlloAccSpegn();

        handlerThread1 = new HandlerThread("background-thread_timer_gps");
        handlerThread1.start();

        int secondiAttesa = VariabiliStaticheGPS.attesaControlloGPS * 60 * 1000;

        handler1 = new Handler(handlerThread1.getLooper());
        r1 = new Runnable() {
            public void run() {
                ControlloAccSpegn();

                handler1.postDelayed(this, secondiAttesa);
            }
        };
        handler1.postDelayed(r1, secondiAttesa); */
    }

    private void ControlloOraPerAccSpegn(Calendar calendar, String disatt, String riatt) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Controllo disattivazione/attivazione:\nOra Attuale: " + hour + ":" + minute + "\n" +
                "Ora Disattivazione: " + disatt + "\n" +
                "Ora Riattivazione: " + riatt + "\nStato Attuale: " + VariabiliStaticheGPS.getInstance().isGpsAttivo());

        String[] oraDisatt = disatt.split(":");

        int hDD = Integer.parseInt(oraDisatt[0]);
        int mDD = Integer.parseInt(oraDisatt[1]);

        String[] oraRiatt = riatt.split(":");

        int hDR = Integer.parseInt(oraRiatt[0]);
        int mDR = Integer.parseInt(oraRiatt[1]);

        if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
            if ((hour >= hDD && hour < hDR) || (hour == hDD && minute >= mDD)) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Disattivo: " + hDD + ":" + mDD + " -> " + hour + ":" + minute);

                BloccaGPS("Controllo AccSpegn");
            }
        } else {
            if ((hour >= hDR && hour >= hDD) || (hour == hDR && minute >= mDR)) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Riattivo: " + hDR + ":" + mDR + " -> " + hour + ":" + minute);

                AbilitaGPS("Controllo Ora per AccSpegn");
            }
        }
    }

    public void ControlloAccSpegn(String daDove) {
        /* if (context == null) {
            context = UtilitiesGlobali.getInstance().tornaContextValido();
            if (context == null) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Esco dal controllo GPS per context nullo");
                return;
            }
        } else {
            context = ctx;
        } */

        if (VariabiliStaticheGPS.getInstance().isBloccatoDaTasto()) {
            if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Disattivo per blocco da tasto. Da " + daDove);

                BloccaGPS("Controllo Acc Spegn 3. Da " + daDove);
            } else {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Esco dal controllo perché bloccato da tasto. Da " + daDove);
            }

            return;
        } else {
            if (!VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Attivo. Da " + daDove);

                AbilitaGPS("Controllo Acc Spegn 3. Da " + daDove);
            } else {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Esco dal controllo perché gps già attivo. Da " + daDove);
            }
        }

        // wifi = UtilitiesGlobali.getInstance().checkWifiOnAndConnected();
        // boolean wifi = VariabiliStaticheStart.getInstance().isCeWifi();

        /* UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Controllo disattivazione/attivazione. Controllo wifi connesso: " + wifi); */

        /* boolean wifi = VariabiliStaticheStart.getInstance().isCeWifi();
        if (!VariabiliStaticheGPS.getInstance().isBloccoPerWifi()) {
            wifi = false;
        }

        // METTO PER DEBUG
        wifi = false;
        // METTO PER DEBUG */

        /* if (wifi) {
            if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Disattivo. " +
                                "Blocco WIFI: " + VariabiliStaticheGPS.getInstance().isBloccoPerWifi() + ". Da " + daDove);

                BloccaGPS("Controllo Acc Spegn 2. Da " + daDove);
            } /* else {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Non faccio nulla. Già bloccato");
            } * /
        } else { * /
            if (!VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Riattivo. "); // +
                             //   "Blocco WIFI: " + VariabiliStaticheGPS.getInstance().isBloccoPerWifi() + ". Da " + daDove);

                AbilitaGPS("Controllo Acc/Spegn. Da " + daDove);
            // } else {
                /* UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Non faccio nulla. Già attivo");
            }
            } */
        // }

        /*
        StrutturaAccensioneGPS s = VariabiliStaticheGPS.getInstance().getAccensioneGPS();
        if (s != null) {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            boolean accendiComunque = false;

            switch (day) {
                case Calendar.SUNDAY:
                    if (s.isSpegnimentoAttivoDomenica()) {
                        ControlloOraPerAccSpegn(
                                calendar,
                                s.getOraDisattivazioneDomenica(),
                                s.getOraRiattivazioneDomenica()
                        );
                    } else {
                        accendiComunque = true;
                    }
                    break;
                case Calendar.SATURDAY:
                    if (s.isSpegnimentoAttivoSabato()) {
                        ControlloOraPerAccSpegn(
                                calendar,
                                s.getOraDisattivazioneSabato(),
                                s.getOraRiattivazioneSabato()
                        );
                    } else {
                        accendiComunque = true;
                    }
                    break;
                case Calendar.MONDAY:
                    if (s.isSpegnimentoAttivoLunedi()) {
                        ControlloOraPerAccSpegn(
                                calendar,
                                s.getOraDisattivazioneLunedi(),
                                s.getOraRiattivazioneLunedi()
                        );
                    } else {
                        accendiComunque = true;
                    }
                    break;
                case Calendar.TUESDAY:
                    if (s.isSpegnimentoAttivoMartedi()) {
                        ControlloOraPerAccSpegn(
                                calendar,
                                s.getOraDisattivazioneMartedi(),
                                s.getOraRiattivazioneMartedi()
                        );
                    } else {
                        accendiComunque = true;
                    }
                    break;
                case Calendar.WEDNESDAY:
                    if (s.isSpegnimentoAttivoMercoledi()) {
                        ControlloOraPerAccSpegn(
                                calendar,
                                s.getOraDisattivazioneMercoledi(),
                                s.getOraRiattivazioneMercoledi()
                        );
                    } else {
                        accendiComunque = true;
                    }
                    break;
                case Calendar.THURSDAY:
                    if (s.isSpegnimentoAttivoGiovedi()) {
                        ControlloOraPerAccSpegn(
                                calendar,
                                s.getOraDisattivazioneGiovedi(),
                                s.getOraRiattivazioneGiovedi()
                        );
                    } else {
                        accendiComunque = true;
                    }
                    break;
                case Calendar.FRIDAY:
                    if (s.isSpegnimentoAttivoVenerdi()) {
                        ControlloOraPerAccSpegn(
                                calendar,
                                s.getOraDisattivazioneVenerdi(),
                                s.getOraRiattivazioneVenerdi()
                        );
                    } else {
                        accendiComunque = true;
                    }
                    break;
            }

            if (accendiComunque) {
                if (!VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                    UtilityGPS.getInstance().ScriveLog(
                            context,
                            NomeMaschera,
                            "Controllo disattivazione/attivazione. Attivo perché disattivato e siamo fuori giorno impostato");

                    AbilitaGPS(context);
                }
            }
        }
        */
    }

    private void abilitaGPSModalitaNuova() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(VariabiliStaticheStart.getInstance().getMainActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            locationCallback = null;
            fusedLocationClient = null;
        }

        // Inizializzazione
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "Milli: " + VariabiliStaticheDetector.getInstance().getGpsMs());
        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "Metri: " + VariabiliStaticheDetector.getInstance().getGpsMeters());

        cv = new CalcoloVelocita();

        // Imposta priorità dinamica
        int priorita = VariabiliStaticheDetector.getInstance().isGpsPreciso()
                ? Priority.PRIORITY_HIGH_ACCURACY
                : Priority.PRIORITY_BALANCED_POWER_ACCURACY;

        // Crea LocationRequest robusto
        LocationRequest locationRequest = new LocationRequest.Builder(
                VariabiliStaticheDetector.getInstance().getGpsMs()
        )
                .setMinUpdateIntervalMillis(5000) // minimo 5 secondi tra update
                .setMinUpdateDistanceMeters(VariabiliStaticheDetector.getInstance().getGpsMeters())
                .setMaxUpdateDelayMillis(10000) // forza update anche se il sistema "raggruppa"
                .setPriority(priorita)
                .build();

        // Callback location
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                lastLocationTimestamp = System.currentTimeMillis();

                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        // Filtro opzionale su distanza, se necessario
                        if (vecchiaLocation == null || location.distanceTo(vecchiaLocation) >= VariabiliStaticheDetector.getInstance().getGpsMeters()) {
                            vecchiaLocation = location;

                            funzioneDiScritturaPosizioni(location);
                        }
                    }
                }
            }
        };

        // Pulisce thread vecchio (se esiste)
        if (handlerThread != null) {
            handlerThread.quitSafely();
            handlerThread = null;
        }
        if (looper != null) {
            looper.quit();
            looper = null;
        }

        // Crea nuovo HandlerThread
        handlerThread = new HandlerThread("LocationThreadGPS");
        handlerThread.start();
        looper = handlerThread.getLooper();

        // Avvia aggiornamenti GPS
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, looper);

        /* lastLocationTimestamp = System.currentTimeMillis();

        int finalPriorita = priorita;

        if (handlerThreadGps != null && looperGps != null) {
            handlerThreadGps.quitSafely();
            looperGps.quit();
            handlerThreadGps = null;
            looperGps = null;
        }

        // HANDLER PER PUNTI RITARDATI
        handlerThreadGps = new HandlerThread("LocationThreadPerGPSBloccato");
        handlerThreadGps.start();
        looperGps = handlerThreadGps.getLooper();

        handlerGps = new Handler(looperGps);
        rGps = new Runnable() {
            @SuppressLint("MissingPermission")
            public void run() {
                long now = System.currentTimeMillis();

                UtilityGPS.getInstance().ScriveLog(context, "ThreadBlocco",
                        "Check Secondi: " + now + " - Differenza: " + (now - lastLocationTimestamp));

                if (now - lastLocationTimestamp > VariabiliStaticheDetector.getInstance().getGpsMs()) {
                    fusedLocationClient.getCurrentLocation(finalPriorita, null)
                            .addOnSuccessListener(location -> {
                                if (location != null) {
                                    lastLocationTimestamp = System.currentTimeMillis();

                                    float distanzaInMetri = 999;
                                    if (vecchiaLocation != null) {
                                        distanzaInMetri = location.distanceTo(vecchiaLocation);
                                    }
                                    if (distanzaInMetri >= (VariabiliStaticheDetector.getInstance().getGpsMeters() * 1F)) {
                                        UtilityGPS.getInstance().ScriveLog(context, "ThreadBlocco",
                                                "-----> Acquisito");

                                        vecchiaLocation = location;
                                        funzioneDiScritturaPosizioni(location);
                                    }
                                }
                            });
                }

                handlerGps.postDelayed(this, VariabiliStaticheDetector.getInstance().getGpsMs());
            }
        };
        handlerGps.postDelayed(rGps, VariabiliStaticheDetector.getInstance().getGpsMs());
        // HANDLER PER PUNTI RITARDATI

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                lastLocationTimestamp = System.currentTimeMillis();

                for (Location location : locationResult.getLocations()) {
                    // funzioneDiScritturaPosizioni(Objects.requireNonNull(location.getLastLocation()));
                    funzioneDiScritturaPosizioni(location);
                }
            }
        };

        if (handlerThread != null && looper != null) {
            handlerThread.interrupt();
            handlerThread.quitSafely();
            looper.quit();
            handlerThread = null;
            looper = null;
        }
        handlerThread = new HandlerThread("LocationThreadPerGPS");
        handlerThread.start();
        looper = handlerThread.getLooper();

        // Avvia gli aggiornamenti della posizione
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, looper);
        */

        VariabiliStaticheGPS.getInstance().setGpsAttivo(true);

        GestioneNotificaGPS.getInstance().AggiornaNotifica();
    }

    public void AbilitaGPS(String daDove) {
        if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Abilita GPS. Esco per gps già attivo. Da " + daDove);
            return;
        }

        // context = UtilitiesGlobali.getInstance().tornaContextValido();
        if (context == null) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Abilita GPS. Context nullo da " + daDove + ". Tento di riprenderlo");
            context = UtilitiesGlobali.getInstance().tornaContextValido();
            if (context == null) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Abilita GPS. Esco per context nullo da " + daDove);
                return;
            }
        }

        if (VariabiliStaticheDetector.getInstance().getModalitaGps()) {
            abilitaGPSModalitaNuova();
        } else {
            if (locationManager != null) {
                locationManager.removeUpdates(locationListenerGPS);
                locationManager = null;
                // UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                //         "Abilita GPS. Esco per location manager non nullo da " + daDove);
                // return;
            }

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Abilita GPS da " + daDove);

            VariabiliStaticheGPS.getInstance().setGpsAttivo(true);
            locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

            GestioneNotificaGPS.getInstance().AggiornaNotifica();
            // GestioneNotificheTasti.getInstance().AggiornaNotifica();

            // ultimoTSLocation = new Date().getTime();
            // VariabiliStaticheGPS.getInstance().setCoordinateAttuali(null);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Abilita GPS. ESCO PER PERMESSI NULLI");
                return;
            }

            String provider;

            if (VariabiliStaticheDetector.getInstance().isGpsPreciso()) {
                provider = LocationManager.GPS_PROVIDER;

                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Provider Preciso");
            } else {
                provider = LocationManager.NETWORK_PROVIDER;

                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Provider Network");
            }

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Milli: " + VariabiliStaticheDetector.getInstance().getGpsMs());
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Metri: " + VariabiliStaticheDetector.getInstance().getGpsMeters());

            locationManager.requestLocationUpdates(
                    provider,
                    VariabiliStaticheDetector.getInstance().getGpsMs(),
                    VariabiliStaticheDetector.getInstance().getGpsMeters(),
                    locationListenerGPS);

            if (!locationManager.isProviderEnabled(provider)) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Attiva Location");
                alertDialog.setMessage("L'impostazione delle tue posizioni non è abilitata. " +
                        "Si prega di abilitarlo nel menu delle impostazioni.");
                alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        context.startActivity(intent);
                    }
                });
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.show();
            } /* else {
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
            alertDialog.setTitle("Conferma Location");
            alertDialog.setMessage("Location abilitata");
            alertDialog.setNegativeButton("Back to interface",new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        } */

            // TOLTO PER TEST
            // ControlloAccSpegn(context);
        }

        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "GPS Abilitato con modalità Fused Locator " + VariabiliStaticheDetector.getInstance().getModalitaGps());
    }

    private void funzioneDiScritturaPosizioni(Location location) {
        if (!VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
            return;
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        double altitude = location.getAltitude();

        if (cv == null) { cv = new CalcoloVelocita(); }
        float speed = location.getSpeed(); // cv.calculateRobustSpeed(location);

        /* float speed = location.getSpeed();
        float velocityInMps = speed;
        speed = velocityInMps * 3.6F; */

        float accuracy = location.getAccuracy();
        float direzione = location.hasBearing() ? location.getBearing() : 0.0f;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = sdfD.format(calendar.getTime());

        SimpleDateFormat sdfO = new SimpleDateFormat("HH:mm:ss");
        String currentHour = sdfO.format(calendar.getTime());

        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "\n\nLocation changed. Ingresso: " + location.getLatitude() + ", " + location.getLongitude() + "\n" +
                        "Accuracy: " + location.getAccuracy() + "\n" +
                        "Wifi: " + VariabiliStaticheStart.getInstance().isCeWifi() + "\n" +
                        "Abilitato: " + VariabiliStaticheGPS.getInstance().isGpsAttivo() + "\n" +
                        "Ora: " + currentHour
        );

        // ultimoTSLocation = new Date().getTime();

        if (VariabiliStaticheGPS.getInstance().getCoordinateAttuali() == null) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Coordinate attuali nulle");

            db_dati_gps db = new db_dati_gps(context);
            StrutturaGps s = db.RitornaUltimaPosizione(currentDate);
            if (s != null) {
                VariabiliStaticheGPS.getInstance().setCoordinateAttuali(s);

                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Coordinate attuali lette: " + s.getLat() + " " + s.getLon() + " " +
                                s.getData());
            } else {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Coordinate attuali nulle sul db");
            }
            db.ChiudeDB();

            if (s != null) {
                String dataUltimoPunto = s.getOra();
                VariabiliStaticheGPS.getInstance().setUltimoDataPunto(dataUltimoPunto);
            } else {
                VariabiliStaticheGPS.getInstance().setUltimoDataPunto("--");
            }

            GestioneNotificaGPS.getInstance().AggiornaNotifica();
        }

        boolean ok = true;

        double distanza = 0;

        if (VariabiliStaticheGPS.getInstance().getCoordinateAttuali() != null) {
            StrutturaGps vecchia = VariabiliStaticheGPS.getInstance().getCoordinateAttuali();

                /* float[] results = new float[1];
                Location.distanceBetween(
                        vecchia.getLat(),
                        vecchia.getLon(),
                        location.getLatitude(),
                        location.getLongitude(),
                        results); */

            distanza = meterDistanceBetweenPoints(
                    vecchia.getLat(),
                    vecchia.getLon(),
                    location.getLatitude(),
                    location.getLongitude()
            );

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Distanza con l'ultimo punto: " + distanza);

            // if (distanza > 75) {
            //     ok = false;
            // }

                /* distanza = results[0];
                if (results[0] > 75) {
                    ok = false;
                } */
        } else {
            ok = false;

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Coordinate attuali nulle");
        }

        if (ok) {
            if (VariabiliStaticheGPS.getInstance().isAccuracyAttiva() &&
                    location.getAccuracy() > 100) {
                ok = false;

                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Skippo posizione per Accuracy elevata: " + location.getAccuracy());
            }
        }

            /* if (ok) {
                if (VariabiliStaticheGPS.getInstance().isNonScriverePunti()) {
                    ok = false;

                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                            "Blocco per Non scrivere punti attivo");
                }
            } */

        StrutturaGps s = new StrutturaGps();
        s.setLat(latitude);
        s.setLon(longitude);
        s.setData(currentDate);
        s.setOra(currentHour);
        s.setAltitude(altitude);
        s.setSpeed(speed);
        s.setAccuracy(accuracy);
        s.setDistanza((float) distanza);
        s.setWifi(VariabiliStaticheStart.getInstance().isCeWifi());
        s.setLivelloSegnale(VariabiliStaticheStart.getInstance().getLivelloSegnaleConnessione());
        s.setTipoSegnale(VariabiliStaticheStart.getInstance().getTipoConnessione());
        s.setLevel(VariabiliStaticheStart.getInstance().getLivello());
        s.setDirezione(direzione);

        VariabiliStaticheGPS.getInstance().setCoordinateAttuali(s);

        if (ok && VariabiliStaticheGPS.getInstance().isPuntiSospensioneAttivi()) {
            ok = controlloSeStiamoSuPS(location);
        }

        if (ok) {
            VariabiliStaticheGPS.getInstance().setUltimoDataPunto(currentHour);

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Location changed. SCRITTURA"
            );

                /* UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "---Punto scritto: " + VariabiliStaticheGPS.getInstance().getUltimoDataPunto() + "---\n\n"
                ); */

                /* if (!ok) {
                    if (!ultimoNull) {
                        StrutturaGps s = new StrutturaGps();
                        s.setLat(-1);
                        s.setLon(-1);
                        s.setData(currentDate);
                        s.setOra(currentHour);
                        s.setAltitude(-1);
                        s.setSpeed(-1);
                        s.setAccuracy(-1);
                        s.setDistanza(-1);

                        VariabiliStaticheGPS.getInstance().AggiungeGPS(context, s);
                        ultimoNull = true;
                    }
                } else {
                    ultimoNull = false;
                } */
            // ultimoNull = false;

            if (VariabiliStaticheGPS.getInstance().getMappa() != null) {
                VariabiliStaticheGPS.getInstance().getMappa().AggiungePosizione(s);
                /* } else {
                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                            "---MAPPA NON PRESENTE---"); */
            }

            // UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
            //         "Aggiunta posizione GPS ad array: " + s.getLat() + " " + s.getLon());

            VariabiliStaticheGPS.getInstance().AggiungeGPS(context, s);

            GestioneNotificaGPS.getInstance().AggiornaNotifica();
            // } else {
            // UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Location changed: " +
            //         location.getLatitude() + ", " + location.getLongitude() + " NON Valida");
        }
    }

    private LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // if (!controllaSpegnimentoPerGPS()) {
            //     return;
            // }
            funzioneDiScritturaPosizioni(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "\n\nProvider changed. Status: " + status + "\n\n");
        }

        @Override
        public void onProviderEnabled(String provider) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "\n\nProvider abilitato. Provider: " + provider + "\n\n");
        }

        @Override
        public void onFlushComplete(int requestCode) {
            LocationListener.super.onFlushComplete(requestCode);

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "\n\nOn Flush complete. RequestCode: " + requestCode + "\n\n");
        }

        /* @Override
        public void onLocationChanged(@NonNull List<Location> locations) {
            LocationListener.super.onLocationChanged(locations);

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "\n\nLocaltion changed\n\n");
        } */

        @Override
        public void onProviderDisabled(String provider) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "\n\nProvider disabilitato Provider: " + provider + "\n\n");
        }
    };

    private double meterDistanceBetweenPoints(double lat_a, double lng_a, double lat_b, double lng_b) {
        float pk = (float) (180.f/Math.PI);

        double a1 = lat_a / pk;
        double a2 = lng_a / pk;
        double b1 = lat_b / pk;
        double b2 = lng_b / pk;

        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);

        return 6366000 * tt;
    }

    private boolean controlloSeStiamoSuPS(Location location) {
        boolean DentroPuntoDiSpegnimento = false;
        String Nome = "";

        /* if (VariabiliStaticheGPS.getInstance().isBloccoPerWifi()) {
            if (VariabiliStaticheStart.getInstance().isCeWifi()) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Evito il controllo PS. Wifi attivo");

                return false;
            }
        } */

        for (StrutturaPuntiSpegnimento s : VariabiliStaticheGPS.getInstance().getListaPuntiDiSpegnimento()) {
            double distanza = meterDistanceBetweenPoints(
                    s.getLoc().getLatitude(),
                    s.getLoc().getLongitude(),
                    location.getLatitude(),
                    location.getLongitude()
            );

            if (distanza <= VariabiliStaticheGPS.getInstance().getDistanzaMetriPerPS()) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Entrato nella posizione " + s.getNome() + " con distanza " + distanza);

                Nome = s.getNome();
                DentroPuntoDiSpegnimento = true;
                break;
            }
        }

        if (!DentroPuntoDiSpegnimento) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Fuori da tutti i ps");

            // VariabiliStaticheGPS.getInstance().setNonScriverePunti(false);

            GestioneNotificaGPS.getInstance().AggiornaNotifica();

            return true;
        } else {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Blocco GPS per posizione " + Nome);

            // VariabiliStaticheGPS.getInstance().setNonScriverePunti(true);

            GestioneNotificaGPS.getInstance().AggiornaNotifica();

            return false;
        }

        /* if (!VariabiliStaticheGPS.getInstance().isNonScriverePunti()) {
            if (DentroPuntoDiSpegnimento) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Blocco GPS per posizione " + Nome);

                VariabiliStaticheGPS.getInstance().setNonScriverePunti(true);

                GestioneNotificaGPS.getInstance().AggiornaNotifica();

                return false;
            } else {
                // UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                //         "Non devo scrivere punti e sono dentro un ps. Tutto ok");
                return true;
            }
        } else {
            if (!DentroPuntoDiSpegnimento) {
                //  if (!VariabiliStaticheStart.getInstance().isCeWifi()) {
                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                            "Riabilito GPS per posizione non in punti selezionati");

                VariabiliStaticheGPS.getInstance().setNonScriverePunti(false);
                // }

                GestioneNotificaGPS.getInstance().AggiornaNotifica();

                return true;
            } else {
                // UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                //         "Devo scrivere punti e non sono dentro un ps. Tutto ok");
                return false;
            }
        } */
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void RefreshImpostazioni() {
        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                "Refresh impostazioni");

        if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
            BloccaGPS("Refresh Impostazioni");

            Handler handler1 = new Handler(Looper.getMainLooper());
            Runnable r1 = new Runnable() {
                public void run() {
                    AbilitaGPS("Refresh Impostazioni");
                }
            };
            handler1.postDelayed(r1, 1000);
        } else {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Non faccio nulla in quanto il gps è bloccato");
        }
    }
}
