package com.looigi.wallpaperchanger2.webservice;

import android.content.Context;

import com.looigi.wallpaperchanger2.utilities.Utility;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheServizio;

public class ChiamateWS implements TaskDelegate {
    private static final String NomeMaschera = "CHIAMATEWS";
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheServizio.UrlWS + "/";
    private String ws = "looVF.asmx/";
    private String NS="http://looVF.org/";
    private String SA="http://looVF.org/";
    private String TipoOperazione = "";
    private Context context;

    public ChiamateWS(Context context) {
        this.context = context;
    }

    public void TornaProssimaImmagine() {
        String Urletto="TornaProssimaImmagine";
        boolean ApriDialog = false;
        TipoOperazione = "TornaProssimaImmagine";

        Esegue(
                RadiceWS + ws + Urletto,
                "TornaProssimaImmagine",
                NS,
                SA,
                35000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {

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
        Utility.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

        switch (TipoOperazione) {
            case "TornaProssimaImmagine":
                fTornaProssimaImmagine(result);
                break;
        }
    }

    public void StoppaEsecuzione() {
        bckAsyncTask.cancel(true);
    }

    private void fTornaProssimaImmagine(String result) {
        if (result.contains("ERROR:")) {
            Utility.getInstance().VisualizzaErrore(context, result);
        } else {
            // 2433;/var/www/html/CartelleCondivise/SfondiDir/Donne/MetalWomen/df89106251200cc0021db5ae3e32.jpg
            String[] c = result.split(";");
            String quanteImmagini = c[0];
            String Immagine = c[1].replace("/var/www/html/Sfondi", "");
            String Datella = c[2];
            String Dimensioni = c[3];
            VariabiliStaticheServizio.getInstance().setDataAppoggio(Datella);
            VariabiliStaticheServizio.getInstance().setDimeAppoggio(Dimensioni);

            Immagine = VariabiliStaticheServizio.PercorsoImmagineSuURL + Immagine;
            String[] cc = Immagine.split("/");
            String NomeImmagine = cc[cc.length - 1];
            VariabiliStaticheServizio.getInstance().getTxtQuanteImmagini().setText("Numero immagini online: " + quanteImmagini);

            VariabiliStaticheServizio.getInstance().setImmaginiOnline(Integer.parseInt(quanteImmagini));

            new DownloadImage(context, NomeImmagine).execute(Immagine);
        }
    }
}
