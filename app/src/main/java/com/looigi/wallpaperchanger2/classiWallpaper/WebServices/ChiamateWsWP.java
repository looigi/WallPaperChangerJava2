package com.looigi.wallpaperchanger2.classiWallpaper.WebServices;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classiWallpaper.AdapterListenerImmagini;
import com.looigi.wallpaperchanger2.classiWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classiWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeMostraImmagini.webservice.DownloadImage;
import com.looigi.wallpaperchanger2.classiWallpaper.VariabiliStaticheWallpaper;

import java.util.ArrayList;
import java.util.List;


public class ChiamateWsWP implements TaskDelegate {
    private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = "http://www.wsloovf.looigi.it/";
    private String TipoOperazione = "";
    private Context context;
    private final String ws = "looVF.asmx/";
    private final String NS="http://looVF.org/";
    private final String SA="http://looVF.org/";

    public ChiamateWsWP(Context context) {
        this.context = context;
    }

    public void TornaProssimaImmagine() {
        String Urletto="TornaProssimaImmagine";
        TipoOperazione = "TornaProssimaImmagine";

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

    public void TornaImmagini() {
        String Urletto="TornaImmagini";
        boolean ApriDialog = false;
        TipoOperazione = "TornaImmagini";

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
            case "TornaProssimaImmagine":
                fTornaProssimaImmagine(result);
                break;
            case "TornaImmagini":
                fTornaImmagini(result);
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

    private void fTornaImmagini(String result) {
        if (result.contains("ERROR")) {
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
        if (result.contains("ERROR")) {
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

    public void StoppaEsecuzione() {
        bckAsyncTask.cancel(true);
    }
}
