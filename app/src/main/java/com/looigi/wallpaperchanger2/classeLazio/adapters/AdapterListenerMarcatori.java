package com.looigi.wallpaperchanger2.classeLazio.adapters;

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
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaMarcatori;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.classePlayer.Files;

import java.util.List;

public class AdapterListenerMarcatori extends BaseAdapter {
    private Context context;
    private List<StrutturaMarcatori> lista;
    private LayoutInflater inflter;

    public AdapterListenerMarcatori(Context applicationContext, List<StrutturaMarcatori> Lista) {
        this.context = applicationContext;
        this.lista = Lista;
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
        view = inflter.inflate(R.layout.lista_marcatori, null);

        String Nome = lista.get(i).getNome();
        String Cognome = lista.get(i).getCognome();
        String Squadra = lista.get(i).getSquadra();
        String Goals = String.valueOf(lista.get(i).getGoals());
        String id = String.valueOf(lista.get(i).getIdApiFootball());

        TextView txtNome = view.findViewById(R.id.txtNome);
        txtNome.setText(Nome);

        TextView txtCognome = view.findViewById(R.id.txtCognome);
        txtCognome.setText(Cognome);

        TextView txtSquadra = view.findViewById(R.id.txtSquadra);
        txtSquadra.setText(Squadra);

        TextView txtGoals = view.findViewById(R.id.txtGoals);
        txtGoals.setText(Goals);
        ImageView imgLogo = view.findViewById(R.id.imgLogo);

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

