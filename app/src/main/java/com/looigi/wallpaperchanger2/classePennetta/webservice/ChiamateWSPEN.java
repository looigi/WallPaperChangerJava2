package com.looigi.wallpaperchanger2.classePennetta.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classePennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classePennetta.db_dati_pennetta;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ChiamateWSPEN implements TaskDelegate {
    private static final String NomeMaschera = "Chiamate_WS_Immagini_PEN";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmaginiPennetta.UrlWS + "/";
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private GifImageView imgAttesa;

    public ChiamateWSPEN(Context context) {
        this.context = context;
    }

    public void ImpostaImgAttesa(GifImageView imgAttesa) {
        this.imgAttesa = imgAttesa;
    }

    public void RitornaProssimaImmagine(String Categoria) {
        String Urletto="RitornaProssimoPennetta?" +
                "Categoria=" + Categoria +
                "&Filtro=" + VariabiliStaticheMostraImmaginiPennetta.getInstance().getFiltro() +
                "&Random=" + VariabiliStaticheMostraImmaginiPennetta.getInstance().getRandom() +
                "&UltimaImmagine=" + VariabiliStaticheMostraImmaginiPennetta.getInstance().getIdImmagine() +
                "&OrdinaPerVisualizzato=" + (VariabiliStaticheMostraImmaginiPennetta.getInstance().isRicercaPerVisua() ? "S" : "N");

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

    public void SpostaImmagine(StrutturaImmaginiLibrary s) {
        String Urletto="SpostaImmaginePennetta?" +
                "Categoria=" + s.getCategoria() +
                "&idImmagine=" + s.getIdImmagine() +
                "&NuovaCategoria=" + VariabiliStaticheMostraImmaginiPennetta.getInstance().getIdCategoriaSpostamento();

        TipoOperazione = "SpostaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void ModificaImmagine(StrutturaImmaginiLibrary s, String stringaBase64, boolean Sovrascrive) {
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(true);

        String Urletto="ModificaImmaginePennetta?" +
                "Categoria=" + s.getCategoria() +
                "&idImmagine=" + s.getIdImmagine() +
                "&StringaBase64=" + stringaBase64 +
                "&Sovrascrivi=" + (Sovrascrive ? "S" : "N");

        TipoOperazione = "ModificaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
                ApriDialog);
    }

    public void RitornaCategorie(boolean forzaLettura) {
        if (!forzaLettura) {
            db_dati_pennetta db = new db_dati_pennetta(context);
            List<StrutturaImmaginiCategorie> lista = db.LeggeCategorie();
            db.ChiudeDB();
            if (!lista.isEmpty()) {
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setListaCategorie(lista);
                UtilityPennetta.getInstance().AggiornaCategorie(context);
                UtilityPennetta.getInstance().AggiornaCategorieSpostamento(context);

                return;
            }
        }

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

    public void EliminaImmagine(String id) {
        String Urletto="EliminaImmaginePennetta?idImmagine=" + id;

        TipoOperazione = "EliminaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void RefreshImmagini(String Categoria) {
        String Urletto="RefreshImmagini?" +
                "Categoria=" + Categoria +
                "&Completo=";

        TipoOperazione = "RefreshPennetta";
        // ControllaTempoEsecuzione = false;

        UtilitiesGlobali.getInstance().ApreToast(context, "Refresh immagini lanciato");

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
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

                UtilityPennetta.getInstance().Attesa(false);

                switch (TipoOperazione) {
                    case "ProssimaImmagine":
                        fProssimaImmagine(result);
                        break;
                    case "RitornaCategorie":
                        fRitornaCategorie(result);
                        break;
                    case "ModificaImmagine":
                        fModificaImmagine(result);
                        break;
                    case "RefreshImmagini":
                        fRefreshImmagini(result);
                        break;
                    case "EliminaImmagine":
                        fEliminaImmagine(result);
                        break;
                    case "SpostaImmagine":
                        fSpostaImmagine(result);
                        break;
                }

                if (imgAttesa != null) {
                    imgAttesa.setVisibility(LinearLayout.GONE);
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
            return true;
        }
    }

    private void fSpostaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Sposta Immagine", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine spostata");
            UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
        }
    }

    private void fModificaImmagine(String result) {
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Modifica Immagine", result);
        if (ritorno) {
            String Path = context.getFilesDir() + "/Immagini/AppoggioPEN.jpg";
            Bitmap bmp = BitmapFactory.decodeFile(Path);
            VariabiliStaticheMostraImmaginiPennetta.getInstance().getImg().setImageBitmap(bmp);

            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine modificata");
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fEliminaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Elimina Immagine", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine eliminata");
            UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
        }
    }

    private void fRefreshImmagini(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Refresh Immagini", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
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

                db_dati_pennetta db = new db_dati_pennetta(context);
                db.EliminaCategorie();
                for (StrutturaImmaginiCategorie s : listaCategorie) {
                    db.ScriveCategoria(s);
                }
                db.ChiudeDB();

                VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoriAttuale(CategoriaAttuale);
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setListaCategoriePen(l);
                UtilityPennetta.getInstance().AggiornaCategorie(context);
                UtilityPennetta.getInstance().AggiornaCategorieSpostamento(context);
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
            String path = "";
            int id = -1;
            if (result.contains("§")) {
                String[] p = result.split("§");
                path = VariabiliStaticheMostraImmaginiPennetta.PathUrl + p[0];
                id = Integer.parseInt(p[1]);
            } else {
                path = VariabiliStaticheMostraImmaginiPennetta.PathUrl + result;
            }

            VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdImmagine(id);

            StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary();
            s.setIdImmagine(id);
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
