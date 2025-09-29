package com.looigi.wallpaperchanger2.classeSpostamento;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.flexbox.FlexboxLayout;
import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classePreview.UtilitiesPreview;
import com.looigi.wallpaperchanger2.classePreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.classePreview.webService.DownloadImmaginePreview;
import com.looigi.wallpaperchanger2.classeSpostamento.webService.ChiamateWSSP;
import com.looigi.wallpaperchanger2.classeSpostamento.webService.DownloadImmagineSpostamento;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.util.Collections;

public class MainSpostamento extends Activity {
    private Context context;
    private Activity act;

    // pinimg;romantik;wheel;tata;nylon;fantasy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_spostamento);

        context = this;
        act = this;

        VariabiliStaticheSpostamento.getInstance().setAct(act);

        Intent intent = getIntent();
        String Modalita = intent.getStringExtra("Modalita");
        String idImmagine = intent.getStringExtra("idImmagine");

        VariabiliStaticheSpostamento.getInstance().setModalita(Modalita);
        VariabiliStaticheSpostamento.getInstance().setIdImmagine(idImmagine);

        VariabiliStaticheSpostamento.getInstance().setSpnCategorie(findViewById(R.id.spnSpostaCategorie));

        VariabiliStaticheSpostamento.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoPreview));
        VariabiliStaticheSpostamento.getInstance().Attesa(false);

        EditText edtFiltroSpostamento = findViewById(R.id.edtSpostaFiltroCategoria);
        /* edtFiltroSpostamento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliStaticheSpostamento.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                VariabiliStaticheSpostamento.getInstance().AggiornaCategorieSpostamento(context);
            }
        }); */

        ImageView imgRefreshCat = findViewById(R.id.imgRefreshCat);
        imgRefreshCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheSpostamento.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                VariabiliStaticheSpostamento.getInstance().AggiornaCategorieSpostamento(context);
            }
        });

        ChiamateWSSP c = new ChiamateWSSP(context);
        c.TornaCategoriePerImmaginiContenute(false);

        final boolean[] primoIngresso = {true};
        VariabiliStaticheSpostamento.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                VariabiliStaticheSpostamento.getInstance().setCategoriaSpostamento(Categoria);
                VariabiliStaticheSpostamento.getInstance().ImpostaIdCategoria(Categoria);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        VariabiliStaticheSpostamento.getInstance().setTxtSpostamento(findViewById(R.id.txtSpostamento));
        VariabiliStaticheSpostamento.getInstance().setImgSpostamento(findViewById(R.id.imgSpostamento));

        ImageView imgSposta = findViewById(R.id.imgEsegueSpostamento);
        imgSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheSpostamento.getInstance().getIdCategoriaSpostamento() == -1) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Selezionare una categoria");
                    return;
                }

                VariabiliStaticheSpostamento.getInstance().AggiungeSpostata(
                        context,
                        VariabiliStaticheSpostamento.getInstance().getCategoriaSpostamento()
                );

                StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary(); // CREARE LA STRUTTURA PER LO SPOSTAMENTO
                s.setIdImmagine(Integer.parseInt(idImmagine));

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s, "SPOSTAMENTO");
            }
        });

        ImageView imgRefresh = findViewById(R.id.imgRefresh);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                c.TornaCategoriePerImmaginiContenute(true);
            }
        });

        // PREFERITI
        VariabiliStaticheSpostamento.getInstance().setLayPreferiti(findViewById(R.id.layPreferiti));

        db_dati_spostamento db = new db_dati_spostamento(context);
        db.CreazioneTabelle();
        AggiornaPreferiti(db);

        ImageView imgAggiungeCategoriaVeloce = findViewById(R.id.imgAggiungeCategoriaVeloce);
        imgAggiungeCategoriaVeloce.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheSpostamento.getInstance().getCategoriaSpostamento().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Selezionare una categoria");
                    return;
                }

                db.ScrivePreferito(VariabiliStaticheSpostamento.getInstance().getCategoriaSpostamento());
                AggiornaPreferiti(db);
            }
        });
        // PREFERITI
    }

    private void AggiornaPreferiti(db_dati_spostamento db) {
        db.LeggePreferiti();

        VariabiliStaticheSpostamento.getInstance().getLayPreferiti().removeAllViews();
        Collections.sort(VariabiliStaticheSpostamento.getInstance().getPreferiti());
        for (String p: VariabiliStaticheSpostamento.getInstance().getPreferiti()) {
            addDynamicButton(p);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return true;
        }

        return false;
    }

    private void addDynamicButton(String text) {
        Button button = new Button(this);
        button.setText(text);
        button.setIncludeFontPadding(false);
        button.setTextSize(11);
        button.setMinHeight(0);
        button.setMinimumHeight(0);

        FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                FlexboxLayout.LayoutParams.WRAP_CONTENT,
                FlexboxLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(1, 1, 1, 1);
        button.setLayoutParams(params);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VariabiliStaticheSpostamento.getInstance().ImpostaIdCategoria(text);

                String idImmagine = VariabiliStaticheSpostamento.getInstance().getIdImmagine();

                VariabiliStaticheSpostamento.getInstance().AggiungeSpostata(
                        context,
                        text
                );

                StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary(); // CREARE LA STRUTTURA PER LO SPOSTAMENTO
                s.setIdImmagine(Integer.parseInt(idImmagine));

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s, "SPOSTAMENTO");
            }
        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Spostamento");
                builder.setMessage("Si vuole eliminare il preferito selezionato (" + text + ") ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db_dati_spostamento db = new db_dati_spostamento(context);
                        db.EliminaPreferito(text);
                        AggiornaPreferiti(db);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

                return false;
            }
        });

        // Aggiungi al layout
        VariabiliStaticheSpostamento.getInstance().getLayPreferiti().addView(button);
    }
}
