package com.looigi.wallpaperchanger2.classeLog;

import android.app.Activity;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VariabiliStaticheLog {
    private static VariabiliStaticheLog instance = null;

    private VariabiliStaticheLog() {
    }

    public static VariabiliStaticheLog getInstance() {
        if (instance == null) {
            instance = new VariabiliStaticheLog();
        }

        return instance;
    }

    private Context context;
    private Activity mainActivity;
    private List<File> listaFiles = new ArrayList<>();

    public List<File> getListaFiles() {
        return listaFiles;
    }

    public void setListaFiles(List<File> listaFiles) {
        this.listaFiles = listaFiles;
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