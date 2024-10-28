package com.looigi.wallpaperchanger2.classePlayer.Adapters;

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
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaArtisti;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;

import java.io.File;
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
            String Immagine = PrendeImmagineArtista(NomeArtista);

            ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);
            Bitmap bitmap;
            if (Immagine.isEmpty()) {
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.player);
            } else {
                bitmap = BitmapFactory.decodeFile(Immagine);
            }
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

    private String PrendeImmagineArtista(String Artista) {
        String PathImmagini = Path + Artista + "/ZZZ-ImmaginiArtista";
        File root = new File(PathImmagini);
        File[] list = root.listFiles();

        if (list == null) {
            return "";
        }

        List<String> Nomi = new ArrayList<>();
        for (File f : list) {
            if (f.isDirectory()) {
            } else {
                String Filetto = f.getAbsoluteFile().getPath(); // Questo contiene tutto, sia il path che il nome del file
                Nomi.add(Filetto);
            }
        }

        int n = UtilityPlayer.getInstance().GeneraNumeroRandom(Nomi.size() - 1);

        return Nomi.get(n);
    }
}
