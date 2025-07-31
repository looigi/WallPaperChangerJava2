package com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.adapters;

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
import com.looigi.wallpaperchanger2.classeImmaginiUguali.VariabiliImmaginiUguali;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.DownloadImmagineUguali;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.MainControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.VariabiliStaticheControlloImmagini;

import java.util.List;

public class AdapterListenerListaControllo extends BaseAdapter {
    private Context context;
    private List<String> listaImmagini;
    private LayoutInflater inflter;

    public AdapterListenerListaControllo(Context applicationContext, List<String> Immagini) {
        this.context = applicationContext;
        this.listaImmagini = Immagini;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if (listaImmagini != null) {
            return listaImmagini.size();
        } else {
            return 0;
        }
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
        view = inflter.inflate(R.layout.lista_controllo_immagini, null);

        String[] Dati = listaImmagini.get(i).split("§");
        if (Dati.length > 3) {
            String NomeImmagine = Dati[0];
            String Cartella = Dati[1];
            int idImm = Integer.parseInt(Dati[2]);
            String Cosa = Dati[3];
            String Categoria = VariabiliStaticheControlloImmagini.getInstance().getCategoria();
            String Path = VariabiliImmaginiUguali.PathUrl + Categoria + "/" + Cartella + "/" + NomeImmagine;

            ImageView imgImmagine = view.findViewById(R.id.imgImmagine);
            imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheControlloImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.VISIBLE);

                    DownloadImmagineUguali d = new DownloadImmagineUguali();
                    d.EsegueDownload(context, VariabiliStaticheControlloImmagini.getInstance().getImgPreview(), Path);
                }
            });

            DownloadImmagineUguali d = new DownloadImmagineUguali();
            d.EsegueDownload(context, imgImmagine, Path);

            TextView txtImmagine = view.findViewById(R.id.txtImmagine);
            txtImmagine.setText(NomeImmagine);

            TextView txtCartella = view.findViewById(R.id.txtCartella);
            txtCartella.setText("Cart.: " + Cartella);

            TextView txtId = view.findViewById(R.id.txtIdImmagine);
            txtId.setText("Id: " + Integer.toString(idImm));

            TextView txtCosa = view.findViewById(R.id.txtCosa);
            txtCosa.setText(Cosa);
        } else {
            TextView txtImmagine = view.findViewById(R.id.txtImmagine);
            txtImmagine.setText("ERRORE: " + listaImmagini.get(i));
        }

        return view;
    }
}
