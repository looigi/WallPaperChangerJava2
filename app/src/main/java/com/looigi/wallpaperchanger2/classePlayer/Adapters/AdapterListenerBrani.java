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
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaBrano;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaImmagini;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerBrani extends BaseAdapter {
    private Context context;
    private List<StrutturaBrano> listaBraniOrig;
    private List<StrutturaBrano> listaBrani;
    private LayoutInflater inflater;
    private String Filtro;

    public AdapterListenerBrani(Context applicationContext, List<StrutturaBrano> Brani) {
        this.context = applicationContext;
        this.listaBraniOrig = Brani;
        this.listaBrani = new ArrayList();
        long sO = VariabiliStatichePlayer.getInstance().getSpazioOccupato() / 1024L;
        long sT = VariabiliStatichePlayer.getInstance().getSpazioMassimo() / 1024L;
        VariabiliStatichePlayer.getInstance().getTxtQuanteRicerca().setText("Brani rilevati: 0"
                + "/" + Integer.toString(listaBraniOrig.size()) +
                " Spazio: " + sO + "/" + sT);
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
        this.Filtro = Filtro;
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
        long sO = VariabiliStatichePlayer.getInstance().getSpazioOccupato() / 1024L;
        long sT = VariabiliStatichePlayer.getInstance().getSpazioMassimo() / 1024L;
        VariabiliStatichePlayer.getInstance().getTxtQuanteRicerca().setText("Brani rilevati: " +
                Integer.toString(listaBrani.size()) + "/" + Integer.toString(listaBraniOrig.size()) +
                " Spazio: " + sO + "/" + sT);

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
                        db.EliminaImmagineFisica(listaBrani.get(i).getArtista(),
                                s.getPathImmagine());
                        db.ChiudeDB();
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
                    UtilityPlayer.getInstance().BranoAvanti(context, String.valueOf(idBrano), false, false);
                }
            });

            ImageView imgElimina = view.findViewById(R.id.imgEliminaBrano);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("LooWebPlayer");
                    builder.setMessage("Si vuole eliminare il brano ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String Path = listaBrani.get(i).getPathBrano();
                            if (Files.getInstance().EliminaFileUnico(Path)) {
                                db_dati_player db = new db_dati_player(context);
                                db.EliminaBrano(String.valueOf(listaBrani.get(i).getIdBrano()));
                                listaBrani.remove(i);
                                updateData(Filtro);
                                db.ChiudeDB();
                            } else {
                                UtilitiesGlobali.getInstance().ApreToast(context, "Files non eliminato");
                            }
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
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
