package com.looigi.wallpaperchanger2.classeMostraImmagini;

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
import com.looigi.wallpaperchanger2.classeMostraImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeMostraImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeMostraImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.classeMostraImmagini.webservice.DownloadImageMI;
import com.looigi.wallpaperchanger2.utilities.OnSwipeTouchListener;

import org.json.JSONException;
import org.json.JSONObject;

public class MainMostraImmagini extends Activity {
    private Context context;
    private Activity act;

    public MainMostraImmagini() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostra_immagini);

        context = this;
        act = this;

        VariabiliStaticheMostraImmagini.getInstance().setAct(act);
        VariabiliStaticheMostraImmagini.getInstance().setCtx(context);

        VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(-999);
        VariabiliStaticheMostraImmagini.getInstance().setFiltro("");
        VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(1);
        VariabiliStaticheMostraImmagini.getInstance().setRandom("S");

        VariabiliStaticheMostraImmagini.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoPEN));

        // db_dati_immagini db = new db_dati_immagini(context);
        // db.CaricaImpostazioni();

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.RitornaCategorie();

        VariabiliStaticheMostraImmagini.getInstance().setTxtInfo(findViewById(R.id.txtInfoImmagine));

        VariabiliStaticheMostraImmagini.getInstance().setImg(findViewById(R.id.imgLibrary));
        // ImageView imgIndietro = findViewById(R.id.imgIndietroLibrary);
        // ImageView imgAvanti = findViewById(R.id.imgAvantiLibrary);

        ImageView imgImposta = findViewById(R.id.imgImpostaWallpaper);
        imgImposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityImmagini.getInstance().Attesa(true);

                StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata();

                StrutturaImmagine src = new StrutturaImmagine();
                src.setPathImmagine(context.getFilesDir() + "/Immagini/AppoggioMI.jpg");
                src.setImmagine(s.getNomeFile());
                src.setDimensione("");
                src.setDataImmagine(s.getDataCreazione());

                ChangeWallpaper c = new ChangeWallpaper(context);
                c.setWallpaperLocale(context, src);

                UtilityImmagini.getInstance().Attesa(false);
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
                                Intent iP = new Intent(VariabiliStaticheMostraImmagini.getInstance().getCtx(), MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle b = new Bundle();
                                b.putString("qualeSettaggio", "IMMAGINI");
                                iP.putExtras(b);
                                VariabiliStaticheMostraImmagini.getInstance().getCtx().startActivity(iP);
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
                UtilityImmagini.getInstance().TornaIndietro(context);
            }
            public void onSwipeLeft() {
                // UtilitiesGlobali.getInstance().ApreToast(context, "Swipe Left");
                UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
            }
            public void onSwipeBottom() {
                // UtilitiesGlobali.getInstance().ApreToast(context, "Swipe Bottom");
            }
        });

        EditText edtFiltro = findViewById(R.id.edtFiltroImmagimi);
        edtFiltro.setText(VariabiliStaticheMostraImmagini.getInstance().getFiltro());
        edtFiltro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    VariabiliStaticheMostraImmagini.getInstance().setFiltro(edtFiltro.getText().toString());

                    UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
                }
            }
        });

        boolean letto = false;
        String path1 = context.getFilesDir() + "/Immagini";
        UtilityWallpaper.getInstance().CreaCartelle(path1);
        String NomeFile = "/UltimaImmagine.txt";
        if (UtilityWallpaper.getInstance().EsisteFile(path1 + NomeFile)) {
            String u = UtilityDetector.getInstance().LeggeFileDiTesto(path1 + NomeFile);
            if (u != null) {
                JSONObject jObject = null;
                try {
                    jObject = new JSONObject(u);
                    StrutturaImmaginiLibrary si = UtilityImmagini.getInstance().prendeStruttura(jObject);

                    VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(si.getIdCategoria());
                    VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(si.getIdImmagine());

                    VariabiliStaticheMostraImmagini.getInstance().setUltimaImmagineCaricata(si);

                    VariabiliStaticheMostraImmagini.getInstance().ScriveInfoImmagine(si);

                    VariabiliStaticheMostraImmagini.getInstance().AggiungeCaricata();

                    new DownloadImageMI(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmagini.getInstance().getImg()).execute(si.getUrlImmagine());

                    letto = true;
                } catch (JSONException ignored) {
                }
            }
        }

        final boolean[] primoIngresso = {true};
        VariabiliStaticheMostraImmagini.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        VariabiliStaticheMostraImmagini.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(-1);
                    UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
                } else {
                    for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
                        if (s.getCategoria().equals(Categoria)) {
                            VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(s.getIdCategoria());
                            UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
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
                boolean attivo = VariabiliStaticheMostraImmagini.getInstance().isSlideShowAttivo();
                attivo = !attivo;
                VariabiliStaticheMostraImmagini.getInstance().setSlideShowAttivo(attivo);

                Bitmap bmSS;
                if (attivo) {
                    bmSS = BitmapFactory.decodeResource(context.getResources(), R.drawable.slideshow_on);
                    UtilityImmagini.getInstance().AttivaTimerSlideShow(context);
                } else {
                    bmSS = BitmapFactory.decodeResource(context.getResources(), R.drawable.slideshow_off);
                    UtilityImmagini.getInstance().BloccaTimerSlideShow();
                }
                imgSlideShow.setImageBitmap(bmSS);
            }
        });

        if (!letto) {
            UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
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
                VariabiliStaticheMostraImmagini.getInstance().getAct().finish();

                return false;
        }

        return false;
    } */
}
