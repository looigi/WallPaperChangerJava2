package com.looigi.wallpaperchanger2.classeLazio;

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
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaMarcatori;
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
    private int Giornata = -1;
    private List<StrutturaClassifica> Classifica = new ArrayList<>();
    private LinearLayout layClassifica;
    private LinearLayout laySquadre;
    private LinearLayout layMercato;
    private LinearLayout layCalendario;
    private LinearLayout layFonti;
    private LinearLayout layStati;
    private int MascheraSelezionata = 1;
    private ListView lstClassifica;
    private ListView lstCalendario;
    private ListView lstSquadre;
    private ListView lstMercato;
    private ListView lstFonti;
    private ListView lstStati;
    private int ModalitaClassifica;
    private AdapterListenerClassifica cstmAdptClassifica;
    private TextView txtGiornata;
    private List<StrutturaCalendario> Calendario = new ArrayList<>();
    private int ModalitaMercato = 1;
    private List<StrutturaMercato> Mercato = new ArrayList<>();
    private AdapterListenerMercato cstmAdptMercato;
    private List<StrutturaStati> Stati = new ArrayList<>();
    private List<StrutturaFonti> Fonti = new ArrayList<>();
    private ImageView imgNuovo;
    private int MaxGiornate;
    private boolean nonRicaricareMercato = false;
    private LinearLayout layModifica;
    private TextView txtModifica;
    private String CosaStoModificando;
    private String ModalitaModifica;
    private EditText edtValore1;
    private String ValoreImpostato1;
    private String ValoreImpostato2;
    private LinearLayout layModificaSFS;
    private LinearLayout layModificaCalendario;
    private LinearLayout layModificaMercato;
    private Spinner spnFonti;
    private Spinner spnStati;
    private EditText edtNominativo;
    private EditText edtData;
    private String[] righePerSquadre;
    private EditText edtDataCal;
    private EditText edtRisultato;
    private Spinner spnCasa;
    private Spinner spnFuori;
    private ArrayAdapter<String> adapterFonti;
    private ArrayAdapter<String> adapterStati;
    private int idFonte;
    private int idStato;
    private int idCasa;
    private int idFuori;
    private LinearLayout layGiocatori;
    private Spinner spnSquadreGioc;
    private int idSquadraPerGioc = -1;
    private List<StrutturaGiocatori> Giocatori = new ArrayList<>();
    private AdapterListenerGiocatori cstmAdptGiocatori;
    private ListView lstGiocatori;
    private LinearLayout layModificaGiocatore;
    private EditText edtCognome;
    private EditText edtNome;
    private Spinner spnRuolo;
    private ArrayAdapter<String> adapterRuoli;
    private List<StrutturaRuoli> Ruoli;
    private int idRuoloSelezionato;
    private LinearLayout layRuoli;
    private ListView lstRuoli;
    private LinearLayout layAllenatori;
    private ListView lstAllenatori;
    private List<StrutturaAllenatori> Allenatori;
    private int idSquadraPerAll = -1;
    private Spinner spnSquadreAll;
    private AdapterListenerAllenatori cstmAdptAllenatori;
    private LinearLayout layRuolo;
    private int AcquistiCessioni = 1;
    private LinearLayout layMarcatori;
    private ListView lstMarcatori;
    private List<StrutturaMarcatori> Marcatori;

    public List<StrutturaMarcatori> getMarcatori() {
        return Marcatori;
    }

    public void setMarcatori(List<StrutturaMarcatori> marcatori) {
        Marcatori = marcatori;
    }

    public LinearLayout getLayMarcatori() {
        return layMarcatori;
    }

    public void setLayMarcatori(LinearLayout layMarcatori) {
        this.layMarcatori = layMarcatori;
    }

    public ListView getLstMarcatori() {
        return lstMarcatori;
    }

    public void setLstMarcatori(ListView lstMarcatori) {
        this.lstMarcatori = lstMarcatori;
    }

    public int getAcquistiCessioni() {
        return AcquistiCessioni;
    }

    public void setAcquistiCessioni(int acquistiCessioni) {
        AcquistiCessioni = acquistiCessioni;
    }

    public LinearLayout getLayRuolo() {
        return layRuolo;
    }

    public void setLayRuolo(LinearLayout layRuolo) {
        this.layRuolo = layRuolo;
    }

    public AdapterListenerAllenatori getCstmAdptAllenatori() {
        return cstmAdptAllenatori;
    }

    public void setCstmAdptAllenatori(AdapterListenerAllenatori cstmAdptAllenatori) {
        this.cstmAdptAllenatori = cstmAdptAllenatori;
    }

    public Spinner getSpnSquadreAll() {
        return spnSquadreAll;
    }

    public void setSpnSquadreAll(Spinner spnSquadreAll) {
        this.spnSquadreAll = spnSquadreAll;
    }

    public int getIdSquadraPerAll() {
        return idSquadraPerAll;
    }

    public void setIdSquadraPerAll(int idSquadraPerAll) {
        this.idSquadraPerAll = idSquadraPerAll;
    }

    public List<StrutturaAllenatori> getAllenatori() {
        return Allenatori;
    }

    public void setAllenatori(List<StrutturaAllenatori> allenatori) {
        Allenatori = allenatori;
    }

    public LinearLayout getLayAllenatori() {
        return layAllenatori;
    }

    public void setLayAllenatori(LinearLayout layAllenatori) {
        this.layAllenatori = layAllenatori;
    }

    public ListView getLstAllenatori() {
        return lstAllenatori;
    }

    public void setLstAllenatori(ListView lstAllenatori) {
        this.lstAllenatori = lstAllenatori;
    }

    public LinearLayout getLayRuoli() {
        return layRuoli;
    }

    public void setLayRuoli(LinearLayout layRuoli) {
        this.layRuoli = layRuoli;
    }

    public ListView getLstRuoli() {
        return lstRuoli;
    }

    public void setLstRuoli(ListView lstRuoli) {
        this.lstRuoli = lstRuoli;
    }

    public ArrayAdapter<String> getAdapterRuoli() {
        return adapterRuoli;
    }

    public void setAdapterRuoli(ArrayAdapter<String> adapterRuoli) {
        this.adapterRuoli = adapterRuoli;
    }

    public int getIdRuoloSelezionato() {
        return idRuoloSelezionato;
    }

    public void setIdRuoloSelezionato(int idRuoloSelezionato) {
        this.idRuoloSelezionato = idRuoloSelezionato;
    }

    public List<StrutturaRuoli> getRuoli() {
        return Ruoli;
    }

    public void setRuoli(List<StrutturaRuoli> ruoli) {
        Ruoli = ruoli;
    }

    public EditText getEdtCognome() {
        return edtCognome;
    }

    public void setEdtCognome(EditText edtCognome) {
        this.edtCognome = edtCognome;
    }

    public EditText getEdtNome() {
        return edtNome;
    }

    public void setEdtNome(EditText edtNome) {
        this.edtNome = edtNome;
    }

    public Spinner getSpnRuolo() {
        return spnRuolo;
    }

    public void setSpnRuolo(Spinner spnRuolo) {
        this.spnRuolo = spnRuolo;
    }

    public LinearLayout getLayModificaGiocatore() {
        return layModificaGiocatore;
    }

    public void setLayModificaGiocatore(LinearLayout layModificaGiocatore) {
        this.layModificaGiocatore = layModificaGiocatore;
    }

    public ListView getLstGiocatori() {
        return lstGiocatori;
    }

    public void setLstGiocatori(ListView lstGiocatori) {
        this.lstGiocatori = lstGiocatori;
    }

    public AdapterListenerGiocatori getCstmAdptGiocatori() {
        return cstmAdptGiocatori;
    }

    public void setCstmAdptGiocatori(AdapterListenerGiocatori cstmAdptGiocatori) {
        this.cstmAdptGiocatori = cstmAdptGiocatori;
    }

    public List<StrutturaGiocatori> getGiocatori() {
        return Giocatori;
    }

    public void setGiocatori(List<StrutturaGiocatori> giocatori) {
        Giocatori = giocatori;
    }

    public int getIdSquadraPerGioc() {
        return idSquadraPerGioc;
    }

    public void setIdSquadraPerGioc(int idSquadraPerGioc) {
        this.idSquadraPerGioc = idSquadraPerGioc;
    }

    public Spinner getSpnSquadreGioc() {
        return spnSquadreGioc;
    }

    public void setSpnSquadreGioc(Spinner spnSquadreGioc) {
        this.spnSquadreGioc = spnSquadreGioc;
    }

    public LinearLayout getLayGiocatori() {
        return layGiocatori;
    }

    public void setLayGiocatori(LinearLayout layGiocatori) {
        this.layGiocatori = layGiocatori;
    }

    public int getIdCasa() {
        return idCasa;
    }

    public void setIdCasa(int idCasa) {
        this.idCasa = idCasa;
    }

    public int getIdFuori() {
        return idFuori;
    }

    public void setIdFuori(int idFuori) {
        this.idFuori = idFuori;
    }

    public int getIdFonte() {
        return idFonte;
    }

    public void setIdFonte(int idFonte) {
        this.idFonte = idFonte;
    }

    public int getIdStato() {
        return idStato;
    }

    public void setIdStato(int idStato) {
        this.idStato = idStato;
    }

    public ArrayAdapter<String> getAdapterFonti() {
        return adapterFonti;
    }

    public void setAdapterFonti(ArrayAdapter<String> adapterFonti) {
        this.adapterFonti = adapterFonti;
    }

    public ArrayAdapter<String> getAdapterStati() {
        return adapterStati;
    }

    public void setAdapterStati(ArrayAdapter<String> adapterStati) {
        this.adapterStati = adapterStati;
    }

    public EditText getEdtDataCal() {
        return edtDataCal;
    }

    public void setEdtDataCal(EditText edtDataCal) {
        this.edtDataCal = edtDataCal;
    }

    public EditText getEdtRisultato() {
        return edtRisultato;
    }

    public void setEdtRisultato(EditText edtRisultato) {
        this.edtRisultato = edtRisultato;
    }

    public Spinner getSpnCasa() {
        return spnCasa;
    }

    public void setSpnCasa(Spinner spnCasa) {
        this.spnCasa = spnCasa;
    }

    public Spinner getSpnFuori() {
        return spnFuori;
    }

    public void setSpnFuori(Spinner spnFuori) {
        this.spnFuori = spnFuori;
    }

    public String[] getRighePerSquadre() {
        return righePerSquadre;
    }

    public void setRighePerSquadre(String[] righePerSquadre) {
        this.righePerSquadre = righePerSquadre;
    }

    public String getValoreImpostato2() {
        return ValoreImpostato2;
    }

    public void setValoreImpostato2(String valoreImpostato2) {
        ValoreImpostato2 = valoreImpostato2;
    }

    public EditText getEdtNominativo() {
        return edtNominativo;
    }

    public void setEdtNominativo(EditText edtNominativo) {
        this.edtNominativo = edtNominativo;
    }

    public EditText getEdtData() {
        return edtData;
    }

    public void setEdtData(EditText edtData) {
        this.edtData = edtData;
    }

    public Spinner getSpnFonti() {
        return spnFonti;
    }

    public void setSpnFonti(Spinner spnFonti) {
        this.spnFonti = spnFonti;
    }

    public Spinner getSpnStati() {
        return spnStati;
    }

    public void setSpnStati(Spinner spnStati) {
        this.spnStati = spnStati;
    }

    public LinearLayout getLayModificaCalendario() {
        return layModificaCalendario;
    }

    public void setLayModificaCalendario(LinearLayout layModificaCalendario) {
        this.layModificaCalendario = layModificaCalendario;
    }

    public LinearLayout getLayModificaMercato() {
        return layModificaMercato;
    }

    public void setLayModificaMercato(LinearLayout layModificaMercato) {
        this.layModificaMercato = layModificaMercato;
    }

    public LinearLayout getLayModificaSFS() {
        return layModificaSFS;
    }

    public void setLayModificaSFS(LinearLayout layModificaSFS) {
        this.layModificaSFS = layModificaSFS;
    }

    public String getValoreImpostato1() {
        return ValoreImpostato1;
    }

    public void setValoreImpostato1(String valoreImpostato1) {
        ValoreImpostato1 = valoreImpostato1;
    }

    public EditText getEdtValore1() {
        return edtValore1;
    }

    public void setEdtValore1(EditText edtValore1) {
        this.edtValore1 = edtValore1;
    }

    public String getCosaStoModificando() {
        return CosaStoModificando;
    }

    public void setCosaStoModificando(String cosaStoModificando) {
        CosaStoModificando = cosaStoModificando;
    }

    public String getModalitaModifica() {
        return ModalitaModifica;
    }

    public void setModalitaModifica(String modalitaModifica) {
        ModalitaModifica = modalitaModifica;
    }

    public LinearLayout getLayModifica() {
        return layModifica;
    }

    public void setLayModifica(LinearLayout layModifica) {
        this.layModifica = layModifica;
    }

    public TextView getTxtModifica() {
        return txtModifica;
    }

    public void setTxtModifica(TextView txtModifica) {
        this.txtModifica = txtModifica;
    }

    public boolean isNonRicaricareMercato() {
        return nonRicaricareMercato;
    }

    public void setNonRicaricareMercato(boolean nonRicaricareMercato) {
        this.nonRicaricareMercato = nonRicaricareMercato;
    }

    public ListView getLstFonti() {
        return lstFonti;
    }

    public void setLstFonti(ListView lstFonti) {
        this.lstFonti = lstFonti;
    }

    public ListView getLstStati() {
        return lstStati;
    }

    public void setLstStati(ListView lstStati) {
        this.lstStati = lstStati;
    }

    public LinearLayout getLayFonti() {
        return layFonti;
    }

    public void setLayFonti(LinearLayout layFonti) {
        this.layFonti = layFonti;
    }

    public LinearLayout getLayStati() {
        return layStati;
    }

    public void setLayStati(LinearLayout layStati) {
        this.layStati = layStati;
    }

    public int getMaxGiornate() {
        return MaxGiornate;
    }

    public void setMaxGiornate(int maxGiornate) {
        MaxGiornate = maxGiornate;
    }

    public ImageView getImgNuovo() {
        return imgNuovo;
    }

    public void setImgNuovo(ImageView imgNuovo) {
        this.imgNuovo = imgNuovo;
    }

    public List<StrutturaStati> getStati() {
        return Stati;
    }

    public void setStati(List<StrutturaStati> stati) {
        Stati = stati;
    }

    public List<StrutturaFonti> getFonti() {
        return Fonti;
    }

    public void setFonti(List<StrutturaFonti> fonti) {
        Fonti = fonti;
    }

    public AdapterListenerMercato getCstmAdptMercato() {
        return cstmAdptMercato;
    }

    public void setCstmAdptMercato(AdapterListenerMercato cstmAdptMercato) {
        this.cstmAdptMercato = cstmAdptMercato;
    }

    public List<StrutturaMercato> getMercato() {
        return Mercato;
    }

    public void setMercato(List<StrutturaMercato> mercato) {
        Mercato = mercato;
    }

    public int getModalitaMercato() {
        return ModalitaMercato;
    }

    public void setModalitaMercato(int modalitaMercato) {
        ModalitaMercato = modalitaMercato;
    }

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
