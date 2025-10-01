package com.looigi.wallpaperchanger2.Lazio.api_football;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.looigi.wallpaperchanger2.GoogleDrive.GoogleDrive;
import com.looigi.wallpaperchanger2.GoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.Lazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.Lazio.api_football.WebService.wsApiFootball;
import com.looigi.wallpaperchanger2.Lazio.api_football.adapters.AdapterListenerGiocatoriPartitaAF;
import com.looigi.wallpaperchanger2.Lazio.api_football.adapters.AdapterListenerPartiteAF;
import com.looigi.wallpaperchanger2.Lazio.api_football.adapters.AdapterListenerSquadreAF;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Allenatori.Allenatori;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Giocatori.GiocatoriPartita;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Partita.Partita;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Partite.Partite;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Squadre.StrutturaSquadreLega;
import com.looigi.wallpaperchanger2.Lazio.webService.ChiamateWSLazio;
import com.looigi.wallpaperchanger2.Lazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

public class UtilityApiFootball {
    /* private static UtilityApiFootball instance = null;

    private UtilityApiFootball() {
    }

    public static UtilityApiFootball getInstance() {
        if (instance == null) {
            instance = new UtilityApiFootball();
        }

        return instance;
    } */

    private Handler handlerTimerAF;
    private Runnable rTimerAF;
    private ImageView imgLogo;
    private String NomeSquadra;
    private String Cartella;
    private String OperazioneOriginale;

    public void setImg(ImageView imgLogo) {
        this.imgLogo = imgLogo;
    }

    public void setCartella(String Cartella) {
        this.Cartella = Cartella;
    }

    public void setNomeSquadra(String NomeSquadra) {
        this.NomeSquadra = NomeSquadra;
    }

    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;

