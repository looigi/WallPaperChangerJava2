package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.Mappe.MappeEGps.UtilityGPS;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.adapters.AdapterListenerImmaginiIR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.strutture.StrutturaGruppi;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.VariabiliStaticheImmaginiRaggruppate;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.adapters.AdapterListenerIR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.strutture.StrutturaImmagineRaggruppata;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

public class ChiamateWSIR implements TaskDelegateIR {
    private static final String NomeMaschera = "Chiamate_WS_IR";
    private final String RadiceWS = VariabiliStaticheMostraImmagini.UrlWS;
    private String ws = "newLooVF.asmx/";
    private String NS="http://newLooVF.org/";
    private String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private String TipoFile;

    public ChiamateWSIR(Context context) {
        this.context = context;
    }

    public void RitornaRaggruppamenti(String idCategoria) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheImmaginiRaggruppate.getInstance().getImgCaricamento(),
                true
        );

        String Urletto="";
        String NomeUrl = "";
        if (VariabiliStaticheImmaginiRaggruppate.getInstance().getModalita().equals("1")) {
            switch (VariabiliStaticheImmaginiRaggruppate.getInstance().getMetodo()) {
                case "1":
                    NomeUrl = "RaggruppaPerNome";
                    break;
                case "2":
                    NomeUrl = "RaggruppaPerNomeChatGPT";
                    break;
                case "3":
                    NomeUrl = "RaggruppaPerNomeChatGPTBiGrammi";
                    break;
            }
        } else {
            NomeUrl = "RaggruppaPerNomeStile2";
        }

        if (VariabiliStaticheImmaginiRaggruppate.getInstance().getModalita().equals("1")) {
            Urletto = NomeUrl + "?idCategoria=" + idCategoria + "&Precisione=" +
                    VariabiliStaticheImmaginiRaggruppate.getInstance().getPrecisione();
        } else {
            Urletto = NomeUrl + "?idCategoria=" + idCategoria;
        }

        TipoOperazione = "RitornaRaggruppamenti";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                3000000,
                ApriDialog);
    }

    public void RitornaRaggruppatePerFiltro(String idCategoria, String Filtro) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheImmaginiRaggruppate.getInstance().getImgCaricamento(),
                true
        );

        String Urletto="RitornaRaggruppatePerFiltro?idCategoria=" + idCategoria + "&Filtro=" + Filtro;

        TipoOperazione = "RitornaRaggruppatePerFiltro";

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
        // UtilityLazio.getInstance().ImpostaAttesa(true);

        long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = Long.toString(tsLong);

        InterrogazioneWSIR i = new InterrogazioneWSIR();
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
                        VariabiliStaticheImmaginiRaggruppate.getInstance().getImgCaricamento(),
                        false
                );

                switch (TipoOperazione) {
                    case "RitornaRaggruppamenti":
                        fRitornaRaggruppamenti(result);
                        break;
                    case "RitornaRaggruppatePerFiltro":
                        fRitornaRaggruppatePerFiltro(result);
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

    private void fRitornaRaggruppamenti(String result) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheGPS.getInstance().getImgAttesa(),
                false
        );

        boolean ritorno = ControllaRitorno("Ritorna raggruppamenti", result);
        if (!ritorno) {
            VariabiliStaticheImmaginiRaggruppate.getInstance().getTxtQuante().setText("");
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }
        String[] Risultati = result.split("§", -1);
        List<StrutturaGruppi> lista = new ArrayList<>();
        for (String r: Risultati) {
            if (!r.isEmpty()) {
                String[] c = r.split("%", -1);
                int Quante = Integer.parseInt(c[0]);
                String Filtro = c[1];

                StrutturaGruppi s = new StrutturaGruppi();
                s.setQuante(Quante);
                s.setFiltro(Filtro);

                lista.add(s);
            }
        }

        AdapterListenerIR cstmAdptFonti = new AdapterListenerIR(context, lista);
        VariabiliStaticheImmaginiRaggruppate.getInstance().getLstIR().setAdapter(cstmAdptFonti);
    }

    private void fRitornaRaggruppatePerFiltro(String result) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheGPS.getInstance().getImgAttesa(),
                false
        );

        boolean ritorno = ControllaRitorno("Ritorna raggruppamenti per filtro", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        List<StrutturaImmagineRaggruppata> lista = new ArrayList<>();

        JSONArray jObject = null;
        try {
            jObject = new JSONArray("[" + result + "]");
            JSONObject obj = jObject.getJSONObject(0);
            String dati = obj.getString("Immagini");

            JSONArray jObjectImm = new JSONArray(dati);
            for(int i = 0; i < jObjectImm.length(); i++){
                JSONObject obj2 = jObjectImm.getJSONObject(i);

                StrutturaImmagineRaggruppata sic = new StrutturaImmagineRaggruppata();
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
                sic.setUrlImmagine(obj2.getString("UrlImmagine"));
                sic.setSelezionata(false);

                lista.add(sic);
            }
            VariabiliStaticheImmaginiRaggruppate.getInstance().setListaImmagini(lista);

            VariabiliStaticheImmaginiRaggruppate.getInstance().getTxtQuante().setText("Immagini rilevate: " + lista.size());

            AdapterListenerImmaginiIR customAdapterT = new AdapterListenerImmaginiIR(
                    context,
                    lista);
            VariabiliStaticheImmaginiRaggruppate.getInstance().setCustomAdapterT(customAdapterT);
            VariabiliStaticheImmaginiRaggruppate.getInstance().getLstImmagini().setAdapter(customAdapterT);
        } catch (JSONException e) {
            int i = 0;
        }
    }
}
