package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.webService;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.UtilitiesPreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesLetturaInfoImmagine;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmaginePreview {
    private boolean isCancelled;
    private static final String NomeMaschera = "Download_Image_PREVIEW";
    private boolean Errore;
    private String NomeImmagine;
    private String PercorsoDIR = "";
    private Context context;
    private ImageView immagine;
    private String Url;
    private InputStream in;

    public void EsegueChiamata(Context context, String NomeImmagine, ImageView immagine,
                               String UrlImmagine) {
        isCancelled = false;
        this.NomeImmagine = NomeImmagine;
        this.context = context;
        this.immagine = immagine;
        this.Url = UrlImmagine;

        UtilitiesPreview.getInstance().Attesa(true);

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

        handlerThread = new HandlerThread("background-thread_PV_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > VariabiliStaticheMostraImmagini.TimeoutImmagine) {
                    // UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,
                    //         "Timeout per Immagine Scaricata");

                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {

                        }
                        in = null;
                    }
                    UtilitiesPreview.getInstance().Attesa(false);
                    BloccaTimer();
                    BloccaEsecuzione();

                    UtilitiesGlobali.getInstance().ApreToast(context, "Timeout nel download");
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
        urldisplay = urldisplay.replace("\\", "/");
        Bitmap mIcon11 = null;
        try {
            in = new java.net.URL(urldisplay).openStream();
            if (in != null && !isCancelled) {
                mIcon11 = BitmapFactory.decodeStream(in);

                if (!isCancelled && mIcon11.getHeight() > 100 && mIcon11.getWidth() > 100) {
                    Bitmap finalMIcon1 = mIcon11;

                    String finalUrldisplay = urldisplay;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            immagine.setImageBitmap(finalMIcon1);

                            if (!VariabiliStatichePreview.getInstance().getModalita().equals("OCR")) {
                                UtilitiesPreview.getInstance().Attesa(true);

                                // Lettura e aggiornamento testojava e tags per singola immagine
                                if (VariabiliStatichePreview.getInstance().getStrutturaImmagine() != null) {
                                    UtilitiesLetturaInfoImmagine u = new UtilitiesLetturaInfoImmagine(context);
                                    String TestoJava = VariabiliStatichePreview.getInstance().getStrutturaImmagine().getTestoJava();
                                    if (TestoJava == null) {
                                        TestoJava = "";
                                        VariabiliStatichePreview.getInstance().getStrutturaImmagine().setTestoJava("");
                                    }
                                    if (VariabiliStatichePreview.getInstance().getStrutturaImmagine().getTags() == null) {
                                        VariabiliStatichePreview.getInstance().getStrutturaImmagine().setTags("");
                                    }
                                    u.setImmagine(VariabiliStatichePreview.getInstance().getStrutturaImmagine());
                                    u.ImpostaCategorieGiaMesse(
                                            TestoJava.toUpperCase().trim()
                                    );
                                    u.ImpostaListaCategorie(VariabiliStatichePreview.getInstance().getListaCategorie());
                                    u.ImpostaLayCategorie(VariabiliStatichePreview.getInstance().getLayCategorieRilevate());
                                    u.ImpostaLayScritte(VariabiliStatichePreview.getInstance().getLayScritteRilevate());
                                    u.ImpostaLayTasti(VariabiliStatichePreview.getInstance().getLayTasti());
                                    u.setUrl(finalUrldisplay);
                                    u.setBitmap(finalMIcon1);

                                    u.AvviaControllo();
                                }
                                // Lettura e aggiornamento testojava e tags per singola immagine

                                UtilitiesPreview.getInstance().Attesa(false);
                            }
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
    }

    private void TermineEsecuzione() {
        if (!Errore && !isCancelled) {
        } else {
        }

        // if (immagine == null) {
        UtilitiesPreview.getInstance().Attesa(false);
        // }
    }

    public void BloccaEsecuzione() {
        isCancelled = true;
    }

    private void ImpostaLogo() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                immagine.setImageBitmap(bitmap);
            }
        }, 10);
    }
}
