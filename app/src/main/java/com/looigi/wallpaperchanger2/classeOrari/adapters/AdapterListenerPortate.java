package com.looigi.wallpaperchanger2.classeOrari.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPranzo;
import com.looigi.wallpaperchanger2.classePassword.UtilityPassword;
import com.looigi.wallpaperchanger2.classePassword.VariabiliStatichePWD;
import com.looigi.wallpaperchanger2.classePassword.db_dati_password;

import java.util.List;

public class AdapterListenerPortate extends BaseAdapter {
    Context context;
    List<StrutturaPranzo> listaPranzi;
    LayoutInflater inflter;

    public AdapterListenerPortate(Context applicationContext, List<StrutturaPranzo> Pranzi) {
        this.context = context;
        this.listaPranzi = Pranzi;
        inflter = (LayoutInflater.from(applicationContext));
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
        view = inflter.inflate(R.layout.lista_portate, null);

        int idPortata = listaPranzi.get(i).getIdPortata();

        TextView txtSito = (TextView) view.findViewById(R.id.txtPortata);
        txtSito.setText(listaPranzi.get(i).getPortata());

        ImageView imgAggiunge = view.findViewById(R.id.imgAggiunge);
        imgAggiunge.setOnClickListener(new View.OnClickListener() {
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
