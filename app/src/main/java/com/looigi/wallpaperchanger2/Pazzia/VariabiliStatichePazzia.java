package com.looigi.wallpaperchanger2.Pazzia;

import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.looigi.wallpaperchanger2.Pazzia.GestioneCategorie.StrutturaCategorieFinali;
import com.looigi.wallpaperchanger2.Pennetta.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.UtilitiesVarie.ImmagineZoomabile;
import com.looigi.wallpaperchanger2.UtilitiesVarie.ProportionalVideoView;

import java.util.List;

public class VariabiliStatichePazzia {
    private static VariabiliStatichePazzia instance = null;

    private VariabiliStatichePazzia() {
    }

    public static VariabiliStatichePazzia getInstance() {
        if (instance == null) {
            instance = new VariabiliStatichePazzia();
        }

        return instance;
    }

    private boolean GiaPartito = false;
    private ImmagineZoomabile imgPennetta;
    private ImmagineZoomabile imgImmagini;
    private ProportionalVideoView videoView;
    private ImageView imgCaricamentoPEN;
    private ImageView imgCaricamentoIMM;
    private ImageView imgCaricamentoVID;
    private boolean SlideShowAttivoPEN = true;
    private boolean SlideShowAttivoIMM = true;
    private MediaController mediaController;
    private boolean StaVedendo = true;
    private Spinner spnCategoria;
    private int ultimaPennetta;
    private int ultimaImmagine;
    private int ultimoVideo;
    private List<StrutturaImmaginiCategorie> listaCategoriePEN;
    private List<com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie> listaCategorieIMM;
    private List<String> listaCategorieVID;
    private String Modalita;
    private SeekBar seekBar;
    private boolean staVisualizzandoVideo = true;

    // Che si devono salvare
    private String CategoriaPennetta;
    private String CategoriaImmagini;
    private String CategoriaVideo;
    private String FiltroIMM = "";
    private String FiltroPEN = "";
    private String FiltroVID = "";
    // Che si devono salvare

    private List<StrutturaCategorieFinali> listaCategoriePresentiImmVid;

    public List<StrutturaCategorieFinali> getListaCategoriePresentiImmVid() {
        return listaCategoriePresentiImmVid;
    }

    public void setListaCategoriePresentiImmVid(List<StrutturaCategorieFinali> listaCategoriePresentiImmVid) {
        this.listaCategoriePresentiImmVid = listaCategoriePresentiImmVid;
    }

    public boolean isStaVisualizzandoVideo() {
        return staVisualizzandoVideo;
    }

