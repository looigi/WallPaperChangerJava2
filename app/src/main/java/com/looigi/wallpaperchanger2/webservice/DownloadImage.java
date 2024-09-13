package com.looigi.wallpaperchanger2.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.StrutturaImmagine;
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
    private ImageView immagine;
    public DownloadImage(Context context, String NomeImmagine, ImageView immagine) {
        this.NomeImmagine = NomeImmagine;
        this.context = context;
        this.immagine = immagine;

        if (immagine == null) {
            Utility.getInstance().Attesa(true);
        }

        PercorsoDIR = context.getFilesDir() + "/Download";

        Utility.getInstance().CreaCartelle(PercorsoDIR);
    }

    protected Bitmap doInBackground(String... urls) {
        Errore = false;
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);

            if (immagine == null) {
                FileOutputStream outStream;
                try {
                    outStream = new FileOutputStream(PercorsoDIR + "/Appoggio.jpg"); // .getPathImmagine());
                    if (outStream != null & mIcon11 != null) {
                        mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    }

                    outStream.flush();
                    outStream.close();

                    if (immagine == null) {
                        Utility.getInstance().ScriveLog(context, NomeMaschera, "Immagine Scaricata");
                    }
                } catch (FileNotFoundException e) {
                    if (immagine == null) {
                        Utility.getInstance().ApreToast(context, "File non esistente per il download");

                        Utility.getInstance().ScriveLog(context, NomeMaschera, "Errore nel salvataggio su download Immagine: " + e.getMessage());
                    }
                    Errore = true;
                } catch (IOException e) {
                    if (immagine == null) {
                        Utility.getInstance().ApreToast(context, "Errore nel salvataggio su download Immagine");

                        Utility.getInstance().ScriveLog(context, NomeMaschera, "Errore nel salvataggio su download Immagine: " + e.getMessage());
                    }
                    Errore = true;
                }
            } else {
                immagine.setImageBitmap(mIcon11);
            }
        } catch (Exception e) {
            if (immagine == null) {
                Utility.getInstance().ApreToast(context, "Errore sul download Immagine");

                Utility.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine: " + e.getMessage());
            }

            // e.printStackTrace();
            Errore = true;
        }

        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (!Errore) {
            if (immagine == null) {
                String sNomeImmagine = NomeImmagine;
                if (sNomeImmagine.toUpperCase().contains("HTTP:")) {
                    String[] s = sNomeImmagine.split("/");
                    sNomeImmagine = s[s.length - 2] + "/" + s[s.length - 1];
                }

                StrutturaImmagine si = new StrutturaImmagine();
                si.setPathImmagine(PercorsoDIR + "/Appoggio.jpg");
                si.setImmagine(sNomeImmagine);
                if (VariabiliStaticheServizio.getInstance().getUltimaImmagine() != null) {
                    si.setDataImmagine(VariabiliStaticheServizio.getInstance().getUltimaImmagine().getDataImmagine());
                    si.setDimensione(VariabiliStaticheServizio.getInstance().getUltimaImmagine().getDimensione());
                } else {
                    si.setDataImmagine("---");
                    si.setDimensione("---");
                }

                VariabiliStaticheServizio.getInstance().setUltimaImmagine(si);

                ChangeWallpaper c = new ChangeWallpaper(context);
                boolean fatto = c.setWallpaperLocale(context, si);
                Utility.getInstance().ScriveLog(context, NomeMaschera, "---Immagine impostata online: " + fatto + "---");
            }
        } else {
            Utility.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine.");
        }

        if (immagine == null) {
            Utility.getInstance().Attesa(false);
        }
    }
}