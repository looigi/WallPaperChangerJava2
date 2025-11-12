package com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRicerca;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRicerca.webService.ChiamateWSIFC;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import java.util.ArrayList;
import java.util.List;

public class MainImmaginiFuoriCategoria extends Activity {
    private Context context;
    private Activity act;
    private String txtNuovaCategoria;

    public MainImmaginiFuoriCategoria() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_immagini_fuori_categoria);

        context = this;
        act = this;

        Intent intent = getIntent();
        VariabiliImmaginiFuoriCategoria.getInstance().setIdCategoria(intent.getStringExtra("IDCATEGORIA"));
        VariabiliImmaginiFuoriCategoria.getInstance().setCategoria(intent.getStringExtra("CATEGORIA"));
        String Ricerca = intent.getStringExtra("RICERCA");
        if (Ricerca == null) { Ricerca = ""; }
        VariabiliImmaginiFuoriCategoria.getInstance().setRicerca(Ricerca);

        // TextView txtNuovaCategoria = findViewById(R.id.txtNuovaCategoria);
        // txtNuovaCategoria.setText("");

        VariabiliImmaginiFuoriCategoria.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoIFC));
        UtilitiesGlobali.getInstance().AttesaGif(
                context,
                VariabiliImmaginiFuoriCategoria.getInstance().getImgCaricamento(),
                false
        );

        LinearLayout layCategorie = findViewById(R.id.layCategorie);

        if (!VariabiliImmaginiFuoriCategoria.getInstance().getIdCategoria().equals("-1")
            || !VariabiliImmaginiFuoriCategoria.getInstance().getRicerca().isEmpty()) {
            VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento(
                    VariabiliImmaginiFuoriCategoria.getInstance().getIdCategoria()
            );
            VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaInserita(VariabiliImmaginiFuoriCategoria.getInstance().getCategoria());
            layCategorie.setVisibility(LinearLayout.GONE);
        } else {
            layCategorie.setVisibility(LinearLayout.VISIBLE);

            ImageView imgNuovaCategoria = findViewById(R.id.imgNuova);
            imgNuovaCategoria.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String Filtro = VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias1().getText().toString() + " " +
                            VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias2().getText().toString();
                    String NuovaCategoria = Filtro.replace(";", " ");

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Immagini fuori categoria");
                    builder.setMessage("Nome nuova categoria");

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
                            VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaInserita(valoreInserito);
                            VariabiliImmaginiFuoriCategoria.getInstance().setCategoria(valoreInserito);

                            if (!ok) {
                                ChiamateWSMI c = new ChiamateWSMI(context);
                                c.CreaNuovaCategoria(valoreInserito, "FC");
                            } else {
                                String[] ll2 = new String[VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM().size() + 1];
                                int i2 = 0;
                                for (StrutturaImmaginiCategorie l2: VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                                    if (l2.getCategoria() != null) {
                                        ll2[i2] = l2.getCategoria();
                                    }
                                    i2++;
                                }
                                int i = 0;
                                for (String lll: ll2) {
                                    if (lll == null || lll.isEmpty()) {
                                        ll2[i] = "**NULL**";
                                    }
                                    i++;
                                }
                                UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                        VariabiliImmaginiFuoriCategoria.getInstance().getSpnCategorie(),
                                        ll2,
                                        VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaInserita()
                                );
                                UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                                        VariabiliImmaginiFuoriCategoria.getInstance().getSpnCategorieRicerca(),
                                        ll2,
                                        VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaRicerca()
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

            EditText edtFiltro = findViewById(R.id.edtFiltroCategorie);
            edtFiltro.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Non serve implementare
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<StrutturaImmaginiCategorie> lista = new ArrayList<>();
                    if (s.toString().isEmpty()) {
                        lista = VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM();
                    } else {
                        for (StrutturaImmaginiCategorie s2 : VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                            if (s2.getCategoria().toUpperCase().contains(s.toString().toUpperCase())) {
                                lista.add(s2);
                            }
                        }
                    }

                    String[] ll2 = new String[lista.size()];
                    int i = 0;
                    for (StrutturaImmaginiCategorie lll: lista) {
                        ll2[i] = lll.getCategoria();
                        i++;
                    }
                    VariabiliImmaginiFuoriCategoria.getInstance().setCategoria(ll2[0]);
                    VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaInserita(ll2[0]);

                    UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                            VariabiliImmaginiFuoriCategoria.getInstance().getSpnCategorieRicerca(),
                            ll2,
                            VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaInserita()
                    );
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Non serve implementare
                }
            });

            EditText edtFiltroRicerca = findViewById(R.id.edtFiltroCategorieRicerca);
            edtFiltroRicerca.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Non serve implementare
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    List<StrutturaImmaginiCategorie> lista = new ArrayList<>();
                    if (s.toString().isEmpty()) {
                        lista = VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM();
                    } else {
                        for (StrutturaImmaginiCategorie s2 : VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                            if (s2.getCategoria().toUpperCase().contains(s.toString().toUpperCase())) {
                                lista.add(s2);
                            }
                        }
                    }

                    String[] ll2 = new String[lista.size()];
                    int i = 0;
                    for (StrutturaImmaginiCategorie lll: lista) {
                        ll2[i] = lll.getCategoria();
                        i++;
                    }
                    VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaRicerca(ll2[0]);

                    UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                            VariabiliImmaginiFuoriCategoria.getInstance().getSpnCategorieRicerca(),
                            ll2,
                            VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaRicerca()
                    );
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Non serve implementare
                }
            });

            VariabiliImmaginiFuoriCategoria.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));

            final boolean[] primoIngressoGen = {true};
            VariabiliImmaginiFuoriCategoria.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    if (primoIngressoGen[0]) {
                        primoIngressoGen[0] = false;
                        return;
                    }

                    String Categoria = adapterView.getItemAtPosition(position).toString();
                    // VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaImpostata(Categoria);
                    VariabiliImmaginiFuoriCategoria.getInstance().setCategoria(Categoria);
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });

            VariabiliImmaginiFuoriCategoria.getInstance().setSpnCategorieRicerca(findViewById(R.id.spnCategoriaRicerca));
            final boolean[] primoIngressoCat = {true};
            VariabiliImmaginiFuoriCategoria.getInstance().getSpnCategorieRicerca().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    if (primoIngressoCat[0]) {
                        primoIngressoCat[0] = false;
                        return;
                    }

                    String Categoria = adapterView.getItemAtPosition(position).toString();
                    // VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaImpostata(Categoria);
                    VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaRicerca(Categoria);

                    VariabiliImmaginiFuoriCategoria.getInstance().setIdCategoriaRicerca(null);

                    for (StrutturaImmaginiCategorie s : VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                        if (s.getCategoria().toUpperCase().trim().equals(VariabiliImmaginiFuoriCategoria.getInstance().getCategoriaRicerca().toUpperCase().trim())) {
                            VariabiliImmaginiFuoriCategoria.getInstance().setIdCategoriaRicerca(
                                    Integer.toString(s.getIdCategoria())
                            );
                            break;
                        }
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapter) {  }
            });
        }

        ChiamateWSMI c = new ChiamateWSMI(context);
        c.RitornaCategorie(false, "FC");

        /* if (VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().contains("_")) {
            String[] Aliases = VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().split("_");
            VariabiliImmaginiFuoriCategoria.getInstance().setAlias1(Aliases[0]);
            VariabiliImmaginiFuoriCategoria.getInstance().setAlias2(Aliases[1]);
        } else {
            VariabiliImmaginiFuoriCategoria.getInstance().setAlias1(VariabiliImmaginiFuoriCategoria.getInstance().getCategoria());
            VariabiliImmaginiFuoriCategoria.getInstance().setAlias2("");
        } */

        VariabiliImmaginiFuoriCategoria.getInstance().setEdtAlias1(findViewById(R.id.edtAlias1));
        // edtAlias1.setText(VariabiliImmaginiFuoriCategoria.getInstance().getAlias1());
        VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias1().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String Alias1 = VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias1().getText().toString();
                String Alias2 = VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias2().getText().toString();
                txtNuovaCategoria = Alias1 + " " + Alias2;
            }
        });

        VariabiliImmaginiFuoriCategoria.getInstance().setEdtAlias2(findViewById(R.id.edtAlias2));
        // edtAlias2.setText(VariabiliImmaginiFuoriCategoria.getInstance().getAlias2());
        VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias2().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String Alias1 = VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias1().getText().toString();
                String Alias2 = VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias2().getText().toString();
                txtNuovaCategoria = Alias1 + " " + Alias2;
            }
        });

        String Alias1 = "";
        String Alias2 = "";
        if (!VariabiliImmaginiFuoriCategoria.getInstance().getIdCategoria().equals("-1")) {
            if (VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().contains("_")) {
                String[] c2 = VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().split("_");
                Alias1 = c2[0];
                Alias2 = c2[c2.length - 1];
            } else {
                int lungh = VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().length();
                Alias1 = VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().substring(0, lungh / 2);
                Alias2 = VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().substring((lungh / 2), lungh);
            }
            VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias1().setText(Alias1);
            VariabiliImmaginiFuoriCategoria.getInstance().getEdtAlias2().setText(Alias2);
        }
        // VariabiliImmaginiFuoriCategoria.getInstance().setAlias1(Alias1);
        // VariabiliImmaginiFuoriCategoria.getInstance().setAlias2(Alias2);

        VariabiliImmaginiFuoriCategoria.getInstance().setQuantiCaratteri(findViewById(R.id.edtCaratteri));
        VariabiliImmaginiFuoriCategoria.getInstance().getQuantiCaratteri().setText("4");
        /* edtCaratteri.setText(Integer.toString(VariabiliImmaginiFuoriCategoria.getInstance().getQuantiCaratteri()));
        edtCaratteri.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliImmaginiFuoriCategoria.getInstance().setQuantiCaratteri(
                        Integer.parseInt(edtCaratteri.getText().toString())
                );
            }
        }); */

        // VariabiliImmaginiFuoriCategoria.getInstance().setEdtTag(findViewById(R.id.edtTag));
        /* edtTag.setText(VariabiliImmaginiFuoriCategoria.getInstance().getTag());
        edtTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliImmaginiFuoriCategoria.getInstance().setTag(edtTag.getText().toString());
            }
        }); */

        Spinner spnAndOr = findViewById(R.id.spnAndOr);
        String[] l = { "And", "Or" };
        VariabiliImmaginiFuoriCategoria.getInstance().setAndOr("And");

        UtilitiesGlobali.getInstance().ImpostaSpinner(
                context,
                spnAndOr,
                l,
                VariabiliImmaginiFuoriCategoria.getInstance().getAndOr()
        );

        // ArrayAdapter<String> adapter = new ArrayAdapter<String>
        //         (context, android.R.layout.simple_spinner_item, l);
        /* ArrayAdapter<String> adapter = UtilitiesGlobali.getInstance().CreaAdapterSpinner(
                context,
                l
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnAndOr.setAdapter(adapter);
        spnAndOr.setPrompt(VariabiliImmaginiFuoriCategoria.getInstance().getAndOr()); */
        final boolean[] primoIngresso = {true};
        spnAndOr.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String AndOr = adapterView.getItemAtPosition(position).toString();
                VariabiliImmaginiFuoriCategoria.getInstance().setAndOr(AndOr);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        LinearLayout layCategoriaRicerca = findViewById(R.id.layCategoriaRicerca);

        SwitchCompat swcSoloAltro = findViewById(R.id.switchSoloSuAltro);
        swcSoloAltro.setChecked(VariabiliImmaginiFuoriCategoria.getInstance().isSoloSuAltro());
        if (VariabiliImmaginiFuoriCategoria.getInstance().isSoloSuAltro()) {
            layCategoriaRicerca.setVisibility(LinearLayout.GONE);
        } else {
            layCategoriaRicerca.setVisibility(LinearLayout.VISIBLE);
        }
        swcSoloAltro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliImmaginiFuoriCategoria.getInstance().setSoloSuAltro(isChecked);
                if (isChecked) {
                    layCategoriaRicerca.setVisibility(LinearLayout.GONE);
                } else {
                    layCategoriaRicerca.setVisibility(LinearLayout.VISIBLE);
                }
            }
        });

        SwitchCompat swcCercaExif = findViewById(R.id.switchCercaSuExif);
        swcCercaExif.setChecked(VariabiliImmaginiFuoriCategoria.getInstance().isCercaExif());
        swcCercaExif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                VariabiliImmaginiFuoriCategoria.getInstance().setCercaExif(isChecked);
            }
        });

        CheckBox chkTutti = findViewById(R.id.chkTutti);
        chkTutti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int i2 = 0;
                for (StrutturaImmagineFuoriCategoria s : VariabiliImmaginiFuoriCategoria.getInstance().getListaImmagini()) {
                    StrutturaImmagineFuoriCategoria s2 = s;
                    s2.setSelezionata(chkTutti.isChecked());
                    VariabiliImmaginiFuoriCategoria.getInstance().getListaImmagini().set(i2, s2);
                    i2++;
                }
                VariabiliImmaginiFuoriCategoria.getInstance().getAdapter().notifyDataSetChanged();
            }
        });

        ImageView imgCerca = findViewById(R.id.imgCerca);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSIFC ws = new ChiamateWSIFC(context);
                ws.RitornaImmaginiFuoriCategoria();
            }
        });

        VariabiliImmaginiFuoriCategoria.getInstance().setLstImmagini(findViewById(R.id.lstImmagini));
        // VariabiliImmaginiFuoriCategoria.getInstance().setLaypreview(findViewById(R.id.layPreview));
        // VariabiliImmaginiFuoriCategoria.getInstance().setImgPreview(findViewById(R.id.imgPreview));
        // VariabiliImmaginiFuoriCategoria.getInstance().getLaypreview().setVisibility(LinearLayout.GONE);
        VariabiliImmaginiFuoriCategoria.getInstance().setTxtQuanteImmaginiRilevate(findViewById(R.id.txtQuanti));
        VariabiliImmaginiFuoriCategoria.getInstance().getTxtQuanteImmaginiRilevate().setText("");

        /* ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliImmaginiFuoriCategoria.getInstance().getLaypreview().setVisibility(LinearLayout.GONE);
            }
        }); */

        ImageView imgPulisceCat = findViewById(R.id.imgPulisceCat);
        imgPulisceCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliImmaginiFuoriCategoria.getInstance().setIdCategoriaRicerca(null);
                VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaRicerca("");
                EditText edtFiltroRicerca = findViewById(R.id.edtFiltroCategorieRicerca);
                edtFiltroRicerca.setText("");

                String[] ll2 = new String[VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM().size() + 1];
                int i2 = 0;
                for (StrutturaImmaginiCategorie l2: VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                    if (l2.getCategoria() != null) {
                        ll2[i2] = l2.getCategoria();
                    }
                    i2++;
                }
                int i = 0;
                for (String lll: ll2) {
                    if (lll == null || lll.isEmpty()) {
                        ll2[i] = "**NULL**";
                    }
                    i++;
                }
                UtilitiesGlobali.getInstance().ImpostaSpinner(context,
                        VariabiliImmaginiFuoriCategoria.getInstance().getSpnCategorieRicerca(),
                        ll2,
                        ""
                );
            }
        });

        ImageView imgSpostaTutte = findViewById(R.id.imgSpostaTutte);
        imgSpostaTutte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String NuovaCategoria = VariabiliImmaginiFuoriCategoria.getInstance().getCategoria().toUpperCase().trim();

                if (VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM() == null) {
                    ChiamateWSMI c = new ChiamateWSMI(context);
                    c.RitornaCategorie(false, "FC");
                }

                VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento("");
                // if (VariabiliStaticheMostraImmagini.getInstance().getIdCategoriaSpostamento() == null) {
                    for (StrutturaImmaginiCategorie s : VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                        if (s.getCategoria().toUpperCase().trim().equals(NuovaCategoria)) {
                            VariabiliImmaginiFuoriCategoria.getInstance().setCategoriaInserita(s.getCategoria());
                            VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento(
                                    Integer.toString(s.getIdCategoria())
                            );
                            NuovaCategoria = s.getCategoria();
                            break;
                        }
                    }
                // }
                if (VariabiliStaticheMostraImmagini.getInstance().getIdCategoriaSpostamento().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Id Categoria di spostamento non valida");
                    return;
                }

                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("Immagini fuori categoria");
                builder.setMessage("Si vogliono spostare le immagini selezionate alla categoria " +
                        NuovaCategoria + " ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        List<StrutturaImmagineFuoriCategoria> lista = new ArrayList<>();
                        for (StrutturaImmagineFuoriCategoria s : VariabiliImmaginiFuoriCategoria.getInstance().getListaImmagini()) {
                            if (s.isSelezionata()) {
                                lista.add(s);
                            }
                        }

                        if (!lista.isEmpty()) {
                            VariabiliImmaginiFuoriCategoria.getInstance().setListaDaSpostare(lista);
                            VariabiliImmaginiFuoriCategoria.getInstance().setQualeImmagineStaSpostando(0);
                            int quale = VariabiliImmaginiFuoriCategoria.getInstance().getQualeImmagineStaSpostando();
                            VariabiliImmaginiFuoriCategoria.getInstance().setStaSpostandoTutte(true);
                            VariabiliImmaginiFuoriCategoria.getInstance().setQualeImmagineStaSpostando(quale);

                            VariabiliImmaginiFuoriCategoria.getInstance().ScaricaProssimaImmagine(context, 0);
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

        if (!VariabiliImmaginiFuoriCategoria.getInstance().getIdCategoria().equals("-1")) {
            ChiamateWSIFC ws = new ChiamateWSIFC(context);
            ws.RitornaImmaginiFuoriCategoria();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return false;
        }

        return false;
    }
}
