package com.looigi.wallpaperchanger2.classeLazio.Strutture;

public class StrutturaMercato {
    private int Progressivo;
    private String Data;
    private String Nominativo;
    private int idFonte;
    private String Fonte;
    private int idStato;
    private String Stato;

    public int getProgressivo() {
        return Progressivo;
    }

    public void setProgressivo(int progressivo) {
        Progressivo = progressivo;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getNominativo() {
        return Nominativo;
    }

    public void setNominativo(String nominativo) {
        Nominativo = nominativo;
    }

    public int getIdFonte() {
        return idFonte;
    }

    public void setIdFonte(int idFonte) {
        this.idFonte = idFonte;
    }

    public String getFonte() {
        return Fonte;
    }

    public void setFonte(String fonte) {
        Fonte = fonte;
    }

    public int getIdStato() {
        return idStato;
    }

    public void setIdStato(int idStato) {
        this.idStato = idStato;
    }

    public String getStato() {
        return Stato;
    }

    public void setStato(String stato) {
        Stato = stato;
    }
}
