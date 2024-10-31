package com.looigi.wallpaperchanger2.classeGps;

import android.Manifest;
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
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaPuntiSpegnimento;
import com.looigi.wallpaperchanger2.classeWallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GestioneGPS {
    private static final String NomeMaschera = "Gestione_GPS";
    private LocationManager locationManager;
    private Context context;
    // private boolean ultimoNull = false;
    // private HandlerThread handlerThreadAccensione;
    // private Handler handlerAccensione;
    // private Runnable rAccensione;
    // private long ultimoTSLocation = -1;
    // private boolean statoAttivo = true;
    private HandlerThread handlerThread1;
    private Handler handler1;
    private Runnable r1;
    private boolean wifi;
    // private boolean nonScriverePunti = false;

    public void BloccaGPS(String daDove) {
        context = UtilitiesGlobali.getInstance().tornaContextValido();
        if (context == null) {
            return;
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

        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "GPS Bloccato da " + daDove);

        if (locationManager != null && locationListenerGPS != null) {
            locationManager.removeUpdates(locationListenerGPS);
        }

        // db_dati_gps db = new db_dati_gps(context);
        // db.ScriveAccensioni(context);

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
    }

    public void ChiudeMaschera() {
        if (handler1 != null) {
            handler1.removeCallbacksAndMessages(r1);
            handler1.removeCallbacks(r1);
            handler1 = null;
            handlerThread1 = null;
            // return;
        }
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

                AbilitaGPS();
            }
        }
    }

    public void ControlloAccSpegn(Context ctx) {
        Context context;

        if (ctx == null) {
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
        }

        if (VariabiliStaticheGPS.getInstance().isBloccatoDaTasto()) {
            UtilityGPS.getInstance().ScriveLog(
                    context,
                    NomeMaschera,
                    "Controllo disattivazione/attivazione. Esco dal controllo perché bloccato da tasto");
            return;
        }

        // wifi = UtilitiesGlobali.getInstance().checkWifiOnAndConnected();
        wifi = VariabiliStaticheStart.getInstance().isCeWifi();

        /* UtilityGPS.getInstance().ScriveLog(
                context,
                NomeMaschera,
                "Controllo disattivazione/attivazione. Controllo wifi connesso: " + wifi); */

        if (wifi) {
            if (VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Disattivo per WiFi attivo");

                BloccaGPS("Controllo Acc Spegn 2");
            }
        } else {
            if (!VariabiliStaticheGPS.getInstance().isGpsAttivo()) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Controllo disattivazione/attivazione. Riattivo per WiFi non attivo");

                AbilitaGPS();
            }
        }

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

    public void AbilitaGPS() {
        context = UtilitiesGlobali.getInstance().tornaContextValido();
        if (context == null) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Abilita GPS. Esco per context nullo");
            return;
        }

        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Abilita GPS");

        VariabiliStaticheGPS.getInstance().setGpsAttivo(true);
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        GestioneNotificaGPS.getInstance().AggiornaNotifica();
        // GestioneNotificheTasti.getInstance().AggiornaNotifica();

        // ultimoTSLocation = new Date().getTime();
        VariabiliStaticheGPS.getInstance().setCoordinateAttuali(null);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Abilita GPS. ESCO PER PERMESSI NULLI");
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

        locationManager.requestLocationUpdates(
                provider,
                VariabiliStaticheDetector.getInstance().getGpsMs(),
                VariabiliStaticheDetector.getInstance().getGpsMeters(),
                locationListenerGPS);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
            alertDialog.setTitle("Attiva Location");
            alertDialog.setMessage("L'impostazione delle tue posizioni non è abilitata. Si prega di abilitarlo nel menu delle impostazioni.");
            alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
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

        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "GPS Abilitato");
    }

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // if (!controllaSpegnimentoPerGPS()) {
            //     return;
            // }

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdfD.format(calendar.getTime());

            // ultimoTSLocation = new Date().getTime();

            if (VariabiliStaticheGPS.getInstance().getCoordinateAttuali() == null) {
                db_dati_gps db = new db_dati_gps(context);
                VariabiliStaticheGPS.getInstance().setCoordinateAttuali(
                        db.RitornaUltimaPosizione(currentDate));
                db.ChiudeDB();
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

                // if (distanza > 75) {
                //     ok = false;
                // }

                if (!VariabiliStaticheStart.getInstance().isCeWifi()) {
                    controlloPuntiImpostatiPerSblocco(location);
                }

                /* distanza = results[0];
                if (results[0] > 75) {
                    ok = false;
                } */
            }

            if (ok) {
                if (location.getAccuracy() > 50) {
                    ok = false;

                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                            "Skippo posizione per Accuracy elevata: " + location.getAccuracy());
                }
            }

            if (ok) {
                if (VariabiliStaticheGPS.getInstance().isNonScriverePunti()) {
                    ok = false;
                }
            }

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Location changed: " +
                    location.getLatitude() + ", " + location.getLongitude() + ". Wifi: " +
                    VariabiliStaticheStart.getInstance().isCeWifi() + ". Abilitato: " +
                    VariabiliStaticheGPS.getInstance().isGpsAttivo() + ". NON Scrittura: " + VariabiliStaticheGPS.getInstance().isNonScriverePunti() +
                    ". OK: " + ok);

            SimpleDateFormat sdfO = new SimpleDateFormat("HH:mm:ss");
            String currentHour = sdfO.format(calendar.getTime());

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float accuracy = location.getAccuracy();

            StrutturaGps s = new StrutturaGps();
            s.setLat(latitude);
            s.setLon(longitude);
            s.setData(currentDate);
            s.setOra(currentHour);
            s.setAltitude(altitude);
            s.setSpeed(speed);
            s.setAccuracy(accuracy);
            s.setDistanza((float) distanza);
            s.setWifi(wifi);
            s.setLivelloSegnale(VariabiliStaticheStart.getInstance().getLivelloSegnaleConnessione());
            s.setTipoSegnale(VariabiliStaticheStart.getInstance().getTipoConnessione());
            s.setLevel(VariabiliStaticheStart.getInstance().getLivello());

            VariabiliStaticheGPS.getInstance().setCoordinateAttuali(s);

            if (ok) {
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

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Provider abilitato");
        }

        @Override
        public void onProviderDisabled(String provider) {
            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                    "Provider disabilitato");
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

    private void controlloPuntiImpostatiPerSblocco(Location location) {
        boolean DentroPuntoDiSpegnimento = false;
        String Nome = "";

        for (StrutturaPuntiSpegnimento s : VariabiliStaticheGPS.getInstance().getListaPuntiDiSpegnimento()) {
            double distanza = meterDistanceBetweenPoints(
                    s.getLoc().getLatitude(),
                    s.getLoc().getLongitude(),
                    location.getLatitude(),
                    location.getLongitude()
            );

            if (distanza <= VariabiliStaticheGPS.getInstance().getDistanzaMetriPerPS()) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Entrato nella posizione " + s.getNome());

                Nome = s.getNome();
                DentroPuntoDiSpegnimento = true;
                break;
            }
        }

        if (!VariabiliStaticheGPS.getInstance().isNonScriverePunti()) {
            if (DentroPuntoDiSpegnimento) {
                UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                        "Blocco GPS per posizione " + Nome);

                VariabiliStaticheGPS.getInstance().setNonScriverePunti(true);

                GestioneNotificaGPS.getInstance().AggiornaNotifica();
            }
        } else {
            if (!DentroPuntoDiSpegnimento) {
                //  if (!VariabiliStaticheStart.getInstance().isCeWifi()) {
                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera,
                            "Riabilito GPS per posizione non in punti selezionati e senza wifi");

                VariabiliStaticheGPS.getInstance().setNonScriverePunti(false);
                // }

                GestioneNotificaGPS.getInstance().AggiornaNotifica();
            }
        }
    }
}
