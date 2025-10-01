package com.looigi.wallpaperchanger2.Films.webservice;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.Films.UtilityFilms;
import com.looigi.wallpaperchanger2.Films.VariabiliStaticheFilms;
import com.looigi.wallpaperchanger2.Films.db_dati_films;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class ChiamateWSF implements TaskDelegate {
    private static final String NomeMaschera = "Chiamate_WS_Films";
    //private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheFilms.UrlWS + "/";
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private GifImageView imgAttesa;

    public ChiamateWSF(Context context) {
        this.context = context;
    }

    public void ImpostaImgAttesa(GifImageView imgAttesa) {
        this.imgAttesa = imgAttesa;
    }

    public void RitornaProssimoFilms() {
        String Filtro = VariabiliStaticheFilms.getInstance().getFiltro();
        String Categoria = VariabiliStaticheFilms.getInstance().getCategoria();

        String Urletto="RitornaProssimoFilms?" +
                "Categoria=" + Categoria.replace("\\", "§") +
                "&Filtro=" + Filtro +
                "&Random=" + VariabiliStaticheFilms.getInstance().getRandom() +
                "&pUltimoFilms=" + VariabiliStaticheFilms.getInstance().getIdUltimoFilms() +
                "&OrdinaPerVisualizzato=" + (VariabiliStaticheFilms.getInstance().isRicercaPerVisua() ? "S" : "N");

        TipoOperazione = "RitornaProssimoFilms";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                35000,
                ApriDialog);
    }

    public void RitornaCategorie(boolean forzaLettura) {
        if (!forzaLettura) {
            db_dati_films db = new db_dati_films(context);
            List<String> lista = db.LeggeCategorie();
            db.ChiudeDB();
            if (!lista.isEmpty()) {
                VariabiliStaticheFilms.getInstance().setListaCategorie(lista);
                UtilityFilms.getInstance().AggiornaCategorie(context);
                UtilityFilms.getInstance().AggiornaCategorieSpostamento(context);

                return;
            }
        }

        String Urletto="RitornaCategorieFilms";

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

    public void SpostaFilm() {
        String Urletto="SpostaFilm?" +
                "Categoria=" + VariabiliStaticheFilms.getInstance().getCategoria() +
                "&idFilm=" + VariabiliStaticheFilms.getInstance().getIdUltimoFilms() +
                "&NuovaCategoria=" + VariabiliStaticheFilms.getInstance().getIdCategoriaSpostamento();

        TipoOperazione = "SpostaFilm";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void EliminaFilm(String id) {
        String Urletto="EliminaFilm?" +
                "idFilm=" + id;

        TipoOperazione = "EliminaFilm";
        // ControllaTempoEsecuzione = false;

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                5000,
                ApriDialog);
    }

    public void RefreshFilms(String Categoria) {
        String Urletto="RefreshFilms?" +
                "Categoria=" + Categoria +
                "&Completo=" + (VariabiliStaticheFilms.getInstance().isAggiornamentoCompleto() ? "S" : "");

        TipoOperazione = "RefreshFilms";
        // ControllaTempoEsecuzione = false;

        UtilitiesGlobali.getInstance().ApreToast(context, "Refresh films lanciato");

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

        UtilityFilms.getInstance().ScriveLog(context, NomeMaschera,
                "Chiamata WS " + TipoOperazione + ". OK");

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSF i = new InterrogazioneWSF();
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
                UtilityFilms.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

                switch (TipoOperazione) {
                    case "RitornaProssimoFilms":
                        fRitornaProssimoFilms(result);
                        break;
                    case "RitornaCategorie":
                        fRitornaCategorie(result);
                        break;
                    case "RefreshFilms":
                        fRefreshFilms(result);
                        break;
                    case "SpostaFilm":
                        fSpostaFilm(result);
                        break;
                    case "EliminaFilm":
                        fEliminaFilm(result);
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

    private void fEliminaFilm(String result) {
        boolean ritorno = ControllaRitorno("Elimina Film", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Film eliminato");

            ChiamateWSF ws = new ChiamateWSF(context);
            ws.RitornaProssimoFilms();
        }
    }

    private void fSpostaFilm(String result) {
        boolean ritorno = ControllaRitorno("Sposta Film", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Film spostato");
            // UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
        }
    }

    private void fRefreshFilms(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Refresh Films", result);
        if (ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fRitornaCategorie(String result) {
        boolean ritorno = ControllaRitorno("Ritorna categorie", result);
        if (ritorno) {
            String[] lista = result.split("§");
            List<String> l = new ArrayList<>();
            l.add("Tutte");
            for (String ll : lista) {
                l.add(ll);
            }

            db_dati_films db = new db_dati_films(context);
            db.EliminaCategorie();
            for (String s : l) {
                db.ScriveCategoria(s);
            }
            db.ChiudeDB();

            VariabiliStaticheFilms.getInstance().setListaCategorie(l);
            UtilityFilms.getInstance().AggiornaCategorie(context);
            UtilityFilms.getInstance().AggiornaCategorieSpostamento(context);

            /* ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item, l);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            VariabiliStaticheFilms.getInstance().getSpnCategorie().setAdapter(adapter); */
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fRitornaProssimoFilms(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossima immagine", result);
        if (ritorno) {
            String url ="";
            int id = -1;
            if (result.contains("§")) {
                String[] p = result.split("§");
                url = VariabiliStaticheFilms.PathUrl + p[0];
                id = Integer.parseInt(p[1]);
            } else {
                url = VariabiliStaticheFilms.PathUrl + result;
            }

            VariabiliStaticheFilms.getInstance().setUltimoLink(url);
            VariabiliStaticheFilms.getInstance().setIdUltimoFilms(id);

            VariabiliStaticheFilms.getInstance().ScriveInfo(url);

            db_dati_films db = new db_dati_films(context);
            db.ScriveUltimoFilms();
            db.ChiudeDB();

            UtilityFilms.getInstance().ImpostaFilms();
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
