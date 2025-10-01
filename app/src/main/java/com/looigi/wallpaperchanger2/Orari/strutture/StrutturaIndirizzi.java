package com.looigi.wallpaperchanger2.Orari.strutture;

public class StrutturaIndirizzi {
    private int idIndirizzo;
    private String Indirizzo;
    private String DataInizio;
    private String DataFine;
    private String LatLng;

    public int getIdIndirizzo() {
        return idIndirizzo;
    }

    public void setIdIndirizzo(int idIndirizzo) {
        this.idIndirizzo = idIndirizzo;
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

    public String getLatLng() {
        return LatLng;
    }

    public void setLatLng(String latLng) {
        LatLng = latLng;
    }
}
