package com.looigi.wallpaperchanger2.classeBackup;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.util.ArrayList;
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
                    builder.setTitle("Si vuole eliminare il\nrestore selezionato?");
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
