package com.looigi.wallpaperchanger2.classeMostraVideo.webservice;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.looigi.wallpaperchanger2.classeMostraVideo.UtilityVideo;
import com.looigi.wallpaperchanger2.classeMostraVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.classeMostraVideo.db_dati_video;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWSV implements TaskDelegate {
    private static final String NomeMaschera = "CHIAMATEWSVIDEO";
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheVideo.UrlWS + "/";
    private String ws = "newLooVF.asmx/";
    private String NS="http://newLooVF.org/";
    private String SA="http://newLooVF.org/";
    private String TipoOperazione = "";
    private Context context;
    private boolean ApriDialog = false;

    public ChiamateWSV(Context context) {
        this.context = context;
    }

    public void RitornaProssimoVideo() {
        String Filtro = VariabiliStaticheVideo.getInstance().getFiltro();
        String Categoria = VariabiliStaticheVideo.getInstance().getCategoria();

        String Urletto="RitornaProssimoVideo?" +
                "Categoria=" + Categoria.replace("\\", "§") +
                "&Filtro=" + Filtro;

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
            String url = VariabiliStaticheVideo.PathUrl + result;
            VariabiliStaticheVideo.getInstance().setUltimoLink(url);
            String[] u = result.split("/");
            String res = u[u.length -1];
            VariabiliStaticheVideo.getInstance().getTxtTitolo().setText(res);

            db_dati_video db = new db_dati_video(context);
            db.ScriveUltimoVideo();

            UtilityVideo.getInstance().ImpostaVideo();
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        }
    }
}
