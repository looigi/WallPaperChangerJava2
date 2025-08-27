package com.looigi.wallpaperchanger2.classeUtilityImmagini;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.LinearLayout;

import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.adapters.AdapterListenerUI;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.Objects;

public class UtilityUtilityImmagini {
    private static UtilityUtilityImmagini instance = null;

    private UtilityUtilityImmagini() {
    }

    public static UtilityUtilityImmagini getInstance() {
        if (instance == null) {
            instance = new UtilityUtilityImmagini();
        }

        return instance;
    }

    public void Prosegue(Context context) {
        StrutturaControlloImmagini s = VariabiliStaticheUtilityImmagini.getInstance().getStrutturaAttuale();

        VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");

        boolean ok = true;
        int i = 0;
        for (StrutturaControlloImmagini s2 : VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini()) {
            if (Objects.equals(s2.getCategoria(), s.getCategoria())) {
                ok = false;
                break;
            }
            i++;
        }
        if (ok) {
            VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini().add(s);
        } else {
            VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini().set(i, s);
        }
        VariabiliStaticheUtilityImmagini.getInstance().getAdapter().notifyDataSetChanged();

        db_dati_ui db = new db_dati_ui(context);
        db.ScriveDati(s);
        db.ChiudeDB();

        if (VariabiliStaticheUtilityImmagini.getInstance().isControllaTutto()) {
            Handler handlerTimer = new Handler(Looper.getMainLooper());
            Runnable rTimer = new Runnable() {
                public void run() {
                    if (VariabiliStaticheUtilityImmagini.getInstance().isBloccaElaborazione()) {
                        VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");
                        VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(false);
                        UtilitiesGlobali.getInstance().ApreToast(context, "Elaborazione bloccata");
                        return;
                    }
                    int quale = ControllaProssimoNumero(
                            VariabiliStaticheUtilityImmagini.getInstance().getQualeStaControllando()
                    );
                    if (quale > -1) {
                        VariabiliStaticheUtilityImmagini.getInstance().setQualeStaControllando(quale);

                        if (quale < VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().size()) {
                            int idCategoria2 = VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().get(quale).getIdCategoria();
                            String Categoria2 = VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().get(quale).getCategoria();
                            VariabiliStaticheUtilityImmagini.getInstance().setCategoriaAttuale(Categoria2);
                            VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("Elaborazione " + Categoria2 + " " +
                                    (quale + 1) + "/" + VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().size());

                            if (VariabiliStaticheUtilityImmagini.getInstance().isEsegueAncheRefresh()) {
                                ChiamateWSUI ws = new ChiamateWSUI(context);
                                ws.RefreshImmagini(String.valueOf(idCategoria2), false);
                            } else {
                                ChiamateWSUI ws = new ChiamateWSUI(context);
                                ws.ControlloImmagini(String.valueOf(idCategoria2), "N");
                            }
                        } else {
                            VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");
                            VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(false);
                            UtilitiesGlobali.getInstance().ApreToast(context, "Elaborazione effettuata");

                            VariabiliStaticheUtilityImmagini.getInstance().setAdapter(new AdapterListenerUI(context,
                                    VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM()));
                            VariabiliStaticheUtilityImmagini.getInstance().getLstImmagini().setAdapter(VariabiliStaticheUtilityImmagini.getInstance().getAdapter());
                        }
                    } else {
                        VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");
                        VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(false);
                        UtilitiesGlobali.getInstance().ApreToast(context, "Elaborazione effettuata");

                        VariabiliStaticheUtilityImmagini.getInstance().setAdapter(new AdapterListenerUI(context,
                                VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM()));
                        VariabiliStaticheUtilityImmagini.getInstance().getLstImmagini().setAdapter(VariabiliStaticheUtilityImmagini.getInstance().getAdapter());
                    }
                }
            };
            handlerTimer.postDelayed(rTimer, 500);
        } else {
            VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("");
        }
    }

