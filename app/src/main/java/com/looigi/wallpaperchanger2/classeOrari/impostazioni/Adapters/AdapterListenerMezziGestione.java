package com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.UtilityOrari;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerMezziGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaMezzi> listaMezzi;
    private List<StrutturaMezzi> listaMezziOrig;
    private LayoutInflater inflter;

    public AdapterListenerMezziGestione(Context applicationContext, List<StrutturaMezzi> Mezzi) {
        this.context = applicationContext;
        this.listaMezziOrig = Mezzi;
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

    public void updateData(String Filtro) {
        listaMezzi = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaMezziOrig.size(); i++) {
            String NomeImmagine = listaMezziOrig.get(i).getMezzo() + " " + listaMezziOrig.get(i).getDettaglio();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaMezzi.add(listaMezziOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_mezzi_gestione, null);

        TextView txtSito = (TextView) view.findViewById(R.id.txtMezzo);
        txtSito.setText(listaMezzi.get(i).getMezzo() + " " +
                (listaMezzi.get(i).getDettaglio() == null ? "" : listaMezzi.get(i).getDettaglio())
        );

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listaMezzi.remove(i);
            }
        });

        return view;
    }
}
