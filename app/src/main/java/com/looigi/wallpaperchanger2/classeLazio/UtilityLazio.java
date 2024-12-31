package com.looigi.wallpaperchanger2.classeLazio;

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
}
