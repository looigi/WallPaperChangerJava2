package com.looigi.wallpaperchanger2.classePennetta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classePennetta.webservice.ChiamateWSPEN;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePennetta.webservice.DownloadImmaginePEN;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.OnSwipeTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainMostraPennetta extends Activity {
    private Context context;
    private Activity act;

    public MainMostraPennetta() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_pennetta);

        context = this;
        act = this;

        VariabiliStaticheMostraImmaginiPennetta.getInstance().setAct(act);
        VariabiliStaticheMostraImmaginiPennetta.getInstance().setCtx(context);

        VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoria("");
        VariabiliStaticheMostraImmaginiPennetta.getInstance().setFiltro("");
        VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdImmagine(1);
        // VariabiliStaticheMostraImmaginiPennetta.getInstance().setRandom("S");

        VariabiliStaticheMostraImmaginiPennetta.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoPEN));

        // db_dati_pennetta db = new db_dati_pennetta(context);
        // db.CaricaImpostazioni();

        ChiamateWSPEN ws = new ChiamateWSPEN(context);
        ws.RitornaCategorie();

        VariabiliStaticheMostraImmaginiPennetta.getInstance().setTxtInfo(findViewById(R.id.txtInfoImmagine));

        VariabiliStaticheMostraImmaginiPennetta.getInstance().setImg(findViewById(R.id.imgLibrary));
        // ImageView imgIndietro = findViewById(R.id.imgIndietroLibrary);
        // ImageView imgAvanti = findViewById(R.id.imgAvantiLibrary);

        ImageView imgImposta = findViewById(R.id.imgImpostaWallpaper);
        imgImposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityPennetta.getInstance().Attesa(true);

                StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmaginiPennetta.getInstance().getUltimaImmagineCaricata();

                StrutturaImmagine src = new StrutturaImmagine();
                src.setPathImmagine(context.getFilesDir() + "/Immagini/AppoggioPEN.jpg");
                src.setImmagine(s.getNomeFile());
                src.setDimensione("");
                src.setDataImmagine(s.getDataCreazione());

                ChangeWallpaper c = new ChangeWallpaper(context);
                c.setWallpaperLocale(context, src);

                UtilityPennetta.getInstance().Attesa(false);
            }
        });

        ImageView imgSettings = (ImageView) findViewById(R.id.imgSettings);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iP = new Intent(VariabiliStaticheMostraImmaginiPennetta.getInstance().getCtx(), MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle b = new Bundle();
                                b.putString("qualeSettaggio", "PENNETTA");
                                iP.putExtras(b);
                                VariabiliStaticheMostraImmaginiPennetta.getInstance().getCtx().startActivity(iP);
                            }
                        }, 500);
                    }
                };
                handlerTimer.postDelayed(rTimer, 1000);
            }
        });

        ImageView imgScorri = findViewById(R.id.imgScorri);
        imgScorri.setOnTouchListener(new OnSwipeTouchListener(context) {
            public void onSwipeTop() {
                // UtilitiesGlobali.getInstance().ApreToast(context, "Swipe Top");
            }
            public void onSwipeRight() {
                // UtilitiesGlobali.getInstance().ApreToast(context, "Swipe Right");
                UtilityPennetta.getInstance().TornaIndietro(context);
            }
            public void onSwipeLeft() {
                // UtilitiesGlobali.getInstance().ApreToast(context, "Swipe Left");
                UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
            }
            public void onSwipeBottom() {
                // UtilitiesGlobali.getInstance().ApreToast(context, "Swipe Bottom");
            }
        });

        EditText edtFiltro = findViewById(R.id.edtFiltroImmagimi);
        edtFiltro.setText(VariabiliStaticheMostraImmaginiPennetta.getInstance().getFiltro());
        edtFiltro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setFiltro(edtFiltro.getText().toString());

                    UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
                }
            }
        });

        boolean letto = false;
        String path1 = context.getFilesDir() + "/Immagini";
        UtilityWallpaper.getInstance().CreaCartelle(path1);
        String NomeFile = "/UltimaPennetta.txt";
        if (UtilityWallpaper.getInstance().EsisteFile(path1 + NomeFile)) {
            String u = UtilityDetector.getInstance().LeggeFileDiTesto(path1 + NomeFile);
            if (u != null) {
                String[] uu = u.split("§");

                if (uu.length > 2) {
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoria(uu[2]);
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdImmagine(Integer.parseInt(uu[1]));
                    String path = VariabiliStaticheMostraImmaginiPennetta.PathUrl + uu[0];

                    StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary();
                    s.setUrlImmagine(path);
                    s.setCategoria(VariabiliStaticheMostraImmaginiPennetta.getInstance().getCategoria());
                    s.setNomeFile(uu[0]);
                    s.setDataCreazione("");

                    VariabiliStaticheMostraImmaginiPennetta.getInstance().AggiungeCaricata();

                    VariabiliStaticheMostraImmaginiPennetta.getInstance().ScriveInfoImmagine(s);

                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setUltimaImmagineCaricata(s);

                    DownloadImmaginePEN d = new DownloadImmaginePEN();
                    d.EsegueChiamata(
                            context,
                            path,
                            VariabiliStaticheMostraImmaginiPennetta.getInstance().getImg(),
                            path
                    );
                    // new DownloadImagePEN(context, path,
                    //         VariabiliStaticheMostraImmaginiPennetta.getInstance().getImg()).execute(path);

                    letto = true;
                /* JSONObject jObject = null;
                try {
                    jObject = new JSONObject(u);
                    StrutturaImmaginiLibrary si = UtilityPennetta.getInstance().prendeStruttura(jObject);

                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoria(si.getCategoria());
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdImmagine(si.getIdImmagine());

                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setUltimaImmagineCaricata(si);

                    VariabiliStaticheMostraImmaginiPennetta.getInstance().ScriveInfoImmagine(si);

                    VariabiliStaticheMostraImmaginiPennetta.getInstance().AggiungeCaricata();

                    new DownloadImagePEN(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmaginiPennetta.getInstance().getImg()).execute(si.getUrlImmagine());

                    letto = true;
                } catch (JSONException ignored) {
                } */
                }
            }
        }

        final boolean[] primoIngresso = {true};
        VariabiliStaticheMostraImmaginiPennetta.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        VariabiliStaticheMostraImmaginiPennetta.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoria("");
                    UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
                } else {
                    for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmaginiPennetta.getInstance().getListaCategorie()) {
                        if (s.getCategoria().equals(Categoria)) {
                            VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoria(s.getCategoria());
                            UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
                            break;
                        }
                    }
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

        ImageView imgSlideShow = findViewById(R.id.imgSlideShow);
        imgSlideShow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean attivo = VariabiliStaticheMostraImmaginiPennetta.getInstance().isSlideShowAttivo();
                attivo = !attivo;
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setSlideShowAttivo(attivo);

                Bitmap bmSS;
                if (attivo) {
                    bmSS = BitmapFactory.decodeResource(context.getResources(), R.drawable.slideshow_on);
                    UtilityPennetta.getInstance().AttivaTimerSlideShow(context);
                } else {
                    bmSS = BitmapFactory.decodeResource(context.getResources(), R.drawable.slideshow_off);
                    UtilityPennetta.getInstance().BloccaTimerSlideShow();
                }
                imgSlideShow.setImageBitmap(bmSS);
            }
        });

        if (!letto) {
            UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
        }

        /* imgAvanti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RitornaProssimaImmagine(ws);
            }
        }); */
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }

    /* @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        act.finish();

        super.onKeyDown(keyCode, event);

        /* Utility.getInstance().ScriveLog(this, NomeMaschera,
                "Tasto premuto: " + Integer.toString(keyCode)); * /

        switch(keyCode) {
            case KeyEvent.KEYCODE_BACK:
                VariabiliStaticheMostraImmaginiPennetta.getInstance().getAct().finish();

                return false;
        }

        return false;
    } */
}
