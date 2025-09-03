package com.looigi.wallpaperchanger2.classeAntifurto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.utilities.log.LogInterno;
import com.looigi.wallpaperchanger2.utilities.meteo.struttura.StrutturaMeteo;

import java.util.Date;

public class VariabiliStaticheAntifurto {
    private static VariabiliStaticheAntifurto instance = null;

    private VariabiliStaticheAntifurto() {
    }

    public static VariabiliStaticheAntifurto getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheAntifurto();
        }

        return instance;
    }
    private boolean AllarmeAttivo = false;
    private Activity actAllarme;
    private boolean AllarmeInCorso = false;
    private TextView txtAllarme;
    private TextView txtInfo1;
    private TextView txtInfo2;
    private boolean AllarmeSuMovimento = true;
    private boolean AllarmeSuBT = true;
    private Float gForcePerAllarme = 1.5F;
    private String btMonitorato = "";

    public String getBtMonitorato() {
        return btMonitorato;
    }

    public void setBtMonitorato(String btMonitorato) {
        this.btMonitorato = btMonitorato;
    }

    public Float getgForcePerAllarme() {
        return gForcePerAllarme;
    }

    public void setgForcePerAllarme(Float gForcePerAllarme) {
        this.gForcePerAllarme = gForcePerAllarme;
    }

    public TextView getTxtInfo2() {
        return txtInfo2;
    }

    public void setTxtInfo2(TextView txtInfo2) {
        this.txtInfo2 = txtInfo2;
    }

    public TextView getTxtInfo1() {
        return txtInfo1;
    }

    public void setTxtInfo1(TextView txtInfo1) {
        this.txtInfo1 = txtInfo1;
    }

    public boolean isAllarmeSuBT() {
        return AllarmeSuBT;
    }

    public void setAllarmeSuBT(boolean allarmeSuBT) {
        AllarmeSuBT = allarmeSuBT;
    }

    public boolean isAllarmeSuMovimento() {
        return AllarmeSuMovimento;
    }

    public void setAllarmeSuMovimento(boolean allarmeSuMovimento) {
        AllarmeSuMovimento = allarmeSuMovimento;
    }

    public TextView getTxtAllarme() {
        return txtAllarme;
    }

    public void setTxtAllarme(TextView txtAllarme) {
        this.txtAllarme = txtAllarme;
    }

    public boolean isAllarmeInCorso() {
        return AllarmeInCorso;
    }

    public void setAllarmeInCorso(boolean allarmeInCorso) {
        AllarmeInCorso = allarmeInCorso;
    }

    public Activity getActAllarme() {
        return actAllarme;
    }

    public void setActAllarme(Activity actAllarme) {
        this.actAllarme = actAllarme;
    }

    public boolean isAllarmeAttivo() {
        return AllarmeAttivo;
    }

    public void setAllarmeAttivo(boolean allarmeAttivo) {
        AllarmeAttivo = allarmeAttivo;
    }
}