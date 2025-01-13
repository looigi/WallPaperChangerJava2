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
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaCalendario;
import com.looigi.wallpaperchanger2.classeLazio.Strutture.StrutturaClassifica;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeLazio.webService.DownloadImmagineLazio;
import com.looigi.wallpaperchanger2.classePlayer.Files;

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
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadraCasa)) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadraCasa);
            imgCasa.setImageBitmap(bmp);
        } else {
            String url = VariabiliStaticheLazio.UrlMedia + NomeSquadraCasa + ".Jpg";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgCasa, url, NomeSquadraCasa + ".Jpg");
        }

        ImageView imgFuori = view.findViewById(R.id.imgFuori);
        String NomeSquadraFuori = Fuori.toUpperCase().trim();
        if (Files.getInstance().EsisteFile(PathImmagini + "/" + NomeSquadraFuori)) {
            Bitmap bmp = BitmapFactory.decodeFile(PathImmagini + "/" + NomeSquadraFuori);
            imgFuori.setImageBitmap(bmp);
        } else {
            String url = VariabiliStaticheLazio.UrlMedia + NomeSquadraFuori + ".Jpg";
            DownloadImmagineLazio d = new DownloadImmagineLazio();
            d.EsegueChiamata(context, imgFuori, url, NomeSquadraFuori + ".Jpg");
        }

        return view;
    }
}
