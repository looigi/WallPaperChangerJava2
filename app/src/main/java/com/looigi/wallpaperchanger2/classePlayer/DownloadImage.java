package com.looigi.wallpaperchanger2.classePlayer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    private static final String NomeMaschera = "Download_Immagine_Player";
    private ImageView bmImage;
    private String Immagine;
    private String PathImmagine;
    private String CartellaImmagine;
    private boolean Errore;
    private Context context;

    public DownloadImage(Context context, ImageView bmImage, String Immagine) {
        UtilityPlayer.getInstance().Attesa(true);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("Download immagine");
        this.context = context;
        this.bmImage = bmImage;
        this.Immagine = Immagine;
        PathImmagine = Immagine;
        PathImmagine = PathImmagine.replace(VariabiliStatichePlayer.PercorsoBranoMP3SuURL + "/", "");
        PathImmagine = context.getFilesDir() + "/Player/" + PathImmagine;
        PathImmagine = PathImmagine.replace("\\", "/");
        CartellaImmagine = "";
        String[] c = PathImmagine.split("/");
        for (int i = 0; i < c.length - 1; i++) {
            CartellaImmagine += c[i] + "/";
        }
    }

    protected Bitmap doInBackground(String... urls) {
        Errore = false;
        String urldisplay = urls[0];
        urldisplay = urldisplay.replace("\\", "/");
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);

            // e.printStackTrace();
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine: " + e.getMessage());
            // Utility.getInstance().ImpostaSfondoLogo();
            Errore = true;
        }
        return mIcon11;
    }

    @SuppressLint("WrongThread")
    protected void onPostExecute(Bitmap result) {
        if (!Errore) {
            bmImage.setImageBitmap(result);

            if (result.getHeight() > 100 && result.getWidth() > 100) {
                BitmapDrawable drawable = (BitmapDrawable) bmImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();

                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "URL per salvataggio immagine: " + this.Immagine);
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Creo cartelle per salvataggio immagine: " + this.CartellaImmagine);
                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Path salvataggio immagine: " + this.PathImmagine);
                Files.getInstance().CreaCartelle(this.CartellaImmagine); // .getCartellaImmagine());
                this.PathImmagine = this.PathImmagine.replace("\\", "/");
                VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(PathImmagine);

                FileOutputStream outStream;
                try {
                    outStream = new FileOutputStream(this.PathImmagine); // .getPathImmagine());
                    if (outStream != null & bitmap != null) {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    }
                    /* 100 to keep full quality of the image */

                    outStream.flush();
                    outStream.close();

                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                            "Immagine Scaricata: " + this.PathImmagine);
                } catch (FileNotFoundException e) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                            "Errore nel salvataggio su download Immagine: " + e.getMessage());

                    VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                    UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                } catch (IOException e) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                            "Errore nel salvataggio su download Immagine: " + e.getMessage());

                    VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                    UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                }
            } else {
                UtilityPlayer.getInstance().ImpostaImmagine(context);
            }
        } else {
            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                    "Errore sul download immagine. Imposto logo");

            VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");

            UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
        }

        UtilityPlayer.getInstance().AggiornaInformazioni(false);
        UtilityPlayer.getInstance().Attesa(false);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");
    }
}