package com.looigi.wallpaperchanger2.modificaImmagine;

import android.app.Activity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;

public class VariabiliStaticheModificaImmagine {
    private static VariabiliStaticheModificaImmagine instance = null;

    private VariabiliStaticheModificaImmagine() {
    }

    public static VariabiliStaticheModificaImmagine getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheModificaImmagine();
        }

        return instance;
    }

    private Activity mainActivity;

    public void ChiudeActivity(boolean Finish) {
        if (mainActivity != null) {
            // mainActivity.moveTaskToBack(true);
            // if (Finish) {
            mainActivity.finish();
            // }
        }
    }

    public Activity getMainActivity() {
        return mainActivity;
    }
}
