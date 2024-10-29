package com.looigi.wallpaperchanger2.classeVideo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classeVideo.webservice.ChiamateWSV;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.FileOutputStream;
import java.io.IOException;

public class UtilityVideo {
    private static UtilityVideo instance = null;
    private MediaController mediaController = null;

    private UtilityVideo() {
    }

    public static UtilityVideo getInstance() {
        if (instance == null) {
            instance = new UtilityVideo();
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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("VIDEO", Maschera,  Log);
            // }
        } else {

        }
    }

    public void takeScreenShotMultipli(Context context) {
        VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.VISIBLE);
        VariabiliStaticheVideo.getInstance().getVideoView().pause();

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
                UtilitiesGlobali.getInstance().ApreToast(context, "Immagine " + quale + "/" +
                        VariabiliStaticheVideo.getInstance().getNumeroFrames() + " acquisita");
            } catch (IOException ignored) {
            }
        }
        VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);
    }

    public void takeScreenshot(Context context) {
        VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.VISIBLE);
        VariabiliStaticheVideo.getInstance().getVideoView().pause();

        try {
            String link = VariabiliStaticheVideo.getInstance().getUltimoLink();

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(link);
            int secondi = VariabiliStaticheVideo.getInstance().getVideoView().getCurrentPosition() * 1000;
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
        VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);
    }

    public void ImpostaVideo() {
        Context context = VariabiliStaticheVideo.getInstance().getContext();
        if (context == null) {
            context = UtilitiesGlobali.getInstance().tornaContextValido();
        }
        try {
            String link = VariabiliStaticheVideo.getInstance().getUltimoLink();

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Context finalContext = context;
            Runnable rTimer = new Runnable() {
                public void run() {
                    VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.VISIBLE);
                    if (VariabiliStaticheVideo.getInstance().isBarraVisibile()) {
                        mediaController = new MediaController(finalContext) {
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
                        mediaController = new MediaController(finalContext);
                    }
                    mediaController.addOnUnhandledKeyEventListener((v, event) -> {
                        //Handle BACK button
                        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP)
                        {
                            if (VariabiliStaticheVideo.getInstance().getVideoView() != null) {
                                VariabiliStaticheVideo.getInstance().getVideoView().stopPlayback();
                                VariabiliStaticheVideo.getInstance().getVideoView().clearAnimation();
                                VariabiliStaticheVideo.getInstance().getVideoView().suspend(); // clears media player
                                VariabiliStaticheVideo.getInstance().getVideoView().setVideoURI(null);
                            }

                            VariabiliStaticheVideo.getInstance().getAct().finish();
                        }
                        return true;
                    });

                    VariabiliStaticheVideo.getInstance().setMediaController(mediaController);
                    VariabiliStaticheVideo.getInstance().getMediaController().setAnchorView(VariabiliStaticheVideo.getInstance().getVideoView());
                    VariabiliStaticheVideo.getInstance().getMediaController().setPrevNextListeners(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Handle next click here
                            ChiamateWSV ws = new ChiamateWSV(finalContext);
                            ws.RitornaProssimoVideo();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Handle previous click here
                        }
                    });
                    Uri video = Uri.parse(link);
                    VariabiliStaticheVideo.getInstance().getVideoView().setMediaController(
                            VariabiliStaticheVideo.getInstance().getMediaController());
                    VariabiliStaticheVideo.getInstance().getVideoView().setVideoURI(video);
                    VariabiliStaticheVideo.getInstance().getVideoView().start();

                    VariabiliStaticheVideo.getInstance().getVideoView().setOnErrorListener(new MediaPlayer.OnErrorListener() {
                        @Override
                        public boolean onError(MediaPlayer mp, int what, int extra) {
                            VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);

                            return false;
                        }
                    });
                    VariabiliStaticheVideo.getInstance().getVideoView().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);
                        }
                    });
                    VariabiliStaticheVideo.getInstance().getVideoView().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                        }
                    });

                    /* if (VariabiliStaticheVideo.getInstance().isBarraVisibile()) {
                        // BARRA Visibile
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.postDelayed(
                        new Runnable() {
                            public void run() {
                                VariabiliStaticheVideo.getInstance().getMediaController().show(0);
                            }
                        }, 500);
                    } */
                }
            };
            handlerTimer.postDelayed(rTimer, 500);

        } catch (Exception e) {
            // TODO: handle exception
            // Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }
}