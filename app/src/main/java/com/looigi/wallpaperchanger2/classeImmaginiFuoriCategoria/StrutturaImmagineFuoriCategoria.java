package com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria;

public class StrutturaImmagineFuoriCategoria {
    private int idImmagine;
    private int idCategoria;
    private String Categoria;
    private String Alias;
    private String Tag;
    private String Cartella;
    private String NomeFile;
    private long DimensioneFile;
    private String DataCreazione;
    private String DataModifica;
    private String DimensioniImmagine;
    private String UrlImmagine;
    private String PathImmagine;
    private boolean EsisteImmagine;

    public String getAlias() {
        return Alias;
    }

    public void setAlias(String alias) {
        Alias = alias;
    }

    public String getCartella() {
        return Cartella;
    }

    public void setCartella(String cartella) {
        Cartella = cartella;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
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

    public long getDimensioneFile() {
        return DimensioneFile;
    }

    public void setDimensioneFile(long dimensioneFile) {
        DimensioneFile = dimensioneFile;
    }

    public String getDimensioniImmagine() {
        return DimensioniImmagine;
    }

    public void setDimensioniImmagine(String dimensioniImmagine) {
        DimensioniImmagine = dimensioniImmagine;
    }

    public boolean isEsisteImmagine() {
        return EsisteImmagine;
    }

    public void setEsisteImmagine(boolean esisteImmagine) {
        EsisteImmagine = esisteImmagine;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public String getNomeFile() {
        return NomeFile;
    }

    public void setNomeFile(String nomeFile) {
        NomeFile = nomeFile;
    }

    public String getPathImmagine() {
        return PathImmagine;
    }

    public void setPathImmagine(String pathImmagine) {
        PathImmagine = pathImmagine;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }

    public String getUrlImmagine() {
        return UrlImmagine;
    }

    public void setUrlImmagine(String urlImmagine) {
        UrlImmagine = urlImmagine;
    }
}
