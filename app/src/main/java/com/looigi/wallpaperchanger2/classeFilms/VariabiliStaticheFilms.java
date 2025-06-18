package com.looigi.wallpaperchanger2.classeFilms;

import android.app.Activity;
import android.content.Context;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.List;

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

    public static final String UrlWS = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaLooVF + "/"; // "http://looigi.no-ip.biz:1071/";
    public static final String PathUrl = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaDiscoPublic + "/Video/"; // "http://looigi.no-ip.biz:1085/Video/";
    private String Random = "S";
    private String UltimoLink;
    private VideoView FilmsView;
    private ProgressBar pbLoading;
    private Context context;
    private Activity act;
    private String Filtro = "";
    private String Categoria = "";
    private Spinner spnCategorie;
    private TextView txtId;
    private TextView txtCate;
    private TextView txtTitolo;
    private int idUltimoFilms = -1;
    private boolean SettingsAperto = true;
    private boolean barraVisibile = false;
    private MediaController mediaController;
    private int NumeroFrames = 10;
    private boolean staAcquisendoVideo = false;
    private boolean entratoNelCampoDiTesto = false;
    private List<String> listaCategorie;
    private String filtroCategoria = "";
    private Spinner spnSpostaCategorie;
    private String filtroCategoriaSpostamento = "";
    private String idCategoriaSpostamento;
    private boolean ricercaPerVisua = true;
    private boolean AggiornamentoCompleto = false;

    public boolean isAggiornamentoCompleto() {
        return AggiornamentoCompleto;
    }

    public void setAggiornamentoCompleto(boolean aggiornamentoCompleto) {
        AggiornamentoCompleto = aggiornamentoCompleto;
    }

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

    public void ScriveInfo(String url) {
        String[] c = url.split("/");
        String NomeFile = c[c.length - 1];
        String Categoria = url.replace("/" + NomeFile, "");

        VariabiliStaticheFilms.getInstance().getTxtId().setText(
                Integer.toString(VariabiliStaticheFilms.getInstance().getIdUltimoFilms())
        );
        VariabiliStaticheFilms.getInstance().getTxtCate().setText(
                Categoria.replace(PathUrl, "")
        );
        VariabiliStaticheFilms.getInstance().getTxtTitolo().setText(
                NomeFile
        );
    }
}
