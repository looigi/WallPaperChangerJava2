package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.InterrogazioneWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.TaskDelegate;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.adapters.AdapterListenerImmaginiFuoricategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.StrutturaImmagineFuoriCategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

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
        String Alias1 = VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias1().getText().toString();
        String Alias2 = VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias2().getText().toString();
        String Tag = ""; // VariabiliImmaginiFuoriCategoria.getInstance().getEdtTag().getText().toString();
        String idCategoria = VariabiliImmaginiFuoriCategoria.getInstance().getIdCategoria();
        if (!VariabiliImmaginiFuoriCategoria.getInstance().getRicerca().isEmpty()) {
            idCategoria = "1";
        }

        VariabiliImmaginiFuoriCategoria.getInstance().setTestoRicercato(Alias1 + ";" + Alias2);

        String Urletto="TrovaNomiSuDbFuoriDallaCategoria?" +
                "idCategoria=" + idCategoria +
                "&Aliases1=" + Alias1 +
                "&Aliases2=" + Alias2 +
                "&QuantiCaratteri=" + VariabiliImmaginiFuoriCategoria.getInstance().getQuantiCaratteri().getText().toString() +
                "&AndOr=" + VariabiliImmaginiFuoriCategoria.getInstance().getAndOr() +
                "&SoloSuAltro=" + (VariabiliImmaginiFuoriCategoria.getInstance().isSoloSuAltro() ? "S" : "N") +
                "&CercaExif=" + (VariabiliImmaginiFuoriCategoria.getInstance().isCercaExif() ? "S" : "N") +
                "&Tag=" + Tag +
                "&ForzaRefresh=S"; // + (!Objects.equals(VariabiliImmaginiFuoriCategoria.getInstance().getIdCategoria(), "-1") ? ForzaRefrsh : "S");

        TipoOperazione = "TrovaNomiSuDbFuoriDallaCategoria";

        // Da categoria: TrovaNomiSuDbFuoriDallaCategoria?idCategoria=13&Aliases1=Adriana&Aliases2=karem&QuantiCaratteri=4&AndOr=And&SoloSuAltro=S&CercaExif=N&Tag=&ForzaRefresh=N
        // Da ricerca:   TrovaNomiSuDbFuoriDallaCategoria?idCategoria=-1&Aliases1=karem,&Aliases2=&QuantiCaratteri=4&AndOr=And&SoloSuAltro=S&CercaExif=N&Tag=&ForzaRefresh=S
        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                600000,
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
                UtilitiesGlobali.getInstance().AttesaGif(
                        context,
                        VariabiliStaticheMostraImmagini.getInstance().getImgCaricamento(),
                        false
                );

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
                String Ridotte = obj.getString("Ridotte");
                if (!Ridotte.equals("0")) {
                    UtilitiesGlobali.getInstance().VisualizzaMessaggio(
                            context,
                            "Immagini fuori categoria",
                            "Troppe immagini: Visualizzate " + Ridotte
                    );
                }
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
                    sic.setTags(obj2.getString("Tags"));
                    sic.setLuoghi(obj2.getString("Luoghi"));
                    sic.setOggetti(obj2.getString("Oggetti"));
                    sic.setVolti(obj2.getString("Volti"));
                    sic.setDescrizione(obj2.getString("Descrizione"));
                    sic.setTesto(obj2.getString("Testo"));

                    lista.add(sic);
                }
                VariabiliImmaginiFuoriCategoria.getInstance().setListaImmagini(lista);

                AdapterListenerImmaginiFuoricategoria customAdapterT = new AdapterListenerImmaginiFuoricategoria(
                        context,
                        lista);
                VariabiliImmaginiFuoriCategoria.getInstance().setAdapter(customAdapterT);
                VariabiliImmaginiFuoriCategoria.getInstance().getLstImmagini().setAdapter(customAdapterT);
            } catch (JSONException e) {
                UtilitiesGlobali.getInstance().ApreToast(context, e.getMessage());
            }
        }
    }
}
