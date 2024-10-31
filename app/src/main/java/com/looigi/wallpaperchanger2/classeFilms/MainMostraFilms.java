package com.looigi.wallpaperchanger2.classeFilms;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFilms.webservice.ChiamateWSF;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classePlayer.db_dati_player;
import com.looigi.wallpaperchanger2.classeVideo.VariabiliStaticheVideo;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

public class MainMostraFilms extends Activity {
    private static String NomeMaschera = "Main_Mostra_Films";
    private Context context;
    private Activity act;
    private boolean SettaggiAperti = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_films);

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
                db.ChiudeDB();

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

        VariabiliStaticheFilms.getInstance().setFilmsView(findViewById(R.id.videoViewFilms));
        VariabiliStaticheFilms.getInstance().setPbLoading(findViewById(R.id.pbFilmsLoading));
        VariabiliStaticheFilms.getInstance().setTxtTitolo(findViewById(R.id.txtTitoloFilms));
        VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.GONE);
        VariabiliStaticheFilms.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        EditText txtFiltro = findViewById(R.id.edtFiltroFilms);
        txtFiltro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    VariabiliStaticheFilms.getInstance().setEntratoNelCampoDiTesto(false);
                } else {
                    VariabiliStaticheFilms.getInstance().setEntratoNelCampoDiTesto(true);
                }
            }
        });

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
                String id = String.valueOf(VariabiliStaticheFilms.getInstance().getIdUltimoFilms());
                db_dati_films db = new db_dati_films(context);
                if (db.VedeSnapshot(id)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Films già scansionato.\nSi vuole procedere di nuovo alla cattura ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.ScriveSnapshot(id);
                            UtilityFilms.getInstance().takeScreenshot(context);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else {
                    db.ScriveSnapshot(id);
                    UtilityFilms.getInstance().takeScreenshot(context);
                }
                db.ChiudeDB();
            }
        });

        ImageView imgScreenShotM = findViewById(R.id.imgScreenshotMultipliF);
        imgScreenShotM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheFilms.getInstance().getIdUltimoFilms());
                db_dati_films db = new db_dati_films(context);
                if (db.VedeSnapshot(id)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Films già scansionato.\nSi vuole procedere di nuovo alla cattura ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            db.ScriveSnapshot(id);
                            UtilityFilms.getInstance().takeScreenShotMultipli(context);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                } else {
                    db.ScriveSnapshot(id);
                    UtilityFilms.getInstance().takeScreenShotMultipli(context);
                }
                db.ChiudeDB();
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
        db.ChiudeDB();

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

        if (VariabiliStaticheFilms.getInstance().getFilmsView() != null) {
            VariabiliStaticheFilms.getInstance().getFilmsView().stopPlayback();
            VariabiliStaticheFilms.getInstance().getFilmsView().clearAnimation();
            VariabiliStaticheFilms.getInstance().getFilmsView().suspend(); // clears media player
            VariabiliStaticheFilms.getInstance().getFilmsView().setVideoURI(null);
            VariabiliStaticheFilms.getInstance().setFilmsView(null);
        }

        act.finish();
    }
}
