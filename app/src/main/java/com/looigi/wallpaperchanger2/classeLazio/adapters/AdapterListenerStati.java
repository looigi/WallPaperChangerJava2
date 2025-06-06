package com.looigi.wallpaperchanger2.classeLazio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaStati;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;

import java.util.List;

public class AdapterListenerStati extends BaseAdapter {
    private Context context;
    private List<StrutturaStati> listaStati;
    private LayoutInflater inflter;

    public AdapterListenerStati(Context applicationContext, List<StrutturaStati> Stati) {
        this.context = applicationContext;
        this.listaStati = Stati;
        inflter = (LayoutInflater.from(applicationContext));
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
        view = inflter.inflate(R.layout.lista_stati, null);

        String Stato = listaStati.get(i).getStato();

        TextView txtStato = view.findViewById(R.id.txtStato);
        txtStato.setText(Stato);

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setIdOggettoModificato(listaStati.get(i).getIdStato());
                UtilityLazio.getInstance().ApreModifica(context, "STATI", "UPDATE",
                        "Modifica stato", Stato);
            }
        });
        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setCosaStoModificando("STATI");
                VariabiliStaticheLazio.getInstance().setModalitaModifica("DELETE");

                VariabiliStaticheLazio.getInstance().setIdOggettoModificato(listaStati.get(i).getIdStato());
                UtilityLazio.getInstance().SalvaValori(context);
            }
        });

        return view;
    }
}
