package com.looigi.wallpaperchanger2.classeLazio.webService;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classePennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmagineLazio {
    private boolean isCancelled;
    private static final String NomeMaschera = "Download_Immagini_LAZIO";
    private boolean Errore;
    private Context context;
    private ImageView immagine;
    private String Url;
    private InputStream in;
    private String NomeImmagine;

    public void EsegueChiamata(Context context, ImageView immagine, String UrlImmagine, String NomeImmagine) {
        isCancelled = false;
        this.context = context;
        this.immagine = immagine;
        this.Url = UrlImmagine;
        this.NomeImmagine = NomeImmagine;

        AttivaTimer();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Esecuzione();
                BloccaTimer();
                TermineEsecuzione();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //UI Thread work here
                    }
                });
            }
        });
    }

    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;
    private int secondiPassati = 0;

    private void AttivaTimer() {
        secondiPassati = 0;

        handlerThread = new HandlerThread("background-thread_LAZ_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > 5) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {

                        }
                        in = null;
                    }
                    BloccaTimer();
                    BloccaEsecuzione();
                } else {
                    if (handler != null) {
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        };
        handler.postDelayed(r, 1000);
    }

    public void BloccaTimer() {
        if (handler != null && r != null && handlerThread != null) {
            handlerThread.quit();

            handler.removeCallbacksAndMessages(null);

            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    }

    private void Esecuzione() {
        Errore = false;
        String urldisplay = Url;
        Bitmap mIcon11 = null;
        try {
            in = new java.net.URL(urldisplay).openStream();
            if (in != null && !isCancelled) {
                mIcon11 = BitmapFactory.decodeStream(in);

                FileOutputStream outStream;
                String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Stemmi/" + NomeImmagine;
                try {
                    outStream = new FileOutputStream(PathImmagini); // .getPathImmagine());
                    if (outStream != null & mIcon11 != null) {
                        mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    }

                    outStream.flush();
                    outStream.close();
                } catch (FileNotFoundException e) {
                    Errore = true;
                } catch (IOException e) {
                    Errore = true;
                }

                Bitmap finalMIcon1 = mIcon11;
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        immagine.setImageBitmap(finalMIcon1);
                    }
                }, 10);
            }
            // }
        } catch (Exception e) {
            if (immagine == null) {
                // UtilitiesGlobali.getInstance().ApreToast(context, "Errore sul download Immagine");
            }

            // e.printStackTrace();
            Errore = true;
        }
    }

    private void TermineEsecuzione() {

    }

    public void BloccaEsecuzione() {
        isCancelled = true;
    }
}
