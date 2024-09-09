package com.looigi.wallpaperchanger2.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classiStandard.Log;
import com.looigi.wallpaperchanger2.classiAttivita.StrutturaImmagine;

import java.util.ArrayList;
import java.util.List;

public class VariabiliStaticheServizio {
    private static VariabiliStaticheServizio instance = null;

    private VariabiliStaticheServizio() {
    }

    public static VariabiliStaticheServizio getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheServizio();
        }

        return instance;
    }

    private Context context;
    private Activity mainActivity;
    private boolean ciSonoPermessi = false;
    private Log l;
    private int idNotifica = 111112;
    public static String channelName = "wallpaperchanger2";
    public static String NOTIFICATION_CHANNEL_STRING = "com.looigi.wallpaperchanger2";
    public static int NOTIFICATION_CHANNEL_ID = 2;
    public static int channelIdIntentOverlay = 152;
    private String NomeFileDiLog = "";
    private Intent servizioForeground;
    private String PercorsoDIRLog = "";
    public static int secondiDiAttesaContatore = 60;

    // INIZIO VARIABILI ATTIVITA'
    public static final String UrlWS = "http://www.wsloovf.looigi.it";
    public static final String PercorsoImmagineSuURL = "http://www.sfondi.looigi.it";
    private ImageView imgCaricamento;
    private boolean screenOn = true;
    private String DataAppoggio;
    private String DimeAppoggio;
    private TextView txtQuanteImmagini;
    private int ImmaginiOnline;
    private boolean retePresente = true;
    private boolean offline = true;
    private StrutturaImmagine UltimaImmagine;
    private ImageView imgImpostata;
    private boolean blur = true;
    private boolean scriveTestoSuImmagine = true;
    private boolean onOff = true;
    private boolean resize = true;
    private List<StrutturaImmagine> listaImmagini = new ArrayList<>();
    private TextView txtPath;
    private boolean ImmagineCambiataConSchermoSpento = false;
    private boolean ePartito = false;
    private int minutiAttesa = 1;

    // private int quantiGiri;
    // private int SecondiAlCambio = 10000;
    private TextView txtTempoAlCambio;
    private int SecondiPassati;
    private String PercorsoIMMAGINI = Environment.getExternalStorageDirectory().getPath()+"/LooigiSoft/looWebPlayer/ImmaginiMusica";
    // private int tempoTimer = 30000;

    public int getMinutiAttesa() {
        return minutiAttesa;
    }

    public void setMinutiAttesa(int minutiAttesa) {
        this.minutiAttesa = minutiAttesa;
    }

    public boolean isePartito() {
        return ePartito;
    }

    public void setePartito(boolean ePartito) {
        this.ePartito = ePartito;
    }

    public boolean isImmagineCambiataConSchermoSpento() {
        return ImmagineCambiataConSchermoSpento;
    }

    public void setImmagineCambiataConSchermoSpento(boolean immagineCambiataConSchermoSpento) {
        ImmagineCambiataConSchermoSpento = immagineCambiataConSchermoSpento;
    }

    public TextView getTxtPath() {
        return txtPath;
    }

    public void setTxtPath(TextView txtPath) {
        this.txtPath = txtPath;
    }

    /* public int getTempoTimer() {
        return tempoTimer;
    }

    public void setTempoTimer(int tempoTimer) {
        this.tempoTimer = tempoTimer;
    } */

    public List<StrutturaImmagine> getListaImmagini() {
        return listaImmagini;
    }

    public void setListaImmagini(List<StrutturaImmagine> listaImmagini) {
        this.listaImmagini = listaImmagini;
    }

    public String getPercorsoIMMAGINI() {
        return PercorsoIMMAGINI;
    }

    public void setPercorsoIMMAGINI(String percorsoIMMAGINI) {
        PercorsoIMMAGINI = percorsoIMMAGINI;
    }

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    /* public int getSecondiAlCambio() {
        return SecondiAlCambio;
    }

    public void setSecondiAlCambio(int secondiAlCambio) {
        SecondiAlCambio = secondiAlCambio;
    } */

    public boolean isBlur() {
        return blur;
    }

    public void setBlur(boolean blur) {
        this.blur = blur;
    }

    public boolean isScriveTestoSuImmagine() {
        return scriveTestoSuImmagine;
    }

    public void setScriveTestoSuImmagine(boolean scriveTestoSuImmagine) {
        this.scriveTestoSuImmagine = scriveTestoSuImmagine;
    }

    public StrutturaImmagine getUltimaImmagine() {
        return UltimaImmagine;
    }

    public void setUltimaImmagine(StrutturaImmagine ultimaImmagine) {
        UltimaImmagine = ultimaImmagine;
    }

    public ImageView getImgImpostata() {
        return imgImpostata;
    }

    public void setImgImpostata(ImageView imgImpostata) {
        this.imgImpostata = imgImpostata;
    }

    public TextView getTxtTempoAlCambio() {
        return txtTempoAlCambio;
    }

    public void setTxtTempoAlCambio(TextView txtTempoAlCambio) {
        this.txtTempoAlCambio = txtTempoAlCambio;
    }

    public int getSecondiPassati() {
        return SecondiPassati;
    }

    public void setSecondiPassati(int secondiPassati) {
        SecondiPassati = secondiPassati;
    }

    /* public int getQuantiGiri() {
        return quantiGiri;
    }

    public void setQuantiGiri(int quantiGiri) {
        this.quantiGiri = quantiGiri;
    } */

    public boolean isOffline() {
        return offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
    }

    public boolean isRetePresente() {
        return retePresente;
    }

    public void setRetePresente(boolean retePresente) {
        this.retePresente = retePresente;
    }

    public String getDataAppoggio() {
        return DataAppoggio;
    }

    public void setDataAppoggio(String dataAppoggio) {
        DataAppoggio = dataAppoggio;
    }

    public String getDimeAppoggio() {
        return DimeAppoggio;
    }

    public void setDimeAppoggio(String dimeAppoggio) {
        DimeAppoggio = dimeAppoggio;
    }

    public TextView getTxtQuanteImmagini() {
        return txtQuanteImmagini;
    }

    public void setTxtQuanteImmagini(TextView txtQuanteImmagini) {
        this.txtQuanteImmagini = txtQuanteImmagini;
    }

    public int getImmaginiOnline() {
        return ImmaginiOnline;
    }

    public void setImmaginiOnline(int immaginiOnline) {
        ImmaginiOnline = immaginiOnline;
    }

    public boolean isScreenOn() {
        return screenOn;
    }

    public void setScreenOn(boolean screenOn) {
        this.screenOn = screenOn;
    }

    public ImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(ImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    /* public long getSecondiDiAttesaContatore() {
        return secondiDiAttesaContatore;
    }

    public void setSecondiDiAttesaContatore(long secondiDiAttesaContatore) {
        this.secondiDiAttesaContatore = secondiDiAttesaContatore;
    } */

    public String getNomeFileDiLog() {
        return NomeFileDiLog;
    }

    public void setNomeFileDiLog(String nomeFileDiLog) {
        NomeFileDiLog = nomeFileDiLog;
    }

    public String getPercorsoDIRLog() {
        return PercorsoDIRLog;
    }

    public void setPercorsoDIRLog(String percorsoDIRLog) {
        PercorsoDIRLog = percorsoDIRLog;
    }

    public Intent getServizioForeground() {
        return servizioForeground;
    }

    public void setServizioForeground(Intent servizioForeground) {
        this.servizioForeground = servizioForeground;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public int getIdNotifica() {
        return idNotifica;
    }

    public void setIdNotifica(int idNotifica) {
        this.idNotifica = idNotifica;
    }

    public Log getLog() {
        return l;
    }

    public void setLog(Log l) {
        this.l = l;
    }

    public boolean isCiSonoPermessi() {
        return ciSonoPermessi;
    }

    public void setCiSonoPermessi(boolean ciSonoPermessi) {
        this.ciSonoPermessi = ciSonoPermessi;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}