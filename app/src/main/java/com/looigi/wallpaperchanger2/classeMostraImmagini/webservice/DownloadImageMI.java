package com.looigi.wallpaperchanger2.classeMostraImmagini.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.classeMostraImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeMostraImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadImageMI extends AsyncTask<String, Void, Bitmap> {
    private static final String NomeMaschera = "DOWNLOADIMAGE";
    private boolean Errore;
    private String NomeImmagine;
    private String PercorsoDIR = "";
    private Context context;
    private ImageView immagine;

    public DownloadImageMI(Context context, String NomeImmagine, ImageView immagine) {
        this.NomeImmagine = NomeImmagine;
        this.context = context;
        this.immagine = immagine;

        UtilityImmagini.getInstance().Attesa(true);

        PercorsoDIR = context.getFilesDir() + "/Immagini";

        UtilityWallpaper.getInstance().CreaCartelle(PercorsoDIR);
    }

    protected Bitmap doInBackground(String... urls) {
        Errore = false;
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);

            // if (immagine == null) {
                FileOutputStream outStream;
                try {
                    outStream = new FileOutputStream(PercorsoDIR + "/AppoggioMI.jpg"); // .getPathImmagine());
                    if (outStream != null & mIcon11 != null) {
                        mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    }

                    outStream.flush();
                    outStream.close();

                    // if (immagine == null) {
                        UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Immagine Scaricata");
                    // }
                } catch (FileNotFoundException e) {
                    // if (immagine == null) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "File non esistente per il download");

                        UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Errore nel salvataggio su download Immagine: " + e.getMessage());
                    // }
                    Errore = true;
                } catch (IOException e) {
                    // if (immagine == null) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "Errore nel salvataggio su download Immagine");

                        UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Errore nel salvataggio su download Immagine: " + e.getMessage());
                    // }
                    Errore = true;
                }
            // } else {
                Bitmap finalMIcon1 = mIcon11;

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        immagine.setImageBitmap(finalMIcon1);

                        if (VariabiliStaticheMostraImmagini.getInstance().isSlideShowAttivo()) {
                            UtilityImmagini.getInstance().RiattivaTimer();
                        }
                    }
                }, 100);

            // }
        } catch (Exception e) {
            if (immagine == null) {
                UtilitiesGlobali.getInstance().ApreToast(context, "Errore sul download Immagine");

                UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine: " + e.getMessage());
            }

            // e.printStackTrace();
            Errore = true;
        }

        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        if (!Errore) {
            /* if (immagine == null) {
                String sNomeImmagine = NomeImmagine;
                if (sNomeImmagine.toUpperCase().contains("HTTP:")) {
                    String[] s = sNomeImmagine.split("/");
                    sNomeImmagine = s[s.length - 2] + "/" + s[s.length - 1];
                }

                StrutturaImmagine si = new StrutturaImmagine();
                si.setPathImmagine(PercorsoDIR + "/Appoggio.jpg");
                si.setImmagine(sNomeImmagine);
                if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
                    si.setDataImmagine(VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getDataImmagine());
                    si.setDimensione(VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getDimensione());
                } else {
                    si.setDataImmagine("---");
                    si.setDimensione("---");
                }

                VariabiliStaticheWallpaper.getInstance().setUltimaImmagine(si);

                ChangeWallpaper c = new ChangeWallpaper(context);
                c.setWallpaperLocale(context, si);
                // UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "---Immagine impostata online: " + fatto + "---");
            } */
        } else {
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine.");
        }

        // if (immagine == null) {
            UtilityImmagini.getInstance().Attesa(false);
        // }
    }
}