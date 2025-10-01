package com.looigi.wallpaperchanger2.Orari.impostazioni.Adapters;

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
import com.looigi.wallpaperchanger2.Orari.impostazioni.VariabiliStaticheImpostazioniOrari;
import com.looigi.wallpaperchanger2.Orari.impostazioni.webService.ChiamateWSImpostazioniOrari;
import com.looigi.wallpaperchanger2.Orari.strutture.StrutturaMezzi;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerMezziGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaMezzi> listaMezzi;
    private List<StrutturaMezzi> listaMezziOrig;
    private LayoutInflater inflter;

    public AdapterListenerMezziGestione(Context applicationContext, List<StrutturaMezzi> Mezzi) {
        this.context = applicationContext;
        this.listaMezziOrig = Mezzi;
        this.listaMezzi = Mezzi;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaMezzi.size();
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
        listaMezzi = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaMezziOrig.size(); i++) {
            String NomeImmagine = listaMezziOrig.get(i).getMezzo() + " " + listaMezziOrig.get(i).getDettaglio();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaMezzi.add(listaMezziOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_mezzi_gestione, null);

        int idMezzo = listaMezzi.get(i).getIdMezzo();

        TextView txtSito = (TextView) view.findViewById(R.id.txtMezzo);
        txtSito.setText(listaMezzi.get(i).getMezzo() + " " +
                (listaMezzi.get(i).getDettaglio() == null ? "" : listaMezzi.get(i).getDettaglio())
        );

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().setIdMezzo(idMezzo);
                VariabiliStaticheImpostazioniOrari.getInstance().getEdtMezzo().setText(listaMezzi.get(i).getMezzo());
                VariabiliStaticheImpostazioniOrari.getInstance().getEdtMezzoDettaglio().setText(listaMezzi.get(i).getDettaglio());
                VariabiliStaticheImpostazioniOrari.getInstance().getLayMezzi().setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Orari");
                builder.setMessage("Si vuole eliminare il mezzo selezionato?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                        ws.EliminaMezzo(String.valueOf(idMezzo));

                        listaMezzi.remove(i);

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
