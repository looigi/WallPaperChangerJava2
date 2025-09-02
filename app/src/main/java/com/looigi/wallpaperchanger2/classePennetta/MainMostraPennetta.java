package com.looigi.wallpaperchanger2.classePennetta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.db_dati_immagini;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeModificaImmagine.MainModificaImmagine;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classePennetta.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classePennetta.webservice.ChiamateWSPEN;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classePennetta.webservice.DownloadImmaginePEN;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.OnSwipeTouchListener;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.io.IOException;

public class MainMostraPennetta extends Activity {
    private Context context;
    private Activity act;
    private boolean SettaggiAperti = false;

    public MainMostraPennetta() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_pennetta);

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

        // VariabiliStaticheMostraImmaginiPennetta.getInstance().setTxtId(findViewById(R.id.txtIdImmagine));
        // VariabiliStaticheMostraImmaginiPennetta.getInstance().setTxtCate(findViewById(R.id.txtCategoriaImmagine));
        // VariabiliStaticheMostraImmaginiPennetta.getInstance().setTxtInfo(findViewById(R.id.txtInfoImmagine));
        VariabiliStaticheMostraImmaginiPennetta.getInstance().setTxtInfoSotto(findViewById(R.id.txtInfoSotto));
        VariabiliStaticheMostraImmaginiPennetta.getInstance().getTxtInfoSotto().setText("");

        VariabiliStaticheMostraImmaginiPennetta.getInstance().setImg(findViewById(R.id.imgLibrary));
        // ImageView imgIndietro = findViewById(R.id.imgIndietroLibrary);
        // ImageView imgAvanti = findViewById(R.id.imgAvantiLibrary);

        SettaggiAperti = VariabiliStaticheMostraImmaginiPennetta.getInstance().isSettingsAperto();
        ImageView imgLinguetta = findViewById(R.id.imgLinguettaPEN);
        LinearLayout laySettaggi = findViewById(R.id.laySettaggiPEN);
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
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setSettingsAperto(SettaggiAperti);
                db_dati_pennetta db = new db_dati_pennetta(context);
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

        ImageView imgModifica = findViewById(R.id.imgModificaPEN);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Path = context.getFilesDir() + "/Immagini/AppoggioPEN.jpg";

                com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata();

                VariabiliStaticheModificaImmagine.getInstance().setMascheraApertura("PENNETTA");
                VariabiliStaticheModificaImmagine.getInstance().setNomeImmagine(
                        Path
                );
                Intent i = new Intent(context, MainModificaImmagine.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

                // ChiamateWSMI c = new ChiamateWSMI(context);
                // c.EliminaImmagine(id);
            }
        });

        ImageView imgElimina = findViewById(R.id.imgEliminaPennetta);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheMostraImmagini.getInstance().getIdImmagine());
                ChiamateWSPEN c = new ChiamateWSPEN(context);
                c.EliminaImmagine(id);
            }
        });

        ImageView imgRefreshCat = findViewById(R.id.imgRefreshCategoriePEN);
        imgRefreshCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSPEN c = new ChiamateWSPEN(context);
                c.RitornaCategorie(true, "PENNETTA");
            }
        });

        ImageView imgShare = findViewById(R.id.imgSharePennetta);
        imgShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata() != null) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(VariabiliStaticheMostraImmaginiPennetta.getInstance().getUltimaImmagineCaricata().getPathImmagine());
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT,VariabiliStaticheMostraImmaginiPennetta.getInstance().getUltimaImmagineCaricata().getPathImmagine());
                    // i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM,uri);
                    i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i,"Share immagine pennetta"));
                }
            }
        });

        ImageView imgImposta = findViewById(R.id.imgImpostaWallpaper);
        imgImposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityPennetta.getInstance().Attesa(true);

                StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmaginiPennetta.getInstance().getUltimaImmagineCaricata();

                String Path = context.getFilesDir() + "/Immagini/AppoggioPEN.jpg";
                String PathImp = context.getFilesDir() + "/Immagini/AppoggioPEN_Impostata.jpg";

                try {
                    Files.getInstance().CopiaFile(Path, PathImp);

                    StrutturaImmagine src = new StrutturaImmagine();
                    src.setPathImmagine(PathImp);
                    src.setImmagine(s.getNomeFile());
                    src.setDimensione("");
                    src.setDataImmagine(s.getDataCreazione());

                    UtilityPennetta.getInstance().ScriveInfoSotto(s);

                    ChangeWallpaper c = new ChangeWallpaper(context,"PENNETTA", src);
                    c.setWallpaperLocale(context, src);

                    UtilityPennetta.getInstance().Attesa(false);
                } catch (IOException ignored) {

                }
            }
        });

        ImageView imgSettings = (ImageView) findViewById(R.id.imgSettingsPEN);
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
        /* edtFiltro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if (hasFocus) {
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setFiltro(edtFiltro.getText().toString());

                    db_dati_pennetta db = new db_dati_pennetta(context);
                    db.ScriveImpostazioni();

                    // UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
                // }
            }
        }); */

        ImageView imgFiltraImmagini = findViewById(R.id.imgFiltraImmagini);
        imgFiltraImmagini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setFiltro(edtFiltro.getText().toString());

                db_dati_pennetta db = new db_dati_pennetta(context);
                db.ScriveImpostazioni();

                // UtilityPennetta.getInstance().RitornaProssimaImmagine(context);
            }
        });

        EditText txtFiltroCate = findViewById(R.id.edtFiltroCategoriaPEN);
        txtFiltroCate.setText(VariabiliStaticheMostraImmaginiPennetta.getInstance().getFiltroCategoria());
        /* txtFiltroCate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if (hasFocus) {
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setFiltroCategoria(txtFiltroCate.getText().toString());

                    db_dati_pennetta db = new db_dati_pennetta(context);
                    db.ScriveImpostazioni();

                    UtilityPennetta.getInstance().AggiornaCategorie(context);
                // }
            }
        }); */

        ImageView imgFiltraCombo = findViewById(R.id.imgFiltraCombo);
        imgFiltraCombo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setFiltroCategoria(txtFiltroCate.getText().toString());

                db_dati_pennetta db = new db_dati_pennetta(context);
                db.ScriveImpostazioni();

                UtilityPennetta.getInstance().AggiornaCategorie(context);
            }
        });

        boolean letto = false;
        String path1 = context.getFilesDir() + "/Immagini";
        UtilityWallpaper.getInstance().CreaCartelle(path1);
        String NomeFile = "/UltimaPennetta.txt";
        if (UtilityWallpaper.getInstance().EsisteFile(path1 + NomeFile)) {
            String u = UtilityDetector.getInstance().LeggeFileDiTesto(path1 + NomeFile);
            if (u.contains("anyType{}")) {
                u = null;
                Files.getInstance().EliminaFileUnico(path1 + NomeFile);
            }
            if (u != null) {
                String[] uu = u.split("§");

                if (uu.length > 2) {
                    String Categoria = uu[0];
                    String[] c = Categoria.split("/");
                    Categoria = c[0] + "\\" + c[1];

                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoria(Categoria);
                    VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdImmagine(Integer.parseInt(uu[1]));
                    String path = VariabiliStaticheMostraImmaginiPennetta.PathUrl + uu[0];

                    StrutturaImmaginiLibrary s = new StrutturaImmaginiLibrary();
                    s.setUrlImmagine(path);
                    s.setCategoria(VariabiliStaticheMostraImmaginiPennetta.getInstance().getCategoria());
                    s.setNomeFile(uu[0]);
                    s.setDataCreazione("");
                    s.setImmaginiFiltrate(Integer.parseInt(uu[1]));
                    s.setImmaginiCategoria(Integer.parseInt(uu[2]));

                    VariabiliStaticheMostraImmaginiPennetta.getInstance().AggiungeCaricata();
                    UtilityPennetta.getInstance().ScriveInfoSotto(s);

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
                }
            }
        }

        db_dati_pennetta db = new db_dati_pennetta(context);
        VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoriaAttuale(db.LeggeUltimaCategoria());
        db.ChiudeDB();

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
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setCategoriaAttuale(Categoria);

                db_dati_pennetta db = new db_dati_pennetta(context);
                db.ScriveUltimaCategoria(Categoria);
                db.ChiudeDB();

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

        ImpostaSpostamento(act);

        ChiamateWSPEN ws = new ChiamateWSPEN(context);
        ws.RitornaCategorie(false, "PENNETTA");
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
                StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmaginiPennetta.getInstance().getUltimaImmagineCaricata();

                ChiamateWSPEN c = new ChiamateWSPEN(context);
                c.SpostaImmagine(s);

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
                VariabiliStaticheMostraImmaginiPennetta.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                UtilityPennetta.getInstance().AggiornaCategorieSpostamento(context);
            }
        });

        VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdCategoriaSpostamento("");
        VariabiliStaticheMostraImmaginiPennetta.getInstance().setSpnSpostaCategorie(findViewById(R.id.spnSpostaCategorie));
        final boolean[] primoIngresso = {true};
        VariabiliStaticheMostraImmaginiPennetta.getInstance().getSpnSpostaCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    String idCategoria = "";
                    for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmaginiPennetta.getInstance().getListaCategorie()) {
                        if (s.getCategoria().equals(Categoria)) {
                            VariabiliStaticheMostraImmaginiPennetta.getInstance().setIdCategoriaSpostamento(String.valueOf(s.getIdCategoria()));
                            break;
                        }
                    }
                    if (VariabiliStaticheMostraImmaginiPennetta.getInstance().getIdCategoriaSpostamento().isEmpty()) {
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
