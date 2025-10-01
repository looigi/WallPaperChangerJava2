package com.looigi.wallpaperchanger2.Player.Adapters;

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
import com.looigi.wallpaperchanger2.Player.Strutture.StrutturaAlbum;
import com.looigi.wallpaperchanger2.Player.UtilityPlayer;
import com.looigi.wallpaperchanger2.Player.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.Player.WebServices.ChiamateWsPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterListenerAlbum extends BaseAdapter {
    private Context context;
    private List<StrutturaAlbum> listaAlbum;
    private LayoutInflater inflater;
    private String Filtro;
    private String Path;

    public AdapterListenerAlbum(Context applicationContext, List<StrutturaAlbum> Artisti) {
        this.context = applicationContext;
        this.listaAlbum = Artisti;
        this.Path = context.getFilesDir() + "/Player/ImmaginiMusica/";
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaAlbum.size();
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
        view = inflater.inflate(R.layout.lista_album, null);

        if (i < listaAlbum.size()) {
            String NomeArtista = listaAlbum.get(i).getArtista();
            String NomeAlbum = listaAlbum.get(i).getAlbum();

            String Immagine = PrendeImmagineAlbumArtista(NomeArtista, NomeAlbum);

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
                    String Artista = listaAlbum.get(i).getArtista();
                    String Album = listaAlbum.get(i).getAlbum();

                    ChiamateWsPlayer ws = new ChiamateWsPlayer(context, false);
                    ws.RitornaListaBrani(Artista, Album, "");

                    VariabiliStatichePlayer.getInstance().getLstBrani().setAdapter(null);
                }
            });

            TextView tAlbum = (TextView) view.findViewById(R.id.txtAlbum);
            tAlbum.setText(NomeAlbum);
        }

        return view;
    }

    private String PrendeImmagineAlbumArtista(String Artista, String Album) {
        String PathImmagini = Path + Artista + "/" + Album;
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
