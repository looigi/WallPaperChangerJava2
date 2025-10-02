package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiModifica;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class VariabiliStaticheModificaImmagine {
    private static VariabiliStaticheModificaImmagine instance = null;

    private VariabiliStaticheModificaImmagine() {
    }

    public static VariabiliStaticheModificaImmagine getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheModificaImmagine();
        }

        return instance;
    }

    private Activity mainActivity;
    private String NomeImmagine;
    private String mascheraApertura;
    private ImageView imgAttendere;

    public void ChiudeActivity(boolean Finish) {
        if (mainActivity != null) {
            // mainActivity.moveTaskToBack(true);
            // if (Finish) {
            mainActivity.finish();
            // }
        }
    }

    /* public void ImpostaAttesa(boolean Attesa) {
        if (imgAttendere != null) {
            if (!Attesa) {
                imgAttendere.setVisibility(LinearLayout.GONE);
            } else {
                imgAttendere.setVisibility(LinearLayout.VISIBLE);
            }
        }
    } */

    public ImageView getImgAttendere() {
        return imgAttendere;
    }

    public void setImgAttendere(ImageView imgAttendere) {
        this.imgAttendere = imgAttendere;
    }

    public String getMascheraApertura() {
        return mascheraApertura;
    }

    public void setMascheraApertura(String mascheraApertura) {
        this.mascheraApertura = mascheraApertura;
    }

    public String getNomeImmagine() {
        return NomeImmagine;
    }

    public void setNomeImmagine(String nomeImmagine) {
        NomeImmagine = nomeImmagine;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }
}
