package com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classeVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.ChiamateWsWP;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.InterrogazioneWSWP;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.TaskDelegate;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;


public class ChiamateWsWPRefresh implements TaskDelegate {
    private final String RadiceWS_IoNos = "http://www.wsloovf.looigi.it/";
    private final String ws_IoNos = "looRefreshImmagini.asmx/";
    private final String NS_IoNos ="http://looRefresh.org/";
    private final String SA_IoNos ="http://looRefresh.org/";

    private final String RadiceWS_Locale = VariabiliStaticheVideo.UrlWS + "/";;
    private final String ws_Locale = "newLooVF.asmx/";
    private final String NS_Locale ="http://newLooVF.org/";
    private final String SA_Locale ="http://newLooVF.org/";

    private String TipoOperazione = "";
    private Context context;

    private String NomeImmaginePerCopia;
    private String Base64PerCopia;
    private boolean cicloEliminazione = false;

    public ChiamateWsWPRefresh(Context context) {
        this.context = context;
    }

    public void EliminaImmagineSuSfondiLocale(String Immagine, boolean perCiclo) {
        if (perCiclo) {
            VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                    "Eliminazione immagine in locale " + (VariabiliStaticheRefresh.getInstance().getIndiceEliminazioneLocale() + 1) + "/" +
                            VariabiliStaticheRefresh.getInstance().getImmaginiDaEliminareDaLocale().size() + "\n" +
                            Immagine
            );
        }

        String Urletto="RefreshEliminaImmagine?" +
                "Immagine=" + Immagine;

        TipoOperazione = "EliminaImmagineSuLocale";
        this.cicloEliminazione = perCiclo;

