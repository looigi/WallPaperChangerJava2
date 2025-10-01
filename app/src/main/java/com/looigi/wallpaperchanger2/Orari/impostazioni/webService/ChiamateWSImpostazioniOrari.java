package com.looigi.wallpaperchanger2.Orari.impostazioni.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.Orari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.Orari.impostazioni.UtilityImpostazioniOrari;
import com.looigi.wallpaperchanger2.Orari.impostazioni.VariabiliStaticheImpostazioniOrari;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaLavoro;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaPasticca;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaPranzo;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaTempo;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

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
    private String Oggetto3;
    private String Oggetto4;
    private String Oggetto5;

    public ChiamateWSImpostazioniOrari(Context context) {
        this.context = context;
    }

    public void SalvaLavori(String idLavoro, String Lavoro, String Indirizzo,
                            String DataInizio, String DataFine, String LatLng) {
        this.idOggetto = idLavoro;
        this.Oggetto1 = Lavoro;
        this.Oggetto2 = Indirizzo;
        this.Oggetto3 = DataInizio;
        this.Oggetto4 = DataFine;
        this.Oggetto5 = LatLng;

        String Urletto="GestioneLavoro?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idLavoro=" + idLavoro +
                "&Lavoro=" + Lavoro +
                "&Indirizzo=" + Indirizzo +
                "&DataInizio=" + DataInizio +
                "&DataFine=" + DataFine +
                "&LatLng=" + LatLng;

        TipoOperazione = "GestioneLavoro";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void EliminaLavoro(String idLavoro) {
        String Urletto="EliminaLavoro?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idLavoro=" + idLavoro;

        TipoOperazione = "EliminaLavoro";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void EliminaPasticca(String idPasticca) {
        String Urletto="EliminaPasticca?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idPasticca=" + idPasticca;

        TipoOperazione = "EliminaPasticca";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
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

    public void SalvaPasticca(String idPasticca, String Pasticca) {
        this.idOggetto = idPasticca;
        this.Oggetto1 = Pasticca;

        String Urletto="GestionePasticca?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idPasticca=" + idPasticca +
                "&Pasticca=" + Pasticca;

        TipoOperazione = "GestionePasticca";

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

    public void EliminaTempo(String idTempo) {
        String Urletto="EliminaTempo?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idTempo=" + idTempo;

        TipoOperazione = "EliminaTempo";

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

    public void SalvaTempo(String idTempo, String Tempo) {
        this.idOggetto = idTempo;
        this.Oggetto1 = Tempo;

        String Urletto="GestioneTempo?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idTempo=" + idTempo +
                "&Tempo=" + Tempo;

        TipoOperazione = "GestioneTempo";

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
                    case "GestioneLavoro":
                        fGestioneLavoro(result);
                        break;
                    case "EliminaLavoro":
                        fEliminaLavoro(result);
                        break;
                    case "GestioneTempo":
                        fGestioneTempo(result);
                        break;
                    case "EliminaTempo":
                        fEliminaTempo(result);
                        break;
                    case "GestionePasticca":
                        fGestionePasticca(result);
                        break;
                    case "EliminaPasticca":
                        fEliminaPasticca(result);
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

    private void fEliminaPasticca(String result) {
        boolean ritorno = ControllaRitorno("Ritorno elimina pasticca", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Pasticca eliminata");
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

    private void fEliminaTempo(String result) {
        boolean ritorno = ControllaRitorno("Ritorno elimina tempo", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Tempo eliminato");
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

    private void fGestioneLavoro(String result) {
        boolean ritorno = ControllaRitorno("Ritorno gestione lavoro", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (idOggetto.equals("-1")) {
                StrutturaLavoro s = new StrutturaLavoro();
                s.setIdLavoro(Integer.parseInt(result));
                s.setLavoro(Oggetto1);
                s.setIndirizzo(Oggetto2);
                s.setDataInizio(Oggetto3);
                s.setDataFine(Oggetto4);
                s.setLatlng(Oggetto5);

                VariabiliStaticheOrari.getInstance().getStrutturaDati().getLavori().add(s);
            } else {
                for (StrutturaLavoro s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getLavori()) {
                    if (idOggetto.equals(Integer.toString(s.getIdLavoro()))) {
                        s.setLavoro(Oggetto1);
                        s.setIndirizzo(Oggetto2);
                        s.setDataInizio(Oggetto3);
                        s.setDataFine(Oggetto4);
                        s.setLatlng(Oggetto5);
                    }
                }
            }
            VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptLavori().updateData(
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());

            UtilitiesGlobali.getInstance().ApreToast(context, "Lavoro salvato");
        }
    }

    private void fEliminaLavoro(String result) {
        boolean ritorno = ControllaRitorno("Ritorno elimina lavoro", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Lavoro eliminato");
        }
    }

    private void fGestioneTempo(String result) {
        boolean ritorno = ControllaRitorno("Ritorno gestione tempo", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (idOggetto.equals("-1")) {
                StrutturaTempo s = new StrutturaTempo();
                s.setIdTempo(Integer.parseInt(result));
                s.setTempo(Oggetto1);

                VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi().add(s);
            } else {
                for (StrutturaTempo s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi()) {
                    if (idOggetto.equals(Integer.toString(s.getIdTempo()))) {
                        s.setTempo(Oggetto1);
                    }
                }
            }
            VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptTempo().updateData(
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());

            UtilitiesGlobali.getInstance().ApreToast(context, "Tempo salvato");
        }
    }

    private void fGestionePasticca(String result) {
        boolean ritorno = ControllaRitorno("Ritorno gestione pasticca", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (idOggetto.equals("-1")) {
                StrutturaPasticca s = new StrutturaPasticca();
                s.setIdPasticca(Integer.parseInt(result));
                s.setPasticca(Oggetto1);

                VariabiliStaticheOrari.getInstance().getStrutturaDati().getPasticche().add(s);
            } else {
                for (StrutturaPasticca s : VariabiliStaticheOrari.getInstance().getStrutturaDati().getPasticche()) {
                    if (idOggetto.equals(Integer.toString(s.getIdPasticca()))) {
                        s.setPasticca(Oggetto1);
                    }
                }
            }
            VariabiliStaticheImpostazioniOrari.getInstance().getCstmAdptPasticche().updateData(
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtRicercaTestoNuovo().getText().toString());

            UtilitiesGlobali.getInstance().ApreToast(context, "Pasticca salvata");
        }
    }

}
