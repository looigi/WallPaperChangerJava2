package com.looigi.wallpaperchanger2.Video;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.UtilitiesVarie.ProportionalVideoView;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

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

    public static final String UrlWS = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaLooVF + "/"; // "http://looigi.no-ip.biz:1071/";
    public static final String PathUrl = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaDiscoPublic + "/Materiale/RobettaVaria/"; // "http://looigi.no-ip.biz:1085/Materiale/RobettaVaria/";
    private String Random = "S";
    private String UltimoLink;
    private ProportionalVideoView videoView;
    private ProgressBar pbLoading;
    private Context context;
    private Activity act;
    private String Filtro = "";
    private String Categoria = "";
    private Spinner spnCategorie;
    // private TextView txtId;
    // private TextView txtCate;
    // private TextView txtTitolo;
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
    private boolean AggiornamentoCompleto = false;
    private SeekBar seekScorri;
    private SeekBar seekScorri2;
    private boolean staVedendo = false;
    private TextView txtAvanzamentoSeek;
    private TextView txtMaxSeek;
    private TextView txtTitoloSeek;
    private LinearLayout layBarraTasti;
    private int secondiAlpha;
    private boolean BarraOscurata = false;
    private TextView txtInfoSotto;

    public TextView getTxtInfoSotto() {
        return txtInfoSotto;
    }

    public void setTxtInfoSotto(TextView txtInfoSotto) {
        this.txtInfoSotto = txtInfoSotto;
    }

    public boolean isBarraOscurata() {
        return BarraOscurata;
    }

    public void setBarraOscurata(boolean barraOscurata) {
        BarraOscurata = barraOscurata;
    }

    public int getSecondiAlpha() {
        return secondiAlpha;
    }

    public void setSecondiAlpha(int secondiAlpha) {
        this.secondiAlpha = secondiAlpha;
    }

    public LinearLayout getLayBarraTasti() {
        return layBarraTasti;
    }

    public void setLayBarraTasti(LinearLayout layBarraTasti) {
        this.layBarraTasti = layBarraTasti;
    }

    public SeekBar getSeekScorri2() {
        return seekScorri2;
    }

    public void setSeekScorri2(SeekBar seekScorri2) {
        this.seekScorri2 = seekScorri2;
    }

    public TextView getTxtTitoloSeek() {
        return txtTitoloSeek;
    }

    public void setTxtTitoloSeek(TextView txtTitoloSeek) {
        this.txtTitoloSeek = txtTitoloSeek;
    }

    public TextView getTxtAvanzamentoSeek() {
        return txtAvanzamentoSeek;
    }

    public void setTxtAvanzamentoSeek(TextView txtAvanzamentoSeek) {
        this.txtAvanzamentoSeek = txtAvanzamentoSeek;
    }

    public TextView getTxtMaxSeek() {
        return txtMaxSeek;
    }

    public void setTxtMaxSeek(TextView txtMaxSeek) {
        this.txtMaxSeek = txtMaxSeek;
    }

    public boolean isStaVedendo() {
        return staVedendo;
    }

    public void setStaVedendo(boolean staVedendo) {
        this.staVedendo = staVedendo;
    }

    public SeekBar getSeekScorri() {
        return seekScorri;
    }

    public void setSeekScorri(SeekBar seekScorri) {
        this.seekScorri = seekScorri;
    }

    public boolean isAggiornamentoCompleto() {
        return AggiornamentoCompleto;
    }

    public void setAggiornamentoCompleto(boolean aggiornamentoCompleto) {
        AggiornamentoCompleto = aggiornamentoCompleto;
    }

    /*
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
    */

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

    /*
    public TextView getTxtTitolo() {
        return txtTitolo;
    }

    public void setTxtTitolo(TextView txtTitolo) {
        this.txtTitolo = txtTitolo;
    }
    */

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

    public ProportionalVideoView getVideoView() {
        return videoView;
    }

    public void setVideoView(ProportionalVideoView videoView) {
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
        return;

        /* String[] c = url.split("/");
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
        ); */
    }
}