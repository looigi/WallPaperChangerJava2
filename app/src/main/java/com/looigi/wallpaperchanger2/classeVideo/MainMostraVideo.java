package com.looigi.wallpaperchanger2.classeVideo;

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
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeVideo.webservice.ChiamateWSV;

public class MainMostraVideo extends Activity {
    private static String NomeMaschera = "Main_Mostra_Video";
    private Context context;
    private Activity act;
    private boolean SettaggiAperti = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        context = this;
        act = this;

        VariabiliStaticheVideo.getInstance().setContext(context);
        VariabiliStaticheVideo.getInstance().setAct(act);

        SettaggiAperti = VariabiliStaticheVideo.getInstance().isSettingsAperto();
        ImageView imgLinguetta = findViewById(R.id.imgLinguettaVideo);
        LinearLayout laySettaggi = findViewById(R.id.laySettaggiVideo);
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
                VariabiliStaticheVideo.getInstance().setSettingsAperto(SettaggiAperti);
                db_dati_video db = new db_dati_video(context);
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

        ImageView imgRefreshCat = findViewById(R.id.imgRefreshCategorieVideo);
        imgRefreshCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSV c = new ChiamateWSV(context);
                c.RitornaCategorie();
            }
        });

        ImageView imgSettings = (ImageView) findViewById(R.id.imgSettingsVideo);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iP = new Intent(VariabiliStaticheVideo.getInstance().getContext(), MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle b = new Bundle();
                                b.putString("qualeSettaggio", "VIDEO");
                                iP.putExtras(b);
                                VariabiliStaticheVideo.getInstance().getContext().startActivity(iP);
                            }
                        }, 500);
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }
        });

        VariabiliStaticheVideo.getInstance().setVideoView(findViewById(R.id.videoView));
        VariabiliStaticheVideo.getInstance().setPbLoading(findViewById(R.id.pbVideoLoading));
        VariabiliStaticheVideo.getInstance().setTxtTitolo(findViewById(R.id.txtTitoloVideo));
        VariabiliStaticheVideo.getInstance().getPbLoading().setVisibility(View.GONE);
        VariabiliStaticheVideo.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        EditText txtFiltro = findViewById(R.id.edtFiltroVideo);
        ImageView imgCerca = findViewById(R.id.imgCerca);
        imgCerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String filtro = txtFiltro.getText().toString();
                VariabiliStaticheVideo.getInstance().setFiltro(filtro);

                ChiamateWSV ws = new ChiamateWSV(context);
                ws.RitornaProssimoVideo();
            }
        });

        ImageView imgScreenShot = findViewById(R.id.imgScreenshot);
        imgScreenShot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityVideo.getInstance().takeScreenshot(context);
            }
        });

        final boolean[] primoIngresso = {true};
        VariabiliStaticheVideo.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    VariabiliStaticheVideo.getInstance().setCategoria("");
                } else {
                    VariabiliStaticheVideo.getInstance().setCategoria(Categoria);
                }

                // ChiamateWSV ws = new ChiamateWSV(context);
                // ws.RitornaProssimoVideo();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        db_dati_video db = new db_dati_video(context);
        String url = db.CaricaVideo();

        if (!url.isEmpty()) {
            String[] u = url.split("/");
            String res = u[u.length - 1];
            res = VariabiliStaticheVideo.getInstance().getIdUltimoVideo() + ": " + res;
            VariabiliStaticheVideo.getInstance().getTxtTitolo().setText(res);
        }

        ChiamateWSV ws = new ChiamateWSV(context);
        ws.RitornaCategorie();

        if (!url.isEmpty()) {
            UtilityVideo.getInstance().ImpostaVideo();
        } else {
            ChiamateWSV ws2 = new ChiamateWSV(context);
            ws2.RitornaProssimoVideo();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }
}
