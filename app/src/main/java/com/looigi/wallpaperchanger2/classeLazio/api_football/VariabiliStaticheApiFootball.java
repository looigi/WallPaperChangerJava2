package com.looigi.wallpaperchanger2.classeLazio.api_football;

import android.widget.ListView;

import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Allenatori.Allenatori;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.GiocatoriPartita;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partita.Partita;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partite.Partite;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Squadre.StrutturaSquadreLega;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheApiFootball {
    private static VariabiliStaticheApiFootball instance = null;

    private VariabiliStaticheApiFootball() {
    }

    public static VariabiliStaticheApiFootball getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheApiFootball();
        }

        return instance;
    }

    public static final int idLegaSerieA = 135;

    private GifImageView imgCaricamento;
    private String PathApiFootball;
    private boolean staLeggendoWS;
    private int AnnoScelto;
    private ListView lstSquadre;
    private ListView lstPartite;
    private ListView lstGiocatoriCasa;
    private ListView lstGiocatoriFuori;
    private int idSquadra;
    private int idPartita;

    // Dati
    private StrutturaSquadreLega listaSquadreAnno;
    private Allenatori AllenatoriSquadraScelta;
    private Partite PartiteSquadra;
    private Partita PartitaSelezionata;
    private GiocatoriPartita GiocatoriDellaPartita;
    // Dati

    public ListView getLstGiocatoriFuori() {
        return lstGiocatoriFuori;
    }

    public void setLstGiocatoriFuori(ListView lstGiocatoriFuori) {
        this.lstGiocatoriFuori = lstGiocatoriFuori;
    }

    public ListView getLstGiocatoriCasa() {
        return lstGiocatoriCasa;
    }

    public void setLstGiocatoriCasa(ListView lstGiocatoriCasa) {
        this.lstGiocatoriCasa = lstGiocatoriCasa;
    }

    public Allenatori getAllenatoriSquadraScelta() {
        return AllenatoriSquadraScelta;
    }

    public void setAllenatoriSquadraScelta(Allenatori allenatoriSquadraScelta) {
        AllenatoriSquadraScelta = allenatoriSquadraScelta;
    }

    public GiocatoriPartita getGiocatoriDellaPartita() {
        return GiocatoriDellaPartita;
    }

    public void setGiocatoriDellaPartita(GiocatoriPartita giocatoriDellaPartita) {
        GiocatoriDellaPartita = giocatoriDellaPartita;
    }

    public Partite getPartiteSquadra() {
        return PartiteSquadra;
    }

    public void setPartiteSquadra(Partite partiteSquadra) {
        PartiteSquadra = partiteSquadra;
    }

    public Partita getPartitaSelezionata() {
        return PartitaSelezionata;
    }

    public void setPartitaSelezionata(Partita partitaSelezionata) {
        PartitaSelezionata = partitaSelezionata;
    }

    public int getIdSquadra() {
        return idSquadra;
    }

    public void setIdSquadra(int idSquadra) {
        this.idSquadra = idSquadra;
    }

    public int getIdPartita() {
        return idPartita;
    }

    public void setIdPartita(int idPartita) {
        this.idPartita = idPartita;
    }

    public ListView getLstPartite() {
        return lstPartite;
    }

    public void setLstPartite(ListView lstPartite) {
        this.lstPartite = lstPartite;
    }

    public ListView getLstSquadre() {
        return lstSquadre;
    }

    public void setLstSquadre(ListView lstSquadre) {
        this.lstSquadre = lstSquadre;
    }

    public StrutturaSquadreLega getListaSquadreAnno() {
        return listaSquadreAnno;
    }

    public void setListaSquadreAnno(StrutturaSquadreLega listaSquadreAnno) {
        this.listaSquadreAnno = listaSquadreAnno;
    }

    public int getAnnoScelto() {
        return AnnoScelto;
    }

    public void setAnnoScelto(int annoScelto) {
        AnnoScelto = annoScelto;
    }

    public boolean isStaLeggendoWS() {
        return staLeggendoWS;
    }

    public void setStaLeggendoWS(boolean staLeggendoWS) {
        this.staLeggendoWS = staLeggendoWS;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public String getPathApiFootball() {
        return PathApiFootball;
    }

    public void setPathApiFootball(String pathApiFootball) {
        PathApiFootball = pathApiFootball;
    }
}
