package com.looigi.wallpaperchanger2.classeUtilityImmagini;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.strutture.StrutturaImmagineRaggruppata;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.StrutturaImmaginiUguali;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.adapters.AdapterListenerUI;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheUtilityImmagini {
    private static VariabiliStaticheUtilityImmagini instance = null;

    private VariabiliStaticheUtilityImmagini() {
    }

    public static VariabiliStaticheUtilityImmagini getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheUtilityImmagini();
        }

        return instance;
    }

    private int idCategoria;
    private List<StrutturaImmaginiCategorie> listaCategorieIMM;
    private Spinner spnCategorie;
    private GifImageView imgCaricamento;
    private ListView lstImmagini;
    private List<StrutturaControlloImmagini> ControlloImmagini = new ArrayList<>();
    private AdapterListenerUI adapter;
    private boolean ControllaTutto = false;
    private int qualeStaControllando;
    private TextView txtQuale;
    private StrutturaControlloImmagini StrutturaAttuale;
    private String CategoriaAttuale;
    private boolean EsegueAncheRefresh = true;
    private boolean BloccaElaborazione = false;
    private boolean PrimoGiroRefreshAltre = true;
    private String FiltroCategorie = "";

    public String getFiltroCategorie() {
        return FiltroCategorie;
    }

    public void setFiltroCategorie(String filtroCategorie) {
        FiltroCategorie = filtroCategorie;
    }

    public boolean isPrimoGiroRefreshAltre() {
        return PrimoGiroRefreshAltre;
    }

    public void setPrimoGiroRefreshAltre(boolean primoGiroRefreshAltre) {
        PrimoGiroRefreshAltre = primoGiroRefreshAltre;
    }

    public boolean isBloccaElaborazione() {
        return BloccaElaborazione;
    }

    public void setBloccaElaborazione(boolean bloccaElaborazione) {
        BloccaElaborazione = bloccaElaborazione;
    }

    public boolean isEsegueAncheRefresh() {
        return EsegueAncheRefresh;
    }

    public void setEsegueAncheRefresh(boolean esegueAncheRefresh) {
        EsegueAncheRefresh = esegueAncheRefresh;
    }

    public String getCategoriaAttuale() {
        return CategoriaAttuale;
    }

    public void setCategoriaAttuale(String categoriaAttuale) {
        CategoriaAttuale = categoriaAttuale;
    }

    public StrutturaControlloImmagini getStrutturaAttuale() {
        return StrutturaAttuale;
    }

    public void setStrutturaAttuale(StrutturaControlloImmagini strutturaAttuale) {
        StrutturaAttuale = strutturaAttuale;
    }

    public TextView getTxtQuale() {
        return txtQuale;
    }

    public void setTxtQuale(TextView txtQuale) {
        this.txtQuale = txtQuale;
    }

    public int getQualeStaControllando() {
        return qualeStaControllando;
    }

    public void setQualeStaControllando(int qualeStaControllando) {
        this.qualeStaControllando = qualeStaControllando;
    }

    public boolean isControllaTutto() {
        return ControllaTutto;
    }

    public void setControllaTutto(boolean controllaTutto) {
        ControllaTutto = controllaTutto;
    }

    public AdapterListenerUI getAdapter() {
        return adapter;
    }

    public void setAdapter(AdapterListenerUI adapter) {
        this.adapter = adapter;
    }

    public List<StrutturaControlloImmagini> getControlloImmagini() {
        return ControlloImmagini;
    }

    public void setControlloImmagini(List<StrutturaControlloImmagini> controlloImmagini) {
        ControlloImmagini = controlloImmagini;
    }

    public ListView getLstImmagini() {
        return lstImmagini;
    }

    public void setLstImmagini(ListView lstImmagini) {
        this.lstImmagini = lstImmagini;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public List<StrutturaImmaginiCategorie> getListaCategorieIMM() {
        return listaCategorieIMM;
    }

    public void setListaCategorieIMM(List<StrutturaImmaginiCategorie> listaCategorieIMM) {
        this.listaCategorieIMM = listaCategorieIMM;
    }

    public Spinner getSpnCategorie() {
        return spnCategorie;
    }

    public void setSpnCategorie(Spinner spnCategorie) {
        this.spnCategorie = spnCategorie;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public void Attesa(boolean bAttesa) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bAttesa) {
                    imgCaricamento.setVisibility(View.VISIBLE);
                } else {
                    imgCaricamento.setVisibility(View.GONE);
                }
            }
        }, 10);
    }
}
