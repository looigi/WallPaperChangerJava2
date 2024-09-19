package com.looigi.wallpaperchanger2.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.AdapterListenerImmagini;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classiStandard.LogInterno;
import com.looigi.wallpaperchanger2.gps.StrutturaGps;

import java.util.ArrayList;
import java.util.List;

public class VariabiliStaticheStart {
    private static VariabiliStaticheStart instance = null;

    private VariabiliStaticheStart() {
    }

    public static VariabiliStaticheStart getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheStart();
        }

        return instance;
    }

    private Context context;
    private Activity mainActivity;
    private boolean GiaPartito = false;
    public void ChiudeActivity(boolean Finish) {
        if (mainActivity != null) {
            // mainActivity.moveTaskToBack(true);
            // if (Finish) {
                mainActivity.finish();
            // }
        }
    }

    public boolean isGiaPartito() {
        return GiaPartito;
    }

    public void setGiaPartito(boolean giaPartito) {
        GiaPartito = giaPartito;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}