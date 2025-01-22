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
import com.looigi.wallpaperchanger2.classeOrari.VariabiliStaticheOrari;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.VariabiliStaticheImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.webService.ChiamateWSImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaLavoro;
import com.looigi.wallpaperchanger2.classeOrari.webService.ChiamateWSOrari;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerLavoriGestione extends BaseAdapter {
    private Context context;
    private List<StrutturaLavoro> listaLavori;
    private List<StrutturaLavoro> listaLavoriOrig;
    private LayoutInflater inflter;
    private boolean SoloPerScelta;

    public AdapterListenerLavoriGestione(Context applicationContext, List<StrutturaLavoro> Lavori,
                                         boolean SoloPerScelta) {
        this.context = applicationContext;
        this.listaLavoriOrig = Lavori;
        this.listaLavori = Lavori;
        this.SoloPerScelta = SoloPerScelta;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaLavori.size();
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
        listaLavori = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaLavoriOrig.size(); i++) {
            String NomeImmagine = listaLavoriOrig.get(i).getLavoro();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaLavori.add(listaLavoriOrig.get(i));
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (!SoloPerScelta) {
            view = inflter.inflate(R.layout.lista_lavori_gestione, null);
        } else {
            view = inflter.inflate(R.layout.lista_lavori_gestione_scelta, null);
        }

        TextView txtSito = (TextView) view.findViewById(R.id.txtLavoro);
        txtSito.setText(listaLavori.get(i).getLavoro());

        int idLavoro = listaLavori.get(i).getIdLavoro();

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!SoloPerScelta) {
                    VariabiliStaticheImpostazioniOrari.getInstance().setIdLavoro(idLavoro);
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtLavoro().setText(listaLavori.get(i).getLavoro());
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtIndirizzo().setText(listaLavori.get(i).getIndirizzo());
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtDataInizio().setText(listaLavori.get(i).getDataInizio());
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtDataFine().setText(listaLavori.get(i).getDataFine());
                    VariabiliStaticheImpostazioniOrari.getInstance().getEdtLatLng().setText(listaLavori.get(i).getLatlng());
                    VariabiliStaticheImpostazioniOrari.getInstance().getLayLavoro().setVisibility(LinearLayout.VISIBLE);
                } else {
                    VariabiliStaticheImpostazioniOrari.getInstance().setIdLavoro(idLavoro);
                    ChiamateWSOrari ws = new ChiamateWSOrari(context);
                    ws.RitornaCommesseLavoro(String.valueOf(idLavoro), true, false);
                }
            }
        });

        if (!SoloPerScelta) {
            ImageView imgElimina = view.findViewById(R.id.imgElimina);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Si vuole eliminare il lavoro selezionato?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ChiamateWSImpostazioniOrari ws = new ChiamateWSImpostazioniOrari(context);
                            ws.EliminaLavoro(String.valueOf(idLavoro));

                            listaLavori.remove(i);

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
        }

        return view;
    }
}
