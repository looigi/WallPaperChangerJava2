package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.strutture.StrutturaVoltiRilevati;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.webService.DownloadImmaginePreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.VariabiliStaticheSpostamento;

import java.util.List;

public class AdapterListenerVoltiRilevati extends BaseAdapter {
    private Context context;
    private List<StrutturaVoltiRilevati> Immagini;
    private LayoutInflater inflater;
    private String Filtro;
    private String Modalita;

    public AdapterListenerVoltiRilevati(Context applicationContext, List<StrutturaVoltiRilevati> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;
        this.Filtro = Filtro;
        this.Modalita = Modalita;

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if (Immagini != null) {
            return Immagini.size();
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
        view = inflater.inflate(R.layout.lista_volti_rilevati, null);

        ImageView imgOrigine = view.findViewById(R.id.imgOrigine);
        ImageView imgDestinazione = view.findViewById(R.id.imgDestinazione);
        TextView txtCatOrigine = view.findViewById(R.id.txtOrigine);
        TextView txtCatDest = view.findViewById(R.id.txtDestinazione);

        StrutturaVoltiRilevati si = Immagini.get(i);
        txtCatOrigine.setText(si.getIdCategoriaOrigine() + "-" + si.getCategoriaOrigine());
        txtCatDest.setText(si.getIdCategoria() + "-" + si.getCategoria());

        DownloadImmaginePreview dO = new DownloadImmaginePreview();
        dO.EsegueChiamata(
                context,
                "",
                imgOrigine,
                si.getUrlOrigine()
        );
        DownloadImmaginePreview dD = new DownloadImmaginePreview();
        dD.EsegueChiamata(
                context,
                "",
                imgDestinazione,
                si.getUrlDestinazione()
        );

        ImageView imgSposta = view.findViewById(R.id.imgSposta);
        imgSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int idImmagine = si.getIdImmagine();

                StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary(); // CREARE LA STRUTTURA PER LO SPOSTAMENTO
                s.setIdImmagine(idImmagine);
                VariabiliStaticheSpostamento.getInstance().setIdCategoriaSpostamento(si.getIdCategoria());

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s, "SPOSTAMENTO");
            }
        });

        return view;
    }
}
