package com.looigi.wallpaperchanger2.Wallpaper.RefreshImmagini;

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
    private int indiceCopiaIoNos = 0;
    private int indiceCopiaLocale = 0;
    private int indiceEliminazioneIoNos = 0;
    private int indiceEliminazioneLocale = 0;
    private List<String> immaginiMancantiSuIoNos;
    private List<String> immaginiMancantiInLocale;
    private List<String> immaginiDaEliminareDaIoNos;
    private List<String> immaginiDaEliminareDaLocale;
    private boolean aggiornaSoloLocale = false;
    private boolean aggiornaSoloIoNos = false;

    public int getIndiceEliminazioneIoNos() {
        return indiceEliminazioneIoNos;
    }

    public void setIndiceEliminazioneIoNos(int indiceEliminazioneIoNos) {
        this.indiceEliminazioneIoNos = indiceEliminazioneIoNos;
    }

    public int getIndiceEliminazioneLocale() {
        return indiceEliminazioneLocale;
    }

    public void setIndiceEliminazioneLocale(int indiceEliminazioneLocale) {
        this.indiceEliminazioneLocale = indiceEliminazioneLocale;
    }

    public List<String> getImmaginiDaEliminareDaIoNos() {
        return immaginiDaEliminareDaIoNos;
    }

    public void setImmaginiDaEliminareDaIoNos(List<String> immaginiDaEliminareDaIoNos) {
        this.immaginiDaEliminareDaIoNos = immaginiDaEliminareDaIoNos;
    }

    public List<String> getImmaginiDaEliminareDaLocale() {
        return immaginiDaEliminareDaLocale;
    }

    public void setImmaginiDaEliminareDaLocale(List<String> immaginiDaEliminareDaLocale) {
        this.immaginiDaEliminareDaLocale = immaginiDaEliminareDaLocale;
    }

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

    public int getIndiceCopiaIoNos() {
        return indiceCopiaIoNos;
    }

    public void setIndiceCopiaIoNos(int indiceCopiaIoNos) {
        this.indiceCopiaIoNos = indiceCopiaIoNos;
    }

    public int getIndiceCopiaLocale() {
        return indiceCopiaLocale;
    }

    public void setIndiceCopiaLocale(int indiceCopiaLocale) {
        this.indiceCopiaLocale = indiceCopiaLocale;
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
