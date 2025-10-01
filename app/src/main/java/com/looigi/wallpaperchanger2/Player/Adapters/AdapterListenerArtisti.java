package com.looigi.wallpaperchanger2.Player.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaArtisti;
import com.looigi.wallpaperchanger2.Player.UtilityPlayer;
import com.looigi.wallpaperchanger2.Player.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.Player.WebServices.ChiamateWsPlayer;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerArtisti extends BaseAdapter {
    private Context context;
    private List<StrutturaArtisti> listaArtistiOrig;
    private List<StrutturaArtisti> listaArtisti;
    private LayoutInflater inflater;
    private String Filtro;
    private String Path;

    public AdapterListenerArtisti(Context applicationContext, List<StrutturaArtisti> Artisti) {
        this.context = applicationContext;
        this.listaArtistiOrig = Artisti;
        this.listaArtisti = new ArrayList<>();
        inflater = (LayoutInflater.from(applicationContext));
        this.Path = context.getFilesDir() + "/Player/ImmaginiMusica/";

        if (listaArtistiOrig != null) {
            if (VariabiliStatichePlayer.getInstance().getTxtQuanteRicerca() != null) {
                VariabiliStatichePlayer.getInstance().getTxtQuanteRicerca().setText("Artisti rilevati: " +
                        "0/" + Integer.toString(listaArtistiOrig.size()));
            }
        }
    }

    @Override
    public int getCount() {
        return listaArtisti.size();
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
        listaArtisti = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaArtistiOrig.size(); i++) {
            String NomeArtista = listaArtistiOrig.get(i).getNomeArtista();
            boolean Ok = false;
            if (!Filtro.isEmpty()) {
                if (NomeArtista.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = true;
                }
            } else {
                Ok = true;
            }
            if (Ok) {
                listaArtisti.add(listaArtistiOrig.get(i));
            }
        }
        if (VariabiliStatichePlayer.getInstance().getTxtQuanteRicerca() != null) {
            VariabiliStatichePlayer.getInstance().getTxtQuanteRicerca().setText("Artisti rilevati: " +
                    Integer.toString(listaArtisti.size()) + "/" + Integer.toString(listaArtistiOrig.size()));
        }
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.lista_artisti, null);

        if (i < listaArtisti.size()) {
            String NomeArtista = listaArtisti.get(i).getNomeArtista();
            ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);

            /* String Immagine = UtilityPlayer.getInstance().PrendeImmagineArtistaACaso(context, NomeArtista);

            Bitmap bitmap;
            if (Immagine.isEmpty()) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
            } else {
                bitmap = BitmapFactory.decodeFile(Immagine);
            }
            imgImmagine.setImageBitmap(bitmap); */

            Bitmap bitmap = UtilityPlayer.getInstance().PrendeImmagineArtistaACaso(
                    context, NomeArtista);
            imgImmagine.setImageBitmap(bitmap);

            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String Artista = listaArtisti.get(i).getNomeArtista();

                    ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                    ws.RitornaListaAlbum(Artista, "");

                    VariabiliStatichePlayer.getInstance().getLstAlbum().setAdapter(null);
                    VariabiliStatichePlayer.getInstance().getLstBrani().setAdapter(null);
                }
            });

            TextView tArtista = (TextView) view.findViewById(R.id.txtArtista);
            tArtista.setText(NomeArtista);
        }

        return view;
    }
}
