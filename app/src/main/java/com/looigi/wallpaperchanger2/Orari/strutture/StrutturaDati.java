package com.looigi.wallpaperchanger2.Orari.strutture;

import java.util.List;

public class StrutturaDati {
    private List<StrutturaTempo> Tempi;
    private List<StrutturaLavoro> Lavori;
    private List<StrutturaPranzo> Portate;
    private List<StrutturaIndirizzi> Indirizzi;
    private List<StrutturaMezzi> Mezzi;
    private List<StrutturaPasticca> Pasticche;

    public List<StrutturaPasticca> getPasticche() {
        return Pasticche;
    }

    public void setPasticche(List<StrutturaPasticca> pasticche) {
        Pasticche = pasticche;
    }

    public List<StrutturaTempo> getTempi() {
        return Tempi;
    }

    public void setTempi(List<StrutturaTempo> tempi) {
        Tempi = tempi;
    }

    public List<StrutturaLavoro> getLavori() {
        return Lavori;
    }

    public void setLavori(List<StrutturaLavoro> lavori) {
        Lavori = lavori;
    }

    public List<StrutturaPranzo> getPortate() {
        return Portate;
    }

    public void setPortate(List<StrutturaPranzo> portate) {
        Portate = portate;
    }

    public List<StrutturaIndirizzi> getIndirizzi() {
        return Indirizzi;
    }

    public void setIndirizzi(List<StrutturaIndirizzi> indirizzi) {
        Indirizzi = indirizzi;
    }

    public List<StrutturaMezzi> getMezzi() {
        return Mezzi;
    }

    public void setMezzi(List<StrutturaMezzi> mezzi) {
        Mezzi = mezzi;
    }
}
