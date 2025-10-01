package com.looigi.wallpaperchanger2.Orari.strutture;

public class StrutturaCommesse {
    private int idCommessa;
    private String Descrizione;
    private String Commessa;
    private String Cliente;
    private String Indirizzo;
    private String LatLng;
    private String DataInizio;
    private String DataFine;

    public int getIdCommessa() {
        return idCommessa;
    }

    public void setIdCommessa(int idCommessa) {
        this.idCommessa = idCommessa;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public String getCommessa() {
        return Commessa;
    }

    public void setCommessa(String commessa) {
        Commessa = commessa;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getIndirizzo() {
        return Indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        Indirizzo = indirizzo;
    }

    public String getLatLng() {
        return LatLng;
    }

    public void setLatLng(String latLng) {
        LatLng = latLng;
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
}
