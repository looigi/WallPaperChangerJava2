package com.looigi.wallpaperchanger2.UtilitiesVarie;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.telephony.CellSignalStrength;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.flexbox.FlexboxLayout;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.OCRPreprocessor;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.GoogleDrive.GoogleDrive;
import com.looigi.wallpaperchanger2.GoogleDrive.VariabiliStaticheGoogleDrive;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.GestioneNotificaGPS;
import com.looigi.wallpaperchanger2.Mappe.MappeEGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.UtilitiesVarie.LogInterno.MainLog;
import com.looigi.wallpaperchanger2.UtilitiesVarie.LogInterno.VariabiliStaticheLog;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.GestioneNotificheOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.RilevaOCRJava.VariabiliStaticheRilevaOCRJava;
import com.looigi.wallpaperchanger2.Video.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.Detector.GestioneNotificheDetector;
import com.looigi.wallpaperchanger2.Detector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.Player.GestioneNotifichePlayer;
import com.looigi.wallpaperchanger2.Player.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.Wallpaper.GestioneNotificheWP;
import com.looigi.wallpaperchanger2.Wallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.Wallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.log.LogInterno;
import com.looigi.wallpaperchanger2.Notifiche.notificaTasti.GestioneNotificheTasti;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static androidx.core.app.ActivityCompat.finishAffinity;

import javax.mail.MessagingException;

public class UtilitiesGlobali {
    private static final String NomeMaschera = "Utilities_Globali";
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

