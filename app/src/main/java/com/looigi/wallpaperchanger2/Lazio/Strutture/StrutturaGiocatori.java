package com.looigi.wallpaperchanger2.Lazio.Strutture;

public class StrutturaGiocatori {
    private int idGiocatore;
    private String Cognome;
    private String Nome;
    private String Ruolo;
    private int idRuolo;
    private int idApiFootball;
    // private String Logo;

    /* public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    } */

    public int getIdApiFootball() {
        return idApiFootball;
    }

    public void setIdApiFootball(int idApiFootball) {
        this.idApiFootball = idApiFootball;
    }

    public int getIdGiocatore() {
        return idGiocatore;
    }

    public void setIdGiocatore(int idGiocatore) {
        this.idGiocatore = idGiocatore;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getRuolo() {
        return Ruolo;
    }

    public void setRuolo(String ruolo) {
        Ruolo = ruolo;
    }

    public int getIdRuolo() {
        return idRuolo;
    }

    public void setIdRuolo(int idRuolo) {
        this.idRuolo = idRuolo;
    }
}
