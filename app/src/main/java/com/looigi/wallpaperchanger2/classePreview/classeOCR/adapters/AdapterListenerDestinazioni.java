package com.looigi.wallpaperchanger2.classePreview.classeOCR.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.StrutturaImmagineFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.DownloadImmagineUguali;
import com.looigi.wallpaperchanger2.classePreview.MainPreview;
import com.looigi.wallpaperchanger2.classePreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.classePreview.classeOCR.VariabiliStaticheOCR;
import com.looigi.wallpaperchanger2.classePreview.classeOCR.strutture.StrutturaDestinazioni;
import com.looigi.wallpaperchanger2.classePreview.classeOCR.webService.ChiamateWSOCR;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.List;

public class AdapterListenerDestinazioni extends BaseAdapter {
    private Context context;
    private List<StrutturaDestinazioni> Destinazioni;
    private LayoutInflater inflater;

    public AdapterListenerDestinazioni(Context applicationContext, List<StrutturaDestinazioni> Dests) {
        this.context = applicationContext;
        this.Destinazioni = Dests;

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if (Destinazioni != null) {
            return Destinazioni.size();
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
        // if (view != null) return view;

        view = inflater.inflate(R.layout.lista_destinazioni_ocr, null);

        if (i < Destinazioni.size()) {
            TextView txtDestinazione = view.findViewById(R.id.txtDestinazione);
            TextView txtQuante = view.findViewById(R.id.txtQuante);

            txtDestinazione.setText(Destinazioni.get(i).getDestinazione());
            txtQuante.setText(Integer.toString(Destinazioni.get(i).getQuante()));

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheOCR.getInstance().setFiltroPremuto(Destinazioni.get(i).getDestinazione());

                    ChiamateWSOCR ws = new ChiamateWSOCR(context);
                    ws.RitornaImmagini(Destinazioni.get(i).getDestinazione());
                }
            });
        }

        return view;
    }
}