        if (level <= 2 && !VariabiliStaticheStart.getInstance().isCeWifi()) {
            if (VariabiliStatichePlayer.getInstance().getImgRetePresente() != null) {
                VariabiliStatichePlayer.getInstance().getImgRetePresente().setVisibility(LinearLayout.GONE);
            }
            VariabiliStaticheStart.getInstance().setSegnaleAttivo(false);
            return false;
        } else {
            // contaStatoRete = 0;
            if (VariabiliStatichePlayer.getInstance().getImgRetePresente() != null) {
                VariabiliStatichePlayer.getInstance().getImgRetePresente().setVisibility(LinearLayout.VISIBLE);
            }
            VariabiliStaticheStart.getInstance().setSegnaleAttivo(true);
            return true;
        }
    }

    /* public void ImpostaServizioGPS(Context context, String Azione) {
        if (VariabiliStaticheGPS.getInstance().getGestioneGPS() != null) {
            context.stopService(VariabiliStaticheGPS.getInstance().getGestioneGPS());
            VariabiliStaticheGPS.getInstance().setGestioneGPS(null);
        }

        Intent intentGPS = new Intent(context, GestioneGPS.class);
        VariabiliStaticheGPS.getInstance().setGestioneGPS(intentGPS);
        VariabiliStaticheGPS.getInstance().getGestioneGPS().setAction("");
        context.startForegroundService(VariabiliStaticheGPS.getInstance().getGestioneGPS());
    } */

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

    public void ImpostaServizioGPS(Context context, String Azione, String daDove) {
        switch(Azione) {
            case "CONTROLLO_ATTIVAZIONE":
                if (VariabiliStaticheGPS.getInstance().getGestioneGPS() != null) {
                    VariabiliStaticheGPS.getInstance().getGestioneGPS().ControlloAccSpegn(
                            "Imposta Servizio GPS da " + daDove
                    );
                }
                break;
        }
    }

    public void ChiudeApplicazione(Context context) {
        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Uscita\n\n");

        Activity act = VariabiliStaticheStart.getInstance().getMainActivity();

        ApreToast(context, "Uscita");

        VariabiliStaticheWallpaper.getInstance().setSbragaTutto(true);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Rimozione notifica WP");
                GestioneNotificheWP.getInstance().RimuoviNotifica();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Rimozione notifica Player");
                        GestioneNotifichePlayer.getInstance().RimuoviNotifica();

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Rimozione notifica Tasti");
                                GestioneNotificheTasti.getInstance().RimuoviNotifica();

                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Rimozione notifica Detector");
                                        GestioneNotificheDetector.getInstance().RimuoviNotifica();

                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Rimozione servizio GPS");
                                                VariabiliStaticheGPS.getInstance().getGestioneGPS().BloccaGPS("USCITA");
                                                context.stopService(VariabiliStaticheStart.getInstance().getIntentGPS());

                                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Rimozione notifica GPS");
                                                        GestioneNotificaGPS.getInstance().RimuoviNotifica();

                                                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Rimozione servizio antifurto");

                                                                /* if (VariabiliStaticheAntifurto.getInstance().isAllarmeAttivo()) {
                                                                    UtilityAntifurto.getInstance().FermaTimer();
                                                                    GestioneNotificheAntifurto.getInstance().RimuoviNotifica();

                                                                    // VariabiliStaticheStart.getInstance().getShakeDetector().stop();
                                                                    context.stopService(new Intent(context, ShakeService.class));

                                                                    VariabiliStaticheAntifurto.getInstance().setAllarmeInCorso(false);
                                                                    VariabiliStaticheAntifurto.getInstance().setAllarmeAttivo(false);

                                                                    if (VariabiliStaticheAntifurto.getInstance().getActAllarme() != null) {
                                                                        VariabiliStaticheAntifurto.getInstance().getActAllarme().finish();
                                                                    }
                                                                }

                                                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() { */

                                                                        if (VariabiliStaticheRilevaOCRJava.getInstance().isStaElaborando()) {
                                                                            UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Rimozione notifica OCR");
                                                                            VariabiliStaticheRilevaOCRJava.getInstance().setStaElaborando(false);
                                                                            GestioneNotificheOCR.getInstance().RimuoviNotifica();
                                                                        }

                                                                        UtilityWallpaper.getInstance().ScriveLog(context, NomeMaschera, "Stop Servizio");

                                                                        if (VariabiliStaticheStart.getInstance().getServizioForeground() != null) {
                                                                            context.stopService(VariabiliStaticheStart.getInstance().getServizioForeground());
                                                                            VariabiliStaticheStart.getInstance().setServizioForeground(null);
                                                                        }

                                                                        if (act != null) {
                                                                            finishAffinity(act);
                                                                        }

                                                                        System.exit(0);
                                                                    }
                                                                // }, 500);
                                                            // }
                                                        }, 500);
                                                    }
                                                }, 500);
                                            }
                                        }, 500);
                                    }
                                }, 500);
                            }
                        }, 500);
                    }
                }, 500);
            }
        }, 500);
    }

    public void EliminaLogs(Context context, String qualeLog) {
        creaPaths(context, qualeLog);

        int quanti = 0;
        for (String p : paths) {
            if (!p.contains("/DB")) {
                File directory = new File(p);
                File[] filesW = directory.listFiles();
                quanti += filesW.length;
                for (File f : filesW) {
                    f.delete();
                }
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
        String path4 = context.getFilesDir() + "/Log/Gps";
        String path5 = context.getFilesDir() + "/Log/Player";
        String path6 = context.getFilesDir() + "/Log/Immagini";
        String path7 = context.getFilesDir() + "/Log/Video";
        String path8 = context.getFilesDir() + "/Log/Pennetta";
        String path9 = context.getFilesDir() + "/Log/Films";
        String path10 = context.getFilesDir() + "/Log/Servizio";
        String path11 = context.getFilesDir() + "/Log/Fetekkie";

        if (qualeLog.isEmpty()) {
            App = new String[] {"WallPaper", "Detector", "DB", "GPS", "Player",
                    "Immagini", "Video", "Pennetta", "Films", "Servizio", "Fetekkie"};
            paths = new String[] {path1, path2, path3, path4, path5, path6, path7, path8, path9, path10, path11};
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
                case "FILMS":
                    App = new String[] {"Films"};
                    paths = new String[] {path9};
                    break;
                case "SERVIZIO":
                    App = new String[] {"Servizio"};
                    paths = new String[] {path10};
                    break;
                case "FETEKKIE":
                    App = new String[] {"Fetekkie"};
                    paths = new String[] {path10};
                    break;
            }
        }
    }

    public String TogliePercentualiDalNome(String Nome) {
        /* if (Nome.contains("%")) {
            String Nome2 = Nome;

            while (Nome2.contains("%")) {
                int pos = Nome2.indexOf("%");
                String chars = Nome2.substring(pos - 1, 2);

                Nome2 = Nome2.replace(chars, "_");
            }

            return Nome2;
        } else {
            return Nome;
        } */
        String Nome2 = Nome;
        Nome2 = Nome2.replace("%", "_");
        Nome2 = Nome2.replace("?", "_");
        Nome2 = Nome2.replace("*", "_");
        Nome2 = Nome2.replace("|", "_");
        Nome2 = Nome2.replace("<", "_");
        Nome2 = Nome2.replace(">", "_");
        Nome2 = Nome2.replace("+", "_");
        Nome2 = Nome2.replace("&", "_");
        Nome2 = Nome2.replace("/", "_");
        Nome2 = Nome2.replace("\\", "_");

        return Nome2;
    }

    public String RitornaOra() {
        Calendar calendar = Calendar.getInstance();
        String h = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        if (h.length() == 1) {
            h = "0" + h;
        }
        String m = Integer.toString(calendar.get(Calendar.MINUTE));
        if (m.length() == 1) {
            m = "0" + m;
        }
        String s = Integer.toString(calendar.get(Calendar.SECOND));
        if (s.length() == 1) {
            s = "0" + s;
        }

        return h + ":" + m + ":" + s;
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
        // i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
        i.putExtra(Intent.EXTRA_STREAM,uri);
        i.setType(GetMimeType(context, uri));
        context.startActivity(Intent.createChooser(i,"Share file di log e db"));

        UtilitiesGlobali.getInstance().ApreToast(context, "File di logs condivisi: " + quanti);
    }

    public String TornaNomeFileConData() {
        Calendar Oggi = Calendar.getInstance();
        int Giorno = Oggi.get(Calendar.DAY_OF_MONTH);
        int Mese = Oggi.get(Calendar.MONTH);
        int Anno = Oggi.get(Calendar.YEAR);
        String sGiorno = Integer.toString(Giorno).trim();
        String sMese = Integer.toString(Mese+1).trim();
        String sAnno = Integer.toString(Anno).trim();
        if (sGiorno.length() == 1) {
            sGiorno = "0" + sGiorno;
        }
        if (sMese.length() == 1) {
            sMese = "0" + sMese;
        }
        int Ora = Oggi.get(Calendar.HOUR_OF_DAY);
        String sOra = String.valueOf(Ora).trim();
        if (sOra.length() == 1) {
            sOra = "0" + sOra;
        }
        int Minuti = Oggi.get(Calendar.MINUTE);
        String sMinuti = String.valueOf(Minuti).trim();
        if (sMinuti.length() == 1) {
            sMinuti = "0" + sMinuti;
        }
        int Secondi = Oggi.get(Calendar.SECOND);
        String sSecondi = String.valueOf(Secondi).trim();
        if (sSecondi.length() == 1) {
            sSecondi = "0" + sSecondi;
        }

        return sAnno + sMese + sGiorno + "_" + sOra + sMinuti + sSecondi;
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
            if (context != null) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context,
                                messaggio,
                                Toast.LENGTH_SHORT).show();
                    }
                }, 10);
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
                return "Unsupported level " + level;
        }
    }

    public boolean checkWifiOnAndConnected() {
        Context context = UtilitiesGlobali.instance.tornaContextValido();
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            boolean hasInternet = nc != null && nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            VariabiliStaticheStart.getInstance().setCeWifi(hasInternet);

            return hasInternet;

            /* WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

                WifiInfo wifiInfo = wifiMgr.getConnectionInfo();

                if (wifiInfo.getNetworkId() == -1) {
                    return false; // Not connected to an access point
                }

                return true; // Connected to an access point
            } else {
                return false; // Wi-Fi adapter is OFF
            } */
        } else {
            return false; // Context nullo
        }
    }

    public String convertBmpToBase64(String filePath){
        Bitmap bmp = null;
        ByteArrayOutputStream bos = null;
        byte[] bt = null;
        String encodeString = null;

        try{
            bmp = BitmapFactory.decodeFile(filePath);
            bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bt = bos.toByteArray();
            encodeString = Base64.encodeToString(bt, Base64.DEFAULT);
        }
        catch (Exception ignored){
            // e.printStackTrace();
        }

        return encodeString;
    }

    public String MetteMaiuscole(String Nome) {
        String Nominativo = Nome;
        if (!Nominativo.isEmpty()) {
            Nominativo = Nominativo.toLowerCase();
            String[] n = Nominativo.split(" ");
            Nominativo = "";
            for (String nn : n) {
                String PrimaLettera = nn.substring(0, 1);
                nn = nn.substring(1);
                nn = PrimaLettera.toUpperCase() + nn;
                Nominativo += nn + " ";
            }
        }

        return Nominativo;
    }

    public String MetteMaiuscoleDopoOgniPunto(String Nome) {
        String Nominativo = "";
        if (!Nome.isEmpty()) {
            String Carattere = Character.toString(Nome.charAt(0)).toUpperCase();
            Nominativo += Carattere;
            for (int i = 1; i < Nome.length(); i++) {
                Carattere = Character.toString(Nome.charAt(i)).toLowerCase();
                Nominativo += Carattere;
                if (Carattere.equals(".") || Carattere.equals("?") || Carattere.equals(";") || Carattere.equals(":")) {
                    i++;
                    if (i < Nome.length()) {
                        String Carattere2 = Character.toString(Nome.charAt(i)).toLowerCase();
                        Nominativo += Carattere2;
                        boolean ok = true;
                        while (Carattere2.equals(" ")) {
                            i++;
                            if (i >= Nome.length()) {
                                ok = false;
                                break;
                            }
                            Carattere2 = Character.toString(Nome.charAt(i)).toLowerCase();
                            if (Carattere2.equals(" ")) {
                                Nominativo += Carattere2;
                            }
                        }
                        if (ok) {
                            Carattere2 = Carattere2.toUpperCase();
                            Nominativo += Carattere2;
                        }
                    }
                }
            }
        }

        return Nominativo;
    }

    public void InvioMail(Context context, String Mail, String Oggetto, String Corpo) {
        new Thread(() -> {
            try {
                sendMail sender = new sendMail();
                sender.sendMail(Mail, Oggetto, Corpo);
            } catch (MessagingException e) {
                ApreToast(context, "Mail non inviata");
            }
        }).start();
    }

    public void VisualizzaMessaggio(Context context, String Titolo, String Messaggio) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(Titolo);
        builder.setMessage(Messaggio);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void ControllaNuovaVersione(Context ctx) {
        VariabiliStaticheGoogleDrive.getInstance().setOperazioneDaEffettuare("RilevaVersione");
        Intent apre = new Intent(ctx, GoogleDrive.class);
        apre.addCategory(Intent.CATEGORY_LAUNCHER);
        apre.setAction(Intent.ACTION_MAIN );
        apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
        // apre.putExtra("DO", "RilevaVersione");
        ctx.startActivity(apre);
    }

    public void ControllaNuovaVersione2(Context context) {
        String fileLocale = context.getFilesDir() + "/UltimaVersione.txt";
        String fileScaricato = context.getFilesDir() + "/GoogleDrive/UltimaVersione.txt" ;

        String VersioneLocale = "";
        String VersioneScaricata = "";

        if (Files.getInstance().EsisteFile(fileLocale)) {
            VersioneLocale = Files.getInstance().LeggeFileUnico(fileLocale).replace("\n", "");
        }
        if (Files.getInstance().EsisteFile(fileScaricato)) {
            VersioneScaricata = Files.getInstance().LeggeFileUnico(fileScaricato).replace("\n", "");
        }

        if (VersioneLocale.isEmpty() && !VersioneScaricata.isEmpty()) {
            Files.getInstance().ScriveFile(String.valueOf(context.getFilesDir()), "UltimaVersione.txt", VersioneScaricata);
        }

        VariabiliStaticheGoogleDrive.getInstance().setVersioneScaricata("");
        if (!VersioneLocale.isEmpty() && !VersioneScaricata.isEmpty() && !VersioneLocale.equals(VersioneScaricata)) {
            String finalVersioneScaricata = VersioneScaricata;
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    VariabiliStaticheGoogleDrive.getInstance().setOperazioneDaEffettuare("AggiornaVersione");
                    Intent apre = new Intent(context, GoogleDrive.class);
                    apre.addCategory(Intent.CATEGORY_LAUNCHER);
                    apre.setAction(Intent.ACTION_MAIN );
                    apre.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent. FLAG_ACTIVITY_SINGLE_TOP ) ;
                    context.startActivity(apre);

                    VariabiliStaticheGoogleDrive.getInstance().setVersioneScaricata(finalVersioneScaricata);
                    UtilitiesGlobali.getInstance().ApreToast(context, "Installo versione aggiornata: " + finalVersioneScaricata);
                }
            }, 100);

            Files.getInstance().ScriveFile(String.valueOf(context.getFilesDir()), "UltimaVersione.txt", VersioneScaricata);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Nessuna nuova versione aggiornata");
        }
    }

    public ArrayAdapter<String> CreaAdapterSpinner(Context context, String[] Dati) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_spinner_item,
                Dati) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Questo è il valore selezionato mostrato nello Spinner (non il dropdown)
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(Color.WHITE);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLUE); // Colore personalizzato
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // Questo è il layout degli elementi nel menu a discesa
                View view = super.getDropDownView(position, convertView, parent);
                view.setBackgroundColor(Color.WHITE);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLUE); // Colore per la lista a discesa
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

    public ArrayAdapter<String> ImpostaSpinner(Context context, Spinner spinner, String[] lista, String stringaDefault) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                context,
                R.layout.spinner_item_selected,
                lista
        ) {

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // Inflating custom layout for dropdown
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(R.id.spinnerTextView);
                textView.setText(getItem(position));
                textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                textView.setBackgroundColor(Color.WHITE);
                return view;
            }
        };

        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown); // Layout per il menu a discesa
        if (spinner != null) {
            spinner.setAdapter(adapter);

            if (stringaDefault != null && !stringaDefault.isEmpty()) {
                int spinnerPosition = adapter.getPosition(stringaDefault);
                spinner.setSelection(spinnerPosition);
            }
        }

        return adapter;
    }

    public void AttesaGif(Context context, ImageView imageView, Boolean visualizza) {
        if (imageView == null || context == null) return;

        // Controllo lifecycle Activity
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) return;
        }

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                if (activity.isDestroyed() || activity.isFinishing()) return;
            }

            Glide.with(context)
                    .asGif()
                    .load(R.drawable.loading) // drawable o URL
                    .into(new CustomTarget<GifDrawable>() {
                        @Override
                        public void onResourceReady(@NonNull GifDrawable resource, @Nullable Transition<? super GifDrawable> transition) {
                            if (visualizza) {
                                imageView.setImageDrawable(resource);
                                resource.start();
                                imageView.setVisibility(View.VISIBLE);
                            } else {
                                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                    resource.stop();
                                    imageView.setVisibility(View.GONE);
                                }, 1000);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            imageView.setImageDrawable(null);
                        }
                    });
        }, 10);
    }

    public void RiconoscimentoTesti(Context context, StrutturaImmaginiLibrary struttura, String Url,
                                    Bitmap immagine, List<StrutturaImmaginiCategorie> listaCategorie,
                                    FlexboxLayout layCategorieRilevate, FlexboxLayout layScritteRilevate,
                                    LinearLayout layTasti) {
        // Lettura e aggiornamento testojava e tags per singola immagine
        UtilitiesLetturaInfoImmagine u = new UtilitiesLetturaInfoImmagine(context);
        u.ImpostaListaCategorie(listaCategorie);
        u.ImpostaLayCategorie(layCategorieRilevate);
        u.ImpostaLayScritte(layScritteRilevate);
        u.ImpostaLayTasti(layTasti);

        if (struttura != null) {
            if (struttura.getTestoJava() == null) {
                struttura.setTestoJava("");
            }
            if (struttura.getTags() == null) {
                struttura.setTags("");
            }
            if (struttura.getLuoghi() == null) {
                struttura.setLuoghi("");
            }
            if (struttura.getOggetti() == null) {
                struttura.setOggetti("");
            }
            if (struttura.getVolti() == null) {
                struttura.setVolti("");
            }

            String TestoJava = struttura.getTestoJava();
            if (TestoJava == null) { TestoJava = ""; }
            if (TestoJava.isEmpty()) {
                u.setImmagine(struttura);
                u.ImpostaCategorieGiaMesse(
                        TestoJava.trim()
                );
                u.setLuoghiImpostati(
                        struttura.getLuoghi()
                );
                u.setOggettiImpostati(
                        struttura.getOggetti()
                );
                u.setVoltiImpostati(
                        struttura.getVolti()
                );
                u.setUrl(Url);

                OCRPreprocessor ocrpp = new OCRPreprocessor();
                Bitmap preprocessedBitmap = ocrpp.preprocess(immagine);

                u.setBitmapModificata(preprocessedBitmap);
                u.setBitmapOriginale(immagine);

                u.AvviaControllo();
            } else {
                String Tags = RitornaTestoDescrizioniSistemato("Tags:", struttura.getTags());
                String Luoghi = RitornaTestoDescrizioniSistemato("Luoghi:", struttura.getLuoghi());
                String Oggetti = RitornaTestoDescrizioniSistemato("Oggetti:", struttura.getOggetti());
                String Volti = RitornaTestoDescrizioniSistemato("Volti:", struttura.getVolti());
                String TestoJ = RitornaTestoDescrizioniSistemato("Testo:", TestoJava);
                String Descr = RitornaTestoDescrizioniSistemato("Descr.:", struttura.getDescrizione());
                String Testo = TestoJ +
                        Tags +
                        Luoghi +
                        Oggetti +
                        Volti +
                        Descr;
                u.ScriveValori(Testo);
            }
        } else {
            u.ScriveValori("");
        }
    }

    public String capitalizeAfterSpaces(String input) {
        String[] words = input.split(" ");
        StringBuilder result = new StringBuilder();

        for (String word : words) {
            if (word.length() > 0) {
                result.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return result.toString().trim();
    }

    public String formatCamelCase(String input) {
        String step0 = input.replace("*PV*", " ").replace(";", " ");

        // 1️⃣ Inserisce uno spazio prima di una maiuscola seguita da una minuscola
        String step1 = step0.replaceAll("(?<!^)(?=[A-Z][a-z])", " ");

        // 2️⃣ Gestisce le sequenze di maiuscole seguite da minuscole (es: "HTTPServer" → "HTTP Server")
        String step2 = step1.replaceAll("([A-Z]+)([A-Z][a-z])", "$1 $2");

        // 3️⃣ Mette solo la prima lettera maiuscola, il resto minuscolo, in ogni parola
        StringBuilder result = new StringBuilder();
        for (String word : step2.split(" ")) {
            if (!word.isEmpty()) {
                if (word.length() > 1 && word.equals(word.toUpperCase())) {
                    // parola tutta maiuscola → lascia com'è (es. "HTTP")
                    result.append(word);
                } else {
                    // parola normale → prima maiuscola, resto minuscolo
                    result.append(word.substring(0, 1).toUpperCase())
                            .append(word.substring(1).toLowerCase());
                }
                result.append(" ");
            }
        }

        return result.toString().trim();
    }

    public SpannableString EvidenziaTesto(String text, String DaEvidenziare) {
        String testoDaEvidenziare1 = "";
        String testoDaEvidenziare2 = "";
        if (DaEvidenziare.contains(";")) {
            String[] testo = DaEvidenziare.split(";", -1);
            testoDaEvidenziare1 = testo[0];
            testoDaEvidenziare2 = testo[1];
        } else {
            testoDaEvidenziare1 = DaEvidenziare;
        }

        // Evita errori se il testo da evidenziare è nullo o vuoto
        if (testoDaEvidenziare1 == null || testoDaEvidenziare1.isEmpty()) {
            return new SpannableString(text);
        }

        SpannableString spannable = new SpannableString(text);

        // Usa indexOf in un ciclo per trovare tutte le occorrenze
        int start = text.toLowerCase().indexOf(testoDaEvidenziare1.toLowerCase());
        while (start >= 0) {
            int end = start + testoDaEvidenziare1.length();

            // Applica l'evidenziazione
            spannable.setSpan(
                    new BackgroundColorSpan(Color.YELLOW),
                    start,
                    end,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            );

            // Cerca la prossima occorrenza
            start = text.toLowerCase().indexOf(testoDaEvidenziare1.toLowerCase(), end);
        }

        return spannable;
    }

    public String RitornaTestoDescrizioniSistemato(String Cosa, String Testo) {
        if (Testo == null) {
            return "";
        }

        String step2 = "";
        String step = Testo.replace("*PV*", ";");
        step = step.replace(";;", ";");
        if (step.isEmpty() || step.equals(";")) {
            step2 = "";
        } else {
            step = step.replace(";", " ");
            if (step.contains("(")) {
                step = step.substring(0, step.indexOf("("));
            }
            step = formatCamelCase(step);
            step2 = Cosa.toUpperCase().trim() + " " + capitalizeAfterSpaces(step) + "\n";
        }

        return step2;
    }
}
