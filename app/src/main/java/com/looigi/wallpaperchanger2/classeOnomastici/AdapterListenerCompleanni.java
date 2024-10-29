package com.looigi.wallpaperchanger2.classeOnomastici;

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
import com.looigi.wallpaperchanger2.classeOnomastici.strutture.StrutturaCompleanno;
import com.looigi.wallpaperchanger2.classePlayer.Strutture.StrutturaArtisti;
import com.looigi.wallpaperchanger2.classePlayer.UtilityPlayer;
import com.looigi.wallpaperchanger2.classePlayer.VariabiliStatichePlayer;
import com.looigi.wallpaperchanger2.classePlayer.WebServices.ChiamateWsPlayer;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerCompleanni extends BaseAdapter {
    private Context context;
    private List<StrutturaCompleanno> listaCompleanni;
    private LayoutInflater inflater;
    private String Filtro;
    private String Path;

    public AdapterListenerCompleanni(Context applicationContext, List<StrutturaCompleanno> Compleanno) {
        this.context = applicationContext;
        this.listaCompleanni = Compleanno;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaCompleanni.size();
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
        view = inflater.inflate(R.layout.lista_compleanni, null);

        if (i < listaCompleanni.size()) {
            String Nome = listaCompleanni.get(i).getNome();
            String Anno = String.valueOf(listaCompleanni.get(i).getAnno());

            ImageView imgModifica = view.findViewById(R.id.imgModifica);
            imgModifica.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheOnomastici.getInstance().setIdModifica(
                            listaCompleanni.get(i).getId()
                    );
                    VariabiliStaticheOnomastici.getInstance().getEdtNomeCompleanno().setText(
                            listaCompleanni.get(i).getNome()
                    );
                    VariabiliStaticheOnomastici.getInstance().getEdtGiornoCompleanno().setText(
                            Integer.toString(listaCompleanni.get(i).getGiorno())
                    );
                    VariabiliStaticheOnomastici.getInstance().getEdtMeseCompleanno().setText(
                            Integer.toString(listaCompleanni.get(i).getMese())
                    );
                    VariabiliStaticheOnomastici.getInstance().getEdtAnnoCompleanno().setText(
                            Integer.toString(listaCompleanni.get(i).getAnno())
                    );

                    VariabiliStaticheOnomastici.getInstance().getLayInsComp().setVisibility(LinearLayout.VISIBLE);
                }
            });

            ImageView imgElimina = view.findViewById(R.id.imgElimina);
            imgElimina.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Si vuole eliminare il compleanno ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db_dati_compleanni db = new db_dati_compleanni(context);
                            db.EliminaCompleanno(listaCompleanni.get(i));

                            notifyDataSetChanged();
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

            TextView tNome = (TextView) view.findViewById(R.id.txtNome);
            tNome.setText(Nome);

            TextView tAnno = (TextView) view.findViewById(R.id.txtAnno);
            tAnno.setText(Anno);
        }

        return view;
    }
}
