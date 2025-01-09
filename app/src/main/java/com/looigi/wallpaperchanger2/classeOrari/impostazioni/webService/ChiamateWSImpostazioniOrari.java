package com.looigi.wallpaperchanger2.classeOrari.impostazioni.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeOrari.UtilityOrari;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.UtilityImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.VariabiliStaticheImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaCommesse;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDati;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDatiGiornata;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaIndirizzi;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaLavoro;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezziStandard;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPasticca;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPranzo;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaRicorrenze;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaTempo;
import com.looigi.wallpaperchanger2.classeOrari.webService.InterrogazioneWSOrari;
import com.looigi.wallpaperchanger2.classeOrari.webService.TaskDelegateOrari;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChiamateWSImpostazioniOrari implements TaskDelegateImpostazioniOrari {
    private static final String NomeMaschera = "Chiamate_WS_IMPOSTAZIONI_ORARI";

    private final String RadiceWS = VariabiliStaticheOrari.UrlWS;
    private final String ws = "Orari.asmx/";
    private final String NS="http://orariWSOrari.it/";
    private final String SA="http://orariWSOrari.it/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private String idOggetto;
    private String Oggetto1;
    private String Oggetto2;

    public ChiamateWSImpostazioniOrari(Context context) {
        this.context = context;
    }

    public void SalvaMezziStandard(String Tipologia, String idMezzi) {
        String Urletto="SalvaMezziStandard?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idMezzi=" + idMezzi +
                "&Tipologia=" + Tipologia;

        TipoOperazione = "SalvaMezziStandard";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void SalvaMezzo(String idMezzo, String Mezzo, String Dettaglio) {
        this.idOggetto = idMezzo;
        this.Oggetto1 = Mezzo;
        this.Oggetto2 = Dettaglio;

        String Urletto="GestioneMezzo?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idMezzo=" + idMezzo +
                "&Mezzo=" + Mezzo +
                "&Dettaglio=" + Dettaglio;

        TipoOperazione = "GestioneMezzi";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void EliminaMezzo(String idMezzo) {
        String Urletto="EliminaMezzo?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idMezzo=" + idMezzo;

        TipoOperazione = "EliminaMezzo";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void SalvaPortata(String idPortata, String Portata) {
        this.idOggetto = idPortata;
        this.Oggetto1 = Portata;

        String Urletto="GestionePortata?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idPortata=" + idPortata +
                "&Portata=" + Portata;

        TipoOperazione = "GestionePortata";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void EliminaPortata(String idPortata) {
        String Urletto="EliminaPortata?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idPortata=" + idPortata;

        TipoOperazione = "EliminaPortata";

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
        UtilityImpostazioniOrari.getInstance().ImpostaAttesa(true);

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSImpostazioniOrari i = new InterrogazioneWSImpostazioniOrari();
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
                UtilityImpostazioniOrari.getInstance().ImpostaAttesa(false);

                switch (TipoOperazione) {
                    case "GestionePortata":
                        fGestionePortata(result);
                        break;
                    case "EliminaPortata":
                        fEliminaPortata(result);
                        break;
                    case "GestioneMezzi":
                        fGestioneMezzi(result);
                        break;
                    case "EliminaMezzo":
                        fEliminaMezzo(result);
                        break;
                    case "SalvaMezziStandard":
                        fSalvaMezziStandard(result);
                        break;
                }

                VariabiliStaticheOrari.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
    }

    private boolean ControllaRitorno(String Operazione, String result) {
        if (result.contains("ERROR:")) {
            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION") || result.contains("SOCKETTIMEOUTEXCEPTION")) {
            }

            return false;
        } else {
            return true;
        }
    }

    private void fGestionePortata(String result) {
        boolean ritorno = ControllaRitorno("Ritorno gestione portata", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (idOggetto.equals("-1")) {
                StrutturaPranzo s = new StrutturaPranzo();
                s.setIdPortata(Integer.parseInt(result));
                s.setPortata(Oggetto1);

                VariabiliStaticheOrari.getInstance().getStrutturaDati().getPortate().add(s);
            } else {
                for (StrutturaPranzo s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getPortate()) {
                    if (idOggetto.equals(Integer.toString(s.getIdPortata()))) {
                        s.setPortata(Oggetto1);
                    }
                }
            }
            VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptPranzo().updateData(
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());

            UtilitiesGlobali.getInstance().ApreToast(context, "Portata salvata");
        }
    }

    private void fGestioneMezzi(String result) {
        boolean ritorno = ControllaRitorno("Ritorno gestione mezzi", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (idOggetto.equals("-1")) {
                StrutturaMezzi s = new StrutturaMezzi();
                s.setIdMezzo(Integer.parseInt(result));
                s.setMezzo(Oggetto1);
                s.setDettaglio(Oggetto2);

                VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi().add(s);
            } else {
                for (StrutturaMezzi s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getMezzi()) {
                    if (idOggetto.equals(Integer.toString(s.getIdMezzo()))) {
                        s.setMezzo(Oggetto1);
                        s.setDettaglio(Oggetto2);
                    }
                }
            }
            VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptMezzi().updateData(
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());

            UtilitiesGlobali.getInstance().ApreToast(context, "Mezzo salvato");
        }
    }

    private void fEliminaPortata(String result) {
        boolean ritorno = ControllaRitorno("Ritorno elimina portata", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Portata eliminata");
        }
    }

    private void fEliminaMezzo(String result) {
        boolean ritorno = ControllaRitorno("Ritorno elimina mezzo", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Mezzo eliminato");
        }
    }

    private void fSalvaMezziStandard(String result) {
        boolean ritorno = ControllaRitorno("Ritorno salva mezzi standard", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Mezzi standard salvati");
        }
    }
}
