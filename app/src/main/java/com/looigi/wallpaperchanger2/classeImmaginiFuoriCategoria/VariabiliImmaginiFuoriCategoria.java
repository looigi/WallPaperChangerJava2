package com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.adapters.AdapterListenerImmaginiFuoricategoria;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;

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
    private List<StrutturaImmagineFuoriCategoria> listaImmagini;
    private int qualeImmagineStaSpostando = -1;
    private boolean staSpostandoTutte = false;
    private List<StrutturaImmagineFuoriCategoria> listaDaSpostare;
    private AdapterListenerImmaginiFuoricategoria adapter;

    public AdapterListenerImmaginiFuoricategoria getAdapter() {
        return adapter;
    }

    public void setAdapter(AdapterListenerImmaginiFuoricategoria adapter) {
        this.adapter = adapter;
    }

    public List<StrutturaImmagineFuoriCategoria> getListaDaSpostare() {
        return listaDaSpostare;
    }

    public void setListaDaSpostare(List<StrutturaImmagineFuoriCategoria> listaDaSpostare) {
        this.listaDaSpostare = listaDaSpostare;
    }

    public boolean isStaSpostandoTutte() {
        return staSpostandoTutte;
    }

    public void setStaSpostandoTutte(boolean staSpostandoTutte) {
        this.staSpostandoTutte = staSpostandoTutte;
    }

    public int getQualeImmagineStaSpostando() {
        return qualeImmagineStaSpostando;
    }

    public void setQualeImmagineStaSpostando(int qualeImmagineStaSpostando) {
        this.qualeImmagineStaSpostando = qualeImmagineStaSpostando;
    }

    public List<StrutturaImmagineFuoriCategoria> getListaImmagini() {
        return listaImmagini;
    }

    public void setListaImmagini(List<StrutturaImmagineFuoriCategoria> listaImmagini) {
        this.listaImmagini = listaImmagini;
    }

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

    public void ScaricaProssimaImmagine(Context context, int quale) {
        StrutturaImmagineFuoriCategoria s = VariabiliImmaginiFuoriCategoria.getInstance().getListaDaSpostare().get(quale);
        StrutturaImmaginiLibrary Imm =  new StrutturaImmaginiLibrary();
        Imm.setAlias(s.getAlias());
        Imm.setCategoria(s.getCategoria());
        Imm.setCartella(s.getCartella());
        Imm.setIdCategoria(s.getIdCategoria());
        Imm.setTag(s.getTag());
        Imm.setDataCreazione(s.getDataCreazione());
        Imm.setDataModifica(s.getDataModifica());
        Imm.setDimensioneFile((int) s.getDimensioneFile());
        Imm.setIdImmagine(s.getIdImmagine());
        Imm.setDimensioniImmagine(s.getDimensioniImmagine());
        Imm.setNomeFile(s.getNomeFile());
        Imm.setPathImmagine(s.getPathImmagine());
        Imm.setUrlImmagine(s.getUrlImmagine());

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.SpostaImmagine(Imm, "FC");
    }
}
