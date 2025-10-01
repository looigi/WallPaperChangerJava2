package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiScarica;

import android.widget.CheckBox;
import android.widget.ImageView;

public class StrutturaImmagineDaScaricare {
    private String urlImmagine;
    private ImageView imgImmagine;
    private CheckBox chkSelezione;

    public CheckBox getChkSelezione() {
        return chkSelezione;
    }

    public void setChkSelezione(CheckBox chkSelezione) {
        this.chkSelezione = chkSelezione;
    }

    public ImageView getImgImmagine() {
        return imgImmagine;
    }

    public void setImgImmagine(ImageView imgImmagine) {
        this.imgImmagine = imgImmagine;
    }

    public String getUrlImmagine() {
        return urlImmagine;
    }

    public void setUrlImmagine(String urlImmagine) {
        this.urlImmagine = urlImmagine;
    }
}
