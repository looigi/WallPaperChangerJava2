package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.strutture;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.StrutturaImmagineFuoriCategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.StrutturaImmaginiUguali;

import java.util.List;

public class StrutturaControlloImmagini {
    private String Categoria;
    private int idCategoria;
    private int Giuste;
    private int Errate;
    private int Piccole;
    private int Grandi;
    private int Inesistenti;
    private int Invalide;
    private List<String> listaErrate;
    private List<String> listaPiccole;
    private List<String> listaInesistenti;
    private List<String> listaGrandi;
    private List<String> listaInvalide;
    private List<StrutturaImmaginiUguali> listaUguali;
    private List<StrutturaImmagineFuoriCategoria> listaFC;

    public int getInvalide() {
        return Invalide;
    }

    public void setInvalide(int invalide) {
        Invalide = invalide;
    }

    public List<String> getListaInvalide() {
        return listaInvalide;
    }

    public void setListaInvalide(List<String> listaInvalide) {
        this.listaInvalide = listaInvalide;
    }

    public int getGrandi() {
        return Grandi;
    }

    public void setGrandi(int grandi) {
        Grandi = grandi;
    }

    public List<String> getListaGrandi() {
        return listaGrandi;
    }

    public void setListaGrandi(List<String> listaGrandi) {
        this.listaGrandi = listaGrandi;
    }

    public List<StrutturaImmagineFuoriCategoria> getListaFC() {
        return listaFC;
    }

    public void setListaFC(List<StrutturaImmagineFuoriCategoria> listaFC) {
        this.listaFC = listaFC;
    }

    public List<StrutturaImmaginiUguali> getListaUguali() {
        return listaUguali;
    }

    public void setListaUguali(List<StrutturaImmaginiUguali> listaUguali) {
        this.listaUguali = listaUguali;
    }

    public int getPiccole() {
        return Piccole;
    }

    public void setPiccole(int piccole) {
        Piccole = piccole;
    }

    public int getInesistenti() {
        return Inesistenti;
    }

    public void setInesistenti(int inesistenti) {
        Inesistenti = inesistenti;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getGiuste() {
        return Giuste;
    }

    public void setGiuste(int giuste) {
        Giuste = giuste;
    }

    public int getErrate() {
        return Errate;
    }

    public void setErrate(int errate) {
        Errate = errate;
    }

    public List<String> getListaErrate() {
        return listaErrate;
    }

    public void setListaErrate(List<String> listaErrate) {
        this.listaErrate = listaErrate;
    }

    public List<String> getListaPiccole() {
        return listaPiccole;
    }

    public void setListaPiccole(List<String> listaPiccole) {
        this.listaPiccole = listaPiccole;
    }

    public List<String> getListaInesistenti() {
        return listaInesistenti;
    }

    public void setListaInesistenti(List<String> listaInesistenti) {
        this.listaInesistenti = listaInesistenti;
    }
}
