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
import com.looigi.wallpaperchanger2.classeModificheCodice.webService.ChiamateWSModifiche;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.ClasseZip;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
                            ClasseZip z = new ClasseZip();
                            z.UnzippaArchivio(context, NomeBackup.getNome());
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
                        ws.Importa("BACKUP", "");
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
            ClasseZip z = new ClasseZip();
            z.ZippaFile(context, PathBackup, NomeFile, "");
        } else {
            UtilitiesGlobali.getInstance().ApreToast(context, "Nessun DB presente");
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
