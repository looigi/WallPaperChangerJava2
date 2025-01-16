package com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.Strutture;

import java.util.List;

public class Dettaglio {
    private List<Ammoniti> AmmonitiCasa;
    private List<Ammoniti> AmmonitiFuori;
    private List<Espulsi> EspulsiCasa;
    private List<Espulsi> EspulsiFuori;
    private List<Marcatori> MarcatoriCasa;
    private List<Marcatori> MarcatoriFuori;
    private List<Formazione> FormazioneCasa;
    private List<Formazione> FormazioneFuori;
    private Notelle Notelle;

    public List<Ammoniti> getAmmonitiCasa() {
        return AmmonitiCasa;
    }

    public void setAmmonitiCasa(List<Ammoniti> ammonitiCasa) {
        AmmonitiCasa = ammonitiCasa;
    }

    public List<Ammoniti> getAmmonitiFuori() {
        return AmmonitiFuori;
    }

    public void setAmmonitiFuori(List<Ammoniti> ammonitiFuori) {
        AmmonitiFuori = ammonitiFuori;
    }

    public List<Espulsi> getEspulsiCasa() {
        return EspulsiCasa;
    }

    public void setEspulsiCasa(List<Espulsi> espulsiCasa) {
        EspulsiCasa = espulsiCasa;
    }

    public List<Espulsi> getEspulsiFuori() {
        return EspulsiFuori;
    }

    public void setEspulsiFuori(List<Espulsi> espulsiFuori) {
        EspulsiFuori = espulsiFuori;
    }

    public List<Formazione> getFormazioneCasa() {
        return FormazioneCasa;
    }

    public void setFormazioneCasa(List<Formazione> formazioneCasa) {
        FormazioneCasa = formazioneCasa;
    }

    public List<Formazione> getFormazioneFuori() {
        return FormazioneFuori;
    }

    public void setFormazioneFuori(List<Formazione> formazioneFuori) {
        this.FormazioneFuori = formazioneFuori;
    }

    public List<Marcatori> getMarcatoriCasa() {
        return MarcatoriCasa;
    }

    public void setMarcatoriCasa(List<Marcatori> marcatoriCasa) {
        MarcatoriCasa = marcatoriCasa;
    }

    public List<Marcatori> getMarcatoriFuori() {
        return MarcatoriFuori;
    }

    public void setMarcatoriFuori(List<Marcatori> marcatoriFuori) {
        MarcatoriFuori = marcatoriFuori;
    }

    public com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.Strutture.Notelle getNotelle() {
        return Notelle;
    }

    public void setNotelle(com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.Strutture.Notelle notelle) {
        Notelle = notelle;
    }
}
