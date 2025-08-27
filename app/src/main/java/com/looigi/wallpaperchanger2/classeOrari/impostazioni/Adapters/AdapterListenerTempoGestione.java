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
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaCommesse;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaTempo;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerTempoGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaTempo> listaTempi;
    private List<StrutturaTempo> listaTempiOrig;
    private LayoutInflater inflter;

    public AdapterListenerTempoGestione(Context applicationContext, List<StrutturaTempo> Tempi) {
        this.context = applicationContext;
        this.listaTempiOrig = Tempi;
        this.listaTempi = Tempi;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaTempi.size();
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
        listaTempi = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaTempiOrig.size(); i++) {
            String NomeImmagine = listaTempiOrig.get(i).getTempo();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaTempi.add(listaTempiOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.lista_tempo_gestione, null);

        TextView txtSito = (TextView) view.findViewById(R.id.txtTempo);
        txtSito.setText(listaTempi.get(i).getTempo());

        int idTempo = listaTempi.get(i).getIdTempo();

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImpostazioniOrari.getInstance().setIdTempo(idTempo);
                VariabiliStaticheImpostazioniOrari.getInstance().getEdtTempo().setText(listaTempi.get(i).getTempo());
                VariabiliStaticheImpostazioniOrari.getInstance().getLayTempo().setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Gestione Tempo");
                builder.setMessage("Si vuole eliminare il tempo selezionato?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                        ws.EliminaTempo(String.valueOf(idTempo));

                        listaTempi.remove(i);

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
