package com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheVolti {
    private static VariabiliStaticheVolti instance = null;

    private VariabiliStaticheVolti() {
    }

    public static VariabiliStaticheVolti getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheVolti();
        }

        return instance;
    }

    private GifImageView imgCaricamento;
    private List<StrutturaVoltiRilevati> listaVolti = new ArrayList<>();
    private ListView lstVolti;
    private LinearLayout layPreview;
    private ImmagineZoomabile imgPreview;

    public LinearLayout getLayPreview() {
        return layPreview;
    }

    public void setLayPreview(LinearLayout layPreview) {
        this.layPreview = layPreview;
    }

    public ImmagineZoomabile getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(ImmagineZoomabile imgPreview) {
        this.imgPreview = imgPreview;
    }

    public ListView getLstVolti() {
        return lstVolti;
    }

    public void setLstVolti(ListView lstVolti) {
        this.lstVolti = lstVolti;
    }

    public List<StrutturaVoltiRilevati> getListaVolti() {
        return listaVolti;
    }

    public void setListaVolti(List<StrutturaVoltiRilevati> listaVolti) {
        this.listaVolti = listaVolti;
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
