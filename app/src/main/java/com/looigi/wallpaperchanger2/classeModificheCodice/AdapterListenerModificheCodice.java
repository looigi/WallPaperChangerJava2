package com.looigi.wallpaperchanger2.classeModificheCodice;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeModificheCodice.Strutture.Modifiche;

import java.util.List;

public class AdapterListenerModificheCodice extends BaseAdapter {
    private Context context;
    private List<Modifiche> listaModifiche;
    private LayoutInflater inflater;

    public AdapterListenerModificheCodice(Context applicationContext, List<Modifiche> Modifiche) {
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
            String Stato = VariabiliStaticheModificheCodice.getInstance().RitornaStringaStato(
                                listaModifiche.get(i).getIdStato()
                            );


            ImageView imgModifica = (ImageView) view.findViewById(R.id.imgModifica);
            imgModifica.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheModificheCodice.getInstance().setIdModifica(listaModifiche.get(i).getIdModifica());

                    VariabiliStaticheModificheCodice.getInstance().setTipologia("MODIFICA");
                    VariabiliStaticheModificheCodice.getInstance().setOperazione("UPDATE");

                    VariabiliStaticheModificheCodice.getInstance().getSpnStati().setPrompt(
                            Stato
                    );
                    VariabiliStaticheModificheCodice.getInstance().getLayStato().setVisibility(LinearLayout.VISIBLE);
                    VariabiliStaticheModificheCodice.getInstance().getTxtTipologia().setText("Modifica modifica");
                    VariabiliStaticheModificheCodice.getInstance().getEdtTipologia().setText(NomeModifica);
                    VariabiliStaticheModificheCodice.getInstance().getLayTipologia().setVisibility(LinearLayout.VISIBLE);
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
                            VariabiliStaticheModificheCodice.getInstance().setIdModifica(listaModifiche.get(i).getIdModifica());

                            VariabiliStaticheModificheCodice.getInstance().setTipologia("MODIFICA");
                            VariabiliStaticheModificheCodice.getInstance().setOperazione("DELETE");

                            VariabiliStaticheModificheCodice.getInstance().EffettuaSalvataggio(context);
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

            /* ImageView imgCambiaStato = (ImageView) view.findViewById(R.id.imgCambiaStato);
            imgCambiaStato.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                }
            }); */

            TextView Modifica = (TextView) view.findViewById(R.id.txtModifica);
            Modifica.setText(NomeModifica);

            TextView StatoModifica = (TextView) view.findViewById(R.id.txtStatoModifica);
            StatoModifica.setText("Stato: " + Stato);
        }

        return view;
    }
}