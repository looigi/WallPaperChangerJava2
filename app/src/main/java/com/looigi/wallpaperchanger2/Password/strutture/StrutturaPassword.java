package com.looigi.wallpaperchanger2.Password.strutture;

public class StrutturaPassword {
    private int idRiga = -1;
    private String Sito = "";
    private String Utenza = "";
    private String Password = "";
    private String Note = "";
    private String Indirizzo = "";

    public int getIdRiga() {
        return idRiga;
    }

    public void setIdRiga(int idRiga) {
        this.idRiga = idRiga;
    }

    public String getSito() {
        return Sito;
    }

    public void setSito(String sito) {
        Sito = sito;
    }

    public String getUtenza() {
        return Utenza;
    }

    public void setUtenza(String utenza) {
        Utenza = utenza;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getIndirizzo() {
        return Indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        Indirizzo = indirizzo;
    }
}
