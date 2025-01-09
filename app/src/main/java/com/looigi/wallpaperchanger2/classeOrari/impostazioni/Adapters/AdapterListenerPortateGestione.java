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
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.VariabiliStaticheImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.webService.ChiamateWSImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPranzo;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerPortateGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaPranzo> listaPranzi;
    private LayoutInflater inflter;
    private List<StrutturaPranzo> listaPranziOrig;

    public AdapterListenerPortateGestione(Context applicationContext, List<StrutturaPranzo> Pranzi) {
        this.context = applicationContext;
        this.listaPranziOrig = Pranzi;
        this.listaPranzi = Pranzi;
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void updateData(String Filtro) {
        listaPranzi = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaPranziOrig.size(); i++) {
            String NomeImmagine = listaPranziOrig.get(i).getPortata();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaPranzi.add(listaPranziOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listaPranzi.size();
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
        view = inflter.inflate(R.layout.lista_portate_gestione, null);

        int idPortata = listaPranzi.get(i).getIdPortata();

        TextView txtSito = (TextView) view.findViewById(R.id.txtPortata);
        txtSito.setText(listaPranzi.get(i).getPortata());

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().setIdPortata(idPortata);
                VariabiliStaticheImpostazioniOrari.getInstance().getEdtPortata().setText(listaPranzi.get(i).getPortata());
                VariabiliStaticheImpostazioniOrari.getInstance().getLayPortata().setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                ws.EliminaPortata(String.valueOf(idPortata));

                listaPranzi.remove(i);

                notifyDataSetChanged();
            }
        });

        return view;
    }
}
