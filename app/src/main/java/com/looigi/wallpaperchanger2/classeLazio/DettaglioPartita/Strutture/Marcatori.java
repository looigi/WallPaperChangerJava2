package com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.Strutture;

public class Marcatori {
    private int idGiocatore;
    private String Cognome;
    private String Nome;
    private int Minuto;
    private boolean Rigore;
    private int idApiFootball;

    public int getIdApiFootball() {
        return idApiFootball;
    }

    public void setIdApiFootball(int idApiFootball) {
        this.idApiFootball = idApiFootball;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public int getIdGiocatore() {
        return idGiocatore;
    }

    public void setIdGiocatore(int idGiocatore) {
        this.idGiocatore = idGiocatore;
    }

    public int getMinuto() {
        return Minuto;
    }

    public void setMinuto(int minuto) {
        Minuto = minuto;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public boolean isRigore() {
        return Rigore;
    }

    public void setRigore(boolean rigore) {
        Rigore = rigore;
    }
}
