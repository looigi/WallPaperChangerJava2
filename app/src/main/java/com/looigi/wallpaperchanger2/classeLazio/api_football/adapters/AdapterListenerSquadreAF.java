package com.looigi.wallpaperchanger2.classeLazio.api_football.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.api_football.UtilityApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Squadre.TeamResponse;
import com.looigi.wallpaperchanger2.classeOrari.webService.DownloadImmagineOrari;

import java.util.List;

public class AdapterListenerSquadreAF extends BaseAdapter {
    private Context context;
    private List<TeamResponse> listaSquadre;
    private LayoutInflater inflter;

    public AdapterListenerSquadreAF(Context applicationContext, List<TeamResponse> Squadre) {
        this.context = applicationContext;
        this.listaSquadre = Squadre;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaSquadre.size();
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
        view = inflter.inflate(R.layout.lista_squadre_af, null);

        String Nome = listaSquadre.get(i).team.name;
        String Logo = listaSquadre.get(i).team.logo;

        TextView txtSquadra = view.findViewById(R.id.txtSquadra);
        txtSquadra.setText(Nome);

        ImageView imgLogo = view.findViewById(R.id.imgLogo);

        DownloadImmagineOrari d = new DownloadImmagineOrari();
        d.EsegueChiamata(context, imgLogo, Logo);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Partite Per Squadra
                int idSquadra = listaSquadre.get(i).team.id;
                VariabiliStaticheApiFootball.getInstance().setIdSquadra(idSquadra);
                String urlString = "https://v3.football.api-sports.io/fixtures?" +
                        "team=" + idSquadra + "&" +
                        "season=" + VariabiliStaticheApiFootball.getInstance().getAnnoScelto();
                UtilityApiFootball.getInstance().EffettuaChiamata(
                        context,
                        urlString,
                        "Partite_" + idSquadra + "_" + listaSquadre.get(i).team.name + ".json",
                        false,
                        "PARTITE_SQUADRA"
                );
            }
        });

        return view;
    }
}

