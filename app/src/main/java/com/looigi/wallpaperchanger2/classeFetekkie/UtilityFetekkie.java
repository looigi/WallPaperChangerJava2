package com.looigi.wallpaperchanger2.classeFetekkie;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiCategorieFE;
import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiLibraryFE;
import com.looigi.wallpaperchanger2.classeFetekkie.webservice.ChiamateWSFET;
import com.looigi.wallpaperchanger2.classeFetekkie.webservice.DownloadImmagineFET;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.log.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UtilityFetekkie {
    private static final String NomeMaschera = "Utility_Immagini_FET";
    private static UtilityFetekkie instance = null;
    private int quantiCaricamenti = 0;
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;

    private UtilityFetekkie() {
    }

    public static UtilityFetekkie getInstance() {
        if (instance == null) {
            instance = new UtilityFetekkie();
        }

        return instance;
    }

    public void Attesa(boolean Acceso) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Acceso) {
                    if (quantiCaricamenti == 0) {
                        if (VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImgCaricamento() != null) {
                            VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                        }
                    }
                    quantiCaricamenti++;
                } else {
                    quantiCaricamenti--;
                    if (quantiCaricamenti < 1) {
                        quantiCaricamenti = 0;
                        if (VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImgCaricamento() != null) {
                            VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
                        }
                    }
                }
            }
        }, 50);
    }

    public void VisualizzaErrore(Context context, String Errore) {
        // VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        ScriveLog(context, NomeMaschera, "Visualizzo messaggio di errore. Schermo acceso: " +
                VariabiliStaticheWallpaper.getInstance().isScreenOn());
        if (VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
            act.runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(act).create();
                    alertDialog.setTitle("Messaggio " + VariabiliStaticheWallpaper.channelName);
                    alertDialog.setMessage(Errore);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    // alertDialog.show();
                }
            });
        } else {
            ScriveLog(context, NomeMaschera,"Schermo spento. Non visualizzo messaggio di errore: " + Errore);
        }
    }

    public void ScriveLog(Context context, String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, true);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityDetector.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheDetector.getInstance().getNomeFileDiLog())) { */
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Fetekkie", Maschera,  Log);
            // }
        } else {

        }
    }

    public void AttivaTimerSlideShow(Context context) {
        handlerThread = new HandlerThread("background-thread_" +
                VariabiliStaticheWallpaper.channelName + "_slideshow");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                RitornaProssimaImmagine(context);
            }
        };
        handler.postDelayed(r, VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSecondiAttesa());
    }

    public void RiattivaTimer() {
        if (handler != null) {
            handler.postDelayed(r, VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSecondiAttesa());
        }
    }

    public void BloccaTimerSlideShow() {
        if (handler != null && r != null && handlerThread != null) {
            handlerThread.quit();

            handler.removeCallbacksAndMessages(null);

            handler.removeCallbacks(r);
            handler = null;
            r = null;
        }
    }

    public void AggiungeImmagine(Context context, String result, StrutturaImmaginiLibraryFE si) {
        String path1 = context.getFilesDir() + "/Immagini";
        UtilityWallpaper.getInstance().CreaCartelle(path1);
        String NomeFile = "/UltimaFetekkie.txt";
        if (UtilityWallpaper.getInstance().EsisteFile(path1 + NomeFile)) {
            UtilityDetector.getInstance().EliminaFile(path1 + "/" + NomeFile);
        }
        UtilityDetector.getInstance().CreaFileDiTesto(path1, NomeFile, result +
                "§" + VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoria());

        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setUltimaImmagineCaricata(si);

        VariabiliStaticheMostraImmaginiFetekkie.getInstance().ScriveInfoImmagine(si);

        VariabiliStaticheMostraImmaginiFetekkie.getInstance().AggiungeCaricata();
    }

    public void RitornaProssimaImmagine(Context context) {
        ChiamateWSFET ws = new ChiamateWSFET(context);
        ws.RitornaProssimaImmagine(
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoria()
        );
    }

    public void TornaIndietro(Context context) {
        if (!VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImmaginiCaricate().isEmpty()) {
            int ultima = VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImmaginiCaricate().size() - 1;
            StrutturaImmaginiLibraryFE s = VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImmaginiCaricate().get(
                    ultima);

            VariabiliStaticheMostraImmaginiFetekkie.getInstance().setUltimaImmagineCaricata(s);
            VariabiliStaticheMostraImmaginiFetekkie.getInstance().setCategoria(s.getCategoria());
            VariabiliStaticheMostraImmaginiFetekkie.getInstance().setIdImmagine(s.getIdImmagine());

            DownloadImmagineFET d = new DownloadImmagineFET();
            d.EsegueChiamata(
                    context,
                    s.getUrlImmagine(),
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImg(),
                    s.getUrlImmagine()
            );
            // new DownloadImagePEN(context, s.getUrlImmagine(),
            //         VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImg()).execute(s.getUrlImmagine());

            List<StrutturaImmaginiLibraryFE> lista = new ArrayList<>();
            for (int i = 0; i < ultima; i++) {
                lista.add(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImmaginiCaricate().get(i));
            }
            VariabiliStaticheMostraImmaginiFetekkie.getInstance().setImmaginiCaricate(lista);

            if (VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImmaginiCaricate().isEmpty()) {
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setImmaginiCaricate(new ArrayList<>());
            }
        }
    }

    public StrutturaImmaginiLibraryFE prendeStruttura(JSONObject j) {
        if (j != null) {
            // {"idImmagine": 522,"idCategoria": 1,"Categoria": "21_Sextury","Alias": ";^$$$$-$$$ ;",
            // "Tag": "","Cartella": "0004","NomeFile": "21-Sextury Lesbiandy Fetish Colombia_0050.jpg",
            // "DimensioneFile": 308264,"DataCreazione": "10/08/2024 12:14:26","DataModifica": "04/02/2024 04:56:29",
            // "DimensioniImmagine": "1280x834",
            // "UrlImmagine": "http://looigi.no-ip.biz:1085/Materiale/newPLibrary/21_Sextury/0004/21-Sextury Lesbiandy Fetish Colombia_0050.jpg",
            // "PathImmagine": "*S**S*192.168.0.33*S*Public*S*Materiale*S*newPLibrary*S*21_Sextury*S*0004*S*21-Sextury Lesbiandy Fetish Colombia_0050.jpg",
            // "EsisteImmagine": "True","ImmaginiCategoria": 758}
            StrutturaImmaginiLibraryFE si = new StrutturaImmaginiLibraryFE();
            try {
                si.setIdImmagine(j.getInt("idImmagine"));
                si.setIdCategoria(j.getInt("idCategoria"));
                si.setCategoria(j.getString("Categoria"));
                si.setAlias(j.getString("Alias"));
                si.setTag(j.getString("Tag"));
                si.setCartella(j.getString("Cartella"));
                si.setNomeFile(j.getString("NomeFile"));
                si.setDimensioneFile(j.getInt("DimensioneFile"));
                si.setDataCreazione(j.getString("DataCreazione"));
                si.setDataModifica(j.getString("DataModifica"));
                si.setDimensioniImmagine(j.getString("DimensioniImmagine"));
                si.setUrlImmagine(j.getString("UrlImmagine"));
                si.setPathImmagine(j.getString("PathImmagine"));
                si.setEsisteImmagine(j.getString("EsisteImmagine").equals("True"));
                si.setImmaginiCategoria(j.getInt("ImmaginiCategoria"));
                return si;
            } catch (JSONException e) {
                return null;
            }

        } else {
            return null;
        }
    }

    public void AggiornaCategorie(Context context) {
        List<StrutturaImmaginiCategorieFE> l1 = new ArrayList<>();

        for (StrutturaImmaginiCategorieFE s : VariabiliStaticheMostraImmaginiFetekkie.getInstance().getListaCategorie()) {
            if (s.getCategoria().toUpperCase().trim().contains(
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getFiltroCategoria().toUpperCase().trim())) {
                l1.add(s);
            }
        }

        String[] l = new String[l1.size()];
        int c = 0;
        for (StrutturaImmaginiCategorieFE s : l1) {
            l[c] = s.getCategoria();
            c++;
        }

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, l);

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSpnCategorie(),
                l,
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoriAttuale()
        );

        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                l
        );
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSpnCategorie().setAdapter(adapter); */

        // if (!VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoriAttuale().isEmpty()) {
            // int spinnerPosition = adapter.getPosition(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoriAttuale());
            // VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSpnCategorie().setSelection(spinnerPosition);
        // }
    }

    public void AggiornaCategorieSpostamento(Context context) {
        List<StrutturaImmaginiCategorieFE> l1 = new ArrayList<>();

        for (StrutturaImmaginiCategorieFE s : VariabiliStaticheMostraImmaginiFetekkie.getInstance().getListaCategorie()) {
            if (s.getCategoria().toUpperCase().trim().contains(
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().getFiltroCategoriaSpostamento().toUpperCase().trim())) {
                l1.add(s);
            }
        }

        String[] l = new String[l1.size()];
        int c = 0;
        for (StrutturaImmaginiCategorieFE s : l1) {
            l[c] = s.getCategoria();
            c++;
        }

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, l);
        ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                l
        );
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSpnSpostaCategorie().setAdapter(adapter);
    }

    public void SalvataggioImmagine(Context context, boolean Sovrascrive) {
        String Path = context.getFilesDir() + "/Immagini/AppoggioFET.jpg";
        StrutturaImmaginiLibraryFE s = VariabiliStaticheMostraImmaginiFetekkie.getInstance().getUltimaImmagineCaricata();

        String encodedImage = UtilitiesGlobali.getInstance().convertBmpToBase64(Path);

        ChiamateWSFET c = new ChiamateWSFET(context);
        c.ModificaImmagine(s, encodedImage, Sovrascrive);
    }
}
