package com.looigi.wallpaperchanger2.gps;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class VariabiliStaticheGPS {
    private static VariabiliStaticheGPS instance = null;
    private db_dati_gps db;

    private VariabiliStaticheGPS() {
    }

    public static VariabiliStaticheGPS getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheGPS();
        }

        return instance;
    }

    private StrutturaGps CoordinateAttuali;
    private GestioneGPS gestioneGPS;
    private boolean gpsAttivo = true;

    public boolean isGpsAttivo() {
        return gpsAttivo;
    }

    public void setGpsAttivo(boolean gpsAttivo) {
        this.gpsAttivo = gpsAttivo;
    }

    public GestioneGPS getGestioneGPS() {
        return gestioneGPS;
    }

    public void setGestioneGPS(GestioneGPS gestioneGPS) {
        this.gestioneGPS = gestioneGPS;
    }

    public StrutturaGps getCoordinateAttuali() {
        return CoordinateAttuali;
    }

    public void setCoordinateAttuali(StrutturaGps coordinateAttuali) {
        CoordinateAttuali = coordinateAttuali;
    }

    public void AggiungeGPS(Context context, StrutturaGps s) {
        if (db == null) {
            db = new db_dati_gps(context);
        }
        db.AggiungePosizione(s);
    }

    public List<StrutturaGps> RitornaPercorsoGPS(String Data) {
        return db.RitornaPosizioni(Data);
    }
}