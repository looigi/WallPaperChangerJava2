package com.looigi.wallpaperchanger2.classeWallpaper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VariabiliStaticheWallpaper {
    private static VariabiliStaticheWallpaper instance = null;

    private VariabiliStaticheWallpaper() {
    }

    public static VariabiliStaticheWallpaper getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheWallpaper();
        }

        return instance;
    }

    private Context context;
    private Activity mainActivity;
    private boolean ciSonoPermessi = false;
    // private LogInterno l;
    private int idNotifica = 111112;
    public static String channelName = "WallPaperChangerII";
    public static String NOTIFICATION_CHANNEL_STRING = "com.looigi.wallpaperchanger2";
    public static int NOTIFICATION_CHANNEL_ID = 2;
    public static final int TimeoutImmagine = 5;
    public static int channelIdIntentOverlay = 152;
    // private String NomeFileDiLog = "";
    // private String PercorsoDIRLog = "";
    public static int secondiDiAttesaContatore = 60;
    private boolean staPartendo = true;
    private int errori = 0;
    // private boolean Detector = false;
    private boolean servizioAttivo = true;
    public static float percAumentoX = .115F;
    public static float percAumentoY = .185F;
    private String immagineImpostataDaChi = "";
    private String Filtro = "";

    // INIZIO VARIABILI ATTIVITA'
    public static final String UrlWS = "http://www.wsloovf.looigi.it";
    public static final String PercorsoImmagineSuURL = "http://www.sfondi.looigi.it";
    // private ImageView imgCaricamento;
    private boolean screenOn = true;
    private String DataAppoggio;
    private String DimeAppoggio;
    private TextView txtQuanteImmagini;
    private int ImmaginiOnline;
    private boolean retePresente = true;
    // private boolean letteImpostazioni = false;
    private int modalitaImmagini = 0; // 0 - Web / 1 - Locale / 2 - Immagini
    private StrutturaImmagine UltimaImmagine;
    private StrutturaImmagine UltimaImmaginePerLock;
    private ImageView imgImpostata;
    private ImageView imgImpostataFinale;
    private boolean blur = true;
    private boolean scriveTestoSuImmagine = true;
    private boolean onOff = true;
    private boolean home = true;
    private boolean lock = false;
    private boolean perData = false;
    private int giorniDifferenza = 7;
    private boolean resize = true;
    private boolean effetti = false;
    private List<StrutturaImmagine> listaImmagini = new ArrayList<>();
    private TextView txtPath;
    // private boolean ImmagineCambiataConSchermoSpento = false;
    private boolean ePartito = false;
    private int minutiAttesa = 15;
    private ListView lstImmagini;
    private RelativeLayout layScelta;
    private String filtroRicerca = "";
    private AdapterListenerImmagini adapterImmagini;
    private TextView txtQuanteRicerca;
    private RelativeLayout layAttesa;
    private TextView txtTempoAlCambio;
    private int SecondiPassati;
    private String PercorsoIMMAGINI = Environment.getExternalStorageDirectory().getPath();
    private boolean sbragaTutto = false;
    private boolean Espansa = false;
    private boolean SoloVolti = true;
    private boolean staPrendendoVolto = false;
    private List<Rect> quadratiFaccia;
    private StrutturaImmagine immagineSelezionataDaLista;
    private boolean ApreRicerca = false;
    private TextView txtAvanzamentoRefresh;
    private boolean ImpostataConSchermoSpento = false;
    private long ultimoCambio = -1L;

    public void ChiudeActivity(boolean Finish) {
        if (mainActivity != null) {
            // mainActivity.moveTaskToBack(true);
            // if (Finish) {
                mainActivity.finish();
            // }
        }
    }

    public int getGiorniDifferenza() {
        return giorniDifferenza;
    }

    public void setGiorniDifferenza(int giorniDifferenza) {
        this.giorniDifferenza = giorniDifferenza;
    }

    public boolean isPerData() {
        return perData;
    }

    public void setPerData(boolean perData) {
        this.perData = perData;
    }

    public String getFiltro() {
        return Filtro;
    }

    public void setFiltro(String filtro) {
        Filtro = filtro;
    }

    public long getUltimoCambio() {
        return ultimoCambio;
    }

    public void setUltimoCambio(long ultimoCambio) {
        this.ultimoCambio = ultimoCambio;
    }

    public String getImmagineImpostataDaChi() {
        return immagineImpostataDaChi;
    }

    public void setImmagineImpostataDaChi(String immagineImpostataDaChi) {
        this.immagineImpostataDaChi = immagineImpostataDaChi;
    }

    public boolean isImpostataConSchermoSpento() {
        return ImpostataConSchermoSpento;
    }

    public void setImpostataConSchermoSpento(boolean impostataConSchermoSpento) {
        ImpostataConSchermoSpento = impostataConSchermoSpento;
    }

    public TextView getTxtAvanzamentoRefresh() {
        return txtAvanzamentoRefresh;
    }

    public void setTxtAvanzamentoRefresh(TextView txtAvanzamentoRefresh) {
        this.txtAvanzamentoRefresh = txtAvanzamentoRefresh;
    }

    public boolean isApreRicerca() {
        return ApreRicerca;
    }

    public void setApreRicerca(boolean apreRicerca) {
        ApreRicerca = apreRicerca;
    }

    public StrutturaImmagine getImmagineSelezionataDaLista() {
        return immagineSelezionataDaLista;
    }

    public void setImmagineSelezionataDaLista(StrutturaImmagine immagineSelezionataDaLista) {
        this.immagineSelezionataDaLista = immagineSelezionataDaLista;
    }

    public ImageView getImgImpostataFinale() {
        return imgImpostataFinale;
    }

    public void setImgImpostataFinale(ImageView imgImpostataFinale) {
        this.imgImpostataFinale = imgImpostataFinale;
    }

    public boolean isEffetti() {
        return effetti;
    }

    public void setEffetti(boolean effetti) {
        this.effetti = effetti;
    }

    public boolean isSoloVolti() {
        return SoloVolti;
    }

    public void setSoloVolti(boolean soloVolti) {
        SoloVolti = soloVolti;
    }

    public List<Rect> getQuadratiFaccia() {
        return quadratiFaccia;
    }

    public void setQuadratiFaccia(List<Rect> quadratiFaccia) {
        this.quadratiFaccia = quadratiFaccia;
    }

    public boolean isStaPrendendoVolto() {
        return staPrendendoVolto;
    }

    public void setStaPrendendoVolto(boolean staPrendendoVolto) {
        this.staPrendendoVolto = staPrendendoVolto;
    }

    public boolean isEspansa() {
        return Espansa;
    }

    public void setEspansa(boolean espansa) {
        Espansa = espansa;
    }

    public StrutturaImmagine getUltimaImmaginePerLock() {
        return UltimaImmaginePerLock;
    }

    public void setUltimaImmaginePerLock(StrutturaImmagine ultimaImmaginePerLock) {
        UltimaImmaginePerLock = ultimaImmaginePerLock;
    }

    public boolean isSbragaTutto() {
        return sbragaTutto;
    }

    public void setSbragaTutto(boolean sbragaTutto) {
        this.sbragaTutto = sbragaTutto;
    }

    public boolean isServizioAttivo() {
        return servizioAttivo;
    }

    public void setServizioAttivo(boolean servizioAttivo) {
        this.servizioAttivo = servizioAttivo;
    }

    // SEZIONE DETECTOR
    private boolean Vibrazione = true;

    public boolean isVibrazione() {
        return Vibrazione;
    }

    public void setVibrazione(boolean vibrazione) {
        Vibrazione = vibrazione;
    }

    /* public boolean isDetector() {
        return Detector;
    }

    public void setDetector(boolean detector) {
        Detector = detector;
    } */

    public int getErrori() {
        return errori;
    }

    public void setErrori(int errori) {
        this.errori = errori;
    }

    public boolean isStaPartendo() {
        return staPartendo;
    }

    public void setStaPartendo(boolean staPartendo) {
        this.staPartendo = staPartendo;
    }

    public RelativeLayout getLayAttesa() {
        return layAttesa;
    }

    public void setLayAttesa(RelativeLayout layAttesa) {
        this.layAttesa = layAttesa;
    }

    /* public boolean isLetteImpostazioni() {
        return letteImpostazioni;
    }

    public void setLetteImpostazioni(boolean letteImpostazioni) {
        this.letteImpostazioni = letteImpostazioni;
    } */

    public TextView getTxtQuanteRicerca() {
        return txtQuanteRicerca;
    }

    public void setTxtQuanteRicerca(TextView txtQuanteRicerca) {
        this.txtQuanteRicerca = txtQuanteRicerca;
    }

    public AdapterListenerImmagini getAdapterImmagini() {
        return adapterImmagini;
    }

    public void setAdapterImmagini(AdapterListenerImmagini adapterImmagini) {
        this.adapterImmagini = adapterImmagini;
    }

    public String getFiltroRicerca() {
        return filtroRicerca;
    }

    public void setFiltroRicerca(String filtroRicerca) {
        this.filtroRicerca = filtroRicerca;
    }

    public RelativeLayout getLayScelta() {
        return layScelta;
    }

    public void setLayScelta(RelativeLayout layScelta) {
        this.layScelta = layScelta;
    }

    public ListView getLstImmagini() {
        return lstImmagini;
    }

    public void setLstImmagini(ListView lstImmagini) {
        this.lstImmagini = lstImmagini;
    }

    public boolean isHome() {
        return home;
    }

    public void setHome(boolean home) {
        this.home = home;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

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

    /* public boolean isImmagineCambiataConSchermoSpento() {
        return ImmagineCambiataConSchermoSpento;
    }

    public void setImmagineCambiataConSchermoSpento(boolean immagineCambiataConSchermoSpento) {
        ImmagineCambiataConSchermoSpento = immagineCambiataConSchermoSpento;
    } */

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

    public int getModoRicercaImmagine() {
        return modalitaImmagini;
    }

    public void setModoRicercaImmagine(int numero) {
        this.modalitaImmagini = numero;
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

    /* public ImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(ImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public long getSecondiDiAttesaContatore() {
        return secondiDiAttesaContatore;
    }

    public void setSecondiDiAttesaContatore(long secondiDiAttesaContatore) {
        this.secondiDiAttesaContatore = secondiDiAttesaContatore;
    }

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
    */

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

    /* public LogInterno getLog() {
        return l;
    }

    public void setLog(LogInterno l) {
        this.l = l;
    } */

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