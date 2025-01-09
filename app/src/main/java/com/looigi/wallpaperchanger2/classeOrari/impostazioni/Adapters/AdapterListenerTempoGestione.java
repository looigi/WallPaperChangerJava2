package com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaCommesse;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaTempo;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerTempoGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaTempo> listaTempi;
    private List<StrutturaTempo> listaTempiOrig;
    private LayoutInflater inflter;

    public AdapterListenerTempoGestione(Context applicationContext, List<StrutturaTempo> Tempi) {
        this.context = applicationContext;
        this.listaTempiOrig = Tempi;
        this.listaTempi = Tempi;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaTempi.size();
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
        listaTempi = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaTempiOrig.size(); i++) {
            String NomeImmagine = listaTempiOrig.get(i).getTempo();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaTempi.add(listaTempiOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_tempo_gestione, null);

        TextView txtSito = (TextView) view.findViewById(R.id.txtTempo);
        txtSito.setText(listaTempi.get(i).getTempo());

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listaTempi.remove(i);
            }
        });

        return view;
    }
}
