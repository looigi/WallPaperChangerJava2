package com.looigi.wallpaperchanger2.classeLazio.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaMercato;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.webService.ChiamateWSLazio;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.webService.ChiamateWSImpostazioniOrari;

import java.util.ArrayList;
import java.util.List;

import kotlin.random.Random;

public class AdapterListenerMercato extends BaseAdapter {
    private Context context;
    private List<StrutturaMercato> listaMercato;
    private LayoutInflater inflter;

    public AdapterListenerMercato(Context applicationContext, List<StrutturaMercato> Mercato) {
        this.context = applicationContext;
        /* List<StrutturaMercato> MercatoOrd = new ArrayList<>();
        for (int i = Mercato.size() - 1; i >= 0; i--) {
            MercatoOrd.add(Mercato.get(i));
        } */
        this.listaMercato = Mercato; // Ord;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaMercato.size();
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
        view = inflter.inflate(R.layout.lista_mercato, null);

        String Data = listaMercato.get(i).getData();
        String Nominativo = listaMercato.get(i).getNominativo();
        String Fonte = listaMercato.get(i).getFonte();
        String Stato = listaMercato.get(i).getStato();

        TextView txtData = view.findViewById(R.id.txtData);
        txtData.setText(Data);

        TextView txtNominativo = view.findViewById(R.id.txtNominativo);
        txtNominativo.setText(Nominativo);

        TextView txtFonte = view.findViewById(R.id.txtFonte);
        txtFonte.setText(Fonte);

        TextView txtStato = view.findViewById(R.id.txtStato);
        txtStato.setText(Stato);

        switch (Stato) {
            case "Sfumato":
                view.setBackgroundColor(Color.rgb(255,150, 150));
                break;
            case "Confermato":
                view.setBackgroundColor(Color.rgb(150, 255,150));
                break;
            default:
                view.setBackgroundColor(Color.TRANSPARENT);
                break;
        }

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().ApreModifica(context, "MERCATO", "UPDATE",
                        "Modifica movimento di mercato", String.valueOf(i));
            }
        });
        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare il lavoro selezionato?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheLazio.getInstance().setIdPerOperazione(listaMercato.get(i).getProgressivo());

                        ChiamateWSLazio ws = new ChiamateWSLazio(context);
                        ws.EliminaMercato();

                        listaMercato.remove(i);

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
