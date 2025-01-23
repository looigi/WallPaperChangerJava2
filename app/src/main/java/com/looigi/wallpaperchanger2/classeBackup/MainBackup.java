package com.looigi.wallpaperchanger2.classeBackup;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeModificheCodice.webService.ChiamateWSModifiche;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class MainBackup extends Activity {
    private static final String NomeMaschera = "MainBackup";
    private Activity act;
    private Context context;
    private String PathBackup;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_backup);

        act = this;
        context = this;

        PathBackup = context.getFilesDir() + "/Backups/";

        ListView lstBackups = findViewById(R.id.lstBackups);
        UtilityBackup.getInstance().setTxtSelezionato(findViewById(R.id.txtBackupSelezionato));

        UtilityBackup.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoBackup));
        UtilityBackup.getInstance().Attende(false);

        Button btnBackup = findViewById(R.id.btnBackup);
        btnBackup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /* String Path1 = UtilityDetector.getInstance().PrendePathDB(context) + "dati_compleanni.db";
                String Path2 = UtilityDetector.getInstance().PrendePathDB(context) + "dati_compleanni.db-journal";
                Files.getInstance().EliminaFileUnico(Path1);
                Files.getInstance().EliminaFileUnico(Path2);
                UtilityDetector.getInstance().VisualizzaToast(context, "Files eliminati", true); */
                EffettuaNuovoBackup(context);

                PrendeBackups(lstBackups);
            }
        });

        Button btnRestore = findViewById(R.id.btnRestore);
        btnRestore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (UtilityBackup.getInstance().getNomeFileZipSelezionato() != null) {
                    StrutturaBackups NomeBackup = UtilityBackup.getInstance().getNomeFileZipSelezionato();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Si vuole effettuare il\nrestore del file selezionato?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UtilityBackup.getInstance().UnzippaArchivio(context, NomeBackup.getNome());
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Prima selezionare un archivio");
                }
            }
        });

        Button btnImporta = findViewById(R.id.btnImporta);
        btnImporta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vogliono importare i dati dal db remoto?\nTutti i dati attuali verranno sovrascritti");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                        ws.Importa("BACKUP");
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        PrendeBackups(lstBackups);
    }

    private void EffettuaNuovoBackup(Context context) {
        Files.getInstance().CreaCartelle(PathBackup);
        String PathOrigine = context.getFilesDir() + "/DB/";

        List<String> NomeFile = new ArrayList<>();

        File root = new File(PathOrigine);
        File[] list = root.listFiles();
        assert list != null;
        for (File f : list) {
            if (!f.isDirectory()) {
                NomeFile.add(f.getAbsoluteFile().getPath());
            }
        }
        if (!NomeFile.isEmpty()) {
            ZippaFile(context, NomeFile);
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Nessun DB presente");
        }
    }

    private void ZippaFile(Context context, List<String> NomeFile) {
        String NomeFileZip1 = UtilitiesGlobali.getInstance().TornaNomeFileConData();

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

    private void PrendeBackups(ListView lstBackups) {
        Files.getInstance().CreaCartelle(PathBackup);

        File root = new File(PathBackup);
        File[] list = root.listFiles();

        List<StrutturaBackups> Nomi = new ArrayList<>();

        assert list != null;
        for (File f : list) {
            if (!f.isDirectory()) {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                String Nome = f.getAbsoluteFile().getName(); // Questo contiene solo il nome del file

                StrutturaBackups s = new StrutturaBackups();
                s.setNome(Nome);
                s.setPath(Filetto);

                Nomi.add(s);
            }
        }

        AdapterListenerBackups customAdapterT = new AdapterListenerBackups(context, Nomi);
        lstBackups.setAdapter(customAdapterT);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return false;
        }

        return false;
    }
}
