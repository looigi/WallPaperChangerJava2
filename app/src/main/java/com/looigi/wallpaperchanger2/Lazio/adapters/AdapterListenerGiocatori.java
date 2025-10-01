package com.looigi.wallpaperchanger2.Lazio.adapters;

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
import com.looigi.wallpaperchanger2.Lazio.Strutture.StrutturaGiocatori;
import com.looigi.wallpaperchanger2.Lazio.UtilityLazio;
import com.looigi.wallpaperchanger2.Lazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.Lazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;

import java.util.List;

public class AdapterListenerGiocatori extends BaseAdapter {
    private Context context;
    private List<StrutturaGiocatori> listaGiocatori;
    private LayoutInflater inflter;

    public AdapterListenerGiocatori(Context applicationContext, List<StrutturaGiocatori> Giocatori) {
        this.context = applicationContext;
        this.listaGiocatori = Giocatori;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaGiocatori.size();
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
        view = inflter.inflate(R.layout.lista_giocatori, null);

        String idGiocatore = String.valueOf(listaGiocatori.get(i).getIdGiocatore());
        String Nome = listaGiocatori.get(i).getNome();
        String Cognome = listaGiocatori.get(i).getCognome();
        String Ruolo = listaGiocatori.get(i).getRuolo();
        String idApiFootball = String.valueOf(listaGiocatori.get(i).getIdApiFootball());

        TextView txtNome = view.findViewById(R.id.txtNome);
        txtNome.setText(Nome);

        TextView txtCognome = view.findViewById(R.id.txtCognome);
        txtCognome.setText(Cognome);

        TextView txtRuolo = view.findViewById(R.id.txtRuolo);
        txtRuolo.setText(Ruolo);

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazio.getInstance().setIdApiFootballPerModifica(idApiFootball);
                VariabiliStaticheLazio.getInstance().setIdGiocatorePerModifica(idGiocatore);

                UtilityLazio.getInstance().ApreModifica(context, "GIOCATORI", "UPDATE",
                        "Modifica giocatore", String.valueOf(i));
            }
        });
        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        ImageView imgLogo = view.findViewById(R.id.imgLogo);

        String NomeGiocatore = Nome + " " + Cognome;
        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Giocatori";
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeGiocatore + ".png")) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeGiocatore + ".png");
            imgLogo.setImageBitmap(bmp);
        } else {
            String Url = "https://media.api-sports.io/football/players/" + idApiFootball + ".png";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgLogo, Url, NomeGiocatore + ".Jpg", "Giocatori");
        }

        return view;
    }
}
