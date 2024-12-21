package com.looigi.wallpaperchanger2.classeOrari.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;

import java.util.List;

public class AdapterListenerMezzi extends BaseAdapter {
    Context context;
    List<StrutturaMezzi> listaMezzi;
    LayoutInflater inflter;

    public AdapterListenerMezzi(Context applicationContext, List<StrutturaMezzi> Mezzi) {
        this.context = context;
        this.listaMezzi = Mezzi;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaMezzi.size();
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
        view = inflter.inflate(R.layout.lista_mezzi, null);

        TextView txtSito = (TextView) view.findViewById(R.id.txtMezzo);
        txtSito.setText(listaMezzi.get(i).getMezzo() + " " +
                (listaMezzi.get(i).getDettaglio() == null ? "" : listaMezzi.get(i).getDettaglio())
        );

        ImageView imgAggiunge = view.findViewById(R.id.imgAggiunge);
        imgAggiunge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgSu = view.findViewById(R.id.imgSu);
        imgSu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgGiu = view.findViewById(R.id.imgGiu);
        imgGiu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

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
