package com.looigi.wallpaperchanger2.classiAttivitaWallpaper;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.classiAttivitaDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classiAttivitaDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classiStandard.GestioneNotifiche;
import com.looigi.wallpaperchanger2.classiStandard.LogInterno;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

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

public class UtilityWallpaper {
    private static final String NomeMaschera = "UTILITY";
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

    public void ChiudeApplicazione(Context context) {
        VariabiliStaticheWallpaper.getInstance().setSbragaTutto(true);

        GestioneNotifiche.getInstance().RimuoviNotifica();

        GestioneNotificheDetector.getInstance().RimuoviNotifica();

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Stop Servizio");

        if (VariabiliStaticheWallpaper.getInstance().getServizioForeground() != null) {
            context.stopService(VariabiliStaticheWallpaper.getInstance().getServizioForeground());
            VariabiliStaticheWallpaper.getInstance().setServizioForeground(null);
        }

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Uscita\n\n");
        UtilityWallpaper.getInstance().ApreToast(context, "Uscita");

        finishAffinity(VariabiliStaticheWallpaper.getInstance().getMainActivity());

        System.exit(0);
    }

    /* public void generaPath(Context context) {
        String pathLog = context.getFilesDir() + "/Log";
        VariabiliStaticheStart.getInstance().setPercorsoDIRLog(pathLog);
        String nomeFileLog = VariabiliStaticheWallpaper.channelName + ".txt";
        VariabiliStaticheWallpaper.getInstance().setNomeFileDiLog(nomeFileLog);
    } */

    public void ApreToast(Context context, String messaggio) {
        if (VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            if (context != null && VariabiliStaticheWallpaper.getInstance().getMainActivity() != null) {
                VariabiliStaticheWallpaper.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context,
                                VariabiliStaticheWallpaper.channelName + ": " + messaggio,
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
        VariabiliStaticheWallpaper.getInstance().getMainActivity().runOnUiThread(new Runnable() {
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
            try {
                progressDialog = new ProgressDialog(VariabiliStaticheWallpaper.getInstance().getMainActivity());
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
            VariabiliStaticheWallpaper.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                public void run() {
                    AlertDialog alertDialog = new AlertDialog.Builder(VariabiliStaticheWallpaper.getInstance().getMainActivity()).create();
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
        ChangeWallpaper c = new ChangeWallpaper(context);
        if (!VariabiliStaticheWallpaper.getInstance().isOffline()) {
            boolean fatto = c.setWallpaper(context, null);
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata manualmente: " + fatto + "---");
        } else {
            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Cambio Immagine---");
            int numeroRandom = UtilityWallpaper.getInstance().GeneraNumeroRandom(
                    VariabiliStaticheWallpaper.getInstance().getListaImmagini().size() - 1);
            if (numeroRandom > -1) {
                boolean fatto = c.setWallpaper(context, VariabiliStaticheWallpaper.getInstance().getListaImmagini().get(numeroRandom));
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine cambiata: " + fatto + "---");
            } else {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera,"---Immagine NON cambiata: Caricamento immagini in corso---");
            }
        }
    }

    private int attese = 0;

    public void Attesa(boolean Come) {
        if (!VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            attese = 0;
            VariabiliStaticheWallpaper.getInstance().getMainActivity().runOnUiThread(new Runnable() {
                public void run() {
                    VariabiliStaticheWallpaper.getInstance().getLayAttesa().setVisibility(LinearLayout.GONE);
                }
            });
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

    public void EliminaLogs(Context context) {
        String path = context.getFilesDir() + "/Log/WallPaper";
        File directory = new File(path);
        File[] filesW = directory.listFiles();

        path = context.getFilesDir() + "/Log/Detector";
        directory = new File(path);
        File[] filesD = directory.listFiles();

        path = context.getFilesDir() + "/Log/GPS";
        directory = new File(path);
        File[] filesG = directory.listFiles();

        int quanti = filesW.length + filesD.length + filesG.length;

        for (File f : filesW) {
            f.delete();
        }

        for (File f : filesD) {
            f.delete();
        }

        for (File f : filesG) {
            f.delete();
        }

        ApreToast(context, "File di logs eliminati: " + quanti);
    }

    public void CondividiLogs(Context context) {
        String path1 = context.getFilesDir() + "/Log/WallPaper";
        // File directory = new File(path);
        // File[] filesW = directory.listFiles();

        String path2 = context.getFilesDir() + "/Log/Detector";
        // directory = new File(path);
        // File[] filesD = directory.listFiles();

        // int quanti = filesW.length + filesD.length;

        String path3 = context.getFilesDir() + "/DB";

        String path4 = context.getFilesDir() + "/Log/GPS";

        String pathDest = context.getFilesDir() + "/Appoggio";
        String destFile = pathDest + "/logs.zip";
        CreaCartelle(pathDest);
        if (EsisteFile(destFile)) {
            EliminaFileUnico(destFile);
        }

        int quanti = 0;

        String[] App = { "WallPaper", "Detector", "DB", "GPS" };
        String[] paths = { path1, path2, path3, path4 };
        try {
            quanti += zip(App , paths, destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        File f = new File(destFile);
        Uri uri = FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".provider",
                f);

        Intent i = new Intent(Intent.ACTION_SEND);
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT,"logs.zip");
        i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
        i.putExtra(Intent.EXTRA_STREAM,uri);
        i.setType(UtilityWallpaper.getInstance().GetMimeType(context, uri));
        context.startActivity(Intent.createChooser(i,"Share file di log e db"));

        ApreToast(context, "File di logs condivisi: " + quanti);
    }

    public int zip(String[] Applicazione, String[] Path, String zipFile) throws IOException {
        int BUFFER_SIZE = 1024;

        BufferedInputStream origin = null;
        ZipOutputStream out = new ZipOutputStream(
                new BufferedOutputStream(
                new FileOutputStream(zipFile)));

        int quanti = 0;

        try {
            byte data[] = new byte[BUFFER_SIZE];
            int q = 0;

            for (String p : Path) {
                File directory = new File(p);
                File[] files = directory.listFiles();

                for (File f : files) {
                    String nome = Applicazione[q] + "/" + f.getName();

                    FileInputStream fi = new FileInputStream(f);
                    origin = new BufferedInputStream(fi, BUFFER_SIZE);
                    try {
                        ZipEntry entry = new ZipEntry(nome);
                        out.putNextEntry(entry);
                        int count;
                        while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                            out.write(data, 0, count);
                        }
                    } finally {
                        origin.close();
                        quanti = files.length;
                    }
                }
                q++;
            }
        }
        finally {
            out.close();
        }

        return quanti;
    }
}
