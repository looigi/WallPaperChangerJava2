package com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.UtilityLazioDettaglio;
import com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.VariabiliStaticheLazioDettaglio;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaAllenatori;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaAnni;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCalendario;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaClassifica;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCompetizioni;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaFonti;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaGiocatori;
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
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerMercato;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerRuoli;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerSquadre;
import com.looigi.wallpaperchanger2.classeLazio.adapters.AdapterListenerStati;
import com.looigi.wallpaperchanger2.classeLazio.webService.InterrogazioneWSLazio;
import com.looigi.wallpaperchanger2.classeLazio.webService.TaskDelegateLazio;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSLazioDettaglio implements TaskDelegateLazioDettaglio {
    private static final String NomeMaschera = "Chiamate_WS_LAZIO_DETTAGLIO";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheLazio.UrlWS;
    private final String ws = "wsLazio.asmx/";
    private final String NS="http://looLazio.it/";
    private final String SA="http://looLazio.it/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private boolean inCasa;

    public ChiamateWSLazioDettaglio(Context context) {
        this.context = context;
    }

    public void RitornaGiocatori(String idSquadra, boolean InCasa) {
        inCasa = InCasa;

        String Urletto="RitornaGiocatori?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idSquadra=" + idSquadra;

        TipoOperazione = "RitornaGiocatori";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaDettaglio(String idPartita, String idCasa, String idFuori) {
        String Urletto="RitornaDettaglio?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idTipologia=" + VariabiliStaticheLazio.getInstance().getIdTipologia() +
                "&idPartita=" + idPartita +
                "&idSquadraCasa=" + idCasa +
                "&idSquadraFuori=" + idFuori;

        TipoOperazione = "RitornaDettaglio";

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
        UtilityLazioDettaglio.getInstance().ImpostaAttesa(true);

        long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = Long.toString(tsLong);

        InterrogazioneWSLazioDettaglio i = new InterrogazioneWSLazioDettaglio();
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
                UtilityLazioDettaglio.getInstance().ImpostaAttesa(false);

                switch (TipoOperazione) {
                    case "RitornaDettaglio":
                        fRitornaDettaglio(result);
                        break;
                    case "RitornaGiocatori":
                        fRitornaGiocatori(result);
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

    private void fRitornaGiocatori(String result) {
        boolean ritorno = ControllaRitorno("Ritorno giocatori", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaGiocatori> lista = new ArrayList<>();
            String[] righe = result.split("§");
            String[] righePerSpinner = new String[righe.length];
            int i = 0;
            for (String r : righe) {
                if (!r.isEmpty() && !r.equals("\n")) {
                    String[] campi = r.split(";");

                    StrutturaGiocatori s = new StrutturaGiocatori();
                    s.setIdGiocatore(Integer.parseInt(campi[0]));
                    s.setCognome(campi[1]);
                    s.setNome(campi[2]);
                    s.setIdRuolo(Integer.parseInt(campi[3]));
                    s.setRuolo(campi[4]);

                    righePerSpinner[i] = s.getCognome() + " " + s.getNome();
                    i++;

                    lista.add(s);
                }
            }

            if (inCasa) {
                VariabiliStaticheLazioDettaglio.getInstance().setGiocatoriCasa(lista);
                VariabiliStaticheLazioDettaglio.getInstance().setGiocatoriCasaPerSpn(righePerSpinner);
            } else {
                VariabiliStaticheLazioDettaglio.getInstance().setGiocatoriFuori(lista);
                VariabiliStaticheLazioDettaglio.getInstance().setGiocatoriFuoriPerSpn(righePerSpinner);
            }
        }
    }

    private void fRitornaDettaglio(String result) {
        boolean ritorno = ControllaRitorno("Ritorno dettaglio", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
        }
    }
}
