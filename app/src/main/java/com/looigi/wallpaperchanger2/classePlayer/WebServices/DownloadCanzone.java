package com.looigi.wallpaperchanger2.classePlayer.WebServices;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadCanzone {
    private static final String NomeMaschera = "Download_Brano_Player";
    private StrutturaBrano sb;
    // private boolean bloccatoCiclo = false;
    // private boolean skippatoBrano = false;
    private boolean erroreDownload = false;
    private int tempoImpiegato;
    private int totTempoDaImpiegare = 45;
    private int vecchiSecondi = -1;
    private float p3Appo;
    private String sAppo;
    private String percAppo;
    private float Dimensione;
    private double ultimiBytes = 0;
    private double attualeBytes = 0;
    private int contaUguale = 0;
    private boolean PulisceBrani = false;
    private Context context;
    private boolean Pregresso = false;
    private String UrlBrano = "";
    private boolean isCancelled;

    public void EsegueDownload(Context context, StrutturaBrano s, boolean Pregresso) {
        UtilityPlayer.getInstance().Attesa(true);
        UtilityPlayer.getInstance().AggiornaOperazioneInCorso("Download brano: " + s.getBrano());

        this.context = context;
        this.Pregresso = Pregresso;
        sb = s;
        tempoImpiegato = 0;
        ultimiBytes = 0;
        PulisceBrani = false;
        Dimensione = Math.round((s.getDimensione() / 1024F) * 100F) / 100F;
        UrlBrano = s.getUrlBrano();
        erroreDownload = false;
        isCancelled = false;

        AttivaTimer();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                Files.getInstance().CreaCartelle(sb.getCartellaBrano());

                int count;

                try {
                    URL url = new URL(UrlBrano);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty(
                            "Content-Type", "audio/mpeg" );
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.setDoInput(true);
                    // urlConnection.connect();

                    // this will be useful so that you can show a tipical 0-100%
                    // progress bar
                    int lenghtOfFile = urlConnection.getContentLength();
                    // OggettiAVideo.getInstance().getProgressDownload().setMax(lenghtOfFile);

                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Lunghezza file: " + Long.toString(lenghtOfFile));

                    // download the file
                    InputStream input = new BufferedInputStream(url.openStream(),8192);

                    // Output stream
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Creazione file output: " + sb.getPathBrano());
                    Files.getInstance().EliminaFileUnico(sb.getPathBrano());
                    OutputStream output = java.nio.file.Files.newOutputStream(Paths.get(sb.getPathBrano()));

                    byte[] data = new byte[1024];

                    long total = 0;

                    long dimensione = sb.getDimensione() * 1024;
                    int vecchioPerc = -1;

                    while ((count = input.read(data)) != -1 && !isCancelled) {
                        total += count;
                        // publishing the progress....
                        // After this onProgressUpdate will be called
                        // publishProgress("" + (int) (total));

                        // writing data to file
                        output.write(data, 0, count);

                        int perc = Math.round(((float) total / dimensione) * 100);
                        if (perc != vecchioPerc) {
                            UtilityPlayer.getInstance().AggiornaOperazioneInCorso("Download brano: " + s.getBrano() + " " + perc + "%");
                        }
                    }

                    if (!isCancelled) {
                        // flushing output
                        output.flush();

                        // closing streams
                        output.close();
                        input.close();
                    }
                } catch (Exception e) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Errore: " + e.getMessage());
                    erroreDownload = true;
                    Files.getInstance().EliminaFileUnico(sb.getPathBrano());
                }

                BloccaTimer();

                if (!erroreDownload) {
                    final Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            float DimensioneFile = Files.getInstance().DimensioniFile(sb.getPathBrano()) * 1024F;
                            float perc = Math.abs(Math.round(DimensioneFile / (Dimensione * 1024F * 1024F)) * 100F);
                            if (perc < 80 && Dimensione > 0F) {
                                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Elimino file " + sb.getPathBrano() + " in quanto più piccolo dell'80%");
                                Files.getInstance().EliminaFileUnico(sb.getPathBrano());
                            } else {
                                long dime = Files.getInstance().DimensioniFile(sb.getPathBrano());
                                UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "File scaricato: " + sb.getPathBrano() + ". Dimensioni: " + dime);
                                if (dime > 1000) {
                                    sb.setDimensione(dime * 1024L);

                                    if (!Pregresso) {
                                        db_dati_player db = new db_dati_player(context);
                                        db.ScriveBrano(sb);
                                        db.ScriveUltimoBranoAscoltato(sb);
                                        db.ChiudeDB();

                                        VariabiliStatichePlayer.getInstance().setUltimoBrano(sb);

                                        UtilityPlayer.getInstance().CaricaBranoNelLettore(context);
                                    } else {
                                        VariabiliStatichePlayer.getInstance().setStrutturaBranoPregressoCaricata(sb);
                                        VariabiliStatichePlayer.getInstance().setHaCaricatoBranoPregresso(true);

                                        UtilityPlayer.getInstance().ScriveBranoPregresso();
                                    }
                                } else {
                                    if (Pregresso) {
                                        VariabiliStatichePlayer.getInstance().setStrutturaBranoPregressoCaricata(null);
                                        VariabiliStatichePlayer.getInstance().setStaCaricandoBranoPregresso(false);
                                    }
                                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Elimino file. Dimensioni troppo piccole");
                                    Files.getInstance().EliminaFileUnico(sb.getPathBrano());
                                }
                            }
                            // OggettiAVideo.getInstance().getProgressDownload().setVisibility(LinearLayout.GONE);
                        }
                    }, 100);
                } else {
                    if (Pregresso) {
                        VariabiliStatichePlayer.getInstance().setStrutturaBranoPregressoCaricata(null);
                        VariabiliStatichePlayer.getInstance().setStaCaricandoBranoPregresso(false);
                    }
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Errore download brano");
                }

                VariabiliStatichePlayer.getInstance().setDownCanzone(null);
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
                if (secondiPassati > VariabiliStatichePlayer.TimeoutBrano) {
                    UtilityPlayer.getInstance().ScriveLog(context, NomeMaschera, "Timeout per Immagine Scaricata");

                    UtilityPlayer.getInstance().Attesa(false);
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
        VariabiliStatichePlayer.getInstance().setDownCanzone(null);
        isCancelled = true;
    }
}
