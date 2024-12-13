package com.looigi.wallpaperchanger2.classeOrari.webService;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeOrari.UtilityOrari;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaDatiGiornata;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezziStandard;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPasticca;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPranzo;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaRicorrenze;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

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

    public ChiamateWSOrari(Context context) {
        this.context = context;
    }

    public void RitornaDatiGiorno() {
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
        // ControllaTempoEsecuzione = false;

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
        VariabiliStaticheOrari.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSOrari i = new InterrogazioneWSOrari();
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
