package com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.Strutture.Ammoniti;
import com.looigi.wallpaperchanger2.classeLazio.DettaglioPartita.Strutture.Formazione;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;

import java.util.List;

public class AdapterListenerFormazione extends BaseAdapter {
    private Context context;
    private List<Formazione> lista;
    private LayoutInflater inflter;
    private boolean inCasa;

    public AdapterListenerFormazione(Context applicationContext, List<Formazione> Lista, boolean Casa) {
        this.context = applicationContext;
        this.lista = Lista;
        this.inCasa = Casa;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return lista.size();
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
        view = inflter.inflate(R.layout.lista_formazione, null);

        String Nome = lista.get(i).getNome();
        String Cognome = lista.get(i).getCognome();
        String Entrato = String.valueOf(lista.get(i).getEntrato());
        if (!Entrato.isEmpty()) {
            Entrato += "°";
        }
        String Uscito = String.valueOf(lista.get(i).getUscito());
        if (!Uscito.isEmpty()) {
            Uscito += "°";
        }

        // TextView txtNome = view.findViewById(R.id.txtNome);
        // txtNome.setText(Nome);

        TextView txtCognome = view.findViewById(R.id.txtCognome);
        txtCognome.setText(Cognome + " " + Nome);

        TextView txtEntrato = view.findViewById(R.id.txtEntrato);
        txtEntrato.setText(Entrato);

        TextView txtUscito = view.findViewById(R.id.txtUscito);
        txtUscito.setText(Uscito);

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        return view;
    }
}
