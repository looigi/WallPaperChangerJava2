package com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.Detector.UtilityDetector;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.DownloadImmagineMI;
import com.looigi.wallpaperchanger2.UtilitiesVarie.log.LogInterno;
import com.looigi.wallpaperchanger2.Wallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.UtilitiesVarie.VariabiliStaticheStart;

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

    /* public void Attesa(boolean Acceso) {
        if (VariabiliStaticheMostraImmagini.getInstance().getImgCaricamento() == null) {
            return;
        }

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
    } */

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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Immagini", Maschera,  Log);
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
                VariabiliStaticheMostraImmagini.getInstance().getIdImmagine(),
                VariabiliStaticheMostraImmagini.getInstance().getRandom(),
                "IMMAGINI"
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
                    s.getUrlImmagine(),
                    false,
                    false
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

                String urlImmagine = j.getString("UrlImmagine");
                urlImmagine = urlImmagine.replace("*V1*", "\"");
                urlImmagine = urlImmagine.replace("*V2*", "'");
                urlImmagine = urlImmagine.replace("*S*", "\\");
                urlImmagine = urlImmagine.replace("*LE*", "<");
                urlImmagine = urlImmagine.replace("*GR*", ">" );
                urlImmagine = urlImmagine.replace("*GA*", "{" );
                urlImmagine = urlImmagine.replace("*GC*", "}" );
                urlImmagine = urlImmagine.replace("*QA*", "[" );
                urlImmagine = urlImmagine.replace("*QC*", "]" );
                urlImmagine = urlImmagine.replace("*AN*", "&" );
                si.setUrlImmagine(urlImmagine);

                si.setPathImmagine(j.getString("PathImmagine"));
                si.setEsisteImmagine(j.getString("EsisteImmagine").equals("True"));
                si.setImmaginiCategoria(j.getInt("ImmaginiCategoria"));
                si.setImmaginiFiltrate(j.getInt("ImmaginiFiltrate"));
                si.setExif(j.getString("Exif"));
                si.setTestoJava(j.getString("Testo"));
                si.setTags(j.getString("Tags"));

                return si;
            } catch (JSONException e) {
                if (si != null) {
                    return si;
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public void ScriveInfoSotto(StrutturaImmaginiLibrary si) {
        if (si != null) {
            if (VariabiliStaticheMostraImmagini.getInstance().getTxtInfoSotto() != null) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int immaginiCategoria = si.getImmaginiCategoria();
                        int immaginiFiltrate = si.getImmaginiFiltrate();
                        // int idImmagine = si.getIdImmagine();
                        String Nome = si.getCategoria() + "\\" + si.getCartella() + "\\" + si.getNomeFile();

                        if (immaginiCategoria == immaginiFiltrate) {
                            VariabiliStaticheMostraImmagini.getInstance().getTxtInfoSotto().setText(
                                    "Immagini " + immaginiCategoria + ". " + Nome);
                        } else {
                            VariabiliStaticheMostraImmagini.getInstance().getTxtInfoSotto().setText(
                                    "Immagine Filtrate " +
                                            immaginiFiltrate + "/" + immaginiCategoria + ". " + Nome
                            );
                        }
                    }
                }, 50);
            }
        } else {
            if (VariabiliStaticheMostraImmagini.getInstance().getTxtInfoSotto() != null) {
                VariabiliStaticheMostraImmagini.getInstance().getTxtInfoSotto().setText("");
            }
        }
    }

    public void AggiornaCategorie(Context context) {
        List<StrutturaImmaginiCategorie> l1 = new ArrayList<>();

        for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
            if (s.getCategoria().toUpperCase().trim().contains(
                    VariabiliStaticheMostraImmagini.getInstance().getFiltroCategoria().toUpperCase().trim())) {
                l1.add(s);
            }
        }

        String[] l = new String[l1.size()];
        int c = 0;
        for (StrutturaImmaginiCategorie s : l1) {
            l[c] = s.getCategoria();
            c++;
        }

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStaticheMostraImmagini.getInstance().getSpnCategorie(),
                l,
                VariabiliStaticheMostraImmagini.getInstance().getCategoriaAttuale()
        );

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, l);
        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                l
        );
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VariabiliStaticheMostraImmagini.getInstance().getSpnCategorie().setAdapter(adapter);

        if (!VariabiliStaticheMostraImmagini.getInstance().getCategoriaAttuale().isEmpty()) {
            int spinnerPosition = adapter.getPosition(VariabiliStaticheMostraImmagini.getInstance().getCategoriaAttuale());
            VariabiliStaticheMostraImmagini.getInstance().getSpnCategorie().setSelection(spinnerPosition);
        } */
    }

    public void AggiornaCategorieSpostamento(Context context) {
        List<StrutturaImmaginiCategorie> l1 = new ArrayList<>();

        for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
            if (s.getCategoria().toUpperCase().trim().contains(
                    VariabiliStaticheMostraImmagini.getInstance().getFiltroCategoriaSpostamento().toUpperCase().trim())) {
                l1.add(s);
            }
        }

        String[] l = new String[l1.size()];
        int c = 0;
        for (StrutturaImmaginiCategorie s : l1) {
            l[c] = s.getCategoria();
            c++;
        }

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                VariabiliStaticheMostraImmagini.getInstance().getSpnSpostaCategorie(),
                l,
                ""
        );

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, l);
        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                l
        );
        // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        VariabiliStaticheMostraImmagini.getInstance().getSpnSpostaCategorie().setAdapter(adapter); */
    }

    public void SalvataggioImmagine(Context context, boolean Sovrascrive) {
        String Path = context.getFilesDir() + "/Immagini/AppoggioMI.jpg";
        StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata();

        String encodedImage = UtilitiesGlobali.getInstance().convertBmpToBase64(Path);

        ChiamateWSMI c = new ChiamateWSMI(context);
        c.ModificaImmagine(s, encodedImage, Sovrascrive);
    }
}
