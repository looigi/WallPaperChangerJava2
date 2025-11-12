package com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.webService;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.UtilitiesRilevaOCRJava;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.VariabiliStaticheRilevaOCRJava;
import com.looigi.wallpaperchanger2.Pennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.UtilitiesOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.VariabiliStaticheOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.adapters.AdapterListenerDestinazioni;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.adapters.AdapterListenerImmaginiOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.strutture.StrutturaDestinazioni;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.strutture.StrutturaImmaginiOCR;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSOCR implements TaskDelegateOCR {
    private static final String NomeMaschera = "Chiamate_WS_OCR";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmaginiPennetta.UrlWS;
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private String daDove;

    public ChiamateWSOCR(Context context) {
        this.context = context;
    }

    public void ImpostaImmagineSpostata() {
        String Urletto="ImpostaImmagineSpostata?" +
                "idImmagine=" + VariabiliStaticheOCR.getInstance().getIdImmagineDaSpostare();

        TipoOperazione = "ImpostaImmagineSpostata";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                250000,
                ApriDialog);
    }

    public void AggiornaCategorieOCR() {
        String Urletto="AggiornaCategorieOCR";

        TipoOperazione = "AggiornaCategorieOCR";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                6000000,
                ApriDialog);
    }

    public void RitornaDestinazioni() {
        String Urletto="";

        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheOCR.getInstance().getImgCaricamento(),
                true
        );

        if (VariabiliStaticheRilevaOCRJava.getInstance().getModalita() == 1) {
            Urletto = "TrovaImmaginiConDateUgualiSulNomeFile?" +
                    "SoloCategorieDiRicerca=S";
        } else {
            if (VariabiliStaticheRilevaOCRJava.getInstance().getModalita() == 2) {
                Urletto = "RitornaDestinazioni?" +
                        "AncheVuote=" + (VariabiliStaticheOCR.getInstance().isAncheDestinazioniVuote() ? "S" : "");
            } else {
                Urletto = "TrovaTagSulNomeFile?" +
                        "SoloCategorieDiRicerca=S";
            }
        }

        TipoOperazione = "RitornaDestinazioni";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                250000,
                ApriDialog);
    }

    private String RitornaFiltroSplittato(String Filtro) {
        String FiltroFinale = "";

        if (!Filtro.isEmpty()) {
            if (Filtro.contains("§")) {
                String[] s = Filtro.split("§", -1);
                for (String ss : s) {
                    if (ss.contains(";")) {
                        String[] s2 = ss.split(";", -1);
                        FiltroFinale += s2[1] + ";";
                    } else {
                        FiltroFinale += ss + ";";
                    }
                }
            } else {
                FiltroFinale = Filtro.replace(";", "");
            }
        }
        FiltroFinale = FiltroFinale.replace(";;", ";");
        if (FiltroFinale.endsWith(";")) {
            FiltroFinale = FiltroFinale.substring(0, FiltroFinale.length() - 1);
        }

        return FiltroFinale;
    }

    public void RitornaImmagini(String Filtro) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheOCR.getInstance().getImgCaricamento(),
                true
        );

        String Urletto="";

        if (VariabiliStaticheRilevaOCRJava.getInstance().getModalita() == 1) {
            Urletto = "RitornaImmaginiConDateUgualiSulNomeFile?" +
                    "Filtro=" + RitornaFiltroSplittato(Filtro) +
                    "&SoloCategorieDiRicerca=S";
        } else {
            if (VariabiliStaticheRilevaOCRJava.getInstance().getModalita() == 2) {
                Urletto = "RitornaImmaginiOCR?" +
                        "Filtro=" + RitornaFiltroSplittato(Filtro) +
                        "&AncheVuote=" + (VariabiliStaticheOCR.getInstance().isAncheDestinazioniVuote() ? "S" : "");
            } else {
                Urletto = "RitornaImmaginiConTagSulNomeFile?" +
                        "Filtro=" + RitornaFiltroSplittato(Filtro) +
                        "&SoloCategorieDiRicerca=S";
            }
        }

        TipoOperazione = "RitornaImmaginiOCR";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                250000,
                ApriDialog);
    }

    public void RitornaImmaginiFiltro(String Filtro) {
        String Urletto="RitornaImmaginiOCRDaFiltro?" +
                "Filtro=" + RitornaFiltroSplittato(Filtro);

        TipoOperazione = "RitornaImmaginiOCR";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                250000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheOCR.getInstance().getImgCaricamento(),
                true
        );

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSOCR i = new InterrogazioneWSOCR();
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
                        VariabiliStaticheOCR.getInstance().getImgCaricamento(),
                        false
                );

                switch (TipoOperazione) {
                    case "RitornaDestinazioni":
                        fRitornaDestinazioni(result);
                        break;
                    case "RitornaImmaginiOCR":
                        fRitornaImmaginiOCR(result);
                        break;
                    case "ImpostaImmagineSpostata":
                        fImpostaImmagineSpostata(result);
                        break;
                    case "AggiornaCategorieOCR":
                        fAggiornaCategorieOCR(result);
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
        if (result.contains("ERROR:")) {
            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION") || result.contains("SOCKETTIMEOUTEXCEPTION")) {
            }

            return false;
        } else {
            if (result.contains("anyType{}")) {
                return false;
            } else {
                return true;
            }
        }
    }

    private void fAggiornaCategorieOCR(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Aggiorna Categorie OCR", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, "Immagini su db aggiornate");
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fImpostaImmagineSpostata(String result) {
        boolean ritorno = ControllaRitorno("Ritorna imposta immagine spostata", result);
        if (ritorno) {
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fRitornaDestinazioni(String result) {
        boolean ritorno = ControllaRitorno("Ritorna destinazioni", result);
        if (ritorno) {
            JSONArray jObject = null;

            try {
                List<StrutturaDestinazioni> lista = new ArrayList<>();
                jObject = new JSONArray("[" + result + "]");
                JSONObject obj = jObject.getJSONObject(0);
                String dati = obj.getString("Destinazioni");
                JSONArray jObjectImm = new JSONArray(dati);
                for(int i = 0; i < jObjectImm.length(); i++){
                    JSONObject obj2 = jObjectImm.getJSONObject(i);

                    StrutturaDestinazioni sic = new StrutturaDestinazioni();
                    String Dest = ConverteCaratteriStrani(obj2.getString("Categoria"));
                    if (!Dest.equals(";")) {
                        sic.setDestinazione(RitornaFiltroSplittato(Dest));
                        sic.setQuante(obj2.getInt("Quante"));

                        lista.add(sic);
                    }
                }

                VariabiliStaticheOCR.getInstance().setFiltroPremuto("");
                AdapterListenerImmaginiOCR customAdapterTi = new AdapterListenerImmaginiOCR(
                        context,
                        null);
                VariabiliStaticheOCR.getInstance().getLstImmagini().setAdapter(customAdapterTi);

                AdapterListenerDestinazioni customAdapterT = new AdapterListenerDestinazioni(
                        context,
                        lista);
                VariabiliStaticheOCR.getInstance().getLstDestinazioni().setAdapter(customAdapterT);
            } catch (JSONException e) {
                AdapterListenerDestinazioni customAdapterT = new AdapterListenerDestinazioni(
                        context,
                        null);
                VariabiliStaticheOCR.getInstance().getLstDestinazioni().setAdapter(customAdapterT);
            }
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fRitornaImmaginiOCR(String result) {
        boolean ritorno = ControllaRitorno("Ritorna immagini ocr", result);
        if (ritorno) {
            JSONArray jObject = null;

            try {
                List<StrutturaImmaginiOCR> lista = new ArrayList<>();
                jObject = new JSONArray("[" + result + "]");
                JSONObject obj = jObject.getJSONObject(0);
                String dati = obj.getString("Immagini");
                String Ridotte = obj.getString("Ridotte");
                if (!Ridotte.equals("0")) {
                    UtilitiesGlobali.getInstance().VisualizzaMessaggio(
                            context,
                            "Immagini OCR",
                            "Troppe immagini: Visualizzate " + Ridotte
                    );
                }

                JSONArray jObjectImm = new JSONArray(dati);
                for(int i = 0; i < jObjectImm.length(); i++){
                    JSONObject obj2 = jObjectImm.getJSONObject(i);

                    StrutturaImmaginiOCR sic = new StrutturaImmaginiOCR();
                    sic.setIdImmagine(Integer.parseInt(obj2.getString("idImmagine")));
                    String url = ConverteCaratteriStrani(obj2.getString("URL"));
                    url = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaDiscoPublic + "/Materiale/newPLibrary/" + url;
                    sic.setURL(url);
                    sic.setTesto(ConverteCaratteriStrani(obj2.getString("Testo")));
                    sic.setIdCategoriaOrigine(Integer.parseInt(obj2.getString("idCategoriaOrigine")));
                    sic.setCategoriaOrigine(ConverteCaratteriStrani(obj2.getString("CategoriaOrigine")));
                    sic.setCategorieDestinazione(ConverteCaratteriStrani(obj2.getString("idCategoriaDestinazione")));

                    sic.setLuoghi(ConverteCaratteriStrani(obj2.getString("Luoghi")));
                    sic.setOggetti(ConverteCaratteriStrani(obj2.getString("Oggetti")));
                    sic.setVolti(ConverteCaratteriStrani(obj2.getString("Volti")));
                    sic.setDescrizione(ConverteCaratteriStrani(obj2.getString("Descrizione")));
                    sic.setSitiRilevati(ConverteCaratteriStrani(obj2.getString("SitiRilevati")));

                    lista.add(sic);
                }

                AdapterListenerImmaginiOCR customAdapterT = new AdapterListenerImmaginiOCR(
                        context,
                        lista);
                VariabiliStaticheOCR.getInstance().getLstImmagini().setAdapter(customAdapterT);
            } catch (JSONException e) {
                AdapterListenerImmaginiOCR customAdapterT = new AdapterListenerImmaginiOCR(
                        context,
                        null);
                VariabiliStaticheOCR.getInstance().getLstImmagini().setAdapter(customAdapterT);
            }
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private String ConverteCaratteriStrani(String Stringa) {
        String Ritorno = Stringa;

        if (!Ritorno.isEmpty()) {
            Ritorno = Ritorno.replace("*2V*", "\"");
            Ritorno = Ritorno.replace("*GA*", "{");
            Ritorno = Ritorno.replace("*GC*", "}");
            Ritorno = Ritorno.replace("*QA*", "[");
            Ritorno = Ritorno.replace("*QC*", "]");
            Ritorno = Ritorno.replace("*2P*", ":");
            Ritorno = Ritorno.replace("*V*", ",");
        }

        return Ritorno;
    }
}
