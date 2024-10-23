package com.looigi.wallpaperchanger2.classeFilms.webservice;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeFilms.UtilityFilms;
import com.looigi.wallpaperchanger2.classeFilms.VariabiliStaticheFilms;
import com.looigi.wallpaperchanger2.classeFilms.db_dati_films;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

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
                "&pUltimoFilms=" + VariabiliStaticheFilms.getInstance().getIdUltimoFilms();

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

    public void RitornaCategorie() {
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

    public void RefreshFilms() {
        String Urletto="RefreshFilms";

        TipoOperazione = "RefreshFilms?" +
                "Completo=";
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

    private void fRefreshFilms(String result) {
        boolean ritorno = ControllaRitorno("Ritorna Refresh Films", result);
        if (ritorno) {
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
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (context, android.R.layout.simple_spinner_item, l);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            VariabiliStaticheFilms.getInstance().getSpnCategorie().setAdapter(adapter);
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

            String[] u = result.split("/");
            String res = u[u.length -1];
            if (res.contains("§")) {
                String[] r = res.split("§");
                res = r[0];
            }
            res = VariabiliStaticheFilms.getInstance().getIdUltimoFilms() + ": " + res;
            VariabiliStaticheFilms.getInstance().getTxtTitolo().setText(res);

            db_dati_films db = new db_dati_films(context);
            db.ScriveUltimoFilms();

            UtilityFilms.getInstance().ImpostaFilms();
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
