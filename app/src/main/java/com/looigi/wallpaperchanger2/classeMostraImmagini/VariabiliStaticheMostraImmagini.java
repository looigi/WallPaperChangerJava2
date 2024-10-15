package com.looigi.wallpaperchanger2.classeMostraImmagini;

import android.app.Activity;
import android.content.Context;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeMostraImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeMostraImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheMostraImmagini {
    private static VariabiliStaticheMostraImmagini instance = null;

    private VariabiliStaticheMostraImmagini() {
    }

    public static VariabiliStaticheMostraImmagini getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheMostraImmagini();
        }

        return instance;
    }

    private Activity act;
    private Context ctx;
    public static final String UrlWS = "http://looigi.no-ip.biz:1071/";
    public static final String PercorsoImmagineSuURL = "http://www.sfondi.looigi.it";
    private ImmagineZoomabile img;
    private StrutturaImmaginiLibrary ultimaImmagineCaricata;
    private int idCategoria;
    private String Filtro = "";
    private int idImmagine;
    private String Random = "S";
    private List<StrutturaImmaginiCategorie> listaCategorie = new ArrayList<>();
    private Spinner spnCategorie;
    private TextView txtInfo;
    private List<StrutturaImmaginiLibrary> immaginiCaricate = new ArrayList<>();
    private GifImageView imgCaricamento;
    private List<StrutturaImmagine> listaImmagini = new ArrayList<>();
    private boolean slideShowAttivo = false;
    private int secondiAttesa = 5000;

    public int getSecondiAttesa() {
        return secondiAttesa;
    }

    public void setSecondiAttesa(int secondiAttesa) {
        this.secondiAttesa = secondiAttesa;
    }

    public boolean isSlideShowAttivo() {
        return slideShowAttivo;
    }

    public void setSlideShowAttivo(boolean slideShowAttivo) {
        this.slideShowAttivo = slideShowAttivo;
    }

    public List<StrutturaImmagine> getListaImmagini() {
        return listaImmagini;
    }

    public void setListaImmagini(List<StrutturaImmagine> listaImmagini) {
        this.listaImmagini = listaImmagini;
    }

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public void AggiungeCaricata() {
        this.immaginiCaricate.add(ultimaImmagineCaricata);
    }

    public void setImmaginiCaricate(List<StrutturaImmaginiLibrary> immaginiCaricate) {
        this.immaginiCaricate = immaginiCaricate;
    }

    public List<StrutturaImmaginiLibrary> getImmaginiCaricate() {
        return immaginiCaricate;
    }

    public TextView getTxtInfo() {
        return txtInfo;
    }

    public void setTxtInfo(TextView txtInfo) {
        this.txtInfo = txtInfo;
    }

    public Spinner getSpnCategorie() {
        return spnCategorie;
    }

    public void setSpnCategorie(Spinner spnCategorie) {
        this.spnCategorie = spnCategorie;
    }

    public List<StrutturaImmaginiCategorie> getListaCategorie() {
        return listaCategorie;
    }

    public void setListaCategorie(List<StrutturaImmaginiCategorie> listaCategorie) {
        this.listaCategorie = listaCategorie;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getFiltro() {
        return Filtro;
    }

    public void setFiltro(String filtro) {
        Filtro = filtro;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public String getRandom() {
        return Random;
    }

    public void setRandom(String random) {
        Random = random;
    }

    public StrutturaImmaginiLibrary getUltimaImmagineCaricata() {
        return ultimaImmagineCaricata;
    }

    public void setUltimaImmagineCaricata(StrutturaImmaginiLibrary ultimaImmagineCaricata) {
        this.ultimaImmagineCaricata = ultimaImmagineCaricata;
    }

    public Activity getAct() {
        return act;
    }

    public void setAct(Activity act) {
        this.act = act;
    }

    public ImmagineZoomabile getImg() {
        return img;
    }

    public void setImg(ImmagineZoomabile img) {
        this.img = img;
    }

    public void ScriveInfoImmagine(StrutturaImmaginiLibrary si) {
        if (si != null && VariabiliStaticheMostraImmagini.getInstance().getTxtInfo() != null) {
            String testo = si.getNomeFile() + "\nSize: " + si.getDimensioniImmagine();
            testo += " Dim.: B." + si.getDimensioneFile();
            VariabiliStaticheMostraImmagini.getInstance().getTxtInfo().setText(testo);
        }
    }
}
