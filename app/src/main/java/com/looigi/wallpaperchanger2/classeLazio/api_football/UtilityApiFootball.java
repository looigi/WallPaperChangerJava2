package com.looigi.wallpaperchanger2.classeLazio.api_football;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.looigi.wallpaperchanger2.classeLazio.api_football.WebService.wsApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.adapters.AdapterListenerGiocatoriPartitaAF;
import com.looigi.wallpaperchanger2.classeLazio.api_football.adapters.AdapterListenerPartiteAF;
import com.looigi.wallpaperchanger2.classeLazio.api_football.adapters.AdapterListenerSquadreAF;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Allenatori.Allenatori;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.GiocatoriPartita;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partita.Partita;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partite.Partite;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Squadre.StrutturaSquadreLega;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class UtilityApiFootball {
    private static UtilityApiFootball instance = null;
    private Handler handlerTimerAF;
    private Runnable rTimerAF;

    private UtilityApiFootball() {
    }

    public static UtilityApiFootball getInstance() {
        if (instance == null) {
            instance = new UtilityApiFootball();
        }

        return instance;
    }

    public void ImpostaAttesa(boolean Come) {
        if (Come) {
            VariabiliStaticheApiFootball.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
        } else {
            VariabiliStaticheApiFootball.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        }
    }

    public void EffettuaChiamata(Context context, String urlString, String NomeFile, boolean Refresh, String Operazione) {
        UtilityApiFootball.getInstance().ImpostaAttesa(true);

        Files.getInstance().CreaCartelle(
                VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                        VariabiliStaticheApiFootball.getInstance().getAnnoScelto()
        );

        if (!Refresh && Files.getInstance().EsisteFile(
                VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/" +
                        VariabiliStaticheApiFootball.getInstance().getAnnoScelto() + "/" +
                        NomeFile)
        ) {
            ElaboraChiamata(context, NomeFile, Operazione);
        } else {
            VariabiliStaticheApiFootball.getInstance().setStaLeggendoWS(true);

            wsApiFootball ws = new wsApiFootball();
            ws.RitornaDati(context, urlString, NomeFile);

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
    }

    private void ElaboraChiamata(Context context, String NomeFile, String Operazione) {
        String Contenuto = Files.getInstance().LeggeFile(
                VariabiliStaticheApiFootball.getInstance().getPathApiFootball(),
                VariabiliStaticheApiFootball.getInstance().getAnnoScelto() + "/" + NomeFile
        );

        switch (Operazione) {
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

        UtilityApiFootball.getInstance().ImpostaAttesa(false);
    }

    private void gestisceSquadreLega(Context context, String Contenuto) {
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

    private void gestiscePartiteSquadra(Context context, String Contenuto) {
        Gson gson = new Gson();
        Partite p = gson.fromJson(Contenuto, Partite.class);
        VariabiliStaticheApiFootball.getInstance().setPartiteSquadra(p);

        AdapterListenerPartiteAF customAdapterT = new AdapterListenerPartiteAF(
                context,
                p.response
        );
        VariabiliStaticheApiFootball.getInstance().getLstPartite().setAdapter(
                customAdapterT
        );

        // Prende Allenatori Squadra scelta
        String urlString = "https://v3.football.api-sports.io/coachs?" +
                "team=" + VariabiliStaticheApiFootball.getInstance().getIdSquadra();
        UtilityApiFootball.getInstance().EffettuaChiamata(
                context,
                urlString,
                "Allenatori_" + VariabiliStaticheApiFootball.getInstance().getIdSquadra() + ".json",
                false,
                "ALLENATORI"
        );

        UtilitiesGlobali.getInstance().ApreToast(context, "Partite scaricate");
    }

    private void gestiscePartita(Context context, String Contenuto) {
        Gson gson = new Gson();
        Partita p = gson.fromJson(Contenuto, Partita.class);
        VariabiliStaticheApiFootball.getInstance().setPartitaSelezionata(p);

        // Caricamento Giocatori partita
        String urlString = "https://v3.football.api-sports.io/fixtures/players?" +
                "fixture=" + VariabiliStaticheApiFootball.getInstance().getIdPartita(); // + "&" +
                // "team=" + VariabiliStaticheApiFootball.getInstance().getIdSquadra();
        UtilityApiFootball.getInstance().EffettuaChiamata(
                context,
                urlString,
                "GiocatoriPartita_" + VariabiliStaticheApiFootball.getInstance().getIdSquadra() + "_" +
                        VariabiliStaticheApiFootball.getInstance().getIdPartita() + ".json",
                false,
                "GIOCATORI"
        );

        UtilitiesGlobali.getInstance().ApreToast(context, "Dettaglio Partita scaricata");
    }

    private void gestisceAllenatori(Context context, String Contenuto) {
        Gson gson = new Gson();
        Allenatori p = gson.fromJson(Contenuto, Allenatori.class);
        VariabiliStaticheApiFootball.getInstance().setAllenatoriSquadraScelta(p);

        UtilitiesGlobali.getInstance().ApreToast(context, "Allenatori squadra scelta scaricati");
    }

    private void gestisceGiocatori(Context context, String Contenuto) {
        Gson gson = new Gson();
        GiocatoriPartita p = gson.fromJson(Contenuto, GiocatoriPartita.class);

        AdapterListenerGiocatoriPartitaAF customAdapterTC = new AdapterListenerGiocatoriPartitaAF(
                context,
                p.response.get(0).players
        );
        VariabiliStaticheApiFootball.getInstance().getLstGiocatoriCasa().setAdapter(
                customAdapterTC
        );

        AdapterListenerGiocatoriPartitaAF customAdapterTF = new AdapterListenerGiocatoriPartitaAF(
                context,
                p.response.get(1).players
        );
        VariabiliStaticheApiFootball.getInstance().getLstGiocatoriFuori().setAdapter(
                customAdapterTF
        );

        UtilitiesGlobali.getInstance().ApreToast(context, "Giocatori partita scaricati");
    }
}
