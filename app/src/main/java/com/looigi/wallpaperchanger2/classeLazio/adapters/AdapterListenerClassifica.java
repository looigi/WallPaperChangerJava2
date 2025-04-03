package com.looigi.wallpaperchanger2.classeLazio.adapters;

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
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaClassifica;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.api_football.UtilityApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.api_football.VariabiliStaticheApiFootball;
import com.looigi.wallpaperchanger2.classeLazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.Commesse.MainCommessa;
import com.looigi.wallpaperchanger2.classeOrari.impostazioni.VariabiliStaticheImpostazioniOrari;
import com.looigi.wallpaperchanger2.classeOrari.strutture.StrutturaCommesse;
import com.looigi.wallpaperchanger2.classePlayer.Files;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerClassifica extends BaseAdapter {
    private Context context;
    private List<StrutturaClassifica> listaClassifica;
    private LayoutInflater inflter;

    public AdapterListenerClassifica(Context applicationContext, List<StrutturaClassifica> Classifica) {
        this.context = applicationContext;
        this.listaClassifica = Classifica;
        inflter = (LayoutInflater.from(applicationContext));

        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Stemmi";
        Files.getInstance().CreaCartelle(PathImmagini);
    }

    @Override
    public int getCount() {
        return listaClassifica.size();
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
        view = inflter.inflate(R.layout.lista_classifica, null);

        String Squadra = listaClassifica.get(i).getSquadra();
        String Punti = String.valueOf(listaClassifica.get(i).getPunti());
        String Giocate = String.valueOf(listaClassifica.get(i).getGiocate());
        String MediaInglese = String.valueOf(listaClassifica.get(i).getMediaInglese());

        String Vinte = "";
        String Pareggiate = "";
        String Perse = "";
        String GF = "";
        String GS = "";

        switch (VariabiliStaticheLazio.getInstance().getModalitaClassifica()) {
            case 1:
                Vinte = String.valueOf(listaClassifica.get(i).getVinte());
                Pareggiate = String.valueOf(listaClassifica.get(i).getPareggiate());
                Perse = String.valueOf(listaClassifica.get(i).getPerse());
                GF = String.valueOf(listaClassifica.get(i).getGoalFatti());
                GS = String.valueOf(listaClassifica.get(i).getGoalSubiti());
                break;
            case 2:
                Vinte = String.valueOf(listaClassifica.get(i).getVinteCasa());
                Pareggiate = String.valueOf(listaClassifica.get(i).getPareggiateCasa());
                Perse = String.valueOf(listaClassifica.get(i).getPerseCasa());
                GF = String.valueOf(listaClassifica.get(i).getGoalFattiCasa());
                GS = String.valueOf(listaClassifica.get(i).getGoalSubitiCasa());
                break;
            case 3:
                Vinte = String.valueOf(listaClassifica.get(i).getVinteFuori());
                Pareggiate = String.valueOf(listaClassifica.get(i).getPareggiateFuori());
                Perse = String.valueOf(listaClassifica.get(i).getPerseFuori());
                GF = String.valueOf(listaClassifica.get(i).getGoalFattiFuori());
                GS = String.valueOf(listaClassifica.get(i).getGoalSubitiFuori());
                break;
        }

        TextView txtSquadra = view.findViewById(R.id.txtSquadra);
        txtSquadra.setText(Squadra);

        TextView txtPunti = view.findViewById(R.id.txtPunti);
        txtPunti.setText(Punti);

        TextView txtGiocate = view.findViewById(R.id.txtGiocate);
        txtGiocate.setText(Giocate);

        TextView txtVinte = view.findViewById(R.id.txtVinte);
        txtVinte.setText(Vinte);

        TextView txtPareggiate = view.findViewById(R.id.txtPareggiate);
        txtPareggiate.setText(Pareggiate);

        TextView txtPerse = view.findViewById(R.id.txtPerse);
        txtPerse.setText(Perse);

        TextView txtGF = view.findViewById(R.id.txtGF);
        txtGF.setText(GF);

        TextView txtGS = view.findViewById(R.id.txtGS);
        txtGS.setText(GS);

        TextView txtMedia = view.findViewById(R.id.txtMedia);
        txtMedia.setText(MediaInglese);

        ImageView imgLogo = view.findViewById(R.id.imgLogo);

        String NomeSquadra = Squadra.toUpperCase().trim();
        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Stemmi";
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadra)) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadra);
            imgLogo.setImageBitmap(bmp);
        } else {
            VariabiliStaticheApiFootball.getInstance().setAnnoIniziale(VariabiliStaticheLazio.getInstance().getIdAnnoSelezionato());

            String urlString = "https://v3.football.api-sports.io/teams?" +
                    "league=" + VariabiliStaticheApiFootball.idLegaSerieA + "&" +
                    "season=" + VariabiliStaticheApiFootball.getInstance().getAnnoIniziale() + "&" +
                    "name=" + Squadra;

            UtilityApiFootball u = new UtilityApiFootball();
            u.setImg(imgLogo);
            u.setNomeSquadra(NomeSquadra);
            u.EffettuaChiamata(
                    context,
                    urlString,
                    "Squadra_" + NomeSquadra + ".json",
                    false,
                    "SQUADRA"
            );
            /* String url = VariabiliStaticheLazio.UrlMedia + NomeSquadra + ".Jpg";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgLogo, url, NomeSquadra + ".Jpg"); */
        }

        return view;
    }
}
