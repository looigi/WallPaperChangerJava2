package com.looigi.wallpaperchanger2.classeFilms;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.MediaController;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFilms.webservice.ChiamateWSF;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classeVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.FileOutputStream;
import java.io.IOException;

public class UtilityFilms {
    private static UtilityFilms instance = null;
    private MediaController mediaController = null;

    private UtilityFilms() {
    }

    public static UtilityFilms getInstance() {
        if (instance == null) {
            instance = new UtilityFilms();
        }

        return instance;
    }

    public void ScriveLog(Context context, String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, true);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityDetector.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) { */
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Films", Maschera,  Log);
            // }
        } else {

        }
    }

    public void takeScreenShotMultipli(Context context) {
        VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.VISIBLE);
        VariabiliStaticheFilms.getInstance().getFilmsView().pause();

        String link = VariabiliStaticheVideo.getInstance().getUltimoLink();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(link);
        int tempoTotale = VariabiliStaticheVideo.getInstance().getVideoView().getDuration() * 1000;
        int ogniSecondi = tempoTotale / VariabiliStaticheVideo.getInstance().getNumeroFrames();
        int quale = 0;

        String Cartella = UtilityDetector.getInstance().PrendePath(context);
        UtilityWallpaper.getInstance().CreaCartelle(Cartella);
        UtilityDetector.getInstance().ControllaFileNoMedia(Cartella);
        String[] n = link.split("/");
        String nn = n[n.length - 1];
        String[] e = nn.split("\\.");
        String est = e[e.length - 1];
        nn = nn.replace("." + est, "");
        int conta = 0;

        for (int secondi = 0; secondi <= tempoTotale; secondi += ogniSecondi) {
            Bitmap thummbnailBitmap = mmr.getFrameAtTime(secondi);
            String sconta = String.format("%03d", conta);
            String nomeFile = "Frame_" + nn + "_" + sconta + ".jpg";
            while (Files.getInstance().EsisteFile(Cartella + nomeFile)) {
                conta++;
                sconta = String.format("%03d", conta);
                nomeFile = "Frame_" + nn + "_" + sconta + ".jpg";
            }
            String Dest = Cartella + nomeFile;
            try (FileOutputStream out = new FileOutputStream(Dest)) {
                thummbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                quale++;
                UtilitiesGlobali.getInstance().ApreToast(context, "Immagine " + quale  + "/" +
                        VariabiliStaticheFilms.getInstance().getNumeroFrames() + " acquisita");
            } catch (IOException ignored) {
            }
        }
        VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);
    }

    public void takeScreenshot(Context context) {
        VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.VISIBLE);
        VariabiliStaticheFilms.getInstance().getFilmsView().pause();

        try {
            String link = VariabiliStaticheFilms.getInstance().getUltimoLink();

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(link);
            int secondi = VariabiliStaticheFilms.getInstance().getFilmsView().getCurrentPosition() * 1000;
            Bitmap thummbnailBitmap = mmr.getFrameAtTime(secondi);

            String Cartella = UtilityDetector.getInstance().PrendePath(context);
            UtilityWallpaper.getInstance().CreaCartelle(Cartella);
            UtilityDetector.getInstance().ControllaFileNoMedia(Cartella);
            String[] n = link.split("/");
            String nn = n[n.length - 1];
            String[] e = nn.split("\\.");
            String est = e[e.length - 1];
            nn = nn.replace("." + est, "");
            int conta = 1;
            String sconta = String.format("%03d", conta);
            String nomeFile = "Frame_" + nn + "_" + sconta + ".jpg";
            while (Files.getInstance().EsisteFile(Cartella + nomeFile)) {
                conta++;
                sconta = String.format("%03d", conta);
                nomeFile = "Frame_" + nn + "_" + sconta + ".jpg";
            }
            String Dest = Cartella + nomeFile;
            try (FileOutputStream out = new FileOutputStream(Dest)) {
                thummbnailBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

                UtilitiesGlobali.getInstance().ApreToast(context, "Immagine acquisita");
            } catch (IOException ignored) {
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.GONE);
    }

    public void ImpostaFilms() {
        Context context = VariabiliStaticheFilms.getInstance().getContext();
        if (context == null) {
            context = UtilitiesGlobali.getInstance().tornaContextValido();
        }
        try {
            String link = VariabiliStaticheFilms.getInstance().getUltimoLink();

            VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.VISIBLE);
            if (VariabiliStaticheFilms.getInstance().isBarraVisibile()) {
                mediaController = new MediaController(context) {
                    @Override
                    public void show() {
                        show(0);
                    }

                    @Override
                    public void show(int timeout) {
                        super.show(0);
                    }
                };
            } else {
                mediaController = new MediaController(context);
            }
            mediaController.addOnUnhandledKeyEventListener((v, event) -> {
                //Handle BACK button
                if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                {
                    if (VariabiliStaticheFilms.getInstance().getFilmsView() != null) {
                        VariabiliStaticheFilms.getInstance().getFilmsView().stopPlayback();
                        VariabiliStaticheFilms.getInstance().getFilmsView().clearAnimation();
                        VariabiliStaticheFilms.getInstance().getFilmsView().suspend(); // clears media player
                        VariabiliStaticheFilms.getInstance().getFilmsView().setVideoURI(null);
                    }

                    VariabiliStaticheFilms.getInstance().getAct().finish();
                }
                return true;
            });

            VariabiliStaticheFilms.getInstance().setMediaController(mediaController);
            VariabiliStaticheFilms.getInstance().getMediaController().setAnchorView(VariabiliStaticheFilms.getInstance().getFilmsView());
            Context finalContext = context;
            VariabiliStaticheFilms.getInstance().getMediaController().setPrevNextListeners(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Handle next click here
                    ChiamateWSF ws = new ChiamateWSF(finalContext);
                    ws.RitornaProssimoFilms();
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Handle previous click here
                }
            });
            Uri Films = Uri.parse(link);
            VariabiliStaticheFilms.getInstance().getFilmsView().setMediaController(
                    VariabiliStaticheFilms.getInstance().getMediaController());
            VariabiliStaticheFilms.getInstance().getFilmsView().setVideoURI(Films);
            VariabiliStaticheFilms.getInstance().getFilmsView().start();
            VariabiliStaticheFilms.getInstance().getFilmsView().setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.GONE);

                    return false;
                }
            });
            VariabiliStaticheFilms.getInstance().getFilmsView().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // mediaController.show(0);

                    VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.GONE);
                }
            });

            if (VariabiliStaticheFilms.getInstance().isBarraVisibile()) {
                // BARRA Visibile
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(
                        new Runnable() {
                            public void run() {
                                VariabiliStaticheFilms.getInstance().getMediaController().show(0);
                            }
                        }, 500);
            }
        } catch (Exception e) {
            // TODO: handle exception
            // Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }
}
