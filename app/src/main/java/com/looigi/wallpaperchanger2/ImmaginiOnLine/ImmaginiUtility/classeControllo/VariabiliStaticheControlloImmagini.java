package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.classeControllo;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ListView;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.strutture.StrutturaControlloImmagini;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheControlloImmagini {
    private static VariabiliStaticheControlloImmagini instance = null;

    private VariabiliStaticheControlloImmagini() {
    }

    public static VariabiliStaticheControlloImmagini getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheControlloImmagini();
        }

        return instance;
    }

    private GifImageView imgCaricamento;
    private int idCategoria;
    private StrutturaControlloImmagini StrutturaDati;
    private ListView lstLista;
    private String Tipologia;
    private String Categoria;
    // private LinearLayout layPreview;
    // private ImmagineZoomabile imgPreview;

    /* public ImmagineZoomabile getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(ImmagineZoomabile imgPreview) {
        this.imgPreview = imgPreview;
    }

    public LinearLayout getLayPreview() {
        return layPreview;
    }

    public void setLayPreview(LinearLayout layPreview) {
        this.layPreview = layPreview;
    }
    */

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getTipologia() {
        return Tipologia;
    }

    public void setTipologia(String tipologia) {
        Tipologia = tipologia;
    }

    public ListView getLstLista() {
        return lstLista;
    }

    public void setLstLista(ListView lstLista) {
        this.lstLista = lstLista;
    }

    public StrutturaControlloImmagini getStrutturaDati() {
        return StrutturaDati;
    }

    public void setStrutturaDati(StrutturaControlloImmagini strutturaDati) {
        StrutturaDati = strutturaDati;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public void Attesa(boolean bAttesa) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bAttesa) {
                    imgCaricamento.setVisibility(View.VISIBLE);
                } else {
                    imgCaricamento.setVisibility(View.GONE);
                }
            }
        }, 10);
    }
}
