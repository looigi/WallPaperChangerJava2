package com.looigi.wallpaperchanger2.Player.Strutture;

import java.util.List;

public class StrutturaBrano {
    private Integer idBrano;
    private Integer quantiBrani;
    private String Artista;
    private String Album;
    private String Brano;
    private String Anno;
    private String Traccia;
    private String Estensione;
    private String Data;
    private Long Dimensione;
    private List<StrutturaImmagini> Immagini;
    private Integer Ascoltata;
    private Integer Bellezza;
    private String Testo;
    private String TestoTradotto;
    private String UrlBrano;
    private String PathBrano;
    private String CartellaBrano;
    private boolean EsisteBranoSuDisco;
    private String Tags;
    private int TipoBrano;

    public int getTipoBrano() {
        return TipoBrano;
    }

    public void setTipoBrano(int tipoBrano) {
        TipoBrano = tipoBrano;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public Integer getIdBrano() {
        return idBrano;
    }

    public void setIdBrano(Integer idBrano) {
        this.idBrano = idBrano;
    }

    public String getCartellaBrano() {
        return CartellaBrano;
    }

    public void setCartellaBrano(String cartellaBrano) {
        CartellaBrano = cartellaBrano;
    }

    public Integer getQuantiBrani() {
        return quantiBrani;
    }

    public void setQuantiBrani(Integer quantiBrani) {
        this.quantiBrani = quantiBrani;
    }

    public String getArtista() {
        return Artista;
    }

    public void setArtista(String artista) {
        Artista = artista;
    }

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public String getBrano() {
        return Brano;
    }

    public void setBrano(String brano) {
        Brano = brano;
    }

    public String getAnno() {
        return Anno;
    }

    public void setAnno(String anno) {
        Anno = anno;
    }

    public String getTraccia() {
        return Traccia;
    }

    public void setTraccia(String traccia) {
        Traccia = traccia;
    }

    public String getEstensione() {
        return Estensione;
    }

    public void setEstensione(String estensione) {
        Estensione = estensione;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public Long getDimensione() {
        return Dimensione;
    }

    public void setDimensione(Long dimensione) {
        Dimensione = dimensione;
    }

    public List<StrutturaImmagini> getImmagini() {
        return Immagini;
    }

    public void setImmagini(List<StrutturaImmagini> immagini) {
        Immagini = immagini;
    }

    public Integer getAscoltata() {
        return Ascoltata;
    }

    public void setAscoltata(Integer ascoltata) {
        Ascoltata = ascoltata;
    }

    public Integer getBellezza() {
        return Bellezza;
    }

    public void setBellezza(Integer bellezza) {
        Bellezza = bellezza;
    }

    public String getTesto() {
        return Testo;
    }

    public void setTesto(String testo) {
        Testo = testo;
    }

    public String getTestoTradotto() {
        return TestoTradotto;
    }

    public void setTestoTradotto(String testoTradotto) {
        TestoTradotto = testoTradotto;
    }

    public String getUrlBrano() {
        return UrlBrano;
    }

    public void setUrlBrano(String urlBrano) {
        UrlBrano = urlBrano;
    }

    public String getPathBrano() {
        return PathBrano;
    }

    public void setPathBrano(String pathBrano) {
        PathBrano = pathBrano;
    }

    public boolean isEsisteBranoSuDisco() {
        return EsisteBranoSuDisco;
    }

    public void setEsisteBranoSuDisco(boolean esisteBranoSuDisco) {
        EsisteBranoSuDisco = esisteBranoSuDisco;
    }
}
