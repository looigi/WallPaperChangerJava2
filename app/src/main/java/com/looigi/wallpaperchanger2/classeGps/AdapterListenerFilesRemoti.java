package com.looigi.wallpaperchanger2.classeGps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeGps.strutture.StrutturaNomeFileRemoti;
import com.looigi.wallpaperchanger2.classeModificheCodice.webService.ChiamateWSModifiche;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.ChiamateWsWP;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.DownloadImmagineWP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterListenerFilesRemoti extends BaseAdapter {
    private Context context;
    private List<StrutturaNomeFileRemoti> listaFilesOrig;
    private List<StrutturaNomeFileRemoti> listaFiles;
    private LayoutInflater inflater;

    public AdapterListenerFilesRemoti(Context applicationContext, List<StrutturaNomeFileRemoti> Files) {
        this.context = applicationContext;
        this.listaFilesOrig = Files;
        this.listaFiles = Files; //  new ArrayList();

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaFiles.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void updateData(String Filtro) {
        listaFiles = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaFilesOrig.size(); i++) {
            String NomeFile = listaFilesOrig.get(i).getPath();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeFile.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaFiles.add(listaFilesOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.lista_files_remoti, null);

        if (i < listaFiles.size()) {
            String sNomeFile = listaFiles.get(i).getNomeFile();

            ImageView imgElimina = (ImageView) view.findViewById(R.id.imgElimina);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                    ws.EliminaFileRemoto("GPS", sNomeFile);
                }
            });

            ImageView imgScarica = (ImageView) view.findViewById(R.id.imgScarica);
            imgScarica.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                    ws.Importa("GPS", sNomeFile);
                }
            });

            TextView NomeFile = (TextView) view.findViewById(R.id.txtNomeFile);
            NomeFile.setText(sNomeFile);
        }

        return view;
    }
}
