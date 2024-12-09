package com.looigi.wallpaperchanger2.classeImmaginiUguali;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.ChiamateWSMIU;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.DownloadImmagineSI;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.StrutturaImmagineDaScaricare;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerTipi extends BaseAdapter {
    private Context context;
    private List<StrutturaImmaginiUguali> Immagini;
    private LayoutInflater inflater;

    public AdapterListenerTipi(Context applicationContext, List<StrutturaImmaginiUguali> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Immagini.size();
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
        if (view != null) return view;

        view = inflater.inflate(R.layout.lista_tipi, null);

        if (i < Immagini.size()) {
            TextView txtQuanti = view.findViewById(R.id.txtQuanti);
            txtQuanti.setText(Integer.toString(Immagini.get(i).getQuanti()));

            TextView txtFiltro = view.findViewById(R.id.txtFiltro);
            txtFiltro.setText(Immagini.get(i).getFiltro());

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String Filtro = Immagini.get(i).getFiltro();

                    ChiamateWSMIU c = new ChiamateWSMIU(context);
                    c.RitornaImmaginiUgualiFiltro(
                            VariabiliImmaginiUguali.getInstance().getCategoria(),
                            VariabiliImmaginiUguali.getInstance().getTipoImpostato(),
                            Filtro
                    );
                }
            });
        }

        return view;
    }
}
