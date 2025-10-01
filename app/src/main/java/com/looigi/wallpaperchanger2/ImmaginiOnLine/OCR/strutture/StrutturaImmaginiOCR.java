package com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.strutture;

public class StrutturaImmaginiOCR {
    private int idImmagine;
    private String URL;
    private String Testo;
    private int idCategoriaOrigine;
    private String CategoriaOrigine;
    private String CategorieDestinazione;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public String getTesto() {
        return Testo;
    }

    public void setTesto(String testo) {
        Testo = testo;
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

    public String getCategorieDestinazione() {
        return CategorieDestinazione;
    }

    public void setCategorieDestinazione(String categorieDestinazione) {
        this.CategorieDestinazione = categorieDestinazione;
    }
}
