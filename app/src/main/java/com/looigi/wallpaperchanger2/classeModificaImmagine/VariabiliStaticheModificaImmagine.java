package com.looigi.wallpaperchanger2.classeModificaImmagine;

import android.app.Activity;

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
