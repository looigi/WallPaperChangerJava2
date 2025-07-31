package com.looigi.wallpaperchanger2.classeWallpaper.WebServices;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.adapters.AdapterListenerImmagini;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.db_dati_wallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.ArrayList;
import java.util.List;


public class ChiamateWsWP implements TaskDelegate {
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheStart.UrlWSGlobale + ":" + VariabiliStaticheStart.PortaWallPaperChanger + "/"; // "http://www.wsloovf.looigi.it/";
    private String TipoOperazione = "";
    private Context context;
    private final String ws = "wallPaperChangerWS.asmx/";
    private final String NS="http://wallpaperChangerWS.it/";
    private final String SA="http://wallpaperChangerWS.it/";
    private String NomeImmaginePerModifica;
    private String StringaBase64;

    public ChiamateWsWP(Context context) {
        this.context = context;
    }

    public void ModificaImmagine(StrutturaImmagine s, String immagine) {
        /* NomeImmaginePerModifica = s.getImmagine().replace(
                VariabiliStaticheWallpaper.PercorsoImmagineSuURL, ""
        ); */
        StringaBase64 = immagine;

        String PathImmagine = VariabiliStaticheStart.UrlWSGlobale + ":" +
                VariabiliStaticheStart.PortaDiscoPublic + "/Sfondi/" +
                s.getCartellaRemota(); // "http://www.sfondi.looigi.it/" + s.getCartellaRemota();
        NomeImmaginePerModifica = s.getCartellaRemota().replace("/", "\\");
        PathImmagine = PathImmagine.replace("&", "-A-")
                .replace("?", "-P-")
                .replace(":", "-D-")
                .replace("/", "-S-")
                .replace("\\", "-B-");
        String Urletto="ModificaImmagineWP?" +
                "Immagine=" + PathImmagine +
                "&DatiImmagine=" + immagine;

        TipoOperazione = "ModificaImmagine";
        // ControllaTempoEsecuzione = false;

        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(true);
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

    public void EliminaImmagine(StrutturaImmagine s) {
        NomeImmaginePerModifica = s.getPathImmagine().replace(
                VariabiliStaticheWallpaper.PercorsoImmagineSuURL, ""
        );

        String Urletto="EliminaImmagineWP?" +
                "Immagine=" + s.getPathImmagine()
                .replace("&", "-A-")
                .replace("?", "-P-")
                .replace(":", "-D-")
                .replace("/", "-S-")
                .replace("\\", "-B-");

        TipoOperazione = "EliminaImmagine";
        // ControllaTempoEsecuzione = false;

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

    public void TornaProssimaImmagine() {
        String perData = "";

        if (VariabiliStaticheWallpaper.getInstance().getModoRicercaImmagine() == 0) {
            if (VariabiliStaticheWallpaper.getInstance().isPerData()) {
                perData = String.valueOf(VariabiliStaticheWallpaper.getInstance().getGiorniDifferenza()).trim();
            }
        }

        String Urletto="TornaProssimaImmagine?" +
                "Filtro=" + VariabiliStaticheWallpaper.getInstance().getFiltro() + "&" +
                "PerData=" + perData;
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

    public void TornaImmagini(boolean ForzaRefresh) {
        if (!ForzaRefresh) {
            db_dati_wallpaper db = new db_dati_wallpaper(context);
            List<StrutturaImmagine> lista = db.TornaImmaginiRemote();
            if (!lista.isEmpty()) {
                VariabiliStaticheWallpaper.getInstance().setListaImmagini(lista);

                VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Numero immagini online: " + lista.size());

                VariabiliStaticheWallpaper.getInstance().setAdapterImmagini(new AdapterListenerImmagini(context,
                        VariabiliStaticheWallpaper.getInstance().getListaImmagini()));
                VariabiliStaticheWallpaper.getInstance().getLstImmagini().setAdapter(VariabiliStaticheWallpaper.getInstance().getAdapterImmagini());

                VariabiliStaticheWallpaper.getInstance().getLayScelta().setVisibility(LinearLayout.VISIBLE);

                return;
            }
        }

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

        InterrogazioneWSWP i = new InterrogazioneWSWP();
        i.EsegueChiamata(
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                "0", // TimeStampAttuale,
                this
        );
        /* bckAsyncTask = new LetturaWSAsincrona(
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                "0", // TimeStampAttuale,
                this);
        bckAsyncTask.execute(Urletto); */
    }

    @Override
    public void TaskCompletionResult(String result) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                UtilityWallpaper.getInstance().Attesa(false);

                switch (TipoOperazione) {
                    case "TornaProssimaImmagine":
                        fTornaProssimaImmagine(result);
                        break;
                    case "TornaImmagini":
                        fTornaImmagini(result);
                        break;
                    case "ModificaImmagine":
                        fModificaImmagine(result);
                        break;
                    case "EliminaImmagine":
                        fEliminaImmagine(result);
                        break;
                    case "RitornaListaImmagini":
                        fRitornaListaImmagini(result);
                        break;
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
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

    private void fRitornaListaImmagini(String result) {
        if (result.contains("ERROR:") || result.toUpperCase().contains("ANYTYPE")) {
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            List<String> ImmaginiSuIONOS = new ArrayList<>();
            String[] lista = result.split(";");
            for (String s : lista) {
                String nome = s.replace("*PV*", ";").replace("/", "\\");

                ImmaginiSuIONOS.add(nome);
            }


        }
    }

    private void fModificaImmagine(String result) {
        VariabiliStaticheModificaImmagine.getInstance().ImpostaAttesa(false);
        if (result.contains("ERROR:") || result.toUpperCase().contains("ANYTYPE")) {
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                    ws.ScriveImmagineSuSfondiLocale(NomeImmaginePerModifica, StringaBase64);
                }
            };
            handlerTimer.postDelayed(rTimer, 100);
        }
    }

    private void fEliminaImmagine(String result) {
        if (result.contains("ERROR:") || result.toUpperCase().contains("ANYTYPE")) {
            UtilityWallpaper.getInstance().VisualizzaErrore(context, result);
        } else {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                    ws.EliminaImmagineSuSfondiLocale(NomeImmaginePerModifica, false);
                }
            };
            handlerTimer.postDelayed(rTimer, 100);

            // UtilitiesGlobali.getInstance().ApreToast(context, "Immagine eliminata");
        }
    }

