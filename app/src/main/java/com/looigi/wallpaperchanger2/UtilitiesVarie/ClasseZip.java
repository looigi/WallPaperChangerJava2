package com.looigi.wallpaperchanger2.UtilitiesVarie;

import android.content.Context;

import com.looigi.wallpaperchanger2.Backup.UtilityBackup;
import com.looigi.wallpaperchanger2.Detector.UtilityDetector;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ClasseZip {
    private static final String NomeMaschera = "MainZIP";

    public void ZippaFile(Context context, String PathBackup, List<String> NomeFile, String NomeFileZipPassato) {
        String NomeFileZip1 = "";
        if (NomeFileZipPassato.isEmpty()) {
            NomeFileZip1 = UtilitiesGlobali.getInstance().TornaNomeFileConData();
        } else {
            NomeFileZip1 = NomeFileZipPassato;
        }

        String NomeFileZip = PathBackup + NomeFileZip1 + ".zip";

        UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,"Zip files");

        int BUFFER_SIZE = 1024;
        byte data[] = new byte[BUFFER_SIZE];

        BufferedInputStream origin = null;
        try {
            ZipOutputStream out = new ZipOutputStream(
                    new BufferedOutputStream(
                            new FileOutputStream(NomeFileZip)));
            for (String filetto : NomeFile) {
                UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,"Zip files: " + filetto);

                File f = new File(filetto);
                FileInputStream fi = new FileInputStream(f);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                try {
                    ZipEntry entry = new ZipEntry(f.getAbsoluteFile().getName());
                    out.putNextEntry(entry);
                    int count;
                    while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                        out.write(data, 0, count);
                    }
                } catch (IOException e) {
                    UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                            "Errore su zip files 1: " +
                                    UtilityDetector.getInstance().PrendeErroreDaException(e));
                } finally {
                    origin.close();
                }
            }
            out.close();
        } catch (IOException e) {
            UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                    "Errore su zip files 2: " +
                            UtilityDetector.getInstance().PrendeErroreDaException(e));
        }
    }

    public void UnzippaArchivio(Context context, String NomeArchivio) {
        String PathBackup = context.getFilesDir() + "/Backups/" + NomeArchivio;
        String PathDest = context.getFilesDir() + "/Backups/Estrazioni/";
        deleteRecursive(new File(PathDest));

        UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                "Origine: " + PathBackup);
        UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                "Destinazione: " + PathDest);

        try {
            unzip(context, PathBackup, PathDest);

            UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                    "Unzip Effettuato");
        } catch (IOException e) {
            UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                    "Errore su unzip: " +
                            UtilityDetector.getInstance().PrendeErroreDaException(e));

            UtilitiesGlobali.getInstance().ApreToast(context, "Errore su unzip");
        } finally {
            try {
                copiaFiles(context, PathDest);

                UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                        "Copia Effettuata");
            } catch (IOException e) {
                UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                        "Errore su copia: " +
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

    private void unzip(Context context, String sZipFile, String sTargetDirectory) throws IOException {
        UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                "Unzip file: " + sZipFile + " -> " + sTargetDirectory);

        File zipFile = new File(sZipFile);
        File targetDirectory = new File(sTargetDirectory);

        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                        "Estrazione file: " + ze.getName());

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

                UtilityBackup.getInstance().ScriveLog(context, NomeMaschera,
                        "Copia file: " + FileOrigine + " -> " + FileDestinazione);
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
}
