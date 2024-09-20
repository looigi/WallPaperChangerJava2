package com.looigi.wallpaperchanger2.webservice;

import android.content.Context;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.AdapterListenerImmagini;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.VariabiliStaticheWallpaper;

import java.util.ArrayList;
import java.util.List;

public class ChiamateWS implements TaskDelegate {
    private static final String NomeMaschera = "CHIAMATEWS";
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheWallpaper.UrlWS + "/";
    private String ws = "looVF.asmx/";
    private String NS="http://looVF.org/";
    private String SA="http://looVF.org/";
    private String TipoOperazione = "";
    private Context context;

    public ChiamateWS(Context context) {
        this.context = context;
    }

    public void TornaImmagini() {
        String Urletto="TornaImmagini";
        boolean ApriDialog = false;
        TipoOperazione = "TornaImmagini";

        Esegue(
                RadiceWS + ws + Urletto,
                "TornaImmagini",
                NS,
                SA,
                35000,
                ApriDialog);
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

        UtilityWallpaper.getInstance().Attesa(true);

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
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Ritorno WS " + TipoOperazione + ". OK");

        UtilityWallpaper.getInstance().Attesa(false);

        switch (TipoOperazione) {
            case "TornaProssimaImmagine":
                fTornaProssimaImmagine(result);
                break;
            case "TornaImmagini":
                fTornaImmagini(result);
                break;
        }
    }

    public void StoppaEsecuzione() {
        bckAsyncTask.cancel(true);
    }

    private void fTornaImmagini(String result) {
        if (result.contains("ERROR:")) {
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            // ColtoEDiClasse.jpg;Sport/ColtoEDiClasse.jpg;130629;05/26/2015
            List<StrutturaImmagine> lista = new ArrayList<>();
            String[] righe = result.split("§");
            for (int i = 0; i < righe.length; i++) {
                String[] Campi = righe[i].split(";");
                StrutturaImmagine s = new StrutturaImmagine();
                s.setImmagine(Campi[0]);
                s.setPathImmagine(VariabiliStaticheWallpaper.PercorsoImmagineSuURL + "/" + Campi[1]);
                s.setDimensione(Campi[2]);
                s.setDataImmagine(Campi[3]);
                lista.add(s);
            }
            VariabiliStaticheWallpaper.getInstance().setListaImmagini(lista);
            VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Numero immagini online: " + lista.size());

            VariabiliStaticheWallpaper.getInstance().setAdapterImmagini(new AdapterListenerImmagini(context,
                    VariabiliStaticheWallpaper.getInstance().getListaImmagini()));
            VariabiliStaticheWallpaper.getInstance().getLstImmagini().setAdapter(VariabiliStaticheWallpaper.getInstance().getAdapterImmagini());

            VariabiliStaticheWallpaper.getInstance().getLayScelta().setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void fTornaProssimaImmagine(String result) {
        if (result.contains("ERROR:")) {
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            // 2433;/var/www/html/CartelleCondivise/SfondiDir/Donne/MetalWomen/df89106251200cc0021db5ae3e32.jpg
            String[] c = result.split(";");
            String quanteImmagini = c[0];
            String Immagine = c[1].replace("/var/www/html/Sfondi", "");
            String Datella = c[2];
            String Dimensioni = c[3];
            VariabiliStaticheWallpaper.getInstance().setDataAppoggio(Datella);
            VariabiliStaticheWallpaper.getInstance().setDimeAppoggio(Dimensioni);

            StrutturaImmagine si = new StrutturaImmagine();
            String sNomeImmagine = c[1];
            if (sNomeImmagine.toUpperCase().contains("HTTP:")) {
                String[] s = sNomeImmagine.split("/");
                sNomeImmagine = s[s.length - 2] + "/" + s[s.length - 1];
            }
            si.setImmagine(sNomeImmagine);
            si.setPathImmagine(c[1]);
            si.setDimensione(c[3]);
            si.setDataImmagine(c[2]);

            VariabiliStaticheWallpaper.getInstance().setUltimaImmagine(si);

            Immagine = VariabiliStaticheWallpaper.PercorsoImmagineSuURL + Immagine;
            String[] cc = Immagine.split("/");
            String NomeImmagine = cc[cc.length - 1];
            VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Numero immagini online: " + quanteImmagini);

            VariabiliStaticheWallpaper.getInstance().setImmaginiOnline(Integer.parseInt(quanteImmagini));

            new DownloadImage(context, NomeImmagine, null).execute(Immagine);
        }
    }
}
