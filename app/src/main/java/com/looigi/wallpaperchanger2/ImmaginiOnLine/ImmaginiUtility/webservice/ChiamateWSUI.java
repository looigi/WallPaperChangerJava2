package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.webservice;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.StrutturaImmagineFuoriCategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.StrutturaImmaginiUguali;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.UtilitiesPreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.adapters.AdapterListenerVoltiRilevati;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.strutture.StrutturaVoltiRilevati;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.UtilityUtilityImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.VariabiliStaticheUtilityImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.adapters.AdapterListenerUI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSUI implements TaskDelegateUI {
    private static final String NomeMaschera = "Chiamate_WS_UI";
    private final String RadiceWS = VariabiliStaticheMostraImmagini.UrlWS + "/";
    private String ws = "newLooVF.asmx/";
    private String NS="http://newLooVF.org/";
    private String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private String TipoFile;
    private String ForzaRefresh;
    private String Categoria;

    public ChiamateWSUI(Context context) {
        this.context = context;
    }

    public void RitornaProssimaImmagine(int idCategoria, String daDove) {
        VariabiliStatichePreview.getInstance().getImgProssima().setVisibility(LinearLayout.GONE);
        VariabiliStatichePreview.getInstance().getImgPrecedente().setVisibility(LinearLayout.GONE);
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStatichePreview.getInstance().getImgCaricamento(),
                true
        );

        String Urletto = "";

        if (daDove.equals("PREVIEW")) {
            Urletto = "ProssimaImmagine?" +
                    "idCategoria=" + (idCategoria > 0 ? idCategoria : "") +
                    "&Filtro=" +
                    "&idImmagine=" + VariabiliStatichePreview.getInstance().getUltimaImmagineVisualizzata() +
                    "&Random=N" +
                    "&OrdinaPerVisualizzato=S" +
                    "&Operatore=Or" +
                    "&Preview=S";
        } else {
            Urletto = "ProssimaImmagine?" +
                    "idCategoria=" + (idCategoria > 0 ? idCategoria : "") +
                    "&Filtro=" +
                    "&idImmagine=0" +
                    "&Random=S" +
                    "&OrdinaPerVisualizzato=S" +
                    "&Operatore=Or" +
                    "&Preview=N";
        }

        TipoOperazione = "ProssimaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void RitornaCategorieDiRicerca() {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        this.ForzaRefresh = ForzaRefresh;

        String Urletto="RitornaCategorieDiRicerca";

        TipoOperazione = "RitornaCategorieDiRicerca";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                6000000,
                ApriDialog);
    }

    public void SistemaImmaginiSenzaHash() {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        this.ForzaRefresh = ForzaRefresh;

        String Urletto="SistemaImmaginiSenzaHash";

        TipoOperazione = "SistemaImmaginiSenzaHash";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                6000000,
                ApriDialog);
    }

    public void ControllaVolto(String idImmagine) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStatichePreview.getInstance().getImgCaricamento(),
                true
        );

        String Urletto="ControllaVolto?idImmagine=" + idImmagine;

        TipoOperazione = "ControllaVolto";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                6000000,
                ApriDialog);
    }

    public void EliminaCategoria(String idCategoria) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        String Urletto="EliminaCategoria?idCategoria=" + idCategoria;

        TipoOperazione = "EliminaCategoria";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                6000000,
                ApriDialog);
    }

    public void RitornaImmaginiFuoriCategoria(String idCategoria, String Alias1, String Alias2, String ForzaRefresh) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        this.ForzaRefresh = ForzaRefresh;

        String Urletto="TrovaNomiSuDbFuoriDallaCategoria?" +
                "idCategoria=" + idCategoria +
                "&Aliases1=" + Alias1 +
                "&Aliases2=" + Alias2 +
                "&QuantiCaratteri=4" +
                "&AndOr=And" +
                "&SoloSuAltro=S" +
                "&CercaExif=N" +
                "&Tag=" +
                "&ForzaRefresh=" + ForzaRefresh;

        TipoOperazione = "TrovaNomiSuDbFuoriDallaCategoria";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void ControlloImmagini(String id, String ForzaRefresh) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        this.ForzaRefresh = ForzaRefresh;
        if (ForzaRefresh == null) { ForzaRefresh = "S"; }
        ForzaRefresh = "S"; // Mi sono accorto che non serve a nulla prendere la cache

        String Urletto="ControlloImmaginiCategoria?idCategoria=" + id + "&ForzaRefresh=" + ForzaRefresh;

        TipoOperazione = "ControlloImmagini";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000000,
                ApriDialog);
    }

    public void PulisceCache() {
        String Urletto="PulisceCacheControllo";

        TipoOperazione = "PulisceCacheControllo";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                165000,
                ApriDialog);
    }

    private String idCategoria;
    private boolean DaTasto;

    public void RefreshImmagini(String idCategoria, boolean DaTasto) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        this.idCategoria = idCategoria;
        this.DaTasto = DaTasto;

        String Urletto="RefreshImmagini?" +
                "idCategoria=" + idCategoria +
                "&Completo=S";

        TipoOperazione = "RefreshImmagini";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                500000,
                ApriDialog);
    }

    public void RefreshImmaginiAltre() {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        String Urletto="RefreshImmagini?" +
                "idCategoria=41" +
                "&Completo=N";

        TipoOperazione = "RefreshImmaginiAltre";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                500000,
                ApriDialog);
    }

    public void RitornaImmaginiUguali(String Categoria, String ForzaRefresh) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        this.ForzaRefresh = ForzaRefresh;

        String Urletto="ImmaginiUgualiMobile?" +
                "Categoria=" + Categoria +
                "&ForzaRefresh=S"; // Messo a S per dare sempre risultati aggiornati

        TipoOperazione = "ImmaginiUgualiMobile";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void RinominaImmagine(String idImmagine, String NuovoNome) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        String Urletto="RinominaImmagine?" +
                "idImmagine=" + idImmagine +
                "&NuovoNome=" + NuovoNome;

        TipoOperazione = "RinominaImmagine";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                60000,
                ApriDialog);
    }

    public void ConverteImmagine(String idImmagine) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                true
        );

        String Urletto="ConverteImmagine?" +
                "idImmagine=" + idImmagine;

        TipoOperazione = "ConverteImmagine";

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
        // UtilityLazio.getInstance().ImpostaAttesa(true);

        long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = Long.toString(tsLong);

        InterrogazioneWSUI i = new InterrogazioneWSUI();
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
                        VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                        false
                );

                switch (TipoOperazione) {
                    case "TrovaNomiSuDbFuoriDallaCategoria":
                        fTrovaNomiSuDbFuoriDallaCategoria(result);
                        break;
                    case "ImmaginiUgualiMobile":
                        fImmaginiUgualiMobile(result);
                        break;
                    case "ControlloImmagini":
                        fControlloImmagini(result);
                        break;
                    case "ProssimaImmagine":
                        fProssimaImmagine(result);
                        break;
                    case "RefreshImmagini":
                        fRefreshImmagini(result);
                        break;
                    case "PulisceCacheControllo":
                        fPulisceCacheControllo(result);
                        break;
                    case "RefreshImmaginiAltre":
                        fRefreshImmaginiAltre(result);
                        break;
                    case "SistemaImmaginiSenzaHash":
                        fSistemaImmaginiSenzaHash(result);
                        break;
                    case "RitornaCategorieDiRicerca":
                        fRitornaCategorieDiRicerca(result);
                        break;
                    case "RinominaImmagine":
                        fRinominaImmagine(result);
                        break;
                    case "ConverteImmagine":
                        fConverteImmagine(result);
                        break;
                    case "EliminaCategoria":
                        fEliminaCategoria(result);
                        break;
                    case "ControllaVolto":
                        fControllaVolto(result);
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

    private void fControllaVolto(String result) {
        boolean ritorno = ControllaRitorno("Controlla Volto", result);
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStatichePreview.getInstance().getImgCaricamento(),
                false
        );
        if (ritorno) {
            // {"Rilevate":[{"idCategoria": 566,"Categoria": "Gina_Pistol","Confidenza": 0.0210}]}
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jObject = jsonObject.getJSONArray("Rilevate");

                List<StrutturaVoltiRilevati> lista = new ArrayList<>();
                for(int i = 0; i < jObject.length(); i++) {
                    JSONObject obj = jObject.getJSONObject(i);

                    StrutturaVoltiRilevati s = new StrutturaVoltiRilevati();
                    s.setIdImmagine(obj.getInt("idImmagine"));
                    s.setIdCategoria(obj.getInt("idCategoria"));
                    s.setCategoria(obj.getString("Categoria"));
                    s.setConfidenza(obj.getString("Confidenza"));
                    s.setIdCategoriaOrigine(obj.getInt("idCategoriaOrigine"));
                    s.setCategoriaOrigine(obj.getString("CategoriaOrigine"));
                    String url = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaDiscoPublic +
                            "/Materiale/newPLibrary/";
                    s.setUrlOrigine(url + obj.getString("UrlOrigine"));
                    s.setUrlDestinazione(url + obj.getString("UrlDestinazione"));
                    s.setNomeOrigine(obj.getString("NomeOrigine"));
                    s.setNomeDestinazione(obj.getString("NomeDestinazione"));

                    lista.add(s);
                }
                VariabiliStatichePreview.getInstance().setListaVoltiRilevati(lista);

                AdapterListenerVoltiRilevati customAdapterT = new AdapterListenerVoltiRilevati(
                        context,
                        lista);
                VariabiliStatichePreview.getInstance().getLstVolti().setAdapter(customAdapterT);

                VariabiliStatichePreview.getInstance().getLayVolti().setVisibility(LinearLayout.VISIBLE);
            } catch (JSONException e) {
                UtilitiesGlobali.getInstance().ApreToast(context, result);
            }
        } else {

        }
    }

    private void fProssimaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossima immagine", result);

        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                false
        );

        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStatichePreview.getInstance().getImgCaricamento(),
                false
        );
        VariabiliStatichePreview.getInstance().getImgProssima().setVisibility(LinearLayout.VISIBLE);
        VariabiliStatichePreview.getInstance().getImgPrecedente().setVisibility(LinearLayout.VISIBLE);

        if (ritorno) {
            // VariabiliStaticheUtilityImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.VISIBLE);

            try {
                JSONObject jObject = new JSONObject(result);
                StrutturaImmaginiLibrary si = UtilityImmagini.getInstance().prendeStruttura(jObject);

                // VariabiliStaticheUtilityImmagini.getInstance().setIdImmagineInPreview(si.getIdImmagine());
                if (si != null) {
                    VariabiliStatichePreview.getInstance().AggiungeImmagineAVisualizzate(si);

                    VariabiliStatichePreview.getInstance().setStrutturaImmagine(si);
                    if (VariabiliStatichePreview.getInstance().getTxtDescrizione() != null) {
                        String Descrizione = "idImmagine: " + si.getIdImmagine() + " - Immagine: " + si.getNomeFile() + " - Categoria: " + si.getCategoria() +
                                " - Cartella: " + si.getCartella() + " - Dimensioni: " + si.getDimensioniImmagine() +
                                " - Bytes: " + si.getDimensioneFile();
                        VariabiliStatichePreview.getInstance().getTxtDescrizione().setText(Descrizione);
                    }

                    SharedPreferences prefs = context.getSharedPreferences("PREVIEW", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt(
                            "idImmagineCategoria_" + VariabiliStatichePreview.getInstance().getIdCategoria(),
                            si.getIdImmagine());
                    editor.apply();
                    VariabiliStatichePreview.getInstance().setUltimaImmagineVisualizzata(si.getIdImmagine());

                    /* DownloadImmagineMI d = new DownloadImmagineMI();
                    d.EsegueChiamata(
                            context, si.getUrlImmagine(),
                            VariabiliStaticheUtilityImmagini.getInstance().getImgPreview(),
                            si.getUrlImmagine(),
                            false,
                            false
                    ); */
                    UtilitiesPreview.getInstance().RitornoProssimaImmagine(context, si);
                }
            } catch (JSONException e) {
                UtilitiesGlobali.getInstance().ApreToast(context, result);
            }
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fRitornaCategorieDiRicerca(String result) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                false
        );

        boolean ritorno = ControllaRitorno("RitornaCategorieDiRicerca", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            String[] id = result.split(";");
            List<Integer> lista = new ArrayList<>();
            for (String i: id) {
                lista.add(Integer.valueOf(i));
            }
            VariabiliStaticheUtilityImmagini.getInstance().setListaCategorieDiRicerca(lista);

            VariabiliStaticheUtilityImmagini.getInstance().setAdapter(new AdapterListenerUI(
                    context,
                    VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM())
            );
            VariabiliStaticheUtilityImmagini.getInstance().getLstImmagini().setAdapter(
                    VariabiliStaticheUtilityImmagini.getInstance().getAdapter()
            );
        }
    }

    private void fControlloImmagini(String result) {
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheUtilityImmagini.getInstance().getImgCaricamento(),
                false
        );

        boolean ritorno = ControllaRitorno("Controllo Immagini", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {

            // { "Categoria": "3D", "idCategoria": 2, "Giuste": 4, "Errate": 0,  "listaErrate": [] ,"Piccole": 0,
            // "listaPiccole": [],"Inesistenti": 0, "listaInesistenti": [] }
            try {
                JSONObject root = new JSONObject(result);
                String Categoria = root.getString("Categoria");
                int idCategoria = root.getInt("idCategoria");
                int Giuste = root.getInt("Giuste");
                int Errate = root.getInt("Errate");
                int Inesistenti = root.getInt("Inesistenti");
                int Invalide = root.getInt("Invalide");
                JSONArray listaErrate = root.getJSONArray("listaErrate");
                List<String> listaErrateS = new ArrayList<>();
                for(int i = 0; i < listaErrate.length(); i++) {
                    JSONObject obj = listaErrate.getJSONObject(i);
                    String Immagine = obj.getString("ImmagineErrata");
                    listaErrateS.add(Immagine);
                }
                int Grande = root.getInt("Grandi");
                JSONArray listaGrandi = root.getJSONArray("listaGrandi");
                List<String> listaGrandiS = new ArrayList<>();
                for(int i = 0; i < listaGrandi.length(); i++) {
                    JSONObject obj = listaGrandi.getJSONObject(i);
                    String Immagine = obj.getString("ImmagineGrande");
                    listaGrandiS.add(Immagine);
                }
                int Piccole = root.getInt("Piccole");
                JSONArray listaPiccole = root.getJSONArray("listaPiccole");
                List<String> listaPiccoleS = new ArrayList<>();
                for(int i = 0; i < listaPiccole.length(); i++) {
                    JSONObject obj = listaPiccole.getJSONObject(i);
                    String Immagine = obj.getString("ImmaginePiccola");
                    listaPiccoleS.add(Immagine);
                }
                JSONArray listaInesistenti = root.getJSONArray("listaInesistenti");
                List<String> listaInesistentiS = new ArrayList<>();
                for(int i = 0; i < listaInesistenti.length(); i++) {
                    JSONObject obj = listaInesistenti.getJSONObject(i);
                    String Immagine = obj.getString("ImmagineInesistente");
                    listaInesistentiS.add(Immagine);
                }
                JSONArray listaInvalide = root.getJSONArray("listaInvalide");
                List<String> listaInvalideS = new ArrayList<>();
                for(int i = 0; i < listaInvalide.length(); i++) {
                    JSONObject obj = listaInvalide.getJSONObject(i);
                    String Immagine = obj.getString("ImmagineInvalida");
                    listaInvalideS.add(Immagine);
                }

                StrutturaControlloImmagini s = new StrutturaControlloImmagini();
                s.setCategoria(Categoria);
                s.setIdCategoria(idCategoria);
                s.setErrate(Errate);
                s.setGiuste(Giuste);
                s.setPiccole(Piccole);
                s.setGrandi(Grande);
                s.setInesistenti(Inesistenti);
                s.setListaErrate(listaErrateS);
                s.setListaPiccole(listaPiccoleS);
                s.setListaGrandi(listaGrandiS);
                s.setListaInesistenti(listaInesistentiS);
                s.setInvalide(Invalide);
                s.setListaInvalide(listaInvalideS);
                VariabiliStaticheUtilityImmagini.getInstance().setStrutturaAttuale(s);

                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        ChiamateWSUI ws = new ChiamateWSUI(context);
                        ws.RitornaImmaginiUguali(Categoria, ForzaRefresh);
                    }
                };
                handlerTimer.postDelayed(rTimer, 500);
            } catch (JSONException e) {
                /* Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        ChiamateWSUI ws = new ChiamateWSUI(context);
                        ws.RitornaImmaginiUguali(Categoria);
                    }
                };
                handlerTimer.postDelayed(rTimer, 500); */
                VariabiliStaticheUtilityImmagini.getInstance().setBloccaElaborazione(true);
                VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");
                VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(false);
                UtilitiesGlobali.getInstance().ApreToast(context, "Errore nel parse json del controllo immagini");
            }
        }
    }

    private void fConverteImmagine(String result) {
        boolean ritorno = ControllaRitorno("Converte Immagine", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    // ChiamateWSUI ws = new ChiamateWSUI(context);
                    // ws.RitornaImmaginiUguali(Categoria, ForzaRefresh);
                }
            };
            handlerTimer.postDelayed(rTimer, 500);

            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine convertita");
        }
    }

    private void fEliminaCategoria(String result) {
        boolean ritorno = ControllaRitorno("Elimina Categoria", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Categoria eliminata");
        }
    }

    private void fRinominaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Rinomina Immagine", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    // ChiamateWSUI ws = new ChiamateWSUI(context);
                    // ws.RitornaImmaginiUguali(Categoria, ForzaRefresh);
                }
            };
            handlerTimer.postDelayed(rTimer, 500);

            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine rinominata");
        }
    }

    private void fSistemaImmaginiSenzaHash(String result) {
        boolean ritorno = ControllaRitorno("Sistema immagini senza hash", result);
        if (!ritorno) {
            // Utility.getInstance().VisualizzaMessaggio(result);
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fPulisceCacheControllo(String result) {
        boolean ritorno = ControllaRitorno("Pulizia cache", result);
        if (!ritorno) {
            // Utility.getInstance().VisualizzaMessaggio(result);
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Cache eliminata");
        }
    }

    private void fRefreshImmaginiAltre(String result) {
        boolean ritorno = ControllaRitorno("Refresh immagini altre", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Refresh altre effettuato");
        }
    }

    private void fRefreshImmagini(String result) {
        boolean ritorno = ControllaRitorno("Refresh immagini", result);
        if (!ritorno) {
        }

        if (DaTasto) {
            String r = result.replace("{", "").replace("}", "");
            String[] rr = r.split(",");
            StringBuilder Messaggio = new StringBuilder();
            for (String rrr : rr) {
                Messaggio.append(rrr.replace("\"", "").trim()).append("\n");
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Utility Immagini");
            builder.setMessage(Messaggio);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            builder.show();

            // UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                ChiamateWSUI ws = new ChiamateWSUI(context);
                ws.ControlloImmagini(
                        String.valueOf(idCategoria),
                        ForzaRefresh
                );
            }
        };
        handlerTimer.postDelayed(rTimer, 500);
    }

    private void fImmaginiUgualiMobile(String result) {
        boolean ritorno = ControllaRitorno("Ritorna immagini uguali", result);
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);

            VariabiliStaticheUtilityImmagini.getInstance().getStrutturaAttuale().setListaUguali(new ArrayList<>());

            ProseguElaborazione();
        } else {
            List<StrutturaImmaginiUguali> lista = new ArrayList<>();
            String[] r = result.split("§");
            for (String rr : r) {
                if (!rr.isEmpty()) {
                    String[] rrr = rr.split(";");
                    StrutturaImmaginiUguali s = new StrutturaImmaginiUguali();
                    s.setTipo(rrr[0]);
                    if (rrr[1].contains("x")) {
                        s.setQuanti(1);
                    } else {
                        s.setQuanti(Integer.parseInt(rrr[1]));
                    }
                    s.setFiltro(rrr[2]);
                    lista.add(s);
                }
            }
            VariabiliStaticheUtilityImmagini.getInstance().getStrutturaAttuale().setListaUguali(lista);

            ProseguElaborazione();
        }
    }

    private void ProseguElaborazione() {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                String Categoria = VariabiliStaticheUtilityImmagini.getInstance().getCategoriaAttuale();
                String Alias1 = "";
                String Alias2 = "";
                if (Categoria.contains("_")) {
                    String[] c = Categoria.split("_");
                    Alias1 = c[0];
                    Alias2 = c[c.length - 1];
                } else {
                    int lungh = Categoria.length();
                    Alias1 = Categoria.substring(0, lungh / 2);
                    Alias2 = Categoria.substring((lungh / 2), lungh);
                }

                int idCategoria2 = -1;
                for (StrutturaImmaginiCategorie s : VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM()) {
                    if (s.getCategoria().equals(Categoria)) {
                        idCategoria2 = s.getIdCategoria();
                        break;
                    }
                }

                ChiamateWSUI ws = new ChiamateWSUI(context);
                ws.RitornaImmaginiFuoriCategoria(
                        Integer.toString(idCategoria2),
                        Alias1,
                        Alias2,
                        ForzaRefresh
                );
            }
        };
        handlerTimer.postDelayed(rTimer, 500);
    }

    private void fTrovaNomiSuDbFuoriDallaCategoria(String result) {
        boolean ritorno = ControllaRitorno("TrovaNomiSuDbFuoriDallaCategoria", result);
        if (result.contains("ERROR")) {
            int a = 0;
        }
        if (!ritorno) {
            // UtilitiesGlobali.getInstance().ApreToast(context, result);
            VariabiliStaticheUtilityImmagini.getInstance().getStrutturaAttuale().setListaFC(new ArrayList<>());

            UtilityUtilityImmagini.getInstance().Prosegue(context);
        } else {
            List<StrutturaImmagineFuoriCategoria> lista = new ArrayList<>();

            JSONArray jObject = null;
            try {
                jObject = new JSONArray("[" + result + "]");
                JSONObject obj = jObject.getJSONObject(0);
                String dati = obj.getString("Immagini");

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

                    lista.add(sic);
                }
                VariabiliStaticheUtilityImmagini.getInstance().getStrutturaAttuale().setListaFC(lista);

                UtilityUtilityImmagini.getInstance().Prosegue(context);
            } catch (JSONException e) {
                VariabiliStaticheUtilityImmagini.getInstance().getStrutturaAttuale().setListaFC(new ArrayList<>());

                UtilityUtilityImmagini.getInstance().Prosegue(context);
            }
        }
    }
}
