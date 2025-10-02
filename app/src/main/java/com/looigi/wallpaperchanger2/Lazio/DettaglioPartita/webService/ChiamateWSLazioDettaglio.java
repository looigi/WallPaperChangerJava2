package com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Ammoniti;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Dettaglio;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Espulsi;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Formazione;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Marcatori;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Notelle;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.UtilityLazioDettaglio;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.VariabiliStaticheLazioDettaglio;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.adapters.AdapterListenerAmmoniti;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.adapters.AdapterListenerEspulsi;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.adapters.AdapterListenerFormazione;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.adapters.AdapterListenerMarcatoriD;
import com.looigi.wallpaperchanger2.Lazio.Strutture.StrutturaGiocatori;
import com.looigi.wallpaperchanger2.Lazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

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

    public void SalvaDettaglio(
        String idPartita,
        String idSquadraCasa,
        String idSquadraFuori,
        String Risultato,
        String Arbitro,
        String Localita,
        String Spettatori,
        String marcatoriCasa,
        String marcatoriFuori,
        String ammonitiCasa,
        String ammonitiFuori,
        String espulsiCasa,
        String espulsiFuori,
        String formazioneCasa,
        String formazioneFuori,
        String note) {

        String Urletto="SalvaDettaglio?" +
                "idAnno=" + VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato() +
                "&idTipologia=" + VariabiliStaticheLazio.getInstance().getIdTipologia()  +
                "&idPartita=" + idPartita +
                "&idGiornata=" + VariabiliStaticheLazio.getInstance().getGiornata() +
                "&DataPartita=" + VariabiliStaticheLazioDettaglio.getInstance().getData() +
                "&idSquadraCasa=" + idSquadraCasa +
                "&idSquadraFuori=" + idSquadraFuori +
                "&Risultato=" + Risultato +
                "&Arbitro=" + Arbitro +
                "&Localita=" + Localita +
                "&Spettatori=" + Spettatori +
                "&marcatoriCasa=" + marcatoriCasa +
                "&marcatoriFuori=" + marcatoriFuori +
                "&ammonitiCasa=" + ammonitiCasa +
                "&ammonitiFuori=" + ammonitiFuori +
                "&espulsiCasa=" + espulsiCasa +
                "&espulsiFuori=" + espulsiFuori +
                "&formazioneCasa=" + formazioneCasa +
                "&formazioneFuori=" + formazioneFuori +
                "&note=" + note;

        TipoOperazione = "SalvaDettaglio";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
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
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheLazioDettaglio.getInstance().getImgCaricamento(),
                true
        );

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
                UtilitiesGlobali.getInstance().AttesaGif(
                        context,
                        VariabiliStaticheLazioDettaglio.getInstance().getImgCaricamento(),
                        false
                );

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
                    s.setIdGiocatore(campi[0].isEmpty() ? 0 : Integer.parseInt(campi[0]));
                    s.setCognome(campi[1]);
                    s.setNome(campi[2]);
                    s.setIdRuolo(campi[3].isEmpty() ? 0 : Integer.parseInt(campi[3]));
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
            String[] CampiEsterni = result.split("\\|", -1);

            String[] marcCasa = CampiEsterni[0].split("§", -1);
            List<Marcatori> listaMC = new ArrayList<>();
            for (String mc : marcCasa) {
                if (!mc.isEmpty() && !mc.equals("\n")) {
                    if (mc.contains(";")) {
                        String[] Marcatore = mc.split(";", -1);

                        Marcatori m = new Marcatori();
                        m.setIdGiocatore(Marcatore[0].isEmpty() ? 0 : Integer.parseInt(Marcatore[0]));
                        m.setCognome(Marcatore[1]);
                        m.setNome(Marcatore[2]);
                        m.setMinuto(Marcatore[3].isEmpty() ? 0 : Integer.parseInt(Marcatore[3]));
                        m.setRigore(Marcatore[4].equals("S"));
                        m.setIdApiFootball(Marcatore[5].isEmpty() ? 0 : Integer.parseInt(Marcatore[5]));

                        listaMC.add(m);
                    }
                }
            }

            String[] marcFuori = CampiEsterni[1].split("§", -1);
            List<Marcatori> listaMF = new ArrayList<>();
            for (String mc : marcFuori) {
                if (!mc.isEmpty() && !mc.equals("\n")) {
                    if (mc.contains(";")) {
                        String[] Marcatore = mc.split(";", -1);

                        Marcatori m = new Marcatori();
                        m.setIdGiocatore(Marcatore[0].isEmpty() ? 0 : Integer.parseInt(Marcatore[0]));
                        m.setCognome(Marcatore[1]);
                        m.setNome(Marcatore[2]);
                        m.setMinuto(Marcatore[3].isEmpty() ? 0 : Integer.parseInt(Marcatore[3]));
                        m.setRigore(Marcatore[4].equals("S"));
                        m.setIdApiFootball(Marcatore[5].isEmpty() ? 0 : Integer.parseInt(Marcatore[5]));

                        listaMF.add(m);
                    }
                }
            }

            String[] ammCasa = CampiEsterni[2].split("§", -1);
            List<Ammoniti> listaAC = new ArrayList<>();
            for (String mc : ammCasa) {
                if (!mc.isEmpty() && !mc.equals("\n")) {
                    if (mc.contains(";")) {
                        String[] Ammonito = mc.split(";", -1);

                        Ammoniti m = new Ammoniti();
                        m.setIdGiocatore(Ammonito[0].isEmpty() ? 0 : Integer.parseInt(Ammonito[0]));
                        m.setCognome(Ammonito[1]);
                        m.setNome(Ammonito[2]);
                        m.setMinuto(Ammonito[3].isEmpty() ? 0 : Integer.parseInt(Ammonito[3]));
                        m.setIdApiFootball(Ammonito[4].isEmpty() ? 0 : Integer.parseInt(Ammonito[4]));

                        listaAC.add(m);
                    }
                }
            }

            String[] ammFuori = CampiEsterni[3].split("§", -1);
            List<Ammoniti> listaAF = new ArrayList<>();
            for (String mc : ammFuori) {
                if (!mc.isEmpty() && !mc.equals("\n")) {
                    if (mc.contains(";")) {
                        String[] Ammonito = mc.split(";", -1);

                        Ammoniti m = new Ammoniti();
                        m.setIdGiocatore(Ammonito[0].isEmpty() ? 0 : Integer.parseInt(Ammonito[0]));
                        m.setCognome(Ammonito[1]);
                        m.setNome(Ammonito[2]);
                        m.setMinuto(Ammonito[3].isEmpty() ? 0 : Integer.parseInt(Ammonito[3]));
                        m.setIdApiFootball(Ammonito[4].isEmpty() ? 0 : Integer.parseInt(Ammonito[4]));

                        listaAF.add(m);
                    }
                }
            }

            String[] espCasa = CampiEsterni[4].split("§", -1);
            List<Espulsi> listaEC = new ArrayList<>();
            for (String mc : espCasa) {
                if (!mc.isEmpty() && !mc.equals("\n")) {
                    if (mc.contains(";")) {
                        String[] Espulso = mc.split(";", -1);

                        Espulsi m = new Espulsi();
                        m.setIdGiocatore(Espulso[0].isEmpty() ? 0 : Integer.parseInt(Espulso[0]));
                        m.setCognome(Espulso[1]);
                        m.setNome(Espulso[2]);
                        m.setMinuto(Espulso[3].isEmpty() ? 0 : Integer.parseInt(Espulso[3]));
                        m.setIdApiFootball(Espulso[4].isEmpty() ? 0 : Integer.parseInt(Espulso[4]));

                        listaEC.add(m);
                    }
                }
            }

            String[] espFuori = CampiEsterni[5].split("§", -1);
            List<Espulsi> listaEF = new ArrayList<>();
            for (String mc : espFuori) {
                if (!mc.isEmpty() && !mc.equals("\n")) {
                    if (mc.contains(";")) {
                        String[] Espulso = mc.split(";", -1);

                        Espulsi m = new Espulsi();
                        m.setIdGiocatore(Espulso[0].isEmpty() ? 0 : Integer.parseInt(Espulso[0]));
                        m.setCognome(Espulso[1]);
                        m.setNome(Espulso[2]);
                        m.setMinuto(Espulso[3].isEmpty() ? 0 : Integer.parseInt(Espulso[3]));
                        m.setIdApiFootball(Espulso[4].isEmpty() ? 0 : Integer.parseInt(Espulso[4]));

                        listaEF.add(m);
                    }
                }
            }

            String[] formCasa = CampiEsterni[6].split("§", -1);
            List<Formazione> listaFC = new ArrayList<>();
            for (String mc : formCasa) {
                if (!mc.isEmpty() && !mc.equals("\n")) {
                    if (mc.contains(";")) {
                        String[] Formazione = mc.split(";", -1);

                        Formazione m = new Formazione();
                        m.setIdGiocatore(Formazione[0].isEmpty() ? 0 : Integer.parseInt(Formazione[0]));
                        m.setCognome(Formazione[1]);
                        m.setNome(Formazione[2]);
                        m.setEntrato(Formazione[3].isEmpty() ? 0 : Integer.parseInt(Formazione[3]));
                        m.setUscito(Formazione[4].isEmpty() ? 0 : Integer.parseInt(Formazione[4]));
                        m.setIdApiFootball(Formazione[5].isEmpty() ? 0 : Integer.parseInt(Formazione[5]));

                        listaFC.add(m);
                    }
                }
            }

            String[] formFuori = CampiEsterni[7].split("§", -1);
            List<Formazione> listaFF = new ArrayList<>();
            for (String mc : formFuori) {
                if (!mc.isEmpty() && !mc.equals("\n")) {
                    if (mc.contains(";")) {
                        String[] Formazione = mc.split(";", -1);

                        Formazione m = new Formazione();
                        m.setIdGiocatore(Formazione[0].isEmpty() ? 0 : Integer.parseInt(Formazione[0]));
                        m.setCognome(Formazione[1]);
                        m.setNome(Formazione[2]);
                        m.setEntrato(Formazione[3].isEmpty() ? 0 : Integer.parseInt(Formazione[3]));
                        m.setUscito(Formazione[4].isEmpty() ? 0 : Integer.parseInt(Formazione[4]));
                        m.setIdApiFootball(Formazione[5].isEmpty() ? 0 : Integer.parseInt(Formazione[5]));

                        listaFF.add(m);
                    }
                }
            }

            String[] Notelle = CampiEsterni[8].split(";", -1);
            Notelle n = new Notelle();
            n.setArbitro(Notelle[0]);
            n.setLocalita(Notelle[1]);
            n.setSpettatori(Notelle[2]);
            String notelline = Notelle[3];
            if (notelline != null) {
                notelline = notelline.replace("***PV***", ";");
            } else {
                notelline = "";
            }
            n.setNotelle(notelline);

            Dettaglio d = new Dettaglio();
            d.setAmmonitiCasa(listaAC);
            d.setAmmonitiFuori(listaAF);
            d.setEspulsiCasa(listaEC);
            d.setEspulsiFuori(listaEF);
            d.setMarcatoriCasa(listaMC);
            d.setMarcatoriFuori(listaMF);
            d.setFormazioneCasa(listaFC);
            d.setFormazioneFuori(listaFF);
            d.setNotelle(n);

            VariabiliStaticheLazioDettaglio.getInstance().setDettaglioPartita(d);

            VariabiliStaticheLazioDettaglio.getInstance().getEdtArbitro().setText(n.getArbitro());
            VariabiliStaticheLazioDettaglio.getInstance().getEdtSpettatori().setText(n.getSpettatori());
            VariabiliStaticheLazioDettaglio.getInstance().getEdtLocalita().setText(n.getLocalita());
            VariabiliStaticheLazioDettaglio.getInstance().getEdtNote().setText(notelline);

            AdapterListenerAmmoniti cstmAdptAC = new AdapterListenerAmmoniti(context, listaAC, true);
            VariabiliStaticheLazioDettaglio.getInstance().getLstAC().setAdapter(cstmAdptAC);

            AdapterListenerAmmoniti cstmAdptAF = new AdapterListenerAmmoniti(context, listaAF, false);
            VariabiliStaticheLazioDettaglio.getInstance().getLstAF().setAdapter(cstmAdptAF);

            AdapterListenerEspulsi cstmAdptEC = new AdapterListenerEspulsi(context, listaEC, true);
            VariabiliStaticheLazioDettaglio.getInstance().getLstEC().setAdapter(cstmAdptEC);

            AdapterListenerEspulsi cstmAdptEF = new AdapterListenerEspulsi(context, listaEF, false);
            VariabiliStaticheLazioDettaglio.getInstance().getLstEF().setAdapter(cstmAdptEF);

            AdapterListenerMarcatoriD cstmAdptMC = new AdapterListenerMarcatoriD(context, listaMC, true);
            VariabiliStaticheLazioDettaglio.getInstance().getLstMC().setAdapter(cstmAdptMC);

            AdapterListenerMarcatoriD cstmAdptMF = new AdapterListenerMarcatoriD(context, listaMF, false);
            VariabiliStaticheLazioDettaglio.getInstance().getLstMF().setAdapter(cstmAdptMF);

            AdapterListenerFormazione cstmAdptFC = new AdapterListenerFormazione(context, listaFC, true);
            VariabiliStaticheLazioDettaglio.getInstance().getLstFC().setAdapter(cstmAdptFC);

            AdapterListenerFormazione cstmAdptFF = new AdapterListenerFormazione(context, listaFF, false);
            VariabiliStaticheLazioDettaglio.getInstance().getLstFF().setAdapter(cstmAdptFF);
        }
    }
}
