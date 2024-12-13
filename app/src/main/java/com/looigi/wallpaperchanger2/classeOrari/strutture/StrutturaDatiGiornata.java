package com.looigi.wallpaperchanger2.classeOrari.strutture;

import java.util.List;

public class StrutturaDatiGiornata {
    private boolean GiornoInserito;
    private int QuanteOre;
    private String Note;
    private String Misti;
    private int CodCommessa;
    private String Entrata;
    private int idLavoro;
    private int idIndirizzo;
    private String Km;
    private String Lavoro;
    private String Indirizzo;
    private String Commessa;
    private String Tempo;
    private String Gradi;
    private List<StrutturaMezzi> MezziAndata;
    private List<StrutturaMezzi> MezziRitorno;
    private List<StrutturaPranzo> Pranzo;
    private List<StrutturaPasticca> Pasticca;
    private String Santo;
    private List<StrutturaRicorrenze> Ricorrenze;
    private List<StrutturaMezziStandard> MezziStandardAndata;
    private List<StrutturaMezziStandard> MezziStandardRitorno;
    private int CommessaDefault;
    private int LavoroDefault;
    private int OreStandard;

    public int getCodCommessa() {
        return CodCommessa;
    }

    public void setCodCommessa(int codCommessa) {
        CodCommessa = codCommessa;
    }

    public String getCommessa() {
        return Commessa;
    }

    public void setCommessa(String commessa) {
        Commessa = commessa;
    }

    public int getCommessaDefault() {
        return CommessaDefault;
    }

    public void setCommessaDefault(int commessaDefault) {
        CommessaDefault = commessaDefault;
    }

    public String getEntrata() {
        return Entrata;
    }

    public void setEntrata(String entrata) {
        Entrata = entrata;
    }

    public boolean isGiornoInserito() {
        return GiornoInserito;
    }

    public void setGiornoInserito(boolean giornoInserito) {
        GiornoInserito = giornoInserito;
    }

    public String getGradi() {
        return Gradi;
    }

    public void setGradi(String gradi) {
        Gradi = gradi;
    }

    public int getIdIndirizzo() {
        return idIndirizzo;
    }

    public void setIdIndirizzo(int idIndirizzo) {
        this.idIndirizzo = idIndirizzo;
    }

    public int getIdLavoro() {
        return idLavoro;
    }

    public void setIdLavoro(int idLavoro) {
        this.idLavoro = idLavoro;
    }

    public String getIndirizzo() {
        return Indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        Indirizzo = indirizzo;
    }

    public String getKm() {
        return Km;
    }

    public void setKm(String km) {
        Km = km;
    }

    public String getLavoro() {
        return Lavoro;
    }

    public void setLavoro(String lavoro) {
        Lavoro = lavoro;
    }

    public int getLavoroDefault() {
        return LavoroDefault;
    }

    public void setLavoroDefault(int lavoroDefault) {
        LavoroDefault = lavoroDefault;
    }

    public List<StrutturaMezzi> getMezziAndata() {
        return MezziAndata;
    }

    public void setMezziAndata(List<StrutturaMezzi> mezziAndata) {
        MezziAndata = mezziAndata;
    }

    public List<StrutturaMezzi> getMezziRitorno() {
        return MezziRitorno;
    }

    public void setMezziRitorno(List<StrutturaMezzi> mezziRitorno) {
        MezziRitorno = mezziRitorno;
    }

    public List<StrutturaMezziStandard> getMezziStandardAndata() {
        return MezziStandardAndata;
    }

    public void setMezziStandardAndata(List<StrutturaMezziStandard> mezziStandardAndata) {
        MezziStandardAndata = mezziStandardAndata;
    }

    public List<StrutturaMezziStandard> getMezziStandardRitorno() {
        return MezziStandardRitorno;
    }

    public void setMezziStandardRitorno(List<StrutturaMezziStandard> mezziStandardRitorno) {
        MezziStandardRitorno = mezziStandardRitorno;
    }

    public String getMisti() {
        return Misti;
    }

    public void setMisti(String misti) {
        Misti = misti;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public int getOreStandard() {
        return OreStandard;
    }

    public void setOreStandard(int oreStandard) {
        OreStandard = oreStandard;
    }

    public List<StrutturaPasticca> getPasticca() {
        return Pasticca;
    }

    public void setPasticca(List<StrutturaPasticca> pasticca) {
        Pasticca = pasticca;
    }

    public List<StrutturaPranzo> getPranzo() {
        return Pranzo;
    }

    public void setPranzo(List<StrutturaPranzo> pranzo) {
        Pranzo = pranzo;
    }

    public int getQuanteOre() {
        return QuanteOre;
    }

    public void setQuanteOre(int quanteOre) {
        QuanteOre = quanteOre;
    }

    public List<StrutturaRicorrenze> getRicorrenze() {
        return Ricorrenze;
    }

    public void setRicorrenze(List<StrutturaRicorrenze> ricorrenze) {
        Ricorrenze = ricorrenze;
    }

    public String getSanto() {
        return Santo;
    }

    public void setSanto(String santo) {
        Santo = santo;
    }

    public String getTempo() {
        return Tempo;
    }

    public void setTempo(String tempo) {
        Tempo = tempo;
    }
}