    public int ControllaProssimoNumero(int quale) {
        int quale2 = quale;
        boolean ancora = true;

        while (ancora && quale2 < VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().size()) {
            StrutturaImmaginiCategorie c = VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieIMM().get(quale2);
            boolean ok = true;

            if (!VariabiliStaticheUtilityImmagini.getInstance().getListaCategorieDiRicerca().contains(c.getIdCategoria())) {
                for (StrutturaControlloImmagini i : VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini()) {
                    if (c.getIdCategoria() == i.getIdCategoria()) {
                        ok = false;
                        break;
                    }
                }
            } else {
                ok = false;
            }
            if (ok) {
                ancora = false;
            } else {
                quale2++;
            }
        }

        return quale2;
    }

    public void showDownloadDialog(Context context, String imageUrl) {
        UtilitiesGlobali.getInstance().ApreToast(context, "Download iniziato");

        downloadImage(context, imageUrl);

        /* new android.app.AlertDialog.Builder(context)
                .setTitle("Scarica immagine")
                .setMessage("Vuoi scaricare questa immagine?")
                .setPositiveButton("Sì", (dialog, which) -> downloadImage(context, imageUrl))
                .setNegativeButton("No", null)
                .show(); */
    }

    public void esegueRicercaWV(Context context, String Filtro) {
        VariabiliStaticheUtilityImmagini.getInstance().getWvRicerca().getSettings().setJavaScriptEnabled(true);
        // String url = "https://www.google.com/search?tbm=isch&q=" + Uri.encode(query) + "&safe=off";
        String url = "https://duckduckgo.com/?q=" + Uri.encode(Filtro) +
                "&iar=images&iax=images&ia=images&kp=-2";
        VariabiliStaticheUtilityImmagini.getInstance().getWvRicerca().loadUrl(url);
    }

    public void downloadImage(Context context, String imageUrl) {
        String fileName = "immagineSI_" + System.currentTimeMillis() + ".jpg";

        String Categoria = "DownloadSI"; // VariabiliStaticheMostraImmagini.getInstance().getCategoria().replace("\\", "§");

        File folder = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), Categoria);
        if (!folder.exists()) folder.mkdirs();
        File file = new File(folder, fileName);
        String cartellaDestinazione = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + Categoria;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationUri(Uri.fromFile(file));

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        VariabiliStaticheUtilityImmagini.getInstance().setDownloadId(manager.enqueue(request));

        Runnable r;
        HandlerThread handlerThread;
        Handler handler;
        final int[] giro = {0};

        handlerThread = new HandlerThread("background-thread_SI");
        handlerThread.start();

        handler = new Handler(handlerThread.getLooper());
        r = new Runnable() {
            public void run() {
                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(VariabiliStaticheUtilityImmagini.getInstance().getDownloadId());
                Cursor cursor = manager.query(query);
                if (cursor.moveToFirst()) {
                    int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                    if (status == DownloadManager.STATUS_SUCCESSFUL) {
                        VariabiliStaticheUtilityImmagini.getInstance().setDownloadId(-1);
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // download completato
                                if (VariabiliStatichePlayer.getInstance().getLayCaricamentoSI() != null) {
                                    VariabiliStatichePlayer.getInstance().getLayCaricamentoSI().setVisibility(LinearLayout.VISIBLE);
                                }
                                VariabiliScaricaImmagini.getInstance().setScaricaMultiplo(false);
                                String result = UtilitiesGlobali.getInstance().convertBmpToBase64(cartellaDestinazione + "/" + fileName);
                                if (result != null) {
                                    VariabiliStaticheUtilityImmagini.getInstance().setFileDaEliminare(cartellaDestinazione + "/" + fileName);

                                    ChiamateWSMI wsmi = new ChiamateWSMI(context);
                                    wsmi.UploadImmagine(fileName, result, null, imageUrl);

                                    UtilitiesGlobali.getInstance().ApreToast(context, "Download completato");
                                }
                            }
                        }, 100);
                    } else {
                        giro[0]++;
                        if (giro[0] > 10) {
                            VariabiliStaticheUtilityImmagini.getInstance().setDownloadId(-1);
                            UtilitiesGlobali.getInstance().ApreToast(context, "Errore sul timeout del download");
                        } else {
                            if (handler != null) {
                                handler.postDelayed(this, 1000);
                            }
                        }
                    }
                } else {
                    int a = 0;
                }
            }
        };
        handler.postDelayed(r, 1000);

        // Toast.makeText(this, "Download avviato", Toast.LENGTH_SHORT).show();
    }

}
