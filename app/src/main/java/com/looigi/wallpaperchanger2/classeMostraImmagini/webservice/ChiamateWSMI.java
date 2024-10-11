package com.looigi.wallpaperchanger2.classeMostraImmagini.webservice;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.looigi.wallpaperchanger2.classeMostraImmagini.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeMostraImmagini.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeMostraImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeMostraImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSMI implements TaskDelegate {
    private static final String NomeMaschera = "CHIAMATEWSIMMAGINI";
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmagini.UrlWS + "/";
    private String ws = "newLooVF.asmx/";
    private String NS="http://newLooVF.org/";
    private String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private Context context;
    private boolean ApriDialog = false;

    public ChiamateWSMI(Context context) {
        this.context = context;
    }

    public void RitornaProssimaImmagine(int idCategoria, String Filtro,
                                        int idImmagine, String Random) {
        if (idCategoria == -999) {
            return;
        }

        String Urletto="ProssimaImmagine?" +
                "idCategoria=" + (idCategoria != -1 ? idCategoria : "") +
                "&Filtro=" + Filtro +
                "&idImmagine=" + idImmagine +
                "&Random=" + Random;

        TipoOperazione = "ProssimaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                35000,
                ApriDialog);
    }

    public void RitornaCategorie() {
        String Urletto="RitornaCategorie";

        TipoOperazione = "RitornaCategorie";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                35000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

        UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Chiamata WS " + TipoOperazione + ". OK");
        UtilityImmagini.getInstance().Attesa(true);

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        bckAsyncTask = new LetturaWSAsincrona(
                context,
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                TimeStampAttuale,
                this);
        bckAsyncTask.execute(Urletto);
    }

    @Override
    public void TaskCompletionResult(String result) {
        UtilityImmagini.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

        UtilityImmagini.getInstance().Attesa(false);

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
        bckAsyncTask.cancel(true);
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
            try {
                List<StrutturaImmaginiCategorie> listaCategorie = new ArrayList<>();
                StrutturaImmaginiCategorie sicT = new StrutturaImmaginiCategorie();
                sicT.setIdCategoria(-1);
                sicT.setCategoria("Tutte");
                sicT.setAlias("");
                sicT.setTag("");

                listaCategorie.add(sicT);

                JSONArray jObject = new JSONArray(result);

                for(int i = 0; i < jObject.length(); i++){
                    JSONObject obj = jObject.getJSONObject(i);

                    StrutturaImmaginiCategorie sic = new StrutturaImmaginiCategorie();
                    sic.setIdCategoria(obj.getInt("idCategoria"));
                    sic.setCategoria(obj.getString("Categoria"));
                    sic.setAlias(obj.getString("Alias"));
                    sic.setTag(obj.getString("Tag"));

                    listaCategorie.add(sic);
                }

                VariabiliStaticheMostraImmagini.getInstance().setListaCategorie(listaCategorie);

                int idCategoriaImpostata = -1;
                if (VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata() != null) {
                    idCategoriaImpostata = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata().getIdCategoria();
                }
                String CategoriaAttuale = "";

                String[] l = new String[listaCategorie.size()];
                int c = 0;
                for (StrutturaImmaginiCategorie s : listaCategorie) {
                    l[c] = s.getCategoria();
                    if (s.getIdCategoria() == idCategoriaImpostata) {
                        CategoriaAttuale = s.getCategoria();
                    }
                    c++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (context, android.R.layout.simple_spinner_item, l);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                VariabiliStaticheMostraImmagini.getInstance().getSpnCategorie().setAdapter(adapter);

                if (!CategoriaAttuale.isEmpty()) {
                    int spinnerPosition = adapter.getPosition(CategoriaAttuale);
                    VariabiliStaticheMostraImmagini.getInstance().getSpnCategorie().setSelection(spinnerPosition);
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fProssimaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossima immagine", result);
        if (ritorno) {
            try {
                JSONObject jObject = new JSONObject(result);
                StrutturaImmaginiLibrary si = UtilityImmagini.getInstance().prendeStruttura(jObject);
                if (si != null) {
                    VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(si.getIdCategoria());
                    VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(si.getIdImmagine());

                    UtilityImmagini.getInstance().AggiungeImmagine(context, result, si);

                    new DownloadImageMI(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmagini.getInstance().getImg()).execute(si.getUrlImmagine());
                }
            } catch (JSONException e) {

            }
            // Utility.getInstance().VisualizzaMessaggio(result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
