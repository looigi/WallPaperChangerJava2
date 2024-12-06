package com.looigi.wallpaperchanger2.classeModifiche;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.VariabiliStaticheDetector;
import com.looigi.wallpaperchanger2.classeModifiche.Strutture.Modifiche;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.VariabiliStaticheWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.ChiamateWsWP;
import com.looigi.wallpaperchanger2.classeWallpaper.WebServices.DownloadImmagineWP;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdapterListenerModifiche extends BaseAdapter {
    private Context context;
    private List<Modifiche> listaModifiche;
    private LayoutInflater inflater;

    public AdapterListenerModifiche(Context applicationContext, List<Modifiche> Modifiche) {
        this.context = applicationContext;
        this.listaModifiche = Modifiche;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaModifiche.size();
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
        view = inflater.inflate(R.layout.lista_modifiche, null);

        if (i < listaModifiche.size()) {
            String NomeModifica = listaModifiche.get(i).getModifica();

            ImageView imgModifica = (ImageView) view.findViewById(R.id.imgModifica);
            imgModifica.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheModifiche.getInstance().setIdModifica(listaModifiche.get(i).getIdModifica());

                    VariabiliStaticheModifiche.getInstance().setTipologia("MODIFICA");
                    VariabiliStaticheModifiche.getInstance().setOperazione("UPDATE");

                    VariabiliStaticheModifiche.getInstance().getSpnStati().setPrompt(
                            VariabiliStaticheModifiche.getInstance().RitornaStringaStato(
                                    listaModifiche.get(i).getIdStato()
                            )
                    );
                    VariabiliStaticheModifiche.getInstance().getLayStato().setVisibility(LinearLayout.VISIBLE);
                    VariabiliStaticheModifiche.getInstance().getTxtTipologia().setText("Modifica modifica");
                    VariabiliStaticheModifiche.getInstance().getEdtTipologia().setText(NomeModifica);
                    VariabiliStaticheModifiche.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
                }
            });

            ImageView imgElimina = (ImageView) view.findViewById(R.id.imgElimina);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Si vuole eliminare la modifica '" + NomeModifica + "' ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            VariabiliStaticheModifiche.getInstance().setIdModifica(listaModifiche.get(i).getIdModifica());

                            VariabiliStaticheModifiche.getInstance().setTipologia("MODIFICA");
                            VariabiliStaticheModifiche.getInstance().setOperazione("DELETE");

                            VariabiliStaticheModifiche.getInstance().EffettuaSalvataggio(context);
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

            ImageView imgCambiaStato = (ImageView) view.findViewById(R.id.imgCambiaStato);
            imgCambiaStato.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            });

            TextView Modifica = (TextView) view.findViewById(R.id.txtModifica);
            Modifica.setText(NomeModifica);
        }

        return view;
    }
}
