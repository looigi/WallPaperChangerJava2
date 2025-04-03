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
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaClassifica;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaGiocatori;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.api_football.UtilityApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.classePlayer.Files;

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

        String Nome = listaGiocatori.get(i).getNome();
        String Cognome = listaGiocatori.get(i).getCognome();
        String Ruolo = listaGiocatori.get(i).getRuolo();

        TextView txtNome = view.findViewById(R.id.txtNome);
        txtNome.setText(Nome);

        TextView txtCognome = view.findViewById(R.id.txtCognome);
        txtCognome.setText(Cognome);

        TextView txtRuolo = view.findViewById(R.id.txtRuolo);
        txtRuolo.setText(Ruolo);

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().ApreModifica(context, "GIOCATORI", "UPDATE",
                        "Modifica giocatore", String.valueOf(i));
            }
        });
        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        // Per l'immagine del giocatore su ApiFootball serve l'id
        /* ImageView imgLogo = view.findViewById(R.id.imgLogo);

        String NomeGiocatore = Nome + " " + Cognome;
        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Giocatori";
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeGiocatore)) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeGiocatore);
            imgLogo.setImageBitmap(bmp);
        } else {
            VariabiliStaticheApiFootball.getInstance().setAnnoIniziale(VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato());
            String urlString = "https://v3.football.api-sports.io/teams?" +
                    "league=" + VariabiliStaticheApiFootball.idLegaSerieA + "&" +
                    "season=" + Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "&" +
                    "name=" + NomeGiocatore;

            UtilityApiFootball u = new UtilityApiFootball();
            u.setImg(imgLogo);
            u.setNomeSquadra(NomeGiocatore);
            u.EffettuaChiamata(
                    context,
                    urlString,
                    "Squadra_" + NomeGiocatore + ".json",
                    false,
                    "GIOCATORE"
            );
        } */

        return view;
    }
}
