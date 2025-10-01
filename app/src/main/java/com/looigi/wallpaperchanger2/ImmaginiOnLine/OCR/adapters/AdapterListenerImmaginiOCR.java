package com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.MainPreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.VariabiliStaticheOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.strutture.StrutturaImmaginiOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.OCR.webService.DownloadImmagineOCR;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.VariabiliStaticheSpostamento;

import java.util.List;

public class AdapterListenerImmaginiOCR extends BaseAdapter {
    private Context context;
    private List<StrutturaImmaginiOCR> Immagini;
    private LayoutInflater inflater;

    public AdapterListenerImmaginiOCR(Context applicationContext, List<StrutturaImmaginiOCR> Imms) {
        this.context = applicationContext;
        this.Immagini = Imms;

        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if (Immagini != null) {
            return Immagini.size();
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
        // if (view != null) return view;

        view = inflater.inflate(R.layout.lista_immagini_ocr, null);

        if (i < Immagini.size()) {
            ImageView imgOrigine = view.findViewById(R.id.imgOrigine);
            TextView txtNomeFile = view.findViewById(R.id.txtNomeImmagine);
            TextView txtTesto = view.findViewById(R.id.txtTesto);
            txtTesto.setText(Immagini.get(i).getTesto());
            TextView txtCategoria = view.findViewById(R.id.txtCategoria);
            TextView txtDestinazione = view.findViewById(R.id.txtDestinazione);

            DownloadImmagineOCR dO = new DownloadImmagineOCR();
            dO.EsegueChiamata(
                    context,
                    "",
                    imgOrigine,
                    Immagini.get(i).getURL()
            );

            txtCategoria.setText(Immagini.get(i).getCategoriaOrigine());
            String Destinazione = Immagini.get(i).getCategorieDestinazione();
            String Destinazione2 = "";
            int quanteDestinazioni = 0;
            if (!Destinazione.isEmpty()) {
                String[] d = Destinazione.split("§");
                for (String dd: d) {
                    if (!dd.isEmpty()) {
                        String[] ddd = dd.split(";");
                        if (ddd.length > 0) {
                            Destinazione2 += ddd[1] + "\n";
                            quanteDestinazioni++;
                        }
                    }
                }
            }
            txtDestinazione.setText(Destinazione2);

            ImageView imgSpostaACategoria = view.findViewById(R.id.imgSpostaACategoria);
            imgSpostaACategoria.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View v) {
                     LinearLayout layout = new LinearLayout(context);
                     layout.setOrientation(LinearLayout.VERTICAL);
                     layout.setPadding(50, 40, 50, 10);

                     // Crea lo Spinner
                     Spinner spinner = new Spinner(context);
                     layout.addView(spinner);

                     // Prepara gli array di categorie
                     int listaSize = VariabiliStaticheOCR.getInstance().getListaCategorie().size();
                     String[] items = new String[listaSize];
                     String[] id = new String[listaSize];

                     for (int i = 0; i < listaSize; i++) {
                         StrutturaImmaginiCategorie s = VariabiliStaticheOCR.getInstance().getListaCategorie().get(i);
                         items[i] = s.getCategoria();
                         id[i] = String.valueOf(s.getIdCategoria());
                     }

                     // Adapter per lo Spinner
                     ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, items);
                     adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                     spinner.setAdapter(adapter);

                     // Imposta voce predefinita
                     int defaultSelection = VariabiliStaticheOCR.getInstance().getUltimoValoreSelezionatoSpinner();
                     spinner.setSelection(defaultSelection);

                     AlertDialog.Builder builder = new AlertDialog.Builder(context);
                     builder.setTitle("Scegli una destinazione");
                     builder.setView(layout); // inserisci lo Spinner

                     builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                         @Override
                         public void onClick(DialogInterface dialog, int which) {
                             VariabiliStaticheOCR.getInstance().setIdImmagineDaSpostare(String.valueOf(Immagini.get(i).getIdImmagine()));

                             int selectedPos = spinner.getSelectedItemPosition();
                             VariabiliStaticheOCR.getInstance().setUltimoValoreSelezionatoSpinner(selectedPos);

                             VariabiliStaticheSpostamento.getInstance()
                                     .setIdCategoriaSpostamento(Integer.parseInt(id[selectedPos]));

                             StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary();
                             s.setIdImmagine(Immagini.get(i).getIdImmagine());

                             ChiamateWSMI c = new ChiamateWSMI(context);
                             c.SpostaImmagine(s, "OCR");

                             imgOrigine.setImageBitmap(null);
                         }
                     });

                     builder.setNegativeButton("Annulla", null);

                     // Mostra il dialog
                     builder.create().show();
                 }
             });

            ImageView imgSposta = view.findViewById(R.id.imgSposta);
            if (VariabiliStaticheOCR.getInstance().isAncheDestinazioniVuote()) {
                imgSposta.setVisibility(LinearLayout.GONE);
            } else {
                imgSposta.setVisibility(LinearLayout.VISIBLE);
            }
            int finalQuanteDestinazioni = quanteDestinazioni;
            imgSposta.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheOCR.getInstance().setIdImmagineDaSpostare(String.valueOf(Immagini.get(i).getIdImmagine()));

                    String[] d = Destinazione.split("§");
                    final String[] idCategoria = {""};
                    if (finalQuanteDestinazioni == 1) {
                        String Dest = d[0];
                        String[] dd = Dest.split(";");
                        idCategoria[0] = dd[0];

                        VariabiliStaticheSpostamento.getInstance().setIdCategoriaSpostamento(Integer.parseInt(idCategoria[0]));
                        StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary(); // CREARE LA STRUTTURA PER LO SPOSTAMENTO
                        s.setIdImmagine(Immagini.get(i).getIdImmagine());
                        ChiamateWSMI c = new ChiamateWSMI(context);
                        c.SpostaImmagine(s, "OCR");

                        imgOrigine.setImageBitmap(null);
                    } else {
                        String[] items = new String[d.length];
                        String[] id = new String[d.length];
                        int i2 = 0;
                        for (String dd: d) {
                            if (dd.contains(";")) {
                                String[] ddd = dd.split(";");
                                items[i2] = ddd[1];
                                id[i2] = ddd[0];
                                i2++;
                            }
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Scegli una destinazione")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    idCategoria[0] = id[which];

                                    VariabiliStaticheSpostamento.getInstance().setIdCategoriaSpostamento(Integer.parseInt(idCategoria[0]));
                                    StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary(); // CREARE LA STRUTTURA PER LO SPOSTAMENTO
                                    s.setIdImmagine(Immagini.get(i).getIdImmagine());
                                    ChiamateWSMI c = new ChiamateWSMI(context);
                                    c.SpostaImmagine(s, "OCR");

                                    imgOrigine.setImageBitmap(null);
                                }
                            });

                        builder.create().show();
                    }
                }
            });

            String NomeFile = Immagini.get(i).getURL();
            if (NomeFile.contains("/")) {
                String[] n = NomeFile.split("/");
                NomeFile = n[n.length - 1];
            }
            txtNomeFile.setText(NomeFile);

            String finalNomeFile = NomeFile;
            imgOrigine.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    StrutturaImmaginiLibrary si = new StrutturaImmaginiLibrary();
                    si.setUrlImmagine(Immagini.get(i).getURL());
                    si.setNomeFile(finalNomeFile);
                    si.setIdImmagine(Immagini.get(i).getIdImmagine());
                    VariabiliStatichePreview.getInstance().setStrutturaImmagine(si);

                    Intent i = new Intent(context, MainPreview.class);
                    i.putExtra("Modalita", "OCR");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });
        }

        return view;
    }
}
