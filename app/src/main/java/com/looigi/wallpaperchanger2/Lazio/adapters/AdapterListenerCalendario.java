package com.looigi.wallpaperchanger2.Lazio.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.MainDettaglioPartita;
import com.looigi.wallpaperchanger2.Lazio.DettaglioPartita.VariabiliStaticheLazioDettaglio;
import com.looigi.wallpaperchanger2.Lazio.Strutture.StrutturaCalendario;
import com.looigi.wallpaperchanger2.Lazio.UtilityLazio;
import com.looigi.wallpaperchanger2.Lazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.Lazio.api_football.UtilityApiFootball;
import com.looigi.wallpaperchanger2.Lazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;

import java.util.List;

public class AdapterListenerCalendario extends BaseAdapter {
    private Context context;
    private List<StrutturaCalendario> listaCalendario;
    private LayoutInflater inflter;

    public AdapterListenerCalendario(Context applicationContext, List<StrutturaCalendario> Calendario) {
        this.context = applicationContext;
        this.listaCalendario = Calendario;
        inflter = (LayoutInflater.from(applicationContext));

        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Stemmi";
        Files.getInstance().CreaCartelle(PathImmagini);
    }

    @Override
    public int getCount() {
        return listaCalendario.size();
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
        view = inflter.inflate(R.layout.lista_calendario, null);

        String Casa = listaCalendario.get(i).getCasa();
        String Fuori = listaCalendario.get(i).getFuori();
        String Risultato = listaCalendario.get(i).getRisultato1() + "-" + listaCalendario.get(i).getRisultato2();

        TextView txtCasa = view.findViewById(R.id.txtCasa);
        txtCasa.setText(Casa);

        TextView txtFuori = view.findViewById(R.id.txtFuori);
        txtFuori.setText(Fuori);

        TextView txtRisultato = view.findViewById(R.id.txtRisultato);
        txtRisultato.setText(Risultato);

        ImageView imgCasa = view.findViewById(R.id.imgCasa);

        String NomeSquadraCasa = Casa.toUpperCase().trim();
        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Stemmi";
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadraCasa + ".png")) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadraCasa + ".png");
            imgCasa.setImageBitmap(bmp);
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
                    "name=" + Casa;

            UtilityApiFootball u = new UtilityApiFootball();
            u.setImg(imgCasa);
            u.setCartella("Stemmi");
            u.setNomeSquadra(NomeSquadraCasa);
            u.EffettuaChiamata(
                    context,
                    urlString,
                    "Squadra_" + NomeSquadraCasa + ".json",
                    false,
                    "SQUADRA"
            );
            /* String url = VariabiliStaticheLazio.UrlMedia + NomeSquadraCasa + ".Jpg";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgCasa, url, NomeSquadraCasa + ".Jpg"); */
        }

        ImageView imgFuori = view.findViewById(R.id.imgFuori);
        String NomeSquadraFuori = Fuori.toUpperCase().trim();
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadraFuori + ".png")) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadraFuori + ".png");
            imgFuori.setImageBitmap(bmp);
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
                    "name=" + Fuori;

            UtilityApiFootball u = new UtilityApiFootball();
            u.setImg(imgFuori);
            u.setCartella("Stemmi");
            u.setNomeSquadra(NomeSquadraFuori);
            u.EffettuaChiamata(
                    context,
                    urlString,
                    "Squadra_" + NomeSquadraFuori + ".json",
                    false,
                    "SQUADRA"
            );
            /* String url = VariabiliStaticheLazio.UrlMedia + NomeSquadraFuori + ".Jpg";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgFuori, url, NomeSquadraFuori + ".Jpg"); */
        }

        ImageView imgVisualizza = view.findViewById(R.id.imgVisualizza);
        imgVisualizza.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheLazioDettaglio.getInstance().setIdPartita(listaCalendario.get(i).getIdPartita());
                VariabiliStaticheLazioDettaglio.getInstance().setIdSquadraCasa(listaCalendario.get(i).getIdSquadraCasa());
                VariabiliStaticheLazioDettaglio.getInstance().setIdSquadraFuori(listaCalendario.get(i).getIdSquadraFuori());
                VariabiliStaticheLazioDettaglio.getInstance().setCasa(listaCalendario.get(i).getCasa());
                VariabiliStaticheLazioDettaglio.getInstance().setFuori(listaCalendario.get(i).getFuori());
                String ris = listaCalendario.get(i).getRisultato1() + "-" + listaCalendario.get(i).getRisultato2();
                VariabiliStaticheLazioDettaglio.getInstance().setData(listaCalendario.get(i).getDataPartita());
                VariabiliStaticheLazioDettaglio.getInstance().setRisultato(ris);

                Intent iP = new Intent(context, MainDettaglioPartita.class);
                context.startActivity(iP);
            }
        });
        ImageView imgModifica = view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityLazio.getInstance().ApreModifica(context, "CALENDARIO", "UPDATE",
                        "Modifica partita", String.valueOf(i));
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
