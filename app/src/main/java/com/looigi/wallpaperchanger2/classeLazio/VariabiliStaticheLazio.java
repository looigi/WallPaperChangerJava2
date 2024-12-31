package com.looigi.wallpaperchanger2.classeLazio;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaCommesse;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDati;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDatiGiornata;

import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheLazio {
    private static VariabiliStaticheLazio instance = null;

    private VariabiliStaticheLazio() {
    }

    public static VariabiliStaticheLazio getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheLazio();
        }

        return instance;
    }

    public static final String UrlWS = "http://www.wsorari.looigi.it/"; // "http://looigi.no-ip.biz:1071/";
    private GifImageView imgCaricamento;

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }
}
