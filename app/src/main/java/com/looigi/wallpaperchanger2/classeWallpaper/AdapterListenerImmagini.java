package com.looigi.wallpaperchanger2.classeWallpaper;

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
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.ChiamateWsWP;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.DownloadImmagineWP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterListenerImmagini extends BaseAdapter {
    private Context context;
    private List<StrutturaImmagine> listaImmaginiOrig;
    private List<StrutturaImmagine> listaImmagini;
    private LayoutInflater inflater;

    public AdapterListenerImmagini(Context applicationContext, List<StrutturaImmagine> Immagini) {
        this.context = applicationContext;
        this.listaImmaginiOrig = Immagini;
        this.listaImmagini = new ArrayList();
        VariabiliStaticheWallpaper.getInstance().getTxtQuanteRicerca().setText("Immagini rilevate: 0");
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaImmagini.size();
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
        listaImmagini = new ArrayList<>();

        notifyDataSetChanged();

        for (int i = 0; i < listaImmaginiOrig.size(); i++) {
            String NomeImmagine = listaImmaginiOrig.get(i).getPathImmagine();
            boolean Ok = true;
            if (!Filtro.isEmpty()) {
                if (!NomeImmagine.toUpperCase().contains(Filtro.toUpperCase())) {
                    Ok = false;
                }
            }
            if (Ok) {
                listaImmagini.add(listaImmaginiOrig.get(i));
            }
        }
        VariabiliStaticheWallpaper.getInstance().getTxtQuanteRicerca().setText("Immagini rilevate: " + Integer.toString(listaImmagini.size()));

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.lista_immagini, null);

        if (i < listaImmagini.size()) {
            String NomeImmagine = listaImmagini.get(i).getImmagine();
            String PathImmagine = listaImmagini.get(i).getPathImmagine().replace(VariabiliStaticheWallpaper.PercorsoImmagineSuURL + "/", "")
                    .replace(NomeImmagine, "");

            // ImageView imgVisualizza = (ImageView) view.findViewById(R.id.imgVisualizza);

            /* ImageView imgPresenteSuDisco = (ImageView) view.findViewById(R.id.imgPresenteSuDisco);
            if (Files.getInstance().EsisteFile(PathImmagine)) {
                imgPresenteSuDisco.setVisibility(LinearLayout.VISIBLE);
            } else {
                imgPresenteSuDisco.setVisibility(LinearLayout.GONE);
            } */

            ImageView imgElimina = (ImageView) view.findViewById(R.id.imgElimina);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    UtilityWallpaper.getInstance().Attesa(true);

                    ChiamateWsWP c = new ChiamateWsWP(context);
                    c.EliminaImmagine(listaImmagini.get(i));
                }
            });

            ImageView imgModifica = (ImageView) view.findViewById(R.id.imgModifica);
            imgModifica.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!VariabiliStaticheWallpaper.getInstance().isOffline()) {
                        VariabiliStaticheWallpaper.getInstance().setImmagineSelezionataDaLista(listaImmagini.get(i));

                        UtilityWallpaper.getInstance().Attesa(true);

                        DownloadImmagineWP d = new DownloadImmagineWP();
                        d.EsegueChiamata(context, listaImmagini.get(i).getImmagine(), null,
                                listaImmagini.get(i).getPathImmagine(), true, "MODIFICA");
                    }
                }
            });

            ImageView imgCondividi = (ImageView) view.findViewById(R.id.imgCondividi);
            imgCondividi.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (!VariabiliStaticheWallpaper.getInstance().isOffline()) {
                        VariabiliStaticheWallpaper.getInstance().setImmagineSelezionataDaLista(listaImmagini.get(i));

                        UtilityWallpaper.getInstance().Attesa(true);

                        DownloadImmagineWP d = new DownloadImmagineWP();
                        d.EsegueChiamata(context, listaImmagini.get(i).getImmagine(), null,
                                listaImmagini.get(i).getPathImmagine(), true, "CONDIVIDI");
                    }
                }
            });

            ImageView imgImmagine = (ImageView) view.findViewById(R.id.imgImmagine);
            if (VariabiliStaticheWallpaper.getInstance().isOffline()) {
                File imgFile = new File(listaImmagini.get(i).getPathImmagine());
                if (imgFile.exists()) {
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    imgImmagine.setImageBitmap(myBitmap);
                }
            } else {
                String PathImmagine2 = listaImmagini.get(i).getPathImmagine();
                DownloadImmagineWP d = new DownloadImmagineWP();
                d.EsegueChiamata(context, NomeImmagine, imgImmagine, PathImmagine2, false, "");
            }
            imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheWallpaper.getInstance().setUltimaImmagine(listaImmagini.get(i));

                    if (!VariabiliStaticheWallpaper.getInstance().isOffline()) {
                        UtilityWallpaper.getInstance().Attesa(true);
                        DownloadImmagineWP d = new DownloadImmagineWP();
                        d.EsegueChiamata(context, listaImmagini.get(i).getImmagine(), null,
                                listaImmagini.get(i).getPathImmagine(), false, "");
                    } else {
                        ChangeWallpaper c = new ChangeWallpaper(context, "WALLPAPER");
                        c.setWallpaperLocale(context, VariabiliStaticheWallpaper.getInstance().getUltimaImmagine());
                    }
                }
            });

            TextView Immagine = (TextView) view.findViewById(R.id.txtImmagine);
            Immagine.setText(NomeImmagine);

            TextView Path = (TextView) view.findViewById(R.id.txtPath);
            Path.setText(PathImmagine);
        }

        return view;
    }
}
