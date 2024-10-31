package com.looigi.wallpaperchanger2.classePlayer;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerArtisti;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaUtenti;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadCanzone;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadImmagine;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.InterrogazioneWSPlayer;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStatichePlayer {
    private static VariabiliStatichePlayer instance = null;

    private VariabiliStatichePlayer() {
    }

    public static VariabiliStatichePlayer getInstance() {
        if (instance == null) {
            instance = new VariabiliStatichePlayer();
        }

        return instance;
    }

    private Activity act;
    private Context context;
    private boolean giaPartito = false;
    private int idNotifica = 111114;
    public static String channelName = "Player";
    public static String NOTIFICATION_CHANNEL_STRING = "com.looigi.wallpaperchanger2.player";
    public static int NOTIFICATION_CHANNEL_ID = 4;
    public static int channelIdIntentOverlay = 152;
    // public static int SecondiCambioImmagine = 30;
    public static int SecondiBranoPregresso= 15;
    public static final int TimeoutImmagine = 5;
    public static final int TimeoutBrano = 45;
    private boolean staSuonando = false;
    public static String UrlWS = "http://looigi.no-ip.biz:1081";
    private StrutturaUtenti Utente;
    private int braniTotali;
    public static String PercorsoBranoMP3SuURL = "http://looigi.no-ip.biz:1085";
    private StrutturaBrano ultimoBrano;
    private MediaPlayer mp;
    private ImageView imgAvanti;
    private ImageView imgIndietro;
    private ImageView imgPlayStop;
    private ImmagineZoomabile imgBrano;
    private TextView txtTitolo;
    private GifImageView imgCaricamento;
    private TextView txtOperazione;
    private String pathUltimaImmagine;
    private TextView txtInizio;
    private TextView txtFine;
    private SeekBar seekBarBrano;
    private int DurataBranoInSecondi;
    private int SecondiPassati;
    private boolean FermaTimer = false;
    public boolean mascheraNascosta = false;
    private String inizioMinuti;
    private String fineMinuti;
    // private TextView txtInformazioniPlayer;
    private int StavaSuonando = -1;
    private boolean staCaricandoBranoPregresso = false;
    private StrutturaBrano StrutturaBranoPregressoCaricata;
    private TextView txtBranoPregresso;
    private boolean haCaricatoBranoPregresso = false;
    private ImageView imgCambiaPregresso;
    private ImageView imgCuffie;
    private List<ImageView> imgBellezza;
    private ChiamateWsPlayer classeChiamata;
    // private List<StrutturaChiamateWSPlayer> chiamate;
    private boolean playerAttivo = false;
    private float limiteInGb = 1.5F;
    private List<String> braniSuSD = new ArrayList<>();
    private TextView txtQuanteRicerca;
    private boolean CuffieInserite = false;
    private InterrogazioneWSPlayer ClasseInterrogazione;
    private DownloadCanzone DownCanzone;
    private DownloadImmagine DownImmagine;
    private long SpazioOccupato;
    private long SpazioMassimo;
    private boolean settingsAperte = false;
    private boolean nonMostrareToast = false;
    private StrutturaImmagini immagineImpostata;
    private int idImmagineImpostata = -1;
    private ImmagineZoomabile imgSfondoSettings;
    private boolean CambiaImmagine = true;
    private int TempoCambioImmagine = 30;
    private TextView txtNumeroImmagine;
    private long ultimaOperazioneTS;
    private boolean ceImmaginePerModifica = false;
    private StrutturaImmagini immagineVisualizzataPerModifica;
    private TextView txtNomeImmaginePerModifica;
    private List<Integer> idBraniAscoltati;
    private ListView lstArtisti;
    private ListView lstAlbum;
    private ListView lstBrani;
    private AdapterListenerArtisti customAdapterA;
    private ImageView imgImposta;
    private ImageView imgIndietroSfondo;
    private ImageView imgAvantiSfondo;
    private ImageView imgEliminaSfondo;
    private ImageView imgRefreshImmagini;
    private ImageView imgCondividi;
    private TextView txtPercentuale;
    private int Perc;

    // RICERCHE
    private boolean Random = true;
    private int StelleDaRicercare = 7;
    private boolean StelleSuperiori = true;
    private boolean RicercaMaiAscoltata = false;
    private boolean RicercaStelle = true;
    private String TestoDaRicercare = "";
    private String TestoDaNonRicercare = "";
    private boolean RicercaTesto = false;
    private String Preferiti = "";
    private String PreferitiElimina = "";
    private boolean AndOrPref = true;
    private boolean RicercaPreferiti = false;
    private String PreferitiTags = "";
    private String PreferitiEliminaTags = "";
    private boolean AndOrTags = true;
    private boolean RicercaTags = false;
    private boolean Date = false;
    private boolean DataSuperiore = false;
    private String TxtDataSuperiore = "";
    private boolean DataInferiore = false;
    private String TxtDataInferiore = "";
    private int idUltimoBrano;
    // RICERCHE

    public void ChiudeActivity(boolean Finish) {
        if (act != null) {
            // mainActivity.moveTaskToBack(true);
            // if (Finish) {
            act.finish();
            // }
        }
    }

    public int getPerc() {
        return Perc;
    }

    public void setPerc(int perc) {
        Perc = perc;
    }

    public TextView getTxtPercentuale() {
        return txtPercentuale;
    }

    public void setTxtPercentuale(TextView txtPercentuale) {
        this.txtPercentuale = txtPercentuale;
    }

    public ImageView getImgCondividi() {
        return imgCondividi;
    }

    public void setImgCondividi(ImageView imgCondividi) {
        this.imgCondividi = imgCondividi;
    }

    public ImageView getImgRefreshImmagini() {
        return imgRefreshImmagini;
    }

    public void setImgRefreshImmagini(ImageView imgRefreshImmagini) {
        this.imgRefreshImmagini = imgRefreshImmagini;
    }

    public ImageView getImgEliminaSfondo() {
        return imgEliminaSfondo;
    }

    public void setImgEliminaSfondo(ImageView imgEliminaSfondo) {
        this.imgEliminaSfondo = imgEliminaSfondo;
    }

    public ImageView getImgAvantiSfondo() {
        return imgAvantiSfondo;
    }

    public void setImgAvantiSfondo(ImageView imgAvantiSfondo) {
        this.imgAvantiSfondo = imgAvantiSfondo;
    }

    public ImageView getImgIndietroSfondo() {
        return imgIndietroSfondo;
    }

    public void setImgIndietroSfondo(ImageView imgIndietroSfondo) {
        this.imgIndietroSfondo = imgIndietroSfondo;
    }

    public ImageView getImgImposta() {
        return imgImposta;
    }

    public void setImgImposta(ImageView imgImposta) {
        this.imgImposta = imgImposta;
    }

    public AdapterListenerArtisti getCustomAdapterA() {
        return customAdapterA;
    }

    public void setCustomAdapterA(AdapterListenerArtisti customAdapterA) {
        this.customAdapterA = customAdapterA;
    }

    public ListView getLstAlbum() {
        return lstAlbum;
    }

    public void setLstAlbum(ListView lstAlbum) {
        this.lstAlbum = lstAlbum;
    }

    public ListView getLstArtisti() {
        return lstArtisti;
    }

    public void setLstArtisti(ListView lstArtisti) {
        this.lstArtisti = lstArtisti;
    }

    public ListView getLstBrani() {
        return lstBrani;
    }

    public void setLstBrani(ListView lstBrani) {
        this.lstBrani = lstBrani;
    }

    public List<Integer> getIdBraniAscoltati() {
        return idBraniAscoltati;
    }

    public void setIdBraniAscoltati(List<Integer> idBraniAscoltati) {
        this.idBraniAscoltati = idBraniAscoltati;
    }

    public int getIdUltimoBrano() {
        return idUltimoBrano;
    }

    public void setIdUltimoBrano(int idUltimoBrano) {
        this.idUltimoBrano = idUltimoBrano;
    }

    public boolean isRandom() {
        return Random;
    }

    public void setRandom(boolean random) {
        Random = random;
    }

    public TextView getTxtNomeImmaginePerModifica() {
        return txtNomeImmaginePerModifica;
    }

    public void setTxtNomeImmaginePerModifica(TextView txtNomeImmaginePerModifica) {
        this.txtNomeImmaginePerModifica = txtNomeImmaginePerModifica;
    }

    public StrutturaImmagini getImmagineVisualizzataPerModifica() {
        return immagineVisualizzataPerModifica;
    }

    public void setImmagineVisualizzataPerModifica(StrutturaImmagini immagineVisualizzataPerModifica) {
        this.immagineVisualizzataPerModifica = immagineVisualizzataPerModifica;
    }

    public boolean isCeImmaginePerModifica() {
        return ceImmaginePerModifica;
    }

    public void setCeImmaginePerModifica(boolean ceImmaginePerModifica) {
        this.ceImmaginePerModifica = ceImmaginePerModifica;
    }

    public long getUltimaOperazioneTS() {
        return ultimaOperazioneTS;
    }

    public void setUltimaOperazioneTS(long ultimaOperazioneTS) {
        this.ultimaOperazioneTS = ultimaOperazioneTS;
    }

    public TextView getTxtNumeroImmagine() {
        return txtNumeroImmagine;
    }

    public void setTxtNumeroImmagine(TextView txtNumeroImmagine) {
        this.txtNumeroImmagine = txtNumeroImmagine;
    }

    public int getIdImmagineImpostata() {
        return idImmagineImpostata;
    }

    public void setIdImmagineImpostata(int idImmagineImpostata) {
        this.idImmagineImpostata = idImmagineImpostata;
    }

    public int getTempoCambioImmagine() {
        return TempoCambioImmagine;
    }

    public void setTempoCambioImmagine(int tempoCambioImmagine) {
        TempoCambioImmagine = tempoCambioImmagine;
    }

    public boolean isCambiaImmagine() {
        return CambiaImmagine;
    }

    public void setCambiaImmagine(boolean cambiaImmagine) {
        CambiaImmagine = cambiaImmagine;
    }

    public ImmagineZoomabile getImgSfondoSettings() {
        return imgSfondoSettings;
    }

    public void setImgSfondoSettings(ImmagineZoomabile imgSfondoSettings) {
        this.imgSfondoSettings = imgSfondoSettings;
    }

    public StrutturaImmagini getImmagineImpostata() {
        return immagineImpostata;
    }

    public void setImmagineImpostata(StrutturaImmagini immagineImpostata) {
        this.immagineImpostata = immagineImpostata;
    }

    public boolean isNonMostrareToast() {
        return nonMostrareToast;
    }

    public void setNonMostrareToast(boolean nonMostrareToast) {
        this.nonMostrareToast = nonMostrareToast;
    }

    public boolean isSettingsAperte() {
        return settingsAperte;
    }

    public void setSettingsAperte(boolean settingsAperte) {
        this.settingsAperte = settingsAperte;
    }

    public long getSpazioOccupato() {
        return SpazioOccupato;
    }

    public void setSpazioOccupato(long spazioOccupato) {
        SpazioOccupato = spazioOccupato;
    }

    public long getSpazioMassimo() {
        return SpazioMassimo;
    }

    public void setSpazioMassimo(long spazioMassimo) {
        SpazioMassimo = spazioMassimo;
    }

    public DownloadImmagine getDownImmagine() {
        return DownImmagine;
    }

    public void setDownImmagine(DownloadImmagine downImmagine) {
        DownImmagine = downImmagine;
    }

    public DownloadCanzone getDownCanzone() {
        return DownCanzone;
    }

    public void setDownCanzone(DownloadCanzone downCanzone) {
        DownCanzone = downCanzone;
    }

    public InterrogazioneWSPlayer getClasseInterrogazione() {
        return ClasseInterrogazione;
    }

    public void setClasseInterrogazione(InterrogazioneWSPlayer classeInterrogazione) {
        ClasseInterrogazione = classeInterrogazione;
    }

    public boolean isCuffieInserite() {
        return CuffieInserite;
    }

    public void setCuffieInserite(boolean cuffieInserite) {
        CuffieInserite = cuffieInserite;
    }

    public TextView getTxtQuanteRicerca() {
        return txtQuanteRicerca;
    }

    public void setTxtQuanteRicerca(TextView txtQuanteRicerca) {
        this.txtQuanteRicerca = txtQuanteRicerca;
    }

    public List<String> getBraniSuSD() {
        return braniSuSD;
    }

    public void setBraniSuSD(List<String> braniSuSD) {
        this.braniSuSD = braniSuSD;
    }

    public float getLimiteInGb() {
        return limiteInGb;
    }

    public void setLimiteInGb(float limiteInBytes) {
        this.limiteInGb = limiteInBytes;
    }

    public boolean isPlayerAttivo() {
        return playerAttivo;
    }

    public void setPlayerAttivo(boolean playerAttivo) {
        this.playerAttivo = playerAttivo;
    }

    /*
    public void RimuovePrimaChiamata() {
        chiamate.remove(0);
    }

    public List<StrutturaChiamateWSPlayer> getChiamate() {
        return chiamate;
    }

    public void AggiungeChiamata(StrutturaChiamateWSPlayer c) {
        if (this.chiamate == null) {
            this.chiamate = new ArrayList<>();
        }
        this.chiamate.add(c);
    }
    public void setChiamate(List<StrutturaChiamateWSPlayer> chiamate) {
        this.chiamate = chiamate;
    } */

    public ChiamateWsPlayer getClasseChiamata() {
        return classeChiamata;
    }

    public void setClasseChiamata(ChiamateWsPlayer classeChiamata) {
        this.classeChiamata = classeChiamata;
    }

    public List<ImageView> getImgBellezza() {
        return imgBellezza;
    }

    public void setImgBellezza(List<ImageView> imgBellezza) {
        this.imgBellezza = imgBellezza;
    }

    public ImageView getImgCuffie() {
        return imgCuffie;
    }

    public void setImgCuffie(ImageView imgCuffie) {
        this.imgCuffie = imgCuffie;
    }

    public ImageView getImgCambiaPregresso() {
        return imgCambiaPregresso;
    }

    public void setImgCambiaPregresso(ImageView imgCambiaPregresso) {
        this.imgCambiaPregresso = imgCambiaPregresso;
    }

    public boolean isHaCaricatoBranoPregresso() {
        return haCaricatoBranoPregresso;
    }

    public void setHaCaricatoBranoPregresso(boolean haCaricatoBranoPregresso) {
        this.haCaricatoBranoPregresso = haCaricatoBranoPregresso;
    }

    public TextView getTxtBranoPregresso() {
        return txtBranoPregresso;
    }

    public void setTxtBranoPregresso(TextView txtBranoPregresso) {
        this.txtBranoPregresso = txtBranoPregresso;
    }

    public boolean isStaCaricandoBranoPregresso() {
        return staCaricandoBranoPregresso;
    }

    public void setStaCaricandoBranoPregresso(boolean staCaricandoBranoPregresso) {
        this.staCaricandoBranoPregresso = staCaricandoBranoPregresso;
    }

    public StrutturaBrano getStrutturaBranoPregressoCaricata() {
        return StrutturaBranoPregressoCaricata;
    }

    public void setStrutturaBranoPregressoCaricata(StrutturaBrano strutturaBranoPregressoCaricata) {
        StrutturaBranoPregressoCaricata = strutturaBranoPregressoCaricata;
    }

    public int getStavaSuonando() {
        return StavaSuonando;
    }

    public void setStavaSuonando(int stavaSuonando) {
        StavaSuonando = stavaSuonando;
    }

    /*
    public TextView getTxtInformazioniPlayer() {
        return txtInformazioniPlayer;
    }

    public void setTxtInformazioniPlayer(TextView txtInformazioniPlayer) {
        this.txtInformazioniPlayer = txtInformazioniPlayer;
    }
    */

    public String getInizioMinuti() {
        return inizioMinuti;
    }

    public void setInizioMinuti(String inizioMinuti) {
        this.inizioMinuti = inizioMinuti;
    }

    public String getFineMinuti() {
        return fineMinuti;
    }

    public void setFineMinuti(String fineMinuti) {
        this.fineMinuti = fineMinuti;
    }

    public boolean isMascheraNascosta() {
        return mascheraNascosta;
    }

    public void setMascheraNascosta(boolean mascheraNascosta) {
        this.mascheraNascosta = mascheraNascosta;
    }

    public boolean isFermaTimer() {
        return FermaTimer;
    }

    public void setFermaTimer(boolean fermaTimer) {
        FermaTimer = fermaTimer;
    }

    public int getSecondiPassati() {
        return SecondiPassati;
    }

    public void setSecondiPassati(int secondiPassati) {
        SecondiPassati = secondiPassati;
    }

    public int getDurataBranoInSecondi() {
        return DurataBranoInSecondi;
    }

    public void setDurataBranoInSecondi(int durataBranoInSecondi) {
        DurataBranoInSecondi = durataBranoInSecondi;
    }

    public SeekBar getSeekBarBrano() {
        return seekBarBrano;
    }

    public void setSeekBarBrano(SeekBar seekBarBrano) {
        this.seekBarBrano = seekBarBrano;
    }

    public TextView getTxtFine() {
        return txtFine;
    }

    public void setTxtFine(TextView txtFine) {
        this.txtFine = txtFine;
    }

    public TextView getTxtInizio() {
        return txtInizio;
    }

    public void setTxtInizio(TextView txtInizio) {
        this.txtInizio = txtInizio;
    }

    public String getPathUltimaImmagine() {
        return pathUltimaImmagine;
    }

    public void setPathUltimaImmagine(String pathUltimaImmagine) {
        this.pathUltimaImmagine = pathUltimaImmagine;
    }

    public TextView getTxtOperazione() {
        return txtOperazione;
    }

    public void setTxtOperazione(TextView txtOperazione) {
        this.txtOperazione = txtOperazione;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public TextView getTxtTitolo() {
        return txtTitolo;
    }

    public void setTxtTitolo(TextView txtTitolo) {
        this.txtTitolo = txtTitolo;
    }

    public ImageView getImgAvanti() {
        return imgAvanti;
    }

    public void setImgAvanti(ImageView imgAvanti) {
        this.imgAvanti = imgAvanti;
    }

    public ImageView getImgIndietro() {
        return imgIndietro;
    }

    public void setImgIndietro(ImageView imgIndietro) {
        this.imgIndietro = imgIndietro;
    }

    public ImageView getImgPlayStop() {
        return imgPlayStop;
    }

    public void setImgPlayStop(ImageView imgPlayStop) {
        this.imgPlayStop = imgPlayStop;
    }

    public ImmagineZoomabile getImgBrano() {
        return imgBrano;
    }

    public void setImgBrano(ImmagineZoomabile imgBrano) {
        this.imgBrano = imgBrano;
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }

    public StrutturaBrano getUltimoBrano() {
        return ultimoBrano;
    }

    public void setUltimoBrano(StrutturaBrano ultimoBrano) {
        this.ultimoBrano = ultimoBrano;
    }

    public int getBraniTotali() {
        return braniTotali;
    }

    public void setBraniTotali(int braniTotali) {
        this.braniTotali = braniTotali;
    }

    public void setIdNotifica(int idNotifica) {
        this.idNotifica = idNotifica;
    }

    public int getStelleDaRicercare() {
        return StelleDaRicercare;
    }

    public void setStelleDaRicercare(int stelleDaRicercare) {
        StelleDaRicercare = stelleDaRicercare;
    }

    public boolean isStelleSuperiori() {
        return StelleSuperiori;
    }

    public void setStelleSuperiori(boolean stelleSuperiori) {
        StelleSuperiori = stelleSuperiori;
    }

    public boolean isRicercaMaiAscoltata() {
        return RicercaMaiAscoltata;
    }

    public void setRicercaMaiAscoltata(boolean ricercaMaiAscoltata) {
        RicercaMaiAscoltata = ricercaMaiAscoltata;
    }

    public boolean isRicercaStelle() {
        return RicercaStelle;
    }

    public void setRicercaStelle(boolean ricercaStelle) {
        RicercaStelle = ricercaStelle;
    }

    public String getTestoDaRicercare() {
        return TestoDaRicercare;
    }

    public void setTestoDaRicercare(String testoDaRicercare) {
        TestoDaRicercare = testoDaRicercare;
    }

    public String getTestoDaNonRicercare() {
        return TestoDaNonRicercare;
    }

    public void setTestoDaNonRicercare(String testoDaNonRicercare) {
        TestoDaNonRicercare = testoDaNonRicercare;
    }

    public boolean isRicercaTesto() {
        return RicercaTesto;
    }

    public void setRicercaTesto(boolean ricercaTesto) {
        RicercaTesto = ricercaTesto;
    }

    public String getPreferiti() {
        return Preferiti;
    }

    public void setPreferiti(String preferiti) {
        Preferiti = preferiti;
    }

    public String getPreferitiElimina() {
        return PreferitiElimina;
    }

    public void setPreferitiElimina(String preferitiElimina) {
        PreferitiElimina = preferitiElimina;
    }

    public boolean isAndOrPref() {
        return AndOrPref;
    }

    public void setAndOrPref(boolean andOrPref) {
        AndOrPref = andOrPref;
    }

    public boolean isRicercaPreferiti() {
        return RicercaPreferiti;
    }

    public void setRicercaPreferiti(boolean ricercaPreferiti) {
        RicercaPreferiti = ricercaPreferiti;
    }

    public String getPreferitiTags() {
        return PreferitiTags;
    }

    public void setPreferitiTags(String preferitiTags) {
        PreferitiTags = preferitiTags;
    }

    public String getPreferitiEliminaTags() {
        return PreferitiEliminaTags;
    }

    public void setPreferitiEliminaTags(String preferitiEliminaTags) {
        PreferitiEliminaTags = preferitiEliminaTags;
    }

    public boolean isAndOrTags() {
        return AndOrTags;
    }

    public void setAndOrTags(boolean andOrTags) {
        AndOrTags = andOrTags;
    }

    public boolean isRicercaTags() {
        return RicercaTags;
    }

    public void setRicercaTags(boolean ricercaTags) {
        RicercaTags = ricercaTags;
    }

    public boolean isDate() {
        return Date;
    }

    public void setDate(boolean date) {
        Date = date;
    }

    public boolean isDataSuperiore() {
        return DataSuperiore;
    }

    public void setDataSuperiore(boolean dataSuperiore) {
        DataSuperiore = dataSuperiore;
    }

    public String getTxtDataSuperiore() {
        return TxtDataSuperiore;
    }

    public void setTxtDataSuperiore(String txtDataSuperiore) {
        TxtDataSuperiore = txtDataSuperiore;
    }

    public boolean isDataInferiore() {
        return DataInferiore;
    }

    public void setDataInferiore(boolean dataInferiore) {
        DataInferiore = dataInferiore;
    }

    public String getTxtDataInferiore() {
        return TxtDataInferiore;
    }

    public void setTxtDataInferiore(String txtDataInferiore) {
        TxtDataInferiore = txtDataInferiore;
    }

    public StrutturaUtenti getUtente() {
        return Utente;
    }

    public void setUtente(StrutturaUtenti utente) {
        Utente = utente;
    }

    public boolean isStaSuonando() {
        return staSuonando;
    }

    public void setStaSuonando(boolean staSuonando) {
        this.staSuonando = staSuonando;
    }

    public int getIdNotifica() {
        return idNotifica;
    }

    public boolean isGiaPartito() {
        return giaPartito;
    }

    public void setGiaPartito(boolean giaPartito) {
        this.giaPartito = giaPartito;
    }

    public Activity getAct() {
        return act;
    }

    public void setAct(Activity act) {
        this.act = act;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
