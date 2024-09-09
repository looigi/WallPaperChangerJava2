package com.looigi.wallpaperchanger2.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;

import com.looigi.wallpaperchanger2.classiAttivita.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classiAttivita.StrutturaImmagine;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    private static final String NomeMaschera = "DOWNLOADIMAGE";
    private boolean Errore;
    private String NomeImmagine;
    private String PercorsoDIR = "";
    private Context context;

    public DownloadImage(Context context, String NomeImmagine) {
        this.NomeImmagine = NomeImmagine;
        this.context = context;

        PercorsoDIR = Environment.getExternalStorageDirectory() + "/" +
                Environment.DIRECTORY_DOWNLOADS + "/LooigiSoft/" + VariabiliStaticheServizio.channelName;

        Utility.getInstance().CreaCartelle(PercorsoDIR + "/Download");
    }

    protected Bitmap doInBackground(String... urls) {
        Errore = false;
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);

            FileOutputStream outStream;
            try {
                outStream = new FileOutputStream(PercorsoDIR + "/Download/Appoggio.jpg"); // .getPathImmagine());
                if (outStream != null & mIcon11 != null) {
                    mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                }

                outStream.flush();
                outStream.close();

                Utility.getInstance().ScriveLog(context, NomeMaschera,"Immagine Scaricata");
            } catch (FileNotFoundException e) {
                Utility.getInstance().ScriveLog(context, NomeMaschera,"Errore nel salvataggio su download Immagine: " + e.getMessage());
                Errore = true;
            } catch (IOException e) {
                Utility.getInstance().ScriveLog(context, NomeMaschera,"Errore nel salvataggio su download Immagine: " + e.getMessage());
                Errore = true;
            }
        } catch (Exception e) {
            // e.printStackTrace();
            Utility.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine: " + e.getMessage());
            Errore = true;
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (!Errore) {
            StrutturaImmagine si = new StrutturaImmagine();
            si.setPathImmagine(PercorsoDIR + "/Download/Appoggio.jpg");
            si.setImmagine(NomeImmagine);
            si.setDataImmagine(VariabiliStaticheServizio.getInstance().getDataAppoggio());
            si.setDimensione(VariabiliStaticheServizio.getInstance().getDimeAppoggio());

            ChangeWallpaper c = new ChangeWallpaper(context);
            boolean fatto = c.setWallpaperLocale(si);
            Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine impostata online: " + fatto + "---");
        } else {
            Utility.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine.");
        }
    }
}