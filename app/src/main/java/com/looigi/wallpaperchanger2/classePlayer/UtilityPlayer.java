package com.looigi.wallpaperchanger2.classePlayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaFiltroBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
// import com.looigi.wallpaperchanger2.classiPlayer.WebServices.RipristinoChiamate;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.DownloadImmagine;
import com.looigi.wallpaperchanger2.utilities.LogInterno;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class UtilityPlayer {
    private static final String NomeMaschera = "Utility_Player";
    private static UtilityPlayer instance = null;
    private int quantiCaricamenti = 0;
    private int SecondiPassati = 0;
    private Runnable runTimer;
    private Handler handlerTimer;
    private Runnable runTimerChiusura;
    private Handler handlerTimerChiusura;
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

    /* public boolean isBluetoothHeadsetConnected() {
        Context context = UtilitiesGlobali.getInstance().tornaContextValido();
        BluetoothManager manager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        List<BluetoothDevice> connected = manager.getConnectedDevices(GATT);
        for (BluetoothDevice b : connected) {
            int tipo = b.getType();
            String nome = b.getName();
            int connesso = b.getBondState();
            if (nome.equals("ppp")) {

            }
        }
        // Log.i("Connected Devices: ", connected.size()+"");
        return true;
    } */

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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("PLAYER", Maschera,  Log);
            // }
        } else {

        }
    }

    public void PressionePlay(Context context, boolean Acceso) {
        if (VariabiliStatichePlayer.getInstance().getMp() != null) {
            Bitmap bmpStart;

            if (Acceso) {
                VariabiliStatichePlayer.getInstance().getMp().start();
                bmpStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_pausa);
                FaiRipartireTimer();
                VariabiliStatichePlayer.getInstance().setStaSuonando(true);
            } else {
                VariabiliStatichePlayer.getInstance().getMp().pause();
                bmpStart = BitmapFactory.decodeResource(context.getResources(), R.drawable.icona_suona);
                FermaTimer();
                VariabiliStatichePlayer.getInstance().setStaSuonando(false);
            }

            VariabiliStatichePlayer.getInstance().getImgPlayStop().setImageBitmap(bmpStart);
            if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
                AggiornaInformazioni(false);
            } else {
                AggiornaInformazioni(true);
            }
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

            ImpostaBellezza();

            ScriveLog(context, NomeMaschera, "Eseguo il brano: " + path);

            if (VariabiliStatichePlayer.getInstance().getMp() == null) {
                VariabiliStatichePlayer.getInstance().setMp(new MediaPlayer());
                ScriveLog(context, NomeMaschera, "Instanzio MP");
            } else {
                ScriveLog(context, NomeMaschera, "Rilascio il vecchio brano");
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
                        ScriveLog(context, NomeMaschera, "Terminato il brano. Skippo da Main");

                        StoppaTimer();

                        BranoAvanti(context, "", false, false);
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

                VariabiliStatichePlayer.getInstance().setStaCaricandoBranoPregresso(false);
                VariabiliStatichePlayer.getInstance().setStrutturaBranoPregressoCaricata(null);
                VariabiliStatichePlayer.getInstance().setHaCaricatoBranoPregresso(false);

                db_dati_player db = new db_dati_player(context);
                db.ScriveUltimoBranoAscoltato(sb);

                String Immagine = UtilityPlayer.getInstance().PrendeImmagineArtistaACaso(
                        context, sb.getArtista());

                Bitmap bitmap;
                if (Immagine.isEmpty()) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                } else {
                    bitmap = BitmapFactory.decodeFile(Immagine);
                    VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(Immagine);
                }
                VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(bitmap);

                AggiungeBranoAllaListaAscoltati(context, sb);

                FaiPartireTimer(context);

                ScriveLog(context, NomeMaschera, "Brano caricato");
            } catch (IOException e) {
                ScriveLog(context, NomeMaschera, "Errore caricamento brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }

            ImpostaImmagine(context);

            AggiornaInformazioni(false);

            GestioneNotifichePlayer.getInstance().AggiornaNotifica(sb.getBrano());

            if (sb.getBellezza() == -2) {
                ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                VariabiliStatichePlayer.getInstance().setClasseChiamata(ws);
                ws.RitornaStelleBrano();
            }
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
            ScriveLog(context, NomeMaschera, "Acquisizione durata brano: NON Possibile. Struttura non presente");

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
            ScriveLog(context, NomeMaschera, "Acquisizione durata brano: " + sb.getPathBrano());

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

                ScriveLog(context, NomeMaschera, "Acquisita durata brano: " + Long.toString(duration));
                VariabiliStatichePlayer.getInstance().getSeekBarBrano().setVisibility(LinearLayout.VISIBLE);
                VariabiliStatichePlayer.getInstance().getTxtInizio().setVisibility(LinearLayout.VISIBLE);
                VariabiliStatichePlayer.getInstance().getTxtFine().setVisibility(LinearLayout.VISIBLE);

                return true;
            } catch (Exception e) {
                ScriveLog(context, NomeMaschera, "Errore acquisizione durata brano: " + e.getMessage());

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
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
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
        };
        handlerTimer.postDelayed(rTimer, 50);
    }

    public void AggiornaOperazioneInCorso(String Operazione) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (Operazione.isEmpty()) {
                    VariabiliStatichePlayer.getInstance().getTxtOperazione().setVisibility(LinearLayout.GONE);

                    VariabiliStatichePlayer.getInstance().getTxtOperazione().setText(Operazione);
                } else {
                    VariabiliStatichePlayer.getInstance().getTxtOperazione().setVisibility(LinearLayout.VISIBLE);

                    VariabiliStatichePlayer.getInstance().getTxtOperazione().setText(Operazione);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 50);
    }

    public void AggiornaInformazioni(boolean Elimina) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                if (Elimina) {
                    VariabiliStatichePlayer.getInstance().getTxtTitolo().setText("");
                    GestioneNotifichePlayer.getInstance().AggiornaNotifica("");
                } else {
                    String Brano = VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista() + ": " +
                            VariabiliStatichePlayer.getInstance().getUltimoBrano().getBrano();
                    VariabiliStatichePlayer.getInstance().getTxtTitolo().setText(Brano);
                    GestioneNotifichePlayer.getInstance().AggiornaNotifica(Brano);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 50);
    }

    public void ResettaCampi(Context context) {
        Attesa(false);
        AggiornaOperazioneInCorso("");

        StoppaTimer();

        ImpostaLogoApplicazione(context);

        if (VariabiliStatichePlayer.getInstance().getClasseChiamata() != null) {
            VariabiliStatichePlayer.getInstance().getClasseChiamata().StoppaEsecuzione();
        }
        if (VariabiliStatichePlayer.getInstance().getDownCanzone() != null) {
            VariabiliStatichePlayer.getInstance().getDownCanzone().BloccaEsecuzione();
        }
        if (VariabiliStatichePlayer.getInstance().getDownImmagine() != null) {
            VariabiliStatichePlayer.getInstance().getDownImmagine().BloccaEsecuzione();
        }
    }

    public void IndietroBrano(Context context) {
        if (VariabiliStatichePlayer.getInstance().getIdBraniAscoltati().size() > 1) {
            int idBrano = VariabiliStatichePlayer.getInstance().getIdBraniAscoltati().get(
                    VariabiliStatichePlayer.getInstance().getIdBraniAscoltati().size() - 2
            );
            VariabiliStatichePlayer.getInstance().getIdBraniAscoltati().remove(
                    VariabiliStatichePlayer.getInstance().getIdBraniAscoltati().size() - 1
            );

            ScriveLog(context, NomeMaschera, "Indietro Brano: " + idBrano);

            ResettaCampi(context);

            PrendeBranoInLocaleEsatto(context, String.valueOf(idBrano));
        }
    }

    public void BranoAvanti(Context context, String Brano, boolean Pregresso, boolean VieneDaTasto) {
        db_dati_player db = new db_dati_player(context);

        if (VariabiliStatichePlayer.getInstance().isHaCaricatoBranoPregresso() &&
                VariabiliStatichePlayer.getInstance().getStrutturaBranoPregressoCaricata() != null) {
            // C'è un brano pregresso. Devo impostarlo
            StrutturaBrano sb = VariabiliStatichePlayer.getInstance().getStrutturaBranoPregressoCaricata();

            StoppaTimer();

            ScriveLog(context, NomeMaschera, "Avanzo Brano. Brano pregresso: " + sb.getBrano());

            db.ScriveBrano(sb);
            db.ScriveUltimoBranoAscoltato(sb);

            VariabiliStatichePlayer.getInstance().setUltimoBrano(sb);
            VariabiliStatichePlayer.getInstance().getTxtBranoPregresso().setText("");
            VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.GONE);

            CaricaBranoNelLettore(context);

            return;
        }

        ScriveLog(context, NomeMaschera, "Avanzo Brano. Brano esatto: " + Brano + ". Pregresso: " + Pregresso);

        ResettaCampi(context);

        // VariabiliStatichePlayer.getInstance().setChiamate(new ArrayList<>());
        // RipristinoChiamate.getInstance().RimuoveTimer();

        if (Brano.isEmpty()) {
            boolean cercaBranoInLocale = false;

            int quantiBrani = db.QuantiBraniInArchivio();
            int random = UtilityPlayer.getInstance().GeneraNumeroRandom(6);
            if (random == 1 || random == 3 || (VieneDaTasto && quantiBrani > 0)) {
                cercaBranoInLocale = true;
            }
            if (!cercaBranoInLocale) {
                if (!UtilitiesGlobali.getInstance().isRetePresente()) {
                    cercaBranoInLocale = true;
                }
            }

            if (cercaBranoInLocale) {
                if (quantiBrani == 0) {
                    cercaBranoInLocale = false;
                }
            }

            if (!cercaBranoInLocale) {
                boolean wifi = VariabiliStaticheStart.getInstance().isCeWifi();
                int livello = VariabiliStaticheStart.getInstance().getLivelloSegnaleConnessione();
                int level = VariabiliStaticheStart.getInstance().getLivello();
                String tipo = VariabiliStaticheStart.getInstance().getTipoConnessione();

                ScriveLog(context, NomeMaschera, "Avanzo Brano. WiFi: " + wifi);
                ScriveLog(context, NomeMaschera, "Avanzo Brano. Livello Segnale: " + livello);
                ScriveLog(context, NomeMaschera, "Avanzo Brano. Livello: " + level);
                ScriveLog(context, NomeMaschera, "Avanzo Brano. Tipo: " + tipo);

                PrendeBranoInRete(context, Brano, Pregresso);
            } else {
                PrendeBranoInLocaleNonEsatto(context, Pregresso);
            }
        } else {
            PrendeBranoInLocaleEsatto(context, Brano);
        }
    }

    private void PrendeBranoInLocaleEsatto(Context context, String Brano) {
        // Imposto brano con id esatto
        ScriveLog(context, NomeMaschera, "Avanzo Brano. Brano " + Brano);

        db_dati_player db = new db_dati_player(context);
        StrutturaBrano sb = db.CaricaBrano(Brano);
        if (sb != null) {
            ScriveLog(context, NomeMaschera, "Avanzo Brano con id Esatto. Preso brano " + sb.getBrano() + ".");

            VariabiliStatichePlayer.getInstance().setUltimoBrano(sb);

            CaricaBranoNelLettore(context);

            VariabiliStatichePlayer.getInstance().getTxtBranoPregresso().setText("");
            VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.GONE);
        } else {
            ScriveLog(context, NomeMaschera, "Avanzo Brano. Brano non valido");
            UtilitiesGlobali.getInstance().ApreToast(context, "Impossibile rilevare brano " + Brano);
        }
    }

    public void PrendeBranoInLocaleNonEsatto(Context context, boolean Pregresso) {
        ScriveLog(context, NomeMaschera, "Avanzo Brano. Rete non valida. Prendo in locale");

        db_dati_player db = new db_dati_player(context);
        int max = db.QuantiBraniInArchivio();
        ScriveLog(context, NomeMaschera, "Avanzo Brano. Quanti brani: " + max);

        boolean ancora = true;
        int c = 0;
        boolean ok = false;

        while (ancora) {
            int numeroRigaBrano = 0;
            if (VariabiliStatichePlayer.getInstance().isRandom()) {
                numeroRigaBrano = GeneraNumeroRandom(max);
            } else {
                numeroRigaBrano = VariabiliStatichePlayer.getInstance().getIdUltimoBrano();
                numeroRigaBrano++;
                if (numeroRigaBrano > max) {
                    numeroRigaBrano = 0;
                }
            }

            int idBrano = db.PrendeBranoDaNumeroRiga(numeroRigaBrano);
            VariabiliStatichePlayer.getInstance().setIdUltimoBrano(idBrano);

            ScriveLog(context, NomeMaschera, "Avanzo Brano. Brano " + idBrano);
            StrutturaBrano sb = db.CaricaBrano(Integer.toString(idBrano));
            if (sb != null) {
                ScriveLog(context, NomeMaschera, "Avanzo Brano. Preso brano " + sb.getBrano() + ". Pregresso: " + Pregresso);

                ancora = false;
                if (Pregresso) {
                    VariabiliStatichePlayer.getInstance().setHaCaricatoBranoPregresso(true);
                    VariabiliStatichePlayer.getInstance().setStrutturaBranoPregressoCaricata(sb);

                    VariabiliStatichePlayer.getInstance().getTxtBranoPregresso().setText(sb.getArtista() + ":" + sb.getBrano());
                    VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.VISIBLE);
                } else {
                    VariabiliStatichePlayer.getInstance().setUltimoBrano(sb);

                    CaricaBranoNelLettore(context);

                    VariabiliStatichePlayer.getInstance().getTxtBranoPregresso().setText("");
                    VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.GONE);
                }

                ok = true;
            } else {
                ScriveLog(context, NomeMaschera, "Avanzo Brano. Brano non valido. Riprovo: " + c);

                c++;
                if (c > 5) {
                    ancora = false;
                }
            }
        }

        if (!ok) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Impossibile rilevare brano");
        }
    }

    private void PrendeBranoInRete(Context context, String Brano, boolean Pregresso) {
        ScriveLog(context, NomeMaschera, "Avanzo Brano. Scarico Brano");

        if (VariabiliStatichePlayer.getInstance().getClasseChiamata() != null) {
            VariabiliStatichePlayer.getInstance().getClasseChiamata().StoppaEsecuzione();
        }
        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
        VariabiliStatichePlayer.getInstance().setClasseChiamata(ws);
        ws.RitornaBranoDaID("", Pregresso);
    }

    public void ImpostaImmagine(Context context) {
        if (!VariabiliStatichePlayer.getInstance().isMascheraNascosta() &&
                VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
                if (VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini() != null) {
                    List<StrutturaImmagini> lista = VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini();
                    if (lista.isEmpty()) {
                        db_dati_player db = new db_dati_player(context);
                        lista = db. CaricaImmaginiBrano(VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista());
                        if (lista.isEmpty()) {
                            ChiamateWsPlayer c = new ChiamateWsPlayer(context, false);
                            c.RitornaImmaginiArtista(VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista());
                            return;
                        }
                    }

                    final int[] immagine = {GeneraNumeroRandom(lista.size() - 1)};
                    if (immagine[0] > -1) {
                        Handler handlerTimer = new Handler(Looper.getMainLooper());
                        List<StrutturaImmagini> finalLista = lista;
                        Runnable rTimer = new Runnable() {
                            public void run() {
                                boolean ancora = true;

                                while (ancora) {
                                    String path = finalLista.get(immagine[0]).getUrlImmagine();

                                    String PathImmagine = "";
                                    if (path.toUpperCase().contains("HTTP://")) {
                                        PathImmagine = path.replace(VariabiliStatichePlayer.PercorsoBranoMP3SuURL + "/", "");
                                        PathImmagine = context.getFilesDir() + "/Player/" + PathImmagine;
                                        PathImmagine = PathImmagine.replace("\\", "/");
                                    } else {
                                        PathImmagine = path;
                                    }

                                    if (Files.getInstance().EsisteFile(PathImmagine)) {
                                        Bitmap bitmap = BitmapFactory.decodeFile(PathImmagine);
                                        if (bitmap != null) {
                                            if (bitmap.getWidth() > 100 && bitmap.getHeight() > 100) {
                                                ancora = false;

                                                VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(PathImmagine);
                                                VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(bitmap);
                                                VariabiliStatichePlayer.getInstance().setImmagineImpostata(finalLista.get(immagine[0]));
                                                VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(immagine[0]);
                                                VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(finalLista.get(immagine[0]));

                                                if (VariabiliStatichePlayer.getInstance().getImgSfondoSettings() != null) {
                                                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmap);
                                                }
                                                if (VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine() != null) {
                                                    VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine().setText("Immagine " + immagine[0] + "/" + (finalLista.size() - 1));
                                                }

                                                AggiornaInformazioni(false);
                                            } else {
                                                Files.getInstance().EliminaFileUnico(PathImmagine);

                                                VariabiliStatichePlayer.getInstance().setImmagineImpostata(null);

                                                db_dati_player db = new db_dati_player(context);
                                                db.EliminaImmagineFisica(
                                                        finalLista.get(immagine[0]).getAlbum(),
                                                        finalLista.get(immagine[0]).getNomeImmagine());

                                                VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(null);
                                                // VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(0);
                                                // if (VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine() != null) {
                                                //     VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine().setText("Immagine 0/" + (finalLista.size() - 1));
                                                // }
                                                if (VariabiliStatichePlayer.getInstance().getImgSfondoSettings() != null) {
                                                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(null);
                                                }
                                            }
                                        }
                                    } else {
                                        boolean ok = true;
                                        int level = VariabiliStaticheStart.getInstance().getLivello();

                                        if (!UtilitiesGlobali.getInstance().isRetePresente()) {
                                            ok = false;
                                            ImpostaLogoApplicazione(context);
                                            VariabiliStatichePlayer.getInstance().setImmagineImpostata(null);
                                            ancora = false;
                                        }
                                        if (ok) {
                                            ancora = false;

                                            VariabiliStatichePlayer.getInstance().setImmagineImpostata(finalLista.get(immagine[0]));
                                            VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(finalLista.get(immagine[0]));
                                            VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(immagine[0]);

                                            DownloadImmagine d = new DownloadImmagine();
                                            VariabiliStatichePlayer.getInstance().setDownImmagine(d);
                                            d. EsegueDownload(
                                                    context,
                                                    VariabiliStatichePlayer.getInstance().getImgBrano(),
                                                    finalLista.get(immagine[0]).getUrlImmagine()
                                            );
                                    /* new DownloadImage(
                                            context,
                                            VariabiliStatichePlayer.getInstance().getImgBrano(),
                                            lista.get(immagine).getUrlImmagine()).execute(
                                            lista.get(immagine).getUrlImmagine()
                                    ); */
                                        }
                                    }

                                    immagine[0]++;
                                    if (immagine[0] > finalLista.size() - 1) {
                                        immagine[0] = 0;
                                    }
                                }
                            }
                        };
                        handlerTimer.postDelayed(rTimer, 1000);
                    } else {
                        VariabiliStatichePlayer.getInstance().setImmagineImpostata(null);
                        ImpostaLogoApplicazione(context);
                    }
                } else {
                    VariabiliStatichePlayer.getInstance().setImmagineImpostata(null);
                    ImpostaLogoApplicazione(context);
                }
            } else {
                VariabiliStatichePlayer.getInstance().setImmagineImpostata(null);
                ImpostaLogoApplicazione(context);
            }
        }
    }

    public void ImpostaLogoApplicazione(Context context) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(bitmap);
                // VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(-1);
                if (VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine() != null) {
                    VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine().setText("Immagine " +
                            VariabiliStatichePlayer.getInstance().getIdImmagineImpostata() +
                            "/" + (VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size() - 1));
                }
                if (VariabiliStatichePlayer.getInstance().getImgSfondoSettings() != null) {
                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmap);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 1000);
    }

    public void FaiPartireTimerChiusura(Context context) {
        if (handlerTimerChiusura != null) {
            handlerTimerChiusura.removeCallbacks(runTimerChiusura);
            runTimerChiusura = null;
        }

        int secondiDiAttesa = 60;
        int minutiPrimaDellaChiusuraAutomatica = 5;

        handlerTimerChiusura = new Handler(Looper.getMainLooper());
        handlerTimerChiusura.postDelayed(runTimerChiusura = new Runnable() {
            @Override
            public void run() {
                long ora = new Date().getTime();
                long diff = ora - VariabiliStatichePlayer.getInstance().getUltimaOperazioneTS();
                if (diff >= ((secondiDiAttesa * 1000) * minutiPrimaDellaChiusuraAutomatica)) {
                    if (!VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                        ChiudePlayer(context);
                    }
                } else {
                    handlerTimerChiusura.postDelayed(this, (secondiDiAttesa * 1000));
                }
            }
        }, (secondiDiAttesa * 1000));
    }

    public void StoppaTimerChiusura() {
        if (handlerTimerChiusura != null) {
            handlerTimerChiusura.removeCallbacks(runTimerChiusura);
            runTimerChiusura = null;
        }
    }

    private void ChiudePlayer(Context context) {
        PressionePlay(context, false);

        StoppaTimerChiusura();
        StoppaTimer();

        GestioneNotifichePlayer.getInstance().RimuoviNotifica();

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                VariabiliStatichePlayer.getInstance().ChiudeActivity(true);
            }
        };
        handlerTimer.postDelayed(rTimer, 1000);
    }

    public String PrendeImmagineArtistaACaso(Context context, String Artista) {
        String Path = context.getFilesDir() + "/Player/ImmaginiMusica/";
        String PathImmagini = Path + Artista + "/ZZZ-ImmaginiArtista";
        File root = new File(PathImmagini);
        File[] list = root.listFiles();

        if (list == null) {
            return "";
        }

        List<String> Nomi = new ArrayList<>();
        for (File f : list) {
            if (f.isDirectory()) {
            } else {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                Nomi.add(Filetto);
            }
        }

        int n = UtilityPlayer.getInstance().GeneraNumeroRandom(Nomi.size() - 1);

        return Nomi.get(n);
    }

    public void StoppaTimer() {
        if (handlerTimer != null) {
            handlerTimer.removeCallbacks(runTimer);
            runTimer = null;
        }
    }

    public void FaiPartireTimer(Context context) {
        ScriveLog(context, NomeMaschera, "Fatto Partire Timer");

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
                    if (SecondiPassatiCambioImmagine >= VariabiliStatichePlayer.getInstance().getTempoCambioImmagine()) {
                        SecondiPassatiCambioImmagine = 0;

                        if (VariabiliStatichePlayer.getInstance().isCambiaImmagine()) {
                            ImpostaImmagine(context);
                        }
                    }

                    if (SecondiPassati > VariabiliStatichePlayer.SecondiBranoPregresso &&
                            !VariabiliStatichePlayer.getInstance().isStaCaricandoBranoPregresso() &&
                        !VariabiliStatichePlayer.getInstance().isHaCaricatoBranoPregresso()) {
                        VariabiliStatichePlayer.getInstance().setStaCaricandoBranoPregresso(true);

                        BranoAvanti(context, "", true, false);
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

    public void ScriveBranoPregresso() {
        StrutturaBrano bp = VariabiliStatichePlayer.getInstance().getStrutturaBranoPregressoCaricata();
        VariabiliStatichePlayer.getInstance().getTxtBranoPregresso().setText("Prossimo: " + bp.getArtista() + " - " + bp.getBrano());
        VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.VISIBLE);
    }

    public void RicaricaPregresso() {
        Context context = UtilitiesGlobali.getInstance().tornaContextValido();
        if (context != null) {
            VariabiliStatichePlayer.getInstance().setStrutturaBranoPregressoCaricata(null);
            VariabiliStatichePlayer.getInstance().setStaCaricandoBranoPregresso(true);

            BranoAvanti(context, "", true, false);
        }
    }

    public void ChiudeActivity(boolean Finish) {
        if (VariabiliStatichePlayer.getInstance().getAct() != null) {
            // mainActivity.moveTaskToBack(true);
            // if (Finish) {
            VariabiliStatichePlayer.getInstance().getAct().finish();
            // }
        }
    }

    public void ImpostaBellezza() {
        for (int i = 0; i <= 10; i++) {
            VariabiliStatichePlayer.getInstance().getImgBellezza().get(i).setImageResource(R.drawable.preferito_vuoto);
        }

        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
            int bellezza = VariabiliStatichePlayer.getInstance().getUltimoBrano().getBellezza();
            if (bellezza > -1) {
                for (int i = 0; i <= bellezza; i++) {
                    VariabiliStatichePlayer.getInstance().getImgBellezza().get(i).setImageResource(R.drawable.preferito);
                }
                /* for (int i = bellezza + 1; i <= 10; i++) {
                    VariabiliStatichePlayer.getInstance().getImgBellezza().get(i).setImageResource(R.drawable.preferito_vuoto);
                } */
            }
        }
    }

    public StrutturaFiltroBrano CreaDatiFiltrobrani() {
        StrutturaFiltroBrano s = new StrutturaFiltroBrano();
        String Where = "";

        String Stelle = Integer.toString(VariabiliStatichePlayer.getInstance().getStelleDaRicercare());
        String StelleSuperiori = VariabiliStatichePlayer.getInstance().isStelleSuperiori() ? "S" : "N";
        String MaiAscoltata = VariabiliStatichePlayer.getInstance().isRicercaMaiAscoltata() ? "S" : "N";
        if (!VariabiliStatichePlayer.getInstance().isRicercaStelle()) {
            Stelle = "";
            StelleSuperiori = "";
        } else {
            if (MaiAscoltata.equals("S")) {
                Stelle = "0";
                StelleSuperiori = "";
            }
        }

        if (MaiAscoltata.equals("S")) {
            Where += "Cast(Bellezza As Integer) <= 0 And ";
        } else {
            if (!Stelle.isEmpty()) {
                if (StelleSuperiori.equals("S")) {
                    Where +="Cast(Bellezza As Integer) >= " + Stelle + " And ";
                } else {
                    Where +="Cast(Bellezza As Integer) = " + Stelle + " And ";
                }
            }
        }

        String Testo = VariabiliStatichePlayer.getInstance().getTestoDaRicercare();
        String TestoNon = VariabiliStatichePlayer.getInstance().getTestoDaNonRicercare();
        if (!VariabiliStatichePlayer.getInstance().isRicercaTesto()) {
            Testo = "";
            TestoNon = "";
        }

        if (!Testo.isEmpty()) {
            Where += "(";
            Where += "Artista Like '%" + Testo.toUpperCase().trim().replace("'", "''") + "%' Or ";
            Where += "Album Like '%" + Testo.toUpperCase().trim().replace("'", "''") + "%' Or ";
            Where += "Brano Like '%" + Testo.toUpperCase().trim().replace("'", "''") + "%'";
            // Where += "Testo Like '%" + Testo.toUpperCase().trim().replace("'", "''") + "%' Or ";
            Where += ") And ";
        }
        if (!TestoNon.isEmpty()) {
            Where += "(";
            Where += "Artista Not Like '%" + Testo.toUpperCase().trim().replace("'", "''") + "%' And ";
            Where += "Album Not Like '%" + Testo.toUpperCase().trim().replace("'", "''") + "%' And ";
            Where += "Brano Not Like '%" + Testo.toUpperCase().trim().replace("'", "''") + "%'";
            Where += ") And ";
            // Where += "Testo Not Like '%" + TestoNon.toUpperCase().trim().replace("'", "''") + "%' And ";
        }

        String Preferiti = VariabiliStatichePlayer.getInstance().getPreferiti();
        String PreferitiElimina = VariabiliStatichePlayer.getInstance().getPreferitiElimina();
        String AndOrPref = VariabiliStatichePlayer.getInstance().isAndOrPref() ? "S" : "N";
        if (!VariabiliStatichePlayer.getInstance().isRicercaPreferiti()) {
            Preferiti = "";
            PreferitiElimina = "";
            AndOrPref = "";
        }

        if (!Preferiti.isEmpty()) {
            String[] p;
            if (Preferiti.contains(";")) {
                p = Preferiti.split(";");
            } else {
                p = new String[]{Preferiti};
            }
            String ClausolaAndOr = "";
            if (AndOrPref.equals("S")) {
                ClausolaAndOr = "And";
            } else {
                ClausolaAndOr = "Or ";
            }
            String pWhere = "";
            for (String pp : p) {
                pWhere += "Artista = '" + pp.replace("'", "''") + "' " + ClausolaAndOr + " ";
            }
            if (!pWhere.isEmpty()) {
                pWhere = pWhere.substring(0, pWhere.length() - 4);
                Where += " (" + pWhere + ") And ";
            }
        }

        if (!PreferitiElimina.isEmpty()) {
            String[] p;
            if (PreferitiElimina.contains(";")) {
                p = PreferitiElimina.split(";");
            } else {
                p = new String[]{PreferitiElimina};
            }
            String pWhere = "";
            for (String pp : p) {
                pWhere += "Artista <> '" + pp.replace("'", "''") + "' And ";
            }
            if (!pWhere.isEmpty()) {
                pWhere = pWhere.substring(0, pWhere.length() - 4);
                Where += " (" + pWhere + ") And ";
            }
        }

        String Tags = VariabiliStatichePlayer.getInstance().getPreferitiTags();
        String TagsElimina = VariabiliStatichePlayer.getInstance().getPreferitiEliminaTags();
        String AndOrTags = VariabiliStatichePlayer.getInstance().isAndOrTags() ? "S" : "N";
        if (!VariabiliStatichePlayer.getInstance().isRicercaTags()) {
            Tags = "";
            TagsElimina = "";
            AndOrTags = "";
        }

        if (!Tags.isEmpty()) {
            String[] p;
            if (Tags.contains(";")) {
                p = Tags.split(";");
            } else {
                p = new String[]{Tags};
            }
            String ClausolaAndOr = "";
            if (AndOrTags.equals("S")) {
                ClausolaAndOr = "And";
            } else {
                ClausolaAndOr = "Or ";
            }
            String pWhere = "";
            for (String pp : p) {
                pWhere += "Artista = '" + pp.replace("'", "''") + "' " + ClausolaAndOr + " ";
            }
            if (!pWhere.isEmpty()) {
                pWhere = pWhere.substring(0, pWhere.length() - 4);
                Where += " (" + pWhere + ") And ";
            }
        }

        if (!TagsElimina.isEmpty()) {
            String[] p;
            if (TagsElimina.contains(";")) {
                p = TagsElimina.split(";");
            } else {
                p = new String[]{TagsElimina};
            }
            String pWhere = "";
            for (String pp : p) {
                pWhere += "Artista <> '" + pp.replace("'", "''") + "' And ";
            }
            if (!pWhere.isEmpty()) {
                pWhere = pWhere.substring(0, pWhere.length() - 4);
                Where += " (" + pWhere + ") And ";
            }
        }

        String DataSuperiore = "";
        String DataInferiore = "";

        if (VariabiliStatichePlayer.getInstance().isDate()) {
            if (VariabiliStatichePlayer.getInstance().isDataSuperiore()) {
                if (!VariabiliStatichePlayer.getInstance().getTxtDataSuperiore().isEmpty()) {
                    DataSuperiore = VariabiliStatichePlayer.getInstance().getTxtDataSuperiore();
                }
            }

            if (VariabiliStatichePlayer.getInstance().isDataInferiore()) {
                if (!VariabiliStatichePlayer.getInstance().getTxtDataInferiore().isEmpty()) {
                    DataInferiore = VariabiliStatichePlayer.getInstance().getTxtDataInferiore();
                }
            }
        }

        int idUltimoBrano;
        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
            idUltimoBrano = VariabiliStatichePlayer.getInstance().getUltimoBrano().getIdBrano();
        } else {
            idUltimoBrano = 1;
        }

        if (!Where.isEmpty()) {
            Where = "Where " + Where.substring(0, Where.length() - 4);
        }

        s.setStelle(Stelle);
        s.setStelleSuperiori(StelleSuperiori);
        s.setMaiAscoltata(MaiAscoltata);
        s.setTesto(Testo);
        s.setTestoNon(TestoNon);
        s.setPreferiti(Preferiti);
        s.setPreferitiElimina(PreferitiElimina);
        s.setAndOrPref(AndOrPref);
        s.setTags(Tags);
        s.setTagsElimina(TagsElimina);
        s.setAndOrTags(AndOrTags);
        s.setDataSuperiore(DataSuperiore);
        s.setDataInferiore(DataInferiore);
        s.setIdUltimoBrano(idUltimoBrano);
        s.setWhere(Where);

        return s;
    }

    private void AggiungeBranoAllaListaAscoltati(Context context, StrutturaBrano sb) {
        db_dati_player db = new db_dati_player(context);
        int idBrano = db.PrendeBranoDaDati(sb);
        if (idBrano > -1) {
            if (VariabiliStatichePlayer.getInstance().getIdBraniAscoltati() == null) {
                VariabiliStatichePlayer.getInstance().setIdBraniAscoltati(new ArrayList<>());
            }
            VariabiliStatichePlayer.getInstance().getIdBraniAscoltati().add(idBrano);
        }
    }

    public void ImpostaTastiSfondo(boolean Accesi) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                boolean visibile;

                if (Accesi) {
                    visibile = true; // LinearLayout.VISIBLE;
                } else {
                    visibile = false; // LinearLayout.GONE;
                }
                if (VariabiliStatichePlayer.getInstance().getImgCondividi() != null) {
                    VariabiliStatichePlayer.getInstance().getImgCondividi().setEnabled(visibile);
                }
                if (VariabiliStatichePlayer.getInstance().getImgRefreshImmagini() != null) {
                    VariabiliStatichePlayer.getInstance().getImgRefreshImmagini().setEnabled(visibile);
                }
                if (VariabiliStatichePlayer.getInstance().getImgEliminaSfondo() != null) {
                    VariabiliStatichePlayer.getInstance().getImgEliminaSfondo().setEnabled(visibile);
                }
                if (VariabiliStatichePlayer.getInstance().getImgAvantiSfondo() != null) {
                    VariabiliStatichePlayer.getInstance().getImgAvantiSfondo().setEnabled(visibile);
                }
                if (VariabiliStatichePlayer.getInstance().getImgIndietroSfondo() != null) {
                    VariabiliStatichePlayer.getInstance().getImgIndietroSfondo().setEnabled(visibile);
                }
                if (VariabiliStatichePlayer.getInstance().getImgImposta() != null) {
                    VariabiliStatichePlayer.getInstance().getImgImposta().setEnabled(visibile);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }
}
