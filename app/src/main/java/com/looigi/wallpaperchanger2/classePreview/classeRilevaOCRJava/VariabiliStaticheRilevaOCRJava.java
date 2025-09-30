package com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava.strutture.StrutturaRilevaOCR;

import pl.droidsonroids.gif.GifImageView;

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

    private GifImageView imgCaricamento;
    private StrutturaRilevaOCR immagineAttuale;
    private ImageView imgImmagine;
    private boolean StaElaborando = false;
    private TextView txtAvanzamento;
    private String idUltimaImmagine = "";
    private boolean GiaEntrato = false;

    public boolean isGiaEntrato() {
        return GiaEntrato;
    }

    public void setGiaEntrato(boolean giaEntrato) {
        GiaEntrato = giaEntrato;
    }

    public String getIdUltimaImmagine() {
        return idUltimaImmagine;
    }

    public void setIdUltimaImmagine(String idUltimaImmagine) {
        this.idUltimaImmagine = idUltimaImmagine;
    }

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

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }
}
