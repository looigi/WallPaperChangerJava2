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