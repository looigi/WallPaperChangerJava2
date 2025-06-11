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
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaSquadre;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.api_football.UtilityApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.utilities.Files;

import java.util.List;

public class AdapterListenerSquadre extends BaseAdapter {
    private Context context;
    private List<StrutturaSquadre> listaSquadre;
    private LayoutInflater inflter;

    public AdapterListenerSquadre(Context applicationContext, List<StrutturaSquadre> Squadre) {
        this.context = applicationContext;
        this.listaSquadre = Squadre;
        inflter = (LayoutInflater.from(applicationContext));

        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Stemmi";
        Files.getInstance().CreaCartelle(PathImmagini);
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
        view = inflter.inflate(R.layout.lista_squadre, null);

        String Squadra = listaSquadre.get(i).getSquadra();
        String idSquadra = String.valueOf(listaSquadre.get(i).getIdSquadra());

        TextView txtSquadra = view.findViewById(R.id.txtSquadra);
        txtSquadra.setText(Squadra);

        ImageView imgLogo = view.findViewById(R.id.imgLogo);

        String NomeSquadra = Squadra.toUpperCase().trim();
        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Stemmi";
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadra + ".png")) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadra + ".png");
            imgLogo.setImageBitmap(bmp);
        } else {
            String Anno = VariabiliStaticheLazio.getInstance().getAnnoSelezionato();
            String[] a = Anno.split("-");
            VariabiliStaticheApiFootball.getInstance().setAnnoIniziale(
                    Integer.parseInt(a[0])
            );
            if (VariabiliStaticheApiFootball.getInstance().getPathApiFootball() == null) {
                VariabiliStaticheApiFootball.getInstance().setPathApiFootball(
                        context.getFilesDir() + "/ApiFootball"
                );
            }

            String urlString = "https://v3.football.api-sports.io/teams?" +
                    "league=" + VariabiliStaticheApiFootball.idLegaSerieA + "&" +
                    "season=" + VariabiliStaticheApiFootball.getInstance().getAnnoIniziale() + "&" +
                    "name=" + Squadra;

            UtilityApiFootball u = new UtilityApiFootball();
            u.setImg(imgLogo);
            u.setNomeSquadra(NomeSquadra);
            u.setCartella("Stemmi");
            u.EffettuaChiamata(
                    context,
                    urlString,
                    "Squadra_" + NomeSquadra + ".json",
                    false,
                    "SQUADRA"
            );
        }

        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().ApreModifica(context, "SQUADRE", "UPDATE",
                        "Modifica squadra", Squadra);
            }
        });
        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            }
        });

        return view;
    }
}
