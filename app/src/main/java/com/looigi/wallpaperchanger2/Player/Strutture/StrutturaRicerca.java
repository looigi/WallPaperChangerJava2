package com.looigi.wallpaperchanger2.Player.Strutture;

public class StrutturaRicerca {
    private int id;
    private String Artista;
    private String Album;
    private String Brano;
    private String Bellezza;
    private String Traccia;

    public String getAlbum() {
        return Album;
    }

    public void setAlbum(String album) {
        Album = album;
    }

    public String getArtista() {
        return Artista;
    }

    public void setArtista(String artista) {
        Artista = artista;
    }

    public String getBellezza() {
        return Bellezza;
    }

    public void setBellezza(String bellezza) {
        Bellezza = bellezza;
    }

    public String getBrano() {
        return Brano;
    }

    public void setBrano(String brano) {
        Brano = brano;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTraccia() {
        return Traccia;
    }

    public void setTraccia(String traccia) {
        Traccia = traccia;
    }
}
