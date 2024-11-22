package com.looigi.wallpaperchanger2.classePlayer.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaRicerca;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerRicerca extends BaseAdapter {
    private Context context;
    private List<StrutturaRicerca> listaBrani;
    private LayoutInflater inflater;

    public AdapterListenerRicerca(Context applicationContext, List<StrutturaRicerca> Brani) {
        this.context = applicationContext;
        this.listaBrani = Brani;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaBrani.size();
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
        view = inflater.inflate(R.layout.lista_brani_ricercati, null);

        if (i < listaBrani.size()) {
            String Bellezza = listaBrani.get(i).getBellezza();
            String NomeBrano = listaBrani.get(i).getBrano();
            String Artista = listaBrani.get(i).getArtista();
            String Album = listaBrani.get(i).getAlbum();

            // ImageView imgVisualizza = (ImageView) view.findViewById(R.id.imgVisualizza);

            ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);
            Bitmap bitmap = UtilityPlayer.getInstance().PrendeImmagineArtistaACaso(context, Artista);
            if (bitmap == null) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                imgImmagine.setImageBitmap(bitmap);
            } else {
                imgImmagine.setImageBitmap(bitmap);
            }

            imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int idBrano = listaBrani.get(i).getId();

                    db_dati_player db = new db_dati_player(context);
                    boolean esiste = db.EsisteBrano(Artista, Album, NomeBrano);
                    if (esiste) {
                        UtilityPlayer.getInstance().BranoAvanti(context, String.valueOf(idBrano),
                                false, false);
                    } else {
                        UtilityPlayer.getInstance().PrendeBranoInRete(context,
                                String.valueOf(idBrano), false);
                    }
                }
            });

            TextView tBrano = (TextView) view.findViewById(R.id.txtBrano);
            tBrano.setText(NomeBrano);

            TextView tArtista = (TextView) view.findViewById(R.id.txtArtista);
            tArtista.setText(Artista);

            TextView tAlbum = (TextView) view.findViewById(R.id.txtAlbum);
            tAlbum.setText(Album);

            TextView tBellezza = (TextView) view.findViewById(R.id.txtBellezza);
            tBellezza.setText(Bellezza);
        }

        return view;
    }
}
