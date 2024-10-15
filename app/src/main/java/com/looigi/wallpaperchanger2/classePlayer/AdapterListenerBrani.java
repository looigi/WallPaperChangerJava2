package com.looigi.wallpaperchanger2.classePlayer;

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
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerBrani extends BaseAdapter {
    private Context context;
    private List<StrutturaBrano> listaBraniOrig;
    private List<StrutturaBrano> listaBrani;
    private LayoutInflater inflater;

    public AdapterListenerBrani(Context applicationContext, List<StrutturaBrano> Brani) {
        this.context = applicationContext;
        this.listaBraniOrig = Brani;
        this.listaBrani = new ArrayList();
        VariabiliStatichePlayer.getInstance().getTxtQuanteRicerca().setText("Brani rilevati: 0"
                + "/" + Integer.toString(listaBraniOrig.size()));
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

    public void updateData(String Filtro) {
        listaBrani = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaBraniOrig.size(); i++) {
            String NomeBrano = listaBraniOrig.get(i).getBrano();
            String Artista = listaBraniOrig.get(i).getArtista();
            String Album = listaBraniOrig.get(i).getAlbum();
            boolean Ok = false;
            if (!Filtro.isEmpty()) {
                if (NomeBrano.toUpperCase().contains(Filtro.toUpperCase()) ||
                    Artista.toUpperCase().contains(Filtro.toUpperCase()) ||
                    Album.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = true;
                }
            } else {
                Ok = true;
            }
            if (Ok) {
                listaBrani.add(listaBraniOrig.get(i));
            }
        }
        VariabiliStatichePlayer.getInstance().getTxtQuanteRicerca().setText("Brani rilevati: " +
                Integer.toString(listaBrani.size()) + "/" + Integer.toString(listaBraniOrig.size()));

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.lista_brani, null);

        if (i < listaBrani.size()) {
            String NomeBrano = listaBrani.get(i).getBrano();
            String Artista = listaBrani.get(i).getArtista();
            String Album = listaBrani.get(i).getAlbum();

            // ImageView imgVisualizza = (ImageView) view.findViewById(R.id.imgVisualizza);

            ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);
            boolean ok = false;
            for (StrutturaImmagini s : listaBrani.get(i).getImmagini()) {
                String PathImmagine2 = s.getPathImmagine();
                if (Files.getInstance().EsisteFile(PathImmagine2)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(PathImmagine2);
                    if (bitmap.getWidth() > 100 && bitmap.getHeight() > 100) {
                        imgImmagine.setImageBitmap(bitmap);
                        ok = true;
                        break;
                    } else {
                        Files.getInstance().EliminaFileUnico(PathImmagine2);

                        db_dati_player db = new db_dati_player(context);
                        db.EliminaImmagine(listaBrani.get(i), s);
                    }
                }
            }
            if (!ok) {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                imgImmagine.setImageBitmap(bitmap);
            }

            /* int maxId = listaBrani.get(i).getImmagini().size() - 1;
            if (maxId > 0) {
                int numeroImmagine = UtilityPlayer.getInstance().GeneraNumeroRandom(
                        maxId
                );
                String PathImmagine2 = listaBrani.get(i).getImmagini().get(numeroImmagine).getPathImmagine();
                if (Files.getInstance().EsisteFile(PathImmagine2)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(PathImmagine2);
                    imgImmagine.setImageBitmap(bitmap);
                } else {
                    new DownloadImageWP(context, PathImmagine2, imgImmagine).execute(PathImmagine2);
                }
            } else {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
                imgImmagine.setImageBitmap(bitmap);
            } */

            imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    int idBrano = listaBrani.get(i).getIdBrano();
                    UtilityPlayer.getInstance().BranoAvanti(context, String.valueOf(idBrano), false);
                }
            });

            /* TextView Immagine = (TextView) view.findViewById(R.id.txtImmagine);
            Immagine.setText(NomeBrano); */

            TextView tBrano = (TextView) view.findViewById(R.id.txtBrano);
            tBrano.setText(NomeBrano);

            TextView tArtista = (TextView) view.findViewById(R.id.txtArtista);
            tArtista.setText(Artista);

            TextView tAlbum = (TextView) view.findViewById(R.id.txtAlbum);
            tAlbum.setText(Album);
        }

        return view;
    }
}