        Esegue(
                RadiceWS_Locale + ws_Locale + Urletto,
                TipoOperazione,
                NS_Locale,
                SA_Locale,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void EliminaImmagineSuIoNos(String Immagine, boolean perCiclo) {
        if (perCiclo) {
            VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                    "Eliminazione immagine su IoNos " + (VariabiliStaticheRefresh.getInstance().getIndiceEliminazioneIoNos() + 1) + "/" +
                            VariabiliStaticheRefresh.getInstance().getImmaginiDaEliminareDaIoNos().size() + "\n" +
                            Immagine
            );
        }

        String Urletto="EliminaImmagine?" +
                "Immagine=" + Immagine;

        TipoOperazione = "EliminaImmagineSuIoNos";
        this.cicloEliminazione = perCiclo;

        Esegue(
                RadiceWS_IoNos + ws_IoNos + Urletto,
                TipoOperazione,
                NS_Locale,
                SA_Locale,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void ScriveImmagineSuSfondiLocale(String Immagine, String Base64) {
        NomeImmaginePerCopia = Immagine;
        Base64PerCopia = Base64;

        String Urletto="RefreshScriveImmagine?" +
                "Immagine=" + Immagine +
                "&Base64=" + Base64;

        TipoOperazione = "ScriveImmagineSuLocale";

        Esegue(
                RadiceWS_Locale + ws_Locale + Urletto,
                TipoOperazione,
                NS_Locale,
                SA_Locale,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void ScriveImmagineDaSfondiIoNosALocale(String Base64) {
        String Immagine = VariabiliStaticheRefresh.getInstance().getImmaginiMancantiInLocale().get(
                VariabiliStaticheRefresh.getInstance().getIndiceCopiaLocale()
        );

        VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                "Scrittura immagine in locale " + (VariabiliStaticheRefresh.getInstance().getIndiceCopiaLocale() + 1) + "/" +
                        VariabiliStaticheRefresh.getInstance().getImmaginiMancantiInLocale().size() + "\n" +
                        Immagine
        );

        String Urletto="RefreshScriveImmagine?" +
                "Immagine=" + Immagine +
                "&Base64=" + Base64;

        TipoOperazione = "ScriveImmagineBase64DaIoNos";

        Esegue(
                RadiceWS_Locale + ws_Locale + Urletto,
                TipoOperazione,
                NS_Locale,
                SA_Locale,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void TornaBase64DaSfondiIoNos() {
        String Immagine = VariabiliStaticheRefresh.getInstance().getImmaginiMancantiInLocale().get(
                VariabiliStaticheRefresh.getInstance().getIndiceCopiaLocale()
        );

        VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                "Lettura immagine da IoNOS " + (VariabiliStaticheRefresh.getInstance().getIndiceCopiaLocale() + 1) + "/" +
                        VariabiliStaticheRefresh.getInstance().getImmaginiMancantiInLocale().size() + "\n" +
                        Immagine
        );

        String Urletto="ConverteImmagineBase64?" +
                "Path=" + Immagine;

        TipoOperazione = "ConverteImmagineBase64DaIoNos";

        Esegue(
                RadiceWS_IoNos + ws_IoNos + Urletto,
                TipoOperazione,
                NS_IoNos,
                SA_IoNos,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void ScriveImmagineSuSfondiIoNos(String Immagine, String Base64) {
        String Urletto="ScriveImmagine?" +
                "Path=" + Immagine +
                "&Base64=" + Base64;

        TipoOperazione = "ScriveImmagineSuIoNos";

        Esegue(
                RadiceWS_IoNos + ws_IoNos + Urletto,
                TipoOperazione,
                NS_IoNos,
                SA_IoNos,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void ScriveImmagineDaSfondiLocaleAIoNos(String Base64) {
        String Immagine = VariabiliStaticheRefresh.getInstance().getImmaginiMancantiSuIoNos().get(
                VariabiliStaticheRefresh.getInstance().getIndiceCopiaIoNos()
        );

        VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                "Scrittura immagine su IoNOS " + (VariabiliStaticheRefresh.getInstance().getIndiceCopiaIoNos() + 1) + "/" +
                        VariabiliStaticheRefresh.getInstance().getImmaginiMancantiSuIoNos().size() + "\n" +
                        Immagine
        );

        String Urletto="ScriveImmagine?" +
                "Path=" + Immagine +
                "&Base64=" + Base64;

        TipoOperazione = "ScriveImmagineBase64DaLocale";

        Esegue(
                RadiceWS_IoNos + ws_IoNos + Urletto,
                TipoOperazione,
                NS_IoNos,
                SA_IoNos,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void TornaBase64DaSfondiLocale() {
        String Immagine = VariabiliStaticheRefresh.getInstance().getImmaginiMancantiSuIoNos().get(
                VariabiliStaticheRefresh.getInstance().getIndiceCopiaIoNos()
        );

        VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                "Lettura immagine da locale " + (VariabiliStaticheRefresh.getInstance().getIndiceCopiaIoNos() + 1) + "/" +
                        VariabiliStaticheRefresh.getInstance().getImmaginiMancantiSuIoNos().size() + "\n" +
                        Immagine
        );

        String Urletto="RefreshRitornaBase64?" +
                "Immagine=" + Immagine;

        TipoOperazione = "ConverteImmagineBase64DaLocale";

        Esegue(
                RadiceWS_Locale + ws_Locale + Urletto,
                TipoOperazione,
                NS_Locale,
                SA_Locale,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void RitornaListaImmaginiSfondiIoNOSPerRefresh() {
        String Urletto="RitornaListaImmagini";

        TipoOperazione = "RitornaListaImmaginiIoNos";

        Esegue(
                RadiceWS_IoNos + ws_IoNos + Urletto,
                TipoOperazione,
                NS_IoNos,
                SA_IoNos,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void RitornaListaImmaginiSfondiLocalePerRefresh() {
        String Urletto="RefreshRitornaListaImmagini";

        TipoOperazione = "RitornaListaImmaginiLocale";

        Esegue(
                RadiceWS_Locale + ws_Locale + Urletto,
                TipoOperazione,
                NS_Locale,
                SA_Locale,
                20000,
                true,
                true,
                false,
                -1);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog, boolean ChiamataDiretta,
                       boolean ControllaTempoEsecuzione, int Stelle) {

        UtilityWallpaper.getInstance().Attesa(true);

        InterrogazioneWSWP i = new InterrogazioneWSWP();
        i.EsegueChiamata(
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                "0", // TimeStampAttuale,
                this
        );
    }

    @Override
    public void TaskCompletionResult(String result) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                UtilityWallpaper.getInstance().Attesa(false);

                switch (TipoOperazione) {
                    case "RitornaListaImmaginiIoNos":
                        fRitornaListaImmaginiIoNos(result);
                        break;
                    case "RitornaListaImmaginiLocale":
                        fRitornaListaImmaginiLocale(result);
                        break;
                    case "ConverteImmagineBase64DaLocale":
                        fConverteImmagineBase64DaLocale(result);
                        break;
                    case "ScriveImmagineBase64DaLocale":
                        fScriveImmagineBase64DaLocale(result);
                        break;
                    case "ConverteImmagineBase64DaIoNos":
                        fConverteImmagineBase64DaIoNos(result);
                        break;
                    case "ScriveImmagineBase64DaIoNos":
                        fScriveImmagineBase64DaIoNos(result);
                        break;
                    case "ScriveImmagineSuLocale":
                        fScriveImmagineSuLocale(result);
                        break;
                    case "ScriveImmagineSuIoNos":
                        fScriveImmagineSuIoNos(result);
                        break;
                    case "EliminaImmagineSuLocale":
                        fEliminaImmagineSuLocale(result);
                        break;
                    case "EliminaImmagineSuIoNos":
                        fEliminaImmagineSuIoNos(result);
                        break;
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    private void fEliminaImmagineSuIoNos(String result) {
        if (result.contains("ERROR")) {
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            if (cicloEliminazione) {
                int n = VariabiliStaticheRefresh.getInstance().getIndiceEliminazioneIoNos();
                n++;
                if (n < VariabiliStaticheRefresh.getInstance().getImmaginiDaEliminareDaIoNos().size() - 1) {
                    VariabiliStaticheRefresh.getInstance().setIndiceEliminazioneIoNos(n);
                    String NomeImmaginePerModifica = VariabiliStaticheRefresh.getInstance().getImmaginiDaEliminareDaIoNos().get(n);

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                            ws.EliminaImmagineSuIoNos(NomeImmaginePerModifica, false);
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 100);
                } else {
                    VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);
                }
            } else {
                UtilitiesGlobali.getInstance().ApreToast(context, "Immagine eliminata");
            }
        }
    }

    private void fEliminaImmagineSuLocale(String result) {
        if (result.contains("ERROR")) {
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            if (cicloEliminazione) {
                int n = VariabiliStaticheRefresh.getInstance().getIndiceEliminazioneLocale();
                n++;
                if (n < VariabiliStaticheRefresh.getInstance().getImmaginiDaEliminareDaLocale().size() - 1) {
                    VariabiliStaticheRefresh.getInstance().setIndiceEliminazioneLocale(n);
                    String NomeImmaginePerModifica = VariabiliStaticheRefresh.getInstance().getImmaginiDaEliminareDaLocale().get(n);

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                            ws.EliminaImmagineSuSfondiLocale(NomeImmaginePerModifica, false);
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 100);
                } else {
                    VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);
                }
            } else {
                UtilitiesGlobali.getInstance().ApreToast(context, "Immagine eliminata");
            }
        }
    }

    private void fScriveImmagineSuIoNos(String result) {
        UtilityPlayer.getInstance().AttesaSI(false);

        if (result.contains("ERROR")) {
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine salvata");
        }
    }

    private void fScriveImmagineSuLocale(String result) {
        if (result.contains("ERROR")) {
            UtilityPlayer.getInstance().AttesaSI(false);

            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                    ws.ScriveImmagineSuSfondiIoNos(NomeImmaginePerCopia, Base64PerCopia);
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }

    private void fScriveImmagineBase64DaIoNos(String result) {
        if (result.contains("ERROR")) {
            VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } // else {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    prossimaImmaginePerCopiaInLocale();
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        // }
    }

    private void fConverteImmagineBase64DaIoNos(String result) {
        if (result.contains("ERROR")) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    prossimaImmaginePerCopiaInLocale();
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        } else {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                    ws.ScriveImmagineDaSfondiIoNosALocale(result);
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }

    private void fScriveImmagineBase64DaLocale(String result) {
        if (result.contains("ERROR")) {
            VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } // else {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    prossimaImmaginePerCopiaSuIoNOS();
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        // }
    }

    private void prossimaImmaginePerCopiaInLocale() {
        int n = VariabiliStaticheRefresh.getInstance().getIndiceCopiaLocale();
        n++;
        if (n < VariabiliStaticheRefresh.getInstance().getImmaginiMancantiInLocale().size()) {
            VariabiliStaticheRefresh.getInstance().setIndiceCopiaLocale(
                    n
            );

            ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
            ws.TornaBase64DaSfondiIoNos();
        } else {
            // Fine elaborazione
            if ((VariabiliStaticheRefresh.getInstance().isAggiornaSoloLocale() && !VariabiliStaticheRefresh.getInstance().isAggiornaSoloIoNos()) ||
                (!VariabiliStaticheRefresh.getInstance().isAggiornaSoloLocale() && VariabiliStaticheRefresh.getInstance().isAggiornaSoloIoNos())) {
                ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                if (VariabiliStaticheRefresh.getInstance().isAggiornaSoloIoNos()) {
                    // Elimino le immagini da IoNos
                    VariabiliStaticheRefresh.getInstance().setIndiceEliminazioneIoNos(0);
                    String NomeImmaginePerModifica = VariabiliStaticheRefresh.getInstance().getImmaginiDaEliminareDaIoNos().get(0);

                    ws.EliminaImmagineSuIoNos(NomeImmaginePerModifica, false);
                }
                if (VariabiliStaticheRefresh.getInstance().isAggiornaSoloLocale()) {
                    // Elimino le immagini da locale
                    VariabiliStaticheRefresh.getInstance().setIndiceEliminazioneLocale(0);
                    String NomeImmaginePerModifica = VariabiliStaticheRefresh.getInstance().getImmaginiDaEliminareDaLocale().get(0);

                    ws.EliminaImmagineSuSfondiLocale(NomeImmaginePerModifica, false);
                }
            } else {
                VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);
            }
        }
    }

    private void prossimaImmaginePerCopiaSuIoNOS() {
        int n = VariabiliStaticheRefresh.getInstance().getIndiceCopiaIoNos();
        n++;
        if (n < VariabiliStaticheRefresh.getInstance().getImmaginiMancantiSuIoNos().size()) {
            VariabiliStaticheRefresh.getInstance().setIndiceCopiaIoNos(
                    n
            );

            ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
            ws.TornaBase64DaSfondiLocale();
        } else {
            if (!VariabiliStaticheRefresh.getInstance().getImmaginiMancantiInLocale().isEmpty()) {
                ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                ws.TornaBase64DaSfondiIoNos();
            } else {
                // Fine elaborazione
                VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);
            }
        }
    }

    private void fConverteImmagineBase64DaLocale(String result) {
        if (result.contains("ERROR")) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    prossimaImmaginePerCopiaSuIoNOS();
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        } else {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                    ws.ScriveImmagineDaSfondiLocaleAIoNos(result);
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }

    private void fRitornaListaImmaginiLocale(String result) {
        if (result.contains("ERROR")) {
            VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            List<StrutturaImmaginiPerRefresh> ImmaginiLocali = new ArrayList<>();
            String[] lista = result.split("§");
            for (String s : lista) {
                String[] Campi = s.split(";");

                StrutturaImmaginiPerRefresh si = new StrutturaImmaginiPerRefresh();
                si.setNomeFile(Campi[0].replace("*PV*", ";").replace("/", "\\"));
                si.setData(Campi[2]);
                si.setDimensione(Campi[1]);

                ImmaginiLocali.add(si);
            }
            VariabiliStaticheRefresh.getInstance().setListaImmaginiLocali(ImmaginiLocali);

            VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                    "Immagini Locali ritornate: " + (VariabiliStaticheRefresh.getInstance().getListaImmaginiIoNOS().size() - 1)
            );

            UtilityWallpaper.getInstance().Attesa(true);

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                            "Check Immagini mancanti su IoNos"
                    );

                    // Controllo immagini locali che mancano su ioNos
                    List<String> immaginiMancantiSuIoNos = new ArrayList<>();
                    List<String> immaginiDaEliminareDaIoNos = new ArrayList<>();
                    if (VariabiliStaticheRefresh.getInstance().isAggiornaSoloIoNos()) {
                        for (StrutturaImmaginiPerRefresh Locale : VariabiliStaticheRefresh.getInstance().getListaImmaginiLocali()) {
                            boolean esiste = false;
                            if (!Locale.getNomeFile().toUpperCase().contains(".RSZ")) {
                                for (StrutturaImmaginiPerRefresh IoNos : VariabiliStaticheRefresh.getInstance().getListaImmaginiIoNOS()) {
                                    if (Locale.getNomeFile().equals(IoNos.getNomeFile())) {
                                        esiste = true;
                                        break;
                                    }
                                }
                                if (!esiste) {
                                    immaginiMancantiSuIoNos.add(Locale.getNomeFile());
                                }
                            }
                        }
                    } // else {

                    if (VariabiliStaticheRefresh.getInstance().isAggiornaSoloIoNos() &&
                        !VariabiliStaticheRefresh.getInstance().isAggiornaSoloLocale()) {
                        // Controllo immagini che devo essere cancellate da IoNos visto che non esistono in locale
                        // però solamente se sto aggiornando solo IoNOS
                        for (StrutturaImmaginiPerRefresh IoNos : VariabiliStaticheRefresh.getInstance().getListaImmaginiIoNOS()) {
                            boolean esiste = false;
                            if (!IoNos.getNomeFile().toUpperCase().contains(".RSZ")) {
                                for (StrutturaImmaginiPerRefresh Locale : VariabiliStaticheRefresh.getInstance().getListaImmaginiLocali()) {
                                    if (IoNos.getNomeFile().equals(Locale.getNomeFile())) {
                                        esiste = true;
                                        break;
                                    }
                                }
                                if (!esiste) {
                                    immaginiDaEliminareDaIoNos.add(IoNos.getNomeFile());
                                }
                            }
                        }
                    }
                    // }
                    VariabiliStaticheRefresh.getInstance().setImmaginiDaEliminareDaIoNos(immaginiDaEliminareDaIoNos);
                    VariabiliStaticheRefresh.getInstance().setImmaginiMancantiSuIoNos(immaginiMancantiSuIoNos);

                    VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                            "Immagini mancanti su IoNos: " + (immaginiMancantiSuIoNos.size() - 1) + "\n" +
                            "Immagini da eliminare da IoNos: " + (immaginiDaEliminareDaIoNos.size() - 1)
                    );

                    // Controllo immagini IoNos che mancano in locale
                    List<String> immaginiMancantiInLocale = new ArrayList<>();
                    List<String> immaginiDaEliminareInLocale = new ArrayList<>();
                    if (VariabiliStaticheRefresh.getInstance().isAggiornaSoloLocale()) {
                        for (StrutturaImmaginiPerRefresh IoNos : VariabiliStaticheRefresh.getInstance().getListaImmaginiIoNOS()) {
                            boolean esiste = false;
                            if (!IoNos.getNomeFile().toUpperCase().contains(".RSZ")) {
                                for (StrutturaImmaginiPerRefresh Locale : VariabiliStaticheRefresh.getInstance().getListaImmaginiLocali()) {
                                    if (IoNos.getNomeFile().equals(Locale.getNomeFile())) {
                                        esiste = true;
                                        break;
                                    }
                                }
                                if (!esiste) {
                                    immaginiMancantiInLocale.add(IoNos.getNomeFile());
                                }
                            }
                        }
                    } // else {

                    if (!VariabiliStaticheRefresh.getInstance().isAggiornaSoloIoNos() &&
                        VariabiliStaticheRefresh.getInstance().isAggiornaSoloLocale()) {
                        // Controllo immagini che devo essere cancellate da locale visto che non esistono su IoNos
                        // però solamente se sto aggiornando solo locale
                        for (StrutturaImmaginiPerRefresh Locale : VariabiliStaticheRefresh.getInstance().getListaImmaginiLocali()) {
                            boolean esiste = false;
                            if (!Locale.getNomeFile().toUpperCase().contains(".RSZ")) {
                                for (StrutturaImmaginiPerRefresh IoNos : VariabiliStaticheRefresh.getInstance().getListaImmaginiIoNOS()) {
                                    if (Locale.getNomeFile().equals(IoNos.getNomeFile())) {
                                        esiste = true;
                                        break;
                                    }
                                }
                                if (!esiste) {
                                    immaginiDaEliminareInLocale.add(Locale.getNomeFile());
                                }
                            }
                        }
                    }
                    // }
                    VariabiliStaticheRefresh.getInstance().setImmaginiDaEliminareDaLocale(immaginiDaEliminareInLocale);
                    VariabiliStaticheRefresh.getInstance().setImmaginiMancantiInLocale(immaginiMancantiInLocale);

                    VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                            "Immagini mancanti in locale: " + (immaginiMancantiInLocale.size() - 1) + "\n" +
                            "Immagini da eliminare in locale: " + (immaginiDaEliminareInLocale.size() - 1)
                    );

                    VariabiliStaticheRefresh.getInstance().setIndiceCopiaLocale(0);
                    VariabiliStaticheRefresh.getInstance().setIndiceCopiaIoNos(0);

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            UtilityWallpaper.getInstance().Attesa(false);

                            if (!VariabiliStaticheRefresh.getInstance().getImmaginiMancantiSuIoNos().isEmpty()) {
                                ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                                ws.TornaBase64DaSfondiLocale();
                            } else {
                                if (!VariabiliStaticheRefresh.getInstance().getImmaginiMancantiInLocale().isEmpty()) {
                                    ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                                    ws.TornaBase64DaSfondiIoNos();
                                } else {
                                    VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);
                                }
                            }
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 1000);
                }
            };
            handlerTimer.postDelayed(rTimer, 1000);
        }
    }

