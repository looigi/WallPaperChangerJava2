package com.looigi.wallpaperchanger2.gps;

import android.content.Context;
import android.widget.ImageView;

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
    public static int attesaControlloGPS = 1;
    // private int oraGpsAccensione = 16;
    // private int minutiGpsAccensione = 5;
    // private int oraGpsSpengimento = 7;
    // private int minutiGpsSpengimento = 30;
    // private int minutiDiAttesaGpsPrimaDelloSpengimento = 3;
    private StrutturaAccensioneGPS accensioneGPS;
    private ImageView bitmapHome;

    public ImageView getBitmapHome() {
        return bitmapHome;
    }

    public void setBitmapHome(ImageView bitmapHome) {
        this.bitmapHome = bitmapHome;
    }

    public StrutturaAccensioneGPS getAccensioneGPS() {
        return accensioneGPS;
    }

    public void setAccensioneGPS(StrutturaAccensioneGPS accensioneGPS) {
        this.accensioneGPS = accensioneGPS;
    }

    /* public int getMinutiDiAttesaGpsPrimaDelloSpengimento() {
        return minutiDiAttesaGpsPrimaDelloSpengimento;
    }

    public void setMinutiDiAttesaGpsPrimaDelloSpengimento(int minutiDiAttesaGpsPrimaDelloSpengimento) {
        this.minutiDiAttesaGpsPrimaDelloSpengimento = minutiDiAttesaGpsPrimaDelloSpengimento;
    }

    public int getMinutiGpsSpengimento() {
        return minutiGpsSpengimento;
    }

    public void setMinutiGpsSpengimento(int minutiGpsSpengimento) {
        this.minutiGpsSpengimento = minutiGpsSpengimento;
    }

    public int getOraGpsSpengimento() {
        return oraGpsSpengimento;
    }

    public void setOraGpsSpengimento(int oraGpsSpengimento) {
        this.oraGpsSpengimento = oraGpsSpengimento;
    }

    public int getOraGpsAccensione() {
        return oraGpsAccensione;
    }

    public void setOraGpsAccensione(int oraGpsAccensione) {
        this.oraGpsAccensione = oraGpsAccensione;
    }

    public int getMinutiGpsAccensione() {
        return minutiGpsAccensione;
    }

    public void setMinutiGpsAccensione(int minutiGpsAccensione) {
        this.minutiGpsAccensione = minutiGpsAccensione;
    } */

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