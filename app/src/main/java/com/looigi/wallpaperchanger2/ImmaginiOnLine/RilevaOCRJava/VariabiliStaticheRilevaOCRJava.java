package com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.strutture.StrutturaRilevaOCR;

public class VariabiliStaticheRilevaOCRJava {
    private static VariabiliStaticheRilevaOCRJava instance = null;

    private VariabiliStaticheRilevaOCRJava() {
    }

    public static VariabiliStaticheRilevaOCRJava getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheRilevaOCRJava();
        }

        return instance;
    }

    private Context context;
    private ImageView imgCaricamento;
    private StrutturaRilevaOCR immagineAttuale;
    private ImageView imgImmagine;
    private boolean StaElaborando = false;
    private TextView txtAvanzamento;
    // private String idUltimaImmagine = "";
    private boolean GiaEntrato = false;
    public static String channelName = "Rileva OCR";
    public static String NOTIFICATION_CHANNEL_STRING = "com.looigi.wallpaperchanger2";
    public static int NOTIFICATION_CHANNEL_ID = 17;
    private String MessaggioNotifica = "";
    private int Contatore;
    private int Fatte = 0;

    public int getFatte() {
        return Fatte;
    }

    public void setFatte(int fatte) {
        Fatte = fatte;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getContatore() {
        return Contatore;
    }

    public void setContatore(int contatore) {
        Contatore = contatore;
    }

    public String getMessaggioNotifica() {
        return MessaggioNotifica;
    }

    public void setMessaggioNotifica(String messaggioNotifica) {
        MessaggioNotifica = messaggioNotifica;
    }

    public boolean isGiaEntrato() {
        return GiaEntrato;
    }

    public void setGiaEntrato(boolean giaEntrato) {
        GiaEntrato = giaEntrato;
    }

    /* public String getIdUltimaImmagine() {
        return idUltimaImmagine;
    }

    public void setIdUltimaImmagine(String idUltimaImmagine) {
        this.idUltimaImmagine = idUltimaImmagine;
    } */

    public TextView getTxtAvanzamento() {
        return txtAvanzamento;
    }

    public void setTxtAvanzamento(TextView txtAvanzamento) {
        this.txtAvanzamento = txtAvanzamento;
    }

    public boolean isStaElaborando() {
        return StaElaborando;
    }

    public void setStaElaborando(boolean staElaborando) {
        StaElaborando = staElaborando;
    }

    public ImageView getImgImmagine() {
        return imgImmagine;
    }

    public void setImgImmagine(ImageView imgImmagine) {
        this.imgImmagine = imgImmagine;
    }

    public StrutturaRilevaOCR getImmagineAttuale() {
        return immagineAttuale;
    }

    public void setImmagineAttuale(StrutturaRilevaOCR immagineAttuale) {
        this.immagineAttuale = immagineAttuale;
    }

    public ImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(ImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }
}
