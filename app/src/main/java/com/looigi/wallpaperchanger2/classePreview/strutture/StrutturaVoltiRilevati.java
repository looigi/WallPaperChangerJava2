package com.looigi.wallpaperchanger2.classePreview.strutture;

public class StrutturaVoltiRilevati {
    private int idCategoria;
    private String Categoria;
    private String Confidenza;
    private int idCategoriaOrigine;
    private String CategoriaOrigine;
    private String urlOrigine;
    private String urlDestinazione;

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
