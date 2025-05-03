package com.looigi.wallpaperchanger2.classeImmaginiUguali.webService;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmagineUguali {
    private static final String NomeMaschera = "Download_Immagine_Uguali";
    private ImageView bmImage;
    private boolean Errore;
    private Context context;
    private boolean isCancelled;
    private InputStream in;

    public void EsegueDownload(Context context, ImageView bmImage, String UrlImmagine) {
        this.context = context;
        this.bmImage = bmImage;

        // AttivaTimer();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Errore = false;

                String urldisplay = UrlImmagine;
                urldisplay = urldisplay.replace("\\", "/");
                Bitmap mIcon11 = null;
                try {
                    in = new java.net.URL(urldisplay).openStream();
                    if (in != null && !isCancelled) {
                        mIcon11 = BitmapFactory.decodeStream(in);

                        if (!isCancelled && mIcon11.getHeight() > 100 && mIcon11.getWidth() > 100) {
                            Bitmap finalMIcon1 = mIcon11;

                            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    bmImage.setImageBitmap(finalMIcon1);
                                }
                            }, 100);
                        } else {
                            ImpostaLogo();
                        }
                    } else {
                        ImpostaLogo();
                    }
                } catch (Exception e) {
                    ImpostaLogo();

                    Errore = true;
                }

                // BloccaTimer();

                /* switch (Modalita) {
                    case "IMMAGINI":
                        UtilityImmagini.getInstance().Attesa(false);
                        break;
                    case "PLAYER":
                        UtilityPlayer.getInstance().Attesa(false);
                        break;
                } */

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

    /* private void AttivaTimer() {
        secondiPassati = 0;

        handlerThread = new HandlerThread("background-thread_" +
                VariabiliStatichePlayer.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > VariabiliStatichePlayer.TimeoutImmagine) {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {

                        }
                        in = null;
                    }

                    ImpostaLogo();

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
    } */

    public void BloccaEsecuzione() {
        VariabiliStatichePlayer.getInstance().setDownImmagine(null);
        isCancelled = true;
    }

    private void ImpostaLogo() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                bmImage.setImageBitmap(bitmap);
            }
        }, 10);
    }
}
