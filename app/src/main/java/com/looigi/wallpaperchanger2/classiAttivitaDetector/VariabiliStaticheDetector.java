package com.looigi.wallpaperchanger2.classiAttivitaDetector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

import com.looigi.wallpaperchanger2.classiStandard.LogInterno;

import java.util.List;

public class VariabiliStaticheDetector {
    private static VariabiliStaticheDetector instance = null;

    private VariabiliStaticheDetector() {
    }

    public static VariabiliStaticheDetector getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheDetector();
        }

        return instance;
    }

    private Context context;
    private Activity mainActivity;
    private boolean ciSonoPermessi = false;
    // private LogInterno l;
    private int errori = 0;
    private int idNotifica = 111113;
    public static String channelName = "DetectorII";
    public static String NOTIFICATION_CHANNEL_STRING = "com.looigi.detector2";
    public static int NOTIFICATION_CHANNEL_ID = 3;
    public static int channelIdIntentOverlay = 153;
    // private String NomeFileDiLog = "";
    private String NomeLogServizio = "";
    private Intent servizioForeground;
    // private String PercorsoDIRLog = "";
    private long secondiDiAttesaContatore = 60L;
    // private Camera2 Camera;
    private boolean FaiLog=false;
    private int TipologiaScatto;
    private int Secondi = 3;
    private int Fotocamera;
    private String Risoluzione = "";
    private int Estensione;
    private int NumeroScatti;
    private String Anteprima;
    private int Orientamento;
    private String Lingua;
    private boolean Vibrazione=true;
    private int DimensioniThumbs=70;
    private int DimensioniThumbsM=50;
    private boolean VisualizzaToast = true;
    private List<String> Dimensioni;
    private TextView txtCoords;
    private String ModelloTelefono="";
    // private ListView Lista;
    private TextView txtImm;
    private TextView txtNomeImm;
    private ImageView img;
    private ImageView audio;
    private VideoView vView;
    public boolean StaVedendo=false;
    public boolean StaSuonando=false;
    // private boolean StoScattando = false;
    public boolean MascheraImmaginiMostrata = false;
    private ImageView btnRuotaSin;
    private ImageView btnRuotaDes;
    private ImageView btnFlipX;
    private ImageView btnFlipY;
    private List<String> Immagini;
    public Integer numMultimedia;
    public Integer totImmagini;
    private MediaPlayer mp;
    private boolean letteImpostazioni = false;
    private boolean MascheraPartita = false;
    private boolean chiudiActivity = false;
    private boolean CameraImpostata = false;
    private boolean GpsPreciso = false;

    public void ChiudeActivity(boolean Finish) {
        if (mainActivity != null) {
            // mainActivity.moveTaskToBack(true);
            // if (Finish) {
                mainActivity.finish();
            // }
        }
    }

    public boolean isGpsPreciso() {
        return GpsPreciso;
    }

    public void setGpsPreciso(boolean gpsPreciso) {
        GpsPreciso = gpsPreciso;
    }

    public boolean isCameraImpostata() {
        return CameraImpostata;
    }

    public void setCameraImpostata(boolean cameraImpostata) {
        CameraImpostata = cameraImpostata;
    }

    public boolean isChiudiActivity() {
        return chiudiActivity;
    }

    public void setChiudiActivity(boolean chiudiActivity) {
        this.chiudiActivity = chiudiActivity;
    }

    public boolean isMascheraPartita() {
        return MascheraPartita;
    }

    public void setMascheraPartita(boolean mascheraPartita) {
        MascheraPartita = mascheraPartita;
    }

    public int getErrori() {
        return errori;
    }

    public void setErrori(int errori) {
        this.errori = errori;
    }

    public boolean isLetteImpostazioni() {
        return letteImpostazioni;
    }

    public void setLetteImpostazioni(boolean letteImpostazioni) {
        this.letteImpostazioni = letteImpostazioni;
    }

    public MediaPlayer getMp() {
        return mp;
    }

    public void setMp(MediaPlayer mp) {
        this.mp = mp;
    }

    /* public boolean isStoScattando() {
        return StoScattando;
    }

    public void setStoScattando(boolean stoScattando) {
        StoScattando = stoScattando;
    } */

    public List<String> getImmagini() {
        return Immagini;
    }

    public void setImmagini(List<String> immagini) {
        Immagini = immagini;
    }

    public Integer getNumMultimedia() {
        return numMultimedia;
    }

    public void setNumMultimedia(Integer numMultimedia) {
        this.numMultimedia = numMultimedia;
    }

    public Integer getTotImmagini() {
        return totImmagini;
    }

    public void setTotImmagini(Integer totImmagini) {
        this.totImmagini = totImmagini;
    }

    public ImageView getBtnRuotaSin() {
        return btnRuotaSin;
    }

    public void setBtnRuotaSin(ImageView btnRuotaSin) {
        this.btnRuotaSin = btnRuotaSin;
    }

    public ImageView getBtnRuotaDes() {
        return btnRuotaDes;
    }

    public void setBtnRuotaDes(ImageView btnRuotaDes) {
        this.btnRuotaDes = btnRuotaDes;
    }

    public ImageView getBtnFlipX() {
        return btnFlipX;
    }

    public void setBtnFlipX(ImageView btnFlipX) {
        this.btnFlipX = btnFlipX;
    }

    public ImageView getBtnFlipY() {
        return btnFlipY;
    }

    public void setBtnFlipY(ImageView btnFlipY) {
        this.btnFlipY = btnFlipY;
    }

    public boolean isMascheraImmaginiMostrata() {
        return MascheraImmaginiMostrata;
    }

    public void setMascheraImmaginiMostrata(boolean mascheraImmaginiMostrata) {
        MascheraImmaginiMostrata = mascheraImmaginiMostrata;
    }

    public boolean isStaSuonando() {
        return StaSuonando;
    }

    public void setStaSuonando(boolean staSuonando) {
        StaSuonando = staSuonando;
    }

    public boolean isStaVedendo() {
        return StaVedendo;
    }

    public void setStaVedendo(boolean staVedendo) {
        StaVedendo = staVedendo;
    }

    public TextView getTxtImm() {
        return txtImm;
    }

    public void setTxtImm(TextView txtImm) {
        this.txtImm = txtImm;
    }

    public TextView getTxtNomeImm() {
        return txtNomeImm;
    }

    public void setTxtNomeImm(TextView txtNomeImm) {
        this.txtNomeImm = txtNomeImm;
    }

    public ImageView getImg() {
        return img;
    }

    public void setImg(ImageView img) {
        this.img = img;
    }

    public ImageView getAudio() {
        return audio;
    }

    public void setAudio(ImageView audio) {
        this.audio = audio;
    }

    public VideoView getvView() {
        return vView;
    }

    public void setvView(VideoView vView) {
        this.vView = vView;
    }

    /* public ListView getLista() {
        return Lista;
    }

    public void setLista(ListView lista) {
        Lista = lista;
    } */

    public String getModelloTelefono() {
        return ModelloTelefono;
    }

    public void setModelloTelefono(String modelloTelefono) {
        ModelloTelefono = modelloTelefono;
    }

    public TextView getTxtCoords() {
        return txtCoords;
    }

    public void setTxtCoords(TextView txtCoords) {
        this.txtCoords = txtCoords;
    }

    public List<String> getDimensioni() {
        return Dimensioni;
    }

    public void setDimensioni(List<String> dimensioni) {
        Dimensioni = dimensioni;
    }

    public boolean isVibrazione() {
        return Vibrazione;
    }

    public void setVibrazione(boolean vibrazione) {
        Vibrazione = vibrazione;
    }

    public int getDimensioniThumbs() {
        return DimensioniThumbs;
    }

    public void setDimensioniThumbs(int dimensioniThumbs) {
        DimensioniThumbs = dimensioniThumbs;
    }

    public int getDimensioniThumbsM() {
        return DimensioniThumbsM;
    }

    public void setDimensioniThumbsM(int dimensioniThumbsM) {
        DimensioniThumbsM = dimensioniThumbsM;
    }

    public boolean isVisualizzaToast() {
        return VisualizzaToast;
    }

    public void setVisualizzaToast(boolean visualizzaToast) {
        VisualizzaToast = visualizzaToast;
    }

    public boolean isFaiLog() {
        return FaiLog;
    }

    public void setFaiLog(boolean faiLog) {
        FaiLog = faiLog;
    }

    public int getTipologiaScatto() {
        return TipologiaScatto;
    }

    public void setTipologiaScatto(int tipologiaScatto) {
        TipologiaScatto = tipologiaScatto;
    }

    public int getSecondi() {
        return Secondi;
    }

    public void setSecondi(int secondi) {
        Secondi = secondi;
    }

    public int getFotocamera() {
        return Fotocamera;
    }

    public void setFotocamera(int fotocamera) {
        Fotocamera = fotocamera;
    }

    public String getRisoluzione() {
        return Risoluzione;
    }

    public void setRisoluzione(String risoluzione) {
        Risoluzione = risoluzione;
    }

    public int getEstensione() {
        return Estensione;
    }

    public void setEstensione(int estensione) {
        Estensione = estensione;
    }

    public int getNumeroScatti() {
        return NumeroScatti;
    }

    public void setNumeroScatti(int numeroScatti) {
        NumeroScatti = numeroScatti;
    }

    public String getAnteprima() {
        return Anteprima;
    }

    public void setAnteprima(String anteprima) {
        Anteprima = anteprima;
    }

    public int getOrientamento() {
        return Orientamento;
    }

    public void setOrientamento(int orientamento) {
        Orientamento = orientamento;
    }

    public String getLingua() {
        return Lingua;
    }

    public void setLingua(String lingua) {
        Lingua = lingua;
    }

    public String getNomeLogServizio() {
        return NomeLogServizio;
    }

    public void setNomeLogServizio(String nomeLogServizio) {
        NomeLogServizio = nomeLogServizio;
    }

    /* public Camera2 getCamera() {
        return Camera;
    }

    public void setCamera(Camera2 camera) {
        Camera = camera;
    } */

    public long getSecondiDiAttesaContatore() {
        return secondiDiAttesaContatore;
    }

    public void setSecondiDiAttesaContatore(long secondiDiAttesaContatore) {
        this.secondiDiAttesaContatore = secondiDiAttesaContatore;
    }

    /* public String getNomeFileDiLog() {
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
    } */

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