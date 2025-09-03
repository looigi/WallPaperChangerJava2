package com.looigi.wallpaperchanger2.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.utilities.log.LogInterno;
import com.looigi.wallpaperchanger2.utilities.meteo.struttura.StrutturaMeteo;

import java.util.Date;

public class VariabiliStaticheStart {
    private static VariabiliStaticheStart instance = null;

    private VariabiliStaticheStart() {
    }

    public static VariabiliStaticheStart getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheStart();
        }

        return instance;
    }

    public static final String UrlWSGlobale = "http://looigi.no-ip.biz"; // http://looigi.ddnsfree.com";
    public static final String PortaLooVF = "1071";
    public static final String PortaLazio = "1072";
    public static final String PortaPassword = "1073";
    public static final String PortaOrari = "1074";
    public static final String PortaVecchioLooVF = "1084";
    public static final String PortaPlayer = "1081";
    public static final String PortaTotoMioImmagini = "1083";
    public static final String PortaDiscoPublic = "1085";
    public static final String PortaWallPaperChanger = "1086";

    private Context context;
    private LogInterno l;
    private Activity mainActivity;
    private boolean GiaPartito = false;
    private String PercorsoDIRLog;
    private boolean Detector;
    private boolean ceWifi = false;
    private boolean presoWiFi = false;
    private int livelloSegnaleConnessione;
    private String tipoConnessione;
    private int velocitaUpload;
    private int velocitaDownload;
    private String ultimoControlloRete;
    private int livello;
    private boolean logAttivo = true;
    private boolean playerAperto = false;
    private boolean VisibileImmagini = false;
    private boolean VisibileVideo = false;
    private boolean VisibilePennetta = false;
    private Intent servizioForeground;
    // private Intent servizioForegroundGPS;
    private boolean segnaleAttivo = true;
    private int accensioniDiSchermo = 0;
    private int chiamate = 0;
    private Date oraEntrata;
    private boolean haFattoTraduzione;
    private boolean haPresoMeteo;
    private StrutturaMeteo meteo;
    private Intent intentGPS;

    /* private boolean AllarmeAttivo = false;
    private Activity actAllarme;
    private boolean AllarmeInCorso = false;
    private TextView txtAllarme;
    private TextView txtInfo1;
    private TextView txtInfo2;
    private boolean AllarmeSuMovimento = true;
    private boolean AllarmeSuBT = true;
    private Float gForcePerAllarme = 1.5F;
    private String btMonitorato = "";

    public String getBtMonitorato() {
        return btMonitorato;
    }

    public void setBtMonitorato(String btMonitorato) {
        this.btMonitorato = btMonitorato;
    }

    public Float getgForcePerAllarme() {
        return gForcePerAllarme;
    }

    public void setgForcePerAllarme(Float gForcePerAllarme) {
        this.gForcePerAllarme = gForcePerAllarme;
    }

    public TextView getTxtInfo2() {
        return txtInfo2;
    }

    public void setTxtInfo2(TextView txtInfo2) {
        this.txtInfo2 = txtInfo2;
    }

    public TextView getTxtInfo1() {
        return txtInfo1;
    }

    public void setTxtInfo1(TextView txtInfo1) {
        this.txtInfo1 = txtInfo1;
    }

    public boolean isAllarmeSuBT() {
        return AllarmeSuBT;
    }

    public void setAllarmeSuBT(boolean allarmeSuBT) {
        AllarmeSuBT = allarmeSuBT;
    }

    public boolean isAllarmeSuMovimento() {
        return AllarmeSuMovimento;
    }

    public void setAllarmeSuMovimento(boolean allarmeSuMovimento) {
        AllarmeSuMovimento = allarmeSuMovimento;
    }

    public TextView getTxtAllarme() {
        return txtAllarme;
    }

    public void setTxtAllarme(TextView txtAllarme) {
        this.txtAllarme = txtAllarme;
    }

    public boolean isAllarmeInCorso() {
        return AllarmeInCorso;
    }

    public void setAllarmeInCorso(boolean allarmeInCorso) {
        AllarmeInCorso = allarmeInCorso;
    }

    public Activity getActAllarme() {
        return actAllarme;
    }

    public void setActAllarme(Activity actAllarme) {
        this.actAllarme = actAllarme;
    }

    public boolean isAllarmeAttivo() {
        return AllarmeAttivo;
    }

    public void setAllarmeAttivo(boolean allarmeAttivo) {
        AllarmeAttivo = allarmeAttivo;
    } */

    public Intent getIntentGPS() {
        return intentGPS;
    }

    public void setIntentGPS(Intent intentGPS) {
        this.intentGPS = intentGPS;
    }

    public boolean isPresoWiFi() {
        return presoWiFi;
    }

    public void setPresoWiFi(boolean presoWiFi) {
        this.presoWiFi = presoWiFi;
    }

    public StrutturaMeteo getMeteo() {
        return meteo;
    }

    public void setMeteo(StrutturaMeteo meteo) {
        this.meteo = meteo;
    }

    public boolean isHaPresoMeteo() {
        return haPresoMeteo;
    }

    public void setHaPresoMeteo(boolean haPresoMeteo) {
        this.haPresoMeteo = haPresoMeteo;
    }

    public boolean isHaFattoTraduzione() {
        return haFattoTraduzione;
    }

    public void setHaFattoTraduzione(boolean haFattoTraduzione) {
        this.haFattoTraduzione = haFattoTraduzione;
    }

    public Date getOraEntrata() {
        return oraEntrata;
    }

    public void setOraEntrata(Date oraEntrata) {
        this.oraEntrata = oraEntrata;
    }

    public int getChiamate() {
        return chiamate;
    }

    public void setChiamate(int chiamate) {
        this.chiamate = chiamate;
    }

    public int getAccensioniDiSchermo() {
        return accensioniDiSchermo;
    }

    public void setAccensioniDiSchermo(int accensioniDiSchermo) {
        this.accensioniDiSchermo = accensioniDiSchermo;
    }

    public boolean isSegnaleAttivo() {
        return segnaleAttivo;
    }

    public void setSegnaleAttivo(boolean segnaleAttivo) {
        this.segnaleAttivo = segnaleAttivo;
    }

    public Intent getServizioForeground() {
        return servizioForeground;
    }

    public void setServizioForeground(Intent servizioForeground) {
        this.servizioForeground = servizioForeground;
    }

    /* public Intent getServizioForegroundGPS() {
        return servizioForegroundGPS;
    }

    public void setServizioForegroundGPS(Intent servizioForegroundGPS) {
        this.servizioForegroundGPS = servizioForegroundGPS;
    } */

    public boolean isVisibilePennetta() {
        return VisibilePennetta;
    }

    public void setVisibilePennetta(boolean visibilePennetta) {
        VisibilePennetta = visibilePennetta;
    }

    public String getUltimoControlloRete() {
        return ultimoControlloRete;
    }

    public void setUltimoControlloRete(String ultimoControlloRete) {
        this.ultimoControlloRete = ultimoControlloRete;
    }

    public boolean isVisibileVideo() {
        return VisibileVideo;
    }

    public void setVisibileVideo(boolean visibileVideo) {
        VisibileVideo = visibileVideo;
    }

    public boolean isVisibileImmagini() {
        return VisibileImmagini;
    }

    public void setVisibileImmagini(boolean visibileImmagini) {
        VisibileImmagini = visibileImmagini;
    }

    public boolean isPlayerAperto() {
        return playerAperto;
    }

    public void setPlayerAperto(boolean playerAperto) {
        this.playerAperto = playerAperto;
    }

    public boolean isLogAttivo() {
        return logAttivo;
    }

    public void setLogAttivo(boolean logAttivo) {
        this.logAttivo = logAttivo;
    }

    public int getLivello() {
        return livello;
    }

    public void setLivello(int livello) {
        this.livello = livello;
    }

    public int getVelocitaUpload() {
        return velocitaUpload;
    }

    public void setVelocitaUpload(int velocitaUpload) {
        this.velocitaUpload = velocitaUpload;
    }

    public int getVelocitaDownload() {
        return velocitaDownload;
    }

    public void setVelocitaDownload(int velocitaDownload) {
        this.velocitaDownload = velocitaDownload;
    }

    public boolean isCeWifi() {
        return ceWifi;
    }

    public String getTipoConnessione() {
        return tipoConnessione;
    }

    public void setTipoConnessione(String tipoConnessione) {
        this.tipoConnessione = tipoConnessione;
    }

    public void setCeWifi(boolean ceWifi) {
        this.ceWifi = ceWifi;
    }

    public int getLivelloSegnaleConnessione() {
        return livelloSegnaleConnessione;
    }

    public void setLivelloSegnaleConnessione(int livelloSegnaleConnessione) {
        this.livelloSegnaleConnessione = livelloSegnaleConnessione;
    }

    public void ChiudeActivity(boolean Finish) {
        if (mainActivity != null) {
            // mainActivity.moveTaskToBack(true);
            // if (Finish) {
            mainActivity.finish();
            // }
        }
    }

    public boolean isDetector() {
        return Detector;
    }

    public void setDetector(boolean detector) {
        Detector = detector;
    }

    public String getPercorsoDIRLog() {
        return PercorsoDIRLog;
    }

    public void setPercorsoDIRLog(String percorsoDIRLog) {
        PercorsoDIRLog = percorsoDIRLog;
    }

    public LogInterno getLog() {
        return l;
    }

    public void setLog(LogInterno l) {
        this.l = l;
    }

    public boolean isGiaPartito() {
        return GiaPartito;
    }

    public void setGiaPartito(boolean giaPartito) {
        GiaPartito = giaPartito;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}