package com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita;

import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaAllenatori;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaAnni;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCalendario;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaClassifica;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCompetizioni;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaFonti;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaGiocatori;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaMercato;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaRuoli;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaSquadre;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaStati;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerAllenatori;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerClassifica;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerGiocatori;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerMercato;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheLazioDettaglio {
    private static VariabiliStaticheLazioDettaglio instance = null;

    private VariabiliStaticheLazioDettaglio() {
    }

    public static VariabiliStaticheLazioDettaglio getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheLazioDettaglio();
        }

        return instance;
    }

    public static final String UrlWS = "http://www.wslazio.looigi.it/";
    public static final String UrlMedia = "http://www.totomiomedia.looigi.it/StemmiSquadre/";
    private GifImageView imgCaricamento;
    private String PathLazio;
    private int idPartita;
    private int idSquadraCasa;
    private int idSquadraFuori;
    private String Casa;
    private String Fuori;
    private String Risultato;
    private String Data;
    private List<StrutturaGiocatori> GiocatoriCasa = new ArrayList<>();
    private List<StrutturaGiocatori> GiocatoriFuori = new ArrayList<>();
    private String[] GiocatoriCasaPerSpn;
    private String[] GiocatoriFuoriPerSpn;

    public String[] getGiocatoriCasaPerSpn() {
        return GiocatoriCasaPerSpn;
    }

    public void setGiocatoriCasaPerSpn(String[] giocatoriCasaPerSpn) {
        GiocatoriCasaPerSpn = giocatoriCasaPerSpn;
    }

    public String[] getGiocatoriFuoriPerSpn() {
        return GiocatoriFuoriPerSpn;
    }

    public void setGiocatoriFuoriPerSpn(String[] giocatoriFuoriPerSpn) {
        GiocatoriFuoriPerSpn = giocatoriFuoriPerSpn;
    }

    public List<StrutturaGiocatori> getGiocatoriCasa() {
        return GiocatoriCasa;
    }

    public void setGiocatoriCasa(List<StrutturaGiocatori> giocatoriCasa) {
        GiocatoriCasa = giocatoriCasa;
    }

    public List<StrutturaGiocatori> getGiocatoriFuori() {
        return GiocatoriFuori;
    }

    public void setGiocatoriFuori(List<StrutturaGiocatori> giocatoriFuori) {
        GiocatoriFuori = giocatoriFuori;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getCasa() {
        return Casa;
    }

    public void setCasa(String casa) {
        Casa = casa;
    }

    public String getFuori() {
        return Fuori;
    }

    public void setFuori(String fuori) {
        Fuori = fuori;
    }

    public String getRisultato() {
        return Risultato;
    }

    public void setRisultato(String risultato) {
        Risultato = risultato;
    }

    public int getIdPartita() {
        return idPartita;
    }

    public void setIdPartita(int idPartita) {
        this.idPartita = idPartita;
    }

    public int getIdSquadraCasa() {
        return idSquadraCasa;
    }

    public void setIdSquadraCasa(int idSquadraCasa) {
        this.idSquadraCasa = idSquadraCasa;
    }

    public int getIdSquadraFuori() {
        return idSquadraFuori;
    }

    public void setIdSquadraFuori(int idSquadraFuori) {
        this.idSquadraFuori = idSquadraFuori;
    }

    public String getPathLazio() {
        return PathLazio;
    }

    public void setPathLazio(String pathLazio) {
        PathLazio = pathLazio;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }
}
