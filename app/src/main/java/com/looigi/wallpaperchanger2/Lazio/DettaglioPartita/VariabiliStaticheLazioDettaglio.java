package com.looigi.wallpaperchanger2.Lazio.DettaglioPartita;

import android.widget.EditText;
import android.widget.ListView;

import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Dettaglio;
import com.looigi.wallpaperchanger2.Lazio.Strutture.StrutturaGiocatori;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

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

    public static final String UrlWS = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaLazio + "/"; // "http://www.wslazio.looigi.it/";
    public static final String UrlMedia = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaTotoMioImmagini + "/StemmiSquadre/"; // "http://www.totomiomedia.looigi.it/StemmiSquadre/";
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
    private Dettaglio DettaglioPartita;
    private ListView lstAC;
    private ListView lstAF;
    private ListView lstEC;
    private ListView lstEF;
    private ListView lstMC;
    private ListView lstMF;
    private ListView lstFC;
    private ListView lstFF;
    private EditText edtArbitro;
    private EditText edtSpettatori;
    private EditText edtLocalita;
    private EditText edtNote;

    public EditText getEdtNote() {
        return edtNote;
    }

    public void setEdtNote(EditText edtNote) {
        this.edtNote = edtNote;
    }

    public EditText getEdtArbitro() {
        return edtArbitro;
    }

    public void setEdtArbitro(EditText edtArbitro) {
        this.edtArbitro = edtArbitro;
    }

    public EditText getEdtLocalita() {
        return edtLocalita;
    }

    public void setEdtLocalita(EditText edtLocalita) {
        this.edtLocalita = edtLocalita;
    }

    public EditText getEdtSpettatori() {
        return edtSpettatori;
    }

    public void setEdtSpettatori(EditText edtSpettatori) {
        this.edtSpettatori = edtSpettatori;
    }

    public ListView getLstAC() {
        return lstAC;
    }

    public void setLstAC(ListView lstAC) {
        this.lstAC = lstAC;
    }

    public ListView getLstAF() {
        return lstAF;
    }

    public void setLstAF(ListView lstAF) {
        this.lstAF = lstAF;
    }

    public ListView getLstEC() {
        return lstEC;
    }

    public void setLstEC(ListView lstEC) {
        this.lstEC = lstEC;
    }

    public ListView getLstEF() {
        return lstEF;
    }

    public void setLstEF(ListView lstEF) {
        this.lstEF = lstEF;
    }

    public ListView getLstFC() {
        return lstFC;
    }

    public void setLstFC(ListView lstFC) {
        this.lstFC = lstFC;
    }

    public ListView getLstFF() {
        return lstFF;
    }

    public void setLstFF(ListView lstFF) {
        this.lstFF = lstFF;
    }

    public ListView getLstMC() {
        return lstMC;
    }

    public void setLstMC(ListView lstMC) {
        this.lstMC = lstMC;
    }

    public ListView getLstMF() {
        return lstMF;
    }

    public void setLstMF(ListView lstMF) {
        this.lstMF = lstMF;
    }

    public Dettaglio getDettaglioPartita() {
        return DettaglioPartita;
    }

    public void setDettaglioPartita(Dettaglio dettaglioPartita) {
        DettaglioPartita = dettaglioPartita;
    }

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
