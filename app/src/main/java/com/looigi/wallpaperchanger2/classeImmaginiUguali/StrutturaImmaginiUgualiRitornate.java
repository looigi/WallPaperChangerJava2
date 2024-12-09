package com.looigi.wallpaperchanger2.classeImmaginiUguali;

public class StrutturaImmaginiUgualiRitornate {
    private String Cartella;
    private int idImmagine;
    private String NomeFile;
    private String DimensioneFile;
    private String DimensioneImmagine;

    public String getDimensioneFile() {
        return DimensioneFile;
    }

    public void setDimensioneFile(String dimensioneFile) {
        DimensioneFile = dimensioneFile;
    }

    public String getDimensioneImmagine() {
        return DimensioneImmagine;
    }

    public void setDimensioneImmagine(String dimensioneImmagine) {
        DimensioneImmagine = dimensioneImmagine;
    }

    public String getCartella() {
        return Cartella;
    }

    public void setCartella(String cartella) {
        Cartella = cartella;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public String getNomeFile() {
        return NomeFile;
    }

    public void setNomeFile(String nomeFile) {
        NomeFile = nomeFile;
    }
}
