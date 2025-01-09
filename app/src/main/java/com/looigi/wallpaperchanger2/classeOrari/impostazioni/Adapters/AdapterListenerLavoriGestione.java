package com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaLavoro;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerLavoriGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaLavoro> listaLavori;
    private List<StrutturaLavoro> listaLavoriOrig;
    private LayoutInflater inflter;

    public AdapterListenerLavoriGestione(Context applicationContext, List<StrutturaLavoro> Lavori) {
        this.context = applicationContext;
        this.listaLavoriOrig = Lavori;
        this.listaLavori = Lavori;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaLavori.size();
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
        listaLavori = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaLavoriOrig.size(); i++) {
            String NomeImmagine = listaLavoriOrig.get(i).getLavoro();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaLavori.add(listaLavoriOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_lavori_gestione, null);

        TextView txtSito = (TextView) view.findViewById(R.id.txtLavoro);
        txtSito.setText(listaLavori.get(i).getLavoro());

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listaLavori.remove(i);
            }
        });

        return view;
    }
}
