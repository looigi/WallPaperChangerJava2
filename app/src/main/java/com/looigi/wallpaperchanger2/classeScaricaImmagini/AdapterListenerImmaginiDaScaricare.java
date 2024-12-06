package com.looigi.wallpaperchanger2.classeScaricaImmagini;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;

import java.util.List;

public class AdapterListenerImmaginiDaScaricare extends BaseAdapter {
    private Context context;
    private List<String> Immagini;
    private LayoutInflater inflater;
    private String Filtro;
    private String Modalita;

    public AdapterListenerImmaginiDaScaricare(Context applicationContext, String Modalita,
                                              String Filtro, List<String> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;
        this.Filtro = Filtro;
        this.Modalita = Modalita;

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
        view = inflater.inflate(R.layout.lista_immagini_da_scaricare, null);

        if (i < Immagini.size()) {
            String UrlImmagine = Immagini.get(i);

            ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);

            DownloadImmagineSI d = new DownloadImmagineSI();
            d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                    Filtro, false, "");

            /* imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            }); */

            TextView tImmagine = (TextView) view.findViewById(R.id.txtImmagine);
            tImmagine.setText(UrlImmagine);

            ImageView imgScarica = (ImageView) view.findViewById(R.id.imgScarica);
            imgScarica.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                            Filtro, true, "SCARICA");
                }
            });

            ImageView imgCondividi = (ImageView) view.findViewById(R.id.imgCondividi);
            imgCondividi.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                            Filtro, true, "CONDIVIDI");
                }
            });

            ImageView imgCopiaSuSfondi = (ImageView) view.findViewById(R.id.imgCopiaSuSfondi);
            imgCopiaSuSfondi.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    d.EsegueDownload(context, imgImmagine, UrlImmagine, Modalita,
                            Filtro, true, "COPIA");
                }
            });
        }

        return view;
    }
}
