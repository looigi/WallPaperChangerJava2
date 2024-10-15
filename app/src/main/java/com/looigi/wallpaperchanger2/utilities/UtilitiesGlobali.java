package com.looigi.wallpaperchanger2.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.StrictMode;
import android.telephony.CellSignalStrength;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.classeLog.MainLog;
import com.looigi.wallpaperchanger2.classeLog.VariabiliStaticheLog;
import com.looigi.wallpaperchanger2.classeMostraImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeMostraVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.classeDetector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classePlayer.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classeStandard.LogInterno;
import com.looigi.wallpaperchanger2.classeWallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static androidx.core.app.ActivityCompat.finishAffinity;

public class UtilitiesGlobali {
    private static final String NomeMaschera = "UTILITIESGLOBALI";
    private static UtilitiesGlobali instance = null;
    private String[] App = new String[0];
    private String[] paths = new String[0];

    private UtilitiesGlobali() {
    }

    public static UtilitiesGlobali getInstance() {
        if (instance == null) {
            instance = new UtilitiesGlobali();
        }

        return instance;
    }

    private long ultimoTmsStatorete = -1;
    private int contaStatoRete = 0;
    private boolean reteAttiva = true;

    public boolean isRetePresente() {
        if (!reteAttiva) {
            return false;
        }

        int level = VariabiliStaticheStart.getInstance().getLivello();

        long ora = new Date().getTime();
        if (ora - ultimoTmsStatorete >= 1000) {
            // contaStatoRete++;
            // if (contaStatoRete > 5) {
            //     return false;
            // }
        }
        ultimoTmsStatorete = ora;

        if (level <= 2) {
            return false;
        } else {
            // contaStatoRete = 0;
            return true;
        }
    }

    public void setRetePresente(boolean Stato) {
        reteAttiva = Stato;
    }

    public void ChiudeApplicazione(Context context) {
        VariabiliStaticheWallpaper.getInstance().setSbragaTutto(true);

        GestioneNotificheWP.getInstance().RimuoviNotifica();
        GestioneNotifichePlayer.getInstance().RimuoviNotifica();
        GestioneNotificheTasti.getInstance().RimuoviNotifica();
        GestioneNotificheDetector.getInstance().RimuoviNotifica();

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Stop Servizio");

        if (VariabiliStaticheWallpaper.getInstance().getServizioForeground() != null) {
            context.stopService(VariabiliStaticheWallpaper.getInstance().getServizioForeground());
            VariabiliStaticheWallpaper.getInstance().setServizioForeground(null);
        }

        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Uscita\n\n");
        ApreToast(context, "Uscita");

        Activity act = tornaActivityValida();

        if (act != null) {
            finishAffinity(act);
        }

        System.exit(0);
    }

    public void EliminaLogs(Context context, String qualeLog) {
        creaPaths(context, qualeLog);

        int quanti = 0;
        for (String p : paths) {
            File directory = new File(p);
            File[] filesW = directory.listFiles();
            quanti += filesW.length;
            for (File f : filesW) {
                f.delete();
            }
        }

        UtilitiesGlobali.getInstance().ApreToast(context, "File di logs eliminati: " + quanti);
    }

    public void VisualizzaLogs(Context context, String qualeLog) {
        creaPaths(context, qualeLog);

        List<File> filetti = new ArrayList<>();
        for (String p : paths) {
            File directory = new File(p);
            File[] filesW = directory.listFiles();
            if (filesW != null) {
                for (File f : filesW) {
                    filetti.add(f);
                }
            }
        }
        VariabiliStaticheLog.getInstance().setListaFiles(filetti);

        Intent iP = new Intent(context, MainLog.class);
        iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(iP);
    }

