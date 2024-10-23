package com.looigi.wallpaperchanger2.classeFilms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeFilms.webservice.ChiamateWSF;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classePennetta.VariabiliStaticheMostraImmaginiPennetta;
import com.looigi.wallpaperchanger2.classeVideo.UtilityVideo;
import com.looigi.wallpaperchanger2.classeVideo.webservice.ChiamateWSV;

public class MainMostraFilms extends Activity {
    private static String NomeMaschera = "Main_Mostra_Films";
    private Context context;
    private Activity act;
    private boolean SettaggiAperti = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_films);

        context = this;
        act = this;

        VariabiliStaticheFilms.getInstance().setContext(context);
        VariabiliStaticheFilms.getInstance().setAct(act);

        SettaggiAperti = VariabiliStaticheFilms.getInstance().isSettingsAperto();
        ImageView imgLinguetta = findViewById(R.id.imgLinguettaFilms);
        LinearLayout laySettaggi = findViewById(R.id.laySettaggiFilms);
        if (SettaggiAperti) {
            laySettaggi.setLayoutParams(
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT));
        } else {
            laySettaggi.setLayoutParams(
                    new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT,
                            1));
        }

        imgLinguetta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SettaggiAperti = !SettaggiAperti;
                VariabiliStaticheFilms.getInstance().setSettingsAperto(SettaggiAperti);
                db_dati_films db = new db_dati_films(context);
                db.ScriveImpostazioni();

                if (!SettaggiAperti) {
                    laySettaggi.setLayoutParams(
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    1));
                } else {
                    laySettaggi.setLayoutParams(
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT));
                }
            }
        });

        ImageView imgRefreshCat = findViewById(R.id.imgRefreshCategorieFilms);
        imgRefreshCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSF c = new ChiamateWSF(context);
                c.RitornaCategorie();
            }
        });

        ImageView imgRefreshImm = findViewById(R.id.imgSettingsFilms);
        imgRefreshImm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iP = new Intent(VariabiliStaticheFilms.getInstance().getContext(), MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle b = new Bundle();
                                b.putString("qualeSettaggio", "FILMS");
                                iP.putExtras(b);
                                VariabiliStaticheFilms.getInstance().getContext().startActivity(iP);
                            }
                        }, 500);
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }
        });

        VariabiliStaticheFilms.getInstance().setFilmsView(findViewById(R.id.videoView));
        VariabiliStaticheFilms.getInstance().setPbLoading(findViewById(R.id.pbVideoLoading));
        VariabiliStaticheFilms.getInstance().setTxtTitolo(findViewById(R.id.txtTitoloFilms));
        VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.GONE);
        VariabiliStaticheFilms.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        EditText txtFiltro = findViewById(R.id.edtFiltroFilms);
        ImageView imgCerca = findViewById(R.id.imgCerca);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filtro = txtFiltro.getText().toString();
                VariabiliStaticheFilms.getInstance().setFiltro(filtro);

                ChiamateWSF ws = new ChiamateWSF(context);
                ws.RitornaProssimoFilms();
            }
        });

        ImageView imgScreenShot = findViewById(R.id.imgScreenshot);
        imgScreenShot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityFilms.getInstance().takeScreenshot(context);
            }
        });

        final boolean[] primoIngresso = {true};
        VariabiliStaticheFilms.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    VariabiliStaticheFilms.getInstance().setCategoria("");
                } else {
                    VariabiliStaticheFilms.getInstance().setCategoria(Categoria);
                }

                // ChiamateWSV ws = new ChiamateWSV(context);
                // ws.RitornaProssimoFilms();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        db_dati_films db = new db_dati_films(context);
        String url = db.CaricaFilms();

        if (!url.isEmpty()) {
            String[] u = url.split("/");
            String res = u[u.length - 1];
            res = VariabiliStaticheFilms.getInstance().getIdUltimoFilms() + ": " + res;
            VariabiliStaticheFilms.getInstance().getTxtTitolo().setText(res);
        }

        ChiamateWSF ws = new ChiamateWSF(context);
        ws.RitornaCategorie();

        if (!url.isEmpty()) {
            UtilityFilms.getInstance().ImpostaFilms();
        } else {
            ChiamateWSF ws2 = new ChiamateWSF(context);
            ws2.RitornaProssimoFilms();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }
}
