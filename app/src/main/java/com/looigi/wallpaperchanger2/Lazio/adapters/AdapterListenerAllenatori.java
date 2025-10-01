package com.looigi.wallpaperchanger2.Lazio.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Lazio.Strutture.StrutturaAllenatori;
import com.looigi.wallpaperchanger2.Lazio.UtilityLazio;

import java.util.List;

public class AdapterListenerAllenatori extends BaseAdapter {
    private Context context;
    private List<StrutturaAllenatori> listaAllenatori;
    private LayoutInflater inflter;

    public AdapterListenerAllenatori(Context applicationContext, List<StrutturaAllenatori> Allenatori) {
        this.context = applicationContext;
        this.listaAllenatori = Allenatori;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaAllenatori.size();
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
        view = inflter.inflate(R.layout.lista_allenatori, null);

        String Nome = listaAllenatori.get(i).getNome();
        String Cognome = listaAllenatori.get(i).getCognome();

        TextView txtNome = view.findViewById(R.id.txtNome);
        txtNome.setText(Nome);

        TextView txtCognome = view.findViewById(R.id.txtCognome);
        txtCognome.setText(Cognome);

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().ApreModifica(context, "ALLENATORI", "UPDATE",
                        "Modifica allenatore", String.valueOf(i));
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
