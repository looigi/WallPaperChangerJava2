package com.looigi.wallpaperchanger2.classePreview.classeOCR;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classePreview.strutture.StrutturaVoltiRilevati;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheOCR {
    private static VariabiliStaticheOCR instance = null;

    private VariabiliStaticheOCR() {
    }

    public static VariabiliStaticheOCR getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheOCR();
        }

        return instance;
    }

    private GifImageView imgCaricamento;
    private boolean ancheDestinazioniVuote = false;
    private ListView lstDestinazioni;
    private ListView lstImmagini;
    private String idImmagineDaSpostare;
    private String FiltroPremuto;
    private List<StrutturaImmaginiCategorie> listaCategorie = new ArrayList<>();
    private int UltimoValoreSelezionatoSpinner = -1;

    public int getUltimoValoreSelezionatoSpinner() {
        return UltimoValoreSelezionatoSpinner;
    }

    public void setUltimoValoreSelezionatoSpinner(int ultimoValoreSelezionatoSpinner) {
        UltimoValoreSelezionatoSpinner = ultimoValoreSelezionatoSpinner;
    }

    public List<StrutturaImmaginiCategorie> getListaCategorie() {
        return listaCategorie;
    }

    public void setListaCategorie(List<StrutturaImmaginiCategorie> listaCategorie) {
        this.listaCategorie = listaCategorie;
    }

    public String getFiltroPremuto() {
        return FiltroPremuto;
    }

    public void setFiltroPremuto(String filtroPremuto) {
        FiltroPremuto = filtroPremuto;
    }

    public String getIdImmagineDaSpostare() {
        return idImmagineDaSpostare;
    }

    public void setIdImmagineDaSpostare(String idImmagineDaSpostare) {
        this.idImmagineDaSpostare = idImmagineDaSpostare;
    }

    public ListView getLstDestinazioni() {
        return lstDestinazioni;
    }

    public void setLstDestinazioni(ListView lstDestinazioni) {
        this.lstDestinazioni = lstDestinazioni;
    }

    public ListView getLstImmagini() {
        return lstImmagini;
    }

    public void setLstImmagini(ListView lstImmagini) {
        this.lstImmagini = lstImmagini;
    }

    public boolean isAncheDestinazioniVuote() {
        return ancheDestinazioniVuote;
    }

    public void setAncheDestinazioniVuote(boolean ancheDestinazioniVuote) {
        this.ancheDestinazioniVuote = ancheDestinazioniVuote;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }
}
