package com.looigi.wallpaperchanger2.classeOrari.impostazioni;

import android.content.Context;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.adapters.AdapterListenerMezzi;
import com.looigi.wallpaperchanger2.classeOrari.adapters.AdapterListenerPortate;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDatiGiornata;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezziStandard;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UtilityImpostazioniOrari {
    private static UtilityImpostazioniOrari instance = null;

    private UtilityImpostazioniOrari() {
    }

    public static UtilityImpostazioniOrari getInstance() {
        if (instance == null) {
            instance = new UtilityImpostazioniOrari();
        }

        return instance;
    }

    public void ImpostaAttesa(boolean Come) {
        if (Come) {
            VariabiliStaticheImpostazioniOrari.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheImpostazioniOrari.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        }
    }
}
