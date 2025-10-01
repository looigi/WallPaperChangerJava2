package com.looigi.wallpaperchanger2.Wallpaper.RefreshImmagini;

public class StrutturaImmaginiPerRefresh {
    private String NomeFile;
    private String Data;
    private String Dimensione;

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getDimensione() {
        return Dimensione;
    }

    public void setDimensione(String dimensione) {
        Dimensione = dimensione;
    }

    public String getNomeFile() {
        return NomeFile;
    }

    public void setNomeFile(String nomeFile) {
        NomeFile = nomeFile;
    }
}
