package com.looigi.wallpaperchanger2.classePazzia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmaginePAZZIA_IMM {
    private boolean isCancelled;
    private static final String NomeMaschera = "Download_Immagini_PAZ_IMM";
    private boolean Errore;
    private Context context;
    private ImageView immagine;
    private String Url;
    private InputStream in;

    public void EsegueChiamata(Context context, String NomeImmagine, ImageView immagine, String UrlImmagine) {
        isCancelled = false;

        this.context = context;
        this.immagine = immagine;
        this.Url = UrlImmagine;

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

        handlerThread = new HandlerThread("background-thread_PAZIMM_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > VariabiliStaticheMostraImmaginiPennetta.TimeoutImmagine) {
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
                Bitmap finalMIcon1 = BitmapFactory.decodeStream(in);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        immagine.setImageBitmap(finalMIcon1);
                    }
                }, 100);
            }
            // }
        } catch (Exception e) {
            Errore = true;
        }
    }

    private void TermineEsecuzione() {
        UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                VariabiliStatichePazzia.getInstance().getImgCaricamentoIMM(),
                false
        );
    }

    public void BloccaEsecuzione() {
        isCancelled = true;
    }
}
