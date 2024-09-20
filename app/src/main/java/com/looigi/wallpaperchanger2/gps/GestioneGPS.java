package com.looigi.wallpaperchanger2.gps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.health.connect.datatypes.BloodPressureRecord;
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

import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GestioneGPS {
    private static final String NomeMaschera = "GESTIONEGPS";
    private LocationManager locationManager;
    private Context context;
    private boolean ultimoNull = false;
    // private HandlerThread handlerThreadAccensione;
    // private Handler handlerAccensione;
    // private Runnable rAccensione;
    private long ultimoTSLocation = -1;
    private boolean statoAttivo = true;

    public void BloccaGPS() {
        statoAttivo = false;

        /* if (handlerThreadAccensione != null) {
            handlerThreadAccensione.stop();
            handlerThreadAccensione = null;
        }

        if (handlerAccensione != null && rAccensione != null) {
            handlerAccensione.removeCallbacks(rAccensione);
            handlerAccensione = null;
            rAccensione = null;
        } */

        if (locationManager != null && locationListenerGPS != null) {
            locationManager.removeUpdates(locationListenerGPS);
        }
    }

    public void AbilitaGPS(Context context) {
        UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Abilita GPS");

        this.context = context;
        statoAttivo = true;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        ultimoTSLocation = new Date().getTime();
        VariabiliStaticheGPS.getInstance().setCoordinateAttuali(null);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        String provider;
        if (VariabiliStaticheDetector.getInstance().isGpsPreciso()) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        locationManager.requestLocationUpdates(
                provider,
                5000,
                10,
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
    }

    /* private void controllaStatoGPS() {
        if (handlerAccensione != null) {
            return;
        }

        handlerThreadAccensione = new HandlerThread("background-thread_gps");
        handlerThreadAccensione.start();

        handlerAccensione = new Handler(handlerThreadAccensione.getLooper());
        rAccensione = new Runnable() {
            public void run() {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_WEEK);
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                if (statoAttivo) {
                    // Controllo se devo spegnere
                    long tmsAttuale = new Date().getTime();
                    long diff = tmsAttuale - ultimoTSLocation;
                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Controllo GPS. Diff: " + diff);
                    if (diff >= (60000 * VariabiliStaticheGPS.getInstance().getMinutiDiAttesaGpsPrimaDelloSpengimento())) {
                        // Passato troppo tempo... Spengo GPS
                        BloccaGPS();
                    }
                } else {

                }

                handlerAccensione.postDelayed(this, 60000);

                /* boolean acceso = false;

                switch (day) {
                    case Calendar.SUNDAY:
                    case Calendar.SATURDAY:
                        break;
                    case Calendar.MONDAY:
                    case Calendar.THURSDAY:
                    case Calendar.WEDNESDAY:
                    case Calendar.TUESDAY:
                    case Calendar.FRIDAY:
                        if (hour >= VariabiliStaticheGPS.getInstance().getOraGpsAccensione()) {
                            if (minute >= VariabiliStaticheGPS.getInstance().getMinutiGpsAccensione()) {
                                acceso = true;
                            }
                        }
                        break;
                }

                if (acceso) {
                    handlerAccensione.removeCallbacks(rAccensione);
                    handlerAccensione = null;
                    rAccensione = null;

                    UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Ripartenza GPS");

                    AbilitaGPS(context);
                } else {
                    if (handlerAccensione != null) {
                        handlerAccensione.postDelayed(this, 60000);
                    }
                } * /
            }
        };

        handlerAccensione.postDelayed(rAccensione, 60000);
    } */

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            // if (!controllaSpegnimentoPerGPS()) {
            //     return;
            // }

            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdfD.format(calendar.getTime());

            ultimoTSLocation = new Date().getTime();

            if (VariabiliStaticheGPS.getInstance().getCoordinateAttuali() == null) {
                db_dati_gps db = new db_dati_gps(context);
                VariabiliStaticheGPS.getInstance().setCoordinateAttuali(
                        db.RitornaUltimaPosizione(currentDate));
            }

            boolean ok = true;

            float distanza = 0;

            if (VariabiliStaticheGPS.getInstance().getCoordinateAttuali() != null) {
                StrutturaGps vecchia = VariabiliStaticheGPS.getInstance().getCoordinateAttuali();
                float[] results = new float[1];
                Location.distanceBetween(
                        vecchia.getLat(),
                        vecchia.getLon(),
                        location.getLatitude(),
                        location.getLongitude(),
                        results);
                distanza = results[0];
                if (results[0] > 75) {
                    // ok = false;
                }
            }

            UtilityGPS.getInstance().ScriveLog(context, NomeMaschera, "Location changed: " +
                    location.getLatitude() + ", " + location.getLongitude());

            SimpleDateFormat sdfO = new SimpleDateFormat("HH:mm:ss");
            String currentHour = sdfO.format(calendar.getTime());

            if (!ok) {
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
            }

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
            s.setDistanza(distanza);

            VariabiliStaticheGPS.getInstance().setCoordinateAttuali(s);

            if (ok) {
                VariabiliStaticheGPS.getInstance().AggiungeGPS(context, s);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
