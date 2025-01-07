package com.looigi.wallpaperchanger2.classeWallpaper.WebServices;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.StrictMode;
import android.widget.ImageView;

import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.classeModificaImmagine.MainModificaImmagine;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classePennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
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
    private boolean PerModifica;
    private String Operazione;

    public void EsegueChiamata(Context context, String NomeImmagine, ImageView immagine, String Url,
                               boolean PerModifica, String Operazione) {
        if (context == null) {
            return;
        }

        this.NomeImmagine = NomeImmagine;
        this.context = context;
        this.immagine = immagine;
        this.Url = Url;
        this.PerModifica = PerModifica;
        this.Operazione = Operazione;

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
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                            "Timeout per Immagine Scaricata");

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
                String NomeFile = "";
                if (PerModifica) {
                    NomeFile = "AppoggioWP";
                } else {
                    NomeFile = "Appoggio";
                }
                FileOutputStream outStream;
                try {
                    outStream = new FileOutputStream(PercorsoDIR + "/" + NomeFile + ".jpg"); // .getPathImmagine());
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

                String NomeFile = "";
                if (PerModifica) {
                    NomeFile = "AppoggioWP";
                } else {
                    NomeFile = "Appoggio";
                }

                StrutturaImmagine si = new StrutturaImmagine();
                si.setPathImmagine(PercorsoDIR + "/" + NomeFile + ".jpg");
                si.setImmagine(sNomeImmagine);
                if (VariabiliStaticheWallpaper.getInstance().getUltimaImmagine() != null) {
                    si.setDataImmagine(VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getDataImmagine());
                    si.setDimensione(VariabiliStaticheWallpaper.getInstance().getUltimaImmagine().getDimensione());
                } else {
                    si.setDataImmagine("---");
                    si.setDimensione("---");
                }

                if(!PerModifica) {
                    VariabiliStaticheWallpaper.getInstance().setUltimaImmagine(si);

                    ChangeWallpaper c = new ChangeWallpaper(context, "WALLPAPER");
                    c.setWallpaperLocale(context, si);
                } else {
                    String finalNomeFile = NomeFile;

                    switch (Operazione) {
                        case "MODIFICA":
                            Handler handlerTimer = new Handler(Looper.getMainLooper());
                            Runnable rTimer = new Runnable() {
                                public void run() {
                                    String Path = context.getFilesDir() + "/Download/" + finalNomeFile + ".jpg";

                                    VariabiliStaticheModificaImmagine.getInstance().setMascheraApertura("WALLPAPER2");
                                    VariabiliStaticheModificaImmagine.getInstance().setNomeImmagine(
                                            Path
                                    );
                                    Intent i = new Intent(context, MainModificaImmagine.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(i);
                                }
                            };
                            handlerTimer.postDelayed(rTimer, 100);
                            break;

                        case "CONDIVIDI":
                            Handler handlerTimer2 = new Handler(Looper.getMainLooper());
                            Runnable rTimer2 = new Runnable() {
                                public void run() {
                                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                    StrictMode.setVmPolicy(builder.build());

                                    String Path = context.getFilesDir() + "/Download/" + finalNomeFile + ".jpg";

                                    File f = new File(Path);
                                    Uri uri = FileProvider.getUriForFile(context,
                                            context.getApplicationContext().getPackageName() + ".provider",
                                            f);

                                    Intent i = new Intent(Intent.ACTION_SEND);
                                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                                    i.putExtra(Intent.EXTRA_SUBJECT, si.getImmagine());
                                    i.putExtra(Intent.EXTRA_TEXT, "Dettagli nel file allegato");
                                    i.putExtra(Intent.EXTRA_STREAM, uri);
                                    i.setType(UtilityWallpaper.getInstance().GetMimeType(context, uri));
                                    context.startActivity(Intent.createChooser(i, "Share wallpaper"));
                                }
                            };
                            handlerTimer2.postDelayed(rTimer2, 100);
                            break;
                    }
                }
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
