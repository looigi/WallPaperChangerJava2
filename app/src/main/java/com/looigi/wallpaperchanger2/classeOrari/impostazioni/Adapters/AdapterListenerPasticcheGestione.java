package com.looigi.wallpaperchanger2.classeOrari.impostazioni.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.VariabiliStaticheImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.webService.ChiamateWSImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaPasticca;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerPasticcheGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaPasticca> listaPasticche;
    private List<StrutturaPasticca> listaPasticcheOrig;
    private LayoutInflater inflter;

    public AdapterListenerPasticcheGestione(Context applicationContext, List<StrutturaPasticca> Pasticche) {
        this.context = applicationContext;
        this.listaPasticcheOrig = Pasticche;
        this.listaPasticche = Pasticche;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaPasticche.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void updateData(String Filtro) {
        listaPasticche = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaPasticcheOrig.size(); i++) {
            String NomeImmagine = listaPasticcheOrig.get(i).getPasticca();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaPasticche.add(listaPasticcheOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_pasticche_gestione, null);

        TextView txtSito = (TextView) view.findViewById(R.id.txtPasticca);
        txtSito.setText(listaPasticche.get(i).getPasticca());

        int idPasticca = listaPasticche.get(i).getIdPasticca();

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().setIdPasticca(idPasticca);
                VariabiliStaticheImpostazioniOrari.getInstance().getEdtPasticca().setText(listaPasticche.get(i).getPasticca());
                VariabiliStaticheImpostazioniOrari.getInstance().getLayPasticca().setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Orari");
                builder.setMessage("Si vuole eliminare la pasticca selezionata?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                        ws.EliminaPasticca(String.valueOf(idPasticca));

                        listaPasticche.remove(i);

                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        return view;
    }
}