    private void creaPaths(Context context, String qualeLog) {
        String path1 = context.getFilesDir() + "/Log/WallPaper";
        String path2 = context.getFilesDir() + "/Log/Detector";
        String path3 = context.getFilesDir() + "/DB";
        String path4 = context.getFilesDir() + "/Log/GPS";
        String path5 = context.getFilesDir() + "/Log/PLAYER";
        String path6 = context.getFilesDir() + "/Log/IMMAGINI";
        String path7 = context.getFilesDir() + "/Log/VIDEO";
        String path8 = context.getFilesDir() + "/Log/PENNETTA";

        if (qualeLog.isEmpty()) {
            App = new String[] {"WallPaper", "Detector", "DB", "GPS", "Player", "Immagini", "Video", "Pennetta"};
            paths = new String[] {path1, path2, path3, path4, path5, path6, path7};
        } else {
            switch (qualeLog) {
                case "WALLPAPER":
                    App = new String[] {"WallPaper"};
                    paths = new String[] {path1};
                    break;
                case "DETECTOR":
                    App = new String[] {"Detector"};
                    paths = new String[] {path2};
                    break;
                case "MAPPA":
                    App = new String[] {"GPS"};
                    paths = new String[] {path4};
                    break;
                case "PLAYER":
                    App = new String[] {"Player"};
                    paths = new String[] {path5};
                    break;
                case "IMMAGINI":
                    App = new String[] {"Immagini"};
                    paths = new String[] {path6};
                    break;
                case "VIDEO":
                    App = new String[] {"Video"};
                    paths = new String[] {path7};
                    break;
                case "PENNETTA":
                    App = new String[] {"Pennetta"};
                    paths = new String[] {path8};
                    break;
            }
        }
    }

    public void CondividiLogs(Context context, String qualeLog) {
        int quanti = 0;

        creaPaths(context, qualeLog);

        String pathDest = context.getFilesDir() + "/Appoggio";
        String destFile = pathDest + "/logs.zip";
        UtilityWallpaper.getInstance().CreaCartelle(pathDest);
        if (UtilityWallpaper.getInstance().EsisteFile(destFile)) {
            UtilityWallpaper.getInstance().EliminaFileUnico(destFile);
        }

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

        UtilitiesGlobali.getInstance().ApreToast(context, "File di logs condivisi: " + quanti);
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
                if (files != null) {
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
                }
                q++;
            }
        }
        finally {
            out.close();
        }

        return quanti;
    }

    public Activity tornaActivityValida() {
        Activity act = VariabiliStaticheStart.getInstance().getMainActivity();

        if (act == null) {
            act = VariabiliStaticheWallpaper.getInstance().getMainActivity();
        }
        if (act == null) {
            act = VariabiliStaticheDetector.getInstance().getMainActivity();
        }
        if (act == null) {
            act = VariabiliStatichePlayer.getInstance().getAct();
        }
        if (act == null) {
            act = VariabiliStaticheMostraImmagini.getInstance().getAct();
        }
        if (act == null) {
            act = VariabiliStaticheVideo.getInstance().getAct();
        }

        return act;
    }

    public Context tornaContextValido() {
        Context ctx = VariabiliStaticheWallpaper.getInstance().getContext();
        if (ctx == null) {
            ctx = VariabiliStaticheStart.getInstance().getContext();
        }
        if (ctx == null) {
            ctx = VariabiliStaticheDetector.getInstance().getContext();
        }
        if (ctx == null) {
            ctx = VariabiliStatichePlayer.getInstance().getContext();
        }
        if (ctx == null) {
            ctx = VariabiliStaticheMostraImmagini.getInstance().getCtx();
        }
        if (ctx == null) {
            ctx = VariabiliStaticheVideo.getInstance().getContext();
        }

        return ctx;
    }

    public void ScriveLog(Context context, String Applicazione,  String Maschera, String Log) {
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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog(Applicazione, Maschera,  Log);
            // }
        } else {

        }
    }

    public void ApreToast(Context context, String messaggio) {
        if (VariabiliStaticheWallpaper.getInstance().isScreenOn()) {
            Activity act = UtilitiesGlobali.getInstance().tornaActivityValida();
            if (context != null && act != null) {
                act.runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(context,
                                VariabiliStaticheWallpaper.channelName + ": " + messaggio,
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public String getLevelString(int level) {
        switch(level) {
            case CellSignalStrength.SIGNAL_STRENGTH_GOOD:
                return "GOOD";
            case CellSignalStrength.SIGNAL_STRENGTH_GREAT:
                return "GREAT";
            case CellSignalStrength.SIGNAL_STRENGTH_MODERATE:
                return "MODERATE";
            case CellSignalStrength.SIGNAL_STRENGTH_NONE_OR_UNKNOWN:
                return "UNKNOWN";
            case CellSignalStrength.SIGNAL_STRENGTH_POOR:
                return "POOR";
            default:
                throw new RuntimeException("Unsupported level " + level);
        }
    }

    public boolean checkWifiOnAndConnected() {
        Context context = UtilitiesGlobali.instance.tornaContextValido();
        if (context != null) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

                if (wifiInfo.getNetworkId() == -1) {
                    return false; // Not connected to an access point
                }

                return true; // Connected to an access point
            } else {
                return false; // Wi-Fi adapter is OFF
            }
        } else {
            return false; // Context nullo
        }
    }
}
