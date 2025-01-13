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
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.classePlayer.Files;

import java.io.File;
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

        TextView txtSquadra = view.findViewById(R.id.txtSquadra);
        txtSquadra.setText(Squadra);

        ImageView imgLogo = view.findViewById(R.id.imgLogo);

        String NomeSquadra = Squadra.toUpperCase().trim();
        String PathImmagini = VariabiliStaticheLazio.getInstance().getPathLazio() + "/Stemmi";
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadra)) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadra);
            imgLogo.setImageBitmap(bmp);
        } else {
            String url = VariabiliStaticheLazio.UrlMedia + NomeSquadra + ".Jpg";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgLogo, url, NomeSquadra + ".Jpg");
        }

        return view;
    }
}
