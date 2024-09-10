package com.looigi.wallpaperchanger2.classiStandard;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.looigi.wallpaperchanger2.classiAttivita.ChangeWallpaper;
import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

import static androidx.core.content.ContextCompat.registerReceiver;

public class Esecuzione {
    private Handler handler;
    private int errori;
    private long tmsPrecedente = -1L;
    private static final String NomeMaschera = "Esecuzione";
    private final Context context;
    private Runnable r;

    public Esecuzione(Context context) {
        this.context = context;

        errori = 0;
    }

    public void restartServizio() {
        if (handler != null && r != null) {
            handler.removeCallbacks(r);
            r = null;
        }

        startServizio1();
    }

    public void startServizio1() {
        int minuti = VariabiliStaticheServizio.getInstance().getMinutiAttesa();
        int quantiGiri = (minuti * 60) / VariabiliStaticheServizio.secondiDiAttesaContatore;

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Start contatore di tipo 1. " +
                "Secondi di attesa: " + VariabiliStaticheServizio.secondiDiAttesaContatore +
                "Tempo Timer: " + VariabiliStaticheServizio.getInstance().getMinutiAttesa() +
                "Quanti Giri: " + quantiGiri);

        HandlerThread handlerThread = new HandlerThread("background-thread_" + VariabiliStaticheServizio.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                Controllo();

                handler.postDelayed(this, (VariabiliStaticheServizio.secondiDiAttesaContatore * 1000));
            }
        };
        handler.postDelayed(r, (VariabiliStaticheServizio.secondiDiAttesaContatore * 1000));
    }

    private void Controllo() {
        VariabiliStaticheServizio.getInstance().setSecondiPassati(
                VariabiliStaticheServizio.getInstance().getSecondiPassati() + 1);

        long tmsAttuale = System.currentTimeMillis() / 1000L;
        if (tmsPrecedente == -1L) {
            tmsPrecedente = tmsAttuale;
        } else {
            long diff = (tmsAttuale - tmsPrecedente);
            if (diff > ((VariabiliStaticheServizio.secondiDiAttesaContatore * 1000) + 10000L)) {
                errori++;
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

        if (VariabiliStaticheServizio.getInstance().getSecondiPassati() >= quantiGiri) {
            VariabiliStaticheServizio.getInstance().setSecondiPassati(0);

            if (VariabiliStaticheServizio.getInstance().isScreenOn()) {
                if (VariabiliStaticheServizio.getInstance().isOnOff()) {
                    VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                    CambiaImmagine();
                }
            } else {
                if (!VariabiliStaticheServizio.getInstance().isImmagineCambiataConSchermoSpento()) {
                    if (VariabiliStaticheServizio.getInstance().isOnOff()) {
                        Utility.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine per schermo spento---");
                        VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(true);
                        CambiaImmagine();
                    }
                }
                // VariabiliGlobali.getInstance().setImmagineDaCambiare(true);
            }
        }

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Contatore " +
                VariabiliStaticheServizio.getInstance().getSecondiPassati() + "/" +
                quantiGiri);

        GestioneNotifiche.getInstance().AggiornaNotifica();
    }

    private void CambiaImmagine() {
        ChangeWallpaper c = new ChangeWallpaper(context);
        if (!VariabiliStaticheServizio.getInstance().isOffline()) {
            boolean fatto = c.setWallpaper(context, null);
            Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata manualmente: " + fatto + "---");
        } else {
            Utility.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine---");
            int numeroRandom = Utility.getInstance().GeneraNumeroRandom(
                    VariabiliStaticheServizio.getInstance().getListaImmagini().size() - 1);
            if (numeroRandom > -1) {
                boolean fatto = c.setWallpaper(context, VariabiliStaticheServizio.getInstance().getListaImmagini().get(numeroRandom));
                Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata: " + fatto + "---");
            } else {
                Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine NON cambiata: Caricamento immagini in corso---");
            }
        }
    }
}

