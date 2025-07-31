package com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.InterrogazioneWSMI;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.TaskDelegate;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.adapters.AdapterListenerImmaginiFuoricategoria;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.StrutturaImmagineFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSIFC implements TaskDelegate {
    private static final String NomeMaschera = "Chiamate_WS_Immagini_Fuori_Categoria";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmagini.UrlWS + "/";
    private String ws = "newLooVF.asmx/";
    private String NS="http://newLooVF.org/";
    private String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private Context context;
    private boolean ApriDialog = false;
    private String idImmagine;

    public ChiamateWSIFC(Context context) {
        this.context = context;
    }

    public void RitornaImmaginiFuoriCategoria(String ForzaRefrsh) {
        String Urletto="TrovaNomiSuDbFuoriDallaCategoria?" +
                "idCategoria=" + VariabiliImmaginiFuoriCategoria.getInstance().getIdCategoria() +
                "&Aliases1=" + VariabiliImmaginiFuoriCategoria.getInstance().getAlias1() +
                "&Aliases2=" + VariabiliImmaginiFuoriCategoria.getInstance().getAlias2() +
                "&QuantiCaratteri=" + VariabiliImmaginiFuoriCategoria.getInstance().getQuantiCaratteri() +
                "&AndOr=" + VariabiliImmaginiFuoriCategoria.getInstance().getAndOr() +
                "&SoloSuAltro=" + (VariabiliImmaginiFuoriCategoria.getInstance().isSoloSuAltro() ? "S" : "N") +
                "&CercaExif=" + (VariabiliImmaginiFuoriCategoria.getInstance().isCercaExif() ? "S" : "N") +
                "&Tag=" + VariabiliImmaginiFuoriCategoria.getInstance().getTag() +
                "&ForzaRefresh=" + ForzaRefrsh;

        TipoOperazione = "TrovaNomiSuDbFuoriDallaCategoria";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

        VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSMI i = new InterrogazioneWSMI();
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
                UtilityImmagini.getInstance().Attesa(false);

                switch (TipoOperazione) {
                    case "TrovaNomiSuDbFuoriDallaCategoria":
                        fTrovaNomiSuDbFuoriDallaCategoria(result);
                        break;
                }

                VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
        if (VariabiliStaticheMostraImmagini.getInstance().getClasseChiamata() != null) {
            VariabiliStaticheMostraImmagini.getInstance().getClasseChiamata().BloccaEsecuzione();
        }
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

    private void fTrovaNomiSuDbFuoriDallaCategoria(String result) {
        boolean ritorno = ControllaRitorno("TrovaNomiSuDbFuoriDallaCategoria", result);
        if (!ritorno) {
            AdapterListenerImmaginiFuoricategoria customAdapterT = new AdapterListenerImmaginiFuoricategoria(
                    context,
                    null);
            VariabiliImmaginiFuoriCategoria.getInstance().getLstImmagini().setAdapter(customAdapterT);

            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            List<StrutturaImmagineFuoriCategoria> lista = new ArrayList<>();

            JSONArray jObject = null;
            try {
                jObject = new JSONArray("[" + result + "]");
                JSONObject obj = jObject.getJSONObject(0);
                String dati = obj.getString("Immagini");
                VariabiliImmaginiFuoriCategoria.getInstance().getTxtQuanteImmaginiRilevate().setText("Immagini rilevate: " +
                        obj.getInt("Rilevate"));

                JSONArray jObjectImm = new JSONArray(dati);
                for(int i = 0; i < jObjectImm.length(); i++){
                    JSONObject obj2 = jObjectImm.getJSONObject(i);

                    StrutturaImmagineFuoriCategoria sic = new StrutturaImmagineFuoriCategoria();
                    sic.setIdImmagine(obj2.getInt("idImmagine"));
                    sic.setIdCategoria(obj2.getInt("idCategoria"));
                    sic.setCategoria(obj2.getString("Categoria"));
                    sic.setAlias(obj2.getString("Alias"));
                    sic.setTag(obj2.getString("Tag"));
                    sic.setCartella(obj2.getString("Cartella"));
                    sic.setNomeFile(obj2.getString("NomeFile"));
                    sic.setDimensioneFile(obj2.getLong("DimensioneFile"));
                    sic.setDataCreazione(obj2.getString("DataCreazione"));
                    sic.setDataModifica(obj2.getString("DataModifica"));
                    sic.setDimensioniImmagine(obj2.getString("DimensioniImmagine"));
                    sic.setUrlImmagine(obj2.getString("UrlImmagine"));
                    sic.setPathImmagine(obj2.getString("PathImmagine"));
                    sic.setEsisteImmagine(obj2.getBoolean("EsisteImmagine"));
                    sic.setSelezionata(false);

                    lista.add(sic);
                }
                VariabiliImmaginiFuoriCategoria.getInstance().setListaImmagini(lista);

                AdapterListenerImmaginiFuoricategoria customAdapterT = new AdapterListenerImmaginiFuoricategoria(
                        context,
                        lista);
                VariabiliImmaginiFuoriCategoria.getInstance().getLstImmagini().setAdapter(customAdapterT);
            } catch (JSONException e) {
                int i = 0;
            }
        }
    }
}
