package com.looigi.wallpaperchanger2.Lazio.DettaglioPartita;

import android.widget.LinearLayout;

public class UtilityLazioDettaglio {
    private static UtilityLazioDettaglio instance = null;

    private UtilityLazioDettaglio() {
    }

    public static UtilityLazioDettaglio getInstance() {
        if (instance == null) {
            instance = new UtilityLazioDettaglio();
        }

        return instance;
    }

    /* public void ImpostaAttesa(boolean Come) {
        if (Come) {
            VariabiliStaticheLazioDettaglio.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheLazioDettaglio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        }
    } */
}
