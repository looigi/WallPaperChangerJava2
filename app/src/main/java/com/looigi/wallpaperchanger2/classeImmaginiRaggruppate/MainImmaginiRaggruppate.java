package com.looigi.wallpaperchanger2.classeImmaginiRaggruppate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.strutture.StrutturaImmagineRaggruppata;
import com.looigi.wallpaperchanger2.classeImmaginiRaggruppate.webService.ChiamateWSIR;
import com.looigi.wallpaperchanger2.classePazzia.GestioneCategorie.StrutturaCategorieFinali;
import com.looigi.wallpaperchanger2.classePazzia.VariabiliStatichePazzia;
import com.looigi.wallpaperchanger2.classeVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class MainImmaginiRaggruppate extends Activity {
    private Context context;
    private Activity act;

    public MainImmaginiRaggruppate() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_immagini_raggruppate);

        context = this;
        act = this;

        Intent intent = getIntent();
        String idCategoria = intent.getStringExtra("idCategoria");
        String Modalita = intent.getStringExtra("Modalita");
        VariabiliStaticheImmaginiRaggruppate.getInstance().setIdCategoria(idCategoria);
        VariabiliStaticheImmaginiRaggruppate.getInstance().setModalita(Modalita);

        VariabiliStaticheImmaginiRaggruppate.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        VariabiliStaticheImmaginiRaggruppate.getInstance().setTxtQuante(findViewById(R.id.txtQuante));

        RadioButton optMetodo1 = findViewById(R.id.optMetodo1);
        RadioButton optMetodo2 = findViewById(R.id.optMetodo2);
        RadioButton optMetodo3 = findViewById(R.id.optMetodo3);

        SharedPreferences prefs = getSharedPreferences("ImmaginiRaggruppdate", MODE_PRIVATE);
        String nome = prefs.getString("Metodo", "3");
        VariabiliStaticheImmaginiRaggruppate.getInstance().setMetodo(nome);
        switch (nome) {
            case "1":
                optMetodo1.setChecked(true);
                break;
            case "2":
                optMetodo2.setChecked(true);
                break;
            case "3":
                optMetodo3.setChecked(true);
                break;
        }

        optMetodo1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImmaginiRaggruppate.getInstance().setMetodo("1");

                SharedPreferences prefs = getSharedPreferences("ImmaginiRaggruppdate", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Metodo", "1"); // Chiave e valore
                editor.apply(); // o editor.commit();

                ChiamateWSIR ws = new ChiamateWSIR(context);
                ws.RitornaRaggruppamenti(idCategoria);
            }
        });
        optMetodo2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImmaginiRaggruppate.getInstance().setMetodo("2");

                SharedPreferences prefs = getSharedPreferences("ImmaginiRaggruppdate", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Metodo", "2"); // Chiave e valore
                editor.apply(); // o editor.commit();

                ChiamateWSIR ws = new ChiamateWSIR(context);
                ws.RitornaRaggruppamenti(idCategoria);
            }
        });
        optMetodo3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImmaginiRaggruppate.getInstance().setMetodo("3");

                SharedPreferences prefs = getSharedPreferences("ImmaginiRaggruppdate", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("Metodo", "3"); // Chiave e valore
                editor.apply(); // o editor.commit();

                ChiamateWSIR ws = new ChiamateWSIR(context);
                ws.RitornaRaggruppamenti(idCategoria);
            }
        });

        ChiamateWSMI c = new ChiamateWSMI(context);
        c.RitornaCategorie(false, "IR");

        VariabiliStaticheImmaginiRaggruppate.getInstance().setLstIR(findViewById(R.id.lstIR));
        VariabiliStaticheImmaginiRaggruppate.getInstance().setLstImmagini(findViewById(R.id.lstImmagini));
        VariabiliStaticheImmaginiRaggruppate.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoIR));
        VariabiliStaticheImmaginiRaggruppate.getInstance().Attesa(false);

        /* VariabiliStaticheImmaginiRaggruppate.getInstance().setLaypreview(findViewById(R.id.layPreview));
        VariabiliStaticheImmaginiRaggruppate.getInstance().setImgPreview(findViewById(R.id.imgPreview));
        VariabiliStaticheImmaginiRaggruppate.getInstance().getLaypreview().setVisibility(LinearLayout.GONE);

        ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImmaginiRaggruppate.getInstance().getLaypreview().setVisibility(LinearLayout.GONE);
            }
        }); */

        ImageView imgRefreshIR= findViewById(R.id.imgRefreshIR);
        imgRefreshIR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSIR ws = new ChiamateWSIR(context);
                ws.RitornaRaggruppamenti(idCategoria);
            }
        });

        CheckBox chkTutte = findViewById(R.id.imgSelezionaTutte);
        chkTutte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean cosa = false;

                for (StrutturaImmagineRaggruppata s : VariabiliStaticheImmaginiRaggruppate.getInstance().getListaImmagini()) {
                    if (s.isSelezionata()) {
                        cosa = true;
                        break;
                    }
                }
                for (StrutturaImmagineRaggruppata s : VariabiliStaticheImmaginiRaggruppate.getInstance().getListaImmagini()) {
                    s.setSelezionata(!cosa);
                }

                VariabiliStaticheImmaginiRaggruppate.getInstance().getCustomAdapterT().notifyDataSetChanged();
            }
        });

        ImageView imgSpostaTutte = findViewById(R.id.imgSpostaTutte);
        imgSpostaTutte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String NuovaCategoria = VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata().toUpperCase().trim();

                // if (VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM() == null) {
                    ChiamateWSMI c = new ChiamateWSMI(context);
                    c.RitornaCategorie(false, "FC");
                // }

                VariabiliStaticheImmaginiRaggruppate.getInstance().setCategoriaImpostata("");
                // if (VariabiliStaticheMostraImmagini.getInstance().getIdCategoriaSpostamento() == null) {
                for (StrutturaImmaginiCategorie s : VariabiliImmaginiFuoriCategoria.getInstance().getListaCategorieIMM()) {
                    if (s.getCategoria().toUpperCase().trim().equals(NuovaCategoria)) {
                        VariabiliStaticheImmaginiRaggruppate.getInstance().setCategoriaImpostata(s.getCategoria());
                        break;
                    }
                }
                // }

                if (VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata().isEmpty() || VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata() == null) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Categoria di destinazione nulla");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Immagini raggruppate");
                builder.setMessage("Si vogliono spostare tutte le immagini selezionate alla categoria " +
                            VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata() +
                                "?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int quale = VariabiliStaticheImmaginiRaggruppate.getInstance().CercaProssimoNumeroDaSpostare(-1);
                        if (quale > -1) {
                            // VariabiliStaticheImmaginiRaggruppate.getInstance().setIdImmagineDaSpostare(quale);
                            VariabiliStaticheImmaginiRaggruppate.getInstance().setStaSpostandoImmagini(true);
                            VariabiliStaticheImmaginiRaggruppate.getInstance().SpostaTutteLeImmagini(context);
                        } else {
                            UtilitiesGlobali.getInstance().ApreToast(context, "Nessun immagine da spostare");
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

        ImageView imgRefreshCategorie = findViewById(R.id.imgRefreshCategorie);
        imgRefreshCategorie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSMI c = new ChiamateWSMI(context);
                c.RitornaCategorie(true, "IR");
            }
        });

        ChiamateWSIR ws = new ChiamateWSIR(context);
        ws.RitornaRaggruppamenti(idCategoria);

        final boolean[] primoIngresso = {true};
        VariabiliStaticheImmaginiRaggruppate.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                VariabiliStaticheImmaginiRaggruppate.getInstance().setCategoriaImpostata(Categoria);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
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
