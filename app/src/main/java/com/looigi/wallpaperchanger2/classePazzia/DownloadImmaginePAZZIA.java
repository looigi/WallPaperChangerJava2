package com.looigi.wallpaperchanger2.classePazzia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmaginePAZZIA {
    private boolean isCancelled;
    private static final String NomeMaschera = "Download_Image_PAZ_PEN";
    private boolean Errore;
    private Context context;
    private ImageView immagine;
    private String Url;
    private InputStream in;
    private String Modalita;

    public void EsegueChiamata(Context context, ImageView immagine,
                               String UrlImmagine, String Modalita) {
        isCancelled = false;

        this.context = context;
        this.immagine = immagine;
        this.Url = UrlImmagine;
        this.Modalita = Modalita;

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

        handlerThread = new HandlerThread("background-thread_PAZ" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > VariabiliStaticheMostraImmagini.TimeoutImmagine) {
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

    Bitmap mIcon1 = null;

    private void Esecuzione() {
        Errore = false;
        String urldisplay = Url;
        try {
            in = new java.net.URL(urldisplay).openStream();
            if (in != null && !isCancelled) {
                mIcon1 = BitmapFactory.decodeStream(in);
                // immagine.setImageBitmap(mIcon1);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        immagine.setImageBitmap(mIcon1);
                    }
                }, 100);
            }

            // } else {
            if (!isCancelled) {
            }
            // }
        } catch (Exception e) {
            Errore = true;
        }
    }

    private void TermineEsecuzione() {
        if (Modalita.equals("PENNETTA")) {
            if (VariabiliStatichePazzia.getInstance().isSlideShowAttivoPEN()) {
                UtilityPazzia.getInstance().AttivaTimerPEN(context);
            }

            UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                    VariabiliStatichePazzia.getInstance().getImgCaricamentoPEN(),
                    false
            );
        } else {
            if (VariabiliStatichePazzia.getInstance().isSlideShowAttivoIMM()) {
                UtilityPazzia.getInstance().AttivaTimerIMM(context);
            }

            UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                    VariabiliStatichePazzia.getInstance().getImgCaricamentoIMM(),
                    false
            );
        }
    }

    public void BloccaEsecuzione() {
        isCancelled = true;
    }
}
