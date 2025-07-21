package com.looigi.wallpaperchanger2.classePazzia;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeModificheCodice.VariabiliStaticheModificheCodice;
import com.looigi.wallpaperchanger2.classePennetta.webservice.ChiamateWSPEN;
import com.looigi.wallpaperchanger2.classeVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.classeVideo.webservice.ChiamateWSV;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import pl.droidsonroids.gif.GifImageView;

public class UtilityPazzia {
    private static UtilityPazzia instance = null;

    private UtilityPazzia() {
    }

    public static UtilityPazzia getInstance() {
        if (instance == null) {
            instance = new UtilityPazzia();
        }

        return instance;
    }

    public void CambiaImmaginePennetta(Context context) {
        bloccaTimerPEN();

        ChiamateWSPEN ws = new ChiamateWSPEN(context);
        ws.RitornaProssimaImmagine(
                VariabiliStatichePazzia.getInstance().getCategoriaPennetta(),
                "PAZZIA"
        );
    }

    public void CambiaImmagineImmagine(Context context) {
        bloccaTimerIMM();

        int idCategoria = -1;

        for (StrutturaImmaginiCategorie c : VariabiliStatichePazzia.getInstance().getListaCategorieIMM()) {
            if (c.getCategoria().equals(VariabiliStatichePazzia.getInstance().getCategoriaImmagini())) {
                idCategoria = c.getIdCategoria();
                break;
            }
        }

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.RitornaProssimaImmagine(
                idCategoria,
                VariabiliStatichePazzia.getInstance().getUltimaImmagine(),
                "S",
                "PAZZIA"
        );
    }

    public void CambiaVideo(Context context) {
        ChiamateWSV ws = new ChiamateWSV(context);
        ws.RitornaProssimoVideo("PAZZIA");
    }

    private Handler handlerPEN;
    private Runnable updateRunnablePEN;
    private Handler handlerIMM;
    private Runnable updateRunnableIMM;
    private int SecondiPEN = 7500;
    private int SecondiIMM = 10000;

    public void ImpostaAttesaPazzia(GifImageView chi, boolean come) {
        if (chi != null) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    if (come) {
                        chi.setVisibility(LinearLayout.VISIBLE);
                    } else {
                        chi.setVisibility(LinearLayout.GONE);
                    }
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }

    public void bloccaTimerPEN() {
        if (handlerPEN != null) {
            handlerPEN.removeCallbacks(
                    updateRunnablePEN
            );
            handlerPEN.removeCallbacksAndMessages(
                    updateRunnablePEN
            );
            updateRunnablePEN = null;
        }
    }

    public void bloccaTimerIMM() {
        if (handlerIMM != null) {
            handlerIMM.removeCallbacks(
                    updateRunnableIMM
            );
            handlerIMM.removeCallbacksAndMessages(
                    updateRunnableIMM
            );
            updateRunnableIMM = null;
        }
    }

    public void AttivaTimerPEN(Context context) {
        bloccaTimerPEN();

        if (VariabiliStatichePazzia.getInstance().isSlideShowAttivoPEN()) {
            handlerPEN = new Handler(Looper.getMainLooper());
            updateRunnablePEN = new Runnable() {
                @Override
                public void run() {
                    CambiaImmaginePennetta(context);
                }
            };
            handlerPEN.postDelayed(
                    updateRunnablePEN,
                    SecondiPEN);
        }
    }

    public void AttivaTimerIMM(Context context) {
        bloccaTimerIMM();

        if (VariabiliStatichePazzia.getInstance().isSlideShowAttivoIMM()) {
            handlerIMM = new Handler(Looper.getMainLooper());
            updateRunnableIMM = new Runnable() {
                @Override
                public void run() {
                    CambiaImmagineImmagine(context);
                }
            };
            handlerIMM.postDelayed(
                    updateRunnableIMM,
                    SecondiIMM
            );
        }
    }

    private MediaController mediaController = null;

