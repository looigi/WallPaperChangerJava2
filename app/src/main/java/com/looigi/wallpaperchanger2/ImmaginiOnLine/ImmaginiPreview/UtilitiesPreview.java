package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.flexbox.FlexboxLayout;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.webService.DownloadImmaginePreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.VariabiliStaticheSpostamento;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.strutture.StrutturaCategorieSpostamento;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.webService.ChiamateWSSP;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.ModificheCodice.VariabiliStaticheModificheCodice;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class UtilitiesPreview {
    private static UtilitiesPreview instance = null;

    private UtilitiesPreview() {
    }

    public static UtilitiesPreview getInstance() {
        if (instance == null) {
            instance = new UtilitiesPreview();
        }

        return instance;
    }

    /* public void Attesa(boolean Acceso) {
        if (VariabiliStatichePreview.getInstance().getImgCaricamento() == null) {
            return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Acceso) {
                    VariabiliStatichePreview.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                } else {
                    VariabiliStatichePreview.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
                }
            }
        }, 50);
    } */

    public void RitornoProssimaImmagine(Context context, StrutturaImmaginiLibrary si) {
        VariabiliStatichePreview.getInstance().setStrutturaImmagine(si);

        DownloadImmaginePreview d = new DownloadImmaginePreview();
        d.EsegueChiamata(
                context,
                si.getNomeFile(),
                VariabiliStatichePreview.getInstance().getImgPreview(),
                si.getUrlImmagine()
        );
    }

    public void AggiornaCategorieSpostamento(Context context) {
        List<StrutturaImmaginiCategorie> l1 = new ArrayList<>();

        for (StrutturaImmaginiCategorie s : VariabiliStatichePreview.getInstance().getListaCategorie()) {
            if (s.getCategoria().toUpperCase().trim().contains(
                    VariabiliStatichePreview.getInstance().getFiltroCategoriaSpostamento().toUpperCase().trim())) {
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
                VariabiliStatichePreview.getInstance().getSpnSpostaCategorie(),
                l,
                ""
        );
    }

    public void ProssimaImmagine(Context context) {
        if (VariabiliStatichePreview.getInstance().getQualeImmagine() <
                VariabiliStatichePreview.getInstance().getListaImmaginiVisualizzate().size() - 1) {
            UtilitiesGlobali.getInstance().AttesaGif(
                    context,
                    VariabiliStatichePreview.getInstance().getImgCaricamento(),
                    true
            );

            VariabiliStatichePreview.getInstance().getImgProssima().setVisibility(LinearLayout.GONE);
            VariabiliStatichePreview.getInstance().getImgPrecedente().setVisibility(LinearLayout.GONE);

            int quale = VariabiliStatichePreview.getInstance().getQualeImmagine();
            quale++;
            VariabiliStatichePreview.getInstance().setQualeImmagine(quale);
            StrutturaImmaginiLibrary si = VariabiliStatichePreview.getInstance().getListaImmaginiVisualizzate().get(quale);
            if (si != null) {
                VariabiliStatichePreview.getInstance().setStrutturaImmagine(si);
                if (VariabiliStatichePreview.getInstance().getTxtDescrizione() != null) {
                    String Descrizione = "idImmagine: " + si.getIdImmagine() + " - Immagine: " + si.getNomeFile() + " - Categoria: " + si.getCategoria() +
                            " - Cartella: " + si.getCartella() + " - Dimensioni: " + si.getDimensioniImmagine() +
                            " - Bytes: " + si.getDimensioneFile();
                    VariabiliStatichePreview.getInstance().getTxtDescrizione().setText(Descrizione);
                }

                UtilitiesPreview.getInstance().RitornoProssimaImmagine(context, si);
            }

            UtilitiesGlobali.getInstance().AttesaGif(
                    context,
                    VariabiliStatichePreview.getInstance().getImgCaricamento(),
                    false
            );
            VariabiliStatichePreview.getInstance().getImgProssima().setVisibility(LinearLayout.VISIBLE);
            VariabiliStatichePreview.getInstance().getImgPrecedente().setVisibility(LinearLayout.VISIBLE);
        } else {
            ChiamateWSUI ws = new ChiamateWSUI(context);
            ws.RitornaProssimaImmagine(
                    VariabiliStatichePreview.getInstance().getIdCategoria(),
                    "PREVIEW"
            );
        }
    }

    public void DisegnaUltimiSpostamenti(Context context) {
        if (VariabiliStatichePreview.getInstance().getLayTastiDestra() != null) {
            VariabiliStatichePreview.getInstance().getLayTastiDestra().setVisibility(LinearLayout.GONE);
            VariabiliStatichePreview.getInstance().getLayTastiDestra().removeAllViews();
            int q = 0;
            String Messe = "";
            for (String c : VariabiliStaticheSpostamento.getInstance().RitornaCategorieSpostate()) {
                if (!c.isEmpty()) {
                    if (!Messe.contains(c + ";")) {
                        addDynamicButton(context, c);
                        Messe += c + ";";
                        q++;

                        if (q > 4) {
                            break;
                        }
                    }
                }
            }
            if (q > 0) {
                if (VariabiliStatichePreview.getInstance().getModalita().equals("PREVIEW") ||
                        VariabiliStatichePreview.getInstance().getModalita().equals("Utility")) {
                    VariabiliStatichePreview.getInstance().getLayTastiDestra().setVisibility(LinearLayout.VISIBLE);
                }
            }
        }
    }

    private void addDynamicButton(Context context, String text) {
        Button button = new Button(context);
        button.setText(text);
        button.setIncludeFontPadding(false);
        button.setTextSize(11);
        button.setTextColor(Color.BLACK);
        button.setMinHeight(0);
        button.setMinimumHeight(0);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(1, 1, 1, 1);
        button.setLayoutParams(params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImpostaIdCategoria(context, text);

                String idImmagine = String.valueOf(VariabiliStatichePreview.getInstance().getStrutturaImmagine().getIdImmagine());

                VariabiliStaticheSpostamento.getInstance().AggiungeSpostata(
                        context,
                        text
                );

                StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary(); // CREARE LA STRUTTURA PER LO SPOSTAMENTO
                s.setIdImmagine(Integer.parseInt(idImmagine));

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s, "SPOSTAMENTO");
            }
        });

        // Aggiungi al layout
        VariabiliStatichePreview.getInstance().getLayTastiDestra().addView(button);
    }

    public void ImpostaIdCategoria(Context context, String Categoria) {
        if (VariabiliStaticheSpostamento.getInstance().getListaCategorie().isEmpty()) {
            ChiamateWSSP c = new ChiamateWSSP(context);
            c.TornaCategoriePerImmaginiContenute(false);
        }
        VariabiliStaticheSpostamento.getInstance().setIdCategoriaSpostamento(-1);

        for (StrutturaCategorieSpostamento c: VariabiliStaticheSpostamento.getInstance().getListaCategorie()) {
            if (c.getCategoria().equals(Categoria)) {
                VariabiliStaticheSpostamento.getInstance().setIdCategoriaSpostamento(c.getIdCategoria());
                break;
            }
        }
    }
}
