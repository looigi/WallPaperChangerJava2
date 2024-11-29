package com.looigi.wallpaperchanger2.classeVideo;

import android.app.Activity;
import android.content.Context;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.List;

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
    private String Random = "S";
    private String UltimoLink;
    private VideoView videoView;
    private ProgressBar pbLoading;
    private Context context;
    private Activity act;
    private String Filtro = "";
    private String Categoria = "";
    private Spinner spnCategorie;
    private TextView txtId;
    private TextView txtCate;
    private TextView txtTitolo;
    private int idUltimoVideo = -1;
    private boolean SettingsAperto = true;
    private boolean barraVisibile = false;
    private MediaController mediaController;
    private int NumeroFrames = 10;
    private boolean staAcquisendoVideo = false;
    private boolean entratoNelCampoDiTesto = false;
    private TextView txtAvanzamento;
    private List<String> listaCategorie;
    private String filtroCategoria = "";
    private Spinner spnSpostaCategorie;
    private String filtroCategoriaSpostamento = "";
    private String idCategoriaSpostamento;
    private boolean ricercaPerVisua = true;

    public TextView getTxtCate() {
        return txtCate;
    }

    public void setTxtCate(TextView txtCate) {
        this.txtCate = txtCate;
    }

    public TextView getTxtId() {
        return txtId;
    }

    public void setTxtId(TextView txtId) {
        this.txtId = txtId;
    }

    public boolean isRicercaPerVisua() {
        return ricercaPerVisua;
    }

    public void setRicercaPerVisua(boolean ricercaPerVisua) {
        this.ricercaPerVisua = ricercaPerVisua;
    }

    public String getIdCategoriaSpostamento() {
        return idCategoriaSpostamento;
    }

    public void setIdCategoriaSpostamento(String idCategoriaSpostamento) {
        this.idCategoriaSpostamento = idCategoriaSpostamento;
    }

    public String getFiltroCategoriaSpostamento() {
        return filtroCategoriaSpostamento;
    }

    public void setFiltroCategoriaSpostamento(String filtroCategoriaSpostamento) {
        this.filtroCategoriaSpostamento = filtroCategoriaSpostamento;
    }

    public Spinner getSpnSpostaCategorie() {
        return spnSpostaCategorie;
    }

    public void setSpnSpostaCategorie(Spinner spnSpostaCategorie) {
        this.spnSpostaCategorie = spnSpostaCategorie;
    }

    public String getFiltroCategoria() {
        return filtroCategoria;
    }

    public void setFiltroCategoria(String filtroCategoria) {
        this.filtroCategoria = filtroCategoria;
    }

    public List<String> getListaCategorie() {
        return listaCategorie;
    }

    public void setListaCategorie(List<String> listaCategorie) {
        this.listaCategorie = listaCategorie;
    }

    public TextView getTxtAvanzamento() {
        return txtAvanzamento;
    }

    public void setTxtAvanzamento(TextView txtAvanzamento) {
        this.txtAvanzamento = txtAvanzamento;
    }

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

    public int getIdUltimoVideo() {
        return idUltimoVideo;
    }

    public void setIdUltimoVideo(int idUltimoVideo) {
        this.idUltimoVideo = idUltimoVideo;
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

    public void ScriveImmagini(String url) {
        String[] c = url.split("/");
        String NomeFile = c[c.length - 1];
        String Categoria = url.replace("/" + NomeFile, "");

        VariabiliStaticheVideo.getInstance().getTxtId().setText(
                Integer.toString(VariabiliStaticheVideo.getInstance().getIdUltimoVideo())
        );
        VariabiliStaticheVideo.getInstance().getTxtCate().setText(
                Categoria.replace(PathUrl, "")
        );
        VariabiliStaticheVideo.getInstance().getTxtTitolo().setText(
                NomeFile
        );
    }
}