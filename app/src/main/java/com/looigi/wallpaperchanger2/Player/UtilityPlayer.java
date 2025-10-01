package com.looigi.wallpaperchanger2.Player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Detector.UtilityDetector;
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaFiltroBrano;
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.Player.WebServices.ChiamateWsPlayer;
// import com.looigi.wallpaperchanger2.classiPlayer.WebServices.RipristinoChiamate;
import com.looigi.wallpaperchanger2.Player.WebServices.DownloadImmagine;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Chiacchierone;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.UtilitiesVarie.log.LogInterno;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Player", Maschera,  Log);
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
                FaiRipartireTimer(context);
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

                VariabiliStatichePlayer.getInstance().setStaCaricandoBranoPregresso(false);
                VariabiliStatichePlayer.getInstance().setStrutturaBranoPregressoCaricata(null);
                VariabiliStatichePlayer.getInstance().setHaCaricatoBranoPregresso(false);

                db_dati_player db = new db_dati_player(context);
                db.ScriveUltimoBranoAscoltato(sb);
                db.ChiudeDB();

                // Bitmap bitmap = PrendeImmagineDisco(context, sb);
                // VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(bitmap);

                AggiungeBranoAllaListaAscoltati(context, sb);

                if (VariabiliStatichePlayer.getInstance().isChiacchiera()) {
                    new Chiacchierone(context, sb);
                } else {
                    if (VariabiliStatichePlayer.getInstance().isStaSuonando()) {
                        FaiPartireTimer(context);

                        VariabiliStatichePlayer.getInstance().getMp().start();
                    } else {
                        FermaTimer();
                    }
                }

                ScriveLog(context, NomeMaschera, "Brano caricato");
            } catch (IOException e) {
                ScriveLog(context, NomeMaschera, "Errore caricamento brano: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));
            }

            AggiornaInformazioni(false);

            GestioneNotifichePlayer.getInstance().AggiornaNotifica(sb.getBrano());

            if (sb.getBellezza() == -2) {
                ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                VariabiliStatichePlayer.getInstance().setClasseChiamata(ws);
                ws.RitornaStelleBrano();
            }

            VariabiliStatichePlayer.getInstance().getUltimoBrano().setBellezza(sb.getBellezza());
            UtilityPlayer.getInstance().ImpostaBellezza();
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
                        if (VariabiliStatichePlayer.getInstance().getLayCaricamento() != null) {
                            VariabiliStatichePlayer.getInstance().getLayCaricamento().setVisibility(LinearLayout.VISIBLE);
                        }
                    }
                    quantiCaricamenti++;
                } else {
                    quantiCaricamenti--;
                    if (quantiCaricamenti < 1) {
                        quantiCaricamenti = 0;
                        if (VariabiliStatichePlayer.getInstance().getLayCaricamento() != null) {
                            VariabiliStatichePlayer.getInstance().getLayCaricamento().setVisibility(LinearLayout.GONE);
                        }
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
                if (VariabiliStatichePlayer.getInstance().getTxtOperazione() != null) {
                    if (Operazione.isEmpty()) {
                        VariabiliStatichePlayer.getInstance().getTxtOperazione().setVisibility(LinearLayout.GONE);

                        VariabiliStatichePlayer.getInstance().getTxtOperazione().setText(Operazione);
                    } else {
                        VariabiliStatichePlayer.getInstance().getTxtOperazione().setVisibility(LinearLayout.VISIBLE);

                        VariabiliStatichePlayer.getInstance().getTxtOperazione().setText(Operazione);
                    }
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

    public void ResettaCampi(Context context, boolean Pregresso) {
        Attesa(false);
        AggiornaOperazioneInCorso("");

        if (!Pregresso) {
            StoppaTimer();
        }

        VariabiliStatichePlayer.getInstance().getTxtBranoPregresso().setText("");
        VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.GONE);

        if (!Pregresso) {
            ImpostaLogoApplicazione(context);
        }

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

            ResettaCampi(context, false);

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
            VariabiliStatichePlayer.getInstance().setHaCaricatoBranoPregresso(false);

            CaricaBranoNelLettore(context);

            ImpostaImmagine(context, -1);

            return;
        }

        ScriveLog(context, NomeMaschera, "Avanzo Brano. Brano esatto: " + Brano + ". Pregresso: " + Pregresso);

        AggiornaInformazioni(true);
        ResettaCampi(context, Pregresso);

        // ImpostaImmagine(context, -1);

        // VariabiliStatichePlayer.getInstance().setChiamate(new ArrayList<>());
        // RipristinoChiamate.getInstance().RimuoveTimer();

        if (Brano.isEmpty()) {
            boolean cercaBranoInLocale = false;

            int quantiBrani = db.QuantiBraniInArchivio();
            int random = UtilityPlayer.getInstance().GeneraNumeroRandom(10);
            if (random == 1 || random == 3) { // || (VieneDaTasto && quantiBrani > 0)) {
                cercaBranoInLocale = true;
            }
            if (!cercaBranoInLocale) {
                if (!UtilitiesGlobali.getInstance().isRetePresente()) {
                    cercaBranoInLocale = true;
                }
            }

            if (cercaBranoInLocale) {
                if (quantiBrani < 10) {
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
        db.ChiudeDB();
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
        db.ChiudeDB();
    }

    public void AttesaSI(boolean come) {
        if (VariabiliStatichePlayer.getInstance().getLayCaricamentoSI() != null) {
            if (come) {
                VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.VISIBLE);
            } else {
                VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.GONE);
            }
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

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            VariabiliStatichePlayer.getInstance().getTxtBranoPregresso().setText(sb.getArtista() + ":" + sb.getBrano());
                            VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.VISIBLE);
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 10);
                } else {
                    VariabiliStatichePlayer.getInstance().setUltimoBrano(sb);

                    CaricaBranoNelLettore(context);

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            VariabiliStatichePlayer.getInstance().getTxtBranoPregresso().setText("");
                            VariabiliStatichePlayer.getInstance().getImgCambiaPregresso().setVisibility(LinearLayout.GONE);
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 10);
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
        db.ChiudeDB();
    }

    public void PrendeBranoInRete(Context context, String Brano, boolean Pregresso) {
        ScriveLog(context, NomeMaschera, "Avanzo Brano. Scarico Brano");

        if (VariabiliStatichePlayer.getInstance().getClasseChiamata() != null) {
            VariabiliStatichePlayer.getInstance().getClasseChiamata().StoppaEsecuzione();
        }
        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
        VariabiliStatichePlayer.getInstance().setClasseChiamata(ws);
        ws.RitornaBranoDaID(Brano, Pregresso);
    }

    public void AggiornaUltimaImmagine(Context context, String Path) {
        VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(Path);

        String PathFile = String.valueOf(context.getFilesDir());

        if (Files.getInstance().EsisteFile(PathFile +
                "/Player/UltimaImmagine.txt")) {
            Files.getInstance().EliminaFile(
                    PathFile,
                    "/Player/UltimaImmagine.txt"
                    );
        }
        Files.getInstance().ScriveFile(
                PathFile,
                "/Player/UltimaImmagine.txt",
                Path);
    }

    public void ImpostaImmagine(Context context, int idImmagine) {
        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null &&
                !VariabiliStatichePlayer.getInstance().isMascheraNascosta() &&
                VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            boolean ok = true;

            if (VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini() != null) {
                int quante = VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size();
                if (quante == 0) {
                    ok = false;
                } else {
                    if (quante <= 20) {
                        int randomPerNuovaImmagine = GeneraNumeroRandom(quante + 1);
                        if (randomPerNuovaImmagine == 2 || randomPerNuovaImmagine == 7) {
                            ok = false;
                        }
                    }
                }
            } else {
                ok = false;
            }

            // if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null) {
                if (ok) {
                    List<StrutturaImmagini> lista = VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini();
                    if (lista.isEmpty()) {
                        db_dati_player db = new db_dati_player(context);
                        lista = db. CaricaImmaginiBrano(VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista());
                        if (lista.isEmpty()) {
                            ChiamateWsPlayer c = new ChiamateWsPlayer(context, false);
                            c.RitornaImmaginiArtista(VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista());
                            return;
                        }
                        db.ChiudeDB();
                    }

                    final int[] immagine = new int[1];
                    if (idImmagine == -1) {
                        immagine[0] = GeneraNumeroRandom(lista.size());
                    } else {
                        immagine[0] = idImmagine;
                    }
                    if (immagine[0] > -1) {
                        VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(immagine[0]);

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

                                                // VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(PathImmagine);
                                                UtilityPlayer.getInstance().AggiornaUltimaImmagine(context, PathImmagine);
                                                VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(bitmap);
                                                VariabiliStatichePlayer.getInstance().setImmagineImpostata(finalLista.get(immagine[0]));
                                                VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(immagine[0]);
                                                VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(finalLista.get(immagine[0]));

                                                if (VariabiliStatichePlayer.getInstance().getImgSfondoSettings() != null) {
                                                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmap);
                                                }
                                                if (VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine() != null) {
                                                    VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine().setText("Immagine " + immagine[0] + "/" + (finalLista.size()));
                                                }
                                                if (VariabiliStatichePlayer.getInstance().getEdtNumeroImmagine() != null) {
                                                    VariabiliStatichePlayer.getInstance().getEdtNumeroImmagine().setText(Integer.toString(immagine[0]));
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
                                                db.ChiudeDB();
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
                                                    finalLista.get(immagine[0]).getUrlImmagine(),
                                                    false,
                                                    false,
                                                    ""
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
                    if (!VariabiliStatichePlayer.getInstance().isStaScaricandoImmagine()) {
                        VariabiliStatichePlayer.getInstance().setStaScaricandoImmagine(true);

                        ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                        ws.ScaricaNuovaImmagine(
                                VariabiliStatichePlayer.getInstance().getUltimoBrano().getArtista(),
                                VariabiliStatichePlayer.getInstance().getUltimoBrano().getAlbum(),
                                VariabiliStatichePlayer.getInstance().getUltimoBrano().getBrano()
                        );
                    }
                }
            /* } else {
                VariabiliStatichePlayer.getInstance().setImmagineImpostata(null);
                ImpostaLogoApplicazione(context);
            } */
        }
    }

    public void ImpostaLogoApplicazioneInterna(Context context) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(null);

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                if (VariabiliStatichePlayer.getInstance().getImgSfondoSettings() != null) {
                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmap);

                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setVisibility(LinearLayout.VISIBLE);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 1000);
    }

    public void ImpostaLogoApplicazione(Context context) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                UtilityPlayer.getInstance().AggiornaUltimaImmagine(context, "");

                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                VariabiliStatichePlayer.getInstance().getImgBrano().setImageBitmap(bitmap);
                // VariabiliStatichePlayer.getInstance().setIdImmagineImpostata(-1);

                UtilityPlayer.getInstance().ScriveInfoImmagine();

                if (VariabiliStatichePlayer.getInstance().getImgSfondoSettings() != null) {
                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmap);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 1000);
    }

    public void FaiPartireTimerChiusura(Context context) {
        if (handlerTimerChiusura != null) {
            handlerTimerChiusura.removeCallbacksAndMessages(runTimerChiusura);
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

    public Bitmap PrendeImmagineDisco(Context context, StrutturaBrano sb) {
        String Path = context.getFilesDir() + "/Player/ImmaginiMusica/";
        String PathImmagini = Path + sb.getArtista() + "/" + sb.getAnno() + "-" + sb.getAlbum();
        File root = new File(PathImmagini);
        File[] list = root.listFiles();
        Bitmap bitmap = null;

        String Url = VariabiliStatichePlayer.PercorsoBranoMP3SuURL + "/ImmaginiMusica/" + sb.getArtista() +
                "/" + sb.getAnno() + "-" + sb.getAlbum() + "/Cover_" + sb.getArtista() + ".jpg";
        if (list == null) {
            DownloadImmagine d = new DownloadImmagine();
            VariabiliStatichePlayer.getInstance().setDownImmagine(d);
            d. EsegueDownload(
                context,
                VariabiliStatichePlayer.getInstance().getImgBrano(),
                Url,
                false,
                false,
                ""
            );
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
            return bitmap;
        } else {
            for (File f : list) {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                if (!f.isDirectory() && Filetto.toUpperCase().contains("COVER_")) {
                    if (Files.getInstance().EsisteFile(Filetto)) {
                        bitmap = BitmapFactory.decodeFile(Filetto);
                        VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(Filetto);
                    } else {
                        DownloadImmagine d = new DownloadImmagine();
                        VariabiliStatichePlayer.getInstance().setDownImmagine(d);
                        d. EsegueDownload(
                            context,
                            VariabiliStatichePlayer.getInstance().getImgBrano(),
                            Url,
                            false,
                            false,
                            ""
                        );
                        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                        return bitmap;
                    }
                }
            }
        }

        return bitmap;
    }

    public Bitmap PrendeImmagineArtistaACaso(Context context, String Artista) {
        String Path = context.getFilesDir() + "/Player/ImmaginiMusica/";
        String PathImmagini = Path + Artista + "/ZZZ-ImmaginiArtista";
        File root = new File(PathImmagini);
        File[] list = root.listFiles();
        Bitmap bitmap = null;

        if (list == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
        } else {
            List<String> Nomi = new ArrayList<>();

            for (File f : list) {
                if (!f.isDirectory()) {
                    String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                    Nomi.add(Filetto);
                }
            }

            int n = UtilityPlayer.getInstance().GeneraNumeroRandom(Nomi.size());

            boolean ancora = true;
            boolean presa = false;
            int quanti = 0;

            while (ancora) {
                String Nome = "";

                if (n > 0 && n < Nomi.size()) {
                    Nome = Nomi.get(n);
                }

                if (Nome.isEmpty()) {
                    // ImpostaImmagine(context, -1);
                    n++;
                    if (n >= Nomi.size()) {
                        n = 0;
                    }
                    quanti++;
                    if (quanti > 10) {
                        ancora = false;
                    }
                } else {
                    if (Files.getInstance().EsisteFile(Nome)) {
                        bitmap = BitmapFactory.decodeFile(Nome);
                        VariabiliStatichePlayer.getInstance().setPathUltimaImmagine(Nome);

                        presa = true;
                        ancora = false;
                    } else {
                        n++;
                        if (n >= Nomi.size()) {
                            n = 0;
                        }
                        quanti++;
                        if (quanti > 10) {
                            ancora = false;
                        }
                    }
                }
            }

            if (!presa) {
                ImpostaImmagine(context, -1);
            }
        }

        return bitmap;
    }

    public void StoppaTimer() {
        if (handlerTimer != null) {
            handlerTimer.removeCallbacksAndMessages(runTimer);
            handlerTimer.removeCallbacks(runTimer);
            runTimer = null;
        }
    }

    public void FaiPartireTimer(Context context) {
        ScriveLog(context, NomeMaschera, "Fatto (Ri)Partire Timer");

        SecondiPassati = 0;
        VariabiliStatichePlayer.getInstance().setSecondiPassati(SecondiPassati);
        SecondiPassatiCambioImmagine = 0;

        if (handlerTimer != null) {
            // handlerTimer.removeCallbacks(runTimer);
            handlerTimer.removeCallbacksAndMessages(null);
            handlerTimer.removeCallbacks(null);
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
                            ImpostaImmagine(context, -1);
                        }
                    }

                    if (SecondiPassati > VariabiliStatichePlayer.SecondiBranoPregresso &&
                            !VariabiliStatichePlayer.getInstance().isStaCaricandoBranoPregresso() &&
                        !VariabiliStatichePlayer.getInstance().isHaCaricatoBranoPregresso()) {
                        VariabiliStatichePlayer.getInstance().setStaCaricandoBranoPregresso(true);

                        BranoAvanti(context, "", true, false);
                    }
                } else {
                    int a = 0;
                }

                handlerTimer.postDelayed(this, 1000);
            }
        }, 1000);
    }

    public void FaiRipartireTimer(Context context) {
        if (handlerTimer == null) {
            FaiPartireTimer(context);
        }
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

    public void ScrivePerc() {
        float lim = VariabiliStatichePlayer.getInstance().getLimiteInGb();
        long L = (long) (lim * 1024 * 1024 * 1024);
        double ll = Math.round(L * .8);
        long Limite = (long) ll;

        long Spazio = VariabiliStatichePlayer.getInstance().getSpazioOccupato();
        int pe = Math.round(((float) Spazio / Limite) * 100);

        String perc = pe + "%";
        VariabiliStatichePlayer.getInstance().setPerc(pe);

        VariabiliStatichePlayer.getInstance().getTxtPercentuale().setText(perc);
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
            // if (VariabiliStatichePlayer.getInstance().isDataSuperiore()) {
                if (!VariabiliStatichePlayer.getInstance().getsDataSuperiore().isEmpty()) {
                    DataSuperiore = VariabiliStatichePlayer.getInstance().getsDataSuperiore();
                }
            // }

            // if (VariabiliStatichePlayer.getInstance().isDataInferiore()) {
                if (!VariabiliStatichePlayer.getInstance().getsDataInferiore().isEmpty()) {
                    DataInferiore = VariabiliStatichePlayer.getInstance().getsDataInferiore();
                }
            // }
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
        db.ChiudeDB();
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
                if (VariabiliStatichePlayer.getInstance().getImgModificaSfondo() != null) {
                    VariabiliStatichePlayer.getInstance().getImgModificaSfondo().setEnabled(visibile);
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

    public void SalvataggioImmagine(Context context, boolean Sovrascrive) {
        StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica();
        String Path = s.getPathImmagine();

        ImpostaImmagineInterna(context);

        String encodedImage = UtilitiesGlobali.getInstance().convertBmpToBase64(Path);

        ChiamateWsPlayer c = new ChiamateWsPlayer(context, false);
        c.ModificaImmagine(s, encodedImage, Sovrascrive);
    }

    public void ScriveInfoImmagine() {
        if (VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine() != null) {
            if (VariabiliStatichePlayer.getInstance().getUltimoBrano() != null && VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini() != null) {
                VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine().setText("Immagine " +
                        VariabiliStatichePlayer.getInstance().getIdImmagineImpostata() +
                        "/" + (VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size()));
            }
        }

        if (VariabiliStatichePlayer.getInstance().getEdtNumeroImmagine() != null) {
            VariabiliStatichePlayer.getInstance().getEdtNumeroImmagine().setText(
                    Integer.toString(VariabiliStatichePlayer.getInstance().getIdImmagineImpostata())
            );
        }

        if (VariabiliStatichePlayer.getInstance().getTxtNomeImmaginePerModifica() != null) {
            if (VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica() != null) {
                VariabiliStatichePlayer.getInstance().getTxtNomeImmaginePerModifica().setText(
                        VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica().getNomeImmagine()
                );
            }
        }
    }

    public void ImpostaImmagineInterna(Context context) {
        if (VariabiliStatichePlayer.getInstance().getUltimoBrano() == null) {
            return;
        }

        Bitmap bitmapAttesa = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading);
        VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmapAttesa);

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                int n = VariabiliStatichePlayer.getInstance().getIdImmagineImpostata();
                if (!VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().isEmpty() &&
                        n < VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size()) {
                    StrutturaImmagini s = VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().get(n);
                    VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(s);
                    String path = s.getPathImmagine();
                    path = path.replace("//", "/");
                    Bitmap bitmap = null;
                    VariabiliStatichePlayer.getInstance().setCeImmaginePerModifica(false);
                    boolean visibile = true;
                    if (Files.getInstance().EsisteFile(path)) {
                        bitmap = BitmapFactory.decodeFile(path);
                        VariabiliStatichePlayer.getInstance().setCeImmaginePerModifica(true);
                    } else {
                        if (!UtilitiesGlobali.getInstance().isRetePresente()) {
                            // bitmap = null;
                            visibile = false;
                            VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(null);
                        } else {
                            DownloadImmagine d = new DownloadImmagine();
                            VariabiliStatichePlayer.getInstance().setDownImmagine(d);
                            d.EsegueDownload(
                                    context,
                                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings(),
                                    s.getUrlImmagine(),
                                    true,
                                    false,
                                    ""
                            );
                        }
                    }
                /* if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                } */

                    if (visibile) {
                        VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setVisibility(LinearLayout.VISIBLE);
                        VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmap);
                    } else {
                        VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setVisibility(LinearLayout.GONE);
                    }

                    if (VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica() != null &&
                            VariabiliStatichePlayer.getInstance().getTxtNomeImmaginePerModifica() != null) {
                        VariabiliStatichePlayer.getInstance().getTxtNomeImmaginePerModifica().setText(
                                VariabiliStatichePlayer.getInstance().getImmagineVisualizzataPerModifica().getNomeImmagine()
                        );
                    }

                    if (VariabiliStatichePlayer.getInstance().getEdtNumeroImmagine() != null) {
                        VariabiliStatichePlayer.getInstance().getEdtNumeroImmagine().setText(
                                Integer.toString(VariabiliStatichePlayer.getInstance().getIdImmagineImpostata())
                        );
                    }

                    if (VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine() != null) {
                        VariabiliStatichePlayer.getInstance().getTxtNumeroImmagine().setText("Immagine " + n +
                                "/" + (VariabiliStatichePlayer.getInstance().getUltimoBrano().getImmagini().size()));
                    }
                } else {
                    VariabiliStatichePlayer.getInstance().setImmagineVisualizzataPerModifica(null);

                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setImageBitmap(bitmap);

                    VariabiliStatichePlayer.getInstance().getImgSfondoSettings().setVisibility(LinearLayout.VISIBLE);
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 50);

    }

    public void ScrivePreferitiTags() {
        VariabiliStatichePlayer.getInstance().getTxtPreferiti().setText(VariabiliStatichePlayer.getInstance().getPreferiti());
        VariabiliStatichePlayer.getInstance().getTxtNonPreferiti().setText(VariabiliStatichePlayer.getInstance().getPreferitiElimina());
        VariabiliStatichePlayer.getInstance().getTxtTags().setText(VariabiliStatichePlayer.getInstance().getPreferitiTags());
        VariabiliStatichePlayer.getInstance().getTxtNonTags().setText(VariabiliStatichePlayer.getInstance().getPreferitiEliminaTags());
    }
}
