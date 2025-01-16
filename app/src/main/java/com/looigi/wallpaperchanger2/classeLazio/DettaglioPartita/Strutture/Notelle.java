package com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.Strutture;

public class Notelle {
    // notelle = Rec("Arbitro").Value & ";" & Rec("Localita").Value & ";" & Rec("Spettatori").Value & ";" & Replace(Rec("Notelle").Value, ";", "***PV***") & ";"
    private String Arbitro;
    private String Localita;
    private String Spettatori;
    private String Notelle;

    public String getArbitro() {
        return Arbitro;
    }

    public void setArbitro(String arbitro) {
        Arbitro = arbitro;
    }

    public String getLocalita() {
        return Localita;
    }

    public void setLocalita(String localita) {
        Localita = localita;
    }

    public String getNotelle() {
        return Notelle;
    }

    public void setNotelle(String notelle) {
        Notelle = notelle;
    }

    public String getSpettatori() {
        return Spettatori;
    }

    public void setSpettatori(String spettatori) {
        Spettatori = spettatori;
    }
}
