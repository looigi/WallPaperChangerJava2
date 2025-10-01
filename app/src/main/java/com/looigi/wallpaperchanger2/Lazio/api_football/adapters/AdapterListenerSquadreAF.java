package com.looigi.wallpaperchanger2.Lazio.api_football.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Lazio.Strutture.StrutturaSquadre;
import com.looigi.wallpaperchanger2.Lazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.Lazio.api_football.UtilityApiFootball;
import com.looigi.wallpaperchanger2.Lazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.Lazio.api_football.strutture.Squadre.TeamResponse;
import com.looigi.wallpaperchanger2.Lazio.webService.ChiamateWSLazio;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;

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

        String NomeSquadra = listaSquadre.get(i).team.name;
        String Logo = listaSquadre.get(i).team.logo;

        TextView txtSquadra = view.findViewById(R.id.txtSquadra);
        txtSquadra.setText(NomeSquadra);

        CheckBox chkFatto = view.findViewById(R.id.chkFatto);

        // ChiamateWSLazio ws = new ChiamateWSLazio(context);
        // ws.RitornaFatte(chkFatto, NomeSquadra);

        String NomeFileFatto = VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                NomeSquadra + ".txt";
        Files.getInstance().CreaCartelle(VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte");
        if (Files.getInstance().EsisteFile(NomeFileFatto)) {
            String Contenuto = Files.getInstance().LeggeFile(
                    VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                            Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/",
                            NomeSquadra + ".txt"
            );
            if (Contenuto.contains("N")) {
                chkFatto.setChecked(false);
            } else {
                chkFatto.setChecked(true);
            }
        } else {
            chkFatto.setChecked(false);
        }

        ImageView imgLogo = view.findViewById(R.id.imgLogo);

        String Squadra = NomeSquadra.toUpperCase().trim();
        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Stemmi";
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + Squadra + ".png")) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + Squadra + ".png");
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
                    "name=" + NomeSquadra;

            UtilityApiFootball u = new UtilityApiFootball();
            u.setImg(imgLogo);
            u.setNomeSquadra(Squadra);
            u.setCartella("Stemmi");
            u.EffettuaChiamata(
                    context,
                    urlString,
                    "Squadra_" + Squadra + ".json",
                    false,
                    "SQUADRA"
            );
        }

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Partite Per Squadra
                int idSquadra = listaSquadre.get(i).team.id;
                VariabiliStaticheApiFootball.getInstance().setIdSquadra(idSquadra);
                VariabiliStaticheApiFootball.getInstance().setNomeSquadraScelta(NomeSquadra);

                String urlString = "https://v3.football.api-sports.io/fixtures?" +
                        "team=" + idSquadra + "&" +
                        "season=" + Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale());
                UtilityApiFootball u = new UtilityApiFootball();
                u.EffettuaChiamata(
                        context,
                        urlString,
                        "Partite_" + idSquadra + "_" + listaSquadre.get(i).team.name + ".json",
                        false,
                        "PARTITE_SQUADRA"
                );
            }
        });

        ImageView imgSalva = view.findViewById(R.id.imgSalvaSquadra);
        List<StrutturaSquadre> squadre = VariabiliStaticheLazio.getInstance().getSquadre();
        if (squadre != null) {
            boolean ok = false;
            // String SquadraConfronto = ((NomeSquadra.replace("AC ", "")).replace("AS ", "")).toUpperCase().trim();
            String SquadraConfronto = NomeSquadra.toUpperCase().trim();

            for (StrutturaSquadre s : squadre) {
                // String Squadra = ((s.getSquadra().replace("AC ", "")).replace("AS ", "")).toUpperCase().trim();
                String sSquadra = s.getSquadra().toUpperCase().trim();
                if (sSquadra.equals(SquadraConfronto)) {
                    ok = true;
                    break;
                }
            }
            if (ok) {
                imgSalva.setVisibility(LinearLayout.GONE);
            } else {
                imgSalva.setVisibility(LinearLayout.VISIBLE);
            }
        }

        imgSalva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSLazio ws = new ChiamateWSLazio(context);
                ws.AggiungeSquadra(
                        VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato(),
                        NomeSquadra,
                        1
                );

                imgSalva.setVisibility(LinearLayout.GONE);
            }
        });

        return view;
    }
}

