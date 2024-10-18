package com.looigi.wallpaperchanger2.classeMostraVideo.webservice;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;

import com.looigi.wallpaperchanger2.classeMostraVideo.UtilityVideo;
import com.looigi.wallpaperchanger2.classeMostraVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.classeMostraVideo.db_dati_video;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSV implements TaskDelegate {
    private static final String NomeMaschera = "CHiamate_WS_Video";
    //private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheVideo.UrlWS + "/";
    private final String ws = "newLooVF.asmx/";
    private final String NS="http://newLooVF.org/";
    private final String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;

    public ChiamateWSV(Context context) {
        this.context = context;
    }

    public void RitornaProssimoVideo() {
        String Filtro = VariabiliStaticheVideo.getInstance().getFiltro();
        String Categoria = VariabiliStaticheVideo.getInstance().getCategoria();

        String Urletto="RitornaProssimoVideo?" +
                "Categoria=" + Categoria.replace("\\", "§") +
                "&Filtro=" + Filtro +
                "&Random=" + VariabiliStaticheVideo.getInstance().getRandom() +
                "&pUltimoVideo=" + VariabiliStaticheVideo.getInstance().getIdUltimoVideo();

        TipoOperazione = "RitornaProssimoVideo";
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
        String Urletto="RitornaCategorieVideo";

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

        UtilityVideo.getInstance().ScriveLog(context, NomeMaschera, "Chiamata WS " + TipoOperazione + ". OK");

        Long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = tsLong.toString();

        InterrogazioneWSV i = new InterrogazioneWSV();
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
                UtilityVideo.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

                switch (TipoOperazione) {
                    case "RitornaProssimoVideo":
                        fRitornaProssimoVideo(result);
                        break;
                    case "RitornaCategorie":
                        fRitornaCategorie(result);
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
            return true;
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
            VariabiliStaticheVideo.getInstance().getSpnCategorie().setAdapter(adapter);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }

    private void fRitornaProssimoVideo(String result) {
        boolean ritorno = ControllaRitorno("Ritorna prossima immagine", result);
        if (ritorno) {
            String url ="";
            int id = -1;
            if (result.contains("§")) {
                String[] p = result.split("§");
                url = VariabiliStaticheVideo.PathUrl + p[0];
                id = Integer.parseInt(p[1]);
            } else {
                url = VariabiliStaticheVideo.PathUrl + result;
            }

            VariabiliStaticheVideo.getInstance().setUltimoLink(url);
            VariabiliStaticheVideo.getInstance().setIdUltimoVideo(id);

            String[] u = result.split("/");
            String res = u[u.length -1];
            res = VariabiliStaticheVideo.getInstance().getIdUltimoVideo() + ": " + res;
            VariabiliStaticheVideo.getInstance().getTxtTitolo().setText(res);

            db_dati_video db = new db_dati_video(context);
            db.ScriveUltimoVideo();

            UtilityVideo.getInstance().ImpostaVideo();
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
