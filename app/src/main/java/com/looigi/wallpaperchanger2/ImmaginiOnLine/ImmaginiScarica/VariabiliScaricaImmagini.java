package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiScarica;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;

import java.io.File;
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
    // private LinearLayout layPreview;
    // private ImmagineZoomabile imgPreview;
    private ListView lstImmagini;
    private List<String> listaOriginaleDaScaricare = new ArrayList<>();
    private boolean scaricataBene;
    private int qualeImmagineStoScaricando;
    private boolean MascheraAttiva = false;

    public boolean isMascheraAttiva() {
        return MascheraAttiva;
    }

    public void setMascheraAttiva(boolean mascheraAttiva) {
        MascheraAttiva = mascheraAttiva;
    }

    public int getQualeImmagineStoScaricando() {
        return qualeImmagineStoScaricando;
    }

    public void setQualeImmagineStoScaricando(int qualeImmagineStoScaricando) {
        this.qualeImmagineStoScaricando = qualeImmagineStoScaricando;
    }

    public boolean isScaricataBene() {
        return scaricataBene;
    }

    public void setScaricataBene(boolean scaricataBene) {
        this.scaricataBene = scaricataBene;
    }

    public List<String> getListaOriginaleDaScaricare() {
        return listaOriginaleDaScaricare;
    }

    public void setListaOriginaleDaScaricare(List<String> listaOriginaleDaScaricare) {
        this.listaOriginaleDaScaricare = listaOriginaleDaScaricare;
    }

    public ListView getLstImmagini() {
        return lstImmagini;
    }

    public void setLstImmagini(ListView lstImmagini) {
        this.lstImmagini = lstImmagini;
    }

    /*
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
    */

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

    public void PulisceCartellaAppoggio(Context context) {
        Files.getInstance().CreaCartelle(context.getFilesDir() + "/AppoggioLW");

        File file = new File(context.getFilesDir() + "/AppoggioLW");
        String[] myFiles;

        myFiles = file.list();
        for (int i=0; i<myFiles.length; i++) {
            File myFile = new File(file, myFiles[i]);
            myFile.delete();
        }
    }
}
