package com.looigi.wallpaperchanger2.classeWallpaper;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.LinearLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ScannaDiscoPerImmaginiLocali extends AsyncTask<String, Integer, String> {
    private static final String NomeMaschera = "ScanDisk_IL";
    private List<StrutturaImmagine> imms = new ArrayList<>();
    private db_dati_wallpaper db;
    private Context context;
    private GifImageView imgAttesa;

    public ScannaDiscoPerImmaginiLocali(Context context, GifImageView imgAttesa) {
        this.context = context;
        this.imgAttesa = imgAttesa;

        imms = new ArrayList<>();
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
        db = new db_dati_wallpaper(context);
        db.EliminaImmaginiInLocale();

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Lettura immagini presenti su disco su path: " +
                VariabiliStaticheWallpaper.getInstance().getPercorsoIMMAGINI());
    }

    @Override
    protected void onPostExecute(String p) {
        super.onPostExecute(p);

        db.ChiudeDB();

        VariabiliStaticheWallpaper.getInstance().setListaImmagini(imms);
        switch (VariabiliStaticheWallpaper.getInstance().getModoRicercaImmagine()) {
            case 0:
                VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Immagini online");
                break;
            case 1:
                VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate su disco: " + imms.size());
                break;
            case 2:
                VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Immagini da immagini");
                break;
        }
        if (imgAttesa != null) {
            imgAttesa.setVisibility(LinearLayout.GONE);
        }

        // VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Lettura immagini effettuata. Immagini rilevate su disco: " +
            imms.size());
    }

    @Override
    protected String doInBackground(String... strings) {
        File rootPrincipale = new File(VariabiliStaticheWallpaper.getInstance().getPercorsoIMMAGINI());
        if (!rootPrincipale.exists()) {
            rootPrincipale.mkdir();
        }

        walk(rootPrincipale);

        return null;
    }

    private void walk(File root) {
            File[] list = root.listFiles();

            if (list != null) {
                for (File f : list) {
                    if (f.isDirectory()) {
                        walk(f);
                    } else {
                        StrutturaImmagine si = new StrutturaImmagine();

                        String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                        String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file
                        Date lastModDate = new Date(f.lastModified());
                        String Datella = lastModDate.toString();
                        String Dimensione = Long.toString(f.length());
                        String Cartella = f.getAbsoluteFile().getPath();

                        si.setImmagine(Nome);
                        si.setPathImmagine(Filetto);
                        si.setDataImmagine(Datella);
                        si.setDimensione(Dimensione);

                        imms.add(si);

                        db.ScriveImmagineInLocale(Nome, Filetto, Datella, Dimensione, Cartella);
                    }
                }
            }
    }
}
