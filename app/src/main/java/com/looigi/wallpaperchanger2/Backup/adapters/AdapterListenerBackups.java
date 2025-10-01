package com.looigi.wallpaperchanger2.Backup.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Backup.StrutturaBackups;
import com.looigi.wallpaperchanger2.Backup.UtilityBackup;
import com.looigi.wallpaperchanger2.ModificheCodice.webService.ChiamateWSModifiche;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.io.File;
import java.util.List;

public class AdapterListenerBackups extends BaseAdapter {
    private Context context;
    private List<StrutturaBackups> listaBackups;
    private LayoutInflater inflater;
    private String Path;

    public AdapterListenerBackups(Context applicationContext, List<StrutturaBackups> Backups) {
        this.context = applicationContext;
        this.listaBackups = Backups;
        this.Path = context.getFilesDir() + "/Backups/";
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaBackups.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.lista_backups, null);

        if (i < listaBackups.size()) {
            String NomeBackup = listaBackups.get(i).getNome();

            ImageView btnElimina = view.findViewById(R.id.imgElimina);
            btnElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StrutturaBackups NomeBackup = UtilityBackup.getInstance().getNomeFileZipSelezionato();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Backup");
                    builder.setMessage("Si vuole eliminare il\nrestore selezionato?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Files.getInstance().EliminaFileUnico(listaBackups.get(i).getPath());

                            listaBackups.remove(i);
                            notifyDataSetChanged();
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

            ImageView btnCondividi = view.findViewById(R.id.imgCondividi);
            btnCondividi.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StrutturaBackups NomeBackup = listaBackups.get(i);
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(NomeBackup.getPath());
                    Uri uri = FileProvider.getUriForFile(context,
                    context.getApplicationContext().getPackageName() + ".provider",
                        f
                    );

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, NomeBackup.getNome());
                    i.putExtra(Intent.EXTRA_TEXT, "Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i, "Share backup"));
                }
            });

            ImageView btnEsporta = view.findViewById(R.id.imgEsporta);
            btnEsporta.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StrutturaBackups NomeBackup = listaBackups.get(i);

                    String Path = NomeBackup.getPath();

                    ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                    ws.Esporta("BACKUP", Path, false, "");
                }
            });

            TextView tBackup = (TextView) view.findViewById(R.id.txtBackup);
            tBackup.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UtilityBackup.getInstance().getTxtSelezionato().setText(listaBackups.get(i).getNome());
                    UtilityBackup.getInstance().setNomeFileZipSelezionato(listaBackups.get(i));
                }
            });
            tBackup.setText(NomeBackup);
        }

        return view;
    }
}
