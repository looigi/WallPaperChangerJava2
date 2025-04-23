package com.looigi.wallpaperchanger2.classeLazio.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.VariabiliStaticheLazioDettaglio;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaAllenatori;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaAnni;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCalendario;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaClassifica;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCompetizioni;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaFonti;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaGiocatori;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaMarcatori;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaMercato;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaRuoli;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaSquadre;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaStati;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerAllenatori;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerCalendario;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerClassifica;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerFonti;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerGiocatori;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerMarcatori;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerMercato;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerRuoli;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerSquadre;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerStati;
import com.looigi.wallpaperchanger2.classeLazio.api_football.UtilityApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Allenatori.Allenatori;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Allenatori.Coach;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.Cards;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.Games;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.GiocatoriPartita;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.Goals;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.PlayerStatistics;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.Statistics;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partite.FixtureData;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Squadre.TeamResponse;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSLazio implements TaskDelegateLazio {
    private static final String NomeMaschera = "Chiamate_WS_LAZIO";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheLazio.UrlWS;
    private final String ws = "wsLazio.asmx/";
    private final String NS="http://looLazio.it/";
    private final String SA="http://looLazio.it/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;

    public ChiamateWSLazio(Context context) {
        this.context = context;
    }

    public void GestioneMercato() {
        String Data = VariabiliStaticheLazio.getInstance().getEdtData().getText().toString();
        String Nominativo = VariabiliStaticheLazio.getInstance().getEdtNominativo().getText().toString();
        String idStato = String.valueOf(VariabiliStaticheLazio.getInstance().getIdStato());
        String idFonte = String.valueOf(VariabiliStaticheLazio.getInstance().getIdFonte());
        String Progressivo = String.valueOf(VariabiliStaticheLazio.getInstance().getIdPerOperazione());

        if (Data.isEmpty()) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Inserire la data");
            return;
        }
        if (Nominativo.isEmpty()) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Inserire il nominativo");
            return;
        } else {
            if (Progressivo.equals("-1")) {
                for (StrutturaMercato s : VariabiliStaticheLazio.getInstance().getMercato()) {
                    if (s.getNominativo().toUpperCase().trim().equals(Nominativo.toUpperCase().trim())) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "Nominativo già inserito per la data " + s.getData());
                        return;
                    }
                }
            }
        }
        if (idStato.equals("0")) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Inserire lo stato");
            return;
        }
        if (idFonte.equals("0")) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Inserire la fonte");
            return;
        }

        Nominativo = UtilitiesGlobali.getInstance().MetteMaiuscole(Nominativo);

        String Urletto="GestioneMercato?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idModalita=" + VariabiliStaticheLazio.getInstance().getModalitaMercato() +
                "&idAcqCes=" + VariabiliStaticheLazio.getInstance().getAcquistiCessioni() +
                "&Progressivo=" + Progressivo +
                "&Nominativo=" + Nominativo +
                "&Data=" + Data +
                "&idStato=" + idStato +
                "&idFonte=" + idFonte;

        TipoOperazione = "GestioneMercato";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void GestioneAnno() {
        String descAnno = VariabiliStaticheLazio.getInstance().getEdtDescrizioneAnno().getText().toString();
        String punti = VariabiliStaticheLazio.getInstance().getEdtPuntiPerVittoria().getText().toString();

        if (descAnno.isEmpty()) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Inserire la descrizione dell'anno");
            return;
        }
        if (punti.isEmpty()) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Inserire i punti per la vittoria");
            return;
        }

        String idAnno = "";
        if (VariabiliStaticheLazio.getInstance().getIdAnnoPerModifica() > -1) {
            idAnno = String.valueOf(VariabiliStaticheLazio.getInstance().getIdAnnoPerModifica());
        }

        String Urletto="AggiungeModificaAnno?" +
                "Descrizione=" + descAnno +
                "&idAnno=" + idAnno +
                "&PuntiVittoria=" + punti +
                "&SquadreScudetto=1" +
                "&SquadreCL=4" +
                "&SquadrePCL=2" +
                "&SquadreEL=2"  +
                "&SquadreRetrocessioni=4";

        TipoOperazione = "AggiungeModificaAnno";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void ScriveRisultati() {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        String Urletto="ScriveRisultati?" +
            "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato();

        TipoOperazione = "ScriveRisultati";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                300000,
                ApriDialog);
    }

    public void AggiungeModificaGiocatore() {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        String Cognome = VariabiliStaticheLazio.getInstance().getEdtCognome().getText().toString();
        String Nome = VariabiliStaticheLazio.getInstance().getEdtNome().getText().toString();
        String idRuolo = String.valueOf(VariabiliStaticheLazio.getInstance().getIdRuoloSelezionato());

        String Urletto="AggiungeModificaGiocatore?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() + "&" +
                "Cognome=" + Cognome + "&" +
                "Nome=" + Nome + "&" +
                "idRuolo=" + idRuolo + "&" +
                "idGiocatore=" + VariabiliStaticheLazio.getInstance().getIdGiocatorePerModifica() + "&" +
                "idGiocApiFootball=" + VariabiliStaticheLazio.getInstance().getIdApiFootballPerModifica() + "&";

        TipoOperazione = "AggiungeModificaGiocatore";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void EliminaMercato() {
        String Progressivo = String.valueOf(VariabiliStaticheLazio.getInstance().getIdPerOperazione());

        String Urletto="EliminaMercato?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idModalita=" + VariabiliStaticheLazio.getInstance().getModalitaMercato() +
                "&idAcqCess=" + VariabiliStaticheLazio.getInstance().getAcquistiCessioni() +
                "&Progressivo=" + Progressivo;

        TipoOperazione = "EliminaMercato";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaMarcatori() {
        String Urletto="RitornaMarcatori?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idTipologia=" + VariabiliStaticheLazio.getInstance().getIdTipologia();

        TipoOperazione = "RitornaMarcatori";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaAllenatori() {
        String Urletto="RitornaAllenatori?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idSquadra=" + VariabiliStaticheLazio.getInstance().getIdSquadraPerAll();

        TipoOperazione = "RitornaAllenatori";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaGiocatoriPerSalvataggio(int Dove) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        DovePerSalvataggioGiocatori = Dove;

        GiocatoriPartita g = VariabiliStaticheApiFootball.getInstance().getGiocatoriDellaPartita();
        if (!g.response.isEmpty() && g.response.get(Dove) != null) {
            String Squadra = g.response.get(Dove).team.name;
            // Squadra = Squadra.replace("AC ", "").replace("AS ", "")
            //         .replace("Ac ", "").replace("As ", "");
            int idSquadra = -1;
            for (StrutturaSquadre s : VariabiliStaticheLazio.getInstance().getSquadre()) {
                String Confronto = s.getSquadra(); // .replace("AC ", "").replace("AS ", "")
                        // .replace("Ac ", "").replace("As ", "");
                if (Confronto.equals(Squadra)) {
                    idSquadra = s.getIdSquadra();
                    break;
                }
            }
            if (idSquadra == -1) {
                if (DovePerSalvataggioGiocatori == 0) {
                    VariabiliStaticheApiFootball.getInstance().setGiocatoriCasaPS(null);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ChiamateWSLazio ws = new ChiamateWSLazio(context);
                            ws.RitornaGiocatoriPerSalvataggio(1);
                        }
                    }, 500);
                } else {
                    VariabiliStaticheApiFootball.getInstance().setGiocatoriFuoriPS(null);

                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ChiamateWSLazio ws = new ChiamateWSLazio(context);
                            ws.SalvaDettaglio();
                        }
                    }, 500);
                }
            } else {
                String Urletto="RitornaGiocatori?" +
                        "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                        "&idSquadra=" + idSquadra;

                TipoOperazione = "RitornaGiocatoriPS";

                Esegue(
                        RadiceWS + ws + Urletto,
                        TipoOperazione,
                        NS,
                        SA,
                        10000,
                        ApriDialog);
            }
        } else {
            if (DovePerSalvataggioGiocatori == 0) {
                VariabiliStaticheApiFootball.getInstance().setGiocatoriCasaPS(null);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.RitornaGiocatoriPerSalvataggio(1);
                    }
                }, 500);
            } else {
                VariabiliStaticheApiFootball.getInstance().setGiocatoriFuoriPS(null);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.SalvaDettaglio();
                    }
                }, 500);
            }
        }
    }

    public void RitornaGiocatori() {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaGiocatori?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idSquadra=" + VariabiliStaticheLazio.getInstance().getIdSquadraPerGioc();

        TipoOperazione = "RitornaGiocatori";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaRuoli(boolean RefreshDati) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Ruoli.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    UtilityLazio.getInstance().ImpostaAttesa(false);
                    VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);

                    fRitornaRuoli(Dati);
                    return;
                }
            }
        }

        String Urletto="RitornaRuoli";

        TipoOperazione = "RitornaRuoli";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaStati(boolean RefreshDati) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Stati.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    fRitornaStati(Dati);

                    UtilityLazio.getInstance().ImpostaAttesa(false);
                    VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);
                    return;
                }
            }
        }

        String Urletto="RitornaStati";

        TipoOperazione = "RitornaStati";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaFonti(boolean RefreshDati) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Fonti.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    fRitornaFonti(Dati);

                    UtilityLazio.getInstance().ImpostaAttesa(false);
                    VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);
                    return;
                }
            }
        }

        String Urletto="RitornaFonti";

        TipoOperazione = "RitornaFonti";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaMercato() {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaMercato?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idModalita=" + VariabiliStaticheLazio.getInstance().getModalitaMercato() +
                "&idAcqCess=" + VariabiliStaticheLazio.getInstance().getAcquistiCessioni();

        TipoOperazione = "RitornaMercato";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaCalendario() {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaCalendario?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idTipologia=" + VariabiliStaticheLazio.getInstance().getIdTipologia() +
                "&idGiornata=" + VariabiliStaticheLazio.getInstance().getGiornata();

        TipoOperazione = "RitornaCalendario";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaClassifica() {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaClassifica?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idTipologia=" + VariabiliStaticheLazio.getInstance().getIdTipologia() +
                "&idGiornata=" + VariabiliStaticheLazio.getInstance().getGiornata();

        TipoOperazione = "RitornaClassifica";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void AggiungeTuttiGliAllenatori() {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        Allenatori allenatori = VariabiliStaticheApiFootball.getInstance().getAllenatoriSquadraScelta();
        String Cognomi = "";
        String Nomi = "";
        for (Coach s: allenatori.response) {
            Cognomi += s.lastname + ";";
            Nomi += s.firstname + ";";
        }

        if (VariabiliStaticheLazio.getInstance().getSquadre() == null) {
            List<TeamResponse> listaTeam = VariabiliStaticheApiFootball.getInstance().getListaSquadreAnno().response;
            String[] righePerSpinner = new String[listaTeam.size() + 1];
            righePerSpinner[0] = "";
            int i = 1;
            List<StrutturaSquadre> lista = new ArrayList<>();
            for (TeamResponse t: listaTeam) {
                StrutturaSquadre sq = new StrutturaSquadre();
                sq.setIdSquadra(i);
                sq.setSquadra(t.team.name);

                righePerSpinner[i] = t.team.name;
                i++;

                lista.add(sq);
            }

            int giornate = (lista.size() - 1) * 2;
            VariabiliStaticheLazio.getInstance().setMaxGiornate(giornate);
            VariabiliStaticheLazio.getInstance().setGiornata(giornate);

            VariabiliStaticheLazio.getInstance().setRighePerSquadre(righePerSpinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item, righePerSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            VariabiliStaticheLazio.getInstance().getSpnSquadreGioc().setAdapter(adapter);
            VariabiliStaticheLazio.getInstance().getSpnSquadreAll().setAdapter(adapter);

            VariabiliStaticheLazio.getInstance().setSquadre(lista);

            AdapterListenerSquadre cstmAdptSquadre = new AdapterListenerSquadre(context, lista);
            VariabiliStaticheLazio.getInstance().getLstSquadre().setAdapter(cstmAdptSquadre);
        }

        int idSquadra = -1;
        for (StrutturaSquadre ss: VariabiliStaticheLazio.getInstance().getSquadre()) {
            String NomeSquadra1 = (ss.getSquadra().toUpperCase());
                    // .replace("AC ", "").replace("AS ", "")).trim();
            String NomeSquadra2 = (VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta().toUpperCase());
                    // .replace("AC ", "").replace("AS ", "")).trim();
            if (NomeSquadra1.equals(NomeSquadra2)) {
                idSquadra = ss.getIdSquadra();
                break;
            }
        }
        if (idSquadra > -1) {
            String Urletto = "AggiungeTuttiGliAllenatori?" +
                    "idAnno=" + VariabiliStaticheApiFootball.getInstance().getIdAnnoScelto() +
                    "&idSquadra=" + idSquadra +
                    "&Cognomi=" + Cognomi +
                    "&Nomi=" + Nomi;

            TipoOperazione = "AggiungeTuttiGliAllenatori";

            /* Esegue(
                    RadiceWS + ws + Urletto,
                    TipoOperazione,
                    NS,
                    SA,
                    10000,
                    ApriDialog); */
        } else {
            UtilityLazio.getInstance().ImpostaAttesa(false);
            VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);

            UtilitiesGlobali.getInstance().ApreToast(context, "Squadra non rilevata: " +
                    VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta());
        }
    }

    public void RitornaSquadre() {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaSquadre?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idTipologia=" + VariabiliStaticheLazio.getInstance().getIdTipologia();

        TipoOperazione = "RitornaSquadre";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaCompetizioni(boolean RefreshDati) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Competizioni.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    fRitornaCompetizioni(Dati);

                    UtilityLazio.getInstance().ImpostaAttesa(false);
                    VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);
                    return;
                }
            }
        }

        String Urletto="RitornaCompetizioni";

        TipoOperazione = "RitornaCompetizioni";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaIdAnno(int Anno) {
        String ricAnno = Integer.toString(Anno).trim() + "-" + Integer.toString(Anno).trim();

        String Urletto="RitornaIdAnno?Anno=" +ricAnno;

        TipoOperazione = "RitornaIdAnno";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void AggiungeSquadra(int Anno, String Squadra, int idTipologia) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        String NomeSquadra = Squadra; // .replace("AC ", "").replace("AS ", "")
                // .replace("Ac ", "").replace("As ", "");

        String Urletto="AggiungeSquadra?" +
            "idAnno=" + Integer.toString(Anno).trim() +
            "&Squadra=" + NomeSquadra +
            "&idTipologia=" + idTipologia + // Tipologia 1 = Campionato serie A
            "&idSquadra=";

        TipoOperazione = "AggiungeSquadra";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaAnni(boolean RefreshDati) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Anni.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    fRitornaAnni(Dati);

                    UtilityLazio.getInstance().ImpostaAttesa(false);
                    VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);
                    return;
                }
            }
        }

        String Urletto="RitornaAnni";

        TipoOperazione = "RitornaAnni";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void AggiungeModificaCalendario(String idPartita) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        FixtureData datiPartita = VariabiliStaticheApiFootball.getInstance().getPartiteSquadra().response.get(
                VariabiliStaticheApiFootball.getInstance().getIdPartitaDaSalvare()
        );

        String Casa = datiPartita.teams.home.name;
        String Fuori = datiPartita.teams.away.name;

        String d = datiPartita.fixture.date;
        String[] data = d.split("T");
        String[] o = data[1].split("\\+");
        String ora = o[0];
        String DataPartita = data[0] + " " + ora;

        String TipoPartita = datiPartita.league.round;
        String NomeCompetizione = datiPartita.league.name;
        String[] t = TipoPartita.split("-");
        String Tipo = t[0].trim().toUpperCase();
        String idTipologia = RitornaTipologiaPartita(NomeCompetizione, Tipo);

        /* if (!Casa.toUpperCase().trim().equals("LAZIO") && !Fuori.toUpperCase().trim().equals("LAZIO")) {
            VariabiliStaticheApiFootball.getInstance().setIdPartitaSalvata(-1);
            VariabiliStaticheApiFootball.getInstance().setSquadraDiCasaDaSalvare(Casa);
            VariabiliStaticheApiFootball.getInstance().setSquadraFuoriDaSalvare(Fuori);
            VariabiliStaticheApiFootball.getInstance().setIdTipologiaDaSalvare(Integer.parseInt(idTipologia));
            // UtilitiesGlobali.getInstance().ApreToast(context, "Partita inserita/modificata");

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    VariabiliStaticheApiFootball.getInstance().setSquadraCheStaSalvando(0);
                    ChiamateWSLazio ws = new ChiamateWSLazio(context);
                    ws.AggiungeSquadra(
                            VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato(),
                            VariabiliStaticheApiFootball.getInstance().getSquadraDiCasaDaSalvare(),
                            VariabiliStaticheApiFootball.getInstance().getIdTipologiaDaSalvare()
                    );
                };
            };
            handlerTimer.postDelayed(rTimer, 100);
        } else { */
            int Giornata = 0;
            try {
                Giornata = Integer.parseInt(t[1].trim());
            } catch (Exception ignored) {

            }

            int idCasa = datiPartita.teams.home.id;
            int idFuori = datiPartita.teams.away.id;

            int goalCasa = 0;
            if (datiPartita.score.fulltime.home != null) {
                goalCasa = datiPartita.score.fulltime.home;
            }
            int goalFuori = 0;
            if (datiPartita.score.fulltime.away != null) {
                goalFuori = datiPartita.score.fulltime.away;
            }

            VariabiliStaticheApiFootball.getInstance().setSquadraDiCasaDaSalvare(datiPartita.teams.home.name);
            VariabiliStaticheApiFootball.getInstance().setSquadraFuoriDaSalvare(datiPartita.teams.away.name);
            VariabiliStaticheApiFootball.getInstance().setIdTipologiaDaSalvare(Integer.parseInt(idTipologia));

            String Urletto = "AggiungeModificaCalendario?" +
                    "idAnno=" + VariabiliStaticheApiFootball.getInstance().getIdAnnoScelto() +
                    "&idTipologia=" + idTipologia +
                    "&idTipologia1=" + idTipologia +
                    "&idTipologia2=" + idTipologia +
                    "&idGiornata=" + Giornata +
                    "&DataPartita=" + DataPartita +
                    "&idSquadraCasa=" + idCasa +
                    "&idSquadraFuori=" + idFuori +
                    "&Risultato1=" + goalCasa +
                    "&Risultato2=" + goalFuori +
                    "&idPartita=" + idPartita +
                    "&SquadraCasa=" + Casa +
                    "&SquadraFuori=" + Fuori;

            TipoOperazione = "AggiungeModificaCalendario";

            Esegue(
                    RadiceWS + ws + Urletto,
                    TipoOperazione,
                    NS,
                    SA,
                    10000,
                    ApriDialog);
        // }
    }

    private int DovePerSalvataggioGiocatori;

    public void AggiungeGiocatori(int Dove) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        DovePerSalvataggioGiocatori = Dove;

        GiocatoriPartita g = VariabiliStaticheApiFootball.getInstance().getGiocatoriDellaPartita();
        if (!g.response.isEmpty() && g.response.get(Dove) != null) {
            String Squadra = g.response.get(Dove).team.name;
            // Squadra = Squadra.replace("AC ", "").replace("AS ", "")
            //         .replace("Ac ", "").replace("As ", "");
            int idSquadra = -1;
            for (StrutturaSquadre s : VariabiliStaticheLazio.getInstance().getSquadre()) {
                String Confronto = s.getSquadra(); // .replace("AC ", "").replace("AS ", "")
                // .replace("Ac ", "").replace("As ", "");
                if (Confronto.equals(Squadra)) {
                    idSquadra = s.getIdSquadra();
                    break;
                }
            }
            if (idSquadra == -1) {
                if (Dove == 0) {
                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            ChiamateWSLazio ws = new ChiamateWSLazio(context);
                            ws.AggiungeGiocatori(1);
                        };
                    };
                    handlerTimer.postDelayed(rTimer, 100);
                } else {
                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            ChiamateWSLazio ws = new ChiamateWSLazio(context);
                            ws.RitornaGiocatoriPerSalvataggio(0);
                        };
                    };
                    handlerTimer.postDelayed(rTimer, 100);
                }
            } else {
                String Cognomi = "";
                String Nomi = "";
                String idS = "";
                // String Loghi = "";

                for (PlayerStatistics player : g.response.get(Dove).players) {
                    String[] n = player.player.name.split(" ");
                    String Nome = n[0];
                    String Cognome = "";
                    String id = String.valueOf(player.player.id);
                    // String logo = player.player.photo;

                    for (int i = 1; i < n.length; i++) {
                        Cognome += n[i] + " ";
                    }

                    Cognomi += Cognome.trim() + ";";
                    Nomi += Nome.trim() + ";";
                    idS += id.trim() + ";";
                    // Loghi += logo.replace(":", "*2p*").replace("/", "*S*") + ";";
                }

                String Urletto = "AggiungeGiocatori?" +
                        "idAnno=" + VariabiliStaticheApiFootball.getInstance().getIdAnnoScelto() +
                        "&idSquadra=" + idSquadra +
                        "&Cognomi=" + Cognomi +
                        "&Nomi=" + Nomi +
                        "&idGiocApiFootball=" + idS +
                        "&Squadra=" + Squadra;
                        // "&Loghi=" + Loghi;

                TipoOperazione = "AggiungeGiocatori";

                Esegue(
                        RadiceWS + ws + Urletto,
                        TipoOperazione,
                        NS,
                        SA,
                        10000,
                        ApriDialog);
            }
        } else {
            UtilityLazio.getInstance().ImpostaAttesa(false);
            VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);

            if (Dove == 0) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.AggiungeGiocatori(1);
                    };
                };
                handlerTimer.postDelayed(rTimer, 100);
            } else {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.RitornaGiocatoriPerSalvataggio(0);
                    }

                    ;
                };
                handlerTimer.postDelayed(rTimer, 100);
            }
        }
    }

    private String RitornaTipologiaPartita(String Competizione, String Tipo) {
        switch(Competizione) {
            case "Serie A":
                return "1";
            case "Friendlies Clubs":
                return "2";
            case "UEFA Europa League":
                return "3";
            case "UEFA Champions League":
                return "4";
            case "UEFA Europa Conference League":
                return "42";
            case "Coppa Italia":
                return "6";
            case "Super Cup":
                return "15";
            default:
                int a = 0;
                break;
        }

        return "";
    }

    public void SalvaDettaglio() {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        FixtureData datiPartita = VariabiliStaticheApiFootball.getInstance().getPartiteSquadra().response.get(
                VariabiliStaticheApiFootball.getInstance().getIdPartitaDaSalvare()
        );

        String CasaNormale = datiPartita.teams.home.name;
        String FuoriNormale = datiPartita.teams.away.name;
        String Casa = datiPartita.teams.home.name.toUpperCase().trim();
        String Fuori = datiPartita.teams.away.name.toUpperCase().trim();

        if (!Casa.equals("LAZIO") && !Fuori.equals("LAZIO")) {
            controllaProsecuzioneSalvaDettaglio();
            return;
        }

        String Arbitro = datiPartita.fixture.referee;
        if (Arbitro == null) { Arbitro = ""; }

        String d = datiPartita.fixture.date;
        String[] data = d.split("T");
        String[] o = data[1].split("\\+");
        String ora = o[0];
        String DataPartita = data[0] + " " + ora;

        String TipoPartita = datiPartita.league.round;
        String NomeCompetizione = datiPartita.league.name;
        String[] t = TipoPartita.split("-");
        String Tipo = t[0].trim().toUpperCase();
        String idTipologia = RitornaTipologiaPartita(NomeCompetizione, Tipo);

        int Giornata = 0;
        try {
            Giornata = Integer.parseInt(t[1].trim());
        } catch (Exception ignored) {

        }

        int idCasa = datiPartita.teams.home.id;
        int idFuori = datiPartita.teams.away.id;

        int goalCasa = datiPartita.score.fulltime.home;
        int goalFuori = datiPartita.score.fulltime.away;

        String Localita = datiPartita.fixture.venue.name + " (" + datiPartita.fixture.venue.city + ")";
        if (Localita == null) { Localita = ""; }
        String Spettatori = "0";

        String Note =
                Arbitro + ";" +
                Localita + ";" +
                Spettatori + ";" +
                "";

        String FormazioneCasa = "";
        String AmmonitiCasa = "";
        String EspulsiCasa = "";
        String MarcatoriCasa = "";
        String FormazioneFuori = "";
        String AmmonitiFuori = "";
        String EspulsiFuori = "";
        String MarcatoriFuori = "";

        GiocatoriPartita g = VariabiliStaticheApiFootball.getInstance().getGiocatoriDellaPartita();
        for (int i = 0; i < 2; i++) {
            if (!g.response.isEmpty() && g.response.get(i) != null) {
                for (PlayerStatistics player : g.response.get(i).players) {
                    int idGiocatore = -1;
                    String Nome = player.player.name;

                    if (i == 0) {
                        if (VariabiliStaticheApiFootball.getInstance().getGiocatoriCasaPS() != null) {
                            for (StrutturaGiocatori sg : VariabiliStaticheApiFootball.getInstance().getGiocatoriCasaPS()) {
                                String Nome2 = sg.getNome() + " " + sg.getCognome();
                                if (Nome.equals(Nome2)) {
                                    idGiocatore = sg.getIdGiocatore();
                                    break;
                                }
                            }
                        }
                    } else {
                        if (VariabiliStaticheApiFootball.getInstance().getGiocatoriFuoriPS() != null) {
                            for (StrutturaGiocatori sg : VariabiliStaticheApiFootball.getInstance().getGiocatoriFuoriPS()) {
                                String Nome2 = sg.getNome() + " " + sg.getCognome();
                                if (Nome.equals(Nome2)) {
                                    idGiocatore = sg.getIdGiocatore();
                                    break;
                                }
                            }
                        }
                    }

                    if (idGiocatore > 0) {
                        Statistics statistics = player.statistics.get(0);
                        Games ga = statistics.games;
                        int minutiGiocati = ga.minutes;
                        if (minutiGiocati > 0) {
                            int Entrato = 90 - minutiGiocati;
                            int Uscito = Entrato + minutiGiocati;

                            if (i == 0) {
                                FormazioneCasa += idGiocatore + ";" + Entrato + ";" + Uscito + "§";
                            } else {
                                FormazioneFuori += idGiocatore + ";" + Entrato + ";" + Uscito + "§";
                            }

                            Cards cartellini = statistics.cards;
                            int cartelliniGialli = cartellini.yellow;
                            if (cartelliniGialli > 0) {
                                String Minuto = "0";
                                if (i == 0) {
                                    AmmonitiCasa += idGiocatore + ";" + Minuto + "§";
                                } else {
                                    AmmonitiFuori += idGiocatore + ";" + Minuto + "§";
                                }
                            }
                            int cartelliniRossi = cartellini.red;
                            if (cartelliniRossi > 0) {
                                String Minuto = "0";
                                if (i == 0) {
                                    EspulsiCasa += idGiocatore + ";" + Minuto + "§";
                                } else {
                                    EspulsiFuori += idGiocatore + ";" + Minuto + "§";
                                }
                            }

                            Goals goals = statistics.goals;
                            if (goals.total != null) {
                                String Minuto = "0";
                                String Rigore = "";

                                if (i == 0) {
                                    MarcatoriCasa += idGiocatore + ";" + Minuto + ";" + Rigore + "§";
                                } else {
                                    MarcatoriFuori += idGiocatore + ";" + Minuto + ";" + Rigore + "§";
                                }
                            }
                        }
                    }
                }
            }
        }

        String Urletto="SalvaDettaglio?" +
                "idAnno=" + VariabiliStaticheApiFootball.getInstance().getIdAnnoScelto() +
                "&idTipologia=" + idTipologia +
                "&idPartita=" + VariabiliStaticheApiFootball.getInstance().getIdPartitaSalvata() +
                "&idGiornata=" + Giornata +
                "&DataPartita=" + DataPartita +
                "&idSquadraCasa=" + idCasa +
                "&idSquadraFuori=" + idFuori +
                "&Risultato=" + goalCasa + "-" + goalFuori +
                "&Arbitro=" + Arbitro +
                "&Localita=" + Localita +
                "&Spettatori=" + Spettatori +
                "&marcatoriCasa=" + MarcatoriCasa +
                "&marcatoriFuori=" + MarcatoriFuori +
                "&ammonitiCasa=" + AmmonitiCasa +
                "&ammonitiFuori=" + AmmonitiFuori +
                "&espulsiCasa=" + EspulsiCasa +
                "&espulsiFuori=" + EspulsiFuori +
                "&formazioneCasa=" + FormazioneCasa +
                "&formazioneFuori=" + FormazioneFuori +
                "&note=" + Note +
                "&insCalendario=N" +
                "&SquadraCasa=" + CasaNormale +
                "&SquadraFuori=" + FuoriNormale;

                TipoOperazione = "SalvaDettaglio";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {
        UtilityLazio.getInstance().ImpostaAttesa(true);
        VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(true);

        long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = Long.toString(tsLong);

        InterrogazioneWSLazio i = new InterrogazioneWSLazio();
        i.EsegueChiamata(
                context,
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                TimeStampAttuale,
                this
        );
    }

    @Override
    public void TaskCompletionResult(String result) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                switch (TipoOperazione) {
                    case "RitornaAnni":
                        fRitornaAnni(result);
                        break;
                    case "RitornaCompetizioni":
                        fRitornaCompetizioni(result);
                        break;
                    case "RitornaSquadre":
                        fRitornaSquadre(result);
                        break;
                    case "RitornaClassifica":
                        fRitornaClassifica(result);
                        break;
                    case "RitornaCalendario":
                        fRitornaCalendario(result);
                        break;
                    case "RitornaMercato":
                        fRitornaMercato(result);
                        break;
                    case "RitornaStati":
                        fRitornaStati(result);
                        break;
                    case "RitornaRuoli":
                        fRitornaRuoli(result);
                        break;
                    case "RitornaFonti":
                        fRitornaFonti(result);
                        break;
                    case "RitornaGiocatori":
                        fRitornaGiocatori(result);
                        break;
                    case "RitornaGiocatoriPS":
                        fRitornaGiocatoriPS(result);
                        break;
                    case "RitornaAllenatori":
                        fRitornaAllenatori(result);
                        break;
                    case "AggiungeTuttiGliAllenatori":
                        fAggiungeTuttiGliAllenatori(result);
                        break;
                    case "RitornaMarcatori":
                        fRitornaMarcatori(result);
                        break;
                    case "GestioneMercato":
                        fGestioneMercato(result);
                        break;
                    case "EliminaMercato":
                        fEliminaMercato(result);
                        break;
                    case "AggiungeModificaAnno":
                        fAggiungeModificaAnno(result);
                        break;
                    case "RitornaIdAnno":
                        fRitornaIdAnno(result);
                        break;
                    case "AggiungeSquadra":
                        fAggiungeSquadra(result);
                        break;
                    case "AggiungeModificaCalendario":
                        fAggiungeModificaCalendario(result);
                        break;
                    case "AggiungeGiocatori":
                        fAggiungeGiocatori(result);
                        break;
                    case "SalvaDettaglio":
                        fSalvaDettaglio(result);
                        break;
                    case "ScriveRisultati":
                        fScriveRisultati(result);
                        break;
                    case "AggiungeModificaGiocatore":
                        fAggiungeModificaGiocatore(result);
                        break;
                }

                UtilityLazio.getInstance().ImpostaAttesa(false);
                VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
    }

    private boolean ControllaRitorno(String Operazione, String result) {
        if (result.contains("ERROR:") || result.toUpperCase().contains("ANYTYPE")) {
            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION") || result.contains("SOCKETTIMEOUTEXCEPTION")) {
            }

            return false;
        } else {
            return true;
        }
    }

    private void fAggiungeModificaGiocatore(String result) {
        boolean ritorno = ControllaRitorno("Ritorno aggiunge modifica giocatore", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Aggiunge modifica giocatore: " + result);
        } else {
            VariabiliStaticheLazio.getInstance().getLayModificaGiocatore().setVisibility(LinearLayout.GONE);
            VariabiliStaticheLazio.getInstance().getLayRuolo().setVisibility(LinearLayout.GONE);
            UtilitiesGlobali.getInstance().ApreToast(context, "Giocatore salvato");
        }
    }

    private void fScriveRisultati(String result) {
        boolean ritorno = ControllaRitorno("Ritorno scrive risultati", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Scrive risultati: " + result);
        } else {
            VariabiliStaticheApiFootball.getInstance().ScriveAvanzamento("");

            UtilitiesGlobali.getInstance().ApreToast(context, "Risultati aggiornati");
        }
    }

    private void fSalvaDettaglio(String result) {
        boolean ritorno = ControllaRitorno("Ritorno salva dettaglio", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Salva dettaglio: " + result);
        } else {
            controllaProsecuzioneSalvaDettaglio();
        }
    }

    private void controllaProsecuzioneSalvaDettaglio() {
        if (VariabiliStaticheApiFootball.getInstance().isStaSalvandoTutteLePartite()) {
            int quantePartite = VariabiliStaticheApiFootball.getInstance().getPartiteSquadra().response.size();
            int attuale = VariabiliStaticheApiFootball.getInstance().getIndiceSalvataggioTutteLePartite();
            VariabiliStaticheApiFootball.getInstance().setIndiceSalvataggioTutteLePartite(attuale + 1);
            if (VariabiliStaticheApiFootball.getInstance().getIndiceSalvataggioTutteLePartite() < quantePartite ) {
                UtilityApiFootball u = new UtilityApiFootball();

                while (u.RitornaFileFatti(VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta(), attuale)) {
                    attuale++;
                    VariabiliStaticheApiFootball.getInstance().setIndiceSalvataggioTutteLePartite(attuale);
                }

                String testo = "Partita " + (attuale + 1) + "/" + quantePartite + " salvata";
                VariabiliStaticheApiFootball.getInstance().ScriveAvanzamento(testo);
                UtilitiesGlobali.getInstance().ApreToast(context, testo);

                u.AggiornaFileFatti(VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta(), attuale, true);
                u.SalvaTutteLePartite(context);
            } else {
                VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);

                VariabiliStaticheApiFootball.getInstance().setStaSalvandoTutteLePartite(false);
                VariabiliStaticheApiFootball.getInstance().setStaSalvandoPartita(false);
                VariabiliStaticheApiFootball.getInstance().setIndiceSalvataggioTutteLePartite(0);

                VariabiliStaticheApiFootball.getInstance().setIdPartitaDaSalvare(-1);
                VariabiliStaticheApiFootball.getInstance().setIdPartitaSalvata(-1);
                VariabiliStaticheApiFootball.getInstance().setGiocatoriCasaPS(null);
                VariabiliStaticheApiFootball.getInstance().setGiocatoriFuoriPS(null);
                VariabiliStaticheApiFootball.getInstance().ScriveAvanzamento("Partite salvate: " + (attuale + 1));

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        VariabiliStaticheApiFootball.getInstance().ScriveAvanzamento("Aggiornamento risultati");

                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.ScriveRisultati();
                    };
                };
                handlerTimer.postDelayed(rTimer, 5000);
            }
        } else {
            VariabiliStaticheApiFootball.getInstance().ImpostaAttesa(false);

            VariabiliStaticheApiFootball.getInstance().setStaSalvandoPartita(false);

            UtilityApiFootball u = new UtilityApiFootball();
            u.AggiornaFileFatti(
                    VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta(),
                    VariabiliStaticheApiFootball.getInstance().getIdPartitaDaSalvare(),
                    true
                    );

            VariabiliStaticheApiFootball.getInstance().setIdPartitaDaSalvare(-1);
            VariabiliStaticheApiFootball.getInstance().setIdPartitaSalvata(-1);
            VariabiliStaticheApiFootball.getInstance().setGiocatoriCasaPS(null);
            VariabiliStaticheApiFootball.getInstance().setGiocatoriFuoriPS(null);
            VariabiliStaticheApiFootball.getInstance().ScriveAvanzamento("Partita e dettaglio salvati");

            UtilitiesGlobali.getInstance().ApreToast(context, "Partita e dettaglio salvati");
        }
    }
    
    private void fAggiungeGiocatori(String result) {
        boolean ritorno = ControllaRitorno("Ritorno aggiunge giocatori", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Aggiunge giocatori: " + result);
        } else {
            if (DovePerSalvataggioGiocatori == 0) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.AggiungeGiocatori(1);
                    };
                };
                handlerTimer.postDelayed(rTimer, 100);
            } else {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.RitornaGiocatoriPerSalvataggio(0);
                    };
                };
                handlerTimer.postDelayed(rTimer, 100);
            }
        }
    }

    private void fAggiungeModificaCalendario(String result) {
        boolean ritorno = ControllaRitorno("Ritorno aggiunge modifica calendario", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, "A/M Calendario " + result);
        } else {
            VariabiliStaticheApiFootball.getInstance().setIdPartitaSalvata(Integer.parseInt(result));
            // UtilitiesGlobali.getInstance().ApreToast(context, "Partita inserita/modificata");

            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    VariabiliStaticheApiFootball.getInstance().setSquadraCheStaSalvando(0);
                    ChiamateWSLazio ws = new ChiamateWSLazio(context);
                    ws.AggiungeSquadra(
                            VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato(),
                            VariabiliStaticheApiFootball.getInstance().getSquadraDiCasaDaSalvare(),
                            VariabiliStaticheApiFootball.getInstance().getIdTipologiaDaSalvare()
                    );
                };
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }

    private void fAggiungeTuttiGliAllenatori(String result) {
        boolean ritorno = ControllaRitorno("Ritorno aggiunge allenatori", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Allenatori: " + result);
        } else {
            if (VariabiliStaticheApiFootball.getInstance().isStaSalvandoTutteLeSquadre()) {
                /* ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.AggiungeSquadra(
                        VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato(),
                        VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta()
                ); */
            } else {
                UtilitiesGlobali.getInstance().ApreToast(context, "Allenatori aggiunti");
            }
        }
    }

    private void fAggiungeSquadra(String result) {
        boolean ritorno = ControllaRitorno("Ritorno aggiunge squadra", result);
        if (!ritorno && !VariabiliStaticheApiFootball.getInstance().isStaSalvandoTutteLeSquadre() &&
                VariabiliStaticheApiFootball.getInstance().getSquadraCheStaSalvando() == -1) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (VariabiliStaticheApiFootball.getInstance().getSquadraCheStaSalvando() > -1) {
                if (VariabiliStaticheApiFootball.getInstance().getSquadraCheStaSalvando() == 0) {
                    VariabiliStaticheApiFootball.getInstance().setSquadraCheStaSalvando(1);
                    ChiamateWSLazio ws = new ChiamateWSLazio(context);
                    ws.AggiungeSquadra(
                            VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato(),
                            VariabiliStaticheApiFootball.getInstance().getSquadraFuoriDaSalvare(),
                            VariabiliStaticheApiFootball.getInstance().getIdTipologiaDaSalvare()
                    );
                } else {
                    VariabiliStaticheApiFootball.getInstance().setSquadraCheStaSalvando(-1);
                    VariabiliStaticheApiFootball.getInstance().setSquadraDiCasaDaSalvare("");
                    VariabiliStaticheApiFootball.getInstance().setSquadraFuoriDaSalvare("");

                    ChiamateWSLazio ws = new ChiamateWSLazio(context);
                    ws.AggiungeGiocatori(0);
                }
            } else {
                if (VariabiliStaticheApiFootball.getInstance().isStaSalvandoTutteLeSquadre()) {
                    int aggiunte = VariabiliStaticheApiFootball.getInstance().getSquadreAggiunte();
                    VariabiliStaticheApiFootball.getInstance().setSquadreAggiunte(aggiunte + 1);
                    UtilitiesGlobali.getInstance().ApreToast(context, "Squadra aggiunta " +
                            VariabiliStaticheApiFootball.getInstance().getSquadreAggiunte() + ": " +
                            VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta());

                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            int i = VariabiliStaticheApiFootball.getInstance().getIndiceSalvataggioTutteLeSquadre();
                            VariabiliStaticheApiFootball.getInstance().setIndiceSalvataggioTutteLeSquadre(i + 1);
                            if (VariabiliStaticheApiFootball.getInstance().getIndiceSalvataggioTutteLeSquadre() <
                                    VariabiliStaticheApiFootball.getInstance().getListaSquadreAnno().response.size()) {
                                UtilityApiFootball u = new UtilityApiFootball();
                                u.SalvaTutteLeSquadre(context);
                            } else {
                                VariabiliStaticheApiFootball.getInstance().setStaSalvandoTutteLeSquadre(false);

                                UtilitiesGlobali.getInstance().ApreToast(context, "Elaborazione completata. Squadre aggiunte: " +
                                        VariabiliStaticheApiFootball.getInstance().getSquadreAggiunte());

                                VariabiliStaticheApiFootball.getInstance().setSquadreAggiunte(0);
                            }
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 1000);
                } else {
                    Handler handlerTimer = new Handler(Looper.getMainLooper());
                    Runnable rTimer = new Runnable() {
                        public void run() {
                            ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
                            ws1.RitornaSquadre();
                        }
                    };
                    handlerTimer.postDelayed(rTimer, 1000);

                    UtilitiesGlobali.getInstance().ApreToast(context, "Squadra aggiunta");
                }
            }
        }
    }

    private void fRitornaIdAnno(String result) {
        boolean ritorno = ControllaRitorno("Ritorno id anno", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            VariabiliStaticheApiFootball.getInstance().setIdAnnoScelto(Integer.parseInt(result));
        }
    }

    private void fAggiungeModificaAnno(String result) {
        boolean ritorno = ControllaRitorno("Ritorno gestione anno", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Modifiche effettuate");
        }
    }

    private void fEliminaMercato(String result) {
        boolean ritorno = ControllaRitorno("Ritorno elimina mercato", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Voce di mercato eliminata");
        }
    }

    private void fGestioneMercato(String result) {
        boolean ritorno = ControllaRitorno("Ritorno gestione mercato", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            VariabiliStaticheLazio.getInstance().getLayModificaMercato().setVisibility(LinearLayout.GONE);
            VariabiliStaticheLazio.getInstance().getLayModifica().setVisibility(LinearLayout.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChiamateWSLazio ws = new ChiamateWSLazio(context);
                    ws.RitornaMercato();
                }
            }, 500);
        }
    }

    private void fRitornaMarcatori(String result) {
        boolean ritorno = ControllaRitorno("Ritorno marcatori", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaMarcatori> lista = new ArrayList<>();
            String[] righe = result.split("§", -1);
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";", -1);

                    StrutturaMarcatori s = new StrutturaMarcatori();
                    s.setCognome(campi[0]);
                    s.setNome(campi[1]);
                    s.setRuolo(campi[2]);
                    s.setSquadra(campi[3]);
                    s.setGoals(Integer.parseInt(campi[4]));
                    s.setIdApiFootball(Integer.parseInt(campi[5]));

                    lista.add(s);
                }
            }

            VariabiliStaticheLazio.getInstance().setMarcatori(lista);

            AdapterListenerMarcatori adapter = new AdapterListenerMarcatori(context, lista);
            VariabiliStaticheLazio.getInstance().getLstMarcatori().setAdapter(adapter);
        }
    }

    private void fRitornaAllenatori(String result) {
        boolean ritorno = ControllaRitorno("Ritorno allenatori", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaAllenatori> lista = new ArrayList<>();
            String[] righe = result.split("§", -1);
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";", -1);

                    StrutturaAllenatori s = new StrutturaAllenatori();
                    s.setIdAllenatore(Integer.parseInt(campi[0]));
                    s.setCognome(campi[1]);
                    s.setNome(campi[2]);

                    lista.add(s);
                }
            }

            VariabiliStaticheLazio.getInstance().setAllenatori(lista);

            VariabiliStaticheLazio.getInstance().setCstmAdptAllenatori(new AdapterListenerAllenatori(context, lista));
            VariabiliStaticheLazio.getInstance().getLstAllenatori().setAdapter(VariabiliStaticheLazio.getInstance().getCstmAdptAllenatori());
        }
    }

    private void fRitornaGiocatori(String result) {
        boolean ritorno = ControllaRitorno("Ritorno giocatori", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaGiocatori> lista = new ArrayList<>();
            String[] righe = result.split("§", -1);
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";", -1);

                    StrutturaGiocatori s = new StrutturaGiocatori();
                    s.setIdGiocatore(Integer.parseInt(campi[0]));
                    s.setCognome(campi[1]);
                    s.setNome(campi[2]);
                    s.setIdRuolo(Integer.parseInt(campi[3]));
                    s.setRuolo(campi[4]);
                    s.setIdApiFootball(Integer.parseInt(campi[5]));
                    // s.setLogo(campi[6]);

                    lista.add(s);
                }
            }

            VariabiliStaticheLazio.getInstance().setGiocatori(lista);

            VariabiliStaticheLazio.getInstance().setCstmAdptGiocatori(new AdapterListenerGiocatori(context, lista));
            VariabiliStaticheLazio.getInstance().getLstGiocatori().setAdapter(VariabiliStaticheLazio.getInstance().getCstmAdptGiocatori());
        }
    }

    private void fRitornaGiocatoriPS(String result) {
        boolean ritorno = ControllaRitorno("Ritorno giocatori ps", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaGiocatori> lista = new ArrayList<>();
            String[] righe = result.split("§", -1);
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";", -1);

                    StrutturaGiocatori s = new StrutturaGiocatori();
                    s.setIdGiocatore(Integer.parseInt(campi[0]));
                    s.setCognome(campi[1]);
                    s.setNome(campi[2]);
                    s.setIdRuolo(Integer.parseInt(campi[3]));
                    s.setRuolo(campi[4]);
                    s.setIdApiFootball(Integer.parseInt(campi[5]));
                    // s.setLogo(campi[6]);

                    lista.add(s);
                }
            }

            if (DovePerSalvataggioGiocatori == 0) {
                VariabiliStaticheApiFootball.getInstance().setGiocatoriCasaPS(lista);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.RitornaGiocatoriPerSalvataggio(1);
                    }
                }, 500);
            } else {
                VariabiliStaticheApiFootball.getInstance().setGiocatoriFuoriPS(lista);

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        FixtureData datiPartita = VariabiliStaticheApiFootball.getInstance().getPartiteSquadra().response.get(
                                VariabiliStaticheApiFootball.getInstance().getIdPartitaDaSalvare()
                        );

                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.SalvaDettaglio();
                    }
                }, 500);
            }
        }
    }

    private void fRitornaStati(String result) {
        boolean ritorno = ControllaRitorno("Ritorno stati", result);

        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Stati.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                Files.getInstance().EliminaFileUnico(PathFile + "/" + NomeFile);
            }
            Files.getInstance().ScriveFile(PathFile, NomeFile, result);

            List<StrutturaStati> lista = new ArrayList<>();
            String[] righe = result.split("§", -1);
            String[] righePerSpinner = new String[righe.length];
            // righePerSpinner[0] = "";
            int i = 0;
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";", -1);

                    StrutturaStati s = new StrutturaStati();
                    s.setIdStato(Integer.parseInt(campi[0]));
                    s.setStato(campi[1]);

                    if (campi[1] != null) {
                        righePerSpinner[i] = campi[1];
                    } else {
                        righePerSpinner[i] = "";
                    }
                    i++;

                    lista.add(s);
                }
            }

            i = 0;
            for (String s : righePerSpinner) {
                if (s == null) {
                    righePerSpinner[i] = "";
                }
                i++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item, righePerSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            VariabiliStaticheLazio.getInstance().getSpnStati().setAdapter(adapter);
            VariabiliStaticheLazio.getInstance().setAdapterStati(adapter);

            int spinnerPosition = VariabiliStaticheLazio.getInstance().getAdapterStati().getPosition("");
            VariabiliStaticheLazio.getInstance().getSpnStati().setSelection(spinnerPosition);

            VariabiliStaticheLazio.getInstance().setStati(lista);

            AdapterListenerStati cstmAdptStati = new AdapterListenerStati(context, lista);
            VariabiliStaticheLazio.getInstance().getLstStati().setAdapter(cstmAdptStati);
        }
    }

    private void fRitornaRuoli(String result) {
        boolean ritorno = ControllaRitorno("Ritorno ruoli", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Ruoli.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                Files.getInstance().EliminaFileUnico(PathFile + "/" + NomeFile);
            }
            Files.getInstance().ScriveFile(PathFile, NomeFile, result);

            List<StrutturaRuoli> lista = new ArrayList<>();
            String[] righe = result.split("§", -1);
            String[] righePerSpinner = new String[righe.length];
            // righePerSpinner[0] = "";
            int i = 0;
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";", -1);

                    StrutturaRuoli s = new StrutturaRuoli();
                    s.setIdRuolo(Integer.parseInt(campi[0]));
                    s.setRuolo(campi[1]);

                    if (campi[1] != null) {
                        righePerSpinner[i] = campi[1];
                    } else {
                        righePerSpinner[i] = "";
                    }
                    i++;

                    lista.add(s);
                }
            }

            i = 0;
            for (String s : righePerSpinner) {
                if (s == null) {
                    righePerSpinner[i] = "";
                }
                i++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item, righePerSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            VariabiliStaticheLazio.getInstance().getSpnRuolo().setAdapter(adapter);
            VariabiliStaticheLazio.getInstance().setAdapterRuoli(adapter);

            int spinnerPosition = VariabiliStaticheLazio.getInstance().getAdapterRuoli().getPosition("");
            VariabiliStaticheLazio.getInstance().getSpnRuolo().setSelection(spinnerPosition);

            VariabiliStaticheLazio.getInstance().setRuoli(lista);

            AdapterListenerRuoli cstmAdptRuoli = new AdapterListenerRuoli(context, lista);
            VariabiliStaticheLazio.getInstance().getLstRuoli().setAdapter(cstmAdptRuoli);
        }
    }

    private void fRitornaFonti(String result) {
        boolean ritorno = ControllaRitorno("Ritorno fonti", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Fonti.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                Files.getInstance().EliminaFileUnico(PathFile + "/" + NomeFile);
            }
            Files.getInstance().ScriveFile(PathFile, NomeFile, result);

            List<StrutturaFonti> lista = new ArrayList<>();
            String[] righe = result.split("§", -1);
            String[] righePerSpinner = new String[righe.length];
            // righePerSpinner[0] = "";
            int i = 0;
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";", -1);

                    StrutturaFonti s = new StrutturaFonti();
                    s.setIdFonte(Integer.parseInt(campi[0]));
                    s.setFonte(campi[1]);

                    if (campi[1] != null) {
                        righePerSpinner[i] = campi[1];
                    } else {
                        righePerSpinner[i] = "";
                    }
                    i++;

                    lista.add(s);
                }
            }

            i = 0;
            for (String s : righePerSpinner) {
                if (s == null) {
                    righePerSpinner[i] = "";
                }
                i++;
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item, righePerSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            VariabiliStaticheLazio.getInstance().getSpnFonti().setAdapter(adapter);
            VariabiliStaticheLazio.getInstance().setAdapterFonti(adapter);

            int spinnerPosition = VariabiliStaticheLazio.getInstance().getAdapterFonti().getPosition("");
            VariabiliStaticheLazio.getInstance().getSpnFonti().setSelection(spinnerPosition);

            VariabiliStaticheLazio.getInstance().setFonti(lista);

            AdapterListenerFonti cstmAdptFonti = new AdapterListenerFonti(context, lista);
            VariabiliStaticheLazio.getInstance().getLstFonti().setAdapter(cstmAdptFonti);
        }
    }

    private void fRitornaMercato(String result) {
        boolean ritorno = ControllaRitorno("Ritorno mercato", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
            VariabiliStaticheLazio.getInstance().getLstMercato().setAdapter(null);
        } else {
            List<StrutturaMercato> lista = new ArrayList<>();
            String[] righe = result.split("§", -1);
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";", -1);

                    StrutturaMercato s = new StrutturaMercato();
                    s.setProgressivo(Integer.parseInt(campi[0]));
                    s.setData(campi[1]);
                    s.setNominativo(campi[2]);
                    s.setIdFonte(Integer.parseInt(campi[3]));
                    s.setFonte(campi[4]);
                    s.setIdStato(Integer.parseInt(campi[5]));
                    s.setStato(campi[6]);

                    lista.add(s);
                }
            }

            VariabiliStaticheLazio.getInstance().setMercato(lista);

            VariabiliStaticheLazio.getInstance().setCstmAdptMercato(new AdapterListenerMercato(context, lista));
            VariabiliStaticheLazio.getInstance().getLstMercato().setAdapter(VariabiliStaticheLazio.getInstance().getCstmAdptMercato());
        }
    }

    private void fRitornaClassifica(String result) {
        boolean ritorno = ControllaRitorno("Ritorno classifica", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaClassifica> lista = new ArrayList<>();
            String[] righe = result.split("§", -1);
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";", -1);

                    StrutturaClassifica s = new StrutturaClassifica();
                    s.setIdSquadra(Integer.parseInt(campi[0]));
                    s.setSquadra(campi[1]);
                    s.setGiocate(Integer.parseInt(campi[2]));
                    s.setPunti(Integer.parseInt(campi[3]));
                    s.setVinte(Integer.parseInt(campi[4]));
                    s.setPareggiate(Integer.parseInt(campi[5]));
                    s.setPerse(Integer.parseInt(campi[6]));
                    s.setGoalFatti(Integer.parseInt(campi[7]));
                    s.setGoalSubiti(Integer.parseInt(campi[8]));
                    s.setGiocateCasa(Integer.parseInt(campi[9]));
                    s.setVinteCasa(Integer.parseInt(campi[10]));
                    s.setPareggiateCasa(Integer.parseInt(campi[11]));
                    s.setPerseCasa(Integer.parseInt(campi[12]));
                    s.setGoalFattiCasa(Integer.parseInt(campi[13]));
                    s.setGoalSubitiCasa(Integer.parseInt(campi[14]));
                    s.setGiocateFuori(Integer.parseInt(campi[15]));
                    s.setVinteFuori(Integer.parseInt(campi[16]));
                    s.setPareggiateFuori(Integer.parseInt(campi[17]));
                    s.setPerseFuori(Integer.parseInt(campi[18]));
                    s.setGoalFattiFuori(Integer.parseInt(campi[19]));
                    s.setGoalSubitiFuori(Integer.parseInt(campi[20]));
                    s.setMediaInglese(Integer.parseInt(campi[21]));

                    lista.add(s);
                }
            }
            VariabiliStaticheLazio.getInstance().setClassifica(lista);

            VariabiliStaticheLazio.getInstance().getTxtGiornata().setText(
                    "Giornata " + VariabiliStaticheLazio.getInstance().getGiornata() + "/" +
                    VariabiliStaticheLazio.getInstance().getMaxGiornate());

            VariabiliStaticheLazio.getInstance().setCstmAdptClassifica(new AdapterListenerClassifica(context, lista));
            VariabiliStaticheLazio.getInstance().getLstClassifica().setAdapter(VariabiliStaticheLazio.getInstance().getCstmAdptClassifica());

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChiamateWSLazio ws = new ChiamateWSLazio(context);
                    ws.RitornaCalendario();
                }
            }, 100);
        }
    }

    private void fRitornaSquadre(String result) {
        boolean ritorno = ControllaRitorno("Ritorno squadre", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
            VariabiliStaticheLazio.getInstance().setSquadre(null);

            VariabiliStaticheLazio.getInstance().getSpnSquadreGioc().setAdapter(null);
            VariabiliStaticheLazio.getInstance().getSpnSquadreAll().setAdapter(null);

            VariabiliStaticheLazio.getInstance().getLstSquadre().setAdapter(null);
        } else {
            List<StrutturaSquadre> lista = new ArrayList<>();
            String[] squadre = result.split("§", -1);
            String[] righePerSpinner = new String[squadre.length + 1];
            righePerSpinner[0] = "";
            int i = 1;
            for (String s : squadre) {
                if (!s.isEmpty() && !s.equals("\n")) {
                    String[] ss = s.split(";", -1);
                    StrutturaSquadre sq = new StrutturaSquadre();
                    sq.setIdSquadra(Integer.parseInt(ss[0]));
                    sq.setSquadra(ss[1]);

                    righePerSpinner[i] = ss[1];
                    i++;

                    lista.add(sq);
                }
            }

            int giornate = (lista.size() - 1) * 2;
            VariabiliStaticheLazio.getInstance().setMaxGiornate(giornate);
            VariabiliStaticheLazio.getInstance().setGiornata(giornate);

            VariabiliStaticheLazio.getInstance().setRighePerSquadre(righePerSpinner);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item, righePerSpinner);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            VariabiliStaticheLazio.getInstance().getSpnSquadreGioc().setAdapter(adapter);
            VariabiliStaticheLazio.getInstance().getSpnSquadreAll().setAdapter(adapter);

            VariabiliStaticheLazio.getInstance().setSquadre(lista);

            AdapterListenerSquadre cstmAdptSquadre = new AdapterListenerSquadre(context, lista);
            VariabiliStaticheLazio.getInstance().getLstSquadre().setAdapter(cstmAdptSquadre);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChiamateWSLazio ws = new ChiamateWSLazio(context);
                    ws.RitornaClassifica();
                }
            }, 100);
        }
    }

    private void fRitornaCalendario(String result) {
        boolean ritorno = ControllaRitorno("Ritorno calendario", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaCalendario> lista = new ArrayList<>();
            String[] calendario = result.split("§", -1);
            for (String s : calendario) {
                if (!s.isEmpty() && !s.equals("\n")) {
                    String[] ss = s.split(";", -1);
                    StrutturaCalendario sq = new StrutturaCalendario();
                    sq.setIdPartita(Integer.parseInt(ss[0]));
                    sq.setDataPartita(ss[1]);
                    sq.setIdSquadraCasa(Integer.parseInt(ss[2]));
                    sq.setCasa(ss[3]);
                    sq.setIdSquadraFuori(Integer.parseInt(ss[4]));
                    sq.setFuori(ss[5]);
                    sq.setRisultato1(Integer.parseInt(ss[6]));
                    sq.setRisultato2(Integer.parseInt(ss[7]));
                    sq.setSegno(ss[8]);
                    sq.setIdTipologiaCasa(Integer.parseInt(ss[9]));
                    sq.setIdTipologiaFuori(Integer.parseInt(ss[10]));
                    sq.setPreferito(ss[11]);

                    lista.add(sq);
                }
            }

            VariabiliStaticheLazio.getInstance().setCalendario(lista);

            AdapterListenerCalendario cstmAdptCalendario = new AdapterListenerCalendario(context, lista);
            VariabiliStaticheLazio.getInstance().getLstCalendario().setAdapter(cstmAdptCalendario);

            if (!VariabiliStaticheLazio.getInstance().isNonRicaricareMercato()) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.RitornaMercato();
                    }
                }, 100);
            } else {
                VariabiliStaticheLazio.getInstance().setNonRicaricareMercato(false);
            }
        }
    }

    private void fRitornaCompetizioni(String result) {
        boolean ritorno = ControllaRitorno("Ritorno competizioni", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Competizioni.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                Files.getInstance().EliminaFileUnico(PathFile + "/" + NomeFile);
            }
            Files.getInstance().ScriveFile(PathFile, NomeFile, result);

            List<StrutturaCompetizioni> lista = new ArrayList<>();
            String[] comp = result.split("§");
            String[] CompetizioniPerSpinner = new String[comp.length];
            int i = 0;
            for (String c : comp) {
                if (!c.isEmpty() && !c.equals("\n")) {
                    String[] cc = c.split(";", -1);
                    StrutturaCompetizioni s = new StrutturaCompetizioni();
                    s.setIdTipologia(Integer.parseInt(cc[0]));
                    s.setCompetizione(cc[1]);

                    CompetizioniPerSpinner[i] = cc[1];
                    i++;

                    lista.add(s);
                }
            }
            VariabiliStaticheLazio.getInstance().setCompetizioni(lista);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    context,
                    R.layout.spinner_text,
                    CompetizioniPerSpinner
            );

            VariabiliStaticheLazio.getInstance().getSpnCompetizioni().setAdapter(adapter);

            VariabiliStaticheLazio.getInstance().setIdTipologia(1);
            for (StrutturaCompetizioni s : VariabiliStaticheLazio.getInstance().getCompetizioni()) {
                if (s.getIdTipologia() == 1) {
                    int spinnerPosition = adapter.getPosition(s.getCompetizione());
                    VariabiliStaticheLazio.getInstance().getSpnCompetizioni().setSelection(spinnerPosition);
                    break;
                }
            }

            final boolean[] primoIngresso = {true};
            VariabiliStaticheLazio.getInstance().getSpnCompetizioni().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                    if (primoIngresso[0]) {
                        primoIngresso[0] = false;
                        return;
                    }

                    String selected ="";

                    try {
                        selected = (String) adapter.getItemAtPosition(pos).toString().trim();

                        for (StrutturaStati s : VariabiliStaticheLazio.getInstance().getStati()) {
                            if (s.getStato().equals(selected)) {
                                VariabiliStaticheLazio.getInstance().setIdStato(s.getIdStato());
                                break;
                            }
                        }
                    } catch (Exception e) {
                        selected="";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
        }
    }

    private void fRitornaAnni(String result) {
        boolean ritorno = ControllaRitorno("Ritorno Anni", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Anni.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                Files.getInstance().EliminaFileUnico(PathFile + "/" + NomeFile);
            }
            Files.getInstance().ScriveFile(PathFile, NomeFile, result);

            List<StrutturaAnni> anni = new ArrayList<>();
            String[] a = result.split("§");
            String[] AnniPerSpinner = new String[a.length];
            String PrimoAnno = "";
            int idAnno = -1;

            String PathFileSel = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFileSel = "AnnoSelezionato.txt";
            if (Files.getInstance().EsisteFile(PathFileSel + "/" + NomeFileSel)) {
                String sAnno = Files.getInstance().LeggeFile(PathFileSel, NomeFileSel).replace("\n", "");
                String[] anno = sAnno.split(";", -1);
                PrimoAnno = anno[0];
                idAnno = Integer.parseInt(anno[1]);
            }
            int c = 0;
            for (String aa : a) {
                if (!aa.isEmpty() && !aa.equals("\n")) {
                    String[] aaa = aa.split(";", -1);

                    try {
                        StrutturaAnni s = new StrutturaAnni();
                        s.setIdAnno(Integer.parseInt(aaa[0]));
                        s.setDescrizione(aaa[1]);
                        s.setIds1(aaa[2]);
                        s.setIds2(aaa[3]);
                        s.setIds3(aaa[4]);
                        s.setIds4(aaa[5]);
                        s.setIds5(aaa[6]);

                        if (PrimoAnno.isEmpty()) {
                            PrimoAnno = aaa[1];
                            idAnno = Integer.parseInt(aaa[0]);
                        }

                        AnniPerSpinner[c] = aaa[1];
                        c++;

                        anni.add(s);
                    } catch (Exception ignored) {
                        int a1 = 0;
                    }
                }
            }

            VariabiliStaticheLazio.getInstance().setAnni(anni);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                    context,
                    R.layout.spinner_text,
                    AnniPerSpinner
            );
            VariabiliStaticheLazio.getInstance().getSpnAnni().setAdapter(adapter);
            int spinnerPosition = adapter.getPosition(PrimoAnno);
            VariabiliStaticheLazio.getInstance().getSpnAnni().setSelection(spinnerPosition);

            VariabiliStaticheLazio.getInstance().setAnnoSelezionato(PrimoAnno);
            VariabiliStaticheLazio.getInstance().setIdAnnoSelezionato(idAnno);

            UtilityLazio.getInstance().LeggeAnno(context);

            final boolean[] primoIngresso = {true};
            VariabiliStaticheLazio.getInstance().getSpnAnni().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> adapter, View view, int pos, long id) {
                    if (primoIngresso[0]) {
                        primoIngresso[0] = false;
                        return;
                    }

                    String selected ="";

                    try {
                        selected = (String) adapter.getItemAtPosition(pos).toString().trim();

                        for (StrutturaAnni s : VariabiliStaticheLazio.getInstance().getAnni()) {
                            if (s.getDescrizione().equals(selected)) {
                                VariabiliStaticheLazio.getInstance().setIdAnnoSelezionato(s.getIdAnno());
                                VariabiliStaticheLazio.getInstance().setAnnoSelezionato(s.getDescrizione());

                                ChiamateWSLazio ws1 = new ChiamateWSLazio(context);
                                ws1.RitornaSquadre();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        selected="";
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });

        }
    }
}
