package com.looigi.wallpaperchanger2.classePlayer.Strutture;

public class StrutturaUtenti {
    private int id;
    private String Utente;
    private String Password;
    private boolean Amministratore;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUtente() {
        return Utente;
    }

    public void setUtente(String utente) {
        Utente = utente;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public boolean isAmministratore() {
        return Amministratore;
    }

    public void setAmministratore(boolean amministratore) {
        Amministratore = amministratore;
    }
}
