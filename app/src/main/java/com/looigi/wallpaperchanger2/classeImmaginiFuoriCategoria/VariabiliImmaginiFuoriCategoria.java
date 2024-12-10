package com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeImmaginiUguali.AdapterListenerImmaginiUguali;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.AdapterListenerTipi;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.StrutturaImmaginiUguali;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.StrutturaImmaginiUgualiRitornate;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliImmaginiFuoriCategoria {
    private static VariabiliImmaginiFuoriCategoria instance = null;

    private VariabiliImmaginiFuoriCategoria() {
    }

    public static VariabiliImmaginiFuoriCategoria getInstance() {
        if (instance == null) {
            instance = new VariabiliImmaginiFuoriCategoria();
        }

        return instance;
    }

    private String idCategoria = "";
    private String Categoria = "";
    private String Alias1 = "";
    private String Alias2 = "";
    private String Tag = "";
    private int QuantiCaratteri = 4;
    private String AndOr = "Or";
    private boolean SoloSuAltro = true;
    private boolean CercaExif = false;
    private GifImageView imgCaricamento;
    private ListView lstImmagini;
    private LinearLayout laypreview;
    private ImmagineZoomabile imgPreview;
    private TextView txtQuanteImmaginiRilevate;

    public TextView getTxtQuanteImmaginiRilevate() {
        return txtQuanteImmaginiRilevate;
    }

    public void setTxtQuanteImmaginiRilevate(TextView txtQuanteImmaginiRilevate) {
        this.txtQuanteImmaginiRilevate = txtQuanteImmaginiRilevate;
    }

    public ImmagineZoomabile getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(ImmagineZoomabile imgPreview) {
        this.imgPreview = imgPreview;
    }

    public LinearLayout getLaypreview() {
        return laypreview;
    }

    public void setLaypreview(LinearLayout laypreview) {
        this.laypreview = laypreview;
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

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getAlias1() {
        return Alias1;
    }

    public void setAlias1(String alias1) {
        Alias1 = alias1;
    }

    public String getAlias2() {
        return Alias2;
    }

    public void setAlias2(String alias2) {
        Alias2 = alias2;
    }

    public String getAndOr() {
        return AndOr;
    }

    public void setAndOr(String andOr) {
        AndOr = andOr;
    }

    public boolean isCercaExif() {
        return CercaExif;
    }

    public void setCercaExif(boolean cercaExif) {
        CercaExif = cercaExif;
    }

    public int getQuantiCaratteri() {
        return QuantiCaratteri;
    }

    public void setQuantiCaratteri(int quantiCaratteri) {
        QuantiCaratteri = quantiCaratteri;
    }

    public boolean isSoloSuAltro() {
        return SoloSuAltro;
    }

    public void setSoloSuAltro(boolean soloSuAltro) {
        SoloSuAltro = soloSuAltro;
    }

    public String getTag() {
        return Tag;
    }

    public void setTag(String tag) {
        Tag = tag;
    }
}
