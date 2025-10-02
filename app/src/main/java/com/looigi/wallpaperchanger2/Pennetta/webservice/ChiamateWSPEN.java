package com.looigi.wallpaperchanger2.Pennetta.webservice;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiModifica.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.Pazzia.DownloadImmaginePAZZIA;
import com.looigi.wallpaperchanger2.Pazzia.UtilityPazzia;
import com.looigi.wallpaperchanger2.Pazzia.VariabiliStatichePazzia;
import com.looigi.wallpaperchanger2.Pennetta.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.Pennetta.UtilityPennetta;
import com.looigi.wallpaperchanger2.Pennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.Pennetta.db_dati_pennetta;
import com.looigi.wallpaperchanger2.Pennetta.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.Wallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSPEN implements TaskDelegate {
    private static final String NomeMaschera = "Chiamate_WS_Immagini_PEN";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheMostraImmaginiPennetta.UrlWS;
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private ImageView imgAttesa;
    private String daDove;

    public ChiamateWSPEN(Context context) {
        this.context = context;
    }

    public void ImpostaImgAttesa(ImageView imgAttesa) {
        this.imgAttesa = imgAttesa;
    }

    public void RitornaProssimaImmagine(String Categoria, String daDove) {
        this.daDove = daDove;

        String Urletto = "";

        switch (daDove) {
            case "PAZZIA":
                UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                        context,
                        VariabiliStatichePazzia.getInstance().getImgCaricamentoPEN(),
                        true
                );

                Urletto="RitornaProssimoPennetta?" +
                        "Categoria=" + (Categoria == null ? "" : Categoria) +
                        "&Filtro=" + VariabiliStatichePazzia.getInstance().getFiltroPEN() +
                        "&Random=S" +
                        "&UltimaImmagine=" + VariabiliStatichePazzia.getInstance().getUltimaPennetta() +
                        "&OrdinaPerVisualizzato=S";
                break;
            case "PENNETTA":
                Urletto="RitornaProssimoPennetta?" +
                        "Categoria=" + (Categoria == null ? "" : Categoria) +
                        "&Filtro=" + VariabiliStaticheMostraImmaginiPennetta.getInstance().getFiltro() +
                        "&Random=" + VariabiliStaticheMostraImmaginiPennetta.getInstance().getRandom() +
                        "&UltimaImmagine=" + VariabiliStaticheMostraImmaginiPennetta.getInstance().getIdImmagine() +
                        "&OrdinaPerVisualizzato=" + (VariabiliStaticheMostraImmaginiPennetta.getInstance().isRicercaPerVisua() ? "S" : "N");
                break;
        }

        TipoOperazione = "ProssimaImmagine";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                10000,
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
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheModificaImmagine.getInstance().getImgAttendere(),
                true
        );

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

    public void RitornaCategorie(boolean forzaLettura, String daDove) {
        this.daDove = daDove;

        if (!forzaLettura) {
            db_dati_pennetta db = new db_dati_pennetta(context);
            List<StrutturaImmaginiCategorie> lista = db.LeggeCategorie();
            db.ChiudeDB();

            if (!lista.isEmpty()) {
                switch (daDove) {
                    case "PENNETTA":
                        VariabiliStaticheMostraImmaginiPennetta.getInstance().setListaCategorie(lista);
                        UtilityPennetta.getInstance().AggiornaCategorie(context);
                        UtilityPennetta.getInstance().AggiornaCategorieSpostamento(context);
                        break;
                    case "PAZZIA":
                        VariabiliStatichePazzia.getInstance().setListaCategoriePEN(lista);
                        break;
                }

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
        String Urletto="RefreshPennetta?" +
                "Categoria=" + Categoria +
                "&Completo=" + (VariabiliStaticheMostraImmaginiPennetta.getInstance().isAggiornamentoCompleto() ? "S" : "");

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
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheMostraImmaginiPennetta.getInstance().getImgCaricamento(),
                true
        );

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
    }

    @Override
    public void TaskCompletionResult(String result) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                UtilityPennetta.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

                UtilitiesGlobali.getInstance().AttesaGif(
                        context,
                        VariabiliStaticheMostraImmaginiPennetta.getInstance().getImgCaricamento(),
                        false
                );

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
                    case "RefreshPennetta":
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
            if (result.contains("anyType{}")) {
                return false;
            } else {
                return true;
            }
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
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliStaticheModificaImmagine.getInstance().getImgAttendere(),
                false
        );

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
        // boolean ritorno = ControllaRitorno("Ritorna Refresh Immagini", result);
        // if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        // }
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

                VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoriaAttuale(CategoriaAttuale);
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setListaCategoriePen(l);

                switch(daDove) {
                    case "PENNETTA":
                        UtilityPennetta.getInstance().AggiornaCategorie(context);
                        UtilityPennetta.getInstance().AggiornaCategorieSpostamento(context);
                        break;
                    case "PAZZIA":
                        VariabiliStatichePazzia.getInstance().setListaCategoriePEN(listaCategorie);
                        break;
                }
            // } catch (JSONException e) {
            //     throw new RuntimeException(e);
            // }
        // } else {
        //     UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fProssimaImmagine(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossima immagine", result);
        if (ritorno) {
            String path = "";
            int immaginiFiltrate = -1;
            int immaginiCategoria = -1;

            int id = -1;
            if (result.contains("§")) {
                String[] p = result.split("§");
                path = VariabiliStaticheMostraImmaginiPennetta.PathUrl + p[0];
                id = Integer.parseInt(p[1]);
                immaginiCategoria = Integer.parseInt(p[2]);
                immaginiFiltrate = Integer.parseInt(p[3]);
            } else {
                path = VariabiliStaticheMostraImmaginiPennetta.PathUrl + result;
            }

            VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdImmagine(id);

            StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary();
            s.setIdImmagine(id);
            s.setUrlImmagine(path);
            s.setCategoria(VariabiliStaticheMostraImmaginiPennetta.getInstance().getCategoria());
            if (result.contains("§")) {
                String[] r = result.split("§");
                s.setNomeFile(r[0]);
            } else {
                s.setNomeFile(result);
            }
            s.setDataCreazione("");
            s.setImmaginiFiltrate(immaginiFiltrate);
            s.setImmaginiCategoria(immaginiCategoria);

            UtilityPennetta.getInstance().AggiungeImmagine(context, result, s);

            UtilityPennetta.getInstance().ScriveInfoSotto(s);

            VariabiliStaticheMostraImmaginiPennetta.getInstance().setUltimaImmagineCaricata(s);

            String path1 = context.getFilesDir() + "/Immagini";
            UtilityWallpaper.getInstance().CreaCartelle(path1);
            String NomeFile = "/UltimaPennetta.txt";
            if (UtilityWallpaper.getInstance().EsisteFile(path1 + NomeFile)) {
                Files.getInstance().EliminaFileUnico(path1 + NomeFile);
            }
            Files.getInstance().ScriveFile(path1, NomeFile, result);

            switch (daDove) {
                case "PENNETTA":
                    DownloadImmaginePEN d = new DownloadImmaginePEN();
                    d.EsegueChiamata(
                            context,
                            path,
                            VariabiliStaticheMostraImmaginiPennetta.getInstance().getImg(),
                            path
                    );
                    break;
                case "PAZZIA":
                    VariabiliStatichePazzia.getInstance().setUltimaPennetta(id);

                    DownloadImmaginePAZZIA d2 = new DownloadImmaginePAZZIA();
                    d2.EsegueChiamata(
                            context,
                            VariabiliStatichePazzia.getInstance().getImgPennetta(),
                            path,
                            "PENNETTA"
                    );
            }
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
        } else {
            if (daDove.equals("PAZZIA")) {
                UtilityPazzia.getInstance().ImpostaAttesaPazzia(
                        context,
                        VariabiliStatichePazzia.getInstance().getImgCaricamentoPEN(),
                        false
                );
            }

            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
