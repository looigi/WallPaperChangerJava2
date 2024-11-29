package com.looigi.wallpaperchanger2.classeFetekkie;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiCategorieFE;
import com.looigi.wallpaperchanger2.classeFetekkie.strutture.StrutturaImmaginiLibraryFE;
import com.looigi.wallpaperchanger2.classeFetekkie.webservice.ChiamateWSFET;
import com.looigi.wallpaperchanger2.classeFetekkie.webservice.DownloadImmagineFET;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeModificaImmagine.Main_ModificaImmagine;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.OnSwipeTouchListener;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainMostraFetekkie extends Activity {
    private static final int PICKFILE_RESULT_CODE = 27;
    private Context context;
    private Activity act;
    private boolean SettaggiAperti = false;

    public MainMostraFetekkie() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fetekkie);

        context = this;
        act = this;

        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setAct(act);
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setCtx(context);

        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setCategoria("");
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setFiltro("");
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setIdImmagine(1);
        // VariabiliStaticheMostraImmaginiFetekkie.getInstance().setRandom("S");

        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoFET));

        // db_dati_fetekkie db = new db_dati_fetekkie(context);
        // db.CaricaImpostazioni();

        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setTxtInfo(findViewById(R.id.txtInfoImmagine));
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setTxtId(findViewById(R.id.txtIdImmagine));
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setTxtCate(findViewById(R.id.txtCategoriaImmagine));

        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setImg(findViewById(R.id.imgLibrary));
        // ImageView imgIndietro = findViewById(R.id.imgIndietroLibrary);
        // ImageView imgAvanti = findViewById(R.id.imgAvantiLibrary);

        SettaggiAperti = VariabiliStaticheMostraImmaginiFetekkie.getInstance().isSettingsAperto();
        ImageView imgLinguetta = findViewById(R.id.imgLinguettaFET);
        LinearLayout laySettaggi = findViewById(R.id.laySettaggiFET);
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
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setSettingsAperto(SettaggiAperti);
                db_dati_fetekkie db = new db_dati_fetekkie(context);
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

        ImageView imgModifica = findViewById(R.id.imgModificaFET);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Path = context.getFilesDir() + "/Immagini/AppoggioFET.jpg";

                VariabiliStaticheModificaImmagine.getInstance().setMascheraApertura("FETEKKIE");
                VariabiliStaticheModificaImmagine.getInstance().setNomeImmagine(
                        Path
                );
                Intent i = new Intent(context, Main_ModificaImmagine.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        ImageView imgUpload = findViewById(R.id.imgUploadFET);
        imgUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Categoria = VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoria();

                if (Categoria.equals("Tutte")) {
                    UtilitiesGlobali.getInstance().ApreToast(context,
                            "Impostare una categoria. Non Tutte");
                } else {
                    Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                    chooseFile.setType("*/*");
                    chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                    startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                }
            }
        });

        ImageView imgElimina = findViewById(R.id.imgEliminaFetekkie);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheMostraImmagini.getInstance().getIdImmagine());
                ChiamateWSFET c = new ChiamateWSFET(context);
                c.EliminaImmagine(id);
            }
        });

        ImageView imgRefreshCat = findViewById(R.id.imgRefreshCategorieFET);
        imgRefreshCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSFET c = new ChiamateWSFET(context);
                c.RitornaCategorie(true);
            }
        });

        ImageView imgShare = findViewById(R.id.imgShareFetekkie);
        imgShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Path = context.getFilesDir() + "/Immagini/AppoggioFET.jpg";

                if (Files.getInstance().EsisteFile(Path)) {
                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(Path);
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT,VariabiliStaticheMostraImmaginiFetekkie.getInstance().getUltimaImmagineCaricata().getNomeFile());
                    i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM,uri);
                    i.setType(UtilityWallpaper.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i,"Share immagine Fetekkie"));
                }
            }
        });

        ImageView imgImposta = findViewById(R.id.imgImpostaWallpaper);
        imgImposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilityFetekkie.getInstance().Attesa(true);

                StrutturaImmaginiLibraryFE s = VariabiliStaticheMostraImmaginiFetekkie.getInstance().getUltimaImmagineCaricata();

                String Path = context.getFilesDir() + "/Immagini/AppoggioFET.jpg";
                String PathImp = context.getFilesDir() + "/Immagini/AppoggioFET_Impostata.jpg";

                try {
                    Files.getInstance().CopiaFile(Path, PathImp);

                    StrutturaImmagine src = new StrutturaImmagine();
                    src.setPathImmagine(PathImp);
                    src.setImmagine(s.getNomeFile());
                    src.setDimensione("");
                    src.setDataImmagine(s.getDataCreazione());

                    ChangeWallpaper c = new ChangeWallpaper(context, "FETEKKIE");
                    c.setWallpaperLocale(context, src);

                    UtilityFetekkie.getInstance().Attesa(false);
                } catch (IOException ignored) {

                }
            }
        });

        ImageView imgSettings = (ImageView) findViewById(R.id.imgSettingsFET);
        imgSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Handler handlerTimer = new Handler(Looper.getMainLooper());
                Runnable rTimer = new Runnable() {
                    public void run() {
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent iP = new Intent(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCtx(), MainImpostazioni.class);
                                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bundle b = new Bundle();
                                b.putString("qualeSettaggio", "FETEKKIE");
                                iP.putExtras(b);
                                VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCtx().startActivity(iP);
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
                UtilityFetekkie.getInstance().TornaIndietro(context);
            }
            public void onSwipeLeft() {
                // UtilitiesGlobali.getInstance().ApreToast(context, "Swipe Left");
                UtilityFetekkie.getInstance().RitornaProssimaImmagine(context);
            }
            public void onSwipeBottom() {
                // UtilitiesGlobali.getInstance().ApreToast(context, "Swipe Bottom");
            }
        });

        EditText edtFiltro = findViewById(R.id.edtFiltroImmagimi);
        edtFiltro.setText(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getFiltro());
        edtFiltro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if (hasFocus) {
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setFiltro(edtFiltro.getText().toString());

                    db_dati_fetekkie db = new db_dati_fetekkie(context);
                    db.ScriveImpostazioni();

                    // UtilityFetekkie.getInstance().RitornaProssimaImmagine(context);
                // }
            }
        });

        EditText txtFiltroCate = findViewById(R.id.edtFiltroCategoriaFET);
        txtFiltroCate.setText(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getFiltroCategoria());
        txtFiltroCate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if (hasFocus) {
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setFiltroCategoria(txtFiltroCate.getText().toString());

                    db_dati_fetekkie db = new db_dati_fetekkie(context);
                    db.ScriveImpostazioni();

                    UtilityFetekkie.getInstance().AggiornaCategorie(context);
                // }
            }
        });

        boolean letto = false;
        String path1 = context.getFilesDir() + "/Immagini";
        UtilityWallpaper.getInstance().CreaCartelle(path1);
        String NomeFile = "/UltimaFetekkie.txt";
        if (UtilityWallpaper.getInstance().EsisteFile(path1 + NomeFile)) {
            String u = UtilityDetector.getInstance().LeggeFileDiTesto(path1 + NomeFile);
            if (u != null) {
                String[] uu = u.split("§");

                if (uu.length > 2) {
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setCategoria(uu[2]);
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setIdImmagine(Integer.parseInt(uu[1]));
                    String path = VariabiliStaticheMostraImmaginiFetekkie.PathUrl + uu[0];

                    StrutturaImmaginiLibraryFE s = new StrutturaImmaginiLibraryFE();
                    s.setUrlImmagine(path);
                    s.setCategoria(VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoria());
                    s.setNomeFile(uu[0]);
                    s.setDataCreazione("");

                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().AggiungeCaricata();

                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().ScriveInfoImmagine(s);

                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setUltimaImmagineCaricata(s);

                    DownloadImmagineFET d = new DownloadImmagineFET();
                    d.EsegueChiamata(
                            context,
                            path,
                            VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImg(),
                            path
                    );
                    // new DownloadImageFET(context, path,
                    //         VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImg()).execute(path);

                    letto = true;
                /* JSONObject jObject = null;
                try {
                    jObject = new JSONObject(u);
                    StrutturaImmaginiLibrary si = UtilityFetekkie.getInstance().prendeStruttura(jObject);

                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setCategoria(si.getCategoria());
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setIdImmagine(si.getIdImmagine());

                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setUltimaImmagineCaricata(si);

                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().ScriveInfoImmagine(si);

                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().AggiungeCaricata();

                    new DownloadImageFET(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmaginiFetekkie.getInstance().getImg()).execute(si.getUrlImmagine());

                    letto = true;
                } catch (JSONException ignored) {
                } */
                }
            }
        }

        final boolean[] primoIngresso = {true};
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                if (Categoria.equals("Tutte")) {
                    VariabiliStaticheMostraImmaginiFetekkie.getInstance().setCategoria("");
                    UtilityFetekkie.getInstance().RitornaProssimaImmagine(context);
                } else {
                    for (StrutturaImmaginiCategorieFE s : VariabiliStaticheMostraImmaginiFetekkie.getInstance().getListaCategorie()) {
                        if (s.getCategoria().equals(Categoria)) {
                            VariabiliStaticheMostraImmaginiFetekkie.getInstance().setCategoria(s.getCategoria());
                            UtilityFetekkie.getInstance().RitornaProssimaImmagine(context);
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
                boolean attivo = VariabiliStaticheMostraImmaginiFetekkie.getInstance().isSlideShowAttivo();
                attivo = !attivo;
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setSlideShowAttivo(attivo);

                Bitmap bmSS;
                if (attivo) {
                    bmSS = BitmapFactory.decodeResource(context.getResources(), R.drawable.slideshow_on);
                    UtilityFetekkie.getInstance().AttivaTimerSlideShow(context);
                } else {
                    bmSS = BitmapFactory.decodeResource(context.getResources(), R.drawable.slideshow_off);
                    UtilityFetekkie.getInstance().BloccaTimerSlideShow();
                }
                imgSlideShow.setImageBitmap(bmSS);
            }
        });

        if (!letto) {
            UtilityFetekkie.getInstance().RitornaProssimaImmagine(context);
        }

        /* imgAvanti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RitornaProssimaImmagine(ws);
            }
        }); */

        ImpostaSpostamento(act);

        ChiamateWSFET ws = new ChiamateWSFET(context);
        ws.RitornaCategorie(false);
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
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().getAct().finish();

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
                StrutturaImmaginiLibraryFE s = VariabiliStaticheMostraImmaginiFetekkie.getInstance().getUltimaImmagineCaricata();

                ChiamateWSFET c = new ChiamateWSFET(context);
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
                VariabiliStaticheMostraImmaginiFetekkie.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                UtilityFetekkie.getInstance().AggiornaCategorieSpostamento(context);
            }
        });

        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setIdCategoriaSpostamento("");
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().setSpnSpostaCategorie(findViewById(R.id.spnSpostaCategorie));
        final boolean[] primoIngresso = {true};
        VariabiliStaticheMostraImmaginiFetekkie.getInstance().getSpnSpostaCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    for (StrutturaImmaginiCategorieFE s : VariabiliStaticheMostraImmaginiFetekkie.getInstance().getListaCategorie()) {
                        if (s.getCategoria().equals(Categoria)) {
                            VariabiliStaticheMostraImmaginiFetekkie.getInstance().setIdCategoriaSpostamento(String.valueOf(s.getIdCategoria()));
                            break;
                        }
                    }
                    if (VariabiliStaticheMostraImmaginiFetekkie.getInstance().getIdCategoriaSpostamento().isEmpty()) {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            String Categoria = VariabiliStaticheMostraImmaginiFetekkie.getInstance().getCategoria();

            if (Categoria.equals("Tutte")) {
                UtilitiesGlobali.getInstance().ApreToast(context,
                        "Impostare una categoria. Non Tutte");
            } else {
                String Path = context.getFilesDir() + "/Immagini/UploadFET.jpg";

                Uri content_describer = data.getData();
                assert content_describer != null;

                String filename = content_describer.getLastPathSegment();
                assert filename != null;
                String[] f = filename.split("/");
                filename = f[f.length - 1];

                InputStream in = null;
                OutputStream out = null;
                try {
                    in = getContentResolver().openInputStream(content_describer);
                    out = new FileOutputStream(new File(Path));
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                } catch (FileNotFoundException e) {
                    // throw new RuntimeException(e);
                } catch (IOException e) {
                    // throw new RuntimeException(e);
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            // throw new RuntimeException(e);
                        }
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            // throw new RuntimeException(e);
                        }
                    }

                    String base64 = UtilitiesGlobali.getInstance().convertBmpToBase64(Path);

                    ChiamateWSFET ws = new ChiamateWSFET(context);
                    ws.AggiungeImmagine(
                            Categoria,
                            filename,
                            base64
                    );
                }
            }
        }
    }
}
