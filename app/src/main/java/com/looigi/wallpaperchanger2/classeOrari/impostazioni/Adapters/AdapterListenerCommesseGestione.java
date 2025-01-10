package com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Commesse.MainCommessa;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.VariabiliStaticheImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaCommesse;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerCommesseGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaCommesse> listaCommesse;
    private List<StrutturaCommesse> listaCommesseOrig;
    private LayoutInflater inflter;

    public AdapterListenerCommesseGestione(Context applicationContext, List<StrutturaCommesse> Commesse) {
        this.context = applicationContext;
        this.listaCommesseOrig = Commesse;
        this.listaCommesse = Commesse;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaCommesse.size();
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
        listaCommesse = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaCommesseOrig.size(); i++) {
            String NomeImmagine = listaCommesseOrig.get(i).getDescrizione();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaCommesse.add(listaCommesseOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_commesse_gestione, null);

        TextView txtSito = (TextView) view.findViewById(R.id.txtCommessa);
        txtSito.setText(listaCommesse.get(i).getDescrizione());

        /* listaCommesse.get(i).getCommessa()
        listaCommesse.get(i).getCliente()
        listaCommesse.get(i).getDescrizione()
        listaCommesse.get(i).getDataInizio()
        listaCommesse.get(i).getDataFine()
        listaCommesse.get(i).getIndirizzo()
        listaCommesse.get(i).getLatLng() */

        int idCommessa = listaCommesse.get(i).getIdCommessa();

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().setIdCommessa(idCommessa);

                Intent iI = new Intent(context, MainCommessa.class);
                iI.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(iI);
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                listaCommesse.remove(i);
            }
        });

        return view;
    }
}
