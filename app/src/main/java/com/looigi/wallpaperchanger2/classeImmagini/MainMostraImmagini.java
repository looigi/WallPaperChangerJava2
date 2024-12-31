package com.looigi.wallpaperchanger2.classeImmagini;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.MainImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeImmaginiUguali.MainImmaginiUguali;
import com.looigi.wallpaperchanger2.classeImpostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeDetector.UtilityDetector;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.DownloadImmagineMI;
import com.looigi.wallpaperchanger2.classeModificaImmagine.MainModificaImmagine;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classePlayer.Files;
import com.looigi.wallpaperchanger2.classeScaricaImmagini.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.classeWallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.utilities.OnSwipeTouchListener;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainMostraImmagini extends Activity {
    private Context context;
    private Activity act;
    private boolean SettaggiAperti = false;

    public MainMostraImmagini() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_immagini);

        context = this;
        act = this;

        VariabiliStaticheMostraImmagini.getInstance().setAct(act);
        VariabiliStaticheMostraImmagini.getInstance().setCtx(context);

        VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(-999);
        VariabiliStaticheMostraImmagini.getInstance().setFiltro("");
        VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(1);
        VariabiliStaticheMostraImmagini.getInstance().setRandom("S");

        VariabiliStaticheMostraImmagini.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoMI));

        // db_dati_immagini db = new db_dati_immagini(context);
        // db.CaricaImpostazioni();

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

        ImpostaSpostamento(act);

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.RitornaCategorie(false);

        VariabiliStaticheMostraImmagini.getInstance().setTxtId(findViewById(R.id.txtIdImmagine));
        VariabiliStaticheMostraImmagini.getInstance().setTxtCate(findViewById(R.id.txtCategoriaImmagine));
        VariabiliStaticheMostraImmagini.getInstance().setTxtInfo(findViewById(R.id.txtInfoImmagine));

        VariabiliStaticheMostraImmagini.getInstance().setImg(findViewById(R.id.imgLibrary));
        // ImageView imgIndietro = findViewById(R.id.imgIndietroLibrary);
        // ImageView imgAvanti = findViewById(R.id.imgAvantiLibrary);

        SettaggiAperti = VariabiliStaticheMostraImmagini.getInstance().isSettingsAperto();
        ImageView imgLinguetta = findViewById(R.id.imgLinguettaMI);
        LinearLayout laySettaggi = findViewById(R.id.laySettaggiMI);
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
                VariabiliStaticheMostraImmagini.getInstance().setSettingsAperto(SettaggiAperti);
                db_dati_immagini db = new db_dati_immagini(context);
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

        ImageView imgDownload = findViewById(R.id.imgDownloadMI);
        imgDownload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Categoria = "";

                for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
                    if (s.getIdCategoria() == VariabiliStaticheMostraImmagini.getInstance().getIdCategoria()) {
                        Categoria = s.getCategoria();
                        break;
                    }
                }

                if (Categoria.isEmpty() || Categoria.equals("Tutte")) {
                    UtilitiesGlobali.getInstance().ApreToast(context,
                            "Impostare una categoria. Non Tutte");
                    return;
                }

                VariabiliStaticheMostraImmagini.getInstance().setCategoria(Categoria);

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Nome salvataggio");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText(Categoria.replace("_", " "));
                builder.setView(input);

                String finalCategoria = Categoria;
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Salvataggio = input.getText().toString();
                        if (Salvataggio.isEmpty()) {
                            UtilitiesGlobali.getInstance().ApreToast(context,
                                    "Immettere un nome categoria");
                        } else {
                            VariabiliScaricaImmagini.getInstance().setListaDaScaricare(new ArrayList<>());

                            ChiamateWSMI ws = new ChiamateWSMI(context);
                            ws.ScaricaImmagini(finalCategoria, Salvataggio);
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

        ImageView imgNuovaCategoria = findViewById(R.id.imgNuovaCategoriaMI);
        imgNuovaCategoria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Nuova categoria");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setText("");
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String Salvataggio = input.getText().toString();

                        if (Salvataggio.isEmpty()) {
                            UtilitiesGlobali.getInstance().ApreToast(context,
                                    "Immettere un nome categoria");
                        } else {
                            ChiamateWSMI ws = new ChiamateWSMI(context);
                            ws.CreaNuovaCategoria(Salvataggio.replace(" ", "_"));
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

        ImageView imgModifica = findViewById(R.id.imgModificaMI);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Path = context.getFilesDir() + "/Immagini/AppoggioMI.jpg";

                VariabiliStaticheModificaImmagine.getInstance().setMascheraApertura("IMMAGINI");
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

        ImageView imgElimina = findViewById(R.id.imgEliminaMI);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStaticheMostraImmagini.getInstance().getIdImmagine());
                ChiamateWSMI c = new ChiamateWSMI(context);
                c.EliminaImmagine(id);
            }
        });

        ImageView imgRefreshCat = findViewById(R.id.imgRefreshCategorieMI);
        imgRefreshCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSMI c = new ChiamateWSMI(context);
                c.RitornaCategorie(true);
            }
        });

        ImageView imgImposta = findViewById(R.id.imgImpostaWallpaper);
        imgImposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata() != null) {
                    UtilityImmagini.getInstance().Attesa(true);

                    StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata();

                    String Path = context.getFilesDir() + "/Immagini/AppoggioMI.jpg";
                    String PathImp = context.getFilesDir() + "/Immagini/AppoggioMI_Impostata.jpg";
                    try {
                        Files.getInstance().CopiaFile(Path, PathImp);

                        long Dimensione = Files.getInstance().DimensioniFile(PathImp);

                        StrutturaImmagine src = new StrutturaImmagine();
                        src.setPathImmagine(PathImp);
                        src.setImmagine(s.getNomeFile());
                        src.setDimensione(String.valueOf(Dimensione));
                        src.setDataImmagine(s.getDataCreazione());

                        ChangeWallpaper c = new ChangeWallpaper(context,  "IMMAGINI");
                        c.setWallpaperLocale(context, src);

                        UtilityImmagini.getInstance().Attesa(false);
                    } catch (IOException ignored) {
                    }
                }
            }
        });

        ImageView imgShare = findViewById(R.id.imgShareWallpaper);
        imgShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata() != null) {
                    String Path = context.getFilesDir() + "/Immagini/AppoggioMI.jpg";

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(Path);
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata().getNomeFile());
                    i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM,uri);
                    i.setType(UtilityWallpaper.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i,"Share immagine"));
                }
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

        ImageView imgUguali = (ImageView) findViewById(R.id.imgImmaginiUgualiMI);
        imgUguali.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Categoria = "";

                if (VariabiliStaticheMostraImmagini.getInstance().getCategoria() == null) {
                    for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
                        if (s.getIdCategoria() == VariabiliStaticheMostraImmagini.getInstance().getIdCategoria()) {
                            Categoria = s.getCategoria();
                            break;
                        }
                    }
                    if (Categoria.isEmpty()) {
                        return;
                    }
                } else {
                    Categoria = VariabiliStaticheMostraImmagini.getInstance().getCategoria();
                }

                Intent iP = new Intent(VariabiliStaticheMostraImmagini.getInstance().getCtx(), MainImmaginiUguali.class);
                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("CATEGORIA", Categoria);
                iP.putExtras(b);
                VariabiliStaticheMostraImmagini.getInstance().getCtx().startActivity(iP);
            }
        });

        ImageView imgFuoriCategoria = (ImageView) findViewById(R.id.imgImmaginiFCMI);
        imgFuoriCategoria.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Categoria = "";

                if (VariabiliStaticheMostraImmagini.getInstance().getCategoria() == null) {
                    for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
                        if (s.getIdCategoria() == VariabiliStaticheMostraImmagini.getInstance().getIdCategoria()) {
                            Categoria = s.getCategoria();
                            break;
                        }
                    }
                    if (Categoria.isEmpty()) {
                        return;
                    }
                } else {
                    Categoria = VariabiliStaticheMostraImmagini.getInstance().getCategoria();
                }

                Intent iP = new Intent(VariabiliStaticheMostraImmagini.getInstance().getCtx(), MainImmaginiFuoriCategoria.class);
                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("IDCATEGORIA", Integer.toString(VariabiliStaticheMostraImmagini.getInstance().getIdCategoria()));
                b.putString("CATEGORIA", Categoria);
                iP.putExtras(b);
                VariabiliStaticheMostraImmagini.getInstance().getCtx().startActivity(iP);
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
                // if (hasFocus) {
                    VariabiliStaticheMostraImmagini.getInstance().setFiltro(edtFiltro.getText().toString());

                    db_dati_immagini db = new db_dati_immagini(context);
                    db.ScriveImpostazioni();

                    // UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
                // }
            }
        });

        EditText txtFiltroCate = findViewById(R.id.edtFiltroCategoriaMI);
        txtFiltroCate.setText(VariabiliStaticheMostraImmagini.getInstance().getFiltroCategoria());
        txtFiltroCate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if (hasFocus) {
                    VariabiliStaticheMostraImmagini.getInstance().setFiltroCategoria(txtFiltroCate.getText().toString());

                    db_dati_immagini db = new db_dati_immagini(context);
                    db.ScriveImpostazioni();

                    UtilityImmagini.getInstance().AggiornaCategorie(context);
                // }
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

                    DownloadImmagineMI d = new DownloadImmagineMI();
                    d.EsegueChiamata(
                            context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmagini.getInstance().getImg(),
                            si.getUrlImmagine(),
                            false
                    );
                    /* new DownloadImageMI(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmagini.getInstance().getImg()).execute(si.getUrlImmagine()); */

                    letto = true;
                } catch (JSONException ignored) {
                }
            }
        }

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
                StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata();

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s);

                laySposta.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgCopiaSuSfondi = act.findViewById(R.id.imgCopiaSuSfondi);
        imgCopiaSuSfondi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata();
                String UrlImmagine = s.getUrlImmagine();

                DownloadImmagineMI d = new DownloadImmagineMI();
                d.EsegueChiamata(context, s.getNomeFile(), null, UrlImmagine, true);
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
                VariabiliStaticheMostraImmagini.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                UtilityImmagini.getInstance().AggiornaCategorieSpostamento(context);
            }
        });

        VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento("");
        VariabiliStaticheMostraImmagini.getInstance().setSpnSpostaCategorie(findViewById(R.id.spnSpostaCategorie));
        final boolean[] primoIngresso = {true};
        VariabiliStaticheMostraImmagini.getInstance().getSpnSpostaCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                    for (StrutturaImmaginiCategorie s : VariabiliStaticheMostraImmagini.getInstance().getListaCategorie()) {
                        if (s.getCategoria().equals(Categoria)) {
                            VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento(String.valueOf(s.getIdCategoria()));
                            break;
                        }
                    }
                    if (VariabiliStaticheMostraImmagini.getInstance().getIdCategoriaSpostamento().isEmpty()) {
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
