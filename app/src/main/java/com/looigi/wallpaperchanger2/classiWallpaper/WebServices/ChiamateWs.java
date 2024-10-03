package com.looigi.wallpaperchanger2.classiWallpaper.WebServices;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.looigi.wallpaperchanger2.classiDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeMostraImmagini.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeMostraImmagini.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeMostraImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeMostraImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeMostraImmagini.webservice.DownloadImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ChiamateWs implements TaskDelegate {
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = "http://looigi.no-ip.biz:1071/";
    private String TipoOperazione = "";
    private Context context;
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";

    public ChiamateWs(Context context) {
        this.context = context;
    }

    public void RitornaProssimaImmagine(int idCategoria, String Filtro,
                                        int idImmagine, String Random) {
        if (idCategoria == -999) {
            return;
        }

        String Urletto="ProssimaImmagine?" +
                "idCategoria=" + idCategoria +
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
                10000,
                true,
                true,
                false,
                -1);
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
                10000,
                true,
                true,
                false,
                -1);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog, boolean ChiamataDiretta,
                       boolean ControllaTempoEsecuzione, int Stelle) {

        UtilityWallpaper.getInstance().Attesa(true);

        bckAsyncTask = new LetturaWSAsincrona(
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                "0", // TimeStampAttuale,
                this);
        bckAsyncTask.execute(Urletto);
    }

    @Override
    public void TaskCompletionResult(String result) {
        UtilityWallpaper.getInstance().Attesa(false);

        switch (TipoOperazione) {
            case "ProssimaImmagine":
                fProssimaImmagine(result);
                break;
            case "RitornaCategorie":
                fRitornaCategorie(result);
                break;
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

    private void fRitornaCategorie(String result) {
        boolean ritorno = ControllaRitorno("Ritorna categorie", result);
        if (ritorno) {
            try {
                List<StrutturaImmaginiCategorie> listaCategorie = new ArrayList<>();

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

                int idCategoriaImpostata = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata().getIdCategoria();
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
            UtilityWallpaper.getInstance().ApreToast(context, result);
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

                    String path1 = context.getFilesDir() + "/Immagini";
                    UtilityWallpaper.getInstance().CreaCartelle(path1);
                    String NomeFile = "/UltimaImmagine.txt";
                    if (UtilityWallpaper.getInstance().EsisteFile(path1 + NomeFile)) {
                        UtilityDetector.getInstance().EliminaFile(path1 + "/" + NomeFile);
                    }
                    UtilityDetector.getInstance().CreaFileDiTesto(path1, NomeFile, result);

                    VariabiliStaticheMostraImmagini.getInstance().setUltimaImmagineCaricata(si);

                    VariabiliStaticheMostraImmagini.getInstance().ScriveInfoImmagine(si);

                    VariabiliStaticheMostraImmagini.getInstance().AggiungeCaricata();

                    new DownloadImage(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmagini.getInstance().getImg()).execute(si.getUrlImmagine());
                }
            } catch (JSONException e) {

            }
            // Utility.getInstance().VisualizzaMessaggio(result);
        } else {
            UtilityWallpaper.getInstance().ApreToast(context, result);
        }
    }

    public void StoppaEsecuzione() {
        bckAsyncTask.cancel(true);
    }
}
