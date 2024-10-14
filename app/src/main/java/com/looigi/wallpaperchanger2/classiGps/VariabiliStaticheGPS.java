package com.looigi.wallpaperchanger2.classiGps;

import android.content.Context;
import android.location.Location;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.classiGps.strutture.StrutturaAccensioneGPS;
import com.looigi.wallpaperchanger2.classiGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classiGps.strutture.StrutturaPuntiSpegnimento;

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
    private GestioneMappa mappa;
    public static int quantiPuntiSumappa = 990;
    private long distanzaTotale = 0;
    private boolean bloccatoDaTasto = false;
    private boolean segue = true;
    private boolean mostraSegnale = true;
    private boolean mostraPercorso = true;
    private List<StrutturaPuntiSpegnimento> listaPuntiDiSpegnimento = new ArrayList<>();
    private int DistanzaMetriPerPS = 50;

    public int getDistanzaMetriPerPS() {
        return DistanzaMetriPerPS;
    }

    public void setDistanzaMetriPerPS(int distanzaMetriPerPS) {
        DistanzaMetriPerPS = distanzaMetriPerPS;
    }

    public List<StrutturaPuntiSpegnimento> getListaPuntiDiSpegnimento() {
        return listaPuntiDiSpegnimento;
    }

    public void setListaPuntiDiSpegnimento(List<StrutturaPuntiSpegnimento> listaPuntiDiSpegnimento) {
        this.listaPuntiDiSpegnimento = listaPuntiDiSpegnimento;
    }

    public boolean isMostraSegnale() {
        return mostraSegnale;
    }

    public void setMostraSegnale(boolean mostraSegnale) {
        this.mostraSegnale = mostraSegnale;
    }

    public boolean isMostraPercorso() {
        return mostraPercorso;
    }

    public void setMostraPercorso(boolean mostraPercorso) {
        this.mostraPercorso = mostraPercorso;
    }

    public boolean isSegue() {
        return segue;
    }

    public void setSegue(boolean segue) {
        this.segue = segue;
    }

    public boolean isBloccatoDaTasto() {
        return bloccatoDaTasto;
    }

    public void setBloccatoDaTasto(boolean bloccatoDaTasto) {
        this.bloccatoDaTasto = bloccatoDaTasto;
    }

    public long getDistanzaTotale() {
        return distanzaTotale;
    }

    public void setDistanzaTotale(long distanzaTotale) {
        this.distanzaTotale = distanzaTotale;
    }

    public GestioneMappa getMappa() {
        return mappa;
    }

    public void setMappa(GestioneMappa mappa) {
        this.mappa = mappa;
    }

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