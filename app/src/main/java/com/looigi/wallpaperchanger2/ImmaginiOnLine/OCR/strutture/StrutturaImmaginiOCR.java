package com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.strutture;

public class StrutturaImmaginiOCR {
    private int idImmagine;
    private String URL;
    private String Testo;
    private int idCategoriaOrigine;
    private String CategoriaOrigine;
    private String CategorieDestinazione;
    private String Luoghi;
    private String Oggetti;
    private String Volti;
    private String Descrizione;
    private String SitiRilevati;

    public String getLuoghi() {
        return Luoghi;
    }

    public void setLuoghi(String luoghi) {
        Luoghi = luoghi;
    }

    public String getOggetti() {
        return Oggetti;
    }

    public void setOggetti(String oggetti) {
        Oggetti = oggetti;
    }

    public String getVolti() {
        return Volti;
    }

    public void setVolti(String volti) {
        Volti = volti;
    }

    public String getDescrizione() {
        return Descrizione;
    }

    public void setDescrizione(String descrizione) {
        Descrizione = descrizione;
    }

    public String getSitiRilevati() {
        return SitiRilevati;
    }

    public void setSitiRilevati(String sitiRilevati) {
        SitiRilevati = sitiRilevati;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public String getTesto() {
        return Testo;
    }

    public void setTesto(String testo) {
        Testo = testo;
    }

    public int getIdCategoriaOrigine() {
        return idCategoriaOrigine;
    }

    public void setIdCategoriaOrigine(int idCategoriaOrigine) {
        this.idCategoriaOrigine = idCategoriaOrigine;
    }

    public String getCategoriaOrigine() {
        return CategoriaOrigine;
    }

    public void setCategoriaOrigine(String categoriaOrigine) {
        CategoriaOrigine = categoriaOrigine;
    }

    public String getCategorieDestinazione() {
        return CategorieDestinazione;
    }

    public void setCategorieDestinazione(String categorieDestinazione) {
        this.CategorieDestinazione = categorieDestinazione;
    }
}
