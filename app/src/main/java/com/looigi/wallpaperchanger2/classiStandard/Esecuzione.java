package com.looigi.wallpaperchanger2.classiStandard;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import static androidx.core.content.ContextCompat.registerReceiver;

public class Esecuzione {
    private Handler handler;
    // private int errori;
    private long tmsPrecedente = -1L;
    private static final String NomeMaschera = "Esecuzione";
    private final Context context;
    private Runnable r;
    private HandlerThread handlerThread;

    public Esecuzione(Context context) {
        this.context = context;

        VariabiliStaticheServizio.getInstance().setErrori(0);
    }

    public void startServizio1() {
        int minuti = VariabiliStaticheServizio.getInstance().getMinutiAttesa();
        int quantiGiri = (minuti * 60) / VariabiliStaticheServizio.secondiDiAttesaContatore;

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Start contatore di tipo 1. " +
                "Secondi di attesa: " + VariabiliStaticheServizio.secondiDiAttesaContatore +
                "Tempo Timer: " + VariabiliStaticheServizio.getInstance().getMinutiAttesa() +
                "Quanti Giri: " + quantiGiri);

        long secondiAttesa = (VariabiliStaticheServizio.secondiDiAttesaContatore * 1000L);

        if (VariabiliStaticheServizio.getInstance().isServizioAttivo()) {
            BloccaTimer();
        }

        VariabiliStaticheServizio.getInstance().setServizioAttivo(true);

        handlerThread = new HandlerThread("background-thread_" +
                VariabiliStaticheServizio.channelName);
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
        VariabiliStaticheServizio.getInstance().setServizioAttivo(false);

        if (handler != null && r != null && handlerThread != null) {
            handlerThread.quit();

            handler.removeCallbacksAndMessages(null);

            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    }

    private void Controllo() {
        VariabiliStaticheServizio.getInstance().setSecondiPassati(
                VariabiliStaticheServizio.getInstance().getSecondiPassati() + 1);

        long tmsAttuale = System.currentTimeMillis() / 1000L;
        if (tmsPrecedente == -1L) {
            tmsPrecedente = tmsAttuale;
        } else {
            long diff = (tmsAttuale - tmsPrecedente);
            if (diff > ((VariabiliStaticheServizio.secondiDiAttesaContatore * 1000L) + 10000L)) {
                int errori = VariabiliStaticheServizio.getInstance().getErrori() + 1;
                VariabiliStaticheServizio.getInstance().setErrori(errori);

                Utility.getInstance().ScriveLog(context, NomeMaschera, "-------------------");
                Utility.getInstance().ScriveLog(context, NomeMaschera, "ERRORE Secondi " + Long.toString(diff / 1000L) + "/" +
                        Integer.toString(VariabiliStaticheServizio.secondiDiAttesaContatore));
                Utility.getInstance().ScriveLog(context, NomeMaschera, "-------------------");
            }
            tmsPrecedente = tmsAttuale;
        }

        int minuti = VariabiliStaticheServizio.getInstance().getMinutiAttesa();
        int quantiGiri = (minuti * 60) / VariabiliStaticheServizio.secondiDiAttesaContatore;
        
        String prossimoCambio = "Prossimo cambio: " +
                VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
                quantiGiri;
        if (VariabiliStaticheServizio.getInstance().getTxtTempoAlCambio() != null) {
            VariabiliStaticheServizio.getInstance().getTxtTempoAlCambio().setText(prossimoCambio);
        }

        if (VariabiliStaticheServizio.getInstance().isScreenOn()) {
            GestioneNotifiche.getInstance().AggiornaNotifica();
        }

        if (VariabiliStaticheServizio.getInstance().getSecondiPassati() >= quantiGiri) {
            VariabiliStaticheServizio.getInstance().setSecondiPassati(0);

            if (VariabiliStaticheServizio.getInstance().isOnOff()) {
                // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                Utility.getInstance().CambiaImmagine(context);
            }

            if (!VariabiliStaticheServizio.getInstance().isScreenOn() ||
                !VariabiliStaticheServizio.getInstance().isOnOff()) {
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

