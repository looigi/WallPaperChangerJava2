package com.looigi.wallpaperchanger2.classeLazio.Strutture;

public class StrutturaMarcatori {
    private String Cognome;
    private String Nome;
    private String Squadra;
    private String Ruolo;
    private int Goals;
    private int idApiFootball;

    public int getIdApiFootball() {
        return idApiFootball;
    }

    public void setIdApiFootball(int idApiFootball) {
        this.idApiFootball = idApiFootball;
    }

    public String getRuolo() {
        return Ruolo;
    }

    public void setRuolo(String ruolo) {
        Ruolo = ruolo;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public int getGoals() {
        return Goals;
    }

    public void setGoals(int goals) {
        Goals = goals;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public String getSquadra() {
        return Squadra;
    }

    public void setSquadra(String squadra) {
        Squadra = squadra;
    }
}
