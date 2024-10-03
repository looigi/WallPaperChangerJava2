package com.looigi.wallpaperchanger2.classiPlayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classiDetector.Impostazioni;
import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classiPlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classiPlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class UtilityPlayer {
    private static final String NomeMaschera = "UTILITYPLAYER";
    private static UtilityPlayer instance = null;
    private int quantiCaricamenti = 0;
    private boolean spentoTesto = true;
    private int SecondiPassati = 0;
    private Runnable runTimer;
    private Handler handlerTimer;
    private int SecondiPassatiCambioImmagine;

    private UtilityPlayer() {
    }

    public static UtilityPlayer getInstance() {
        if (instance == null) {
            instance = new UtilityPlayer();
        }

        return instance;
    }

    public String ConverteNome(String Stringa) {
        String sStringa = Stringa;

        sStringa = sStringa.replace("&", "***AND***");
        sStringa = sStringa.replace("?", "***PI***");
        sStringa = sStringa.replace("/", "***BS***");
        sStringa = sStringa.replace("\\", "***BD***");
        sStringa = sStringa.replace("%", "***PERC***");

        return sStringa;
    }

    public int GeneraNumeroRandom(int NumeroMassimo) {
        if (NumeroMassimo > 0) {
            final int random = new Random().nextInt(NumeroMassimo);

            return random;
        } else {
            return -1;
        }
    }

    public void PressionePlay(Context context) {
        Bitmap bmpStart;

        if (!VariabiliStatichePlayer.getInstance().isStaSuonando()) {
            VariabiliStatichePlayer.getInstance().getMp().start();
            bmpStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.play);
            FaiRipartireTimer();
            VariabiliStatichePlayer.getInstance().setStaSuonando(true);
        } else {
            VariabiliStatichePlayer.getInstance().getMp().pause();
            bmpStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.pausa);
            FermaTimer();
            VariabiliStatichePlayer.getInstance().setStaSuonando(false);
        }

        VariabiliStatichePlayer.getInstance().getImgPlayStop().setImageBitmap(bmpStart);
        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
            AggiornaInformazioni(
                    VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista() + " " +
                    VariabiliStatichePlayer.getInstance().getUltimoBrano().getBrano());
        } else {
            AggiornaInformazioni("");
        }
    }

    public void ImpostaPosizioneBrano(int Posizione) {
        if (VariabiliStatichePlayer.getInstance().getMp() != null) {
            VariabiliStatichePlayer.getInstance().getMp().seekTo(Posizione * 1000);
            SecondiPassati = Posizione;
        }
    }

    public void CaricaBranoNelLettore(Context context) {
        StrutturaBrano sb = VariabiliStatichePlayer.getInstance().getUltimoBrano();
        if (sb != null) {
            String path = sb.getPathBrano();

            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Eseguo il brano: " + path);

            if (VariabiliStatichePlayer.getInstance().getMp() == null) {
                VariabiliStatichePlayer.getInstance().setMp(new MediaPlayer());
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Instanzio MP");
            } else {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Rilascio il vecchio brano");
                try {
                    if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                        VariabiliStatichePlayer.getInstance().getMp().stop();
                    }
                    VariabiliStatichePlayer.getInstance().getMp().release();
                    VariabiliStatichePlayer.getInstance().setMp(null);
                    VariabiliStatichePlayer.getInstance().setMp(new MediaPlayer());
                } catch (Exception ignored) {

                }
            }

            DurataBrano(context, sb);

            // Imposto termine brano automatico
            VariabiliStatichePlayer.getInstance().getMp().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Terminato il brano. Skippo da Main");

                        UtilityPlayer.getInstance().StoppaTimer();

                        ChiamateWsPlayer ws = new ChiamateWsPlayer(context);
                        ws.RitornaBranoDaID("");
                    }
                }
            });
            // Imposto termine brano automatico

            try {
                VariabiliStatichePlayer.getInstance().getMp().setDataSource(path);
                VariabiliStatichePlayer.getInstance().getMp().prepare();
                if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                    VariabiliStatichePlayer.getInstance().setFermaTimer(false);
                    VariabiliStatichePlayer.getInstance().getMp().start();
                } else {
                    VariabiliStatichePlayer.getInstance().setFermaTimer(true);
                }
                FaiPartireTimer(context);

                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Brano caricato");
            } catch (IOException e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Errore caricamento brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }

            ImpostaImmagine(context);

            AggiornaInformazioni(sb.getArtista() + " - " + sb.getBrano());
        } else {
            GestioneNotifichePlayer.getInstance().AggiornaNotifica("Brano non caricato");
        }
    }

    private String ConverteSecondiInTempo(int SecondiPassati) {
        int Secondi = SecondiPassati;
        int Minuti = 0;
        while (Secondi > 59) {
            Secondi -= 60;
            Minuti++;
        }
        String m = Integer.toString(Minuti);
        String s = Integer.toString(Secondi);
        if (m.length() == 1) {
            m = "0" + m;
        }
        ;
        if (s.length() == 1) {
            s = "0" + s;
        }
        ;

        return m + ":" + s;
    }

    private boolean DurataBrano(Context context, StrutturaBrano sb) {
        if (sb == null) {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Acquisizione durata brano: NON Possibile. Struttura non presente");

            VariabiliStatichePlayer.getInstance().getSeekBarBrano().setMax(0);
            VariabiliStatichePlayer.getInstance().getSeekBarBrano().setProgress(0);
            VariabiliStatichePlayer.getInstance().getSeekBarBrano().setVisibility(LinearLayout.GONE);
            VariabiliStatichePlayer.getInstance().getTxtInizio().setText("00:00");
            VariabiliStatichePlayer.getInstance().getTxtInizio().setVisibility(LinearLayout.GONE);
            VariabiliStatichePlayer.getInstance().getTxtFine().setText("00:00");
            VariabiliStatichePlayer.getInstance().getTxtFine().setVisibility(LinearLayout.GONE);

            VariabiliStatichePlayer.getInstance().setInizioMinuti("00:00");
            VariabiliStatichePlayer.getInstance().setFineMinuti("00:00");

            VariabiliStatichePlayer.getInstance().setDurataBranoInSecondi(-1);

            return false;
        } else {
            UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Acquisizione durata brano: " + sb.getPathBrano());

            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(sb.getPathBrano());
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int timeInmillisec = Integer.parseInt(time);
                int duration = timeInmillisec / 1000;

                VariabiliStatichePlayer.getInstance().setDurataBranoInSecondi(duration);
                VariabiliStatichePlayer.getInstance().getSeekBarBrano().setMax(VariabiliStatichePlayer.getInstance().getDurataBranoInSecondi());
                VariabiliStatichePlayer.getInstance().getSeekBarBrano().setProgress(0);
                VariabiliStatichePlayer.getInstance().getTxtInizio().setText("00:00");
                VariabiliStatichePlayer.getInstance().getTxtFine().setText(ConverteSecondiInTempo(VariabiliStatichePlayer.getInstance().getDurataBranoInSecondi()));

                VariabiliStatichePlayer.getInstance().setInizioMinuti("00:00");
                VariabiliStatichePlayer.getInstance().setFineMinuti(ConverteSecondiInTempo(VariabiliStatichePlayer.getInstance().getDurataBranoInSecondi()));

                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Acquisita durata brano: " + Long.toString(duration));
                VariabiliStatichePlayer.getInstance().getSeekBarBrano().setVisibility(LinearLayout.VISIBLE);
                VariabiliStatichePlayer.getInstance().getTxtInizio().setVisibility(LinearLayout.VISIBLE);
                VariabiliStatichePlayer.getInstance().getTxtFine().setVisibility(LinearLayout.VISIBLE);

                return true;
            } catch (Exception e) {
                UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Errore acquisizione durata brano: " + e.getMessage());

                VariabiliStatichePlayer.getInstance().getSeekBarBrano().setMax(0);
                VariabiliStatichePlayer.getInstance().getSeekBarBrano().setProgress(0);
                VariabiliStatichePlayer.getInstance().getSeekBarBrano().setVisibility(LinearLayout.GONE);
                VariabiliStatichePlayer.getInstance().getTxtInizio().setText("00:00");
                VariabiliStatichePlayer.getInstance().getTxtInizio().setVisibility(LinearLayout.GONE);
                VariabiliStatichePlayer.getInstance().getTxtFine().setText("00:00");
                VariabiliStatichePlayer.getInstance().getTxtFine().setVisibility(LinearLayout.GONE);

                VariabiliStatichePlayer.getInstance().setDurataBranoInSecondi(-1);

                return false;
            }
        }
    }

    public void Attesa(boolean Acceso) {
        if (Acceso) {
            if (quantiCaricamenti == 0) {
                VariabiliStatichePlayer.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
            }
            quantiCaricamenti++;
        } else {
            quantiCaricamenti--;
            if (quantiCaricamenti < 1) {
                quantiCaricamenti = 0;
                VariabiliStatichePlayer.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
            }
        }
    }

    public void AggiornaOperazioneInCorso(String Operazione) {
        if (Operazione.isEmpty()) {
            if (!spentoTesto) {
                VariabiliStatichePlayer.getInstance().getTxtOperazione().setVisibility(LinearLayout.GONE);
            }
            spentoTesto = true;
            VariabiliStatichePlayer.getInstance().getTxtOperazione().setText(Operazione);
        } else {
            if (spentoTesto) {
                VariabiliStatichePlayer.getInstance().getTxtOperazione().setVisibility(LinearLayout.VISIBLE);
            }
            spentoTesto = false;
            VariabiliStatichePlayer.getInstance().getTxtOperazione().setText(Operazione);
        }
    }

    public void AggiornaInformazioni(String Brano) {
        VariabiliStatichePlayer.getInstance().getTxtTitolo().setText(Brano);
        GestioneNotifichePlayer.getInstance().AggiornaNotifica(Brano);
    }

    public void ImpostaImmagine(Context context) {
        if (!VariabiliStatichePlayer.getInstance().isMascheraNascosta() &&
                VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
                if (VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini() != null) {
                    List<StrutturaImmagini> lista = VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini();
                    int immagine = GeneraNumeroRandom(lista.size());

                    String path = lista.get(immagine).getUrlImmagine();
                    String PathImmagine = path.replace(VariabiliStatichePlayer.PercorsoBranoMP3SuURL + "/", "");
                    PathImmagine = context.getFilesDir() + "/Player/" + PathImmagine;
                    PathImmagine = PathImmagine.replace("\\", "/");

                    if (Files.getInstance().EsisteFile(PathImmagine)) {
                        Bitmap bitmap = BitmapFactory.decodeFile(PathImmagine);
                        VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(PathImmagine);
                        VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(bitmap);

                        AggiornaInformazioni(VariabiliStatichePlayer.getInstance().getUltimoBrano().getBrano());
                    } else {
                        new DownloadImage(
                                context,
                                VariabiliStatichePlayer.getInstance().getImgBrano(),
                                lista.get(immagine).getUrlImmagine()).execute(
                                lista.get(immagine).getUrlImmagine()
                        );
                    }
                } else {
                    VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(null);
                }
            } else {
                VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(null);
            }
        }
    }

    public void StoppaTimer() {
        if (handlerTimer != null) {
            handlerTimer.removeCallbacks(runTimer);
            runTimer = null;
        }
    }

    public void FaiPartireTimer(Context context) {
        UtilityDetector.getInstance().ScriveLog(context, NomeMaschera, "Fatto Partire Timer");

        SecondiPassati = 0;
        VariabiliStatichePlayer.getInstance().setSecondiPassati(SecondiPassati);
        SecondiPassatiCambioImmagine = 0;

        if (handlerTimer != null) {
            handlerTimer.removeCallbacks(runTimer);
            runTimer = null;
        }

        handlerTimer = new Handler(Looper.getMainLooper());
        handlerTimer.postDelayed(runTimer = new Runnable() {
            @Override
            public void run() {
                if (!VariabiliStatichePlayer.getInstance().isFermaTimer()) {
                    SecondiPassati++;

                    VariabiliStatichePlayer.getInstance().getSeekBarBrano().setProgress(SecondiPassati);
                    VariabiliStatichePlayer.getInstance().setSecondiPassati(SecondiPassati);
                    String min = ConverteSecondiInTempo(SecondiPassati);
                    VariabiliStatichePlayer.getInstance().getTxtInizio().setText(min);
                    VariabiliStatichePlayer.getInstance().setInizioMinuti(min);

                    SecondiPassatiCambioImmagine++;
                    if (SecondiPassatiCambioImmagine >= VariabiliStatichePlayer.SecondiCambioImmagine) {
                        SecondiPassatiCambioImmagine = 0;

                        ImpostaImmagine(context);
                    }
                }

                handlerTimer.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public void FaiRipartireTimer() {
        VariabiliStatichePlayer.getInstance().setFermaTimer(false);
    }

    public void FermaTimer() {
        VariabiliStatichePlayer.getInstance().setFermaTimer(true);
    }
}
