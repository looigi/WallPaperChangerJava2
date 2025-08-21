package com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.strutture.StrutturaGruppi;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.VariabiliStaticheImmaginiRaggruppate;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.webService.ChiamateWSIR;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.List;

public class AdapterListenerIR extends BaseAdapter {
    private Context context;
    private List<StrutturaGruppi> listaIR;
    private LayoutInflater inflater;

    public AdapterListenerIR(Context applicationContext, List<StrutturaGruppi> listaIR) {
        this.context = applicationContext;
        this.listaIR = listaIR;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return listaIR.size();
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
        view = inflater.inflate(R.layout.lista_ir, null);

        String Quante = String.valueOf(listaIR.get(i).getQuante());
        String Filtro = listaIR.get(i).getFiltro();

        TextView txtQuante = view.findViewById(R.id.txtQuante);
        txtQuante.setText(Quante);

        TextView txtFiltro = view.findViewById(R.id.txtFiltro);
        txtFiltro.setText(Filtro);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImmaginiRaggruppate.getInstance().setFiltro(Filtro);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Filtro ricerca");

                // Crea l'EditText
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(Filtro);
                builder.setView(input);

                // Bottone OK
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String valoreInserito = input.getText().toString().trim();

                        ChiamateWSIR ws = new ChiamateWSIR(context);
                        ws.RitornaRaggruppatePerFiltro(
                                VariabiliStaticheImmaginiRaggruppate.getInstance().getIdCategoria(),
                                valoreInserito
                        );
                    }
                });

                // Bottone Annulla
                builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // Chiude il dialog
                    }
                });

                // Mostra il dialog
                builder.show();
            }
        });

        ImageView imgCreaNuovaCategoria = view.findViewById(R.id.imgCreaNuovaCategoria);
        imgCreaNuovaCategoria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String NuovaCategoria = Filtro.replace(";", " ");

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Nome nuova categoria");

                // Crea l'EditText
                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(NuovaCategoria);
                builder.setView(input);

                // Bottone OK
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String valoreInserito = input.getText().toString().trim();
                        valoreInserito = valoreInserito.replace(" ", "_");

                        if (VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM() == null) {
                            ChiamateWSMI c = new ChiamateWSMI(context);
                            c.RitornaCategorie(false, "FC");
                        }

                        boolean ok = false;
                        String NuovaCategoria2 = valoreInserito.toUpperCase().trim();
                        for (StrutturaImmaginiCategorie s: VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                            if (s.getCategoria().toUpperCase().trim().equals(NuovaCategoria2)) {
                                VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaInserita(s.getCategoria());
                                VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento(
                                        Integer.toString(s.getIdCategoria())
                                );
                                ok = true;
                                break;
                            }
                        }
                        VariabiliStaticheImmaginiRaggruppate.getInstance().setIdCategoria("");
                        VariabiliStaticheImmaginiRaggruppate.getInstance().setCategoriaImpostata(valoreInserito);

                        if (!ok) {
                            ChiamateWSMI c = new ChiamateWSMI(context);
                            c.CreaNuovaCategoria(valoreInserito, "IR");
                        } else {
                            String[] ll = new String[VariabiliStaticheImmaginiRaggruppate.getInstance().getListaCategorieIMM().size() + 1];
                            int i = 0;
                            for (StrutturaImmaginiCategorie l2: VariabiliStaticheImmaginiRaggruppate.getInstance().getListaCategorieIMM()) {
                                if (l2.getCategoria() != null) {
                                    ll[i] = l2.getCategoria();
                                }
                                i++;
                            }
                            i = 0;
                            for (String lll: ll) {
                                if (lll == null || lll.isEmpty()) {
                                    ll[i] = "**NULL**";
                                }
                                i++;
                            }
                            UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                    VariabiliStaticheImmaginiRaggruppate.getInstance().getSpnCategorie(),
                                    ll,
                                    VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata()
                            );
                        }
                    }
                });

                // Bottone Annulla
                builder.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // Chiude il dialog
                    }
                });

                // Mostra il dialog
                builder.show();
            }
        });

        return view;
    }
}