    private void fRitornaListaImmaginiIoNos(String result) {
        if (result.contains("ERROR")) {
            VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setVisibility(LinearLayout.GONE);
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            List<StrutturaImmaginiPerRefresh> ImmaginiSuIONOS = new ArrayList<>();
            String[] lista = result.split("§");
            for (String s : lista) {
                String[] Campi = s.split(";");

                StrutturaImmaginiPerRefresh si = new StrutturaImmaginiPerRefresh();
                si.setNomeFile(Campi[0].replace("*PV*", ";").replace("/", "\\"));
                si.setData(Campi[2]);
                si.setDimensione(Campi[1]);

                ImmaginiSuIONOS.add(si);
            }
            VariabiliStaticheRefresh.getInstance().setListaImmaginiIoNOS(ImmaginiSuIONOS);

            VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                    "Immagini IoNOS ritornate: " + (VariabiliStaticheRefresh.getInstance().getListaImmaginiIoNOS().size() - 1)
            );

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    VariabiliStaticheWallpaper.getInstance().getTxtAvanzamentoRefresh().setText(
                            "Ritorna Immagini locali"
                    );

                    ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                    ws.RitornaListaImmaginiSfondiLocalePerRefresh();
                }
            };
            handlerTimer.postDelayed(rTimer, 1000);
        }
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
    }
}
