package com.looigi.wallpaperchanger2.Mappe.MappeEGps;

import android.content.Context;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.strutture.StrutturaAccensioneGPS;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.strutture.StrutturaPuntiSpegnimento;
import com.looigi.wallpaperchanger2.UtilitiesVarie.ImmagineZoomabile;

import java.util.ArrayList;
import java.util.List;

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
    private ImageView imgAttesa;
    private ListView lstFilesRemoti;
    private TextView txtMappa;
    private boolean primoPassaggio = true;
    private int vecchiDati = -1;
    private int livelloZoomStandard = 16;
    private List<Circle> circolettiPS;
    private List<Marker> markersPS;
    private List<Marker> markersPA;
    private GoogleMap mappetta;
    // private List<Marker> markersPATH;
    private int puntiTotali;
    private boolean disegnaPathComePolyline;
    private boolean cambiaColoreAllaMappaPerVelocita = true;

    public boolean isCambiaColoreAllaMappaPerVelocita() {
        return cambiaColoreAllaMappaPerVelocita;
    }

    public void setCambiaColoreAllaMappaPerVelocita(boolean cambiaColoreAllaMappaPerVelocita) {
        this.cambiaColoreAllaMappaPerVelocita = cambiaColoreAllaMappaPerVelocita;
    }
    /* private Polyline polylineSegnale;
    // private Polyline polylineVelocita;

    public Polyline getPolylineVelocita() {
        return polylineVelocita;
    }

    public void setPolylineVelocita(Polyline polylineVelocita) {
        this.polylineVelocita = polylineVelocita;
    }

    public Polyline getPolylineSegnale() {
        return polylineSegnale;
    }

    public void setPolylineSegnale(Polyline polylineSegnale) {
        this.polylineSegnale = polylineSegnale;
    } */

    public boolean isDisegnaPathComePolyline() {
        return disegnaPathComePolyline;
    }

    public void setDisegnaPathComePolyline(boolean disegnaPathComePolyline) {
        this.disegnaPathComePolyline = disegnaPathComePolyline;
    }

    public int getPuntiTotali() {
        return puntiTotali;
    }

    public void setPuntiTotali(int puntiTotali) {
        this.puntiTotali = puntiTotali;
    }

    public GoogleMap getMappetta() {
        return mappetta;
    }

    public void setMappetta(GoogleMap mappetta) {
        this.mappetta = mappetta;
    }

    public List<Circle> getCircolettiPS() {
        return circolettiPS;
    }

    public void setCircolettiPS(List<Circle> circolettiPS) {
        this.circolettiPS = circolettiPS;
    }

    public List<Marker> getMarkersPS() {
        return markersPS;
    }

    public void setMarkersPS(List<Marker> markersPS) {
        this.markersPS = markersPS;
    }

    public List<Marker> getMarkersPA() {
        return markersPA;
    }

    public void setMarkersPA(List<Marker> markersPA) {
        this.markersPA = markersPA;
    }

    public int getLivelloZoomStandard() {
        return livelloZoomStandard;
    }

    public void setLivelloZoomStandard(int livelloZoomStandard) {
        this.livelloZoomStandard = livelloZoomStandard;
    }

    public int getVecchiDati() {
        return vecchiDati;
    }

    public void setVecchiDati(int vecchiDati) {
        this.vecchiDati = vecchiDati;
    }

    public boolean isPrimoPassaggio() {
        return primoPassaggio;
    }

    public void setPrimoPassaggio(boolean primoPassaggio) {
        this.primoPassaggio = primoPassaggio;
    }

    public TextView getTxtMappa() {
        return txtMappa;
    }

    public void setTxtMappa(TextView txtMappa) {
        this.txtMappa = txtMappa;
    }

    public ListView getLstFilesRemoti() {
        return lstFilesRemoti;
    }

    public void setLstFilesRemoti(ListView lstFilesRemoti) {
        this.lstFilesRemoti = lstFilesRemoti;
    }

    public ImageView getImgAttesa() {
        return imgAttesa;
    }

    public void setImgAttesa(ImageView imgAttesa) {
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