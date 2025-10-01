/* package com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.StrutturaImmagineFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.VariabiliStaticheImmaginiRaggruppate;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.adapters.AdapterListenerImmaginiIR;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.StrutturaImmaginiUguali;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.UtilityUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.VariabiliStaticheUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.adapters.AdapterListenerUI;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.StrutturaVoltiRilevati;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.VariabiliStaticheVolti;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeVolti.adapters.AdapterListenerListaVolti;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.InterrogazioneWSUI;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.TaskDelegateUI;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSV implements TaskDelegateV {
    private static final String NomeMaschera = "Chiamate_WS_V";
    private final String RadiceWS = VariabiliStaticheMostraImmagini.UrlWS + "/";
    private String ws = "newLooVF.asmx/";
    private String NS="http://newLooVF.org/";
    private String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;

    public ChiamateWSV(Context context) {
        this.context = context;
    }

    public void RitornaVoltiRilevati() {
        VariabiliStaticheVolti.getInstance().Attesa(true);

        String Urletto="RitornaVolti";

        TipoOperazione = "RitornaVoltiRilevati";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                6000000,
                ApriDialog);
    }

    public void AggiornaVoltoSpostato(String idImmagine) {
        VariabiliStaticheVolti.getInstance().Attesa(true);

        String Urletto="AggiornaVoltoSpostato";

        TipoOperazione = "AggiornaVoltoSpostato";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                6000000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {
        // UtilityLazio.getInstance().ImpostaAttesa(true);

        long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = Long.toString(tsLong);

        InterrogazioneWSV i = new InterrogazioneWSV();
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
                VariabiliStaticheVolti.getInstance().Attesa(false);

                switch (TipoOperazione) {
                    case "RitornaVoltiRilevati":
                        fRitornaVoltiRilevati(result);
                        break;
                    case "AggiornaVoltoSpostato":
                        fAggiornaVoltoSpostato(result);
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

    private void fAggiornaVoltoSpostato(String result) {
        boolean ritorno = ControllaRitorno("fAggiornaVoltoSpostato", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    ChiamateWSV ws = new ChiamateWSV(context);
                    ws.RitornaVoltiRilevati();
                }
            }, 500);
        }
    }

    private void fRitornaVoltiRilevati(String result) {
        boolean ritorno = ControllaRitorno("RitornaVoltiRilevati", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaVoltiRilevati> lista = new ArrayList<>();

            JSONArray jObject = null;
            try {
                jObject = new JSONArray("[" + result + "]");
                JSONObject obj = jObject.getJSONObject(0);
                String dati = obj.getString("Immagini");

                JSONArray jObjectImm = new JSONArray(dati);
                for(int i = 0; i < jObjectImm.length(); i++){
                    JSONObject obj2 = jObjectImm.getJSONObject(i);

                    StrutturaVoltiRilevati sic = new StrutturaVoltiRilevati();
                    sic.setIdImmagine(obj2.getInt("idImmagine"));
                    sic.setIdCategoriaVecchia(obj2.getInt("idCategoriaVecchia"));
                    sic.setCategoriaVecchia(obj2.getString("CategoriaVecchia"));
                    sic.setIdCategoriaNuova(obj2.getInt("idCategoriaNuova"));
                    sic.setCategoriaNuova(obj2.getString("CategoriaNuova"));
                    sic.setCartella(obj2.getString("Cartella"));
                    sic.setNomeFile(obj2.getString("NomeFile"));
                    sic.setConfidenza(obj2.getInt("Confidenza"));
                    sic.setUrlImmagine(obj2.getString("UrlImmagine"));
                    sic.setUrlImmagineNuova(obj2.getString("UrlImmagineNuova"));

                    lista.add(sic);
                }
                VariabiliStaticheVolti.getInstance().setListaVolti(lista);

                AdapterListenerListaVolti customAdapterT = new AdapterListenerListaVolti(
                        context,
                        lista);
                VariabiliStaticheVolti.getInstance().getLstVolti().setAdapter(customAdapterT);
            } catch (JSONException e) {
                int a = 0;
            }
        }
    }
}
*/