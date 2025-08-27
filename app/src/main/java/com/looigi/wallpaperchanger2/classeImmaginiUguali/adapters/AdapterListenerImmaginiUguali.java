package com.looigi.wallpaperchanger2.classeImmaginiUguali.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.StrutturaImmaginiUgualiRitornate;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.VariabiliImmaginiUguali;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.ChiamateWSMIU;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.DownloadImmagineUguali;
import com.looigi.wallpaperchanger2.classePreview.MainPreview;
import com.looigi.wallpaperchanger2.classePreview.VariabiliStatichePreview;

import java.util.List;

public class AdapterListenerImmaginiUguali extends BaseAdapter {
    private Context context;
    private List<StrutturaImmaginiUgualiRitornate> Immagini;
    private LayoutInflater inflater;
    private int dimensioniMinime;

    public AdapterListenerImmaginiUguali(Context applicationContext, List<StrutturaImmaginiUgualiRitornate> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;

        dimensioniMinime = 9999999;
        inflater = (LayoutInflater.from(applicationContext));

        for (StrutturaImmaginiUgualiRitornate s: Imms) {
            if (Integer.parseInt(s.getDimensioneFile()) < dimensioniMinime) {
                dimensioniMinime = Integer.parseInt(s.getDimensioneFile());
            }
        }
    }

    @Override
    public int getCount() {
        return Immagini.size();
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
        // if (view != null) return view;

        view = inflater.inflate(R.layout.lista_immagini_uguali, null);

        if (i < Immagini.size()) {
            ImageView imgImmagine = view.findViewById(R.id.imgImmagine);
            ImageView imgElimina = view.findViewById(R.id.imgElimina);
            TextView txtIdImmagine = view.findViewById(R.id.txtIdImmagine);
            TextView txtCartella = view.findViewById(R.id.txtCartella);
            TextView txtNomeFile = view.findViewById(R.id.txtNomeFile);
            TextView txtDimeFile = view.findViewById(R.id.txtDimensioneFile);
            TextView txtDimeImm = view.findViewById(R.id.txtDimensioneImmagine);

            if (Immagini.size() > 1) {
                if (Integer.parseInt(Immagini.get(i).getDimensioneFile()) == dimensioniMinime) {
                    view.setBackgroundColor(Color.argb(255, 255, 150, 150));
                }
            }

            String UrlImmagine = VariabiliImmaginiUguali.PathUrl + VariabiliImmaginiUguali.getInstance().getCategoria() +
                    "/" + Immagini.get(i).getCartella() + "/" + Immagini.get(i).getNomeFile();

            DownloadImmagineUguali d = new DownloadImmagineUguali();
            d.EsegueDownload(context, imgImmagine, UrlImmagine);

            txtIdImmagine.setText("Id Immagine: " + Immagini.get(i).getIdImmagine());
            txtCartella.setText("Cartella: " + Immagini.get(i).getCartella());
            txtNomeFile.setText(Immagini.get(i).getNomeFile());
            txtDimeFile.setText("File: " + Immagini.get(i).getDimensioneFile());
            txtDimeImm.setText("Dim.: " + Immagini.get(i).getDimensioneImmagine());

            imgImmagine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // VariabiliImmaginiUguali.getInstance().getLayPreview().setVisibility(LinearLayout.VISIBLE);
                    // d.EsegueDownload(context, VariabiliImmaginiUguali.getInstance().getImgPreview(), UrlImmagine);

                    StrutturaImmaginiLibrary si = new StrutturaImmaginiLibrary();
                    si.setUrlImmagine(UrlImmagine);
                    si.setNomeFile(Immagini.get(i).getNomeFile());
                    si.setIdImmagine(Immagini.get(i).getIdImmagine());
                    VariabiliStatichePreview.getInstance().setStrutturaImmagine(si);

                    Intent i = new Intent(context, MainPreview.class);
                    i.putExtra("Modalita", "ImmaginiUguali");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Immagini uguali");
                    builder.setMessage("Si vuole eliminare l\'immagine selezionata ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ChiamateWSMIU c = new ChiamateWSMIU(context);
                            c.EliminaImmagine(
                                    Integer.toString(Immagini.get(i).getIdImmagine())
                            );
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
        }

        return view;
    }
}
