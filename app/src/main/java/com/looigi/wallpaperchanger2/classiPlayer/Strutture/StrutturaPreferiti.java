package com.looigi.wallpaperchanger2.classiPlayer.Strutture;

import java.util.List;

public class StrutturaPreferiti {
    private String NomeArtista;
    private String Immagine;
    private List<String> Tags;

    public List<String> getTags() {
        return Tags;
    }

    public void setTags(List<String> tags) {
        Tags = tags;
    }

    public String getNomeArtista() {
        return NomeArtista;
    }

    public void setNomeArtista(String nomeArtista) {
        NomeArtista = nomeArtista;
    }

    public String getImmagine() {
        return Immagine;
    }

    public void setImmagine(String immagine) {
        Immagine = immagine;
    }
}
