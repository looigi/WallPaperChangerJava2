package com.looigi.wallpaperchanger2.classeOrari;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.utilities.ImmagineZoomabile;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class VariabiliStaticheOrari {
    private static VariabiliStaticheOrari instance = null;

    private VariabiliStaticheOrari() {
    }

    public static VariabiliStaticheOrari getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheOrari();
        }

        return instance;
    }

    public static final String UrlWS = "http://www.wsorari.looigi.it/"; // "http://looigi.no-ip.biz:1071/";
}
