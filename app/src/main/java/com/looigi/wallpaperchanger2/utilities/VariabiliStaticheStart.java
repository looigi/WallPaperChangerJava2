package com.looigi.wallpaperchanger2.utilities;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.looigi.wallpaperchanger2.classeMostraImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classiDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiPlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classiStandard.LogInterno;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;

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

    private Context context;
    private LogInterno l;
    private Activity mainActivity;
    private boolean GiaPartito = false;
    private String PercorsoDIRLog;
    private boolean Detector;
    private boolean ceWifi = false;
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