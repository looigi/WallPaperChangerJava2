package com.looigi.wallpaperchanger2.classeLazio.api_football.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.Cards;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.Games;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.Goals;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.Player;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.PlayerStatistics;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Giocatori.Statistics;
import com.looigi.wallpaperchanger2.classeOrari.webService.DownloadImmagineOrari;
import com.looigi.wallpaperchanger2.utilities.VariabiliStaticheStart;

import java.util.List;

public class AdapterListenerGiocatoriPartitaAF extends BaseAdapter {
    private Context context;
    private List<PlayerStatistics> listaGiocatori;
    private LayoutInflater inflter;

    public AdapterListenerGiocatoriPartitaAF(Context applicationContext, List<PlayerStatistics> Giocatori) {
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
        view = inflter.inflate(R.layout.lista_giocatori_af, null);

        Player giocatore = listaGiocatori.get(i).player;
        String Nome = giocatore.name;
        String foto = giocatore.photo;
        Statistics statistics = listaGiocatori.get(i).statistics.get(0);
        Games g = statistics.games;
        int minutiGiocati = g.minutes;
        Cards cartellini = statistics.cards;
        int cartelliniGialli = cartellini.yellow;
        int cartelliniRossi = cartellini.red;
        Goals goals = statistics.goals;
        int goalFatti = 0;
        if (goals.total != null) {
            goalFatti = goals.total;
        };

        TextView txtGiocatore = view.findViewById(R.id.txtGiocatore);
        txtGiocatore.setText(Nome);

        TextView txtMinuti = view.findViewById(R.id.txtMinuti);
        txtMinuti.setText(Integer.toString(minutiGiocati));

        ImageView imgGoals = view.findViewById(R.id.imgGoals);
        if (goalFatti > 0) {
            imgGoals.setVisibility(LinearLayout.VISIBLE);
        } else {
            imgGoals.setVisibility(LinearLayout.GONE);
        }

        ImageView imgCartellino = view.findViewById(R.id.imgCartellino);

        Bitmap cartellino;

        if (cartelliniGialli > 0 && cartelliniRossi == 0) {
            cartellino = BitmapFactory.decodeResource(context.getResources(), R.drawable.giallo);
        } else {
            if (cartelliniGialli == 0 && cartelliniRossi > 0) {
                cartellino = BitmapFactory.decodeResource(context.getResources(), R.drawable.rosso);
            } else {
                if (cartelliniGialli > 0 && cartelliniRossi > 0) {
                    cartellino = BitmapFactory.decodeResource(context.getResources(), R.drawable.giallorosso);
                } else {
                    cartellino = null;
                }
            }
        }
        imgCartellino.setImageBitmap(cartellino);

        ImageView imgLogo = view.findViewById(R.id.imgFoto);

        // DownloadImmagineOrari d = new DownloadImmagineOrari();
        // d.EsegueChiamata(context, imgLogo, foto);

        return view;
    }
}

