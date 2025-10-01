package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.strutture;

public class StrutturaVoltiRilevati {
    private int idImmagine;
    private int idCategoria;
    private String Categoria;
    private String Confidenza;
    private int idCategoriaOrigine;
    private String CategoriaOrigine;
    private String urlOrigine;
    private String urlDestinazione;
    private String NomeOrigine;
    private String NomeDestinazione;

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public String getNomeOrigine() {
        return NomeOrigine;
    }

    public void setNomeOrigine(String nomeOrigine) {
        NomeOrigine = nomeOrigine;
    }

    public String getNomeDestinazione() {
        return NomeDestinazione;
    }

    public void setNomeDestinazione(String nomeDestinazione) {
        NomeDestinazione = nomeDestinazione;
    }

    public String getUrlOrigine() {
        return urlOrigine;
    }

    public void setUrlOrigine(String urlOrigine) {
        this.urlOrigine = urlOrigine;
    }

    public String getUrlDestinazione() {
        return urlDestinazione;
    }

    public void setUrlDestinazione(String urlDestinazione) {
        this.urlDestinazione = urlDestinazione;
    }

    public int getIdCategoriaOrigine() {
        return idCategoriaOrigine;
    }

    public void setIdCategoriaOrigine(int idCategoriaOrigine) {
        this.idCategoriaOrigine = idCategoriaOrigine;
    }

    public String getCategoriaOrigine() {
        return CategoriaOrigine;
    }

    public void setCategoriaOrigine(String categoriaOrigine) {
        CategoriaOrigine = categoriaOrigine;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getConfidenza() {
        return Confidenza;
    }

    public void setConfidenza(String confidenza) {
        Confidenza = confidenza;
    }
}
