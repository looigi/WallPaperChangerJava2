package com.looigi.wallpaperchanger2.classeImmaginiRaggruppate;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.UtilityImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.VariabiliImmaginiFuoriCategoria;
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

        ChiamateWSMI c = new ChiamateWSMI(context);
        c.RitornaCategorie(false, "IR");

        VariabiliStaticheImmaginiRaggruppate.getInstance().setLstIR(findViewById(R.id.lstIR));
        VariabiliStaticheImmaginiRaggruppate.getInstance().setLstImmagini(findViewById(R.id.lstImmagini));
        VariabiliStaticheImmaginiRaggruppate.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoIR));
        VariabiliStaticheImmaginiRaggruppate.getInstance().Attesa(false);

        VariabiliStaticheImmaginiRaggruppate.getInstance().setLaypreview(findViewById(R.id.layPreview));
        VariabiliStaticheImmaginiRaggruppate.getInstance().setImgPreview(findViewById(R.id.imgPreview));
        VariabiliStaticheImmaginiRaggruppate.getInstance().getLaypreview().setVisibility(LinearLayout.GONE);

        ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheImmaginiRaggruppate.getInstance().getLaypreview().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgRefreshIR= findViewById(R.id.imgRefreshIR);
        imgRefreshIR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSIR ws = new ChiamateWSIR(context);
                ws.RitornaRaggruppamenti(idCategoria);
            }
        });

        ImageView imgSpostaTutte = findViewById(R.id.imgSpostaTutte);
        imgSpostaTutte.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vogliono spostare tutte le immagini alla categoria " +
                            VariabiliStaticheImmaginiRaggruppate.getInstance().getCategoriaImpostata() +
                                "?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        VariabiliStaticheImmaginiRaggruppate.getInstance().setIdImmagineDaSpostare(0);
                        VariabiliStaticheImmaginiRaggruppate.getInstance().setStaSpostandoImmagini(true);
                        VariabiliStaticheImmaginiRaggruppate.getInstance().SpostaTutteLeImmagini(context);
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
