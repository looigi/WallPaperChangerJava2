package com.looigi.wallpaperchanger2.classeLazio.webService;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
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
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.nio.file.Path;
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

    public void RitornaGiocatori() {
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
        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Ruoli.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
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
        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Stati.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    fRitornaStati(Dati);
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
        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Fonti.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    fRitornaFonti(Dati);
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

    public void RitornaSquadre() {
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
        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Competizioni.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    fRitornaCompetizioni(Dati);
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

    public void RitornaAnni(boolean RefreshDati) {
        if (!RefreshDati) {
            String PathFile = VariabiliStaticheLazio.getInstance().getPathLazio();
            String NomeFile = "Anni.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    fRitornaAnni(Dati);
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

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {
        UtilityLazio.getInstance().ImpostaAttesa(true);

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
                UtilityLazio.getInstance().ImpostaAttesa(false);

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
                    case "RitornaAllenatori":
                        fRitornaAllenatori(result);
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
                }
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

                    lista.add(s);
                }
            }

            VariabiliStaticheLazio.getInstance().setGiocatori(lista);

            VariabiliStaticheLazio.getInstance().setCstmAdptGiocatori(new AdapterListenerGiocatori(context, lista));
            VariabiliStaticheLazio.getInstance().getLstGiocatori().setAdapter(VariabiliStaticheLazio.getInstance().getCstmAdptGiocatori());
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