    public void ImpostaVideo(Context context) {
        try {
            String link = VariabiliStaticheVideo.getInstance().getUltimoLink();

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Context finalContext = context;
            Runnable rTimer = new Runnable() {
                public void run() {
                    UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                            VariabiliStatichePazzia.getInstance().getImgCaricamentoVID(),
                            true
                    );

                    /*
                    // if (VariabiliStatichePazzia.getInstance().isBarraVisibile()) {
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
                    // } else {
                    //     mediaController = new MediaController(finalContext);
                    // }
                    mediaController.addOnUnhandledKeyEventListener((v, event) -> {
                        //Handle BACK button
                        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                            return false;
                        }
                        return false;
                    });

                    VariabiliStatichePazzia.getInstance().setMediaController(mediaController);
                    VariabiliStatichePazzia.getInstance().getMediaController().setAnchorView(
                            VariabiliStatichePazzia.getInstance().getVideoView()
                    );
                    VariabiliStatichePazzia.getInstance().getMediaController().setPrevNextListeners(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Handle next click here
                            UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                                    VariabiliStatichePazzia.getInstance().getImgCaricamentoVID(),
                                    false
                            );

                            // BloccaTimerAvanzamento();

                            CambiaVideo(context);
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Handle previous click here
                        }
                    });
                    */

                    Uri video = Uri.parse(link);
                    // VariabiliStatichePazzia.getInstance().getTxtTitoloSeek().setText(prendeNomeVideo(link));
                    if (VariabiliStatichePazzia.getInstance().getVideoView() != null) {
                        VariabiliStatichePazzia.getInstance().getVideoView().setMediaController(
                                VariabiliStatichePazzia.getInstance().getMediaController());
                        VariabiliStatichePazzia.getInstance().getVideoView().setVideoURI(video);
                        VariabiliStatichePazzia.getInstance().getVideoView().start();

                        VariabiliStatichePazzia.getInstance().getVideoView().setOnErrorListener(new MediaPlayer.OnErrorListener() {
                            @Override
                            public boolean onError(MediaPlayer mp, int what, int extra) {
                                UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                                        VariabiliStatichePazzia.getInstance().getImgCaricamentoVID(),
                                        false
                                );

                                BloccaTimerVID();

                                CambiaVideo(context);

                                VariabiliStatichePazzia.getInstance().getSeekBar().setProgress(0);
                                VariabiliStatichePazzia.getInstance().getSeekBar().setMax(0);

                                return false;
                            }
                        });
                        VariabiliStatichePazzia.getInstance().getVideoView().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                                        VariabiliStatichePazzia.getInstance().getImgCaricamentoVID(),
                                        false
                                );

                                int videoWidth = mp.getVideoWidth();
                                int videoHeight = mp.getVideoHeight();

                                VariabiliStatichePazzia.getInstance().getVideoView().setVideoSize(videoWidth, videoHeight);
                                mp.setVolume(0f, 0f);

                                VariabiliStatichePazzia.getInstance().getVideoView().setMediaController(null);

                                VariabiliStatichePazzia.getInstance().getSeekBar().setProgress(0);
                                VariabiliStatichePazzia.getInstance().getSeekBar().setMax(
                                        VariabiliStatichePazzia.getInstance().getVideoView().getDuration()
                                );
                                AttivaTimerBarraVID();
                            }
                        });
                        VariabiliStatichePazzia.getInstance().getVideoView().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                CambiaVideo(context);
                            }
                        });
                    }
                }
            };
            handlerTimer.postDelayed(rTimer, 500);

        } catch (Exception e) {
            // TODO: handle exception
            // Toast.makeText(this, "Error connecting", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler handlerAvanzamentoBarra;
    private Runnable updateRunnableAB;

    public void BloccaTimerVID() {
        VariabiliStaticheVideo.getInstance().setSecondiAlpha(0);
        if (handlerAvanzamentoBarra != null) {
            handlerAvanzamentoBarra.removeCallbacks(updateRunnableAB);
            updateRunnableAB = null;
        }
    }

    public void AttivaTimerBarraVID() {
        BloccaTimerVID();

        handlerAvanzamentoBarra = new Handler();
        updateRunnableAB = new Runnable() {
            @Override
            public void run() {
                if (VariabiliStatichePazzia.getInstance().getVideoView().isPlaying()) {
                    int currentPosition = VariabiliStatichePazzia.getInstance().getVideoView().getCurrentPosition();
                    VariabiliStatichePazzia.getInstance().getSeekBar().setProgress(currentPosition);
                }

                handlerAvanzamentoBarra.postDelayed(this, 500);
            }
        };
        handlerAvanzamentoBarra.postDelayed(updateRunnableAB, 0);
    }

    public void AggiornaCategorie(Context context, Spinner spinner,
                                  String[] lista, String daDove) {
        String RigaSelezionata = "";

        switch (daDove) {
            case "PENNETTA":
                RigaSelezionata = VariabiliStatichePazzia.getInstance().getCategoriaPennetta();
                break;
            case "IMMAGINI":
                RigaSelezionata = VariabiliStatichePazzia.getInstance().getCategoriaImmagini();
                break;
            case "VIDEO":
                RigaSelezionata = VariabiliStatichePazzia.getInstance().getCategoriaVideo();
                break;
        }

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spinner,
                lista,
                RigaSelezionata
        );

        /* int indiceSelezionato = 0;
        int i = 0;
        for (String s: lista) {
            if (s.toUpperCase().trim().equals(RigaSelezionata.toUpperCase().trim())) {
                indiceSelezionato = i;
                break;
            }
            i++;
        }
        if (indiceSelezionato >= 0) {
            spinner.setSelection(indiceSelezionato);
        } */

        final boolean[] primoIngresso = {true};

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                try {
                    if (adapter.getItemAtPosition(pos) == null) {
                        return;
                    }
                    if (primoIngresso[0]) {
                        primoIngresso[0] = false;
                        return;
                    }

                    String NuovaCategoria = (String) adapter.getItemAtPosition(pos);

                    switch (daDove) {
                        case "PENNETTA":
                            VariabiliStatichePazzia.getInstance().setCategoriaPennetta(NuovaCategoria);
                            CambiaImmaginePennetta(context);
                            break;
                        case "IMMAGINI":
                            VariabiliStatichePazzia.getInstance().setCategoriaImmagini(NuovaCategoria);
                            CambiaImmagineImmagine(context);
                            break;
                        case "VIDEO":
                            VariabiliStatichePazzia.getInstance().setCategoriaVideo(NuovaCategoria);
                            CambiaVideo(context);
                            break;
                    }

                    db_dati_pazzia db = new db_dati_pazzia(context);
                    db.SalvaImpostazioni();
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }
}
