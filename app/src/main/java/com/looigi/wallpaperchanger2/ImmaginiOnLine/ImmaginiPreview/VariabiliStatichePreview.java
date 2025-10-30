package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.strutture.StrutturaVoltiRilevati;

import java.util.ArrayList;
import java.util.List;

public class VariabiliStatichePreview {
    private static VariabiliStatichePreview instance = null;

    private VariabiliStatichePreview() {
    }

    public static VariabiliStatichePreview getInstance() {
        if (instance == null) {
            instance = new VariabiliStatichePreview();
        }

        return instance;
    }

    private StrutturaImmaginiLibrary strutturaImmagine;
    private ImageView imgCaricamento;
    private String Modalita;
    private int idCategoria = -1;
    private ImageView imgPreview;
    private Spinner spnSpostaCategorie;
    private List<StrutturaImmaginiCategorie> listaCategorie = new ArrayList<>();
    private String idCategoriaDiSpostamento;
    private String filtroCategoriaSpostamento = "";
    private List<StrutturaVoltiRilevati> listaVoltiRilevati = new ArrayList<>();
    private LinearLayout layVolti;
    private ListView lstVolti;
    private TextView txtDescrizione;
    private List<StrutturaImmaginiLibrary> listaImmaginiVisualizzate = new ArrayList<>();
    private int qualeImmagine = -1;
    private int ultimaImmagineVisualizzata = 0;
    private ImageView imgPrecedente;
    private ImageView imgProssima;
    private int idCategoriaSpostamento;
    private FlexboxLayout layCategorieRilevate;
    private FlexboxLayout layScritteRilevate;
    private LinearLayout layTasti;
    private LinearLayout layTastiDestra;
    private TextView txtElaborate;
    private String Categoria;

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public TextView getTxtElaborate() {
        return txtElaborate;
    }

    public void setTxtElaborate(TextView txtElaborate) {
        this.txtElaborate = txtElaborate;
    }

    public LinearLayout getLayTastiDestra() {
        return layTastiDestra;
    }

    public void setLayTastiDestra(LinearLayout layTastiDestra) {
        this.layTastiDestra = layTastiDestra;
    }

    public LinearLayout getLayTasti() {
        return layTasti;
    }

    public void setLayTasti(LinearLayout layTasti) {
        this.layTasti = layTasti;
    }

    public FlexboxLayout getLayScritteRilevate() {
        return layScritteRilevate;
    }

    public void setLayScritteRilevate(FlexboxLayout layScritteRilevate) {
        this.layScritteRilevate = layScritteRilevate;
    }

    public int getIdCategoriaSpostamento() {
        return idCategoriaSpostamento;
    }

    public void setIdCategoriaSpostamento(int idCategoriaSpostamento) {
        this.idCategoriaSpostamento = idCategoriaSpostamento;
    }

    public FlexboxLayout getLayCategorieRilevate() {
        return layCategorieRilevate;
    }

    public void setLayCategorieRilevate(FlexboxLayout layCategorieRilevate) {
        this.layCategorieRilevate = layCategorieRilevate;
    }

    public void setQualeImmagine(int qualeImmagine) {
        this.qualeImmagine = qualeImmagine;
    }

    public List<StrutturaImmaginiLibrary> getListaImmaginiVisualizzate() {
        return listaImmaginiVisualizzate;
    }

    public ImageView getImgPrecedente() {
        return imgPrecedente;
    }

    public void setImgPrecedente(ImageView imgPrecedente) {
        this.imgPrecedente = imgPrecedente;
    }

    public ImageView getImgProssima() {
        return imgProssima;
    }

    public void setImgProssima(ImageView imgProssima) {
        this.imgProssima = imgProssima;
    }

    public int getUltimaImmagineVisualizzata() {
        return ultimaImmagineVisualizzata;
    }

    public void setUltimaImmagineVisualizzata(int ultimaImmagineVisualizzata) {
        this.ultimaImmagineVisualizzata = ultimaImmagineVisualizzata;
    }

    public int getQualeImmagine() {
        return qualeImmagine;
    }

    public void AggiornaImmagineVisualizzata(StrutturaImmaginiLibrary s) {
        if (qualeImmagine > -1 && listaImmaginiVisualizzate != null && listaImmaginiVisualizzate.size() > qualeImmagine) {
            listaImmaginiVisualizzate.set(qualeImmagine, s);
        }
    }

    public void AggiungeImmagineAVisualizzate(StrutturaImmaginiLibrary s) {
        if (qualeImmagine < listaImmaginiVisualizzate.size() - 1) {
            listaImmaginiVisualizzate.set(qualeImmagine, s);
        } else {
            listaImmaginiVisualizzate.add(s);
        }

        qualeImmagine++;
    }

    public StrutturaImmaginiLibrary RitornaImmaginePrecedente() {
        qualeImmagine--;

        return listaImmaginiVisualizzate.get(qualeImmagine);
    }

    public TextView getTxtDescrizione() {
        return txtDescrizione;
    }

    public void setTxtDescrizione(TextView txtDescrizione) {
        this.txtDescrizione = txtDescrizione;
    }

    public ListView getLstVolti() {
        return lstVolti;
    }

    public void setLstVolti(ListView lstVolti) {
        this.lstVolti = lstVolti;
    }

    public LinearLayout getLayVolti() {
        return layVolti;
    }

    public void setLayVolti(LinearLayout layVolti) {
        this.layVolti = layVolti;
    }

    public List<StrutturaVoltiRilevati> getListaVoltiRilevati() {
        return listaVoltiRilevati;
    }

    public void setListaVoltiRilevati(List<StrutturaVoltiRilevati> listaVoltiRilevati) {
        this.listaVoltiRilevati = listaVoltiRilevati;
    }

    public String getFiltroCategoriaSpostamento() {
        return filtroCategoriaSpostamento;
    }

    public void setFiltroCategoriaSpostamento(String filtroCategoriaSpostamento) {
        this.filtroCategoriaSpostamento = filtroCategoriaSpostamento;
    }

    public String getIdCategoriaDiSpostamento() {
        return idCategoriaDiSpostamento;
    }

    public void setIdCategoriaDiSpostamento(String IdCategoriaDiSpostamento) {
        idCategoriaDiSpostamento = IdCategoriaDiSpostamento;
    }

    public List<StrutturaImmaginiCategorie> getListaCategorie() {
        return listaCategorie;
    }

    public void setListaCategorie(List<StrutturaImmaginiCategorie> listaCategorie) {
        this.listaCategorie = listaCategorie;
    }

    public Spinner getSpnSpostaCategorie() {
        return spnSpostaCategorie;
    }

    public void setSpnSpostaCategorie(Spinner spnSpostaCategorie) {
        this.spnSpostaCategorie = spnSpostaCategorie;
    }

    public ImageView getImgPreview() {
        return imgPreview;
    }

    public void setImgPreview(ImageView imgPreview) {
        this.imgPreview = imgPreview;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getModalita() {
        return Modalita;
    }

    public void setModalita(String modalita) {
        Modalita = modalita;
    }

    public StrutturaImmaginiLibrary getStrutturaImmagine() {
        return strutturaImmagine;
    }

    public void setStrutturaImmagine(StrutturaImmaginiLibrary strutturaImmagine) {
        this.strutturaImmagine = strutturaImmagine;
    }

    public ImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(ImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }
}
