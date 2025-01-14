package com.looigi.wallpaperchanger2.classeLazio;

import android.content.Context;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeLazio.webService.ChiamateWSLazio;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.VariabiliStaticheImpostazioniOrari;

public class UtilityLazio {
    private static UtilityLazio instance = null;

    private UtilityLazio() {
    }

    public static UtilityLazio getInstance() {
        if (instance == null) {
            instance = new UtilityLazio();
        }

        return instance;
    }

    public void ImpostaAttesa(boolean Come) {
        if (Come) {
            VariabiliStaticheLazio.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheLazio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        }
    }

    public void VisualizzaMaschera() {
        VariabiliStaticheLazio.getInstance().getLayCalendario().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayClassifica().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLaySquadre().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayMercato().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayFonti().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getLayStati().setVisibility(LinearLayout.GONE);
        VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.GONE);
        switch (VariabiliStaticheLazio.getInstance().getMascheraSelezionata()) {
            case 1:
                VariabiliStaticheLazio.getInstance().getLayClassifica().setVisibility(LinearLayout.VISIBLE);
                break;
            case 2:
                VariabiliStaticheLazio.getInstance().getLayCalendario().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
            case 3:
                VariabiliStaticheLazio.getInstance().getLaySquadre().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
            case 4:
                VariabiliStaticheLazio.getInstance().getLayMercato().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
            case 5:
                VariabiliStaticheLazio.getInstance().getLayFonti().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
            case 6:
                VariabiliStaticheLazio.getInstance().getLayStati().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheLazio.getInstance().getImgNuovo().setVisibility(LinearLayout.VISIBLE);
                break;
        }
    }

    public void LeggeAnno(Context context) {
        ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
        ws1.RitornaSquadre();
    }
}
