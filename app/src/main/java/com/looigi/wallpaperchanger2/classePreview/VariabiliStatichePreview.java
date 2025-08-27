package com.looigi.wallpaperchanger2.classePreview;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classePreview.strutture.StrutturaVoltiRilevati;
import com.looigi.wallpaperchanger2.classePreview.webService.DownloadImmaginePreview;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.adapters.AdapterListenerUI;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

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
    private GifImageView imgCaricamento;
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

    public GifImageView getImgCaricamento() {
        return imgCaricamento;
    }

    public void setImgCaricamento(GifImageView imgCaricamento) {
        this.imgCaricamento = imgCaricamento;
    }

    public void Attesa(boolean Acceso) {
        if (imgCaricamento == null) {
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Acceso) {
                    imgCaricamento.setVisibility(LinearLayout.VISIBLE);
                } else {
                    imgCaricamento.setVisibility(LinearLayout.GONE);
                }
            }
        }, 50);
    }

    public void RitornoProssimaImmagine(Context context, StrutturaImmaginiLibrary si) {
        strutturaImmagine = si;

        DownloadImmaginePreview d = new DownloadImmaginePreview();
        d.EsegueChiamata(
                context,
                si.getNomeFile(),
                imgPreview,
                si.getUrlImmagine()
        );
    }

    public void AggiornaCategorieSpostamento(Context context) {
        List<StrutturaImmaginiCategorie> l1 = new ArrayList<>();

        for (StrutturaImmaginiCategorie s : listaCategorie) {
            if (s.getCategoria().toUpperCase().trim().contains(
                    filtroCategoriaSpostamento.toUpperCase().trim())) {
                l1.add(s);
            }
        }

        String[] l = new String[l1.size()];
        int c = 0;
        for (StrutturaImmaginiCategorie s : l1) {
            l[c] = s.getCategoria();
            c++;
        }

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spnSpostaCategorie,
                l,
                ""
        );
    }
}
