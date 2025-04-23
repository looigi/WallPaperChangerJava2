package com.looigi.wallpaperchanger2.classeFetekkie.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeFetekkie.db_dati_fetekkie;
import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiCategorieFE;
import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiLibraryFE;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classeFetekkie.UtilityFetekkie;
import com.looigi.wallpaperchanger2.classeFetekkie.VariabiliStaticheMostraImmaginiFetekkie;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ChiamateWSFET implements TaskDelegate {
    private static final String NomeMaschera = "Chiamate_WS_Immagini_FET";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmaginiFetekkie.UrlWS;
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private GifImageView imgAttesa;

    public ChiamateWSFET(Context context) {
        this.context = context;
    }

    public void ImpostaImgAttesa(GifImageView imgAttesa) {
        this.imgAttesa = imgAttesa;
    }

    public void AggiungeImmagine(String Categoria, String NomeFile, String Base64) {
        String Urletto="AggiungeImmagineFetekkie?" +
                "Categoria=" + Categoria +
                "&NomeFile=" + NomeFile +
                "&Base64=" + Base64;

        TipoOperazione = "AggiungeImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void RitornaProssimaImmagine(String Categoria) {
        String Urletto="RitornaProssimoFetekkie?" +
                "Categoria=" + Categoria +
                "&Filtro=" + VariabiliStaticheMostraImmaginiFetekkie.getInstance().getFiltro() +
                "&Random=" + VariabiliStaticheMostraImmaginiFetekkie.getInstance().getRandom() +
                "&UltimaImmagine=" + VariabiliStaticheMostraImmaginiFetekkie.getInstance().getIdImmagine() +
                "&OrdinaPerVisualizzato=" + (VariabiliStaticheMostraImmaginiFetekkie.getInstance().isRicercaPerVisua() ? "S" : "N");

        TipoOperazione = "ProssimaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void SpostaImmagine(StrutturaImmaginiLibraryFE s) {
        String Urletto="SpostaImmagineFetekkie?" +
                "Categoria=" + s.getCategoria() +
                "&idImmagine=" + s.getIdImmagine() +
                "&NuovaCategoria=" + VariabiliStaticheMostraImmaginiFetekkie.getInstance().getIdCategoriaSpostamento();

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

    public void ModificaImmagine(StrutturaImmaginiLibraryFE s, String stringaBase64, boolean Sovrascrive) {
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(true);

        String Urletto="ModificaImmagineFetekkie?" +
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
            db_dati_fetekkie db = new db_dati_fetekkie(context);
            List<StrutturaImmaginiCategorieFE> lista = db.LeggeCategorie();
            db.ChiudeDB();
            if (!lista.isEmpty()) {
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setListaCategorie(lista);
                UtilityFetekkie.getInstance().AggiornaCategorie(context);
                UtilityFetekkie.getInstance().AggiornaCategorieSpostamento(context);

                return;
            }
        }

        String Urletto="RitornaCategorieFetekkie";

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
        String Urletto="EliminaImmagineFetekkie?idImmagine=" + id;

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
        String Urletto="RefreshFetekkie?" +
                "Categoria=" + Categoria +
                "&Completo=" + (VariabiliStaticheMostraImmaginiFetekkie.getInstance().isAggiornamentoCompleto() ? "S" : "");

        TipoOperazione = "RefreshFetekkie";
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

        UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera, "Chiamata WS " + TipoOperazione + ". OK");
        UtilityFetekkie.getInstance().Attesa(true);

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSFET i = new InterrogazioneWSFET();
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
                UtilityFetekkie.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

                UtilityFetekkie.getInstance().Attesa(false);

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
                    case "RefreshFetekkie":
                        fRefreshImmagini(result);
                        break;
                    case "EliminaImmagine":
                        fEliminaImmagine(result);
                        break;
                    case "SpostaImmagine":
                        fSpostaImmagine(result);
                        break;
                    case "AggiungeImmagine":
                        fAggiungeImmagine(result);
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

    private void fAggiungeImmagine(String result) {
        boolean ritorno = ControllaRitorno("Aggiunge Immagine", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine aggiunta");
        }
    }

    private void fSpostaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Sposta Immagine", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Immagine spostata");
            UtilityFetekkie.getInstance().RitornaProssimaImmagine(context);
        }
    }

    private void fModificaImmagine(String result) {
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Modifica Immagine", result);
        if (ritorno) {
            String Path = context.getFilesDir() + "/Immagini/AppoggioFET.jpg";
            Bitmap bmp = BitmapFactory.decodeFile(Path);
            VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImg().setImageBitmap(bmp);

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
            UtilityFetekkie.getInstance().RitornaProssimaImmagine(context);
        }
    }

    private void fRefreshImmagini(String result) {
        // boolean ritorno = ControllaRitorno("Ritorna Refresh Immagini", result);
        // if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        // } else {

        // }
    }

    private void fRitornaCategorie(String result) {
        boolean ritorno = ControllaRitorno("Ritorna categorie", result);
        if (ritorno) {
            // try {
                List<StrutturaImmaginiCategorieFE> listaCategorie = new ArrayList<>();

                StrutturaImmaginiCategorieFE sicT = new StrutturaImmaginiCategorieFE();
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
                    StrutturaImmaginiCategorieFE sicT2 = new StrutturaImmaginiCategorieFE();
                    sicT2.setIdCategoria(-1);
                    sicT2.setCategoria(c);
                    sicT2.setAlias("");
                    sicT2.setTag("");

                    listaCategorie.add(sicT2);
                }

                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setListaCategorie(listaCategorie);

                /* int idCategoriaImpostata = -1;
                if (VariabiliStaticheMostraImmaginiFetekkie.getInstance().getUltimaImmagineCaricata() != null) {
                    idCategoriaImpostata = VariabiliStaticheMostraImmaginiFetekkie.getInstance().getUltimaImmagineCaricata().getIdCategoria();
                } */
                String idCategoriaImpostata = VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoria();
                String CategoriaAttuale = "";

                String[] l = new String[listaCategorie.size()];
                int c = 0;
                for (StrutturaImmaginiCategorieFE s : listaCategorie) {
                    l[c] = s.getCategoria();
                    if (s.getCategoria().equals(idCategoriaImpostata)) {
                        CategoriaAttuale = s.getCategoria();
                    }
                    c++;
                }

                db_dati_fetekkie db = new db_dati_fetekkie(context);
                db.EliminaCategorie();
                for (StrutturaImmaginiCategorieFE s : listaCategorie) {
                    db.ScriveCategoria(s);
                }
                db.ChiudeDB();

                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setCategoriAttuale(CategoriaAttuale);
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setListaCategorieFet(l);
                UtilityFetekkie.getInstance().AggiornaCategorie(context);
                UtilityFetekkie.getInstance().AggiornaCategorieSpostamento(context);
            // } catch (JSONException e) {
            //     throw new RuntimeException(e);
            // }
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fProssimaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossima immagine", result);
        if (ritorno) {
            String path = "";
            int id = -1;
            if (result.contains("§")) {
                String[] p = result.split("§");
                path = VariabiliStaticheMostraImmaginiFetekkie.PathUrl + p[0];
                id = Integer.parseInt(p[1]);
            } else {
                path = VariabiliStaticheMostraImmaginiFetekkie.PathUrl + result;
            }

            VariabiliStaticheMostraImmaginiFetekkie.getInstance().setIdImmagine(id);

            StrutturaImmaginiLibraryFE s = new StrutturaImmaginiLibraryFE();
            s.setIdImmagine(id);
            s.setUrlImmagine(path);
            s.setCategoria(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoria());
            if (result.contains("§")) {
                String[] r = result.split("§");
                s.setNomeFile(r[0]);
            } else {
                s.setNomeFile(result);
            }
            s.setDataCreazione("");

            UtilityFetekkie.getInstance().AggiungeImmagine(context, result, s);

            VariabiliStaticheMostraImmaginiFetekkie.getInstance().setUltimaImmagineCaricata(s);

            DownloadImmagineFET d = new DownloadImmagineFET();
            d.EsegueChiamata(
                    context,
                    path,
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImg(),
                    path
            );
            // new DownloadImageFET(context, path,
            //         VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImg()).execute(path);

            /* try {
                JSONObject jObject = new JSONObject(result);
                StrutturaImmaginiLibrary si = UtilityFetekkie.getInstance().prendeStruttura(jObject);
                if (si != null) {
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setIdCategoria(si.getIdCategoria());
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setIdImmagine(si.getIdImmagine());

                    UtilityFetekkie.getInstance().AggiungeImmagine(context, result, si);

                    new DownloadImageFET(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImg()).execute(si.getUrlImmagine());
                }
            } catch (JSONException e) {

            } */
            // Utility.getInstance().VisualizzaMessaggio(result);
        // } else {
        //     UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
