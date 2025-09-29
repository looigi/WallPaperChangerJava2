package com.looigi.wallpaperchanger2.classePreview;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.looigi.wallpaperchanger2.R;
import com.looigi.wallpaperchanger2.classeImmagini.VariabiliStaticheMostraImmagini;
import com.looigi.wallpaperchanger2.classeImmagini.strutture.StrutturaImmaginiLibrary;
import com.looigi.wallpaperchanger2.classeImmagini.webservice.ChiamateWSMI;
import com.looigi.wallpaperchanger2.classeImmaginiFuoriCategoria.MainImmaginiFuoriCategoria;
import com.looigi.wallpaperchanger2.classeModificaImmagine.MainModificaImmagine;
import com.looigi.wallpaperchanger2.classeModificaImmagine.VariabiliStaticheModificaImmagine;
import com.looigi.wallpaperchanger2.classePreview.classeOCR.MainOCR;
import com.looigi.wallpaperchanger2.classePreview.classeRilevaOCRJava.MainRilevaOCR;
import com.looigi.wallpaperchanger2.classePreview.webService.DownloadImmaginePreview;
import com.looigi.wallpaperchanger2.classeSpostamento.MainSpostamento;
import com.looigi.wallpaperchanger2.classeSpostamento.VariabiliStaticheSpostamento;
import com.looigi.wallpaperchanger2.classeUtilityImmagini.webservice.ChiamateWSUI;
import com.looigi.wallpaperchanger2.classeWallpaper.ChangeWallpaper;
import com.looigi.wallpaperchanger2.classeWallpaper.RefreshImmagini.ChiamateWsWPRefresh;
import com.looigi.wallpaperchanger2.classeWallpaper.StrutturaImmagine;
import com.looigi.wallpaperchanger2.utilities.Files;
import com.looigi.wallpaperchanger2.utilities.UtilitiesGlobali;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPreview extends Activity {
    private Context context;
    private Activity act;

    // pinimg;romantik;wheel;tata;nylon;fantasy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_preview);

        context = this;
        act = this;

        Intent intent = getIntent();
        String Modalita = intent.getStringExtra("Modalita");
        VariabiliStatichePreview.getInstance().setModalita(Modalita);

        VariabiliStatichePreview.getInstance().setTxtDescrizione(findViewById(R.id.txtDescrizione));
        VariabiliStatichePreview.getInstance().setImgCaricamento(findViewById(R.id.imgCaricamentoPreview));
        UtilitiesPreview.getInstance().Attesa(false);

        VariabiliStatichePreview.getInstance().setImgPreview(findViewById(R.id.imgPreview));

        VariabiliStatichePreview.getInstance().setImgProssima(findViewById(R.id.imgProssima));
        VariabiliStatichePreview.getInstance().setImgPrecedente(findViewById(R.id.imgPrecedente));

        VariabiliStatichePreview.getInstance().setLayCategorieRilevate(findViewById(R.id.layCategorieRilevate));
        VariabiliStatichePreview.getInstance().setLayScritteRilevate(findViewById(R.id.layScritteRilevate));
        VariabiliStatichePreview.getInstance().setLayTasti(findViewById(R.id.layTasti));
        VariabiliStatichePreview.getInstance().setLayTastiDestra(findViewById(R.id.layTastiDestra));
        VariabiliStatichePreview.getInstance().getLayTastiDestra().setVisibility(LinearLayout.GONE);

        SharedPreferences prefs = getSharedPreferences("PREVIEW", MODE_PRIVATE);
        int idUltimaImmagine = prefs.getInt(
                "idImmagineCategoria_" + VariabiliStatichePreview.getInstance().getIdCategoria()
                , -1);
        VariabiliStatichePreview.getInstance().setUltimaImmagineVisualizzata(idUltimaImmagine);

        HorizontalScrollView hrzTasti = findViewById(R.id.hrzTasti);
        hrzTasti.setVisibility(LinearLayout.VISIBLE);

        switch (Modalita) {
            case "OCR":
                VariabiliStatichePreview.getInstance().getImgProssima().setVisibility(LinearLayout.GONE);
                VariabiliStatichePreview.getInstance().getImgPrecedente().setVisibility(LinearLayout.GONE);
                hrzTasti.setVisibility(LinearLayout.GONE);

                if (VariabiliStatichePreview.getInstance().getStrutturaImmagine() == null) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Nessuna struttura immagine impostata");
                    this.finish();
                    return;
                }

                DownloadImmaginePreview d = new DownloadImmaginePreview();
                d.EsegueChiamata(
                        context,
                        VariabiliStatichePreview.getInstance().getStrutturaImmagine().getNomeFile(),
                        VariabiliStatichePreview.getInstance().getImgPreview(),
                        VariabiliStatichePreview.getInstance().getStrutturaImmagine().getUrlImmagine()
                );
                break;
            case "Utility":
                VariabiliStatichePreview.getInstance().getImgProssima().setVisibility(LinearLayout.VISIBLE);
                VariabiliStatichePreview.getInstance().getImgPrecedente().setVisibility(LinearLayout.VISIBLE);

                ChiamateWSUI ws = new ChiamateWSUI(context);
                ws.RitornaProssimaImmagine(
                        VariabiliStatichePreview.getInstance().getIdCategoria(),
                        "PREVIEW"
                );
                break;
            default:
                VariabiliStatichePreview.getInstance().getImgProssima().setVisibility(LinearLayout.GONE);
                VariabiliStatichePreview.getInstance().getImgPrecedente().setVisibility(LinearLayout.GONE);

                if (VariabiliStatichePreview.getInstance().getStrutturaImmagine() == null) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Nessuna struttura immagine impostata");
                    this.finish();
                    return;
                }

                DownloadImmaginePreview d1 = new DownloadImmaginePreview();
                d1.EsegueChiamata(
                        context,
                        VariabiliStatichePreview.getInstance().getStrutturaImmagine().getNomeFile(),
                        VariabiliStatichePreview.getInstance().getImgPreview(),
                        VariabiliStatichePreview.getInstance().getStrutturaImmagine().getUrlImmagine()
                );
                break;
        }

        VariabiliStatichePreview.getInstance().getImgProssima().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesPreview.getInstance().ProssimaImmagine(context);

                UtilitiesPreview.getInstance().DisegnaUltimiSpostamenti(context);
            }
        });

        VariabiliStatichePreview.getInstance().getImgPrecedente().setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePreview.getInstance().getQualeImmagine() > 0) {
                    UtilitiesPreview.getInstance().Attesa(true);
                    VariabiliStatichePreview.getInstance().getImgProssima().setVisibility(LinearLayout.GONE);
                    VariabiliStatichePreview.getInstance().getImgPrecedente().setVisibility(LinearLayout.GONE);

                    StrutturaImmaginiLibrary si = VariabiliStatichePreview.getInstance().RitornaImmaginePrecedente();
                    if (si != null) {
                        VariabiliStatichePreview.getInstance().setStrutturaImmagine(si);
                        if (VariabiliStatichePreview.getInstance().getTxtDescrizione() != null) {
                            String Descrizione = "Immagine: " + si.getNomeFile() + " - Categoria: " + si.getCategoria() +
                                    " - Cartella: " + si.getCartella() + " - Dimensioni: " + si.getDimensioniImmagine() +
                                    " - Bytes: " + si.getDimensioneFile();
                            VariabiliStatichePreview.getInstance().getTxtDescrizione().setText(Descrizione);
                        }

                        UtilitiesPreview.getInstance().RitornoProssimaImmagine(context, si);
                    }

                    UtilitiesPreview.getInstance().Attesa(false);
                    VariabiliStatichePreview.getInstance().getImgProssima().setVisibility(LinearLayout.VISIBLE);
                    VariabiliStatichePreview.getInstance().getImgPrecedente().setVisibility(LinearLayout.VISIBLE);

                    UtilitiesPreview.getInstance().DisegnaUltimiSpostamenti(context);
                }
            }
        });

        ImageView imgGestioneVolti = findViewById(R.id.imgGestioneVolti);
        imgGestioneVolti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ChiamateWSUI ws = new ChiamateWSUI(context);
                ws.ControllaVolto(
                        String.valueOf(VariabiliStatichePreview.getInstance().getStrutturaImmagine().getIdImmagine())
                );
            }
        });

        ImageView imgRicerca = findViewById(R.id.imgRicerca);
        imgRicerca.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent iP = new Intent(context, MainImmaginiFuoriCategoria.class);
                iP.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("IDCATEGORIA", "-1");
                b.putString("CATEGORIA", "NESSUNA");
                iP.putExtras(b);
                context.startActivity(iP);
            }
        });

        ImageView imgCreaOCR = findViewById(R.id.imgCreaOCR);
        imgCreaOCR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(context, MainRilevaOCR.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        ImageView imgShare = findViewById(R.id.imgShareWallpaper);
        imgShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesPreview.getInstance().Attesa(true);

                VariabiliStatichePreview.getInstance().getImgPreview().setDrawingCacheEnabled(true);
                VariabiliStatichePreview.getInstance().getImgPreview().buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(VariabiliStatichePreview.getInstance().getImgPreview().getDrawingCache());
                VariabiliStatichePreview.getInstance().getImgPreview().setDrawingCacheEnabled(false);

                String Path = context.getFilesDir() + "/Immagini/";

                // 2. Salva il bitmap su file (ad esempio in Pictures)
                try {
                    File path = new File(Path);
                    if (!path.exists()) {
                        path.mkdirs();
                    }

                    File file = new File(path, "AppoggioMI.jpg");
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                    StrictMode.setVmPolicy(builder.build());

                    File f = new File(Path);
                    Uri uri = FileProvider.getUriForFile(context,
                            context.getApplicationContext().getPackageName() + ".provider",
                            f);

                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{"looigi@gmail.com"});
                    i.putExtra(Intent.EXTRA_SUBJECT, VariabiliStatichePreview.getInstance().getStrutturaImmagine().getNomeFile());
                    // i.putExtra(Intent.EXTRA_TEXT,"Dettagli nel file allegato");
                    i.putExtra(Intent.EXTRA_STREAM,uri);
                    i.setType(UtilitiesGlobali.getInstance().GetMimeType(context, uri));
                    context.startActivity(Intent.createChooser(i,"Share immagine"));
                } catch (IOException ignored) {
                }
            }
        });

        ImageView imgRinomina = findViewById(R.id.imgRinomina);
        imgRinomina.setOnClickListener(new View.OnClickListener() {
              public void onClick(View v) {
                  StrutturaImmaginiLibrary s = VariabiliStatichePreview.getInstance().getStrutturaImmagine();
                  int idImmagine = s.getIdImmagine();

                  AlertDialog.Builder builder = new AlertDialog.Builder(context);
                  builder.setTitle("Preview");
                  builder.setMessage("Nuovo nome file");

                  final EditText input = new EditText(context);
                  input.setInputType(InputType.TYPE_CLASS_TEXT);
                  input.setText(s.getNomeFile());
                  builder.setView(input);

                  String finalCategoria = s.getCategoria();
                  builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                          String Salvataggio = input.getText().toString();
                          if (Salvataggio.isEmpty()) {
                              UtilitiesGlobali.getInstance().ApreToast(context,
                                      "Immettere un nome file");
                          } else {
                              ChiamateWSUI ws = new ChiamateWSUI(context);
                              ws.RinominaImmagine(String.valueOf(idImmagine), Salvataggio);
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

        ImageView imgElimina = findViewById(R.id.imgElimina);
        imgElimina.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String id = String.valueOf(VariabiliStatichePreview.getInstance().getStrutturaImmagine().getIdImmagine());

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Preview");
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

        ImageView imgImposta = findViewById(R.id.imgImpostaWallpaper);
        imgImposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesPreview.getInstance().Attesa(true);

                VariabiliStatichePreview.getInstance().getImgPreview().setDrawingCacheEnabled(true);
                VariabiliStatichePreview.getInstance().getImgPreview().buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(VariabiliStatichePreview.getInstance().getImgPreview().getDrawingCache());
                VariabiliStatichePreview.getInstance().getImgPreview().setDrawingCacheEnabled(false);

                String Path = context.getFilesDir() + "/Immagini/";

                // 2. Salva il bitmap su file (ad esempio in Pictures)
                try {
                    File path = new File(Path);
                    if (!path.exists()) {
                        path.mkdirs();
                    }

                    File file = new File(path, "AppoggioMI.jpg");
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    VariabiliStaticheModificaImmagine.getInstance().setMascheraApertura("PREVIEW");
                    VariabiliStaticheModificaImmagine.getInstance().setNomeImmagine(
                            Path + "/AppoggioMI.jpg"
                    );

                    long Dimensione = Files.getInstance().DimensioniFile(Path + "/AppoggioMI.jpg");

                    StrutturaImmaginiLibrary s = VariabiliStatichePreview.getInstance().getStrutturaImmagine();

                    StrutturaImmagine src = new StrutturaImmagine();
                    src.setPathImmagine(Path + "/AppoggioMI.jpg");
                    src.setImmagine(s.getNomeFile());
                    src.setDimensione(String.valueOf(Dimensione));
                    src.setDataImmagine(s.getDataCreazione());

                    ChangeWallpaper c = new ChangeWallpaper(context,  "IMMAGINI", src);
                    c.setWallpaperLocale(context, src);

                    UtilitiesPreview.getInstance().Attesa(false);
                } catch (IOException ignored) {
                }
            }
        });

        ImageView imgCopiaSuSfondi = act.findViewById(R.id.imgCopiaSuSfondi);
        imgCopiaSuSfondi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UtilitiesPreview.getInstance().Attesa(true);
                StrutturaImmaginiLibrary s = VariabiliStatichePreview.getInstance().getStrutturaImmagine();

                String Path = context.getFilesDir() + "/Immagini/";

                VariabiliStatichePreview.getInstance().getImgPreview().setDrawingCacheEnabled(true);
                VariabiliStatichePreview.getInstance().getImgPreview().buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(VariabiliStatichePreview.getInstance().getImgPreview().getDrawingCache());
                VariabiliStatichePreview.getInstance().getImgPreview().setDrawingCacheEnabled(false);

                // 2. Salva il bitmap su file (ad esempio in Pictures)
                try {
                    File path = new File(Path);
                    if (!path.exists()) {
                        path.mkdirs();
                    }

                    File file = new File(path, "AppoggioMI.jpg");
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    VariabiliStaticheModificaImmagine.getInstance().setMascheraApertura("PREVIEW");
                    VariabiliStaticheModificaImmagine.getInstance().setNomeImmagine(
                            Path + "/AppoggioMI.jpg"
                    );

                    VariabiliStaticheMostraImmagini.getInstance().setUltimaImmagineCaricata(
                            VariabiliStatichePreview.getInstance().getStrutturaImmagine()
                    );

                    String result = UtilitiesGlobali.getInstance().convertBmpToBase64(Path + "/AppoggioMI.jpg");

                    ChiamateWsWPRefresh ws = new ChiamateWsWPRefresh(context);
                    ws.ScriveImmagineSuSfondiLocale("DaImmagini/" + s.getNomeFile(), result);
                } catch (Exception e) {
                    UtilitiesPreview.getInstance().Attesa(false);
                }
            }
        });

        /* ImageView imgChiudePreview = findViewById(R.id.imgChiudePreview);
        imgChiudePreview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePreview.getInstance().getImgPreview().setImageBitmap(null);
                act.finish();
            }
        }); */

        VariabiliStatichePreview.getInstance().setLayVolti(findViewById(R.id.layVoltiRilevati));
        VariabiliStatichePreview.getInstance().getLayVolti().setVisibility(LinearLayout.GONE);
        VariabiliStatichePreview.getInstance().setLstVolti(findViewById(R.id.lstListaVolti));

        ImageView imgChiudeListaVolti = findViewById(R.id.imgChiudeListaVolti);
        imgChiudeListaVolti.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                VariabiliStatichePreview.getInstance().getLayVolti().setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgModifica = findViewById(R.id.imgModifica);
        imgModifica.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Path = context.getFilesDir() + "/Immagini/";

                VariabiliStatichePreview.getInstance().getImgPreview().setDrawingCacheEnabled(true);
                VariabiliStatichePreview.getInstance().getImgPreview().buildDrawingCache();
                Bitmap bitmap = Bitmap.createBitmap(VariabiliStatichePreview.getInstance().getImgPreview().getDrawingCache());
                VariabiliStatichePreview.getInstance().getImgPreview().setDrawingCacheEnabled(false);

                // 2. Salva il bitmap su file (ad esempio in Pictures)
                try {
                    File path = new File(Path);
                    if (!path.exists()) {
                        path.mkdirs();
                    }

                    File file = new File(path, "AppoggioMI.jpg");
                    FileOutputStream out = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();

                    VariabiliStaticheModificaImmagine.getInstance().setMascheraApertura("PREVIEW");
                    VariabiliStaticheModificaImmagine.getInstance().setNomeImmagine(
                            Path + "/AppoggioMI.jpg"
                    );

                    VariabiliStaticheMostraImmagini.getInstance().setUltimaImmagineCaricata(
                            VariabiliStatichePreview.getInstance().getStrutturaImmagine()
                    );

                    Intent i = new Intent(context, MainModificaImmagine.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } catch (Exception e) {
                }
            }
        });

        // SPOSTAMENTO
        VariabiliStatichePreview.getInstance().setIdCategoriaDiSpostamento("");

        // LinearLayout laySposta = findViewById(R.id.laySposta);
        // laySposta.setVisibility(LinearLayout.GONE);

        ImageView imgSposta = findViewById(R.id.imgSpostaACategoria);
        imgSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // laySposta.setVisibility(LinearLayout.VISIBLE);
                Intent i = new Intent(context, MainSpostamento.class);
                i.putExtra("Modalita", "Preview");
                i.putExtra("idImmagine", Integer.toString(VariabiliStatichePreview.getInstance().getStrutturaImmagine().getIdImmagine()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.RitornaCategorie(false, "PREVIEW");

        /* ImageView imgSpostaImmagine = findViewById(R.id.imgSpostaImmagine);
        imgSpostaImmagine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (VariabiliStatichePreview.getInstance().getIdCategoriaDiSpostamento().isEmpty()) {
                    UtilitiesGlobali.getInstance().ApreToast(context, "Selezionare una categoria");
                    return;
                }

                StrutturaImmaginiLibrary s = VariabiliStatichePreview.getInstance().getStrutturaImmagine();

                ChiamateWSMI c = new ChiamateWSMI(context);
                c.SpostaImmagine(s, "PREVIEW");

                laySposta.setVisibility(LinearLayout.GONE);
            }
        });

        ImageView imgAnnullaSposta = findViewById(R.id.imgAnnullaSposta);
        imgAnnullaSposta.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                laySposta.setVisibility(LinearLayout.GONE);
            }
        });

        EditText edtFiltroSpostamento = findViewById(R.id.edtSpostaFiltroCategoria);
        edtFiltroSpostamento.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                VariabiliStatichePreview.getInstance().setFiltroCategoriaSpostamento(edtFiltroSpostamento.getText().toString());
                VariabiliStatichePreview.getInstance().AggiornaCategorieSpostamento(context);
            }
        });

        VariabiliStatichePreview.getInstance().setSpnSpostaCategorie(findViewById(R.id.spnSpostaCategorie));

        ChiamateWSMI ws = new ChiamateWSMI(context);
        ws.RitornaCategorie(false, "PREVIEW");

        final boolean[] primoIngresso = {true};
        VariabiliStatichePreview.getInstance().getSpnSpostaCategorie().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                if (primoIngresso[0]) {
                    primoIngresso[0] = false;
                    return;
                }

                String Categoria = adapterView.getItemAtPosition(position).toString();

                VariabiliStatichePreview.getInstance().setIdCategoriaDiSpostamento("");
                for (StrutturaImmaginiCategorie c: VariabiliStatichePreview.getInstance().getListaCategorie()) {
                    if (c.getCategoria().equals(Categoria)) {
                        VariabiliStatichePreview.getInstance().setIdCategoriaDiSpostamento(String.valueOf(c.getIdCategoria()));
                        break;
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        }); */
        // SPOSTAMENTO

        String Path = context.getFilesDir() + "/Immagini";
        if (Files.getInstance().EsisteFile(Path + "/CategoriePiuUsate.txt")) {
            String Cate = Files.getInstance().LeggeFile(Path, "CategoriePiuUsate.txt");
            if (!Cate.isEmpty()) {
                String[] c = Cate.split("\n");
                List<String> lista = new ArrayList<>();
                for (String c2 : c) {
                    if (!c2.isEmpty()) {
                        if (!lista.contains(c2)) {
                            lista.add(c2);
                        }
                    }
                }
                VariabiliStaticheSpostamento.getInstance().setCategoriaSpostata(lista);
            }
        } else {
            VariabiliStaticheSpostamento.getInstance().setCategoriaSpostata(new ArrayList<>());
        }

        VariabiliStatichePreview.getInstance().getLayTastiDestra().setOnTouchListener(new View.OnTouchListener() {
            private float dX, dY;

            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_DOWN:
                        // Salva la distanza tra posizione tocco e posizione layout
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        // Aggiorna posizione in base al movimento del dito
                        view.animate()
                                .x(event.getRawX() + dX)
                                .y(event.getRawY() + dY)
                                .setDuration(0)
                                .start();
                        return true;

                    default:
                        return false;
                }
            }
        });
        UtilitiesPreview.getInstance().DisegnaUltimiSpostamenti(context);
    }

    @Override
    protected void onResume() {
        super.onResume();

        UtilitiesPreview.getInstance().DisegnaUltimiSpostamenti(context);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        act.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();

                return true;
        }

        return false;
    }
}
