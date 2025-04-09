package com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.Strutture;

public class Formazione {
    // formCasa &= Rec("idGiocatore").Value & ";" & Rec("Cognome").Value & ";" & Rec("Nome").Value & ";" & Rec("Entrato").Value & ";" & Rec("Uscito").Value & ";" & "§"
    private int idGiocatore;
    private String Cognome;
    private String Nome;
    private int Entrato;
    private int Uscito;
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

    public int getEntrato() {
        return Entrato;
    }

    public void setEntrato(int entrato) {
        Entrato = entrato;
    }

    public int getIdGiocatore() {
        return idGiocatore;
    }

    public void setIdGiocatore(int idGiocatore) {
        this.idGiocatore = idGiocatore;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }

    public int getUscito() {
        return Uscito;
    }

    public void setUscito(int uscito) {
        Uscito = uscito;
    }
}
