package com.looigi.wallpaperchanger2.classeModificheCodice.webService;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.telecom.QueryLocationException;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.MainStart;
import com.looigi.wallpaperchanger2.classeBackup.UtilityBackup;
import com.looigi.wallpaperchanger2.classeGps.AdapterListenerFilesRemoti;
import com.looigi.wallpaperchanger2.classeGps.CalcoloVelocita;
import com.looigi.wallpaperchanger2.classeGps.GestioneMappa;
import com.looigi.wallpaperchanger2.classeGps.UtilityGPS;
import com.looigi.wallpaperchanger2.classeGps.VariabiliStaticheGPS;
import com.looigi.wallpaperchanger2.classeGps.db_dati_gps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaGps;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaNomeFileRemoti;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeModificheCodice.VariabiliStaticheModificheCodice;
import com.looigi.wallpaperchanger2.classeModificheCodice.db_dati_modifiche_codice;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classeWallpaper.AdapterListenerImmagini;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.notificaTasti.GestioneNotificheTasti;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ChiamateWSModifiche implements TaskDelegateModifiche {
    private static final String NomeMaschera = "Chiamate_WS_MODIFICHE";
    // private LetturaWSAsincrona bckAsyncTask;

    private final String RadiceWS = VariabiliStaticheModificheCodice.UrlWS;
    private final String ws = "wsModifiche.asmx/";
    private final String NS="http://looModifiche.it/";
    private final String SA="http://looModifiche.it/";
    private String TipoOperazione = "";
    private final Context context;
    private final boolean ApriDialog = false;
    private String TipoFile;

    public ChiamateWSModifiche(Context context) {
        this.context = context;
    }

    private boolean unzip(File zipFile, File targetDirectory) throws IOException {
        boolean Ritorno = true;

        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } catch(IOException ignored) {
                    Ritorno = false;
                } finally {
                    fout.close();
                }
            }
        } catch(IOException ignored) {
            Ritorno = false;
        } finally {
            zis.close();
        }

        return Ritorno;
    }

    private String ConvertFileToBase64(String uri) {
        String base64File = "";
        File file = new File(uri);
        try (FileInputStream imageInFile = new FileInputStream(file)) {
            // Reading a file from file system
            byte fileData[] = new byte[(int) file.length()];
            imageInFile.read(fileData);
            base64File = Base64.getEncoder().encodeToString(fileData);
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException ioe) {
            return "";
        }
        return base64File;
    }

    public void RitornaFilesRemoti() {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="RitornaFilesGPS";

        TipoOperazione = "RitornaFilesGPS";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void EliminaFileRemoto(String QualeFile, String NomeFile) {
        UtilityGPS.getInstance().ImpostaAttesa(true);

        String Urletto="EliminaFile?" +
                "&TipoFile=" + QualeFile +
                "&NomeFile=" + NomeFile;

        TipoOperazione = "EliminaFileRemoto";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                30000,
                ApriDialog);
    }

    public void Esporta(String QualeFile, String NomeFile) {
        this.TipoFile = QualeFile;
        String PathModifiche = "";
        String b64 = "";
        String sNomeFile = "";

        switch (QualeFile) {
            case "MODIFICHE":
                VariabiliStaticheModificheCodice.getInstance().Attende(true);
                PathModifiche = context.getFilesDir() + "/DB/dati_modifiche.db";
                b64 = ConvertFileToBase64(PathModifiche);
                break;
            case "BACKUP":
                UtilityBackup.getInstance().Attende(true);
                PathModifiche = NomeFile;
                b64 = ConvertFileToBase64(PathModifiche);
                break;
            case "GPS":
                UtilityGPS.getInstance().ImpostaAttesa(false);
                PathModifiche = NomeFile;
                String[] n = NomeFile.split("/");
                sNomeFile = n[n.length - 1];
                b64 = ConvertFileToBase64(PathModifiche);
                break;
        }

        String Urletto="Esporta?" +
                "Filetto=" + b64 +
                "&TipoFile=" + QualeFile +
                "&sNomeFile=" + sNomeFile;

        TipoOperazione = "Esporta";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                120000,
                ApriDialog);
    }

    public void Importa(String TipoFile, String NomeFile) {
        this.TipoFile = TipoFile;

        switch (TipoFile) {
            case "MODIFICHE":
                VariabiliStaticheModificheCodice.getInstance().Attende(true);
                break;
            case "BACKUP":
                UtilityBackup.getInstance().Attende(true);
                break;
            case "GPS":
                UtilityGPS.getInstance().ImpostaAttesa(true);
                break;
        }

        String Urletto="Importa?" +
                "TipoFile=" + TipoFile +
                "&sNomeFile=" + NomeFile;

        TipoOperazione = "Importa";

        Esegue(
                RadiceWS + ws + Urletto,
                TipoOperazione,
                NS,
                SA,
                20000,
                ApriDialog);
    }

    public void Esegue(String Urletto, String tOperazione,
                       String NS, String SOAP_ACTION, int Timeout,
                       boolean ApriDialog) {
        // UtilityLazio.getInstance().ImpostaAttesa(true);

        long tsLong = System.currentTimeMillis()/1000;
        String TimeStampAttuale = Long.toString(tsLong);

        InterrogazioneWSModifiche i = new InterrogazioneWSModifiche();
        i.EsegueChiamata(
                context,
                NS,
                Timeout,
                SOAP_ACTION,
                tOperazione,
                ApriDialog,
                Urletto,
                TimeStampAttuale,
                this
        );
    }

    @Override
    public void TaskCompletionResult(String result) {
        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                // UtilityLazio.getInstance().ImpostaAttesa(false);

                switch (TipoOperazione) {
                    case "Esporta":
                        fEsporta(result);
                        break;
                    case "Importa":
                        fImporta(result);
                        break;
                    case "RitornaFilesGPS":
                        fRitornaFilesGPS(result);
                        break;
                    case "EliminaFileRemoto":
                        fEliminaFileRemoto(result);
                        break;
                }
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    public void StoppaEsecuzione() {
        // bckAsyncTask.cancel(true);
    }

    private boolean ControllaRitorno(String Operazione, String result) {
        if (result.contains("ERROR:") || result.toUpperCase().contains("ANYTYPE")) {
            if (result.contains("JAVA.NET.UNKNOWNHOSTEXCEPTION") || result.contains("SOCKETTIMEOUTEXCEPTION")) {
            }

            return false;
        } else {
            return true;
        }
    }

    private void fEliminaFileRemoto(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Elimina file GPS", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        Handler handlerTimer = new Handler(Looper.getMainLooper());
        Runnable rTimer = new Runnable() {
            public void run() {
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.RitornaFilesRemoti();
            }
        };
        handlerTimer.postDelayed(rTimer, 100);
    }

    private void fRitornaFilesGPS(String result) {
        UtilityGPS.getInstance().ImpostaAttesa(false);

        boolean ritorno = ControllaRitorno("Ritorna files GPS", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        List<StrutturaNomeFileRemoti> files = new ArrayList<>();
        String[] filesGPS = result.split(";");
        for (String f : filesGPS) {
            String[] ff = f.split("/");
            String Nome = ff[ff.length - 1];

            StrutturaNomeFileRemoti s = new StrutturaNomeFileRemoti();
            s.setPath(f);
            s.setNomeFile(Nome);

            files.add(s);
        }

        files.sort((t1, t2) -> t2.getNomeFile().compareTo(t1.getNomeFile()));

        AdapterListenerFilesRemoti customAdapterT = new AdapterListenerFilesRemoti(context,
                files);
        VariabiliStaticheGPS.getInstance().getLstFilesRemoti().setAdapter(customAdapterT);
    }

    private void fImporta(String result) {
        boolean ritorno = ControllaRitorno("Ritorno importa", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
        } else {
            byte[] decodedBytes;

            switch (this.TipoFile) {
                case "MODIFICHE":
                    String PathModifiche = context.getFilesDir() + "/DB/dati_modifiche2.db";
                    String PathModificheFinali = context.getFilesDir() + "/DB/dati_modifiche.db";
                    decodedBytes = Base64.getDecoder().decode(result);
                    try (FileOutputStream fos = new FileOutputStream(PathModifiche)) {
                        fos.write(decodedBytes);
                        fos.close();

                        Files.getInstance().EliminaFileUnico(PathModificheFinali);
                        Files.getInstance().CopiaFile(PathModifiche, PathModificheFinali);
                        Files.getInstance().EliminaFileUnico(PathModifiche);

                        UtilitiesGlobali.getInstance().ApreToast(context, "DB Importato correttamente");

                        Intent intent = VariabiliStaticheModificheCodice.getInstance().getAct().getIntent();
                        VariabiliStaticheModificheCodice.getInstance().getAct().finish();
                        VariabiliStaticheModificheCodice.getInstance().getAct().startActivity(intent);
                    } catch (IOException e) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "ERROR: " + e.getMessage());
                    }

                    VariabiliStaticheModificheCodice.getInstance().Attende(false);
                    break;
                case "BACKUP":
                    Files.getInstance().CreaCartelle(context.getFilesDir() + "/Appoggio/Backup");
                    String PathBackup = context.getFilesDir() + "/Appoggio/Backup/Backup.zip";
                    decodedBytes = Base64.getDecoder().decode(result);
                    try (FileOutputStream fos = new FileOutputStream(PathBackup)) {
                        fos.write(decodedBytes);
                        fos.close();

                        File fileZip = new File(PathBackup);
                        File fileUnzip = new File(context.getFilesDir() + "/Appoggio/Backup");
                        if (unzip(fileZip, fileUnzip)) {
                            Files.getInstance().EliminaFileUnico(PathBackup);

                            String PathOrigine = context.getFilesDir() + "/Appoggio/Backup";
                            String PathDestinazione = context.getFilesDir() + "/DB/";

                            File file = new File(PathOrigine);
                            File[] filetti = file.listFiles();
                            if (filetti != null) {
                                for (File f : filetti) {
                                    String FilettoOrigine = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                                    String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file
                                    String FilettoDestinazione = PathDestinazione + Nome;

                                    Files.getInstance().EliminaFileUnico(FilettoDestinazione);
                                    Files.getInstance().CopiaFile(FilettoOrigine, FilettoDestinazione);
                                    if (Files.getInstance().EsisteFile(FilettoDestinazione)) {
                                        Files.getInstance().EliminaFileUnico(FilettoOrigine);
                                    }
                                }
                                UtilitiesGlobali.getInstance().ApreToast(context, "Import effettuato");

                                Intent mStartActivity = new Intent(context, MainStart.class);
                                int mPendingIntentId = 123654;
                                PendingIntent mPendingIntent = PendingIntent.getActivity(
                                        context,
                                        mPendingIntentId,
                                        mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE
                                );
                                AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                                System.exit(0);
                            }
                        } else {
                            UtilitiesGlobali.getInstance().ApreToast(context, "ERROR: Unzip non riuscito");
                        }
                    } catch (IOException e) {
                        UtilitiesGlobali.getInstance().ApreToast(context, "ERROR: " + e.getMessage());
                    }

                    UtilityBackup.getInstance().Attende(false);
                    break;
                case "GPS":
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Si vogliono eliminare i dati presenti in archivio per le date caricate ?");
                    builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EsegueCaricamento(result, true);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EsegueCaricamento(result, false);
                        }
                    });

                    builder.show();
                    break;
            }
        }
    }

    private void EsegueCaricamento(String result, boolean EliminaVecchiDati) {
        byte[] decodedBytes;

        Files.getInstance().CreaCartelle(context.getFilesDir() + "/Appoggio/BackupGPS");
        File dir = new File(context.getFilesDir() + "/Appoggio/BackupGPS");
        for(File file: Objects.requireNonNull(dir.listFiles()))
            if (!file.isDirectory())
                file.delete();

        String PathGPS = context.getFilesDir() + "/Appoggio/Backup/BackupGPS.zip";
        decodedBytes = Base64.getDecoder().decode(result);
        try (FileOutputStream fos = new FileOutputStream(PathGPS)) {
            fos.write(decodedBytes);
            fos.close();

            File fileZip = new File(PathGPS);
            File fileUnzip = new File(context.getFilesDir() + "/Appoggio/BackupGPS");
            if (unzip(fileZip, fileUnzip)) {
                Files.getInstance().EliminaFileUnico(PathGPS);
            }

            int posizioniAggiunte = 0;
            db_dati_gps db = new db_dati_gps(context);
            String ultimaData = "";
            for(File file: Objects.requireNonNull(dir.listFiles())) {
                if (!file.isDirectory()) {
                    String Filetto = file.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                    String Nome = file.getAbsoluteFile().getName(); // Questo contiene solo il nome del file
                    String AppoNome = Nome.replace("DatiGPS_", "").replace(".csv", "");
                    String[] d = AppoNome.split("-");
                    String Data = d[2] + "/" + d[1] + "/" + d[0];
                    ultimaData = Data;
                    if (EliminaVecchiDati) {
                        db.EliminaPosizioni(Data);
                    }
                    String dati = Files.getInstance().LeggeFileUnico(Filetto);
                    String[] righe = dati.split("\n");
                    for (String r : righe) {
                        String[] c = r.split(";");

                        StrutturaGps s = new StrutturaGps();
                        s.setData(c[0]);
                        s.setOra(c[1]);
                        s.setLat(Double.parseDouble(c[2]));
                        s.setLon(Double.parseDouble(c[3]));
                        s.setSpeed(Float.parseFloat(c[4]));
                        s.setAltitude(Double.parseDouble(c[5]));
                        s.setAccuracy(Float.parseFloat(c[6]));
                        s.setDistanza(Float.parseFloat(c[7]));
                        s.setWifi(c[8].equals("S"));
                        s.setLivelloSegnale(Integer.parseInt(c[9]));
                        s.setTipoSegnale(c[10]);
                        s.setLevel(Integer.parseInt(c[11]));
                        s.setDirezione(Float.parseFloat(c[12]));

                        db.AggiungePosizione(s);
                        posizioniAggiunte++;
                    }

                    Files.getInstance().EliminaFileUnico(Filetto);
                }
            };
            if (!ultimaData.isEmpty()) {
                CalcoloVelocita cv = new CalcoloVelocita();
                UtilityGPS.getInstance().DisegnaPath(context, cv, ultimaData);
            }

            UtilitiesGlobali.getInstance().ApreToast(context, "Posizioni aggiunte: " + posizioniAggiunte);
        } catch (IOException e) {
            UtilitiesGlobali.getInstance().ApreToast(context, "ERROR: " + e.getMessage());
        }
        UtilityGPS.getInstance().ImpostaAttesa(false);
    }

    private void fEsporta(String result) {
        boolean ritorno = ControllaRitorno("Ritorno esporta", result);
        if (!ritorno) {
            UtilitiesGlobali.getInstance().ApreToast(context, result);
            return;
        }

        switch (this.TipoFile) {
            case "MODIFICHE":
                UtilitiesGlobali.getInstance().ApreToast(context, "DB esportato");
                VariabiliStaticheModificheCodice.getInstance().Attende(false);
                break;
            case "BACKUP":
                UtilitiesGlobali.getInstance().ApreToast(context, "DB esportato");
                UtilityBackup.getInstance().Attende(false);
                break;
            case "GPS":
                UtilitiesGlobali.getInstance().ApreToast(context, "Files GPS esportato");
                UtilityGPS.getInstance().ImpostaAttesa(false);
                break;
        }
    }
}
