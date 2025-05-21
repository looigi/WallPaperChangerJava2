package com.looigi.wallpaperchanger2.classeLazio.api_football.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeLazio.api_football.UtilityApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.strutture.Partite.FixtureData;
import com.looigi.wallpaperchanger2.classeOrari.webService.DownloadImmagineOrari;
import com.looigi.wallpaperchanger2.utilities.Files;

import java.util.List;

public class AdapterListenerPartiteAF extends BaseAdapter {
    private Context context;
    private List<FixtureData> listaPartite;
    private LayoutInflater inflter;

    public AdapterListenerPartiteAF(Context applicationContext, List<FixtureData> Partite) {
        this.context = applicationContext;
        this.listaPartite = Partite;
        VariabiliStaticheApiFootball.getInstance().setQuantePartite(Partite.size());
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaPartite.size();
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
        view = inflter.inflate(R.layout.lista_partite_af, null);

        String NomeSquadra = VariabiliStaticheApiFootball.getInstance().getNomeSquadraScelta();

        String NomeCasa = listaPartite.get(i).teams.home.name;
        String NomeFuori = listaPartite.get(i).teams.away.name;
        String LogoCasa = listaPartite.get(i).teams.home.logo;
        String LogoFuori = listaPartite.get(i).teams.away.logo;

        String Risultato = listaPartite.get(i).score.fulltime.home + "-" + listaPartite.get(i).score.fulltime.away;

        String d = listaPartite.get(i).fixture.date;
        String[] data = d.split("T");
        String[] o = data[1].split("\\+");
        String ora = o[0];

        String Dettaglio = "Data: " + data[0] + " " + ora + " - Arbitro: " + listaPartite.get(i).fixture.referee;

        String Lega = listaPartite.get(i).league.name + " " + listaPartite.get(i).league.round;

        TextView txtSquadraCasa = view.findViewById(R.id.txtSquadraCasa);
        txtSquadraCasa.setText(NomeCasa);

        TextView txtSquadraFuori = view.findViewById(R.id.txtSquadraFuori);
        txtSquadraFuori.setText(NomeFuori);

        TextView txtRisultato = view.findViewById(R.id.txtRisultato);
        txtRisultato.setText(Risultato);

        TextView txtDettaglio = view.findViewById(R.id.txtDettaglio);
        txtDettaglio.setText(Dettaglio);

        TextView txtLega = view.findViewById(R.id.txtLega);
        txtLega.setText(Lega);

        ImageView imgLogoCasa = view.findViewById(R.id.imgLogoCasa);
        ImageView imgLogoFuori = view.findViewById(R.id.imgLogoFuori);

        /* DownloadImmagineOrari dC = new DownloadImmagineOrari();
        dC.EsegueChiamata(context, imgLogoCasa, LogoCasa);

        DownloadImmagineOrari dF = new DownloadImmagineOrari();
        dF.EsegueChiamata(context, imgLogoFuori, LogoFuori); */

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int idPartita = listaPartite.get(i).fixture.id;

                VariabiliStaticheApiFootball.getInstance().setIdPartita(idPartita);
                UtilityApiFootball u = new UtilityApiFootball();
                u.CaricaPartita(context, idPartita);
            }
        });

        ImageView imgSalva = view.findViewById(R.id.imgSalvaPartita);
        imgSalva.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int idPartita = listaPartite.get(i).fixture.id;

                VariabiliStaticheApiFootball.getInstance().setStaSalvandoPartita(true);

                VariabiliStaticheApiFootball.getInstance().setIdPartita(idPartita);
                VariabiliStaticheApiFootball.getInstance().setIdPartitaDaSalvare(i);
                UtilityApiFootball u = new UtilityApiFootball();
                u.CaricaPartita(context, idPartita);
            }
        });

        CheckBox chkFatto = view.findViewById(R.id.chkFatta);
        // chkFatto.setEnabled(false);
        String NomeFileFatto = VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/" +
                NomeSquadra + ".txt";
        Files.getInstance().CreaCartelle(VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte");
        if (Files.getInstance().EsisteFile(NomeFileFatto)) {
            String Contenuto = Files.getInstance().LeggeFile(
                    VariabiliStaticheApiFootball.getInstance().getPathApiFootball() + "/Fatte/" +
                            Integer.toString(VariabiliStaticheApiFootball.getInstance().getAnnoIniziale()) + "/",
                    NomeSquadra + ".txt");
            boolean Fatta = false;
            if (!Contenuto.isEmpty()) {
                String c = Contenuto.substring(i, i + 1);
                if (c.equals("S")) {
                    Fatta = true;
                }
            }
            if (Fatta) {
                chkFatto.setChecked(true);
            } else {
                chkFatto.setChecked(false);
            }
        } else {
            chkFatto.setChecked(false);
        }
        chkFatto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityApiFootball u = new UtilityApiFootball();
                u.AggiornaFileFatti(NomeSquadra, i, chkFatto.isChecked());
            }
        });

        return view;
    }
}

