package com.looigi.wallpaperchanger2.classeOrari.strutture;

public class StrutturaLavoro {
    private int idLavoro;
    private String Lavoro;
    private String Indirizzo;
    private String DataInizio;
    private String DataFine;
    private String Latlng;

    public int getIdLavoro() {
        return idLavoro;
    }

    public void setIdLavoro(int idLavoro) {
        this.idLavoro = idLavoro;
    }

    public String getLavoro() {
        return Lavoro;
    }

    public void setLavoro(String lavoro) {
        Lavoro = lavoro;
    }

    public String getIndirizzo() {
        return Indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        Indirizzo = indirizzo;
    }

    public String getDataInizio() {
        return DataInizio;
    }

    public void setDataInizio(String dataInizio) {
        DataInizio = dataInizio;
    }

    public String getDataFine() {
        return DataFine;
    }

    public void setDataFine(String dataFine) {
        DataFine = dataFine;
    }

    public String getLatlng() {
        return Latlng;
    }

    public void setLatlng(String latlng) {
        Latlng = latlng;
    }
}
