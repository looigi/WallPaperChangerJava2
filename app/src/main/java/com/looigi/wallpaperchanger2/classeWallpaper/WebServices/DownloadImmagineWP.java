package com.looigi.wallpaperchanger2.classeWallpaper.WebServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.classePennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmagineWP {
    private boolean isCancelled;
    private InputStream in;
    private static final String NomeMaschera = "Download_Immagine_Wallpaper";
    private boolean Errore;
    private String NomeImmagine;
    private String PercorsoDIR = "";
    private Context context;
    private ImageView immagine;
    private String Url;

    public void EsegueChiamata(Context context, String NomeImmagine, ImageView immagine, String Url) {
        this.NomeImmagine = NomeImmagine;
        this.context = context;
        this.immagine = immagine;
        this.Url = Url;

        if (immagine == null) {
            UtilityWallpaper.getInstance().Attesa(true);
        }

        PercorsoDIR = context.getFilesDir() + "/Download";

        UtilityWallpaper.getInstance().CreaCartelle(PercorsoDIR);

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

        handlerThread = new HandlerThread("background-thread_WP_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > VariabiliStaticheWallpaper.TimeoutImmagine) {
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Timeout per Immagine Scaricata");

                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {

                        }
                        in = null;
                    }
                    UtilityPennetta.getInstance().Attesa(false);
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
            if (in != null) {
                mIcon11 = BitmapFactory.decodeStream(in);
            }
            if (immagine == null) {
                FileOutputStream outStream;
                try {
                    outStream = new FileOutputStream(PercorsoDIR + "/Appoggio.jpg"); // .getPathImmagine());
                    if (mIcon11 != null) {
                        mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    }

                    outStream.flush();
                    outStream.close();

                    if (immagine == null) {
                        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Immagine Scaricata");
                    }
                } catch (FileNotFoundException e) {
                    if (immagine == null) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "File non esistente per il download");

                        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Errore nel salvataggio su download Immagine: " + e.getMessage());
                    }
                    Errore = true;
                } catch (IOException e) {
                    if (immagine == null) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "Errore nel salvataggio su download Immagine");

                        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Errore nel salvataggio su download Immagine: " + e.getMessage());
                    }
                    Errore = true;
                }
            } else {
                Bitmap finalMIcon1 = mIcon11;

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        immagine.setImageBitmap(finalMIcon1);
                    }
                }, 100);

            }
        } catch (Exception e) {
            if (immagine == null) {
                UtilitiesGlobali.getInstance().ApreToast(context, "Errore sul download Immagine");

                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine: " + e.getMessage());
            }

            // e.printStackTrace();
            Errore = true;
        }
    }

    private void TermineEsecuzione() {
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
            }
        } else {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine.");
        }

        // if (immagine == null) {
            UtilityWallpaper.getInstance().Attesa(false);
        // }
    }

    public void BloccaEsecuzione() {
        isCancelled = true;
    }
}
