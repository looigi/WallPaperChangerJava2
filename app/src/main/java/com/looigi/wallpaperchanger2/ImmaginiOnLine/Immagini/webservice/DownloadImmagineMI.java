package com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.Wallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.Wallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.Wallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.Wallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesLetturaInfoImmagine;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmagineMI {
    private boolean isCancelled;
    private static final String NomeMaschera = "Download_Image_MI";
    private boolean Errore;
    private String NomeImmagine;
    private String PercorsoDIR = "";
    private Context context;
    private ImageView immagine;
    private String Url;
    private InputStream in;
    private boolean PerCopia;
    private boolean PerWP;
    private Bitmap mIcon11;
    private String urldisplay;

    public void EsegueChiamata(Context context, String NomeImmagine, ImageView immagine,
                               String UrlImmagine, boolean PerCopia, boolean perWP) {
        isCancelled = false;
        this.NomeImmagine = NomeImmagine;
        this.context = context;
        this.immagine = immagine;
        this.Url = UrlImmagine;
        this.PerCopia = PerCopia;
        this.PerWP = perWP;

        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheMostraImmagini.getInstance().getImgCaricamento(),
                true
        );


        if (perWP) {
            PercorsoDIR = context.getFilesDir() + "/Download";
        } else {
            PercorsoDIR = context.getFilesDir() + "/Immagini";
        }

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

        handlerThread = new HandlerThread("background-thread_MI_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > VariabiliStaticheMostraImmagini.TimeoutImmagine) {
                    UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,
                            "Timeout per Immagine Scaricata");

                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {

                        }
                        in = null;
                    }
                    UtilitiesGlobali.getInstance().AttesaGif(
                            context,
                            VariabiliStaticheMostraImmagini.getInstance().getImgCaricamento(),
                            false
                    );
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
        urldisplay = Url;
        mIcon11 = null;
        try {
            in = new java.net.URL(urldisplay).openStream();
            if (in != null && !isCancelled) {
                mIcon11 = BitmapFactory.decodeStream(in);

                String Nome = "";
                if (PerCopia) {
                    Nome = "AppoggioCopia";
                } else {
                    Nome = "AppoggioMI";
                }
                // if (immagine == null) {
                FileOutputStream outStream;
                try {
                    outStream = new FileOutputStream(PercorsoDIR + "/" + Nome + ".jpg"); // .getPathImmagine());
                    if (mIcon11 != null && !isCancelled) {
                        mIcon11.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                    }

                    if (!isCancelled) {
                        outStream.flush();
                        outStream.close();

                        // if (immagine == null) {
                        UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Immagine Scaricata");
                        // }
                    }
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
            } else {
                UtilitiesGlobali.getInstance().ApreToast(context, "Struttura non esistente per il download");
            }

            // } else {
            if (!isCancelled) {
                if (!PerCopia) {
                    if (!PerWP) {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (immagine != null) {
                                    immagine.setImageBitmap(mIcon11);

                                    if (VariabiliStaticheMostraImmagini.getInstance().isSlideShowAttivo()) {
                                        UtilityImmagini.getInstance().RiattivaTimer();
                                    }
                                }
                            }
                        }, 100);
                    }
                }
            }
            // }
        } catch (Exception e) {
            if (immagine == null) {
                UtilitiesGlobali.getInstance().ApreToast(context, "Errore sul download Immagine");

                UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine: " + e.getMessage());
            }

            // e.printStackTrace();
            Errore = true;
        }
    }

    private void TermineEsecuzione() {
        if (!Errore && !isCancelled) {
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
            if (PerCopia) {
                String[] N = Url.split("/");
                String NomeImmagine = "DaImmagini/" + N[N.length - 1];

                String result = UtilitiesGlobali.getInstance().convertBmpToBase64(PercorsoDIR + "/AppoggioCopia.jpg");

                ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                ws.ScriveImmagineSuSfondiLocale(NomeImmagine, result);
            } else {
                if (PerWP) {
                    // Impostare immagine su wallpaper
                    String Path = PercorsoDIR + "/AppoggioMI.jpg";
                    String[] campiNome = NomeImmagine.split("/");
                    String Nome = "";
                    for (int i = 5; i < campiNome.length; i++) {
                        Nome += campiNome[i] + "/";
                    }
                    StrutturaImmagine src = new StrutturaImmagine();
                    src.setPathImmagine(Path);
                    src.setImmagine(Nome);
                    src.setDimensione("");
                    src.setDataImmagine(new Date().toString());

                    ChangeWallpaper c = new ChangeWallpaper(context, "WALLPAPER",
                            src);
                    c.setWallpaperLocale(context, src);
                } else {
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            UtilitiesGlobali.getInstance().AttesaGif(
                                    context,
                                    VariabiliStaticheMostraImmagini.getInstance().getImgCaricamento(),
                                    true
                            );

                            // Lettura e aggiornamento testojava e tags per singola immagine
                            if (VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale() != null) {
                                UtilitiesLetturaInfoImmagine u = new UtilitiesLetturaInfoImmagine(context);
                                if (VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale() == null) {
                                    VariabiliStaticheMostraImmagini.getInstance().setStrutturaImmagineAttuale(new StrutturaImmaginiLibrary());
                                    VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale().setTestoJava("");
                                }
                                String TestoJava = VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale().getTestoJava();
                                if (TestoJava == null) {
                                    TestoJava = "";
                                    VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale().setTestoJava("");
                                }
                                if (VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale().getTags() == null) {
                                    VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale().setTags("");
                                }
                                u.setImmagine(VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale());
                                u.ImpostaCategorieGiaMesse(TestoJava.toUpperCase().trim());
                                u.ImpostaListaCategorie(VariabiliStaticheMostraImmagini.getInstance().getListaCategorie());
                                u.ImpostaLayCategorie(VariabiliStaticheMostraImmagini.getInstance().getLayCategorieRilevate());
                                u.ImpostaLayScritte(VariabiliStaticheMostraImmagini.getInstance().getLayScritteRilevate());
                                u.ImpostaLayTasti(VariabiliStaticheMostraImmagini.getInstance().getLayTasti());
                                u.setUrl(urldisplay);
                                u.setBitmap(mIcon11);

                                u.AvviaControllo();
                            }
                            // Lettura e aggiornamento testojava e tags per singola immagine

                            UtilitiesGlobali.getInstance().AttesaGif(
                                    context,
                                    VariabiliStaticheMostraImmagini.getInstance().getImgCaricamento(),
                                    false
                            );
                        }
                    }, 100);
                }
            }
        } else {
            UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine.");
        }

        // if (immagine == null) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheMostraImmagini.getInstance().getImgCaricamento(),
                false
        );
        // }
    }

    public void BloccaEsecuzione() {
        isCancelled = true;
    }
}
