package com.looigi.wallpaperchanger2.classeOrari.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeOrari.UtilityOrari;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters.AdapterListenerCommesseGestione;
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
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ChiamateWSOrari implements TaskDelegateOrari {
    private static final String NomeMaschera = "Chiamate_WS_ORARI";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheOrari.UrlWS;
    private final String ws = "Orari.asmx/";
    private final String NS="http://orariWSOrari.it/";
    private final String SA="http://orariWSOrari.it/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private boolean RiempieCombo;
    private boolean PerModificaCommessa;
    private boolean PerTipoLavoro;

    public ChiamateWSOrari(Context context) {
        this.context = context;
    }

    public void RitornaCommesseLavoro(String idLavoro, boolean PerModificaCommessa, boolean PerTipoLavoro) {
        this.PerModificaCommessa = PerModificaCommessa;
        this.PerTipoLavoro = PerTipoLavoro;

        String Urletto="RitornaCommesseLavoro?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idLavoro=" + idLavoro;

        TipoOperazione = "RitornaCommesseLavoro";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void AggiungeMeteo(String idMeteo, String Meteo, String Icona) {
        String Urletto="AggiungeMeteo?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&idMeteo=" + idMeteo +
                "&Meteo=" + Meteo +
                "&Icona=" + (Icona.replace("/", "-SL-").replace(":", "-2P-")
                    .replace("&", "-AN-").replace("?", "-PI-"));

        TipoOperazione = "AggiungeMeteo";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaDatiPerModifica(boolean RefreshDati, boolean RiempieCombo) {
        this.RiempieCombo = RiempieCombo;
        if (!RefreshDati) {
            String PathFile = VariabiliStaticheOrari.getInstance().getPathOrari();
            String NomeFile = "Dati.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                String Dati = Files.getInstance().LeggeFile(PathFile, NomeFile);
                if (!Dati.isEmpty()) {
                    fRitornaDatiPerModificaOrario(Dati);
                    return;
                }
            }
        }
        String Urletto="RitornaDatiPerModificaOrario?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente();

        TipoOperazione = "RitornaDatiPerModificaOrario";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void EliminaOrario() {
        Date datella = VariabiliStaticheOrari.getInstance().getDataAttuale();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datella);

        String Giorno = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String Mese = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String Anno = String.valueOf(calendar.get(Calendar.YEAR));

        String Urletto="EliminaOrario?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&Giorno=" + Giorno +
                "&Mese=" + Mese +
                "&Anno=" + Anno;

        TipoOperazione = "EliminaOrario";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void ScriveOrario() {
        StrutturaDatiGiornata s = VariabiliStaticheOrari.getInstance().getDatiGiornata();

        if (s.getQuanteOre() > 0) {
            if (VariabiliStaticheOrari.getInstance().isPrendeCommessePerSalvataggio()) {
                VariabiliStaticheOrari.getInstance().setPrendeCommessePerSalvataggio(false);
                if (VariabiliStaticheOrari.getInstance().getListaCommesse() == null || VariabiliStaticheOrari.getInstance().getListaCommesse().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Nessuna commessa valida per il lavoro");
                    return;
                }
            }

            if (!VariabiliStaticheOrari.getInstance().getDatiGiornata().isSoloNote()) {
                if (VariabiliStaticheOrari.getInstance().getListaCommesse() == null ||
                        VariabiliStaticheOrari.getInstance().getListaCommesse().isEmpty()) {
                    VariabiliStaticheOrari.getInstance().setPrendeCommessePerSalvataggio(true);
                    RitornaCommesseLavoro(String.valueOf(s.getIdLavoro()), false, false);
                    return;
                }
            }
        }

        Date datella = VariabiliStaticheOrari.getInstance().getDataAttuale();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datella);

        String Giorno = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String Mese = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String Anno = String.valueOf(calendar.get(Calendar.YEAR));

        int QuanteOre = -9999;
        int idCommessa = -1;
        if (!VariabiliStaticheOrari.getInstance().getDatiGiornata().isSoloNote() &&
            VariabiliStaticheOrari.getInstance().getDatiGiornata().isGiornoInserito()) {
            QuanteOre = s.getQuanteOre();

            if (s.getCommessa() != null && !s.getCommessa().isEmpty()) {
                if (VariabiliStaticheOrari.getInstance().getListaCommesse() != null) {
                    for (StrutturaCommesse sc : VariabiliStaticheOrari.getInstance().getListaCommesse()) {
                        if (s.getCommessa().equals(sc.getDescrizione())) {
                            idCommessa = sc.getIdCommessa();
                            break;
                        }
                    }
                }
            }
        }

        String Pranzo = "";
        if (!VariabiliStaticheOrari.getInstance().getDatiGiornata().isSoloNote() &&
                VariabiliStaticheOrari.getInstance().getDatiGiornata().isGiornoInserito()) {
            for (StrutturaPranzo sp : s.getPranzo()) {
                Pranzo += sp.getIdPortata() + ";";
            }
        }

        String MezziAndata = "";
        if (!VariabiliStaticheOrari.getInstance().getDatiGiornata().isSoloNote() &&
                VariabiliStaticheOrari.getInstance().getDatiGiornata().isGiornoInserito()) {
            for (StrutturaMezzi sm : s.getMezziAndata()) {
                MezziAndata += sm.getIdMezzo() + ";";
            }
        }

        String MezziRitorno = "";
        if (!VariabiliStaticheOrari.getInstance().getDatiGiornata().isSoloNote() &&
                VariabiliStaticheOrari.getInstance().getDatiGiornata().isGiornoInserito()) {
            for (StrutturaMezzi sm : s.getMezziRitorno()) {
                MezziRitorno += sm.getIdMezzo() + ";";
            }
        }

        int Pasticca = -1;
        if (!VariabiliStaticheOrari.getInstance().getDatiGiornata().isSoloNote() &&
                VariabiliStaticheOrari.getInstance().getDatiGiornata().isGiornoInserito()) {
            if (!s.getPasticca().isEmpty()) {
                Pasticca = s.getPasticca().get(0).getIdPasticca();
            }
        }

        String idTempo = "";
        if (!VariabiliStaticheOrari.getInstance().getDatiGiornata().isSoloNote() &&
                VariabiliStaticheOrari.getInstance().getDatiGiornata().isGiornoInserito() &&
                VariabiliStaticheOrari.getInstance().getDatiGiornata().getQuanteOre() > 0) {
            String Tempo = VariabiliStaticheOrari.getInstance().getTxtTempo().getText().toString(); // s.getTempo();
            for (StrutturaTempo st : VariabiliStaticheOrari.getInstance().getStrutturaDati().getTempi()) {
                if (Tempo.equals(st.getTempo())) {
                    String Gradi = VariabiliStaticheOrari.getInstance().getEdtGradi().getText().toString();
                    idTempo = st.getIdTempo() + ";" + Gradi;
                    break;
                }
            }
        }

        String Urletto = "ScriveOrario?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&Giorno=" + Giorno +
                "&Mese=" + Mese +
                "&Anno=" + Anno +
                "&QuanteOre=" + QuanteOre +
                "&Note=" + s.getNote() +
                "&Misti=" + s.getMisti() +
                "&CodCommessa=" + idCommessa +
                "&Entrata=" + s.getEntrata() +
                "&idLavoro=" + s.getIdLavoro() +
                "&idIndirizzo=" + s.getIdIndirizzo() +
                "&Km=" + s.getKm() +
                "&Pranzo=" + Pranzo +
                "&Pasticca=" + Pasticca +
                "&MezziAndata=" + MezziAndata +
                "&MezziRitorno=" + MezziRitorno +
                "&Tempo=" + idTempo;

        TipoOperazione = "ScriveOrario";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                20000,
                ApriDialog);
    }

    public void RitornaDatiGiorno() {
        if (VariabiliStaticheOrari.getInstance().getChiamataInCorso() != null) {
            VariabiliStaticheOrari.getInstance().getChiamataInCorso().BloccaEsecuzione();
            VariabiliStaticheOrari.getInstance().setChiamataInCorso(null);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(VariabiliStaticheOrari.getInstance().getDataAttuale());

        int Giorno = calendar.get(Calendar.DAY_OF_MONTH);
        int Mese = calendar.get(Calendar.MONTH) + 1;
        int Anno = calendar.get(Calendar.YEAR);

        String Urletto="RitornaOrario?" +
                "idUtente=" + VariabiliStaticheOrari.getInstance().getIdUtente() +
                "&Giorno=" + Giorno +
                "&Mese=" + Mese +
                "&Anno=" + Anno;

        TipoOperazione = "RitornaOrario";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {
        VariabiliStaticheOrari.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSOrari i = new InterrogazioneWSOrari();
        VariabiliStaticheOrari.getInstance().setChiamataInCorso(i);
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
                    case "RitornaOrario":
                        fRitornaOrario(result);
                        break;
                    case "RitornaDatiPerModificaOrario":
                        fRitornaDatiPerModificaOrario(result);
                        break;
                    case "RitornaCommesseLavoro":
                        fRitornaCommesseLavoro(result);
                        break;
                    case "ScriveOrario":
                        fScriveOrario(result);
                        break;
                    case "EliminaOrario":
                        fEliminaOrario(result);
                        break;
                    case "AggiungeMeteo":
                        fAggiungeMeteo(result);
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

    private void fAggiungeMeteo(String result) {
        boolean ritorno = ControllaRitorno("Ritorno aggiunge meteo", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {

        }
    }

    private void fEliminaOrario(String result) {
        boolean ritorno = ControllaRitorno("Ritorno elimina orario", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilityOrari.getInstance().ScriveDatiGiornata(context);

            UtilitiesGlobali.getInstance().ApreToast(context, "Giornata eliminata");
        }
    }

    private void fScriveOrario(String result) {
        boolean ritorno = ControllaRitorno("Ritorno scrive orario", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Giornata salvata");
        }
    }

    private void fRitornaCommesseLavoro(String result) {
        boolean ritorno = ControllaRitorno("Ritorno commesse lavoro", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            JSONArray jObject = null;
            try {
                jObject = new JSONArray(result);
                List<StrutturaCommesse> listaCommesse = new ArrayList<>();
                for(int i = 0; i < jObject.length(); i++) {
                    JSONObject objCommesse = jObject.getJSONObject(i);

                    StrutturaCommesse s = new StrutturaCommesse();
                    s.setIdCommessa(objCommesse.getInt("idCommessa"));
                    s.setDescrizione(objCommesse.getString("Descrizione"));
                    s.setCommessa(objCommesse.getString("Commessa"));
                    s.setCliente(objCommesse.getString("Cliente"));
                    s.setIndirizzo(objCommesse.getString("Indirizzo"));
                    s.setLatLng(objCommesse.getString("LatLng"));
                    s.setDataInizio(objCommesse.getString("DataInizio"));
                    s.setDataFine(objCommesse.getString("DataFine"));

                    listaCommesse.add(s);
                }
                VariabiliStaticheOrari.getInstance().setListaCommesse(listaCommesse);

                if (PerTipoLavoro) {
                    StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                    String CommessaDefault = "";
                    for (StrutturaCommesse s : VariabiliStaticheOrari.getInstance().getListaCommesse()) {
                        if (s.getIdCommessa() == sdg.getCommessaDefault()) {
                            CommessaDefault = s.getDescrizione();
                            sdg.setCommessa(s.getDescrizione());
                            sdg.setCodCommessa(s.getIdCommessa());
                            break;
                        }
                    }
                    VariabiliStaticheOrari.getInstance().getTxtCommessa().setText(
                            CommessaDefault
                    );
                } else {
                    if (PerModificaCommessa) {
                        AdapterListenerCommesseGestione adapter =
                                new AdapterListenerCommesseGestione(
                                        context,
                                        VariabiliStaticheOrari.getInstance().getListaCommesse()
                                );
                        VariabiliStaticheImpostazioniOrari.getInstance().getLstCommesse().setAdapter(adapter);
                    } else {
                        StrutturaDatiGiornata sdg = VariabiliStaticheOrari.getInstance().getDatiGiornata();
                        String[] lista = new String[listaCommesse.size()];
                        int i = 0;
                        int qualeRiga = -1;
                        String commessaDefault = "";
                        for (StrutturaCommesse s : listaCommesse) {
                            if (sdg != null && sdg.isGiornoInserito()) {
                                if (s.getDescrizione().equals(sdg.getCommessa())) {
                                    qualeRiga = i;
                                }
                            } else {
                                if (s.getIdCommessa() == sdg.getCodCommessa()) {
                                    commessaDefault = s.getDescrizione();
                                }
                            }
                            lista[i] = s.getDescrizione();
                            i++;
                        }

                        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        //         (context, android.R.layout.simple_spinner_item, lista);
                        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                                context,
                                lista
                        );
                        VariabiliStaticheOrari.getInstance().getSpnValori().setAdapter(adapter);

                        if (sdg == null || !sdg.isGiornoInserito()) {
                            VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(commessaDefault);
                        } else {
                            VariabiliStaticheOrari.getInstance().getSpnValori().setPrompt(sdg.getCommessa());
                        }
                        VariabiliStaticheOrari.getInstance().getSpnValori().setSelection(qualeRiga);

                        if (VariabiliStaticheOrari.getInstance().isPrendeCommessePerSalvataggio()) {
                            Handler handlerTimer = new Handler(Looper.getMainLooper());
                            Runnable rTimer = new Runnable() {
                                public void run() {
                                    ScriveOrario();
                                }
                            };
                            handlerTimer.postDelayed(rTimer, 100);
                        }
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private void fRitornaDatiPerModificaOrario(String result) {
        boolean ritorno = ControllaRitorno("Ritorno Dati Generali", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            if (result.isEmpty()) {
                return;
            }
            String PathFile = VariabiliStaticheOrari.getInstance().getPathOrari();
            String NomeFile = "Dati.txt";
            if (Files.getInstance().EsisteFile(PathFile + "/" + NomeFile)) {
                Files.getInstance().EliminaFile(PathFile, NomeFile);
            }
            Files.getInstance().ScriveFile(PathFile, NomeFile, result);

            JSONArray jObject = null;
            try {
                jObject = new JSONArray("[" + result + "]");
                JSONObject obj = jObject.getJSONObject(0);

                String Tempi = obj.getString("Tempi");
                JSONArray jObjTempi = new JSONArray(Tempi);
                List<StrutturaTempo> listaTempi = new ArrayList<>();
                for(int i = 0; i < jObjTempi.length(); i++) {
                    JSONObject objTempi = jObjTempi.getJSONObject(i);
                    StrutturaTempo s = new StrutturaTempo();
                    s.setIdTempo(objTempi.getInt("idTempo"));
                    s.setTempo(objTempi.getString("Tempo"));
                    String icona = objTempi.getString("Icona");
                    icona = icona.replace("-SL-","/").replace("-2P-", ":")
                            .replace("-AN-", "&").replace("-PI-","?");
                    s.setUrlIcona(icona);

                    listaTempi.add(s);
                }

                String Lavori = obj.getString("Lavori");
                JSONArray jObjLavori = new JSONArray(Lavori);
                List<StrutturaLavoro> listaLavori = new ArrayList<>();
                for(int i = 0; i < jObjLavori.length(); i++) {
                    JSONObject objLavori = jObjLavori.getJSONObject(i);
                    StrutturaLavoro s = new StrutturaLavoro();
                    s.setIdLavoro(objLavori.getInt("idLavoro"));
                    s.setLavoro(objLavori.getString("Lavoro"));
                    s.setIndirizzo(objLavori.getString("Indirizzo"));
                    s.setDataInizio(objLavori.getString("DataInizio"));
                    s.setDataFine(objLavori.getString("DataFine"));
                    s.setLatlng(objLavori.getString("LatLng"));

                    listaLavori.add(s);
                }

                String Portate = obj.getString("Portate");
                JSONArray jObjPortate = new JSONArray(Portate);
                List<StrutturaPranzo> listaPortate = new ArrayList<>();
                for(int i = 0; i < jObjPortate.length(); i++) {
                    JSONObject objPortate = jObjPortate.getJSONObject(i);
                    StrutturaPranzo s = new StrutturaPranzo();
                    s.setIdPortata(objPortate.getInt("idPortata"));
                    s.setPortata(objPortate.getString("Portata"));

                    listaPortate.add(s);
                }

                String Indirizzi = obj.getString("Indirizzi");
                JSONArray jObjIndirizzi = new JSONArray(Indirizzi);
                List<StrutturaIndirizzi> listaIndirizzi = new ArrayList<>();
                for(int i = 0; i < jObjIndirizzi.length(); i++) {
                    JSONObject objIndirizzi = jObjIndirizzi.getJSONObject(i);
                    StrutturaIndirizzi s = new StrutturaIndirizzi();
                    s.setIdIndirizzo(objIndirizzi.getInt("idIndirizzo"));
                    s.setIndirizzo(objIndirizzi.getString("Indirizzo"));
                    s.setLatLng(objIndirizzi.getString("LatLng"));

                    listaIndirizzi.add(s);
                }

                String Mezzi = obj.getString("Mezzi");
                JSONArray jObjMezzi = new JSONArray(Mezzi);
                List<StrutturaMezzi> listaMezzi = new ArrayList<>();
                for(int i = 0; i < jObjMezzi.length(); i++) {
                    JSONObject objMezzi = jObjMezzi.getJSONObject(i);
                    StrutturaMezzi s = new StrutturaMezzi();
                    s.setIdMezzo(objMezzi.getInt("idMezzo"));
                    s.setMezzo(objMezzi.getString("Mezzo"));
                    s.setDettaglio(objMezzi.getString("Dettaglio"));

                    listaMezzi.add(s);
                }

                String Pasticche = obj.getString("Pasticche");
                JSONArray jObjPasticche = new JSONArray(Pasticche);
                List<StrutturaPasticca> listaPasticche = new ArrayList<>();
                for(int i = 0; i < jObjPasticche.length(); i++) {
                    JSONObject objPasticche = jObjPasticche.getJSONObject(i);
                    StrutturaPasticca s = new StrutturaPasticca();
                    s.setIdPasticca(objPasticche.getInt("idPasticca"));
                    s.setPasticca(objPasticche.getString("Pasticca"));

                    listaPasticche.add(s);
                }

                StrutturaDati s2 = new StrutturaDati();
                s2.setIndirizzi(listaIndirizzi);
                s2.setLavori(listaLavori);
                s2.setMezzi(listaMezzi);
                s2.setPortate(listaPortate);
                s2.setTempi(listaTempi);
                s2.setPasticche(listaPasticche);

                VariabiliStaticheOrari.getInstance().setStrutturaDati(s2);

                if (RiempieCombo) {
                    UtilityOrari.getInstance().RiempieMezzi(context);
                }
            } catch (JSONException e) {
                UtilitiesGlobali.getInstance().ApreToast(context, UtilityDetector.getInstance().PrendeErroreDaException(e));
            }
        }
    }

    private void fRitornaOrario(String result) {
        boolean ritorno = ControllaRitorno("Ritorno Orario", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            try {
                JSONArray jObject = new JSONArray("[" + result + "]");
                JSONObject obj = jObject.getJSONObject(0);

                StrutturaDatiGiornata sdg = new StrutturaDatiGiornata();
                sdg.setGiornoInserito(obj.getBoolean("GiornoInserito"));
                sdg.setQuanteOre(obj.getInt("Quante"));
                sdg.setNote(obj.getString("Notelle"));
                sdg.setMisti(obj.getString("Misti"));
                sdg.setCodCommessa(obj.getInt("CodCommessa"));
                sdg.setEntrata(obj.getString("Entrata"));
                sdg.setIdLavoro(obj.getInt("idLavoro"));
                sdg.setIdIndirizzo(obj.getInt("idIndirizzo"));
                sdg.setKm(obj.getString("Km"));
                sdg.setLavoro(obj.getString("Lavoro"));
                sdg.setIndirizzo(obj.getString("Indirizzo"));
                sdg.setCommessa(obj.getString("Commessa"));
                sdg.setTempo(obj.getString("Tempo"));
                sdg.setGradi(obj.getString("Gradi"));

                if (!sdg.getNote().isEmpty() && sdg.getQuanteOre() == -9999) {
                    sdg.setSoloNote(true);
                }

                List<StrutturaMezzi> listaMezziAndata = new ArrayList<>();
                String MezziAndata = obj.getString("MezziAndata");
                JSONArray jObjMezziAndata = new JSONArray(MezziAndata);
                for(int i = 0; i < jObjMezziAndata.length(); i++) {
                    JSONObject objMA = jObjMezziAndata.getJSONObject(i);

                    StrutturaMezzi sms = new StrutturaMezzi();
                    sms.setMezzo(objMA.getString("Mezzo"));
                    sms.setDettaglio(objMA.getString("Dettaglio"));

                    listaMezziAndata.add(sms);
                }
                sdg.setMezziAndata(listaMezziAndata);

                List<StrutturaMezzi> listaMezziRitorno = new ArrayList<>();
                String MezziRitorno = obj.getString("MezziRitorno");
                JSONArray jObjMezziRitorno = new JSONArray(MezziRitorno);
                for(int i = 0; i < jObjMezziRitorno.length(); i++) {
                    JSONObject objMR = jObjMezziRitorno.getJSONObject(i);

                    StrutturaMezzi sms = new StrutturaMezzi();
                    sms.setMezzo(objMR.getString("Mezzo"));
                    sms.setDettaglio(objMR.getString("Dettaglio"));

                    listaMezziRitorno.add(sms);
                }
                sdg.setMezziRitorno(listaMezziRitorno);

                List<StrutturaPranzo> listaPranzo = new ArrayList<>();
                String Pranzo = obj.getString("Pranzo");
                JSONArray jObjPranzo = new JSONArray(Pranzo);
                for(int i = 0; i < jObjPranzo.length(); i++) {
                    JSONObject objP = jObjPranzo.getJSONObject(i);

                    StrutturaPranzo sms = new StrutturaPranzo();
                    sms.setIdPortata(objP.getInt("idPortata"));
                    sms.setPortata(objP.getString("Portata"));

                    listaPranzo.add(sms);
                }
                sdg.setPranzo(listaPranzo);

                List<StrutturaPasticca> listaPasticca = new ArrayList<>();
                String Pasticca = obj.getString("Pasticca");
                JSONArray jObjPasticca = new JSONArray(Pasticca);
                for(int i = 0; i < jObjPasticca.length(); i++) {
                    JSONObject objP = jObjPasticca.getJSONObject(i);

                    StrutturaPasticca sms = new StrutturaPasticca();
                    sms.setIdPasticca(objP.getInt("idPasticca"));
                    sms.setPasticca(objP.getString("Pasticca"));

                    listaPasticca.add(sms);
                }
                sdg.setPasticca(listaPasticca);

                sdg.setSanto(obj.getString("Santo"));

                List<StrutturaRicorrenze> listaRicorrenze = new ArrayList<>();
                String Ricorrenze = obj.getString("Ricorrenze");
                JSONArray jObjRicorrenze = new JSONArray(Ricorrenze);
                for(int i = 0; i < jObjRicorrenze.length(); i++) {
                    JSONObject objR = jObjRicorrenze.getJSONObject(i);

                    StrutturaRicorrenze sms = new StrutturaRicorrenze();
                    sms.setAnno(objR.getInt("Anno"));
                    sms.setDescrizione(objR.getString("Descrizione"));

                    listaRicorrenze.add(sms);
                }
                sdg.setRicorrenze(listaRicorrenze);

                List<StrutturaMezziStandard> listaMezziStandardAndata = new ArrayList<>();
                String MezziStandardAndata = obj.getString("MezziStandardAndata");
                JSONArray jObjMezziStandardAndata = new JSONArray(MezziStandardAndata);
                for(int i = 0; i < jObjMezziStandardAndata.length(); i++) {
                    JSONObject objMSA = jObjMezziStandardAndata.getJSONObject(i);

                    StrutturaMezziStandard sms = new StrutturaMezziStandard();
                    sms.setIdMezzo(objMSA.getInt("idMezzo"));

                    listaMezziStandardAndata.add(sms);
                }
                sdg.setMezziStandardAndata(listaMezziStandardAndata);
                VariabiliStaticheOrari.getInstance().setListaMezziAndataStandard(listaMezziStandardAndata);

                List<StrutturaMezziStandard> listaMezziStandardRitorno = new ArrayList<>();
                String MezziStandardRitorno = obj.getString("MezziStandardRitorno");
                JSONArray jObjMezziStandardRitorno = new JSONArray(MezziStandardRitorno);
                for(int i = 0; i < jObjMezziStandardRitorno.length(); i++) {
                    JSONObject objMSA = jObjMezziStandardRitorno.getJSONObject(i);

                    StrutturaMezziStandard sms = new StrutturaMezziStandard();
                    sms.setIdMezzo(objMSA.getInt("idMezzo"));

                    listaMezziStandardRitorno.add(sms);
                }
                sdg.setMezziStandardRitorno(listaMezziStandardRitorno);
                VariabiliStaticheOrari.getInstance().setListaMezziRitornoStandard(listaMezziStandardRitorno);

                sdg.setCommessaDefault(obj.getInt("CommessaDefault"));
                sdg.setLavoroDefault(obj.getInt("LavoroDefault"));
                sdg.setOreStandard(obj.getInt("OreStandard"));

                /* {"GiornoInserito": true, "Quante": 8, "Notelle": "Ajax-Lazio 1-3 Tchaouna-Tele Bashiru-Pedro", "Misti": "",
                "CodCommessa": 52, "Entrata": "08:00:00", "idLavoro": 12, "idIndirizzo": -9999, "Km": "0.00", "Lavoro": "Aubay",
                "Indirizzo": "", "Commessa": "Ants", "Tempo": "Freddo ma bello", "Gradi": "7",
                "MezziAndata": [{"Mezzo": "Treno 6:38", "Dettaglio": "4541"},{"Mezzo": "Treno", "Dettaglio": "Muratella"}],
                "MezziRitorno": [{"Mezzo": "Treno", "Dettaglio": "Muratella"},{"Mezzo": "Treno", "Dettaglio": "17:15 - 7514"}],
                "Pranzo": [{"idPortata": 122,"Portata": "Insalata Col Tonno"}],
                "Pasticca": [{"idPasticca": -1,"Pasticca": ""}], "Santo": "Giovanna",
                "Ricorrenze": [{"Anno": 1901,"Descrizione": "***S***Prima comunicazione che costitu? il primo segnale radio transoceanico***F***"}],
                "MezziStandardAndata": [{"idMezzo": 81},{"idMezzo": 84},{"idMezzo": 42}],
                "MezziStandardRitorno": [{"idMezzo": 42},{"idMezzo": 18}],
                "CommessaDefault": 52, "LavoroDefault": 12, "OreStandard": 8 } */

                VariabiliStaticheOrari.getInstance().setDatiGiornata(sdg);

                UtilityOrari.getInstance().ScriveDatiGiornata(context);
            } catch (JSONException e) {
                int a = 0;
            }
        }
    }
}
