package com.looigi.wallpaperchanger2.classeAvvio;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeWallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.CaricaSettaggi;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import static androidx.core.content.ContextCompat.registerReceiver;

public class Esecuzione {
    // private int errori;
    private long tmsPrecedente = -1L;
    private static final String NomeMaschera = "Esecuzione";
    private Context context;
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;

    public Esecuzione(Context context) {
        this.context = context;

        VariabiliStaticheWallpaper.getInstance().setErrori(0);
    }

    public void startServizio1() {
        int minuti = VariabiliStaticheWallpaper.getInstance().getMinutiAttesa();
        int quantiGiri = (minuti * 60) / VariabiliStaticheWallpaper.secondiDiAttesaContatore;

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Start contatore di tipo 1. " +
                "Secondi di attesa: " + VariabiliStaticheWallpaper.secondiDiAttesaContatore +
                "Tempo Timer: " + VariabiliStaticheWallpaper.getInstance().getMinutiAttesa() +
                "Quanti Giri: " + quantiGiri);

        long secondiAttesa = (VariabiliStaticheWallpaper.secondiDiAttesaContatore * 1000L);

        if (VariabiliStaticheWallpaper.getInstance().isServizioAttivo()) {
            BloccaTimer();
        }

        VariabiliStaticheWallpaper.getInstance().setServizioAttivo(true);

        handlerThread = new HandlerThread("background-thread_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                Controllo();

                if (handler != null) {
                    handler.postDelayed(this, secondiAttesa);
                }
            }
        };
        handler.postDelayed(r, secondiAttesa);
    }

    public void BloccaTimer() {
        VariabiliStaticheWallpaper.getInstance().setServizioAttivo(false);

        if (handler != null && r != null && handlerThread != null) {
            handlerThread.quit();

            handler.removeCallbacksAndMessages(null);

            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    }

    private void ControlloImpostazioni() {
        boolean ricaricaSettaggi = false;

        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniDebug()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Settaggi Debug non validi");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniDetector()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Settaggi Detector non validi");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniGPS()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Settaggi GPS non validi");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniImmagini()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Settaggi Immagini non validi");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniPlayer()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Settaggi Player non validi");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniPennetta()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Settaggi Pennetta non validi");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniWallpaper()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Settaggi Wallpaper non validi");
            ricaricaSettaggi = true;
        }
        if (ricaricaSettaggi) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Ricarico settaggi");
            String ritorno = CaricaSettaggi.getInstance().CaricaImpostazioniGlobali(context, "ESECUZIONE");
            if (!ritorno.equals("OK")) {
                UtilityDetector.getInstance().VisualizzaPOPUP(
                        context, ritorno, false, -1
                );
            }
        }
    }

    private void Controllo() {
        VariabiliStaticheWallpaper.getInstance().setSecondiPassati(
                VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + 1);

        ControlloImpostazioni();

        long tmsAttuale = System.currentTimeMillis() / 1000L;
        if (tmsPrecedente == -1L) {
            tmsPrecedente = tmsAttuale;
        } else {
            long diff = (tmsAttuale - tmsPrecedente);
            if (diff > ((VariabiliStaticheWallpaper.secondiDiAttesaContatore * 1000L) + 10000L)) {
                int errori = VariabiliStaticheWallpaper.getInstance().getErrori() + 1;
                VariabiliStaticheWallpaper.getInstance().setErrori(errori);

                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "-------------------");
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "ERRORE Secondi " + Long.toString(diff / 1000L) + "/" +
                        Integer.toString(VariabiliStaticheWallpaper.secondiDiAttesaContatore));
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "-------------------");
            }
            tmsPrecedente = tmsAttuale;
        }

        int minuti = VariabiliStaticheWallpaper.getInstance().getMinutiAttesa();
        int quantiGiri = (minuti * 60) / VariabiliStaticheWallpaper.secondiDiAttesaContatore;

        if (context != null) {
            VariabiliStaticheStart.getInstance().setContext(context);
        } else {
            context = UtilitiesGlobali.getInstance().tornaContextValido();
            if (context == null) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "-------------------");
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "ERRORE CONTEXT NON VALIDO");
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "-------------------");
            }
        }

        String prossimoCambio = "Prossimo cambio: " +
                VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + "/" +
                quantiGiri;
        if (VariabiliStaticheWallpaper.getInstance().getTxtTempoAlCambio() != null) {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    VariabiliStaticheWallpaper.getInstance().getTxtTempoAlCambio().setText(prossimoCambio);
                }
            }, 100);
        }

        if (VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            GestioneNotificheWP.getInstance().AggiornaNotifica();
        }

        if (VariabiliStaticheWallpaper.getInstance().getSecondiPassati() >= quantiGiri) {
            VariabiliStaticheWallpaper.getInstance().setSecondiPassati(0);

            if (VariabiliStaticheWallpaper.getInstance().isOnOff()) {
                // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                UtilityWallpaper.getInstance().CambiaImmagine(context);
            }

            if (!VariabiliStaticheWallpaper.getInstance().isScreenOn() ||
                !VariabiliStaticheWallpaper.getInstance().isOnOff()) {
                BloccaTimer();
            }

            // if (!VariabiliStaticheServizio.getInstance().isScreenOn()) {
            // } else {
                // if (!VariabiliStaticheServizio.getInstance().isImmagineCambiataConSchermoSpento()) {
                //     BloccaTimer();

                    /* if (VariabiliStaticheServizio.getInstance().isOnOff()) {
                        Utility.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine per schermo spento---");
                        Utility.getInstance().CambiaImmagine(context);
                        // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(true);
                    } */
                // }
                // VariabiliGlobali.getInstance().setImmagineDaCambiare(true);
            // }
        }

        /* Utility.getInstance().ScriveLog(context, NomeMaschera, "Contatore " +
                VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
                quantiGiri); */
    }
}

