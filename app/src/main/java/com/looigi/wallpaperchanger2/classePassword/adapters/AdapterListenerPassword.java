package com.looigi.wallpaperchanger2.classePassword.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classePassword.UtilityPassword;
import com.looigi.wallpaperchanger2.classePassword.VariabiliStatichePWD;
import com.looigi.wallpaperchanger2.classePassword.db_dati_password;
import com.looigi.wallpaperchanger2.classePassword.strutture.StrutturaPassword;

import java.util.List;

public class AdapterListenerPassword extends BaseAdapter {
    Context context;
    List<StrutturaPassword> listaPassword;
    LayoutInflater inflter;

    public AdapterListenerPassword(Context applicationContext, List<StrutturaPassword> Password) {
        this.context = context;
        this.listaPassword = Password;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaPassword.size();
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
        view = inflter.inflate(R.layout.lista_password, null);

        ImageView imgModifica = (ImageView) view.findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePWD.getInstance().setModalitaEdit("MODIFICA");
                VariabiliStatichePWD.getInstance().getLayPassword().setVisibility(LinearLayout.VISIBLE);
                VariabiliStatichePWD.getInstance().getTxtId().setText(Integer.toString(listaPassword.get(i).getIdRiga()));
                VariabiliStatichePWD.getInstance().getEdtSito().setText(listaPassword.get(i).getSito());
                VariabiliStatichePWD.getInstance().getEdtUtenza().setText(listaPassword.get(i).getUtenza());
                VariabiliStatichePWD.getInstance().getEdtPassword().setText(listaPassword.get(i).getPassword());
                VariabiliStatichePWD.getInstance().getEdtNote().setText(listaPassword.get(i).getNote());
                VariabiliStatichePWD.getInstance().getEdtIndirizzo().setText(listaPassword.get(i).getIndirizzo());
            }
        });

        ImageView imgElimino = (ImageView) view.findViewById(R.id.imgElimina);
        imgElimino.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Messaggio = "Si vuole eliminare la password selezionata (id " + Integer.toString(listaPassword.get(i).getIdRiga()) + ") ?";

                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage(Messaggio);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            db_dati_password db = new db_dati_password(context);
                            db.EliminaPassword(Integer.toString(listaPassword.get(i).getIdRiga()), true);

                            db.LeggePasswords();

                            UtilityPassword.getInstance().RiempieArrayLista(context);

                            dialog.dismiss(); //<-- change it with ur code
                        }
                    });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annulla",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                alertDialog.show();
            }
        });

        TextView txtIdRiga = (TextView) view.findViewById(R.id.txtIdRiga);
        txtIdRiga.setText(Integer.toString(listaPassword.get(i).getIdRiga()));

        TextView txtSito = (TextView) view.findViewById(R.id.txtSito);
        txtSito.setText(listaPassword.get(i).getSito());

        TextView txtUtenza = (TextView) view.findViewById(R.id.txtUtenza);
        txtUtenza.setText(listaPassword.get(i).getUtenza());

        TextView txtPassword = (TextView) view.findViewById(R.id.txtPassword);
        txtPassword.setText(listaPassword.get(i).getPassword());

        TextView txtNote = (TextView) view.findViewById(R.id.txtNote);
        txtNote.setText(listaPassword.get(i).getNote());

        TextView txtIndirizzo = (TextView) view.findViewById(R.id.txtIndirizzo);
        txtIndirizzo.setText(listaPassword.get(i).getIndirizzo());

        txtIndirizzo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Indirizzo = listaPassword.get(i).getIndirizzo();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Indirizzo));
                context.startActivity(browserIntent);
            }
        });

        return view;
    }
}
