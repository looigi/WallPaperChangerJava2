package com.looigi.wallpaperchanger2.classeImmaginiRaggruppate;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.strutture.StrutturaImmagineRaggruppata;
import com.looigi.wallpaperchanger2.classePlayer.Adapters.AdapterListenerArtisti;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaArtisti;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaSalvataggi;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaTags;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaUtenti;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadCanzone;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadImmagine;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.InterrogazioneWSPlayer;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheImmaginiRaggruppate {
    private static VariabiliStaticheImmaginiRaggruppate instance = null;

    private VariabiliStaticheImmaginiRaggruppate() {
    }

    public static VariabiliStaticheImmaginiRaggruppate getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheImmaginiRaggruppate();
        }

        return instance;
    }

    private String idCategoria;
    private GifImageView imgCaricamento;
    private ListView lstIR;
    private ListView lstImmagini;
    private List<StrutturaImmaginiCategorie> listaCategorieIMM;
    private LinearLayout laypreview;
    private ImmagineZoomabile imgPreview;
    private String CategoriaImpostata;
    private Spinner spnCategorie;
    private String Filtro;
    private int Precisione = 4;

    public int getPrecisione() {
        return Precisione;
    }

    public void setPrecisione(int precisione) {
        Precisione = precisione;
    }

    public String getFiltro() {
        return Filtro;
    }

    public void setFiltro(String filtro) {
        Filtro = filtro;
    }

    public Spinner getSpnCategorie() {
        return spnCategorie;
    }

    public void setSpnCategorie(Spinner spnCategorie) {
        this.spnCategorie = spnCategorie;
    }

    public String getCategoriaImpostata() {
        return CategoriaImpostata;
    }

    public void setCategoriaImpostata(String categoriaImpostata) {
        CategoriaImpostata = categoriaImpostata;
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

    public List<StrutturaImmaginiCategorie> getListaCategorieIMM() {
        return listaCategorieIMM;
    }

    public void setListaCategorieIMM(List<StrutturaImmaginiCategorie> listaCategorieIMM) {
        this.listaCategorieIMM = listaCategorieIMM;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public ListView getLstImmagini() {
        return lstImmagini;
    }

    public void setLstImmagini(ListView lstImmagini) {
        this.lstImmagini = lstImmagini;
    }

    public ListView getLstIR() {
        return lstIR;
    }

    public void setLstIR(ListView lstIR) {
        this.lstIR = lstIR;
    }

    public void Attesa(boolean bAttesa) {
        if (imgCaricamento != null) {
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

    private int idImmagineDaSpostare;
    private boolean StaSpostandoImmagini = false;
    private List<StrutturaImmagineRaggruppata> listaImmagini;

    public List<StrutturaImmagineRaggruppata> getListaImmagini() {
        return listaImmagini;
    }

    public void setListaImmagini(List<StrutturaImmagineRaggruppata> listaImmagini) {
        this.listaImmagini = listaImmagini;
    }

    public boolean isStaSpostandoImmagini() {
        return StaSpostandoImmagini;
    }

    public void setStaSpostandoImmagini(boolean staSpostandoImmagini) {
        StaSpostandoImmagini = staSpostandoImmagini;
    }

    public int getIdImmagineDaSpostare() {
        return idImmagineDaSpostare;
    }

    public void setIdImmagineDaSpostare(int idImmagineDaSpostare) {
        this.idImmagineDaSpostare = idImmagineDaSpostare;
    }

    public void SpostaTutteLeImmagini(Context context) {
        StrutturaImmagineRaggruppata Immagine = listaImmagini.get(
                VariabiliStaticheImmaginiRaggruppate.getInstance().getIdImmagineDaSpostare()
        );
        StrutturaImmaginiLibrary Imm =  new StrutturaImmaginiLibrary();
        Imm.setAlias(Immagine.getAlias());
        Imm.setCategoria(Immagine.getCategoria());
        Imm.setCartella(Immagine.getCartella());
        Imm.setIdCategoria(Immagine.getIdCategoria());
        Imm.setTag(Immagine.getTag());
        Imm.setDataCreazione(Immagine.getDataCreazione());
        Imm.setDataModifica(Immagine.getDataModifica());
        Imm.setDimensioneFile((int) Immagine.getDimensioneFile());
        Imm.setIdImmagine(Immagine.getIdImmagine());
        Imm.setDimensioniImmagine(Immagine.getDimensioniImmagine());
        Imm.setNomeFile(Immagine.getNomeFile());
        Imm.setPathImmagine(Immagine.getPathImmagine());
        Imm.setUrlImmagine(Immagine.getUrlImmagine());

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.SpostaImmagine(Imm, "IR");
    }
}