    public void setStaVisualizzandoVideo(boolean staVisualizzandoVideo) {
        this.staVisualizzandoVideo = staVisualizzandoVideo;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public String getModalita() {
        return Modalita;
    }

    public void setModalita(String modalita) {
        Modalita = modalita;
    }

    public int getUltimaPennetta() {
        return ultimaPennetta;
    }

    public void setUltimaPennetta(int ultimaPennetta) {
        this.ultimaPennetta = ultimaPennetta;
    }

    public String getFiltroIMM() {
        return FiltroIMM;
    }

    public void setFiltroIMM(String filtroIMM) {
        FiltroIMM = filtroIMM;
    }

    public String getFiltroPEN() {
        return FiltroPEN;
    }

    public void setFiltroPEN(String filtroPEN) {
        FiltroPEN = filtroPEN;
    }

    public String getFiltroVID() {
        return FiltroVID;
    }

    public void setFiltroVID(String filtroVID) {
        FiltroVID = filtroVID;
    }

    public List<String> getListaCategorieVID() {
        return listaCategorieVID;
    }

    public void setListaCategorieVID(List<String> listaCategorieVID) {
        this.listaCategorieVID = listaCategorieVID;
    }

    public List<com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie> getListaCategorieIMM() {
        return listaCategorieIMM;
    }

    public void setListaCategorieIMM(List<com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie> listaCategorieIMM) {
        this.listaCategorieIMM = listaCategorieIMM;
    }

    public List<StrutturaImmaginiCategorie> getListaCategoriePEN() {
        return listaCategoriePEN;
    }

    public void setListaCategoriePEN(List<StrutturaImmaginiCategorie> listaCategoriePEN) {
        this.listaCategoriePEN = listaCategoriePEN;
    }

    public int getUltimoVideo() {
        return ultimoVideo;
    }

    public void setUltimoVideo(int ultimoVideo) {
        this.ultimoVideo = ultimoVideo;
    }

    public int getUltimaImmagine() {
        return ultimaImmagine;
    }

    public void setUltimaImmagine(int ultimaImmagine) {
        this.ultimaImmagine = ultimaImmagine;
    }

    public String getCategoriaPennetta() {
        return CategoriaPennetta;
    }

    public void setCategoriaPennetta(String categoriaPennetta) {
        CategoriaPennetta = categoriaPennetta;
    }

    public String getCategoriaImmagini() {
        return CategoriaImmagini;
    }

    public void setCategoriaImmagini(String categoriaImmagini) {
        CategoriaImmagini = categoriaImmagini;
    }

    public String getCategoriaVideo() {
        return CategoriaVideo;
    }

    public void setCategoriaVideo(String categoriaVideo) {
        CategoriaVideo = categoriaVideo;
    }

    public Spinner getSpnCategoria() {
        return spnCategoria;
    }

    public void setSpnCategoria(Spinner spnCategoria) {
        this.spnCategoria = spnCategoria;
    }

    public boolean isStaVedendo() {
        return StaVedendo;
    }

    public void setStaVedendo(boolean staVedendo) {
        StaVedendo = staVedendo;
    }

    public MediaController getMediaController() {
        return mediaController;
    }

    public void setMediaController(MediaController mediaController) {
        this.mediaController = mediaController;
    }

    public boolean isGiaPartito() {
        return GiaPartito;
    }

    public void setGiaPartito(boolean giaPartito) {
        GiaPartito = giaPartito;
    }

    public boolean isSlideShowAttivoIMM() {
        return SlideShowAttivoIMM;
    }

    public void setSlideShowAttivoIMM(boolean slideShowAttivo) {
        SlideShowAttivoIMM = slideShowAttivo;
    }

    public boolean isSlideShowAttivoPEN() {
        return SlideShowAttivoPEN;
    }

    public void setSlideShowAttivoPEN(boolean slideShowAttivo) {
        SlideShowAttivoPEN = slideShowAttivo;
    }

    public ImageView getImgCaricamentoPEN() {
        return imgCaricamentoPEN;
    }

    public void setImgCaricamentoPEN(ImageView imgCaricamentoPEN) {
        this.imgCaricamentoPEN = imgCaricamentoPEN;
    }

    public ImageView getImgCaricamentoIMM() {
        return imgCaricamentoIMM;
    }

    public void setImgCaricamentoIMM(ImageView imgCaricamentoIMM) {
        this.imgCaricamentoIMM = imgCaricamentoIMM;
    }

    public ImageView getImgCaricamentoVID() {
        return imgCaricamentoVID;
    }

    public void setImgCaricamentoVID(ImageView imgCaricamentoVID) {
        this.imgCaricamentoVID = imgCaricamentoVID;
    }

    public ImmagineZoomabile getImgPennetta() {
        return imgPennetta;
    }

    public void setImgPennetta(ImmagineZoomabile imgPennetta) {
        this.imgPennetta = imgPennetta;
    }

    public ImmagineZoomabile getImgImmagini() {
        return imgImmagini;
    }

    public void setImgImmagini(ImmagineZoomabile imgImmagini) {
        this.imgImmagini = imgImmagini;
    }

    public ProportionalVideoView getVideoView() {
        return videoView;
    }

    public void setVideoView(ProportionalVideoView videoView) {
        this.videoView = videoView;
    }
}
