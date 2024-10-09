package com.looigi.wallpaperchanger2.classeMostraVideo;

import android.app.Activity;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

public class VariabiliStaticheVideo {
    private static VariabiliStaticheVideo instance = null;

    private VariabiliStaticheVideo() {
    }

    public static VariabiliStaticheVideo getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheVideo();
        }

        return instance;
    }

    public static final String UrlWS = "http://looigi.no-ip.biz:1071/";
    public static final String PathUrl = "http://looigi.no-ip.biz:1085/Materiale/RobettaVaria/";
    private String UltimoLink;
    private VideoView videoView;
    private ProgressBar pbLoading;
    private Context context;
    private Activity act;
    private String Filtro = "";
    private String Categoria = "";
    private Spinner spnCategorie;
    private TextView txtTitolo;

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

    public VideoView getVideoView() {
        return videoView;
    }

    public void setVideoView(VideoView videoView) {
        this.videoView = videoView;
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