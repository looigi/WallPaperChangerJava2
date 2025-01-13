package com.looigi.wallpaperchanger2.classeLazio;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaAnni;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCalendario;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaClassifica;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCompetizioni;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaSquadre;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerClassifica;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaCommesse;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDati;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDatiGiornata;

import java.util.ArrayList;
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

    public static final String UrlWS = "http://www.wslazio.looigi.it/";
    public static final String UrlMedia = "http://www.totomiomedia.looigi.it/StemmiSquadre/";
    private GifImageView imgCaricamento;
    private List<StrutturaAnni> Anni = new ArrayList<>();
    private Spinner spnAnni;
    private String PathLazio;
    private int idAnnoSelezionato;
    private String AnnoSelezionato;
    private int idTipologia = 1;
    private Spinner spnCompetizioni;
    private List<StrutturaCompetizioni> Competizioni = new ArrayList<>();
    private List<StrutturaSquadre> Squadre = new ArrayList<>();
    private int Giornata = 1;
    private List<StrutturaClassifica> Classifica = new ArrayList<>();
    private LinearLayout layClassifica;
    private LinearLayout laySquadre;
    private LinearLayout layMercato;
    private LinearLayout layCalendario;
    private int MascheraSelezionata = 1;
    private ListView lstClassifica;
    private ListView lstCalendario;
    private ListView lstSquadre;
    private ListView lstMercato;
    private int ModalitaClassifica;
    private AdapterListenerClassifica cstmAdptClassifica;
    private TextView txtGiornata;
    private List<StrutturaCalendario> Calendario = new ArrayList<>();

    public List<StrutturaCalendario> getCalendario() {
        return Calendario;
    }

    public void setCalendario(List<StrutturaCalendario> calendario) {
        Calendario = calendario;
    }

    public TextView getTxtGiornata() {
        return txtGiornata;
    }

    public void setTxtGiornata(TextView txtGiornata) {
        this.txtGiornata = txtGiornata;
    }

    public AdapterListenerClassifica getCstmAdptClassifica() {
        return cstmAdptClassifica;
    }

    public void setCstmAdptClassifica(AdapterListenerClassifica cstmAdptClassifica) {
        this.cstmAdptClassifica = cstmAdptClassifica;
    }

    public int getModalitaClassifica() {
        return ModalitaClassifica;
    }

    public void setModalitaClassifica(int modalitaClassifica) {
        ModalitaClassifica = modalitaClassifica;
    }

    public ListView getLstClassifica() {
        return lstClassifica;
    }

    public void setLstClassifica(ListView lstClassifica) {
        this.lstClassifica = lstClassifica;
    }

    public ListView getLstCalendario() {
        return lstCalendario;
    }

    public void setLstCalendario(ListView lstCalendario) {
        this.lstCalendario = lstCalendario;
    }

    public ListView getLstSquadre() {
        return lstSquadre;
    }

    public void setLstSquadre(ListView lstSquadre) {
        this.lstSquadre = lstSquadre;
    }

    public ListView getLstMercato() {
        return lstMercato;
    }

    public void setLstMercato(ListView lstMercato) {
        this.lstMercato = lstMercato;
    }

    public int getMascheraSelezionata() {
        return MascheraSelezionata;
    }

    public void setMascheraSelezionata(int mascheraSelezionata) {
        MascheraSelezionata = mascheraSelezionata;
    }

    public LinearLayout getLayClassifica() {
        return layClassifica;
    }

    public void setLayClassifica(LinearLayout layClassifica) {
        this.layClassifica = layClassifica;
    }

    public LinearLayout getLaySquadre() {
        return laySquadre;
    }

    public void setLaySquadre(LinearLayout laySquadre) {
        this.laySquadre = laySquadre;
    }

    public LinearLayout getLayMercato() {
        return layMercato;
    }

    public void setLayMercato(LinearLayout layMercato) {
        this.layMercato = layMercato;
    }

    public LinearLayout getLayCalendario() {
        return layCalendario;
    }

    public void setLayCalendario(LinearLayout layCalendario) {
        this.layCalendario = layCalendario;
    }

    public List<StrutturaClassifica> getClassifica() {
        return Classifica;
    }

    public void setClassifica(List<StrutturaClassifica> classifica) {
        Classifica = classifica;
    }

    public int getGiornata() {
        return Giornata;
    }

    public void setGiornata(int giornata) {
        Giornata = giornata;
    }

    public List<StrutturaSquadre> getSquadre() {
        return Squadre;
    }

    public void setSquadre(List<StrutturaSquadre> squadre) {
        Squadre = squadre;
    }

    public Spinner getSpnCompetizioni() {
        return spnCompetizioni;
    }

    public void setSpnCompetizioni(Spinner spnCompetizioni) {
        this.spnCompetizioni = spnCompetizioni;
    }

    public List<StrutturaCompetizioni> getCompetizioni() {
        return Competizioni;
    }

    public void setCompetizioni(List<StrutturaCompetizioni> competizioni) {
        Competizioni = competizioni;
    }

    public int getIdTipologia() {
        return idTipologia;
    }

    public void setIdTipologia(int idTipologia) {
        this.idTipologia = idTipologia;
    }

    public String getAnnoSelezionato() {
        return AnnoSelezionato;
    }

    public void setAnnoSelezionato(String annoSelezionato) {
        AnnoSelezionato = annoSelezionato;
    }

    public int getIdAnnoSelezionato() {
        return idAnnoSelezionato;
    }

    public void setIdAnnoSelezionato(int idAnnoSelezionato) {
        this.idAnnoSelezionato = idAnnoSelezionato;
    }

    public String getPathLazio() {
        return PathLazio;
    }

    public void setPathLazio(String pathLazio) {
        PathLazio = pathLazio;
    }

    public Spinner getSpnAnni() {
        return spnAnni;
    }

    public void setSpnAnni(Spinner spnAnni) {
        this.spnAnni = spnAnni;
    }

    public List<StrutturaAnni> getAnni() {
        return Anni;
    }

    public void setAnni(List<StrutturaAnni> anni) {
        Anni = anni;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }
}
