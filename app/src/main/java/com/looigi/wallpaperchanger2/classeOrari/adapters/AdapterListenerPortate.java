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
import com.looigi.wallpaperchanger2.classeOrari.UtilityOrari;
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaMezzi;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPranzo;
import com.looigi.wallpaperchanger2.classePassword.UtilityPassword;
import com.looigi.wallpaperchanger2.classePassword.VariabiliStatichePWD;
import com.looigi.wallpaperchanger2.classePassword.db_dati_password;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerPortate extends BaseAdapter {
    private Context context;
    private List<StrutturaPranzo> listaPranzi;
    private LayoutInflater inflter;
    private List<StrutturaPranzo> listaPranziOrig;
    private boolean ModalitaPerNuovo;

    public AdapterListenerPortate(Context applicationContext, List<StrutturaPranzo> Pranzi, boolean ModalitaPerNuovo) {
        this.context = applicationContext;
        if (ModalitaPerNuovo) {
            this.listaPranziOrig = Pranzi;
            this.listaPranzi = Pranzi;
        } else {
            this.listaPranziOrig = Pranzi;
            this.listaPranzi = Pranzi;
        }
        inflter = (LayoutInflater.from(applicationContext));
        this.ModalitaPerNuovo = ModalitaPerNuovo;
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
        if (ModalitaPerNuovo) {
            view = inflter.inflate(R.layout.lista_portate_per_nuovo, null);
        } else {
            view = inflter.inflate(R.layout.lista_portate, null);
        }

        int idPortata = listaPranzi.get(i).getIdPortata();

        TextView txtSito = (TextView) view.findViewById(R.id.txtPortata);
        txtSito.setText(listaPranzi.get(i).getPortata());

        if (!ModalitaPerNuovo) {
            ImageView imgElimina = view.findViewById(R.id.imgElimina);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    listaPranzi.remove(i);

                    VariabiliStaticheOrari.getInstance().getDatiGiornata().setPranzo(listaPranzi);

                    UtilityOrari.getInstance().AggiornaListaPortate(context, false);
                }
            });
        } else {
            ImageView imgAggiunge = view.findViewById(R.id.imgAggiunge);
            imgAggiunge.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StrutturaPranzo s = listaPranzi.get(i);
                    List<StrutturaPranzo> lista = VariabiliStaticheOrari.getInstance().getDatiGiornata().getPranzo();
                    lista.add(s);
                    VariabiliStaticheOrari.getInstance().getDatiGiornata().setPranzo(lista);

                    UtilityOrari.getInstance().AggiornaListaPortate(context, false);

                    VariabiliStaticheOrari.getInstance().getLayBloccoSfondo().setVisibility(LinearLayout.GONE);
                    VariabiliStaticheOrari.getInstance().getLayNuovoDato().setVisibility(LinearLayout.GONE);
                }
            });
        }

        return view;
    }
}