    private void fTornaImmagini(String result) {
        if (result.contains("ERROR:") || result.toUpperCase().contains("ANYTYPE")) {
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
                s.setCartellaRemota(Campi[1]);

                lista.add(s);
            }
            VariabiliStaticheWallpaper.getInstance().setListaImmagini(lista);

            db_dati_wallpaper db = new db_dati_wallpaper(context);
            db.ScriveImmagineRemote(lista);

            VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Numero immagini online: " + lista.size());

            VariabiliStaticheWallpaper.getInstance().setAdapterImmagini(new AdapterListenerImmagini(context,
                    VariabiliStaticheWallpaper.getInstance().getListaImmagini()));
            VariabiliStaticheWallpaper.getInstance().getLstImmagini().setAdapter(VariabiliStaticheWallpaper.getInstance().getAdapterImmagini());

            VariabiliStaticheWallpaper.getInstance().getLayScelta().setVisibility(LinearLayout.VISIBLE);
        }
    }

    private void fTornaProssimaImmagine(String result) {
        if (result.contains("ERROR:") || result.toUpperCase().contains("ANYTYPE")) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            // 2433;/var/www/html/CartelleCondivise/SfondiDir/Donne/MetalWomen/df89106251200cc0021db5ae3e32.jpg
            String[] c = result.split(";");
            String quanteImmagini = c[0];
            String Immagine = c[1].replace("C:\\gDrive\\Sfondi\\", "").replace("\\", "/");
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
            String Cartella = c[1];
            Cartella = Cartella.replace("C:\\gDrive\\Sfondi\\", "");
            // IA/Donne/1683475712232.jpg

            si.setCartellaRemota(Cartella);

            VariabiliStaticheWallpaper.getInstance().setUltimaImmagine(si);

            Immagine = VariabiliStaticheWallpaper.PercorsoImmagineSuURL + Immagine;
            String[] cc = Immagine.split("/");
            String NomeImmagine = cc[cc.length - 1];
            if (VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini() != null) {
                VariabiliStaticheWallpaper.getInstance().getTxtQuanteImmagini().setText("Numero immagini online: " + quanteImmagini);
            }
            VariabiliStaticheWallpaper.getInstance().setImmaginiOnline(Integer.parseInt(quanteImmagini));

            DownloadImmagineWP d = new DownloadImmagineWP();
            d.EsegueChiamata(context, NomeImmagine, null, Immagine, false, "");
        }
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
    }
}
