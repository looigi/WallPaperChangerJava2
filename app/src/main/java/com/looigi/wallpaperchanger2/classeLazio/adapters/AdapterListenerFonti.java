package com.looigi.wallpaperchanger2.classeLazio.adapters;

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
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaFonti;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaStati;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.classePlayer.Files;

import java.util.List;

public class AdapterListenerFonti extends BaseAdapter {
    private Context context;
    private List<StrutturaFonti> listaFonti;
    private LayoutInflater inflter;

    public AdapterListenerFonti(Context applicationContext, List<StrutturaFonti> Fonti) {
        this.context = applicationContext;
        this.listaFonti = Fonti;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaFonti.size();
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
        view = inflter.inflate(R.layout.lista_fonti, null);

        String Fonte = listaFonti.get(i).getFonte();

        TextView txtFonte = view.findViewById(R.id.txtFonte);
        txtFonte.setText(Fonte);

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });
        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        return view;
    }
}
