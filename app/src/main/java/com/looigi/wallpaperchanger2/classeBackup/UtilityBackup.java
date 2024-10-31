package com.looigi.wallpaperchanger2.classeBackup;

import android.content.Context;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.utilities.CaricaSettaggi;
import com.looigi.wallpaperchanger2.utilities.LogInterno;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UtilityBackup {
    private static final String NomeMaschera = "Utility_Backup";
    private static UtilityBackup instance = null;

    private UtilityBackup() {
    }

    public static UtilityBackup getInstance() {
        if (instance == null) {
            instance = new UtilityBackup();
        }

        return instance;
    }

    private TextView txtSelezionato;
    private StrutturaBackups nomeFileZipSelezionato;

    public TextView getTxtSelezionato() {
        return txtSelezionato;
    }

    public void setTxtSelezionato(TextView txtSelezionato) {
        this.txtSelezionato = txtSelezionato;
    }

    public StrutturaBackups getNomeFileZipSelezionato() {
        return nomeFileZipSelezionato;
    }

    public void setNomeFileZipSelezionato(StrutturaBackups nomeFileZipSelezionato) {
        this.nomeFileZipSelezionato = nomeFileZipSelezionato;
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
            VariabiliStaticheStart.getInstance().getLog().ScriveLog("Backup", Maschera,  Log);
            // }
        } else {

        }
    }

    public void UnzippaArchivio(Context context, String NomeArchivio) {
        String PathBackup = context.getFilesDir() + "/Backups/" + NomeArchivio;
        String PathDest = context.getFilesDir() + "/Backups/Estrazioni/";
        deleteRecursive(new File(PathDest));

        ScriveLog(context, NomeMaschera, "Origine: " + PathBackup);
        ScriveLog(context, NomeMaschera, "Destinazione: " + PathDest);

        try {
            unzip(context, PathBackup, PathDest);

            ScriveLog(context, NomeMaschera, "Unzip effettuato");
        } catch (IOException e) {
            ScriveLog(context, NomeMaschera, "Errore su unzip: " +
                    UtilityDetector.getInstance().PrendeErroreDaException(e));

            UtilitiesGlobali.getInstance().ApreToast(context, "Errore su unzip");
        } finally {
            try {
                copiaFiles(context, PathDest);

                ScriveLog(context, NomeMaschera, "Copia effettuata");
            } catch (IOException e) {
                ScriveLog(context, NomeMaschera, "Errore su copia: " +
                        UtilityDetector.getInstance().PrendeErroreDaException(e));

                UtilitiesGlobali.getInstance().ApreToast(context, "Errore su copia");
            } finally {
                deleteRecursive(new File(PathDest));

                String ritorno = CaricaSettaggi.getInstance().CaricaImpostazioniGlobali(context, "MAIN");
                if (!ritorno.equals("OK")) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Restore effettuato ma errore sul caricamento:\n" + ritorno);
                } else {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Restore effettuato");
                }
            }
        }
    }

    private void copiaFiles(Context context, String Origine) throws IOException {
        String PathBackup = context.getFilesDir() + "/DB/";

        File root = new File(Origine);
        File[] list = root.listFiles();
        assert list != null;
        for (File f : list) {
            if (!f.isDirectory()) {
                String FileDestinazione = PathBackup + f.getAbsoluteFile().getName();
                if (Files.getInstance().EsisteFile(FileDestinazione)) {
                    Files.getInstance().EliminaFileUnico(FileDestinazione);
                }
                String FileOrigine = f.getAbsoluteFile().getPath();

                ScriveLog(context, NomeMaschera, "Copia file: " + FileOrigine + " -> " + FileDestinazione);
                Files.getInstance().CopiaFile(FileOrigine, FileDestinazione);
            }
        }
    }

    private void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : Objects.requireNonNull(fileOrDirectory.listFiles()))
                deleteRecursive(child);

        fileOrDirectory.delete();
    }

    private void unzip(Context context, String sZipFile, String sTargetDirectory) throws IOException {
        ScriveLog(context, NomeMaschera, "Unzip file: " + sZipFile + " -> " + sTargetDirectory);

        File zipFile = new File(sZipFile);
        File targetDirectory = new File(sTargetDirectory);

        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                ScriveLog(context, NomeMaschera, "Estrazione file: " + ze.getName());

                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                try (FileOutputStream fout = new FileOutputStream(file)) {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                }
            }
        } finally {
            zis.close();
        }
    }
}
