package com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.VariabiliImmaginiUguali;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.webService.DownloadImmagineUguali;
import com.looigi.wallpaperchanger2.classeLazio.UtilityLazio;
import com.looigi.wallpaperchanger2.classeLazio.VariabiliStaticheLazio;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.MainControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.VariabiliStaticheControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.List;

public class AdapterListenerListaControllo extends BaseAdapter {
    private Context context;
    private List<String> listaImmagini;
    private LayoutInflater inflter;

    public AdapterListenerListaControllo(Context applicationContext, List<String> Immagini) {
        this.context = applicationContext;
        this.listaImmagini = Immagini;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if (listaImmagini != null) {
            return listaImmagini.size();
        } else {
            return 0;
        }
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
        view = inflter.inflate(R.layout.lista_controllo_immagini, null);

        String[] Dati = listaImmagini.get(i).split("§");
        String NomeImmagine = Dati[0];
        String Cartella = Dati[1];
        int idImm = Integer.parseInt(Dati[2]);
        String Cosa = "";
        if (Dati.length > 3) {
            Cosa = Dati[3];
        } else {
            Cosa = "";
        }
        String Categoria = VariabiliStaticheControlloImmagini.getInstance().getCategoria();
        String Path = VariabiliImmaginiUguali.PathUrl + Categoria + "/" + Cartella + "/" + NomeImmagine;

        ImageView imgImmagine = view.findViewById(R.id.imgImmagine);
        imgImmagine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheControlloImmagini.getInstance().getLayPreview().setVisibility(LinearLayout.VISIBLE);

                DownloadImmagineUguali d = new DownloadImmagineUguali();
                d.EsegueDownload(context, VariabiliStaticheControlloImmagini.getInstance().getImgPreview(), Path);
            }
        });

        ImageView imgElimina = view.findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare l'immagine selezionata ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSMI ws = new ChiamateWSMI(context);
                        ws.EliminaImmagine(String.valueOf(idImm));
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

        ImageView imgConverte = view.findViewById(R.id.imgConverte);
        imgConverte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Si vuole convertire l'immagine selezionata ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSUI ws = new ChiamateWSUI(context);
                        ws.ConverteImmagine(String.valueOf(idImm));
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

        ImageView imgRinomina = view.findViewById(R.id.imgRinomina);
        imgRinomina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Nuovo nome file");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(NomeImmagine);
                builder.setView(input);

                String finalCategoria = Categoria;
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Salvataggio = input.getText().toString();
                        if (Salvataggio.isEmpty()) {
                            UtilitiesGlobali.getInstance().ApreToast(context,
                                    "Immettere un nome file");
                        } else {
                            ChiamateWSUI ws = new ChiamateWSUI(context);
                            ws.RinominaImmagine(String.valueOf(idImm), Salvataggio);
                        }
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

        DownloadImmagineUguali d = new DownloadImmagineUguali();
        d.EsegueDownload(context, imgImmagine, Path);

        TextView txtImmagine = view.findViewById(R.id.txtImmagine);
        txtImmagine.setText(NomeImmagine);

        TextView txtCartella = view.findViewById(R.id.txtCartella);
        txtCartella.setText("Cart.: " + Cartella);

        TextView txtId = view.findViewById(R.id.txtIdImmagine);
        txtId.setText("Id: " + Integer.toString(idImm));

        TextView txtCosa = view.findViewById(R.id.txtCosa);
        txtCosa.setText(Cosa);

        return view;
    }
}
