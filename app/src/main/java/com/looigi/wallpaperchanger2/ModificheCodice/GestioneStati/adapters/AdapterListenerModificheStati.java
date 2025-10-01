package com.looigi.wallpaperchanger2.ModificheCodice.GestioneStati.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ModificheCodice.Strutture.Stati;
import com.looigi.wallpaperchanger2.ModificheCodice.VariabiliStaticheModificheCodice;
import com.looigi.wallpaperchanger2.ModificheCodice.webService.ChiamateWSModifiche;

import java.util.List;

public class AdapterListenerModificheStati extends BaseAdapter {
    private Context context;
    private List<Stati> listaStati;
    private LayoutInflater inflater;

    public AdapterListenerModificheStati(Context applicationContext, List<Stati> Stati) {
        this.context = applicationContext;
        this.listaStati = Stati;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaStati.size();
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
        view = inflater.inflate(R.layout.lista_stati, null);

        String Stato = listaStati.get(i).getStato();
        int idStato = listaStati.get(i).getIdStato();

        TextView txtStato = view.findViewById(R.id.txtStato);
        txtStato.setText(Stato);

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheModificheCodice.getInstance().getLayGestioneStato().setVisibility(LinearLayout.VISIBLE);
                VariabiliStaticheModificheCodice.getInstance().getEdtGestioneStato().setText(Stato);
                VariabiliStaticheModificheCodice.getInstance().setIdGestioneStato(idStato);
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSModifiche ws = new ChiamateWSModifiche(context);
                ws.EliminaStato(String.valueOf(idStato), true);
            }
        });

        return view;
    }
}
