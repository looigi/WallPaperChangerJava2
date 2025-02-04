package com.looigi.wallpaperchanger2.classeWallpaper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.ChiamateWsWP;
import com.looigi.wallpaperchanger2.utilities.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

public class UtilityWallpaper {
    private static final String NomeMaschera = "Utility_Wallpaper";
    private ProgressDialog progressDialog;

    private static UtilityWallpaper instance = null;

    private UtilityWallpaper() {
    }

    public static UtilityWallpaper getInstance() {
        if (instance == null) {
            instance = new UtilityWallpaper();
        }

        return instance;
    }
    
    public void ScriveLog(Context context, String Maschera, String Log) {
        /* if (VariabiliStaticheStart.getInstance().getPercorsoDIRLog().isEmpty()) {
            generaPath(context);
        } */

        if (context != null) {
            if (VariabiliStaticheStart.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, false);
                VariabiliStaticheStart.getInstance().setLog(l);
            }

            /* if (!UtilityWallpaper.getInstance().EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheWallpaper.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheStart.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheStart.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheWallpaper.getInstance().getNomeFileDiLog())) { */
                VariabiliStaticheStart.getInstance().getLog().ScriveLog("WallPaper", Maschera,  Log);
            // }
        } else {

        }
    }

    public String PrendeErroreDaException(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));

        return TransformError(errors.toString());
    }

    private String TransformError(String error) {
        String Return = error;

        if (Return.length() > 250) {
            Return = Return.substring(0, 247) + "...";
        }
        Return = Return.replace("\n", " ");

        return Return;
    }

    public boolean EsisteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /* public void generaPath(Context context) {
        String pathLog = context.getFilesDir() + "/Log";
        VariabiliStaticheStart.getInstance().setPercorsoDIRLog(pathLog);
        String nomeFileLog = VariabiliStaticheWallpaper.channelName + ".txt";
        VariabiliStaticheWallpaper.getInstance().setNomeFileDiLog(nomeFileLog);
    } */

    public boolean EliminaFileUnico(String fileName) {
        if (EsisteFile(fileName)) {
            File file = new File(fileName);
            return file.delete();
        } else {
            return false;
        }
    }

    public void VisualizzaMessaggio(Activity context, String Messaggio) {
        context.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(VariabiliStaticheWallpaper.getInstance().getMainActivity()).create();
                alertDialog.setTitle("Messaggio " + VariabiliStaticheWallpaper.channelName);
                alertDialog.setMessage(Messaggio);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                // alertDialog.show();
            }
        });
    }

    public void ChiudeDialog() {
        try {
            progressDialog.dismiss();
        } catch (Exception ignored) {
        }
    }

    public void ApriDialog(boolean ApriDialog, String tOperazione) {
        if (!ApriDialog) {
            // OggettiAVideo.getInstance().getImgRest().setVisibility(LinearLayout.VISIBLE);
        } else {
            Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
            try {
                progressDialog = new ProgressDialog(act);
                progressDialog.setMessage("Attendere Prego...\n\n" + tOperazione);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
            } catch (Exception ignored) {

            }
        }
    }

    public void CreaCartelle(String Path) {
        String[] Pezzetti = Path.split("/");
        String DaCreare = "";

        for (int i = 0; i < Pezzetti.length; i++) {
            if (!Pezzetti[i].isEmpty()) {
                DaCreare += "/" + Pezzetti[i];
                File newFolder = new File(DaCreare);
                if (!newFolder.exists()) {
                    newFolder.mkdir();
                }
            }
        }
    }

    public void Vibra(Context context, long Quanto) {
        ScriveLog(context, NomeMaschera,"Vibrazione: " + VariabiliStaticheWallpaper.getInstance().isVibrazione());

        if (VariabiliStaticheWallpaper.getInstance().isVibrazione()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Vibrator v = (Vibrator) VariabiliStaticheWallpaper.getInstance().getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(VibrationEffect.createOneShot(Quanto, VibrationEffect.DEFAULT_AMPLITUDE));

                    ScriveLog(context, NomeMaschera,"Vibrazione: " + Quanto);
                }
            }, 100);
        }
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

    public int GeneraNumeroRandom(int NumeroMassimo) {
        if (NumeroMassimo > 0) {
            final int random = new Random().nextInt(NumeroMassimo);

            return random;
        } else {
            return -1;
        }
    }

    public void CambiaImmagine(Context context) {
        VariabiliStaticheWallpaper.getInstance().setImpostataConSchermoSpento(false);

        ChangeWallpaper c = new ChangeWallpaper(context, "WALLPAPER",
                VariabiliStaticheWallpaper.getInstance().getUltimaImmagine());
        switch (VariabiliStaticheWallpaper.getInstance().getModoRicercaImmagine()) {
            case 0:
                // Web
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine---");
                int numeroRandom = UtilityWallpaper.getInstance().GeneraNumeroRandom(
                        VariabiliStaticheWallpaper.getInstance().getListaImmagini().size() - 1);
                if (numeroRandom > -1) {
                    c.setWallpaper(context, VariabiliStaticheWallpaper.getInstance().getListaImmagini().get(numeroRandom));
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata");
                } else {
                    UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine NON cambiata: Caricamento immagini in corso---");
                }
                break;
            case 1:
                // Locale
                c.setWallpaper(context, null);
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata manualmente");
                break;
            case 2:
                // Immagini
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata da immagini");

                ChiamateWSMI ws = new ChiamateWSMI(context);
                ws.RitornaProssimaImmaginePerWP(
                        VariabiliStaticheWallpaper.getInstance().getFiltro()
                );
                break;
        }
    }

    private int attese = 0;

    public void Attesa(boolean Come) {
        if (!VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            attese = 0;
            Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
            if (act != null) {
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        VariabiliStaticheWallpaper.getInstance().getLayAttesa().setVisibility(LinearLayout.GONE);
                    }
                });
            }
            return;
        }

        if (attese == 0) {
            if (VariabiliStaticheWallpaper.getInstance().getLayAttesa() != null &&
                    VariabiliStaticheWallpaper.getInstance().getMainActivity() != null) {
                VariabiliStaticheWallpaper.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (Come) {
                            attese++;
                            VariabiliStaticheWallpaper.getInstance().getLayAttesa().setVisibility(LinearLayout.VISIBLE);
                        } else {
                            VariabiliStaticheWallpaper.getInstance().getLayAttesa().setVisibility(LinearLayout.GONE);
                        }
                    }
                });
            }
        } else {
            if (Come) {
                attese++;
            } else {
                attese--;
                if (attese <= 0) {
                    VariabiliStaticheWallpaper.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            VariabiliStaticheWallpaper.getInstance().getLayAttesa().setVisibility(LinearLayout.GONE);
                        }
                    });
                }
            }
        }
    }

    public void ApreRicerca(Context context) {
        switch (VariabiliStaticheWallpaper.getInstance().getModoRicercaImmagine()) {
            case 0:
                // Web
                VariabiliStaticheWallpaper.getInstance().setAdapterImmagini(null);

                ChiamateWsWP c = new ChiamateWsWP(context);
                c.TornaImmagini(false);
                break;
            case 1:
                // Locale
                AdapterListenerImmagini customAdapterT = new AdapterListenerImmagini(context,
                        VariabiliStaticheWallpaper.getInstance().getListaImmagini());
                VariabiliStaticheWallpaper.getInstance().getLstImmagini().setAdapter(customAdapterT);
                VariabiliStaticheWallpaper.getInstance().setAdapterImmagini(customAdapterT);

                VariabiliStaticheWallpaper.getInstance().getLayScelta().setVisibility(LinearLayout.VISIBLE);
                break;
            case 2:
                // Immagini
                break;
        }
    }

    public void SalvataggioImmagine(Context context, boolean Sovrascrive) {
        String Path = "";
        StrutturaImmagine s = VariabiliStaticheWallpaper.getInstance().getUltimaImmagine();

        switch (VariabiliStaticheWallpaper.getInstance().getImmagineImpostataDaChi()) {
            case "WALLPAPER":
            case "":
                Path = context.getFilesDir() + "/Download/Appoggio.jpg";
                break;
            case "PLAYER":
                Path = context.getFilesDir() + "/Download/AppoggioPLA.jpg";
                break;
            case "PENNETTA":
                Path = context.getFilesDir() + "/Download/AppoggioPEN.jpg";
                break;
            case "FETEKKIE":
                Path = context.getFilesDir() + "/Download/AppoggioFET.jpg";
                break;
            case "IMMAGINI":
                Path = context.getFilesDir() + "/Download/AppoggioMI.jpg";
                break;
        }

        String encodedImage = UtilitiesGlobali.getInstance().convertBmpToBase64(Path);

        switch (VariabiliStaticheWallpaper.getInstance().getImmagineImpostataDaChi()) {
            case "WALLPAPER":
            case "":
                ChiamateWsWP c = new ChiamateWsWP(context);
                c.ModificaImmagine(s, encodedImage);
                break;
            case "PLAYER":
                break;
            case "PENNETTA":
                break;
            case "FETEKKIE":
                break;
            case "IMMAGINI":
                break;
        }
    }
}
