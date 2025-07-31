package com.looigi.wallpaperchanger2.classeUtilityImmagini.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.MainImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.MainImmaginiRaggruppate;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.MainImmaginiUguali;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.StrutturaImmaginiUguali;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.MainScaricaImmagini;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.MainUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.VariabiliStaticheUtilityImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.classeControllo.MainControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaControlloImmagini;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.strutture.StrutturaUgualiInterna;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class AdapterListenerUI extends BaseAdapter {
    private Context context;
    private List<StrutturaImmaginiCategorie> listaCategorie;
    private List<StrutturaImmaginiCategorie> listaCategorieFiltrate;
    private LayoutInflater inflater;

    public AdapterListenerUI(Context applicationContext, List<StrutturaImmaginiCategorie> listaCategorie) {
        this.context = applicationContext;
        this.listaCategorie = listaCategorie;
        aggiornaListaConFiltro();
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        if (listaCategorie != null) {
            return listaCategorieFiltrate.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return listaCategorieFiltrate.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void aggiornaListaConFiltro() {
        String Filtro = VariabiliStaticheUtilityImmagini.getInstance().getFiltroCategorie();
        Filtro = Filtro.toUpperCase().trim();
        List<StrutturaImmaginiCategorie> listaFiltrata = new ArrayList<>();

        for (StrutturaImmaginiCategorie cat : listaCategorie) {
            if (Filtro.isEmpty() || cat.getCategoria().toUpperCase().contains(Filtro)) {
                listaFiltrata.add(cat);
            }
        }

        this.listaCategorieFiltrate = listaFiltrata;

        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.lista_ui, null);

        if (i < listaCategorieFiltrate.size()) {
            String Categoria = listaCategorieFiltrate.get(i).getCategoria();
            int idCategoria = listaCategorieFiltrate.get(i).getIdCategoria();

            TextView txtCategoria = view.findViewById(R.id.txtCategoria);
            txtCategoria.setText(Categoria);

            ImageView imgRaggruppate = view.findViewById(R.id.imgRaggruppate);
            imgRaggruppate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(context, MainImmaginiRaggruppate.class);
                    intent.putExtra("idCategoria", Integer.toString(idCategoria));
                    context.startActivity(intent);
                }
            });

            ImageView imgControllo = view.findViewById(R.id.imgControlloImmagini);
            imgControllo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheUtilityImmagini.getInstance().getTxtQuale().setText("Elaborazione " + Categoria);
                    VariabiliStaticheUtilityImmagini.getInstance().setCategoriaAttuale(Categoria);
                    VariabiliStaticheUtilityImmagini.getInstance().setControllaTutto(false);
                    VariabiliStaticheUtilityImmagini.getInstance().setQualeStaControllando(i);

                    if (VariabiliStaticheUtilityImmagini.getInstance().isEsegueAncheRefresh()) {
                        ChiamateWSUI ws = new ChiamateWSUI(context);
                        ws.RefreshImmagini(String.valueOf(idCategoria), false);
                    } else {
                        ChiamateWSUI ws = new ChiamateWSUI(context);
                        ws.ControlloImmagini(String.valueOf(idCategoria), "S");
                    }
                }
            });

            LinearLayout layControllo = view.findViewById(R.id.layControllo);
            TextView txtControllo = view.findViewById(R.id.txtControllo);
            boolean Ok = false;
            for (StrutturaControlloImmagini s : VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini()) {
                if (s.getIdCategoria() == idCategoria) {
                    String Scritta = "CONTROLLO Tot.: " + s.getGiuste();
                    boolean Ok2 = true;
                    if (s.getErrate() > 0) {
                        Scritta += " - Errate " + s.getErrate();
                        Ok2 = false;
                    }
                    if (s.getPiccole() > 0) {
                        Scritta += " - Piccole: " + s.getPiccole();
                        Ok2 = false;
                    }
                    if (s.getInesistenti() > 0) {
                        Scritta += " - Inesistenti: " + s.getInesistenti();
                        Ok2 = false;
                    }
                    if (s.getGrandi() > 0) {
                        Scritta += " - Grandi: " + s.getGrandi();
                        Ok2 = false;
                    }
                    txtControllo.setText(Scritta);
                    if (Ok2) {
                        txtControllo.setTextColor(Color.BLACK);
                    } else {
                        txtControllo.setTextColor(Color.RED);
                    }
                    Ok = true;
                }
            }
            ImageView imgApreControllo = view.findViewById(R.id.imgApreControllo);
            if (Ok) {
                layControllo.setVisibility(LinearLayout.VISIBLE);
                imgApreControllo.setVisibility(LinearLayout.VISIBLE);

                imgApreControllo.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MainControlloImmagini.class);
                        intent.putExtra("idCategoria", Integer.toString(idCategoria));
                        context.startActivity(intent);
                    }
                });
            } else {
                layControllo.setVisibility(LinearLayout.GONE);
                imgApreControllo.setVisibility(LinearLayout.GONE);
            }

            LinearLayout layUguali = view.findViewById(R.id.layUguale);
            TextView txtUguali = view.findViewById(R.id.txtUguale);
            boolean OkU = false;
            for (StrutturaControlloImmagini s : VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini()) {
                if (s.getIdCategoria() == idCategoria) {
                    List<StrutturaImmaginiUguali> ss = s.getListaUguali();
                    if (!ss.isEmpty()) {
                        String Messaggio = "";
                        List<StrutturaUgualiInterna> lii = new ArrayList<>();
                        for (StrutturaImmaginiUguali sss : ss) {
                            String Tipo = sss.getTipo().toUpperCase().trim();
                            String Filtro2 = sss.getFiltro();
                            if (sss.getQuanti() > 0) {
                                boolean ok = false;
                                for (StrutturaUgualiInterna l : lii) {
                                    if (l.getTipo().equals(Tipo)) {
                                        ok = true;
                                        int Quanti = l.getQuanti();
                                        Quanti += sss.getQuanti();
                                        l.setQuanti(Quanti);
                                        break;
                                    }
                                }
                                if (!ok) {
                                    StrutturaUgualiInterna l = new StrutturaUgualiInterna();
                                    l.setFiltro(Filtro2);
                                    l.setQuanti(sss.getQuanti());
                                    l.setTipo(Tipo);
                                    lii.add(l);
                                }
                            }
                        }
                        for (StrutturaUgualiInterna sii : lii) {
                            if (sii.getQuanti() > 0) {
                                Messaggio += sii.getTipo().toUpperCase() + ": " + sii.getQuanti() + " - ";
                            }
                        }
                        if (!Messaggio.isEmpty()) {
                            Messaggio = Messaggio.substring(0, Messaggio.length() - 3);
                            txtUguali.setText("UGUALI: " + Messaggio);
                            OkU = true;
                        }
                    }
                }
            }
            ;
            ImageView imgApreUguali = view.findViewById(R.id.imgApreUguale);
            if (OkU) {
                layUguali.setVisibility(LinearLayout.VISIBLE);
                imgApreUguali.setVisibility(LinearLayout.VISIBLE);

                imgApreUguali.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent iP = new Intent(VariabiliStaticheMostraImmagini.getInstance().getCtx(), MainImmaginiUguali.class);
                        iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle b = new Bundle();
                        b.putString("CATEGORIA", Categoria);
                        iP.putExtras(b);
                        context.startActivity(iP);
                    }
                });
            } else {
                layUguali.setVisibility(LinearLayout.GONE);
                imgApreUguali.setVisibility(LinearLayout.GONE);
            }

            LinearLayout layFC = view.findViewById(R.id.layFuoriCategoria);
            TextView txtFC = view.findViewById(R.id.txtFuoriCategoria);
            boolean OkFC = false;
            for (StrutturaControlloImmagini s : VariabiliStaticheUtilityImmagini.getInstance().getControlloImmagini()) {
                if (s.getIdCategoria() == idCategoria) {
                    int Quante = s.getListaFC().size();
                    if (Quante == 0) {
                        txtFC.setTextColor(Color.BLACK);
                    } else {
                        txtFC.setText("IMMAGINI FUORI CATEGORIA: " + Quante);
                        OkFC = true;
                        txtFC.setTextColor(Color.RED);
                    }
                }
            }
            ;
            ImageView imgApreFC = view.findViewById(R.id.imgApreFC);
            if (OkFC) {
                layFC.setVisibility(LinearLayout.VISIBLE);
                imgApreFC.setVisibility(LinearLayout.VISIBLE);

                imgApreFC.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent iP = new Intent(VariabiliStaticheMostraImmagini.getInstance().getCtx(), MainImmaginiFuoriCategoria.class);
                        iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle b = new Bundle();
                        b.putString("IDCATEGORIA", String.valueOf(idCategoria));
                        b.putString("CATEGORIA", Categoria);
                        iP.putExtras(b);
                        context.startActivity(iP);
                    }
                });
            } else {
                layFC.setVisibility(LinearLayout.GONE);
                imgApreFC.setVisibility(LinearLayout.GONE);
            }

            ImageView imgRefresh = view.findViewById(R.id.imgRefreshImmagini);
            imgRefresh.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    ChiamateWSUI ws = new ChiamateWSUI(context);
                    ws.RefreshImmagini(String.valueOf(idCategoria), true);
                }
            });

            ImageView imgUpload = view.findViewById(R.id.imgUploadImmagini);
            imgUpload.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    VariabiliStaticheMostraImmagini.getInstance().setCategoria(Categoria);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Nome salvataggio");

                    final EditText input = new EditText(context);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setText(Categoria.replace("_", " "));
                    builder.setView(input);

                    String finalCategoria = Categoria;
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String Salvataggio = input.getText().toString();
                            if (Salvataggio.isEmpty()) {
                                UtilitiesGlobali.getInstance().ApreToast(context,
                                        "Immettere un nome categoria");
                            } else {
                                VariabiliScaricaImmagini.getInstance().setListaDaScaricare(new ArrayList<>());

                                ChiamateWSMI ws = new ChiamateWSMI(context);
                                ws.ScaricaImmagini(finalCategoria, Salvataggio);
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
        }

        return view;
    }
}
