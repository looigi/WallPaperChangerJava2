package com.looigi.wallpaperchanger2.classeOnomastici.adapters;

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
import com.looigi.wallpaperchanger2.classeOnomastici.VariabiliStaticheOnomastici;
import com.looigi.wallpaperchanger2.classeOnomastici.db.db_dati_compleanni;
import com.looigi.wallpaperchanger2.classeOnomastici.strutture.StrutturaCompleanno;

import java.util.Calendar;
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
            String Cognome = listaCompleanni.get(i).getCognome();
            String Anno = String.valueOf(listaCompleanni.get(i).getAnno());
            int Eta = CalcolaEta(listaCompleanni.get(i));

            ImageView imgModifica = view.findViewById(R.id.imgModifica);
            imgModifica.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheOnomastici.getInstance().setIdModifica(
                            listaCompleanni.get(i).getId()
                    );
                    VariabiliStaticheOnomastici.getInstance().getEdtNomeCompleanno().setText(
                            listaCompleanni.get(i).getNome()
                    );
                    VariabiliStaticheOnomastici.getInstance().getEdtCognomeCompleanno().setText(
                            listaCompleanni.get(i).getCognome()
                    );
                    VariabiliStaticheOnomastici.getInstance().getEdtNomeCompleanno().setEnabled(false);
                    VariabiliStaticheOnomastici.getInstance().getEdtCognomeCompleanno().setEnabled(false);
                    if (listaCompleanni.get(i).getGiorno() != 0) {
                        VariabiliStaticheOnomastici.getInstance().getEdtGiornoCompleanno().setText(
                                Integer.toString(listaCompleanni.get(i).getGiorno())
                        );
                    }
                    if (listaCompleanni.get(i).getMese() != 0) {
                        VariabiliStaticheOnomastici.getInstance().getEdtMeseCompleanno().setText(
                                Integer.toString(listaCompleanni.get(i).getMese())
                        );
                    }
                    if (listaCompleanni.get(i).getAnno() != 0) {
                        VariabiliStaticheOnomastici.getInstance().getEdtAnnoCompleanno().setText(
                                Integer.toString(listaCompleanni.get(i).getAnno())
                        );
                    }

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
                            db.ChiudeDB();

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
            tNome.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String Nominativo = listaCompleanni.get(i).getNome() + " " +
                            listaCompleanni.get(i).getCognome();
                    VariabiliStaticheOnomastici.getInstance().getTxtNomeSceltoCompleanno().setText(
                            Nominativo
                    );
                }
            });

            TextView tCognome = (TextView) view.findViewById(R.id.txtCognome);
            tCognome.setText(Cognome);
            tCognome.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String Nominativo = listaCompleanni.get(i).getNome() + " " +
                            listaCompleanni.get(i).getCognome();
                    VariabiliStaticheOnomastici.getInstance().getTxtNomeSceltoCompleanno().setText(
                            Nominativo
                    );
                }
            });

            TextView tAnno = (TextView) view.findViewById(R.id.txtAnno);
            tAnno.setText(Anno);

            TextView tEta = (TextView) view.findViewById(R.id.txtEta);
            if (Eta > -1) {
                tEta.setText(Integer.toString(Eta));
            } else {
                tEta.setText("");
            }
        }

        return view;
    }

    private int CalcolaEta(StrutturaCompleanno s) {
        if (s.getGiorno() == 0 || s.getMese() == 0 || s.getAnno() == 0) {
            return -1;
        }

        Calendar Oggi = Calendar.getInstance();
        int Giorno=Oggi.get(Calendar.DAY_OF_MONTH);
        int Mese=Oggi.get(Calendar.MONTH)+1;
        int Anno=Oggi.get(Calendar.YEAR);

        int GiornoNascita=s.getGiorno();
        int MeseNascita=s.getMese();
        int AnnoNascita=s.getAnno();

        int diff = 0;
        if (Mese > MeseNascita) {
            diff = Anno - AnnoNascita;
        } else {
            if (Mese == MeseNascita) {
                if (Giorno >= GiornoNascita) {
                    diff = Anno - AnnoNascita;
                } else {
                    diff = (Anno - AnnoNascita) - 1;
                }
            } else {
                diff = (Anno - AnnoNascita) - 1;
            }
        }

        return diff;
    }
}
