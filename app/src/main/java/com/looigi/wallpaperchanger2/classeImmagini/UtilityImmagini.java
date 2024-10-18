package com.looigi.wallpaperchanger2.classeImmagini;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.DownloadImmagineMI;
import com.looigi.wallpaperchanger2.classeStandard.LogInterno;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UtilityImmagini {
    private static final String NomeMaschera = "Utility_Immagini";
    private static UtilityImmagini instance = null;
    private int quantiCaricamenti = 0;
    private Handler handler;
    private Runnable r;
    private HandlerThread handlerThread;

    private UtilityImmagini() {
    }

    public static UtilityImmagini getInstance() {
        if (instance == null) {
            instance = new UtilityImmagini();
        }

        return instance;
    }

    public void Attesa(boolean Acceso) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Acceso) {
                    if (quantiCaricamenti == 0) {
                        VariabiliStaticheMostraImmagini.getInstance().getImgCaricamento().setVisibility(LinearLayout.VISIBLE);
                    }
                    quantiCaricamenti++;
                } else {
                    quantiCaricamenti--;
                    if (quantiCaricamenti < 1) {
                        quantiCaricamenti = 0;
                        VariabiliStaticheMostraImmagini.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("IMMAGINI", Maschera,  Log);
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
        handler.postDelayed(r, VariabiliStaticheMostraImmagini.getInstance().getSecondiAttesa());
    }

    public void RiattivaTimer() {
        if (handler != null) {
            handler.postDelayed(r, VariabiliStaticheMostraImmagini.getInstance().getSecondiAttesa());
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

    public void AggiungeImmagine(Context context, String result, StrutturaImmaginiLibrary si) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                String path1 = context.getFilesDir() + "/Immagini";
                UtilityWallpaper.getInstance().CreaCartelle(path1);
                String NomeFile = "/UltimaImmagine.txt";
                if (UtilityWallpaper.getInstance().EsisteFile(path1 + NomeFile)) {
                    UtilityDetector.getInstance().EliminaFile(path1 + "/" + NomeFile);
                }
                UtilityDetector.getInstance().CreaFileDiTesto(path1, NomeFile, result);

                VariabiliStaticheMostraImmagini.getInstance().setUltimaImmagineCaricata(si);

                VariabiliStaticheMostraImmagini.getInstance().ScriveInfoImmagine(si);

                VariabiliStaticheMostraImmagini.getInstance().AggiungeCaricata();
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void RitornaProssimaImmagine(Context context) {
        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.RitornaProssimaImmagine(
                VariabiliStaticheMostraImmagini.getInstance().getIdCategoria(),
                VariabiliStaticheMostraImmagini.getInstance().getFiltro(),
                VariabiliStaticheMostraImmagini.getInstance().getIdImmagine(),
                VariabiliStaticheMostraImmagini.getInstance().getRandom()
        );
    }

    public void TornaIndietro(Context context) {
        if (!VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().isEmpty()) {
            int ultima = VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().size() - 1;
            StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().get(
                    ultima);

            VariabiliStaticheMostraImmagini.getInstance().setUltimaImmagineCaricata(s);
            VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(s.getIdCategoria());
            VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(s.getIdImmagine());

            DownloadImmagineMI d = new DownloadImmagineMI();
            d.EsegueChiamata(
                    context, s.getUrlImmagine(),
                    VariabiliStaticheMostraImmagini.getInstance().getImg(),
                    s.getUrlImmagine()
            );
            // new DownloadImageMI(context, s.getUrlImmagine(),
            //         VariabiliStaticheMostraImmagini.getInstance().getImg()).execute(s.getUrlImmagine());

            List<StrutturaImmaginiLibrary> lista = new ArrayList<>();
            for (int i = 0; i < ultima; i++) {
                lista.add(VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().get(i));
            }
            VariabiliStaticheMostraImmagini.getInstance().setImmaginiCaricate(lista);

            if (VariabiliStaticheMostraImmagini.getInstance().getImmaginiCaricate().isEmpty()) {
                VariabiliStaticheMostraImmagini.getInstance().setImmaginiCaricate(new ArrayList<>());
            }
        }
    }

    public StrutturaImmaginiLibrary prendeStruttura(JSONObject j) {
        if (j != null) {
            // {"idImmagine": 522,"idCategoria": 1,"Categoria": "21_Sextury","Alias": ";^$$$$-$$$ ;",
            // "Tag": "","Cartella": "0004","NomeFile": "21-Sextury Lesbiandy Fetish Colombia_0050.jpg",
            // "DimensioneFile": 308264,"DataCreazione": "10/08/2024 12:14:26","DataModifica": "04/02/2024 04:56:29",
            // "DimensioniImmagine": "1280x834",
            // "UrlImmagine": "http://looigi.no-ip.biz:1085/Materiale/newPLibrary/21_Sextury/0004/21-Sextury Lesbiandy Fetish Colombia_0050.jpg",
            // "PathImmagine": "*S**S*192.168.0.33*S*Public*S*Materiale*S*newPLibrary*S*21_Sextury*S*0004*S*21-Sextury Lesbiandy Fetish Colombia_0050.jpg",
            // "EsisteImmagine": "True","ImmaginiCategoria": 758}
            StrutturaImmaginiLibrary si = new StrutturaImmaginiLibrary();
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
}
