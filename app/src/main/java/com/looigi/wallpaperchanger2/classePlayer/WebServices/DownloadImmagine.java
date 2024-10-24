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
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.FileNotFoundException;
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

    public void EsegueDownload(Context context, ImageView bmImage, String Immagine) {
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
        isCancelled = false;
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

                                        VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().add(s);
                                        VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1);
                                        VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine().setText("Immagine " + VariabiliStatichePlayer.getInstance().getIdImmagineImpostata() +
                                                "/" + (VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1));
                                    }

                                    BitmapDrawable drawable = (BitmapDrawable) bmImage.getDrawable();
                                    Bitmap bitmap = drawable.getBitmap();

                                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "URL per salvataggio immagine: " + Immagine);
                                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Creo cartelle per salvataggio immagine: " + CartellaImmagine);
                                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Path salvataggio immagine: " + PathImmagine);
                                    Files.getInstance().CreaCartelle(CartellaImmagine); // .getCartellaImmagine());
                                    PathImmagine = PathImmagine.replace("\\", "/");
                                    VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(PathImmagine);

                                    FileOutputStream outStream;
                                    try {
                                        outStream = new FileOutputStream(PathImmagine); // .getPathImmagine());
                                        if (bitmap != null) {
                                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
                                        }
                                        /* 100 to keep full quality of the image */

                                        outStream.flush();
                                        outStream.close();

                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                                "Immagine Scaricata: " + PathImmagine);
                                    } catch (IOException e) {
                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                                "Errore nel salvataggio su download Immagine: " + e.getMessage());

                                        VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                                        UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                                        /* } catch (IOException e) {
                                        UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                                        "Errore nel salvataggio su download Immagine: " + e.getMessage());

                                        VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");
                                        UtilityPlayer.getInstance().ImpostaLogoApplicazione(context); */
                                    }
                                }
                            }, 100);
                        } else {
                            //     UtilityPlayer.getInstance().ImpostaImmagine(context);
                            UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                        }
                    } else {
                        UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                    }
                } catch (Exception e) {
                    UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);

                    // e.printStackTrace();
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,"Errore sul download immagine: " + e.getMessage());
                    // Utility.getInstance().ImpostaSfondoLogo();
                    Errore = true;
                }

                BloccaTimer();

                if (Errore || isCancelled) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera,
                            "Errore sul download immagine o blocco forzato. Imposto logo");

                    VariabiliStatichePlayer.getInstance().setPathUltimaImmagine("");

                    UtilityPlayer.getInstance().ImpostaLogoApplicazione(context);
                }

                VariabiliStatichePlayer.getInstance().setDownImmagine(null);
                UtilityPlayer.getInstance().Attesa(false);
                UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");

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
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                secondiPassati++;
                if (secondiPassati > VariabiliStatichePlayer.TimeoutImmagine) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Timeout per Immagine Scaricata");

                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException ignored) {

                        }
                        in = null;
                    }
                    UtilityPlayer.getInstance().Attesa(false);
                    UtilityPlayer.getInstance().AggiornaOperazioneInCorso("");
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