    public void EffettuaChiamata(Context context, String urlString, String NomeFile, boolean Refresh, String Operazione) {
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        VariabiliStaticheGoogleDrive.getInstance().setPathOperazione(
                "ApiFootball/" +
                        Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                Operazione
        );
        VariabiliStaticheGoogleDrive.getInstance().setNomeFileApiFootball(NomeFile);

        VariabiliStaticheGoogleDrive.getInstance().setOperazioneDaEffettuare("LeggeFile");
        Intent apre = new Intent(context, GoogleDrive.class);
        apre.addCategory(Intent.CATEGORY_LAUNCHER);
        apre.setAction(Intent.ACTION_MAIN );
        apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        context.startActivity(apre);

        VariabiliStaticheGoogleDrive.getInstance().setStaCheckandoFile(true);

        handlerThread = new HandlerThread("background-thread_AttesaLettura_" +
                VariabiliStaticheWallpaper.channelName);
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                if (!VariabiliStaticheGoogleDrive.getInstance().isStaCheckandoFile()) {
                    EffettuaChiamata2(context, urlString, NomeFile, Refresh, Operazione);
                } else {
                    if (handler != null) {
                        handler.postDelayed(this, 100);
                    }
                }
            }
        };
        handler.postDelayed(r, 100);
    }

    private void EffettuaChiamata2(Context context, String urlString, String NomeFile, boolean Refresh, String Operazione) {
        OperazioneOriginale = Operazione;
        String OperazioneRidotta = Operazione; // RitornaOperazioneSistemata(Operazione);

        Files.getInstance().CreaCartelle(
                VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                        Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale() )+ "/" +
                        OperazioneRidotta
        );

        String NomeFileDaLeggere = VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                OperazioneRidotta + "/" +
                NomeFile.replace(" ", "_");

        if (!Refresh && Files.getInstance().EsisteFile(NomeFileDaLeggere)) {
            String Contenuto = Files.getInstance().LeggeFile(
                    VariabiliStaticheApiFootball.getInstance().getPathApiFootball(),
                    Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                            OperazioneRidotta + "/" +
                            NomeFile.replace(" ", "_")
            );
            boolean errore = ControllaErroreSuRitorno(Contenuto, NomeFile, OperazioneRidotta);

            if (!errore) {
                ElaboraChiamata(context, NomeFile, OperazioneRidotta);
            } else {
                EsegueChiamata(context, urlString, NomeFile, OperazioneRidotta);
            }
        } else {
            EsegueChiamata(context, urlString, NomeFile, OperazioneRidotta);
        }
    }

    private boolean ControllaErroreSuRitorno(String Contenuto, String NomeFile, String Operazione) {
        boolean errore = false;
        if (Contenuto.contains("ERROR:")) {
            Files.getInstance().EliminaFileUnico(
                    VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                            Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                            Operazione + "/" +
                            NomeFile.replace(" ", "_")
            );
            return true;
        }
        String errori = Contenuto.substring(Contenuto.indexOf("\"errors\":") + 9);

        if (errori.contains(",")) {
            errori = errori.substring(0, errori.indexOf(","));
            errori = errori.replace("\"", "");
            if (!errori.equals("[]")) {
                Files.getInstance().EliminaFileUnico(
                        VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                                Operazione + "/" +
                                NomeFile.replace(" ", "_")
                );
                errore = true;
            }
        }

        return errore;
    }

    public String RitornaOperazioneSistemata(String Ope) {
        String Operazione = Ope.toLowerCase();

        if (Operazione.equals("giocatoripartita")) {
            Operazione = "GiocatoriPartita";
        } else {
            if (Operazione.contains("_")) {
                String[] O = Operazione.split("_");
                String O2 = "";
                for (String Oo : O) {
                    O2 += UtilitiesGlobali.getInstance().MetteMaiuscole(Oo).trim() + "_";
                }
                Operazione = O2.substring(0, O2.length() - 1).trim();
            } else {
                Operazione = UtilitiesGlobali.getInstance().MetteMaiuscole(Operazione);
            }
        }
        Operazione = Operazione.trim();

        return Operazione;
    }

    private void EsegueChiamata(Context context, String urlString, String NomeFile, String Operazione) {
        VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(true);

        wsApiFootball ws = new wsApiFootball();
        ws.RitornaDati(context, urlString, NomeFile, Operazione);

        handlerTimerAF = new Handler(Looper.getMainLooper());
        rTimerAF = new Runnable() {
            public void run() {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!VariabiliStaticheApiFootball.getInstance().isStaLeggendoWS()) {
                            handlerTimerAF.removeCallbacksAndMessages(rTimerAF);

                            ElaboraChiamata(context, NomeFile, Operazione);
                        } else {
                            handlerTimerAF.postDelayed(rTimerAF, 500);
                        }
                    }
                }, 500);
            }
        };
        handlerTimerAF.postDelayed(rTimerAF, 500);
    }

    private void ElaboraChiamata(Context context, String NomeFile, String Operazione) {
        String Contenuto = Files.getInstance().LeggeFile(
                VariabiliStaticheApiFootball.getInstance().getPathApiFootball(),
                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                        Operazione + "/" +
                        NomeFile.replace(" ", "_")
        );
        boolean errore = ControllaErroreSuRitorno(Contenuto,
                NomeFile.replace(" ", "_"),
                Operazione);

        if (!errore) {
            switch (OperazioneOriginale) {
                case "SQUADRA":
                    gestisceSquadra(context, Contenuto);
                    break;
                case "SQUADRE_LEGA":
                    gestisceSquadreLega(context, Contenuto);
                    break;
                case "PARTITE_SQUADRA":
                    gestiscePartiteSquadra(context, Contenuto);
                    break;
                case "PARTITA":
                    gestiscePartita(context, Contenuto);
                    break;
                case "ALLENATORI":
                    gestisceAllenatori(context, Contenuto);
                    break;
                case "GIOCATORI":
                    gestisceGiocatori(context, Contenuto);
                    break;
            }
        } else {
            VariabiliStaticheApiFootball.getInstance().setStaSalvandoTutteLePartite(false);

            UtilitiesGlobali.getInstance().VisualizzaMessaggio(
                    context,
                    "Api Football",
                    "Errore nel download: " + Operazione + "\n" + Contenuto
                    );
        }

        Files.getInstance().EliminaFileUnico(VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                        Operazione + "/" +
                        NomeFile.replace(" ", "_"));

        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);
    }

    public void AggiornaFileFatti(String NomeSquadra, int Quale, boolean chkFatto) {
        String Contenuto = "";

        if (Files.getInstance().EsisteFile(VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                        Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                NomeSquadra + ".txt")) {
            Contenuto = Files.getInstance().LeggeFile(
                    VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                            Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/",
                    NomeSquadra + ".txt");
        }

        if (Contenuto.isEmpty()) {
            for (int ii = 0; ii < VariabiliStaticheApiFootball.getInstance().getQuantePartite(); ii++) {
                Contenuto += "N";
            }
        }
        if (!chkFatto) {
            Contenuto = Contenuto.substring(0, Quale) + "N" + Contenuto.substring(Quale + 1);
        } else {
            Contenuto = Contenuto.substring(0, Quale) + "S" + Contenuto.substring(Quale + 1);
        }
        Files.getInstance().EliminaFileUnico(VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                        Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                NomeSquadra + ".txt");
        Files.getInstance().ScriveFile(VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                        Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/",
                NomeSquadra + ".txt",
                Contenuto);


        VariabiliStaticheApiFootball.getInstance().getCustomAdapterPartiteAF().notifyDataSetChanged();
    }

    public boolean RitornaFileFatti(String NomeSquadra, int Quale) {
        String Contenuto = "";
        if (Files.getInstance().EsisteFile(VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                        Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                NomeSquadra + ".txt")) {
            Contenuto = Files.getInstance().LeggeFile(
                    VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                            Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/",
                    NomeSquadra + ".txt");
            if (Contenuto.isEmpty()) {
                return false;
            } else {
                String c = Contenuto.substring(Quale, Quale + 1);
                if (c.equals("S")) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private void gestisceSquadra(Context context, String Contenuto) {
        Gson gson = new Gson();
        if (!Contenuto.contains("ERROR")) {
            StrutturaSquadreLega s = gson.fromJson(Contenuto, StrutturaSquadreLega.class);
            String Url = s.response.get(0).team.logo;
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgLogo, Url, NomeSquadra + ".png", Cartella);
        } else {
            UtilitiesGlobali.getInstance().VisualizzaMessaggio(
                    context,
                    "Api Football",
                    "Errore nel download: Squadra\n" + Contenuto
            );
        }
    }

    private void gestisceSquadreLega(Context context, String Contenuto) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                VariabiliStaticheApiFootball.getInstance().setListaSquadreAnno(
                        gson.fromJson(Contenuto, StrutturaSquadreLega.class)
                );

                AdapterListenerSquadreAF customAdapterT = new AdapterListenerSquadreAF(
                        context,
                        VariabiliStaticheApiFootball.getInstance().getListaSquadreAnno().response
                );
                VariabiliStaticheApiFootball.getInstance().getLstSquadre().setAdapter(
                        customAdapterT
                );
            }
        }, 100);
    }

    private void gestiscePartiteSquadra(Context context, String Contenuto) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                Partite p = gson.fromJson(Contenuto, Partite.class);
                VariabiliStaticheApiFootball.getInstance().setPartiteSquadra(p);

                AdapterListenerPartiteAF customAdapterT = new AdapterListenerPartiteAF(
                        context,
                        p.response
                );
                VariabiliStaticheApiFootball.getInstance().setCustomAdapterPartiteAF(
                        customAdapterT
                );
                VariabiliStaticheApiFootball.getInstance().getLstPartite().setAdapter(
                        customAdapterT
                );

                // Prende Allenatori Squadra scelta
                /* String urlString = "https://v3.football.api-sports.io/coachs?" +
                        "team=" + VariabiliStaticheApiFootball.getInstance().getIdSquadra();
                EffettuaChiamata(
                        context,
                        urlString,
                        "Allenatori_" + VariabiliStaticheApiFootball.getInstance().getIdSquadra() + ".json",
                        false,
                        "ALLENATORI"
                ); */

                VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);

                // UtilitiesGlobali.getInstance().ApreToast(context, "Partite scaricate");
            }
        }, 100);
    }

    private void gestiscePartita(Context context, String Contenuto) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                Partita p = gson.fromJson(Contenuto, Partita.class);
                VariabiliStaticheApiFootball.getInstance().setPartitaSelezionata(p);

                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.ControllaSeEsistePartita(p);
            }
        }, 100);
    }

    private void gestisceAllenatori(Context context, String Contenuto) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                if (Contenuto != null && !Contenuto.isEmpty()) {
                    Allenatori p = gson.fromJson(Contenuto, Allenatori.class);
                    VariabiliStaticheApiFootball.getInstance().setAllenatoriSquadraScelta(p);

                    ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
                    ws1.AggiungeTuttiGliAllenatori();
                }

                VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);

        /* if (VariabiliStaticheApiFootball.getInstance().isStaSalvandoTutteLeSquadre()) {
            ChiamateWSLazio ws = new ChiamateWSLazio(context);
            ws.AggiungeSquadra(
                    VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato(),
                    VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta()
            );
        } else { */
                // UtilitiesGlobali.getInstance().ApreToast(context, "Allenatori squadra scelta scaricati");
                // }
            }
        }, 100);
    }

    public void CaricaPartita(Context context, int idPartita) {
        String urlString = "https://v3.football.api-sports.io/fixtures?" +
                "id=" + idPartita;
        EffettuaChiamata(
                context,
                urlString,
                "Partita_" + VariabiliStaticheApiFootball.getInstance().getIdSquadra() + "_" + idPartita + ".json",
                false,
                "PARTITA"
        );
    }

    private void gestisceGiocatori(Context context, String Contenuto) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                GiocatoriPartita p = gson.fromJson(Contenuto, GiocatoriPartita.class);
                VariabiliStaticheApiFootball.getInstance().setGiocatoriDellaPartita(p);

                if (p != null && p.response != null && !p.response.isEmpty()) {
                    AdapterListenerGiocatoriPartitaAF customAdapterTC = new AdapterListenerGiocatoriPartitaAF(
                            context,
                            p.response.get(0).players
                    );
                    VariabiliStaticheApiFootball.getInstance().getLstGiocatoriCasa().setAdapter(
                            customAdapterTC
                    );
                } else {
                    VariabiliStaticheApiFootball.getInstance().getLstGiocatoriCasa().setAdapter(
                            null
                    );
                }

                if (p != null && p.response != null && !p.response.isEmpty() && p.response.size() > 1) {
                    AdapterListenerGiocatoriPartitaAF customAdapterTF = new AdapterListenerGiocatoriPartitaAF(
                            context,
                            p.response.get(1).players
                    );
                    VariabiliStaticheApiFootball.getInstance().getLstGiocatoriFuori().setAdapter(
                            customAdapterTF
                    );
                } else {
                    VariabiliStaticheApiFootball.getInstance().getLstGiocatoriFuori().setAdapter(
                            null
                    );
                }

                if (VariabiliStaticheApiFootball.getInstance().isStaSalvandoPartita()) {
                    ChiamateWSLazio ws = new ChiamateWSLazio(context);
                    ws.AggiungeModificaCalendario("");
                } else {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Giocatori partita scaricati");
                }
            }
        }, 100);
    }

    public void SalvaTutteLeSquadre(Context context) {
        String NomeSquadra = VariabiliStaticheApiFootball.getInstance().getListaSquadreAnno().response.get(
                VariabiliStaticheApiFootball.getInstance().getIndiceSalvataggioTutteLeSquadre()
        ).team.name;
        VariabiliStaticheApiFootball.getInstance().setNomeSquadraScelta(NomeSquadra);

        int idSquadra = VariabiliStaticheApiFootball.getInstance().getListaSquadreAnno().response.get(
                VariabiliStaticheApiFootball.getInstance().getIndiceSalvataggioTutteLeSquadre()
        ).team.id;
        VariabiliStaticheApiFootball.getInstance().setIdSquadra(idSquadra);

        // Prende Allenatori Squadra scelta
        /* String urlString = "https://v3.football.api-sports.io/coachs?" +
                "team=" + idSquadra;
        EffettuaChiamata(
                context,
                urlString,
                "Allenatori_" + VariabiliStaticheApiFootball.getInstance().getIdSquadra() + ".json",
                false,
                "ALLENATORI"
        ); */

        ChiamateWSLazio ws = new ChiamateWSLazio(context);
        ws.AggiungeSquadra(
                VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato(),
                VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta(),
                1
        );
    }

    public void SalvaTutteLePartite(Context context) {
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);
        // VariabiliStaticheApiFootball.getInstance().ScriveAvanzamento("Inizio salvataggio partite");

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);

                int idPartita = VariabiliStaticheApiFootball.getInstance().getPartiteSquadra().response.get(
                        VariabiliStaticheApiFootball.getInstance().getIndiceSalvataggioTutteLePartite()
                ).fixture.id;

                VariabiliStaticheApiFootball.getInstance().setStaSalvandoPartita(true);

                VariabiliStaticheApiFootball.getInstance().setIdPartita(idPartita);
                VariabiliStaticheApiFootball.getInstance().setIdPartitaDaSalvare(
                        VariabiliStaticheApiFootball.getInstance().getIndiceSalvataggioTutteLePartite()
                );
                CaricaPartita(context, idPartita);
            };
        };
        handlerTimer.postDelayed(rTimer, 10000);
    }
}
