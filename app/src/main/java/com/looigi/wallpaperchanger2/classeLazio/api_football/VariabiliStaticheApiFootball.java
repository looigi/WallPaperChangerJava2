package com.looigi.wallpaperchanger2.classeLazio.api_football;

import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaGiocatori;
import com.looigi.wallpaperchanger2.classeLazio.api_football.adapters.AdapterListenerPartiteAF;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Allenatori.Allenatori;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.GiocatoriPartita;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partita.Partita;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partite.FixtureData;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partite.Partite;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Squadre.StrutturaSquadreLega;

import java.util.List;

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
    private String AnnoScelto;
    private int AnnoIniziale;
    private ListView lstSquadre;
    private ListView lstPartite;
    private ListView lstGiocatoriCasa;
    private ListView lstGiocatoriFuori;
    private int idSquadra;
    private int idPartita;
    private int idAnnoScelto;
    private String NomeSquadraScelta;
    private TextView txtAvanzamento;

    private int indiceSalvataggioTutteLeSquadre;
    private boolean staSalvandoTutteLeSquadre;
    private int squadreAggiunte;

    private boolean staSalvandoPartita;
    private int idPartitaDaSalvare;
    private int idPartitaSalvata;
    private List<StrutturaGiocatori> GiocatoriCasaPS;
    private List<StrutturaGiocatori> GiocatoriFuoriPS;
    private boolean staSalvandoTutteLePartite;
    private int indiceSalvataggioTutteLePartite;
    private String SquadraDiCasaDaSalvare;
    private String SquadraFuoriDaSalvare;
    private int idTipologiaDaSalvare;
    private int SquadraCheStaSalvando = -1;
    private int QuantePartite;
    private AdapterListenerPartiteAF customAdapterPartiteAF;

    // Dati
    private StrutturaSquadreLega listaSquadreAnno;
    private Allenatori AllenatoriSquadraScelta;
    private Partite PartiteSquadra;
    private Partita PartitaSelezionata;
    private GiocatoriPartita GiocatoriDellaPartita;
    // Dati

    public AdapterListenerPartiteAF getCustomAdapterPartiteAF() {
        return customAdapterPartiteAF;
    }

    public void setCustomAdapterPartiteAF(AdapterListenerPartiteAF customAdapterPartiteAF) {
        this.customAdapterPartiteAF = customAdapterPartiteAF;
    }

    public int getQuantePartite() {
        return QuantePartite;
    }

    public void setQuantePartite(int quantePartite) {
        QuantePartite = quantePartite;
    }

    public TextView getTxtAvanzamento() {
        return txtAvanzamento;
    }

    public void setTxtAvanzamento(TextView txtAvanzamento) {
        this.txtAvanzamento = txtAvanzamento;
    }

    public int getSquadraCheStaSalvando() {
        return SquadraCheStaSalvando;
    }

    public void setSquadraCheStaSalvando(int squadraCheStaSalvando) {
        SquadraCheStaSalvando = squadraCheStaSalvando;
    }

    public int getIdTipologiaDaSalvare() {
        return idTipologiaDaSalvare;
    }

    public void setIdTipologiaDaSalvare(int idTipologiaDaSalvare) {
        this.idTipologiaDaSalvare = idTipologiaDaSalvare;
    }

    public String getSquadraDiCasaDaSalvare() {
        return SquadraDiCasaDaSalvare;
    }

    public void setSquadraDiCasaDaSalvare(String squadraDiCasaDaSalvare) {
        SquadraDiCasaDaSalvare = squadraDiCasaDaSalvare;
    }

    public String getSquadraFuoriDaSalvare() {
        return SquadraFuoriDaSalvare;
    }

    public void setSquadraFuoriDaSalvare(String squadraFuoriDaSalvare) {
        SquadraFuoriDaSalvare = squadraFuoriDaSalvare;
    }

    public int getIndiceSalvataggioTutteLePartite() {
        return indiceSalvataggioTutteLePartite;
    }

    public void setIndiceSalvataggioTutteLePartite(int indiceSalvataggioTutteLePartite) {
        this.indiceSalvataggioTutteLePartite = indiceSalvataggioTutteLePartite;
    }

    public boolean isStaSalvandoTutteLePartite() {
        return staSalvandoTutteLePartite;
    }

    public void setStaSalvandoTutteLePartite(boolean staSalvandoTutteLePartite) {
        this.staSalvandoTutteLePartite = staSalvandoTutteLePartite;
    }

    public List<StrutturaGiocatori> getGiocatoriCasaPS() {
        return GiocatoriCasaPS;
    }

    public void setGiocatoriCasaPS(List<StrutturaGiocatori> giocatoriCasaPS) {
        GiocatoriCasaPS = giocatoriCasaPS;
    }

    public List<StrutturaGiocatori> getGiocatoriFuoriPS() {
        return GiocatoriFuoriPS;
    }

    public void setGiocatoriFuoriPS(List<StrutturaGiocatori> giocatoriFuoriPS) {
        GiocatoriFuoriPS = giocatoriFuoriPS;
    }

    public int getIdPartitaSalvata() {
        return idPartitaSalvata;
    }

    public void setIdPartitaSalvata(int idPartitaSalvata) {
        this.idPartitaSalvata = idPartitaSalvata;
    }

    public int getIdPartitaDaSalvare() {
        return idPartitaDaSalvare;
    }

    public void setIdPartitaDaSalvare(int idPartitaDaSalvare) {
        this.idPartitaDaSalvare = idPartitaDaSalvare;
    }

    public boolean isStaSalvandoPartita() {
        return staSalvandoPartita;
    }

    public void setStaSalvandoPartita(boolean staSalvandoPartita) {
        this.staSalvandoPartita = staSalvandoPartita;
    }

    public int getSquadreAggiunte() {
        return squadreAggiunte;
    }

    public void setSquadreAggiunte(int squadreAggiunte) {
        this.squadreAggiunte = squadreAggiunte;
    }

    public boolean isStaSalvandoTutteLeSquadre() {
        return staSalvandoTutteLeSquadre;
    }

    public void setStaSalvandoTutteLeSquadre(boolean staSalvandoTutteLeSquadre) {
        this.staSalvandoTutteLeSquadre = staSalvandoTutteLeSquadre;
    }

    public int getIndiceSalvataggioTutteLeSquadre() {
        return indiceSalvataggioTutteLeSquadre;
    }

    public void setIndiceSalvataggioTutteLeSquadre(int indiceSalvataggioTutteLeSquadre) {
        this.indiceSalvataggioTutteLeSquadre = indiceSalvataggioTutteLeSquadre;
    }

    public String getNomeSquadraScelta() {
        return NomeSquadraScelta;
    }

    public void setNomeSquadraScelta(String nomeSquadraScelta) {
        NomeSquadraScelta = nomeSquadraScelta;
    }

    public int getAnnoIniziale() {
        return AnnoIniziale;
    }

    public void setAnnoIniziale(int annoIniziale) {
        AnnoIniziale = annoIniziale;
    }

    public int getIdAnnoScelto() {
        return idAnnoScelto;
    }

    public void setIdAnnoScelto(int idAnnoScelto) {
        this.idAnnoScelto = idAnnoScelto;
    }

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

    public String getAnnoScelto() {
        return AnnoScelto;
    }

    public void setAnnoScelto(String annoScelto) {
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

    public void ScriveAvanzamento(String Testo) {
        if (txtAvanzamento != null) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    txtAvanzamento.setText(Testo);
                };
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }

    public void ImpostaAttesa(boolean Come) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imgCaricamento != null) {
                    if (Come) {
                        imgCaricamento.setVisibility(LinearLayout.VISIBLE);
                    } else {
                        imgCaricamento.setVisibility(LinearLayout.GONE);
                    }
                }
            }
        }, 100);
    }
}
