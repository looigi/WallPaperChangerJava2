package com.looigi.wallpaperchanger2.classeImmaginiUguali;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.looigi.wallpaperchanger2.classeImmaginiUguali.adapters.AdapterListenerImmaginiUguali;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.adapters.AdapterListenerTipi;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliImmaginiUguali {
    private static VariabiliImmaginiUguali instance = null;

    private VariabiliImmaginiUguali() {
    }

    public static VariabiliImmaginiUguali getInstance() {
        if (instance == null) {
            instance = new VariabiliImmaginiUguali();
        }

        return instance;
    }

    public static final String PathUrl = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaDiscoPublic + "/Materiale/newPLibrary/"; // "http://looigi.no-ip.biz:1085/Materiale/Pennetta/";
    private List<StrutturaImmaginiUguali> lista = new ArrayList<>();
    private String TipoImpostato = "";
    private ListView lstTipi;
    private ListView lstImmagini;
    private GifImageView imgCaricamentoInCorso;
    private String Categoria;
    private List<StrutturaImmaginiUgualiRitornate> lista2 = new ArrayList<>();
    // private LinearLayout layPreview;
    // private ImmagineZoomabile imgPreview;
    private View lastView;

    public View getLastView() {
        return lastView;
    }

    public void setLastView(View lastView) {
        this.lastView = lastView;
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

    public List<StrutturaImmaginiUgualiRitornate> getLista2() {
        return lista2;
    }

    public void setLista2(List<StrutturaImmaginiUgualiRitornate> lista2) {
        this.lista2 = lista2;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public GifImageView getImgCaricamentoInCorso() {
        return imgCaricamentoInCorso;
    }

    public void setImgCaricamentoInCorso(GifImageView imgCaricamentoInCorso) {
        this.imgCaricamentoInCorso = imgCaricamentoInCorso;
    }

    public ListView getLstImmagini() {
        return lstImmagini;
    }

    public void setLstImmagini(ListView lstImmagini) {
        this.lstImmagini = lstImmagini;
    }

    public ListView getLstTipi() {
        return lstTipi;
    }

    public void setLstTipi(ListView lstTipi) {
        this.lstTipi = lstTipi;
    }

    public String getTipoImpostato() {
        return TipoImpostato;
    }

    public void setTipoImpostato(String tipoImpostato) {
        TipoImpostato = tipoImpostato;
    }

    public List<StrutturaImmaginiUguali> getLista() {
        return lista;
    }

    public void setLista(List<StrutturaImmaginiUguali> lista) {
        this.lista = lista;
    }

    public void RiempieListaTipi(Context context) {
        String Tipo = VariabiliImmaginiUguali.getInstance().getTipoImpostato().toUpperCase().trim();

        List<StrutturaImmaginiUguali> listaImmagini = new ArrayList<>();
        for (StrutturaImmaginiUguali s : VariabiliImmaginiUguali.getInstance().getLista()) {
            if (s.getTipo().toUpperCase().trim().equals(Tipo)) {
                listaImmagini.add(s);
            }
        }

        AdapterListenerTipi customAdapterT = new AdapterListenerTipi(
                context,
                listaImmagini);
        VariabiliImmaginiUguali.getInstance().getLstTipi().setAdapter(customAdapterT);

        AdapterListenerImmaginiUguali customAdapterIU = new AdapterListenerImmaginiUguali(
                context,
                new ArrayList<>());
        VariabiliImmaginiUguali.getInstance().getLstImmagini().setAdapter(customAdapterIU);
    }
}
