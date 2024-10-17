package com.looigi.wallpaperchanger2.classePennetta.webservice;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.looigi.wallpaperchanger2.classePennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSPEN implements TaskDelegate {
    private static final String NomeMaschera = "Chiamate_WS_Immagini_PEN";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmaginiPennetta.UrlWS + "/";
    private String ws = "newLooVF.asmx/";
    private String NS="http://newLooVF.org/";
    private String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private Context context;
    private boolean ApriDialog = false;

    public ChiamateWSPEN(Context context) {
        this.context = context;
    }

    public void RitornaProssimaImmagine(String Categoria, String Filtro) {
        String Urletto="RitornaProssimoPennetta?" +
                "Categoria=" + Categoria +
                "&Filtro=" + Filtro;

        TipoOperazione = "ProssimaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void RitornaCategorie() {
        String Urletto="RitornaCategoriePennetta";

        TipoOperazione = "RitornaCategorie";
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

        UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera, "Chiamata WS " + TipoOperazione + ". OK");
        UtilityPennetta.getInstance().Attesa(true);

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSPEN i = new InterrogazioneWSPEN();
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
        /* bckAsyncTask = new LetturaWSAsincrona(
                context,
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                TimeStampAttuale,
                this);
        bckAsyncTask.execute(Urletto); */
    }

    @Override
    public void TaskCompletionResult(String result) {
        UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

        UtilityPennetta.getInstance().Attesa(false);

        switch (TipoOperazione) {
            case "ProssimaImmagine":
                fProssimaImmagine(result);
                break;
            case "RitornaCategorie":
                fRitornaCategorie(result);
                break;
        }
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

    private void fRitornaCategorie(String result) {
        boolean ritorno = ControllaRitorno("Ritorna categorie", result);
        if (ritorno) {
            // try {
                List<StrutturaImmaginiCategorie> listaCategorie = new ArrayList<>();

                StrutturaImmaginiCategorie sicT = new StrutturaImmaginiCategorie();
                sicT.setIdCategoria(-1);
                sicT.setCategoria("Tutte");
                sicT.setAlias("");
                sicT.setTag("");

                listaCategorie.add(sicT);

                /* JSONArray jObject = new JSONArray(result);

                for(int i = 0; i < jObject.length(); i++){
                    JSONObject obj = jObject.getJSONObject(i);

                    StrutturaImmaginiCategorie sic = new StrutturaImmaginiCategorie();
                    sic.setIdCategoria(obj.getInt("idCategoria"));
                    sic.setCategoria(obj.getString("Categoria"));
                    sic.setAlias(obj.getString("Alias"));
                    sic.setTag(obj.getString("Tag"));

                    listaCategorie.add(sic);
                } */

                String[] categorie = result.split("§");
                for (String c : categorie) {
                    StrutturaImmaginiCategorie sicT2 = new StrutturaImmaginiCategorie();
                    sicT2.setIdCategoria(-1);
                    sicT2.setCategoria(c);
                    sicT2.setAlias("");
                    sicT2.setTag("");

                    listaCategorie.add(sicT2);
                }

                VariabiliStaticheMostraImmaginiPennetta.getInstance().setListaCategorie(listaCategorie);

                /* int idCategoriaImpostata = -1;
                if (VariabiliStaticheMostraImmaginiPennetta.getInstance().getUltimaImmagineCaricata() != null) {
                    idCategoriaImpostata = VariabiliStaticheMostraImmaginiPennetta.getInstance().getUltimaImmagineCaricata().getIdCategoria();
                } */
                String idCategoriaImpostata = VariabiliStaticheMostraImmaginiPennetta.getInstance().getCategoria();
                String CategoriaAttuale = "";

                String[] l = new String[listaCategorie.size()];
                int c = 0;
                for (StrutturaImmaginiCategorie s : listaCategorie) {
                    l[c] = s.getCategoria();
                    if (s.getCategoria().equals(idCategoriaImpostata)) {
                        CategoriaAttuale = s.getCategoria();
                    }
                    c++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item, l);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                VariabiliStaticheMostraImmaginiPennetta.getInstance().getSpnCategorie().setAdapter(adapter);

                if (!CategoriaAttuale.isEmpty()) {
                    int spinnerPosition = adapter.getPosition(CategoriaAttuale);
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().getSpnCategorie().setSelection(spinnerPosition);
                }
            // } catch (JSONException e) {
            //     throw new RuntimeException(e);
            // }
        // } else {
        //     UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fProssimaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossima immagine", result);
        if (ritorno) {
            String path =  VariabiliStaticheMostraImmaginiPennetta.PathUrl + result;

            StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary();
            s.setUrlImmagine(path);
            s.setCategoria(VariabiliStaticheMostraImmaginiPennetta.getInstance().getCategoria());
            s.setNomeFile(result);
            s.setDataCreazione("");

            UtilityPennetta.getInstance().AggiungeImmagine(context, result, s);

            VariabiliStaticheMostraImmaginiPennetta.getInstance().setUltimaImmagineCaricata(s);

            DownloadImmaginePEN d = new DownloadImmaginePEN();
            d.EsegueChiamata(
                    context,
                    path,
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().getImg(),
                    path
            );
            // new DownloadImagePEN(context, path,
            //         VariabiliStaticheMostraImmaginiPennetta.getInstance().getImg()).execute(path);

            /* try {
                JSONObject jObject = new JSONObject(result);
                StrutturaImmaginiLibrary si = UtilityPennetta.getInstance().prendeStruttura(jObject);
                if (si != null) {
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdCategoria(si.getIdCategoria());
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdImmagine(si.getIdImmagine());

                    UtilityPennetta.getInstance().AggiungeImmagine(context, result, si);

                    new DownloadImagePEN(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmaginiPennetta.getInstance().getImg()).execute(si.getUrlImmagine());
                }
            } catch (JSONException e) {

            } */
            // Utility.getInstance().VisualizzaMessaggio(result);
        // } else {
        //     UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
