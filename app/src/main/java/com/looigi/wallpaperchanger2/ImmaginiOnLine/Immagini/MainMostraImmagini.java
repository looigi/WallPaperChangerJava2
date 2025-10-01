package com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiFuoriCategoria.MainImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiRaggruppate.MainImmaginiRaggruppate;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUguali.MainImmaginiUguali;
import com.looigi.wallpaperchanger2.Impostazioni.MainImpostazioni;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiCategorie;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.Detector.UtilityDetector;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.Immagini.webservice.DownloadImmagineMI;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiModifica.MainModificaImmagine;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiModifica.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.MainPreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiPreview.VariabiliStatichePreview;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiSpostamento.MainSpostamento;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiUtility.MainUtilityImmagini;
import com.looigi.wallpaperchanger2.UtilitiesVarie.Files;
import com.looigi.wallpaperchanger2.ImmaginiOnLine.ImmaginiScarica.VariabiliScaricaImmagini;
import com.looigi.wallpaperchanger2.Wallpaper.UtilityWallpaper;
import com.looigi.wallpaperchanger2.UtilitiesVarie.OnSwipeTouchListener;
import com.looigi.wallpaperchanger2.UtilitiesVarie.UtilitiesGlobali;

import org.json.JSONException;
import org.json.JSONObject;

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
        db_dati_immagini db = new db_dati_immagini(context);
        VariabiliStaticheMostraImmagini.getInstance().setCategoriaAttuale(
                db.LeggeUltimaCategoria()
        );
        db.ChiudeDB();

        VariabiliStaticheMostraImmagini.getInstance().setSpnSpostaCategorie(findViewById(R.id.spnSpostaCategorie));
        final boolean[] primoIngresso = {true};
        VariabiliStaticheMostraImmagini.getInstance().setSpnCategorie(findViewById(R.id.spnCategorie));

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.RitornaCategorie(false, "IMMAGINI");

        VariabiliStaticheMostraImmagini.getInstance().getSpnCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();
                VariabiliStaticheMostraImmagini.getInstance().setCategoriaAttuale(Categoria);

                db_dati_immagini db = new db_dati_immagini(context);
                db.ScriveUltimaCategoria(Categoria);
                db.ChiudeDB();

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

        // VariabiliStaticheMostraImmagini.getInstance().setTxtId(findViewById(R.id.txtIdImmagine));
        // VariabiliStaticheMostraImmagini.getInstance().setTxtCate(findViewById(R.id.txtCategoriaImmagine));
        // VariabiliStaticheMostraImmagini.getInstance().setTxtInfo(findViewById(R.id.txtInfoImmagine));

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
                builder.setTitle("Immagini");
                builder.setMessage("Nome salvataggio");

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

                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Mappa");
                            builder.setMessage("Filtro valori per nome categoria ?");

                            builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ChiamateWSMI ws = new ChiamateWSMI(context);
                                    ws.ScaricaImmagini(finalCategoria, Salvataggio, "S", "IMMAGINI");
                                }
                            });
                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ChiamateWSMI ws = new ChiamateWSMI(context);
                                    ws.ScaricaImmagini(finalCategoria, Salvataggio, "N", "IMMAGINI");
                                }
                            });

                            builder.show();
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

        /* ImageView imgNuovaCategoria = findViewById(R.id.imgNuovaCategoriaMI);
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
                            ws.CreaNuovaCategoria(Salvataggio.replace(" ", "_"), "IMMAGINI");
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
        }); */

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

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Immagini");
                builder.setMessage("Si vuole eliminare l'immagine ?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ChiamateWSMI c = new ChiamateWSMI(context);
                        c.EliminaImmagine(id);
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

        ImageView imgRilevaVolto = findViewById(R.id.imgPreview);
        imgRilevaVolto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePreview.getInstance().setStrutturaImmagine(
                        VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale()
                );

                Intent i = new Intent(context, MainPreview.class);
                i.putExtra("Modalita", "Immagini");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        ImageView imgRefreshCat = findViewById(R.id.imgRefreshCategorieMI);
        imgRefreshCat.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSMI c = new ChiamateWSMI(context);
                c.RitornaCategorie(true, "IMMAGINI");
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
                String Categoria = VariabiliStaticheMostraImmagini.getInstance().getCategoriaAttuale();

                if (Categoria.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Categoria non valida");
                    return;
                }

                /* if (VariabiliStaticheMostraImmagini.getInstance().getCategoria() == null) {
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
                } */

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
                String Categoria = VariabiliStaticheMostraImmagini.getInstance().getCategoriaAttuale();

                if (Categoria.isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Categoria non valida");
                    return;
                }

                /* if (VariabiliStaticheMostraImmagini.getInstance().getCategoria() == null) {
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
                } */

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
        /* edtFiltro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if (hasFocus) {
                    VariabiliStaticheMostraImmagini.getInstance().setFiltro(edtFiltro.getText().toString());

                    db_dati_immagini db = new db_dati_immagini(context);
                    db.ScriveImpostazioni();

                    // UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
                // }
            }
        }); */

        ImageView imgFiltraImmagini = findViewById(R.id.imgFiltraImmagini);
        imgFiltraImmagini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmagini.getInstance().setFiltro(edtFiltro.getText().toString());

                db_dati_immagini db = new db_dati_immagini(context);
                db.ScriveImpostazioni();

                // UtilityImmagini.getInstance().RitornaProssimaImmagine(context);
            }
        });

        SwitchCompat swcImgAndOr = act.findViewById(R.id.switchAndOr);
        swcImgAndOr.setChecked(VariabiliStaticheMostraImmagini.getInstance().getOperatoreFiltro().equals("And"));
        swcImgAndOr.setText(VariabiliStaticheMostraImmagini.getInstance().getOperatoreFiltro());
        swcImgAndOr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String Cosa = isChecked ? "And" : "Or";
                swcImgAndOr.setText(Cosa);
                VariabiliStaticheMostraImmagini.getInstance().setOperatoreFiltro(Cosa);

                db_dati_immagini db = new db_dati_immagini(context);
                db.ScriveImpostazioni();
                db.ChiudeDB();
            }
        });

        EditText txtFiltroCate = findViewById(R.id.edtFiltroCategoriaMI);
        txtFiltroCate.setText(VariabiliStaticheMostraImmagini.getInstance().getFiltroCategoria());
        /* txtFiltroCate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // if (hasFocus) {
                    VariabiliStaticheMostraImmagini.getInstance().setFiltroCategoria(txtFiltroCate.getText().toString());

                    db_dati_immagini db = new db_dati_immagini(context);
                    db.ScriveImpostazioni();

                    UtilityImmagini.getInstance().AggiornaCategorie(context);
                // }
            }
        }); */

        ImageView imgFiltraCombo = findViewById(R.id.imgFiltraCombo);
        imgFiltraCombo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStaticheMostraImmagini.getInstance().setFiltroCategoria(txtFiltroCate.getText().toString());

                db_dati_immagini db = new db_dati_immagini(context);
                db.ScriveImpostazioni();

                UtilityImmagini.getInstance().AggiornaCategorie(context);
            }
        });

        VariabiliStaticheMostraImmagini.getInstance().setTxtInfoSotto(findViewById(R.id.txtInfoSotto));
        VariabiliStaticheMostraImmagini.getInstance().getTxtInfoSotto().setText("");

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
                    if (si == null) {
                        Files.getInstance().EliminaFile(path1, NomeFile);
                    } else {
                        VariabiliStaticheMostraImmagini.getInstance().setStrutturaImmagineAttuale(si);

                        // VariabiliStaticheMostraImmagini.getInstance().setIdCategoria(si.getIdCategoria());
                        VariabiliStaticheMostraImmagini.getInstance().setIdImmagine(si.getIdImmagine());

                        UtilityImmagini.getInstance().ScriveInfoSotto(si);

                        VariabiliStaticheMostraImmagini.getInstance().setUltimaImmagineCaricata(si);

                        VariabiliStaticheMostraImmagini.getInstance().ScriveInfoImmagine(si);

                        VariabiliStaticheMostraImmagini.getInstance().AggiungeCaricata();

                        DownloadImmagineMI d = new DownloadImmagineMI();
                        d.EsegueChiamata(
                                context, si.getUrlImmagine(),
                                VariabiliStaticheMostraImmagini.getInstance().getImg(),
                                si.getUrlImmagine(),
                                false,
                                false
                        );
                        /* new DownloadImageMI(context, si.getUrlImmagine(),
                            VariabiliStaticheMostraImmagini.getInstance().getImg()).execute(si.getUrlImmagine()); */

                        letto = true;
                    }
                } catch (JSONException ignored) {
                }
            }
        }

        VariabiliStaticheMostraImmagini.getInstance().setLayCategorieRilevate(findViewById(R.id.layCategorieRilevate));
        VariabiliStaticheMostraImmagini.getInstance().setLayScritteRilevate(findViewById(R.id.layScritteRilevate));
        VariabiliStaticheMostraImmagini.getInstance().setLayTasti(findViewById(R.id.layTasti));

        LinearLayout layTastiImmagini = findViewById(R.id.layTastiImmagini);
        layTastiImmagini.setVisibility(LinearLayout.GONE);

        ImageView imgTastiImmagini = findViewById(R.id.imgTastiImmagini);
        imgTastiImmagini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layTastiImmagini.setVisibility(LinearLayout.VISIBLE);
            }
        });

        ImageView imgChiudeTastiImmagini = findViewById(R.id.imgChiudeImmagini);
        imgChiudeTastiImmagini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                layTastiImmagini.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgImmaginiRaggruppate = findViewById(R.id.imgImmaginiRaggruppate);
        imgImmaginiRaggruppate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainImmaginiRaggruppate.class);
                intent.putExtra("idCategoria", Integer.toString(VariabiliStaticheMostraImmagini.getInstance().getIdCategoria()));
                startActivity(intent);
            }
        });

        ImageView imgControlloImmagini = findViewById(R.id.imgControlloImmagini);
        imgControlloImmagini.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, MainUtilityImmagini.class);
                intent.putExtra("idCategoria", Integer.toString(VariabiliStaticheMostraImmagini.getInstance().getIdCategoria()));
                startActivity(intent);
            }
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
        // LinearLayout laySposta = act.findViewById(R.id.laySposta);
        // laySposta.setVisibility(LinearLayout.GONE);

        ImageView imgApre = act.findViewById(R.id.imgSpostaACategoria);
        imgApre.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(context, MainSpostamento.class);
                i.putExtra("Modalita", "Immagine");
                i.putExtra("idImmagine", Integer.toString(VariabiliStaticheMostraImmagini.getInstance().getStrutturaImmagineAttuale().getIdImmagine()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        /* ImageView imgSpostaImmagine = act.findViewById(R.id.imgSpostaImmagine);
        imgSpostaImmagine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StrutturaImmaginiLibrary s = VariabiliStaticheMostraImmagini.getInstance().getUltimaImmagineCaricata();

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s, "IMMAGINI");

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
                VariabiliStaticheMostraImmagini.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                UtilityImmagini.getInstance().AggiornaCategorieSpostamento(context);
            }
        });

        VariabiliStaticheMostraImmagini.getInstance().setIdCategoriaSpostamento("");
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
        }); */
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
