package com.looigi.wallpaperchanger2.classeMostraImmagini.strutture;

public class StrutturaImmaginiLibrary {
    private int idImmagine;
    private int idCategoria;
    private String Categoria;
    private String Alias;
    private String Tag;
    private String Cartella;
    private String NomeFile;
    private int DimensioneFile;
    private String DataCreazione;
    private String DataModifica;
    private String DimensioniImmagine;
    private String UrlImmagine;
    private String PathImmagine;
    private boolean EsisteImmagine;
    private int ImmaginiCategoria;

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getCartella() {
        return Cartella;
    }

    public void setCartella(String cartella) {
        Cartella = cartella;
    }

    public String getNomeFile() {
        return NomeFile;
    }

    public void setNomeFile(String nomeFile) {
        NomeFile = nomeFile;
    }

    public int getDimensioneFile() {
        return DimensioneFile;
    }

    public void setDimensioneFile(int dimensioneFile) {
        DimensioneFile = dimensioneFile;
    }

    public String getDataCreazione() {
        return DataCreazione;
    }

    public void setDataCreazione(String dataCreazione) {
        DataCreazione = dataCreazione;
    }

    public String getDataModifica() {
        return DataModifica;
    }

    public void setDataModifica(String dataModifica) {
        DataModifica = dataModifica;
    }

    public String getDimensioniImmagine() {
        return DimensioniImmagine;
    }

    public void setDimensioniImmagine(String dimensioniImmagine) {
        DimensioniImmagine = dimensioniImmagine;
    }

    public String getUrlImmagine() {
        return UrlImmagine;
    }

    public void setUrlImmagine(String urlImmagine) {
        UrlImmagine = urlImmagine;
    }

    public String getPathImmagine() {
        return PathImmagine;
    }

    public void setPathImmagine(String pathImmagine) {
        PathImmagine = pathImmagine;
    }

    public boolean isEsisteImmagine() {
        return EsisteImmagine;
    }

    public void setEsisteImmagine(boolean esisteImmagine) {
        EsisteImmagine = esisteImmagine;
    }

    public int getImmaginiCategoria() {
        return ImmaginiCategoria;
    }

    public void setImmaginiCategoria(int immaginiCategoria) {
        ImmaginiCategoria = immaginiCategoria;
    }
}
