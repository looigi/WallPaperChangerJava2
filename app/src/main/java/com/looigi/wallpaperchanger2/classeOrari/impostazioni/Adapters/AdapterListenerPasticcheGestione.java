package com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPasticca;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerPasticcheGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaPasticca> listaPasticche;
    private List<StrutturaPasticca> listaPasticcheOrig;
    private LayoutInflater inflter;

    public AdapterListenerPasticcheGestione(Context applicationContext, List<StrutturaPasticca> Pasticche) {
        this.context = applicationContext;
        this.listaPasticcheOrig = Pasticche;
        this.listaPasticche = Pasticche;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaPasticche.size();
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
        listaPasticche = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaPasticcheOrig.size(); i++) {
            String NomeImmagine = listaPasticcheOrig.get(i).getPasticca();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaPasticche.add(listaPasticcheOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_pasticche_gestione, null);

        TextView txtSito = (TextView) view.findViewById(R.id.txtPasticca);
        txtSito.setText(listaPasticche.get(i).getPasticca());

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listaPasticche.remove(i);
            }
        });

        return view;
    }
}
