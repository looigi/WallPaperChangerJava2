package com.looigi.wallpaperchanger2.Onomastici.strutture;

public class StrutturaCompleanno {
    private int id;
    private int Giorno;
    private int Mese;
    private int Anno;
    private String Nome;
    private String Cognome;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCognome() {
        return Cognome;
    }

    public void setCognome(String cognome) {
        Cognome = cognome;
    }

    public int getAnno() {
        return Anno;
    }

    public void setAnno(int anno) {
        Anno = anno;
    }

    public int getGiorno() {
        return Giorno;
    }

    public void setGiorno(int giorno) {
        Giorno = giorno;
    }

    public int getMese() {
        return Mese;
    }

    public void setMese(int mese) {
        Mese = mese;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String nome) {
        Nome = nome;
    }
}
