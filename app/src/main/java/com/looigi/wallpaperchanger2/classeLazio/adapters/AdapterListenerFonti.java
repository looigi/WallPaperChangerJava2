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
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;

import java.util.List;

public class AdapterListenerFonti extends BaseAdapter {
    private Context context;
    private List<StrutturaFonti> listaFonti;
    private LayoutInflater inflter;

    public AdapterListenerFonti(Context applicationContext, List<StrutturaFonti> Fonti) {
        this.context = applicationContext;
        this.listaFonti = Fonti;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaFonti.size();
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
        view = inflter.inflate(R.layout.lista_fonti, null);

        String Fonte = listaFonti.get(i).getFonte();

        TextView txtFonte = view.findViewById(R.id.txtFonte);
        txtFonte.setText(Fonte);

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setIdOggettoModificato(listaFonti.get(i).getIdFonte());
                UtilityLazio.getInstance().ApreModifica(context, "FONTI", "UPDATE", "Modifica fonte", Fonte);
            }
        });
        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setCosaStoModificando("FONTI");
                VariabiliStaticheLazio.getInstance().setModalitaModifica("DELETE");

                VariabiliStaticheLazio.getInstance().setIdOggettoModificato(listaFonti.get(i).getIdFonte());
                UtilityLazio.getInstance().SalvaValori(context);
            }
        });

        return view;
    }
}
