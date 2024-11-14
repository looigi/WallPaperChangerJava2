package com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini;

import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;

import java.util.List;

public class VariabiliStaticheRefresh {
    private static VariabiliStaticheRefresh instance = null;

    private VariabiliStaticheRefresh() {
    }

    public static VariabiliStaticheRefresh getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheRefresh();
        }

        return instance;
    }

    private List<StrutturaImmaginiPerRefresh> listaImmaginiIoNOS;
    private List<StrutturaImmaginiPerRefresh> listaImmaginiLocali;
    private int indiceIoNos = 0;
    private int indiceLocale = 0;
    private List<String> immaginiMancantiSuIoNos;
    private List<String> immaginiMancantiInLocale;
    private boolean aggiornaSoloLocale = false;
    private boolean aggiornaSoloIoNos = false;

    public boolean isAggiornaSoloIoNos() {
        return aggiornaSoloIoNos;
    }

    public void setAggiornaSoloIoNos(boolean aggiornaSoloIoNos) {
        this.aggiornaSoloIoNos = aggiornaSoloIoNos;
    }

    public boolean isAggiornaSoloLocale() {
        return aggiornaSoloLocale;
    }

    public void setAggiornaSoloLocale(boolean aggiornaSoloLocale) {
        this.aggiornaSoloLocale = aggiornaSoloLocale;
    }

    public List<String> getImmaginiMancantiInLocale() {
        return immaginiMancantiInLocale;
    }

    public void setImmaginiMancantiInLocale(List<String> immaginiMancantiInLocale) {
        this.immaginiMancantiInLocale = immaginiMancantiInLocale;
    }

    public List<String> getImmaginiMancantiSuIoNos() {
        return immaginiMancantiSuIoNos;
    }

    public void setImmaginiMancantiSuIoNos(List<String> immaginiMancantiSuIoNos) {
        this.immaginiMancantiSuIoNos = immaginiMancantiSuIoNos;
    }

    public int getIndiceIoNos() {
        return indiceIoNos;
    }

    public void setIndiceIoNos(int indiceIoNos) {
        this.indiceIoNos = indiceIoNos;
    }

    public int getIndiceLocale() {
        return indiceLocale;
    }

    public void setIndiceLocale(int indiceLocale) {
        this.indiceLocale = indiceLocale;
    }

    public List<StrutturaImmaginiPerRefresh> getListaImmaginiIoNOS() {
        return listaImmaginiIoNOS;
    }

    public void setListaImmaginiIoNOS(List<StrutturaImmaginiPerRefresh> listaImmaginiIoNOS) {
        this.listaImmaginiIoNOS = listaImmaginiIoNOS;
    }

    public List<StrutturaImmaginiPerRefresh> getListaImmaginiLocali() {
        return listaImmaginiLocali;
    }

    public void setListaImmaginiLocali(List<StrutturaImmaginiPerRefresh> listaImmaginiLocali) {
        this.listaImmaginiLocali = listaImmaginiLocali;
    }
}
