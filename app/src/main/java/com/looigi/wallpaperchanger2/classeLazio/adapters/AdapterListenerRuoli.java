package com.looigi.wallpaperchanger2.classeLazio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaFonti;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaRuoli;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;

import java.util.List;

public class AdapterListenerRuoli extends BaseAdapter {
    private Context context;
    private List<StrutturaRuoli> listaRuoli;
    private LayoutInflater inflter;

    public AdapterListenerRuoli(Context applicationContext, List<StrutturaRuoli> Ruoli) {
        this.context = applicationContext;
        this.listaRuoli = Ruoli;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaRuoli.size();
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
        view = inflter.inflate(R.layout.lista_ruoli, null);

        String Ruolo = listaRuoli.get(i).getRuolo();

        TextView txtRuolo = view.findViewById(R.id.txtRuolo);
        txtRuolo.setText(Ruolo);

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().ApreModifica(context, "RUOLI", "UPDATE",
                        "Modifica ruolo", Ruolo);
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
