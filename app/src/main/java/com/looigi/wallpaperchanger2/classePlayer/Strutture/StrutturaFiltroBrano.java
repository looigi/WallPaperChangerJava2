package com.looigi.wallpaperchanger2.classePlayer.Strutture;

public class StrutturaFiltroBrano {
    private String Stelle;
    private String StelleSuperiori;
    private String MaiAscoltata;
    private String Testo;
    private String TestoNon;
    private String Preferiti;
    private String PreferitiElimina;
    private String AndOrPref;
    private String Tags;
    private String TagsElimina;
    private String AndOrTags;
    private String DataSuperiore;
    private String DataInferiore;
    private int idUltimoBrano;
    private String Where;

    public String getWhere() {
        return Where;
    }

    public void setWhere(String where) {
        Where = where;
    }

    public int getIdUltimoBrano() {
        return idUltimoBrano;
    }

    public void setIdUltimoBrano(int idUltimoBrano) {
        this.idUltimoBrano = idUltimoBrano;
    }

    public String getAndOrPref() {
        return AndOrPref;
    }

    public void setAndOrPref(String andOrPref) {
        AndOrPref = andOrPref;
    }

    public String getAndOrTags() {
        return AndOrTags;
    }

    public void setAndOrTags(String andOrTags) {
        AndOrTags = andOrTags;
    }

    public String getDataInferiore() {
        return DataInferiore;
    }

    public void setDataInferiore(String dataInferiore) {
        DataInferiore = dataInferiore;
    }

    public String getDataSuperiore() {
        return DataSuperiore;
    }

    public void setDataSuperiore(String dataSuperiore) {
        DataSuperiore = dataSuperiore;
    }

    public String getMaiAscoltata() {
        return MaiAscoltata;
    }

    public void setMaiAscoltata(String maiAscoltata) {
        MaiAscoltata = maiAscoltata;
    }

    public String getPreferiti() {
        return Preferiti;
    }

    public void setPreferiti(String preferiti) {
        Preferiti = preferiti;
    }

    public String getPreferitiElimina() {
        return PreferitiElimina;
    }

    public void setPreferitiElimina(String preferitiElimina) {
        PreferitiElimina = preferitiElimina;
    }

    public String getStelle() {
        return Stelle;
    }

    public void setStelle(String stelle) {
        Stelle = stelle;
    }

    public String getStelleSuperiori() {
        return StelleSuperiori;
    }

    public void setStelleSuperiori(String stelleSuperiori) {
        StelleSuperiori = stelleSuperiori;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }

    public String getTagsElimina() {
        return TagsElimina;
    }

    public void setTagsElimina(String tagsElimina) {
        TagsElimina = tagsElimina;
    }

    public String getTesto() {
        return Testo;
    }

    public void setTesto(String testo) {
        Testo = testo;
    }

    public String getTestoNon() {
        return TestoNon;
    }

    public void setTestoNon(String testoNon) {
        TestoNon = testoNon;
    }
}
