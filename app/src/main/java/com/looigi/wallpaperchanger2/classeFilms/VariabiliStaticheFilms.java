package com.looigi.wallpaperchanger2.classeFilms;

import android.app.Activity;
import android.content.Context;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;

public class VariabiliStaticheFilms {
    private static VariabiliStaticheFilms instance = null;

    private VariabiliStaticheFilms() {
    }

    public static VariabiliStaticheFilms getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheFilms();
        }

        return instance;
    }

    public static final String UrlWS = "http://looigi.no-ip.biz:1071/";
    public static final String PathUrl = "http://looigi.no-ip.biz:1085/Video/";
    private String Random = "S";
    private String UltimoLink;
    private VideoView FilmsView;
    private ProgressBar pbLoading;
    private Context context;
    private Activity act;
    private String Filtro = "";
    private String Categoria = "";
    private Spinner spnCategorie;
    private TextView txtTitolo;
    private int idUltimoFilms = -1;
    private boolean SettingsAperto = true;
    private boolean barraVisibile = true;
    private MediaController mediaController;
    private int NumeroFrames = 10;
    private boolean staAcquisendoVideo = false;
    private boolean entratoNelCampoDiTesto = false;

    public boolean isEntratoNelCampoDiTesto() {
        return entratoNelCampoDiTesto;
    }

    public void setEntratoNelCampoDiTesto(boolean entratoNelCampoDiTesto) {
        this.entratoNelCampoDiTesto = entratoNelCampoDiTesto;
    }

    public boolean isStaAcquisendoVideo() {
        return staAcquisendoVideo;
    }

    public void setStaAcquisendoVideo(boolean staAcquisendoVideo) {
        this.staAcquisendoVideo = staAcquisendoVideo;
    }

    public int getNumeroFrames() {
        return NumeroFrames;
    }

    public void setNumeroFrames(int numeroFrames) {
        NumeroFrames = numeroFrames;
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    public void setMediaController(MediaController mediaController) {
        this.mediaController = mediaController;
    }

    public boolean isBarraVisibile() {
        return barraVisibile;
    }

    public void setBarraVisibile(boolean barraVisibile) {
        this.barraVisibile = barraVisibile;
    }

    public boolean isSettingsAperto() {
        return SettingsAperto;
    }

    public void setSettingsAperto(boolean settingsAperto) {
        SettingsAperto = settingsAperto;
    }

    public int getIdUltimoFilms() {
        return idUltimoFilms;
    }

    public void setIdUltimoFilms(int idUltimoFilms) {
        this.idUltimoFilms = idUltimoFilms;
    }

    public String getRandom() {
        return Random;
    }

    public void setRandom(String random) {
        Random = random;
    }

    public TextView getTxtTitolo() {
        return txtTitolo;
    }

    public void setTxtTitolo(TextView txtTitolo) {
        this.txtTitolo = txtTitolo;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public Spinner getSpnCategorie() {
        return spnCategorie;
    }

    public void setSpnCategorie(Spinner spnCategorie) {
        this.spnCategorie = spnCategorie;
    }

    public String getFiltro() {
        return Filtro;
    }

    public void setFiltro(String filtro) {
        Filtro = filtro;
    }

    public Activity getAct() {
        return act;
    }

    public void setAct(Activity act) {
        this.act = act;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public VideoView getFilmsView() {
        return FilmsView;
    }

    public void setFilmsView(VideoView FilmsView) {
        this.FilmsView = FilmsView;
    }

    public ProgressBar getPbLoading() {
        return pbLoading;
    }

    public void setPbLoading(ProgressBar pbLoading) {
        this.pbLoading = pbLoading;
    }

    public String getUltimoLink() {
        return UltimoLink;
    }

    public void setUltimoLink(String ultimoLink) {
        UltimoLink = ultimoLink;
    }
}
