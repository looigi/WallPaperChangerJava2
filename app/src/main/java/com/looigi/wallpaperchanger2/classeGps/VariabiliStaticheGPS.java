package com.looigi.wallpaperchanger2.classeGps;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.ListView;

import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaAccensioneGPS;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaPuntiSpegnimento;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheGPS {
    private static VariabiliStaticheGPS instance = null;
    // private db_dati_gps db;

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
    private Context context;
    private int idNotifica = 111119;
    public static String channelName = "WallPaperChangerII_GPS";
    public static int puntiPerFreccia = 7;
    public static String NOTIFICATION_CHANNEL_STRING = "com.looigi.wallpaperchanger2";
    public static int NOTIFICATION_CHANNEL_ID = 9;
    private StrutturaAccensioneGPS accensioneGPS;
    private ImageView bitmapHome;
    private GestioneMappa mappa;
    private int quantiPuntiSumappa = 50;
    private long distanzaTotale = 0;
    private boolean bloccatoDaTasto = false;
    private boolean segue = true;
    private boolean mostraSegnale = true;
    private boolean mostraPercorso = true;
    private List<StrutturaPuntiSpegnimento> listaPuntiDiSpegnimento = new ArrayList<>();
    private int DistanzaMetriPerPS = 50;
    private ImmagineZoomabile imgMappa;
    // private boolean NonScriverePunti = false;
    private String ultimoDataPunto = "";
    private boolean puntiSospensioneAttivi = true;
    private boolean accuracyAttiva = true;
    // private boolean bloccoPerWifi = true;
    private boolean MappeAperte = false;
    private GifImageView imgAttesa;
    private ListView lstFilesRemoti;

    public ListView getLstFilesRemoti() {
        return lstFilesRemoti;
    }

    public void setLstFilesRemoti(ListView lstFilesRemoti) {
        this.lstFilesRemoti = lstFilesRemoti;
    }

    public GifImageView getImgAttesa() {
        return imgAttesa;
    }

    public void setImgAttesa(GifImageView imgAttesa) {
        this.imgAttesa = imgAttesa;
    }

    public boolean isMappeAperte() {
        return MappeAperte;
    }

    public void setMappeAperte(boolean mappeAperte) {
        MappeAperte = mappeAperte;
    }

    public int getQuantiPuntiSumappa() {
        return quantiPuntiSumappa;
    }

    public void setQuantiPuntiSumappa(int quantiPuntiSumappa) {
        this.quantiPuntiSumappa = quantiPuntiSumappa;
    }

    /* public boolean isBloccoPerWifi() {
        return bloccoPerWifi;
    }

    public void setBloccoPerWifi(boolean bloccoPerWifi) {
        this.bloccoPerWifi = bloccoPerWifi;
    } */

    public boolean isAccuracyAttiva() {
        return accuracyAttiva;
    }

    public void setAccuracyAttiva(boolean accuracyAttiva) {
        this.accuracyAttiva = accuracyAttiva;
    }

    public boolean isPuntiSospensioneAttivi() {
        return puntiSospensioneAttivi;
    }

    public void setPuntiSospensioneAttivi(boolean puntiSospensioneAttivi) {
        this.puntiSospensioneAttivi = puntiSospensioneAttivi;
    }

    public String getUltimoDataPunto() {
        return ultimoDataPunto;
    }

    public void setUltimoDataPunto(String ultimoDataPunto) {
        this.ultimoDataPunto = ultimoDataPunto;
    }

    /* public boolean isNonScriverePunti() {
        return NonScriverePunti;
    }

    public void setNonScriverePunti(boolean nonScriverePunti) {
        NonScriverePunti = nonScriverePunti;
    } */

    public ImmagineZoomabile getImgMappa() {
        return imgMappa;
    }

    public void setImgMappa(ImmagineZoomabile imgMappa) {
        this.imgMappa = imgMappa;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

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
        db_dati_gps db = new db_dati_gps(context);
        /* if (db == null) {
            db = new db_dati_gps(context);
        } */
        db.AggiungePosizione(s);
        db.ChiudeDB();
    }

    public List<StrutturaGps> RitornaPercorsoGPS(String Data) {
        db_dati_gps db = new db_dati_gps(context);
        List<StrutturaGps> lista = db.RitornaPosizioni(Data);
        db.ChiudeDB();

        return lista;
    }
}