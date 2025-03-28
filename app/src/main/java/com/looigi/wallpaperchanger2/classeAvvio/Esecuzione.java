package com.looigi.wallpaperchanger2.classeAvvio;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Parcelable;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeWallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.CaricaSettaggi;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;
import com.looigi.wallpaperchanger2.watchDog.VariabiliStaticheWatchdog;
import com.looigi.wallpaperchanger2.watchDog.WidgetWatchdog;

import static androidx.core.content.ContextCompat.registerReceiver;

public class Esecuzione {
    // private int errori;
    private long tmsPrecedente = -1L;
    private static final String NomeMaschera = "Esecuzione";
    private Context context;
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;
    private int Giri = 0;
    private int GiriPerAggiornamentoNotifica = 0;

    public Esecuzione(Context context) {
        this.context = context;

        VariabiliStaticheWallpaper.getInstance().setErrori(0);

        Giri = 0;
        GiriPerAggiornamentoNotifica = 0;

        VariabiliStaticheWatchdog.getInstance().setInfo1("Starting");
        VariabiliStaticheWatchdog.getInstance().setInfo2("");

        updateWatchDog(context);
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
            BloccaTimer("Start Servizio");
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

    public void BloccaTimer(String daDove) {
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                "Blocco timer esecuzione da " + daDove);

        VariabiliStaticheWallpaper.getInstance().setServizioAttivo(false);

        if (handler != null && r != null && handlerThread != null) {
            // handlerThread.quit();

            handler.removeCallbacksAndMessages(null);

            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    }

    private void ControlloImpostazioni() {
        boolean ricaricaSettaggi = false;

        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniDebug()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                    "Settaggi Debug non validi. Ricarico");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniDetector()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                    "Settaggi Detector non validi. Ricarico");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniGPS()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                    "Settaggi GPS non validi. Ricarico");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniImmagini()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                    "Settaggi Immagini non validi. Ricarico");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniPlayer()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                    "Settaggi Player non validi. Ricarico");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniPennetta()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                    "Settaggi Pennetta non validi. Ricarico");
            ricaricaSettaggi = true;
        }
        if (!CaricaSettaggi.getInstance().isCaricateImpostazioniWallpaper()) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                    "Settaggi Wallpaper non validi. Ricarico");
            ricaricaSettaggi = true;
        }
        if (ricaricaSettaggi) {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                    "Ricarico settaggi");
            String ritorno = CaricaSettaggi.getInstance().CaricaImpostazioniGlobali(context,
                    "ESECUZIONE");
            if (!ritorno.equals("OK")) {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,
                        "Ricarico settaggi andato male: " + ritorno);

                UtilityDetector.getInstance().VisualizzaPOPUP(
                        context, ritorno, false, -1
                );
            }
        }
    }

    /* private StrutturaGps vecchiaGPS;
    private int Conta = 0;
    private int Conta2 = 0;

    private void ResettaGPS(String daDove) {
        Conta = 0;
        Conta2 = 0;

        if (VariabiliStaticheGPS.getInstance().getGestioneGPS() != null) {
            VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("Resetta GPS");
            VariabiliStaticheGPS.getInstance().getGestioneGPS().NullaLocationManager();
        }

        VariabiliStaticheGPS.getInstance().setGestioneGPS(null);
        VariabiliStaticheGPS.getInstance().setGestioneGPS(new GestioneGPS());
        VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaTimer(context);
        VariabiliStaticheGPS.getInstance().getGestioneGPS().AbilitaGPS(daDove);
    } */

    private void ControlloGPSAttivo() {
        // Controllo sull'attività del GPS
        /* if (VariabiliStaticheStart.getInstance().isDetector()) {
            if (!isMyServiceRunning(GestioneGPS.class)) {
                UtilityGPS.getInstance().ScriveLog(
                        context,
                        NomeMaschera,
                        "Servizio GPS non girante... Faccio ripartire");

                VariabiliStaticheGPS.getInstance().setGestioneGPS(null);

                VariabiliStaticheStart.getInstance().setServizioForegroundGPS(
                        new Intent(context,
                                ServizioDiAvvioGPS.class)
                );
                context.startForegroundService(VariabiliStaticheStart.getInstance().getServizioForegroundGPS());
            } else {
                if (VariabiliStaticheStart.getInstance().getServizioForegroundGPS() == null) {
                    UtilityGPS.getInstance().ScriveLog(
                            context,
                            NomeMaschera,
                            "Servizio GPS Nullo... Faccio ripartire");

                    VariabiliStaticheStart.getInstance().setServizioForegroundGPS(
                            new Intent(context,
                                    ServizioDiAvvioGPS.class)
                    );
                    context.startForegroundService(
                            VariabiliStaticheStart.getInstance().getServizioForegroundGPS()
                    );
                }
            } else {
                if (VariabiliStaticheGPS.getInstance().getGestioneGPS() == null) {
                    UtilityGPS.getInstance().ScriveLog(
                            context,
                            NomeMaschera,
                            "Gestione GPS Nulla... Faccio ripartire");

                    ResettaGPS("Controllo 1");
                } else {
                    if (VariabiliStaticheGPS.getInstance().getGestioneGPS().LocationManagerAttivo() == null) {
                        UtilityGPS.getInstance().ScriveLog(
                                context,
                                NomeMaschera,
                                "Location manager Nullo... Faccio ripartire");

                        ResettaGPS("Controllo 3");
                    } else {
                        if (VariabiliStaticheGPS.getInstance().isGpsAttivo() &&
                            !VariabiliStaticheGPS.getInstance().isBloccatoDaTasto()) {
                            if (VariabiliStaticheGPS.getInstance().getCoordinateAttuali() != null) {
                                Conta2 = 0;

                                StrutturaGps s = VariabiliStaticheGPS.getInstance().getCoordinateAttuali();
                                if (vecchiaGPS != null) {
                                    if (vecchiaGPS == s) {
                                        Conta++;

                                        UtilityGPS.getInstance().ScriveLog(
                                                context,
                                                NomeMaschera,
                                                "Stuttura GPS Uguale alla precedente. Contatore " + Conta);

                                        if (Conta > 6) {
                                            // Un minuto con la stessa struttura. Devo capire se fare qualcosa
                                        }
                                    } else {
                                        Conta = 0;
                                    }
                                }
                                vecchiaGPS = s;
                            } else {
                                Conta2++;
                                UtilityGPS.getInstance().ScriveLog(
                                        context,
                                        NomeMaschera,
                                        "Stuttura GPS Nulla. Contatore " + Conta2);
                                if (Conta2 > 6) {
                                    // Un minuto senza struttura. Devo capire se fare qualcosa
                                }
                                VariabiliStaticheGPS.getInstance().getGestioneGPS().ControlloAccSpegn(context);
                            }
                        } else {
                            // GPS Non attivo per flag
                            Conta = 0;
                            Conta2 = 0;

                            VariabiliStaticheGPS.getInstance().getGestioneGPS().ControlloAccSpegn(context);
                        }
                    }
                }
            }
        } */
        // Controllo sull'attività del GPS
    }

    private void Controllo() {
        VariabiliStaticheWallpaper.getInstance().setSecondiPassati(
                VariabiliStaticheWallpaper.getInstance().getSecondiPassati() + 1);

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Tick: " +
                VariabiliStaticheWallpaper.getInstance().getSecondiPassati());

        ControlloImpostazioni();

        ControlloGPSAttivo();

        Giri++;

        GiriPerAggiornamentoNotifica++;
        if (GiriPerAggiornamentoNotifica > 5) {
            GiriPerAggiornamentoNotifica = 0;

            GestioneNotificheTasti.getInstance().AggiornaNotifica();
        }

        String quando = UtilitiesGlobali.getInstance().RitornaOra();
        VariabiliStaticheWatchdog.getInstance().setInfo1("Loops: " + Giri + " - Last: " + quando + " - Ctx: "
                + (context != null));

        long tmsAttuale = System.currentTimeMillis() / 1000L;
        if (tmsPrecedente == -1L) {
            tmsPrecedente = tmsAttuale;
        } else {
            long diff = (tmsAttuale - tmsPrecedente);
            if (diff > ((VariabiliStaticheWallpaper.secondiDiAttesaContatore * 1000L) + 10000L)) {
                int errori = VariabiliStaticheWallpaper.getInstance().getErrori() + 1;
                VariabiliStaticheWallpaper.getInstance().setErrori(errori);

                String errore = "ERRORI " + errori + ": Ultimo " + Long.toString(diff / 1000L) + "/" +
                        Integer.toString(VariabiliStaticheWallpaper.secondiDiAttesaContatore);

                VariabiliStaticheWatchdog.getInstance().setInfo2(errore);

                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "-------------------");
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, errore);
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "-------------------");
            }
            tmsPrecedente = tmsAttuale;
        }

        updateWatchDog(context);

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
                if (VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
                    // VariabiliStaticheServizio.getInstance().setImmagineCambiataConSchermoSpento(false);
                    VariabiliStaticheWallpaper.getInstance().setImpostataConSchermoSpento(false);

                    UtilityWallpaper.getInstance().CambiaImmagine(context);
                } else {
                    VariabiliStaticheWallpaper.getInstance().setImpostataConSchermoSpento(true);
                }
            }

            /* if (!VariabiliStaticheWallpaper.getInstance().isScreenOn() ||
                !VariabiliStaticheWallpaper.getInstance().isOnOff()) {
                BloccaTimer("Controllo");
            } */

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

    public void updateWatchDog(Context context) {
        AppWidgetManager man = AppWidgetManager.getInstance(context);
        int[] ids = man.getAppWidgetIds(
                new ComponentName(context, WidgetWatchdog.class));
        Intent updateIntent = new Intent();
        updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // updateIntent.putExtra(WidgetWatchdog.WIDGET_ID_KEY, ids);
        // updateIntent.putExtra(WidgetWatchdog.WIDGET_DATA_KEY, data);
        context.sendBroadcast(updateIntent);
    }
}

