package com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.Strutture.Marcatori;
import com.looigi.wallpaperchanger2.Lazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.Lazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;

import java.util.List;

public class AdapterListenerMarcatoriD extends BaseAdapter {
    private Context context;
    private List<Marcatori> lista;
    private LayoutInflater inflter;
    private boolean inCasa;

    public AdapterListenerMarcatoriD(Context applicationContext, List<Marcatori> Lista, boolean Casa) {
        this.context = applicationContext;
        this.lista = Lista;
        this.inCasa = Casa;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return lista.size();
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
        view = inflter.inflate(R.layout.lista_dettaglio_marcatori, null);

        String Nome = lista.get(i).getNome();
        String Cognome = lista.get(i).getCognome();
        String Minuto = String.valueOf(lista.get(i).getMinuto());
        if (!Minuto.isEmpty()) {
            Minuto += "°";
        }

        // TextView txtNome = view.findViewById(R.id.txtNome);
        // txtNome.setText(Nome);

        TextView txtCognome = view.findViewById(R.id.txtCognome);
        txtCognome.setText(Cognome + " " + Nome);

        TextView txtMinuto = view.findViewById(R.id.txtMinuto);
        txtMinuto.setText(Minuto);

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgLogo = view.findViewById(R.id.imgLogo);
        String id = String.valueOf(lista.get(i).getIdApiFootball());

        String NomeGiocatore = Nome + " " + Cognome;
        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Giocatori";
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeGiocatore + ".png")) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeGiocatore + ".png");
            imgLogo.setImageBitmap(bmp);
        } else {
            String Url = "https://media.api-sports.io/football/players/" + id + ".png";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgLogo, Url, NomeGiocatore + ".Jpg", "Giocatori");
        }

        return view;
    }
}

