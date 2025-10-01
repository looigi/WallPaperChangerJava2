package com.looigi.wallpaperchanger2.Orari.impostazioni;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.looigi.wallpaperchanger2.Orari.impostazioni.Adapters.AdapterListenerCommesseGestione;
import com.looigi.wallpaperchanger2.Orari.impostazioni.Adapters.AdapterListenerLavoriGestione;
import com.looigi.wallpaperchanger2.Orari.impostazioni.Adapters.AdapterListenerMezziGestione;
import com.looigi.wallpaperchanger2.Orari.impostazioni.Adapters.AdapterListenerMezziStandardGestione;
import com.looigi.wallpaperchanger2.Orari.impostazioni.Adapters.AdapterListenerPasticcheGestione;
import com.looigi.wallpaperchanger2.Orari.impostazioni.Adapters.AdapterListenerPortateGestione;
import com.looigi.wallpaperchanger2.Orari.impostazioni.Adapters.AdapterListenerTempoGestione;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

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

    public static final String UrlWS = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaOrari + "/"; // "http://www.wsorari.looigi.it/"; // "http://looigi.no-ip.biz:1071/";
    private GifImageView imgCaricamento;
    private LinearLayout layGestione;
    private String Modalita;
    private EditText edtRicercaTestoNuovo;
    private boolean DatiModificati = false;

    private int idPortata;
    private LinearLayout layPortata;
    private EditText edtPortata;
    private AdapterListenerPortateGestione cstmAdptPranzo;

    private AdapterListenerMezziGestione cstmAdptMezzi = null;
    private int idMezzo;
    private LinearLayout layMezzi;
    private EditText edtMezzo;
    private EditText edtMezzoDettaglio;

    private AdapterListenerMezziStandardGestione cstmAdptMSA = null;
    private List<StrutturaMezzi> mezziStandard;
    private ListView lstMezziStandard;
    private int AndataRitorno;

    private AdapterListenerLavoriGestione cstmAdptLavori = null;
    private int idLavoro;
    private LinearLayout layLavoro;
    private EditText edtLavoro;
    private EditText edtIndirizzo;
    private EditText edtDataInizio;
    private EditText edtDataFine;
    private EditText edtLatLng;

    private AdapterListenerCommesseGestione cstmAdptCommessa = null;
    private int idCommessa;
    private LinearLayout layCommesse;
    private ListView lstCommesse;

    private AdapterListenerTempoGestione cstmAdptTempo = null;
    private int idTempo;
    private LinearLayout layTempo;
    private EditText edtTempo;

    private AdapterListenerPasticcheGestione cstmAdptPasticche = null;
    private int idPasticca;
    private LinearLayout layPasticca;
    private EditText edtPasticca;

    public int getIdPasticca() {
        return idPasticca;
    }

    public void setIdPasticca(int idPasticca) {
        this.idPasticca = idPasticca;
    }

    public LinearLayout getLayPasticca() {
        return layPasticca;
    }

    public void setLayPasticca(LinearLayout layPasticca) {
        this.layPasticca = layPasticca;
    }

    public EditText getEdtPasticca() {
        return edtPasticca;
    }

    public void setEdtPasticca(EditText edtPasticca) {
        this.edtPasticca = edtPasticca;
    }

    public int getIdTempo() {
        return idTempo;
    }

    public void setIdTempo(int idTempo) {
        this.idTempo = idTempo;
    }

    public LinearLayout getLayTempo() {
        return layTempo;
    }

    public void setLayTempo(LinearLayout layTempo) {
        this.layTempo = layTempo;
    }

    public EditText getEdtTempo() {
        return edtTempo;
    }

    public void setEdtTempo(EditText edtTempo) {
        this.edtTempo = edtTempo;
    }

    public ListView getLstCommesse() {
        return lstCommesse;
    }

    public void setLstCommesse(ListView lstCommesse) {
        this.lstCommesse = lstCommesse;
    }

    public LinearLayout getLayCommesse() {
        return layCommesse;
    }

    public void setLayCommesse(LinearLayout layCommesse) {
        this.layCommesse = layCommesse;
    }

    public int getIdCommessa() {
        return idCommessa;
    }

    public void setIdCommessa(int idCommessa) {
        this.idCommessa = idCommessa;
    }

    public int getIdLavoro() {
        return idLavoro;
    }

    public void setIdLavoro(int idLavoro) {
        this.idLavoro = idLavoro;
    }

    public EditText getEdtDataFine() {
        return edtDataFine;
    }

    public void setEdtDataFine(EditText edtDataFine) {
        this.edtDataFine = edtDataFine;
    }

    public EditText getEdtDataInizio() {
        return edtDataInizio;
    }

    public void setEdtDataInizio(EditText edtDataInizio) {
        this.edtDataInizio = edtDataInizio;
    }

    public EditText getEdtIndirizzo() {
        return edtIndirizzo;
    }

    public void setEdtIndirizzo(EditText edtIndirizzo) {
        this.edtIndirizzo = edtIndirizzo;
    }

    public EditText getEdtLatLng() {
        return edtLatLng;
    }

    public void setEdtLatLng(EditText edtLatLng) {
        this.edtLatLng = edtLatLng;
    }

    public EditText getEdtLavoro() {
        return edtLavoro;
    }

    public void setEdtLavoro(EditText edtLavoro) {
        this.edtLavoro = edtLavoro;
    }

    public LinearLayout getLayLavoro() {
        return layLavoro;
    }

    public void setLayLavoro(LinearLayout layLavoro) {
        this.layLavoro = layLavoro;
    }

    public boolean isDatiModificati() {
        return DatiModificati;
    }

    public void setDatiModificati(boolean datiModificati) {
        DatiModificati = datiModificati;
    }

    public int getAndataRitorno() {
        return AndataRitorno;
    }

    public void setAndataRitorno(int andataRitorno) {
        AndataRitorno = andataRitorno;
    }

    public ListView getLstMezziStandard() {
        return lstMezziStandard;
    }

    public void setLstMezziStandard(ListView lstMezziStandard) {
        this.lstMezziStandard = lstMezziStandard;
    }

    public List<StrutturaMezzi> getMezziStandard() {
        return mezziStandard;
    }

    public void setMezziStandard(List<StrutturaMezzi> mezziStandard) {
        this.mezziStandard = mezziStandard;
    }

    public EditText getEdtMezzo() {
        return edtMezzo;
    }

    public void setEdtMezzo(EditText edtMezzo) {
        this.edtMezzo = edtMezzo;
    }

    public EditText getEdtMezzoDettaglio() {
        return edtMezzoDettaglio;
    }

    public void setEdtMezzoDettaglio(EditText edtMezzoDettaglio) {
        this.edtMezzoDettaglio = edtMezzoDettaglio;
    }

    public int getIdMezzo() {
        return idMezzo;
    }

    public void setIdMezzo(int idMezzo) {
        this.idMezzo = idMezzo;
    }

    public LinearLayout getLayMezzi() {
        return layMezzi;
    }

    public void setLayMezzi(LinearLayout layMezzi) {
        this.layMezzi = layMezzi;
    }

    public AdapterListenerCommesseGestione getCstmAdptCommessa() {
        return cstmAdptCommessa;
    }

    public void setCstmAdptCommessa(AdapterListenerCommesseGestione cstmAdptCommessa) {
        this.cstmAdptCommessa = cstmAdptCommessa;
    }

    public EditText getEdtRicercaTestoNuovo() {
        return edtRicercaTestoNuovo;
    }

    public void setEdtRicercaTestoNuovo(EditText edtRicercaTestoNuovo) {
        this.edtRicercaTestoNuovo = edtRicercaTestoNuovo;
    }

    public AdapterListenerLavoriGestione getCstmAdptLavori() {
        return cstmAdptLavori;
    }

    public void setCstmAdptLavori(AdapterListenerLavoriGestione cstmAdptLavori) {
        this.cstmAdptLavori = cstmAdptLavori;
    }

    public AdapterListenerMezziGestione getCstmAdptMezzi() {
        return cstmAdptMezzi;
    }

    public void setCstmAdptMezzi(AdapterListenerMezziGestione cstmAdptMezzi) {
        this.cstmAdptMezzi = cstmAdptMezzi;
    }

    public AdapterListenerMezziStandardGestione getCstmAdptMSA() {
        return cstmAdptMSA;
    }

    public void setCstmAdptMSA(AdapterListenerMezziStandardGestione cstmAdptMSA) {
        this.cstmAdptMSA = cstmAdptMSA;
    }

    public AdapterListenerPasticcheGestione getCstmAdptPasticche() {
        return cstmAdptPasticche;
    }

    public void setCstmAdptPasticche(AdapterListenerPasticcheGestione cstmAdptPasticche) {
        this.cstmAdptPasticche = cstmAdptPasticche;
    }

    public AdapterListenerPortateGestione getCstmAdptPranzo() {
        return cstmAdptPranzo;
    }

    public void setCstmAdptPranzo(AdapterListenerPortateGestione cstmAdptPranzo) {
        this.cstmAdptPranzo = cstmAdptPranzo;
    }

    public AdapterListenerTempoGestione getCstmAdptTempo() {
        return cstmAdptTempo;
    }

    public void setCstmAdptTempo(AdapterListenerTempoGestione cstmAdptTempo) {
        this.cstmAdptTempo = cstmAdptTempo;
    }

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
