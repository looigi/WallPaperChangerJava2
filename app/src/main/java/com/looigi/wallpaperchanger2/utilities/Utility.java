package com.looigi.wallpaperchanger2.utilities;

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
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.classiAttivitaDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classiStandard.GestioneNotifiche;
import com.looigi.wallpaperchanger2.classiStandard.LogInterno;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static androidx.core.app.ActivityCompat.finishAffinity;

public class Utility {
    private static final String NomeMaschera = "UTILITY";
    private ProgressDialog progressDialog;

    private static Utility instance = null;

    private Utility() {
    }

    public static Utility getInstance() {
        if (instance == null) {
            instance = new Utility();
        }

        return instance;
    }
    
    public void ScriveLog(Context context, String Maschera, String Log) {
        if (VariabiliStaticheServizio.getInstance().getPercorsoDIRLog().isEmpty() ||
                VariabiliStaticheServizio.getInstance().getNomeFileDiLog().isEmpty()) {
            generaPath(context);
        }

        if (context != null) {
            if (VariabiliStaticheServizio.getInstance().getLog() == null) {
                LogInterno l = new LogInterno(context, false);
                VariabiliStaticheServizio.getInstance().setLog(l);
            }

            if (!Utility.getInstance().EsisteFile(VariabiliStaticheServizio.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheServizio.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheServizio.getInstance().getLog().PulisceFileDiLog();
            }

            if (EsisteFile(VariabiliStaticheServizio.getInstance().getPercorsoDIRLog() + "/" +
                    VariabiliStaticheServizio.getInstance().getNomeFileDiLog())) {
                VariabiliStaticheServizio.getInstance().getLog().ScriveLog(Maschera + ": " + Log);
            }
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

    public String GetMimeType(Context context, Uri uri) {
        String mimeType = null;

        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            mimeType = context.getContentResolver().getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    public void zip(String[] files, String zipFile) throws IOException {
        int BUFFER_SIZE = 1024;

        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
        try {
            byte data[] = new byte[BUFFER_SIZE];

            for (int i = 0; i < files.length; i++) {
                if (EsisteFile(files[i])) {
                    FileInputStream fi = new FileInputStream(files[i]);
                    origin = new BufferedInputStream(fi, BUFFER_SIZE);
                    try {
                        ZipEntry entry = new ZipEntry(files[i].substring(files[i].lastIndexOf("/") + 1));
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                            out.write(data, 0, count);
                        }
                    } finally {
                        origin.close();
                    }
                }
            }
        }
        finally {
            out.close();
        }
    }


    public void ChiudeApplicazione(Context context) {
        VariabiliStaticheServizio.getInstance().setSbragaTutto(true);

        GestioneNotifiche.getInstance().RimuoviNotifica();

        GestioneNotificheDetector.getInstance().RimuoviNotifica();

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Stop Servizio");

        if (VariabiliStaticheServizio.getInstance().getServizioForeground() != null) {
            context.stopService(VariabiliStaticheServizio.getInstance().getServizioForeground());
            VariabiliStaticheServizio.getInstance().setServizioForeground(null);
        }

        Utility.getInstance().ScriveLog(context, NomeMaschera, "Uscita\n\n");
        Utility.getInstance().ApreToast(context, "Uscita");

        finishAffinity(VariabiliStaticheServizio.getInstance().getMainActivity());

        System.exit(0);
    }

    public void generaPath(Context context) {
        String pathLog = context.getFilesDir() + "/Log";
        VariabiliStaticheServizio.getInstance().setPercorsoDIRLog(pathLog);
        String nomeFileLog = VariabiliStaticheServizio.channelName + ".txt";
        VariabiliStaticheServizio.getInstance().setNomeFileDiLog(nomeFileLog);
    }

    public void ApreToast(Context context, String messaggio) {
        if (VariabiliStaticheServizio.getInstance().isScreenOn()) {
            if (context != null && VariabiliStaticheServizio.getInstance().getMainActivity() != null) {
                VariabiliStaticheServizio.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context,
                                VariabiliStaticheServizio.channelName + ": " + messaggio,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public boolean EliminaFileUnico(String fileName) {
        if (EsisteFile(fileName)) {
            File file = new File(fileName);
            return file.delete();
        } else {
            return false;
        }
    }

    public void VisualizzaMessaggio(String Messaggio) {
        VariabiliStaticheServizio.getInstance().getMainActivity().runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog alertDialog = new AlertDialog.Builder(VariabiliStaticheServizio.getInstance().getMainActivity()).create();
                alertDialog.setTitle("Messaggio " + VariabiliStaticheServizio.channelName);
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
            try {
                progressDialog = new ProgressDialog(VariabiliStaticheServizio.getInstance().getMainActivity());
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
        ScriveLog(context, NomeMaschera,"Vibrazione: " + VariabiliStaticheServizio.getInstance().isVibrazione());

        if (VariabiliStaticheServizio.getInstance().isVibrazione()) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    Vibrator v = (Vibrator) VariabiliStaticheServizio.getInstance().getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    v.vibrate(VibrationEffect.createOneShot(Quanto, VibrationEffect.DEFAULT_AMPLITUDE));

                    ScriveLog(context, NomeMaschera,"Vibrazione: " + Quanto);
                }
            }, 100);
        }
    }

    public void VisualizzaErrore(Context context, String Errore) {
        // VariabiliStaticheServizio.getInstance().getImgCaricamento().setVisibility(LinearLayout.GONE);
        ScriveLog(context, NomeMaschera, "Visualizzo messaggio di errore. Schermo acceso: " +
                VariabiliStaticheServizio.getInstance().isScreenOn());
        if (VariabiliStaticheServizio.getInstance().isScreenOn()) {
            VariabiliStaticheServizio.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(VariabiliStaticheServizio.getInstance().getMainActivity()).create();
                    alertDialog.setTitle("Messaggio " + VariabiliStaticheServizio.channelName);
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
        ChangeWallpaper c = new ChangeWallpaper(context);
        if (!VariabiliStaticheServizio.getInstance().isOffline()) {
            boolean fatto = c.setWallpaper(context, null);
            Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata manualmente: " + fatto + "---");
        } else {
            Utility.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine---");
            int numeroRandom = Utility.getInstance().GeneraNumeroRandom(
                    VariabiliStaticheServizio.getInstance().getListaImmagini().size() - 1);
            if (numeroRandom > -1) {
                boolean fatto = c.setWallpaper(context, VariabiliStaticheServizio.getInstance().getListaImmagini().get(numeroRandom));
                Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata: " + fatto + "---");
            } else {
                Utility.getInstance().ScriveLog(context, NomeMaschera,"---Immagine NON cambiata: Caricamento immagini in corso---");
            }
        }
    }

    private int attese = 0;

    public void Attesa(boolean Come) {
        if (!VariabiliStaticheServizio.getInstance().isScreenOn()) {
            attese = 0;
            VariabiliStaticheServizio.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                public void run() {
                    VariabiliStaticheServizio.getInstance().getLayAttesa().setVisibility(LinearLayout.GONE);
                }
            });
            return;
        }

        if (attese == 0) {
            if (VariabiliStaticheServizio.getInstance().getLayAttesa() != null) {
                VariabiliStaticheServizio.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        if (Come) {
                            attese++;
                            VariabiliStaticheServizio.getInstance().getLayAttesa().setVisibility(LinearLayout.VISIBLE);
                        } else {
                            VariabiliStaticheServizio.getInstance().getLayAttesa().setVisibility(LinearLayout.GONE);
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
                    VariabiliStaticheServizio.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            VariabiliStaticheServizio.getInstance().getLayAttesa().setVisibility(LinearLayout.GONE);
                        }
                    });
                }
            }
        }
    }
}
