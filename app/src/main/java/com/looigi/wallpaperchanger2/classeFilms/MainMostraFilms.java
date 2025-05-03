package com.looigi.wallpaperchanger2.classeFilms;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeFilms.webservice.ChiamateWSF;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
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
                c.RitornaCategorie(true);
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
        VariabiliStaticheFilms.getInstance().setTxtId(findViewById(R.id.txtIdFilm));
        VariabiliStaticheFilms.getInstance().setTxtCate(findViewById(R.id.txtCategoriaFilm));
        VariabiliStaticheFilms.getInstance().setTxtTitolo(findViewById(R.id.txtTitoloFilms));
        VariabiliStaticheFilms.getInstance().getPbLoading().setVisibility(View.GONE);
        VariabiliStaticheFilms.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));

        EditText txtFiltro = findViewById(R.id.edtFiltroFilms);
        txtFiltro.setText(VariabiliStaticheFilms.getInstance().getFiltro());
        txtFiltro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    db_dati_films db = new db_dati_films(context);
                    db.ScriveImpostazioni();

                    VariabiliStaticheFilms.getInstance().setEntratoNelCampoDiTesto(false);
                    VariabiliStaticheFilms.getInstance().setFiltro(txtFiltro.getText().toString());
                } else {
                    VariabiliStaticheFilms.getInstance().setEntratoNelCampoDiTesto(true);
                }
            }
        });

        EditText txtFiltroCate = findViewById(R.id.edtFiltroCategoriaFilms);
        txtFiltroCate.setText(VariabiliStaticheFilms.getInstance().getFiltroCategoria());
        txtFiltroCate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if (!hasFocus) {
                    VariabiliStaticheFilms.getInstance().setFiltroCategoria(txtFiltroCate.getText().toString());

                    db_dati_films db = new db_dati_films(context);
                    db.ScriveImpostazioni();

                    UtilityFilms.getInstance().AggiornaCategorie(context);
                // }
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

        ImageView imgElimina = findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheFilms.getInstance().getIdUltimoFilms());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Si vuole eliminare il Film ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSF c = new ChiamateWSF(context);
                        c.EliminaFilm(id);
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

        ImageView imgScreenShot = findViewById(R.id.imgScreenshot);
        imgScreenShot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheFilms.getInstance().getIdUltimoFilms());

                db_dati_films db = new db_dati_films(context);
                boolean fatto = db.VedeSnapshotS(id);
                db.ChiudeDB();

                if (fatto) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Immagine già scansionata per questo film.\nSi vuole procedere di nuovo alla cattura ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UtilityFilms.getInstance().takeScreenshot(context, id);
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
                    UtilityFilms.getInstance().takeScreenshot(context, id);
                }
            }
        });

        ImageView imgScreenShotM = findViewById(R.id.imgScreenshotMultipliF);
        imgScreenShotM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheFilms.getInstance().getIdUltimoFilms());

                db_dati_films db = new db_dati_films(context);
                boolean fatto = db.VedeSnapshot(id);
                db.ChiudeDB();

                if (fatto) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Films già scansionato.\nSi vuole procedere di nuovo alla cattura ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UtilityFilms.getInstance().takeScreenShotMultipli(context, id);
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
                    UtilityFilms.getInstance().takeScreenShotMultipli(context, id);
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
            VariabiliStaticheFilms.getInstance().ScriveInfo(url);
        }

        ImpostaSpostamento(act);

        ChiamateWSF ws = new ChiamateWSF(context);
        ws.RitornaCategorie(false);

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

    private void ImpostaSpostamento(Activity act) {
        LinearLayout laySposta = act.findViewById(R.id.laySposta);
        laySposta.setVisibility(LinearLayout.GONE);

        ImageView imgApre = act.findViewById(R.id.imgSpostaACategoria);
        imgApre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySposta.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgSpostaImmagine = act.findViewById(R.id.imgSpostaImmagine);
        imgSpostaImmagine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSF c = new ChiamateWSF(context);
                c.SpostaFilm();

                laySposta.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaSposta = act.findViewById(R.id.imgAnnullaSposta);
        imgAnnullaSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySposta.setVisibility(LinearLayout.GONE);
            }
        });

        EditText edtFiltroSpostamento = findViewById(R.id.edtSpostaFiltroCategoria);
        edtFiltroSpostamento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliStaticheFilms.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                UtilityFilms.getInstance().AggiornaCategorieSpostamento(context);
            }
        });

        VariabiliStaticheFilms.getInstance().setIdCategoriaSpostamento("");
        VariabiliStaticheFilms.getInstance().setSpnSpostaCategorie(findViewById(R.id.spnSpostaCategorie));
        final boolean[] primoIngresso = {true};
        VariabiliStaticheFilms.getInstance().getSpnSpostaCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    UtilitiesGlobali.getInstance().ApreToast(context,
                            "Impostare una categoria. Non Tutte");
                } else {
                    for (String s : VariabiliStaticheFilms.getInstance().getListaCategorie()) {
                        if (s.equals(Categoria)) {
                            VariabiliStaticheFilms.getInstance().setIdCategoriaSpostamento(s);
                            break;
                        }
                    }
                    if (VariabiliStaticheFilms.getInstance().getIdCategoriaSpostamento().isEmpty()) {
                        UtilitiesGlobali.getInstance().ApreToast(context,
                                "Categoria non valida: " + Categoria);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
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
