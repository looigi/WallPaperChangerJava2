package com.looigi.wallpaperchanger2.classePlayer.WebServices;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadImmagine {
    private static final String NomeMaschera = "Download_Immagine_Player";
    private ImageView bmImage;
    private String Immagine;
    private String PathImmagine;
    private String CartellaImmagine;
    private boolean Errore;
    private Context context;
    private boolean isCancelled;
    private InputStream in;
    private boolean Interna;
    private boolean PerCopia;
    private String NomeImmagine;

    public void EsegueDownload(Context context, ImageView bmImage, String Immagine,
                               boolean Interna, boolean PerCopia, String NomeImmagine) {
        UtilityPlayer.getInstance().Attesa(true);
        UtilityPlayer.getInstance().ImpostaTastiSfondo(false);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("Download immagine");

        this.context = context;
        this.bmImage = bmImage;
        this.Immagine = Immagine;
        this.PerCopia = PerCopia;
        this.NomeImmagine = NomeImmagine;

        if (!PerCopia) {
            PathImmagine = Immagine;
            PathImmagine = PathImmagine.replace(VariabiliStatichePlayer.PercorsoBranoMP3SuURL + "/", "");
            PathImmagine = context.getFilesDir() + "/Player/" + PathImmagine;
            PathImmagine = PathImmagine.replace("\\", "/");
        } else {
            PathImmagine = context.getFilesDir() + "/Appoggio/AppoggioWP.jpg";
        }
        CartellaImmagine = "";
        isCancelled = false;
        this.Interna = Interna;
        String[] c = PathImmagine.split("/");
        for (int i = 0; i < c.length - 1; i++) {
            CartellaImmagine += c[i] + "/";
        }

        AttivaTimer();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Errore = false;
                String urldisplay = Immagine;
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
                                    bmImage.setImageBitmap(finalMIcon1);

                                    if (!PerCopia) {
                                        if (VariabiliStatichePlayer.getInstance().getImgSfondoSettings() != null) {
                                            VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(finalMIcon1);
                                            String[] n = PathImmagine.split("/");
                                            String NomeImmagine = n[n.length - 1];
                                            String Cartella = PathImmagine.replace(NomeImmagine, "");

                                            StrutturaImmagini s = new StrutturaImmagini();
                                            s.setPathImmagine(PathImmagine);
                                            s.setNomeImmagine(NomeImmagine);
                                            s.setCartellaImmagine(Cartella);
                                            s.setArtista(VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista());
                                            s.setAlbum(VariabiliStatichePlayer.getInstance().getUltimoBrano().getAlbum());
                                            s.setUrlImmagine(finalUrldisplay);

                                            // VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().add(s);
                                            // VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1);
                                        }

                                        UtilityPlayer.getInstance().ScriveInfoImmagine();
                                    }

                                    BitmapDrawable drawable = (BitmapDrawable) bmImage.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();

                                    if (!PerCopia) {
                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "URL per salvataggio immagine: " + Immagine);
                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Creo cartelle per salvataggio immagine: " + CartellaImmagine);
                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Path salvataggio immagine: " + PathImmagine);
                                    }

                                    Files.getInstance().CreaCartelle(CartellaImmagine); // .getCartellaImmagine());
                                    PathImmagine = PathImmagine.replace("\\", "/");
                                    if (!PerCopia) {
                                        VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(PathImmagine);
                                    }

                                    FileOutputStream outStream;
                                    try {
                                        outStream = new FileOutputStream(PathImmagine); // .getPathImmagine());
                                        if (bitmap != null) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                                        }
                                        /* 100 to keep full quality of the image */

                                        outStream.flush();
                                        outStream.close();

                                        if (!PerCopia) {
                                            long Spazio = VariabiliStatichePlayer.getInstance().getSpazioOccupato();
                                            Spazio += (Files.getInstance().DimensioniFile(PathImmagine) * 1024);
                                            VariabiliStatichePlayer.getInstance().setSpazioOccupato(Spazio);
                                            UtilityPlayer.getInstance().ScrivePerc();

                                            VariabiliStatichePlayer.getInstance().setCeImmaginePerModifica(true);

                                            UtilityPlayer.getInstance().AggiornaInformazioni(false);

                                            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                                    "Immagine Scaricata: " + PathImmagine);
                                        } else {
                                            // String[] N = PathImmagine.split("/");
                                            // String NomeImmagine = "DaPlayer/" + N[N.length - 1];

                                            UtilityPlayer.getInstance().ImpostaTastiSfondo(true);

                                            String result = UtilitiesGlobali.getInstance().convertBmpToBase64(PathImmagine);

                                            ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                                            ws.ScriveImmagineSuSfondiLocale("DaPlayer/" + NomeImmagine, result);
                                        }
                                    } catch (IOException e) {
                                        if (!PerCopia) {
                                            UtilityPlayer.getInstance().ImpostaTastiSfondo(true);

                                            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                                    "Errore nel salvataggio su download Immagine: " + e.getMessage());

                                            if (!Interna) {
                                                VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                                                UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                                            } else {
                                                UtilityPlayer.getInstance().ImpostaLogoApplicazioneInterna(context);
                                            }

                                            /* } catch (IOException e) {
                                            UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                            "Errore nel salvataggio su download Immagine: " + e.getMessage());

                                            VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                                            UtilityPlayer.getInstance().ImpostaLogoApplicazione(context); */
                                        }
                                    }
                                }
                            }, 100);
                        } else {
                            if (!PerCopia) {
                                UtilityPlayer.getInstance().ImpostaTastiSfondo(true);

                                if (!Interna) {
                                    VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                                    UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                                } else {
                                    UtilityPlayer.getInstance().ImpostaLogoApplicazioneInterna(context);
                                }
                            }
                        }
                    } else {

                        if (!PerCopia) {
                            UtilityPlayer.getInstance().ImpostaTastiSfondo(true);

                            if (!Interna) {
                                VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                                UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                            } else {
                                UtilityPlayer.getInstance().ImpostaLogoApplicazioneInterna(context);
                            }
                        }
                    }
                } catch (Exception e) {
                    if (!PerCopia) {
                        UtilityPlayer.getInstance().ImpostaTastiSfondo(true);

                        if (!Interna) {
                            VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                            UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                        } else {
                            UtilityPlayer.getInstance().ImpostaLogoApplicazioneInterna(context);
                        }
                    }

                    // e.printStackTrace();
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine: " + e.getMessage());
                    // Utility.getInstance().ImpostaSfondoLogo();
                    Errore = true;
                }

                BloccaTimer();

                if (Errore || isCancelled) {
                    if (!PerCopia) {
                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                "Errore sul download immagine o blocco forzato. Imposto logo");

                        if (!Interna) {
                            VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                            UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                        } else {
                            UtilityPlayer.getInstance().ImpostaLogoApplicazioneInterna(context);
                        }
                    }
                }

                UtilityPlayer.getInstance().Attesa(false);

                if (!PerCopia) {
                    VariabiliStatichePlayer.getInstance().setDownImmagine(null);
                    UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");
                    UtilityPlayer.getInstance().ImpostaTastiSfondo(true);
                }

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

        handlerThread = new HandlerThread("background-thread_" +
                VariabiliStatichePlayer.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > VariabiliStatichePlayer.TimeoutImmagine) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                            "Timeout per Immagine Scaricata");

                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {

                        }
                        in = null;
                    }

                    if (!PerCopia) {
                        UtilityPlayer.getInstance().Attesa(false);
                        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");
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

    public void BloccaEsecuzione() {
        VariabiliStatichePlayer.getInstance().setDownImmagine(null);
        isCancelled = true;
    }
}
