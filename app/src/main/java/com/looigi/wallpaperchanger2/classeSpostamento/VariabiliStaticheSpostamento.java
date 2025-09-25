package com.looigi.wallpaperchanger2.classeSpostamento;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.flexbox.FlexboxLayout;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classePreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.classePreview.strutture.StrutturaVoltiRilevati;
import com.looigi.wallpaperchanger2.classePreview.webService.DownloadImmaginePreview;
import com.looigi.wallpaperchanger2.classeSpostamento.strutture.StrutturaCategorieSpostamento;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheSpostamento {
    private static VariabiliStaticheSpostamento instance = null;

    private VariabiliStaticheSpostamento() {
    }

    public static VariabiliStaticheSpostamento getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheSpostamento();
        }

        return instance;
    }

    private GifImageView imgCaricamento;
    private Spinner spnCategorie;
    private String filtroCategoriaSpostamento = "";
    private List<StrutturaCategorieSpostamento> listaCategorie = new ArrayList<>();
    private int idCategoriaSpostamento = -1;
    private String CategoriaSpostamento = "";
    private String Modalita;
    private String idImmagine;
    private ImageView imgSpostamento;
    private TextView txtSpostamento;
    private Activity act;
    private FlexboxLayout layPreferiti;
    private List<String> Preferiti = new ArrayList<>();
    private List<String> CategoriaSpostata;

    public void setCategoriaSpostata(List<String> categoriaSpostata) {
        CategoriaSpostata = categoriaSpostata;
    }

    public void AggiungeSpostata(Context context, String Categoria) {
        if (CategoriaSpostata == null) {
            CategoriaSpostata = new ArrayList<>();
        }
        List<String> imposta = new ArrayList<>();
        if (!CategoriaSpostata.contains(Categoria)) {
            imposta.add(Categoria);
            int q = 0;
            for (String c : CategoriaSpostata) {
                imposta.add(c);
                q++;
                if (q > 4) {
                    break;
                }
            }
        } else {
            imposta = CategoriaSpostata;
        }
        String Cate = "";
        for (String s: imposta) {
            if (!(Cate).contains(s + "\n")) {
                Cate += s + "\n";
            }
        }
        String Path = context.getFilesDir() + "/Immagini";
        if (Files.getInstance().EsisteFile(Path + "/CategoriePiuUsate.txt")) {
            Files.getInstance().EliminaFile(Path, "CategoriePiuUsate.txt");
        }
        Files.getInstance().ScriveFile(Path, "CategoriePiuUsate.txt", Cate);
        CategoriaSpostata = imposta;
    }

    public List<String> RitornaCategorieSpostate() {
        if (CategoriaSpostata == null) {
            CategoriaSpostata = new ArrayList<>();
        }

        return CategoriaSpostata;
    }

    public String getCategoriaSpostamento() {
        return CategoriaSpostamento;
    }

    public void setCategoriaSpostamento(String categoriaSpostamento) {
        CategoriaSpostamento = categoriaSpostamento;
    }

    public List<String> getPreferiti() {
        return Preferiti;
    }

    public void setPreferiti(List<String> preferiti) {
        Preferiti = preferiti;
    }

    public FlexboxLayout getLayPreferiti() {
        return layPreferiti;
    }

    public void setLayPreferiti(FlexboxLayout layPreferiti) {
        this.layPreferiti = layPreferiti;
    }

    public Activity getAct() {
        return act;
    }

    public void setAct(Activity act) {
        this.act = act;
    }

    public TextView getTxtSpostamento() {
        return txtSpostamento;
    }

    public void setTxtSpostamento(TextView txtSpostamento) {
        this.txtSpostamento = txtSpostamento;
    }

    public ImageView getImgSpostamento() {
        return imgSpostamento;
    }

    public void setImgSpostamento(ImageView imgSpostamento) {
        this.imgSpostamento = imgSpostamento;
    }

    public String getModalita() {
        return Modalita;
    }

    public void setModalita(String modalita) {
        Modalita = modalita;
    }

    public String getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(String idImmagine) {
        this.idImmagine = idImmagine;
    }

    public int getIdCategoriaSpostamento() {
        return idCategoriaSpostamento;
    }

    public void setIdCategoriaSpostamento(int idCategoriaSpostamento) {
        this.idCategoriaSpostamento = idCategoriaSpostamento;
    }

    public List<StrutturaCategorieSpostamento> getListaCategorie() {
        return listaCategorie;
    }

    public void setListaCategorie(List<StrutturaCategorieSpostamento> listaCategorie) {
        this.listaCategorie = listaCategorie;
    }

    public String getFiltroCategoriaSpostamento() {
        return filtroCategoriaSpostamento;
    }

    public void setFiltroCategoriaSpostamento(String filtroCategoriaSpostamento) {
        this.filtroCategoriaSpostamento = filtroCategoriaSpostamento;
    }

    public Spinner getSpnCategorie() {
        return spnCategorie;
    }

    public void setSpnCategorie(Spinner spnCategorie) {
        this.spnCategorie = spnCategorie;
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

    public void AggiornaCategorieSpostamento(Context context) {
        List<StrutturaCategorieSpostamento> l1 = new ArrayList<>();

        for (StrutturaCategorieSpostamento s : listaCategorie) {
            if (s.getCategoria().toUpperCase().trim().contains(
                    filtroCategoriaSpostamento.toUpperCase().trim())) {
                l1.add(s);
            }
        }

        String[] l = new String[l1.size()];
        int c = 0;
        for (StrutturaCategorieSpostamento s : l1) {
            l[c] = s.getCategoria();
            c++;
        }

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spnCategorie,
                l,
                ""
        );
    }

    public void ImpostaIdCategoria(String Categoria) {
        VariabiliStaticheSpostamento.getInstance().setIdCategoriaSpostamento(-1);

        for (StrutturaCategorieSpostamento c: VariabiliStaticheSpostamento.getInstance().getListaCategorie()) {
            if (c.getCategoria().equals(Categoria)) {
                VariabiliStaticheSpostamento.getInstance().setIdCategoriaSpostamento(c.getIdCategoria());
                break;
            }
        }
    }
}
