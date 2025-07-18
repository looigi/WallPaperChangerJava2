package com.looigi.wallpaperchanger2.classePazzia;

import android.os.Handler;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.VideoView;

import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiCategorie;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

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
    private ImageView imgPennetta;
    private ImageView imgImmagini;
    private VideoView videoView;
    private GifImageView imgCaricamentoPEN;
    private GifImageView imgCaricamentoIMM;
    private GifImageView imgCaricamentoVID;
    private boolean SlideShowAttivoPEN = true;
    private boolean SlideShowAttivoIMM = true;
    private MediaController mediaController;
    private boolean StaVedendo = true;
    private Spinner spnCategoria;
    private int ultimaPennetta;
    private int ultimaImmagine;
    private int ultimoVideo;
    private List<StrutturaImmaginiCategorie> listaCategoriePEN;
    private List<com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie> listaCategorieIMM;
    private List<String> listaCategorieVID;
    private String Modalita;

    // Che si devono salvare
    private String CategoriaPennetta;
    private String CategoriaImmagini;
    private String CategoriaVideo;
    private String FiltroIMM = "";
    private String FiltroPEN = "";
    private String FiltroVID = "";
    // Che si devono salvare

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

    public List<com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie> getListaCategorieIMM() {
        return listaCategorieIMM;
    }

    public void setListaCategorieIMM(List<com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie> listaCategorieIMM) {
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

    public GifImageView getImgCaricamentoPEN() {
        return imgCaricamentoPEN;
    }

    public void setImgCaricamentoPEN(GifImageView imgCaricamentoPEN) {
        this.imgCaricamentoPEN = imgCaricamentoPEN;
    }

    public GifImageView getImgCaricamentoIMM() {
        return imgCaricamentoIMM;
    }

    public void setImgCaricamentoIMM(GifImageView imgCaricamentoIMM) {
        this.imgCaricamentoIMM = imgCaricamentoIMM;
    }

    public GifImageView getImgCaricamentoVID() {
        return imgCaricamentoVID;
    }

    public void setImgCaricamentoVID(GifImageView imgCaricamentoVID) {
        this.imgCaricamentoVID = imgCaricamentoVID;
    }

    public ImageView getImgPennetta() {
        return imgPennetta;
    }

    public void setImgPennetta(ImageView imgPennetta) {
        this.imgPennetta = imgPennetta;
    }

    public ImageView getImgImmagini() {
        return imgImmagini;
    }

    public void setImgImmagini(ImageView imgImmagini) {
        this.imgImmagini = imgImmagini;
    }

    public VideoView getVideoView() {
        return videoView;
    }

    public void setVideoView(VideoView videoView) {
        this.videoView = videoView;
    }
}
