package com.looigi.wallpaperchanger2.classiAttivitaWallpaper;

import android.content.Context;
import android.os.AsyncTask;

import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScannaDiscoPerImmaginiLocali extends AsyncTask<String, Integer, String> {
    private static final String NomeMaschera = "SCANDISK";
    private List<StrutturaImmagine> imms = new ArrayList<>();
    private db_dati db;
    private Context context;

    public ScannaDiscoPerImmaginiLocali(Context context) {
        this.context = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
        db = new db_dati(context);
        db.EliminaImmaginiInLocale();

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Lettura immagini presenti su disco su path: " +
                VariabiliStaticheServizio.getInstance().getPercorsoIMMAGINI());
    }

    @Override
    protected void onPostExecute(String p) {
        super.onPostExecute(p);

        VariabiliStaticheServizio.getInstance().setListaImmagini(imms);
        if(VariabiliStaticheServizio.getInstance().isOffline()) {
            VariabiliStaticheServizio.getInstance().getTxtQuanteImmagini().setText("Immagini rilevate su disco: " + imms.size());
        }

        // VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        Utility.getInstance().ScriveLog(context, NomeMaschera, "Lettura immagini effettuata. Immagini rilevate su disco: " +
            imms.size());
    }

    @Override
    protected String doInBackground(String... strings) {
        File rootPrincipale = new File(VariabiliStaticheServizio.getInstance().getPercorsoIMMAGINI());
        if (!rootPrincipale.exists()) {
            rootPrincipale.mkdir();
        }
        walk(rootPrincipale);

        return null;
    }

    private void walk(File root) {
        File[] list = root.listFiles();
        
        if(list != null) {
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

                    si.setImmagine(Nome);
                    si.setPathImmagine(Filetto);
                    si.setDataImmagine(Datella);
                    si.setDimensione(Dimensione);

                    imms.add(si);

                    db.ScriveImmagineInLocale(Nome, Filetto, Datella, Dimensione);
                }
            }
        }
    }
}
