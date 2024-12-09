package com.looigi.wallpaperchanger2.classeScaricaImmagini;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;

import java.util.ArrayList;
import java.util.List;

public class VariabiliScaricaImmagini {
    private static VariabiliScaricaImmagini instance = null;

    private VariabiliScaricaImmagini() {
    }

    public static VariabiliScaricaImmagini getInstance() {
        if (instance == null) {
            instance = new VariabiliScaricaImmagini();
        }

        return instance;
    }

    private ImageView imgScaricaDaDisabilitare;
    private List<StrutturaImmagineDaScaricare> listaDaScaricare = new ArrayList<>();
    private TextView txtSelezionate;
    private ImageView imgScaricaTutte;
    private boolean ScaricaMultiplo = false;
    private String Modalita;
    private String Filtro;
    private CheckBox chkSelezione;
    private LinearLayout layPreview;
    private ImmagineZoomabile imgPreview;
    private ListView lstImmagini;

    public ListView getLstImmagini() {
        return lstImmagini;
    }

    public void setLstImmagini(ListView lstImmagini) {
        this.lstImmagini = lstImmagini;
    }

    public ImmagineZoomabile getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(ImmagineZoomabile imgPreview) {
        this.imgPreview = imgPreview;
    }

    public LinearLayout getLayPreview() {
        return layPreview;
    }

    public void setLayPreview(LinearLayout layPreview) {
        this.layPreview = layPreview;
    }

    public CheckBox getChkSelezione() {
        return chkSelezione;
    }

    public void setChkSelezione(CheckBox chkSelezione) {
        this.chkSelezione = chkSelezione;
    }

    public String getFiltro() {
        return Filtro;
    }

    public void setFiltro(String filtro) {
        Filtro = filtro;
    }

    public String getModalita() {
        return Modalita;
    }

    public void setModalita(String modalita) {
        Modalita = modalita;
    }

    public boolean isScaricaMultiplo() {
        return ScaricaMultiplo;
    }

    public void setScaricaMultiplo(boolean scaricaMultiplo) {
        ScaricaMultiplo = scaricaMultiplo;
    }

    public ImageView getImgScaricaTutte() {
        return imgScaricaTutte;
    }

    public void setImgScaricaTutte(ImageView imgScaricaTutte) {
        this.imgScaricaTutte = imgScaricaTutte;
    }

    public TextView getTxtSelezionate() {
        return txtSelezionate;
    }

    public void setTxtSelezionate(TextView txtSelezionate) {
        this.txtSelezionate = txtSelezionate;
    }

    public List<StrutturaImmagineDaScaricare> getListaDaScaricare() {
        return listaDaScaricare;
    }

    public void setListaDaScaricare(List<StrutturaImmagineDaScaricare> listaDaScaricare) {
        this.listaDaScaricare = listaDaScaricare;
    }

    public ImageView getImgScaricaDaDisabilitare() {
        return imgScaricaDaDisabilitare;
    }

    public void setImgScaricaDaDisabilitare(ImageView imgScaricaDaDisabilitare) {
        this.imgScaricaDaDisabilitare = imgScaricaDaDisabilitare;
    }
}