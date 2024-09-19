package com.looigi.wallpaperchanger2.gps;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GestioneGPS {
    private LocationManager locationManager;
    private Context context;
    private boolean ultimoNull = false;

    public void AbilitaGPS(Context context) {
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                2000,
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

    LocationListener locationListenerGPS = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdfD = new SimpleDateFormat("dd/MM/yyyy");
            String currentDate = sdfD.format(calendar.getTime());

            if (VariabiliStaticheGPS.getInstance().getCoordinateAttuali() == null) {
                db_dati_gps db = new db_dati_gps(context);
                VariabiliStaticheGPS.getInstance().setCoordinateAttuali(
                        db.RitornaUltimaPosizione(currentDate));
            }

            boolean ok = true;

            if (VariabiliStaticheGPS.getInstance().getCoordinateAttuali() != null) {
                StrutturaGps vecchia = VariabiliStaticheGPS.getInstance().getCoordinateAttuali();
                float[] results = new float[1];
                Location.distanceBetween(
                        vecchia.getLat(),
                        vecchia.getLon(),
                        location.getLatitude(),
                        location.getLongitude(),
                        results);
                if (results[0] > 75) {
                    ok = false;
                }
            }

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
