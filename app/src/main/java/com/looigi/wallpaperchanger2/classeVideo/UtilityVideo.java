package com.looigi.wallpaperchanger2.classeVideo;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.MediaController;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePazzia.VariabiliStatichePazzia;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.classeVideo.webservice.ChiamateWSV;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.log.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Video", Maschera,  Log);
            // }
        } else {

        }
    }

    private String testo;

    public void takeScreenShotMultipli(Context context, String id) {
        if (VariabiliStaticheVideo.getInstance().isStaAcquisendoVideo()) {
            UtilityDetector.getInstance().VisualizzaToast(context, "Acquisizione in corso", false);
            return;
        }
        VariabiliStaticheVideo.getInstance().setStaAcquisendoVideo(true);

        Attesa(true);
        VariabiliStaticheVideo.getInstance().getVideoView().pause();
        testo = "Elaborazione in corso";
        VariabiliStaticheVideo.getInstance().getTxtAvanzamento().setVisibility(LinearLayout.VISIBLE);
        VariabiliStaticheVideo.getInstance().getTxtAvanzamento().setText(testo);

        String link = VariabiliStaticheVideo.getInstance().getUltimoLink();
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(link);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
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

                        // UtilitiesGlobali.getInstance().ApreToast(context, "Immagine " + quale + "/" +
                        //         VariabiliStaticheVideo.getInstance().getNumeroFrames() + " acquisita");
                        testo += "\nImmagine " + quale + "/" +
                                 VariabiliStaticheVideo.getInstance().getNumeroFrames() + " acquisita";
                    } catch (IOException ignored) {
                        testo += "\nImmagine " + quale + "/" +
                                VariabiliStaticheVideo.getInstance().getNumeroFrames() + " ERRATA";
                    }
                    quale++;

                    VariabiliStaticheVideo.getInstance().getTxtAvanzamento().setText(testo);
                }

                db_dati_video db = new db_dati_video(context);
                db.ScriveSnapshot(id);
                db.ChiudeDB();

                VariabiliStaticheVideo.getInstance().getTxtAvanzamento().setVisibility(LinearLayout.GONE);
                VariabiliStaticheVideo.getInstance().setStaAcquisendoVideo(false);
                Attesa(false);
            }
        }, 500);
    }

    public void Attesa(boolean bAttesa) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (bAttesa) {
                    VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.VISIBLE);
                } else {
                    VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);
                }
            }
        }, 10);
    }

    public void takeScreenshot(Context context, String id) {
        if (VariabiliStaticheVideo.getInstance().isStaAcquisendoVideo()) {
            UtilityDetector.getInstance().VisualizzaToast(context, "Acquisizione in corso", false);
            return;
        }
        VariabiliStaticheVideo.getInstance().setStaAcquisendoVideo(true);

        Attesa(true);
        VariabiliStaticheVideo.getInstance().getVideoView().pause();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
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
                    // e.printStackTrace();
                }

                db_dati_video db = new db_dati_video(context);
                db.ScriveSnapshotS(id);
                db.ChiudeDB();

                VariabiliStaticheVideo.getInstance().setStaAcquisendoVideo(false);
                Attesa(false);
            }
        }, 500);
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
                    Attesa(true);

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
                            if (!VariabiliStaticheVideo.getInstance().isEntratoNelCampoDiTesto()) {
                                if (VariabiliStaticheVideo.getInstance().getVideoView() != null) {
                                    BloccaTimerAvanzamento();

                                    VariabiliStaticheVideo.getInstance().getVideoView().stopPlayback();
                                    VariabiliStaticheVideo.getInstance().getVideoView().clearAnimation();
                                    VariabiliStaticheVideo.getInstance().getVideoView().suspend(); // clears media player
                                    VariabiliStaticheVideo.getInstance().getVideoView().setVideoURI(null);
                                }

                                VariabiliStaticheVideo.getInstance().getAct().finish();
                            }
                        }
                        return true;
                    });

                    VariabiliStaticheVideo.getInstance().setMediaController(mediaController);
                    VariabiliStaticheVideo.getInstance().getMediaController().setAnchorView(VariabiliStaticheVideo.getInstance().getVideoView());
                    VariabiliStaticheVideo.getInstance().getMediaController().setPrevNextListeners(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Handle next click here
                            BloccaTimerAvanzamento();

                            ChiamateWSV ws = new ChiamateWSV(finalContext);
                            ws.RitornaProssimoVideo("VIDEO");
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Handle previous click here
                        }
                    });
                    Uri video = Uri.parse(link);
                    VariabiliStaticheVideo.getInstance().getTxtTitoloSeek().setText(prendeNomeVideo(link));
                    if (VariabiliStaticheVideo.getInstance().getVideoView() != null) {
                        VariabiliStaticheVideo.getInstance().getVideoView().setMediaController(
                                VariabiliStaticheVideo.getInstance().getMediaController());
                        VariabiliStaticheVideo.getInstance().getVideoView().setVideoURI(video);
                        if (VariabiliStaticheVideo.getInstance().isStaVedendo()) {
                            VariabiliStaticheVideo.getInstance().getVideoView().start();
                        } else {
                            VariabiliStaticheVideo.getInstance().getVideoView().pause();
                        }

                        VariabiliStaticheVideo.getInstance().getVideoView().setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                Attesa(false);

                                BloccaTimerAvanzamento();
                                VariabiliStaticheVideo.getInstance().getSeekScorri().setProgress(0);
                                VariabiliStaticheVideo.getInstance().getSeekScorri().setMax(0);
                                VariabiliStaticheVideo.getInstance().getSeekScorri2().setProgress(0);
                                VariabiliStaticheVideo.getInstance().getSeekScorri2().setMax(0);

                                VariabiliStaticheVideo.getInstance().getTxtAvanzamento().setText(formatTime(0));
                                VariabiliStaticheVideo.getInstance().getTxtMaxSeek().setText("00:00");

                                return false;
                            }
                        });
                        VariabiliStaticheVideo.getInstance().getVideoView().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                Attesa(false);

                                int videoWidth = mp.getVideoWidth();
                                int videoHeight = mp.getVideoHeight();

                                VariabiliStaticheVideo.getInstance().getVideoView().setVideoSize(videoWidth, videoHeight);

                                VariabiliStaticheVideo.getInstance().getVideoView().setMediaController(null);
                                VariabiliStaticheVideo.getInstance().getSeekScorri().setProgress(0);
                                VariabiliStaticheVideo.getInstance().getSeekScorri().setMax(
                                        VariabiliStaticheVideo.getInstance().getVideoView().getDuration()
                                );
                                VariabiliStaticheVideo.getInstance().getSeekScorri2().setProgress(0);
                                VariabiliStaticheVideo.getInstance().getSeekScorri2().setMax(
                                        VariabiliStaticheVideo.getInstance().getVideoView().getDuration()
                                );
                                VariabiliStaticheVideo.getInstance().getTxtAvanzamento().setText(formatTime(0));
                                VariabiliStaticheVideo.getInstance().getTxtMaxSeek().setText(
                                        formatTime(VariabiliStaticheVideo.getInstance().getVideoView().getDuration())
                                );

                                AttivaTimerBarraAvanzamento();
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
                }
            };
            handlerTimer.postDelayed(rTimer, 500);

        } catch (Exception e) {
            // TODO: handle exception
            // Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }

    private String prendeNomeVideo(String Video) {
        String Ritorno = "";

        int i = Video.indexOf("RobettaVaria");
        Ritorno = Video.substring(i + 13, Video.length());

        return Ritorno;
    }

    private Handler handlerAvanzamentoBarra;
    private Runnable updateRunnableAB;

    private void BloccaTimerAvanzamento() {
        VariabiliStaticheVideo.getInstance().setSecondiAlpha(0);
        if (handlerAvanzamentoBarra != null) {
            handlerAvanzamentoBarra.removeCallbacks(updateRunnableAB);
            updateRunnableAB = null;
        }
    }

    private void AttivaTimerBarraAvanzamento() {
        BloccaTimerAvanzamento();

        handlerAvanzamentoBarra = new Handler();
        updateRunnableAB = new Runnable() {
            @Override
            public void run() {
                if (VariabiliStaticheVideo.getInstance().getVideoView().isPlaying()) {
                    int currentPosition = VariabiliStaticheVideo.getInstance().getVideoView().getCurrentPosition();
                    VariabiliStaticheVideo.getInstance().getSeekScorri().setProgress(currentPosition);
                    VariabiliStaticheVideo.getInstance().getSeekScorri2().setProgress(currentPosition);
                    VariabiliStaticheVideo.getInstance().getTxtAvanzamento().setText(formatTime(currentPosition));
                    VariabiliStaticheVideo.getInstance().getTxtMaxSeek().setText(
                            formatTime(VariabiliStaticheVideo.getInstance().getVideoView().getDuration())
                    );

                    GestioneBarra();
                }

                handlerAvanzamentoBarra.postDelayed(this, 500);
            }
        };
        handlerAvanzamentoBarra.postDelayed(updateRunnableAB, 0);
    }

    private void GestioneBarra() {
        if (!VariabiliStaticheVideo.getInstance().isBarraOscurata()) {
            VariabiliStaticheVideo.getInstance().setSecondiAlpha(
                    VariabiliStaticheVideo.getInstance().getSecondiAlpha() + 1
            );
            if (VariabiliStaticheVideo.getInstance().getSecondiAlpha() > 10) {
                VariabiliStaticheVideo.getInstance().getLayBarraTasti().setAlpha(0.2f);
                VariabiliStaticheVideo.getInstance().setSecondiAlpha(0);
                VariabiliStaticheVideo.getInstance().setBarraOscurata(true);
            }
        }
    }

    private String formatTime(int millis) {
        int seconds = millis / 1000;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
    }

    public void AggiornaCategorie(Context context) {
        List<String> l = new ArrayList<>();

        for (String s : VariabiliStaticheVideo.getInstance().getListaCategorie()) {
            if (s.toUpperCase().trim().contains(
                    VariabiliStaticheVideo.getInstance().getFiltroCategoria().toUpperCase().trim())) {
                l.add(s);
            }
        }
        String[] ll = l.toArray(new String[0]);

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStaticheVideo.getInstance().getSpnCategorie(),
                ll,
                VariabiliStaticheVideo.getInstance().getCategoria()
        );

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, l);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                ll
        );
        VariabiliStaticheVideo.getInstance().getSpnCategorie().setAdapter(adapter); */
    }

    public void AggiornaCategorieSpostamento(Context context) {
        List<String> l = new ArrayList<>();

        for (String s : VariabiliStaticheVideo.getInstance().getListaCategorie()) {
            if (s.toUpperCase().trim().contains(
                    VariabiliStaticheVideo.getInstance().getFiltroCategoriaSpostamento().toUpperCase().trim())) {
                l.add(s);
            }
        }
        String[] ll = l.toArray(new String[0]);

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStaticheVideo.getInstance().getSpnSpostaCategorie(),
                ll,
                ""
        );

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, l);
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                ll
        );
        VariabiliStaticheVideo.getInstance().getSpnSpostaCategorie().setAdapter(adapter); */
    }
}