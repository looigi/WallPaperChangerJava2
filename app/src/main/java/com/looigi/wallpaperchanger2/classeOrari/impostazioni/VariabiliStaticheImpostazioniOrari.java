package com.looigi.wallpaperchanger2.classeOrari.impostazioni;

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

public class VariabiliStaticheImpostazioniOrari {
    private static VariabiliStaticheImpostazioniOrari instance = null;

    private VariabiliStaticheImpostazioniOrari() {
    }

    public static VariabiliStaticheImpostazioniOrari getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheImpostazioniOrari();
        }

        return instance;
    }

    public static final String UrlWS = "http://www.wsorari.looigi.it/"; // "http://looigi.no-ip.biz:1071/";
    private GifImageView imgCaricamento;
    private LinearLayout layGestione;
    private String Modalita;

    private int idPortata;
    private LinearLayout layPortata;
    private EditText edtPortata;

    public int getIdPortata() {
        return idPortata;
    }

    public void setIdPortata(int idPortata) {
        this.idPortata = idPortata;
    }

    public EditText getEdtPortata() {
        return edtPortata;
    }

    public void setEdtPortata(EditText edtPortata) {
        this.edtPortata = edtPortata;
    }

    public LinearLayout getLayPortata() {
        return layPortata;
    }

    public void setLayPortata(LinearLayout layPortata) {
        this.layPortata = layPortata;
    }

    public String getModalita() {
        return Modalita;
    }

    public void setModalita(String modalita) {
        Modalita = modalita;
    }

    public LinearLayout getLayGestione() {
        return layGestione;
    }

    public void setLayGestione(LinearLayout layGestione) {
        this.layGestione = layGestione;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }
}
